/*
 * jidgen, developed as a part of the IDMOne project at RRZE.
 * Copyright 2008, RRZE, and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors. This
 * product includes software developed by the Apache Software Foundation
 * http://www.apache.org/
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package de.rrze.idmone.utils.jidgen;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.cli.ParseException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.rrze.idmone.utils.jidgen.cli.IdGenOptions;
import de.rrze.idmone.utils.jidgen.filter.BlacklistFilter;
import de.rrze.idmone.utils.jidgen.filter.FilterChain;
import de.rrze.idmone.utils.jidgen.filter.PasswdFilter;
import de.rrze.idmone.utils.jidgen.filter.ShellCmdFilter;
import de.rrze.idmone.utils.jidgen.template.Template;

/**
 * class IdGenerator
 * 
 * 
 * 
 * 
 * <p>
 * 		<b>Used external packages</b>
 * 		<br />
 * 		For further infos about the cli package see <a href="http://commons.apache.org/cli/">
 * 		org.apache.commons.cli</a>.
 * 		<br />
 * 		For further infos about the logging package see 
 * 		<a href="http://commons.apache.org/logging/commons-logging-1.1/index.html">
 * 		org.apache.commons.logging</a>
 * </p>
 *  
 * @author unrza249
 * @author unrz205
 */
public class IdGenerator
{
	/**
	 *  The class logger
	 */
	private static final Log logger = LogFactory.getLog(IdGenerator.class);

	/**
	 * The options manager for IdGen 
	 */
	private IdGenOptions options;

	/**
	 * The option string before parsing
	 */
	private String[] cliArgs;

	/**
	 * Flag that indicates whether or not an update of the
	 * options object is needed.
	 * <b>Only used internally!</b>
	 */
	private boolean updateOptions = true;

	/**
	 * A filter chain for the IdGenerator<br />
	 * This chain contains all filters that should be applied to
	 * the generated ids.
	 */
	private FilterChain filterChain; 

	/**
	 * Default constructor of the IdGenerator
	 */
	public IdGenerator() {
		logger.trace("Invoked default constructor.");
		/*		
 		// uncomment this for using an internally pre-defined configuration
 		// together with the log4j logging system 
 		try {
			// configure the log4j logging system from a external configuration file
			PropertyConfigurator.configure(this.getClass().getResource("/log4j.properties"));
		}
		catch (Exception e) {
			// if that doesn't work use this default configuration
			Properties props = new Properties();
			props.put("log4j.rootLogger", "WARN, A1");
			props.put("log4j.appender.A1", "org.apache.log4j.ConsoleAppender");
			props.put("log4j.appender.A1.layout", "org.apache.log4j.PatternLayout");
			props.put("log4j.appender.A1.layout.ConversionPattern", "%-4r [%t] %-5p %c %x - %m%n");
			PropertyConfigurator.configure(props);
		}
		 */	

		// create options definition for CLI/external library usage
		this.options = buildOptions();

		// create an empty filter chain
		this.filterChain = new FilterChain();
	}


	public IdGenerator(String[] args) {
		this();
		this.setCLIArgs(args);
		this.init();
	}


	public IdGenerator(String args) {
		this();
		this.setCLIArgs(args);
		this.init();
	}

	/**
	 * Entry point of the program (CLI)
	 * 
	 * @param args
	 *            the program arguments
	 */
	public static void main(String[] args) {
		logger.info(Messages.getString("IdGenerator.WELCOME"));

		// create an instance of the IdGenerator and start the generation process
		IdGenerator generator = new IdGenerator();

		// pass on the CLI options array
		generator.setCLIArgs(args);

		// initialize the generator options manually
		// so we can print the usage on error
		if (!generator.init()) {
			generator.printUsage();
			System.exit(150);
		}

		/*
		 * PROCESS CLI ONLY OPTIONS
		 */

		// check for -h option or call with no options at all
		if (generator.options.hasOptionValue("h") || generator.options.getNum() == 0) {
			generator.printUsage();
			System.exit(0);
		}

		// check for -hh option
		if (generator.options.hasOptionValue("hh")) {
			generator.printHelp();
			System.exit(0);
		}

		// set number of ids
		if (generator.options.hasOptionValue("N")) {
			Globals.NUM_IDs = Integer.parseInt(generator.options.getOptionValue("N"));
			logger.trace("Set number of ids to generate to " + Globals.NUM_IDs + ".");
		}

		// enable column output
		if (generator.options.hasOptionValue("C")) {
			Globals.ENABLE_COLUMN_OUTPUT = true;
			logger.trace("Enable column output...");
		}

		// set terminal width
		if (generator.options.hasOptionValue("W")) {
			Globals.TERM_WIDTH = Integer.parseInt(generator.options.getOptionValue("W"));
			logger.trace("Set terminal width to " + Globals.TERM_WIDTH + ".");
		}


		/*
		 * START WORKING
		 */

		// generate ids
		List<String> ids = generator.generateIDs(Globals.NUM_IDs);

		// output the generated ids
		if (ids != null && !ids.isEmpty()) {
			logger.info(Messages.getString("IdGenerator.ID"));
			if (Globals.ENABLE_COLUMN_OUTPUT) {
				generator.printColumns(ids);
			}
			else {
				generator.print(ids);
			}
		}
	}

	/**
	 * Init the IdGenerator object<br />
	 * This got a seperate method, so that it can
	 * be called more flexibly and also from the main
	 * method for CLI usage.
	 * 
	 * @return true if no errors occurred, false otherwise 
	 */
	private boolean init() {
		logger.trace("Init called.");
		// at first: update the options data if needed
		if (this.updateOptions) {
			this.updateOptions = false;

			if (!this.parseOptions(this.cliArgs)) {
				logger.error(Messages.getString("IdGenerator.ERROR_OPTIONS_UPDATE") + " " + Arrays.toString(this.cliArgs));
				return false;
			}
		}

		logger.trace("Processing CLI arguments...");

		/*
		 * FLAGS
		 */


		/*
		 * DATA
		 */


		/*
		 * FILTERS
		 */
		// blacklist filter
		if (this.options.hasOptionValue("B")) {
			logger.trace("Enable blacklist filter...");
			BlacklistFilter bl = new BlacklistFilter();

			if (this.options.hasOptionValue("Bf")) {
				Globals.BLACKLIST_FILE = this.options.getOptionValue("Bf");
				logger.trace("Using ALTERNATE blacklist file (" + Globals.BLACKLIST_FILE + ").");
			}
			else {
				logger.trace("Using DEFAULT blacklist file (" + Globals.BLACKLIST_FILE + ").");
			}

			// read blacklist from file
			File file = new File(Globals.BLACKLIST_FILE);
			while(bl.addToBlacklist(file.getLine())) {
				// just loop
			}
			file.close();

			this.filterChain.addFilter(bl);
		}

		// passwd filter
		if (this.options.hasOptionValue("P")) {
			logger.trace("Enable passwd filter...");
			PasswdFilter passwd = new PasswdFilter();

			if (this.options.hasOptionValue("Pf")) {
				Globals.PASSWD_FILE = this.options.getOptionValue("Pf");
				logger.trace("Using ALTERNATE passwd file (" + Globals.PASSWD_FILE + ").");
			}
			else {
				logger.trace("Using DEFAULT passwd file (" + Globals.PASSWD_FILE + ").");
			}

			passwd.setFile(Globals.PASSWD_FILE);

			this.filterChain.addFilter(passwd);
		}

		// shellcmd filter
		if (this.options.hasOptionValue("S")) {
			logger.trace("Enable shellcmd filter...");
			ShellCmdFilter shellCmd = new ShellCmdFilter();
			
			if (this.options.hasOptionValue("Sf")) {
				Globals.SHELLCMD = this.options.getOptionValue("Sf");
				logger.trace("Using shell command (" + Globals.SHELLCMD + ").");
			}
			else {
				logger.trace("Using DEFAULT shell command (" + Globals.SHELLCMD + ").");
			}
			
			shellCmd.setCmd(Globals.SHELLCMD);
			
			this.filterChain.addFilter(shellCmd);	
		}
		
		
		
		return true;
	}

	/**
	 * Converts the given string into an array
	 * and calls parseOptions(String[]).
	 * 
	 * @param args
	 * 			argument string
	 * @return	true on success, false otherwise
	 */
	private boolean parseOptions(String args) {
		String[] argsArr = args.split(" ");
		return this.parseOptions(argsArr);
	}

	/**
	 * Fills the data array inside the options object
	 * with the arguments specified in the array.
	 *  
	 * @param args
	 * 			the argument array
	 * @return true on success, false otherwise
	 */
	private boolean parseOptions(String[] args) {
		// parse the command line options and
		// fill the data array
		try {
			logger.trace("Parsing cliArgs...");
			this.options.parse(args);
		} 
		catch (ParseException e) {
			logger.debug(e.toString());
			return false;
		} 
		catch (NumberFormatException e)	{
			logger.debug(e.toString());
			return false;
		}

		return true;
	}

	/**
	 * Converts the given string into an array
	 * and calls setCLIArgs(String[]).
	 * 
	 * @param args
	 * 			the argument string
	 */
	public void setCLIArgs(String args) {
		logger.trace("Converting argument string to array...");
		String[] argsArr = args.split(" ");
		this.setCLIArgs(argsArr);
	}

	/**
	 * Sets the stored argument array for
	 * later parsing.<br />
	 * An update of the data array can be 
	 * done explicitly by calling the
	 * updateOptions() method or automatically
	 * on the next call to generateIds(int). 
	 * 
	 * @param args
	 * 			the new argument array
	 */
	public void setCLIArgs(String[] args) {
		this.updateOptions = true;
		this.cliArgs = args;
		logger.trace("Set cliArgs to " + Arrays.toString(this.cliArgs));
	}

	/**
	 * Updates everything that needs to be updated
	 * to be up-to-date again.
	 * <em>This also includes re-reading the blacklist
	 * file.</em>
	 * 
	 * @return true on success, false otherwise
	 */
	public boolean update() {
		this.filterChain.clear();
		return this.init();
	}

	/**
	 * This method tries to generate the given number of ids. 
	 * The method returns an empty list if it does 
	 * not manage to create any suitable id within the <em>MAX_ATTEMPTS</em>
	 * or null if an error occurs.
	 * 
	 * @param num
	 * 			target number of ids to generate
	 * @return a suitable id list, an empty list if such could not be
	 *         generated or null on error
	 */
	public List<String> generateIDs(int num) {
		if (this.updateOptions) {
			this.update();
		}

		ArrayList<String> ids = new ArrayList<String>();

		logger.info(Messages.getString("IdGenerator.START_GENERATION") + num);

		Template template = new Template(this.options.getData());

		int i = 0;
		while (template.hasAlternatives() && (ids.size() < num)) {
			if (i++ == Globals.MAX_ATTEMPTS) {
				logger.fatal(Messages.getString("IdGenerator.MAX_ATTEMPTS_REACHED") + " (" + Globals.MAX_ATTEMPTS + ")");
				System.exit(152);
			}
			String idCandidate = null;
			idCandidate = template.buildString();
			logger.trace(Messages.getString("IdGenerator.TRACE_ID_CANDIDATE") + " " + idCandidate);

			// apply the filter chain to the generated id
			// add to list if we got a valid, unique id 
			if (	(this.filterChain.apply(idCandidate) != null)
					&& (!ids.contains(idCandidate)))
			{
				ids.add(idCandidate);
			}
			else { 
				// log some info about the failed attempt 
				logger.trace(Messages.getString("IdGenerator.TRACE_ATTEMPT_GENERATE") + " " + idCandidate);
			}
		}

		logger.debug(Messages.getString("IdGenerator.NUMBER_OF_ITERATIONS") + i);

		if (ids.size() < num) {
			logger.warn(Messages.getString("IdGenerator.FAILED_TO_REACH_TARGET_NUM") + ids.size());
		}

		if (ids.size() == 0) {
			logger.fatal(Messages.getString("IdGenerator.NO_ALTERNATIVES_LEFT"));
		}

		return ids;
	}

	/**
	 * Prints ids into columns with a predefined terminal width(to
	 * System.out). The number of columns is calculated from the terminal width.
	 * 
	 * @param ids
	 *            a list of ids to be printed
	 */
	public void printColumns(List<String> ids)
	{
		int idLength = ids.get(0).length();
		int numberOfColumns = Globals.TERM_WIDTH / (idLength + 1);
		if (numberOfColumns == 0)
			numberOfColumns = 1;

		logger.debug(Messages.getString("IdGenerator.N_SEPARATOR"));

		int i;
		int column = 0;
		for (i = 0; i < ids.size(); i++)
		{
			column++;
			String id = (String) ids.get(i);
			if (((column % numberOfColumns) == 0))
			{
				System.out.print(id + Messages.getString("IdGenerator.NEW_LINE"));
			} else
			{
				System.out.print(id + ' ');
			}
		}
		if ((column % numberOfColumns) != 0) {
			System.out.print(Messages.getString("IdGenerator.NEW_LINE"));
		}
		logger.debug(Messages.getString("IdGenerator.N_SEPARATOR"));
	}

	/**
	 * Prints a list of ids to a terminal(System.out)
	 * 
	 * @param ids
	 *            a list of ids to be printed
	 */
	public void print(List<String> ids)
	{
		logger.debug(Messages.getString("IdGenerator.N_SEPARATOR"));

		for (int i = 0; i < ids.size(); i++)
		{
			String id = (String) ids.get(i);
			System.out.print(id	+ Messages.getString("IdGenerator.NEW_LINE"));
		}
		logger.debug(Messages.getString("IdGenerator.N_SEPARATOR"));
	}

	/**
	 * Prints the usage info
	 * and the available CLI options
	 */
	public void printUsage() {
		System.out.println(options.getHelp(false));
	}

	/**
	 * Prints a very verbose help page
	 * with more detailed info
	 */
	public void printHelp() {
		System.out.println(options.getHelp(true));		
	}

//	TODO find are more elegant way to specify options globally
	/**
	 * Initializes the CLI (Command Line Interface) options of the IdGenerator.
	 * 
	 * @return the CLI options
	 */
	private IdGenOptions buildOptions()	{
		IdGenOptions opts = new IdGenOptions();

		logger.trace("Building CLI options...");

		// number of ids
		opts.add(
				"W",
				"terminal-width",
				Messages.getString("IIdGenCommandLineOptions.CL_TERMINAL_WIDTH_DESC") + " (Default: " + Globals.DEFAULT_TERM_WIDTH + ")",
				1,
				"number",
				' '
		);

		// number of ids
		opts.add(
				"N",
				"number-ids",
				Messages.getString("IIdGenCommandLineOptions.CL_NUMBER_IDS_DESC"),
				1,
				"number",
				' '
		);

		// print in columns flag
		opts.add(
				"C",
				"print-in-columns",
				Messages.getString("IIdGenCommandLineOptions.CL_PRINT_IN_COLUMNS_DESC")
		);

		// shellcmd filter command
		opts.add(
				"Sf",
				"shellcmd-command",
				Messages.getString("IIdGenCommandLineOptions.CL_SHELLCMD_COMMAND_DESC"),
				1,
				"command",
				' '
		);

		// shellcmd filter
		opts.add(
				"S",
				"enable-shellcmd-filter",
				Messages.getString("IIdGenCommandLineOptions.CL_SHELLCMD_DESC") + " (Default: " + Globals.DEFAULT_SHELLCMD + ")"
		);		
		
		// passwd filter file
		opts.add(
				"Pf",
				"passwd-file",
				Messages.getString("IIdGenCommandLineOptions.CL_PASSWD_FILE_DESC"),
				1,
				"file",
				' '
		);

		// passwd filter
		opts.add(
				"P",
				"enable-passwd-filter",
				Messages.getString("IIdGenCommandLineOptions.CL_PASSWD_DESC") + " (Default: " + Globals.DEFAULT_PASSWD_FILE + ")"
		);

		// blacklist filter file
		opts.add(
				"Bf",
				"blacklist-file",
				Messages.getString("IIdGenCommandLineOptions.CL_BLACKLIST_FILE_DESC"),
				1,
				"file",
				' '
		);

		// blacklist filter
		opts.add(
				"B",
				"enable-blacklist-filter",
				Messages.getString("IIdGenCommandLineOptions.CL_BLACKLIST_DESC") + " (Default: " + Globals.DEFAULT_BLACKLIST_FILE + ")"
		);

		// create all "T[a-z]" options as invisible and a dummy option for them
		for (char currentChar = 'a'; currentChar < 'z'; currentChar++) {
			opts.addInvisible(
					"T" + currentChar,
					"template-variable-" + currentChar, 
					1,
					"data",
					' '
			);
		}
		opts.addDummy(
				"T[a-z]",
				"template-variable-[a-z]",
				Messages.getString("IIdGenCommandLineOptions.CL_TEMPLATE_VARIABLE_DESC"),
				1,
				"data"
		);

		// id template string
		opts.add(
				"T",
				"template", 
				Messages.getString("IIdGenCommandLineOptions.CL_TEMPLATE_DESC"), 
				1,
				"template",
				' '
		);

		// display usage (short help)
		opts.add(
				"h",
				"help", 
				Messages.getString("IIdGenCommandLineOptions.CL_HELP")
		);

		// display help page (long help)
		opts.add(
				"hh",
				"help-page", 
				Messages.getString("IIdGenCommandLineOptions.CL_HELP_PAGE")
		);

		return opts;
	}

	/**
	 * Set an option with it's value
	 * 
	 * @param opt
	 * 			the short option parameter to be set
	 * @param value
	 * 			the value to be associated with the parameter
	 */
	public void setOption(String opt, String value) {
		updateOptions = false;
		this.options.setOptionValue(opt, value);
	}
}