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

package de.rrze.idmone.utils.jidgen.cli;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.rrze.idmone.utils.jidgen.Messages;

/**
 * An options manager class for jidgen extending the 
 * org.apache.commons.cli.Options class.
 * 
 * @see <a href="http://commons.apache.org/cli/api-release/org/apache/commons/cli/Options.html">http://commons.apache.org/cli/api-release/org/apache/commons/cli/Options.html</a>
 * 
 * @author unrza249
 */
public class IdGenOptions 
	extends Options
{
	/**
	 *  The class logger
	 */
	private static final Log logger = LogFactory.getLog(IdGenOptions.class);

	/**
	 * The internal data array for storing
	 * all parsed CLI options with their
	 * arguments.
	 */
	private HashMap<String,String> data = new HashMap<String,String>();
	
	/**
	 * List of dummy options that are displayed but not 
	 * processed by the parser.
	 */
	private HashMap<String,IdGenOption> dummyOptions = new HashMap<String,IdGenOption>();

	/**
	 * A rather complex function with the possibility to set most
	 * of the available configuration options at once.<br />
	 * This also handles the special dummy options.
	 * 
	 * @param shortOption
	 *            	a one letter flag
	 * @param longOption
	 *            	long flag
	 * @param description
	 *            	the description of the cCLI option
	 * @param numArgs
	 *            	specifies whether the option has arguments and how many
	 * @param argName
	 * 				sets the argument name to be displayed
	 * @param valueSeparator
	 * 				sets the value separator
	 * @param required
	 *            	specifies whether the option is required
	 * @param visible
	 * 				specifies whether the option should be displayed in the help string
	 * @param dummy
	 * 				specifies whether the option should be interpreted by the parser<br/>
	 * 				This is useful for creating options only for output in the help string
	 * 				that describe one or more hidden options in one.
	 */
	protected IdGenOption add(String shortOption, String longOption,
			String description, int numArgs, String argName, char valueSeparator, 
			boolean required, boolean visible, boolean dummy) {


		IdGenOption option;

		if (dummy) {
			option = new IdGenOption("dummy_" + this.dummyOptions.size(), longOption, description, numArgs, argName, valueSeparator, required);
		}
		else {
			option = new IdGenOption(shortOption, longOption, description, numArgs, argName, valueSeparator, required);
		}

		option.setVisible(visible);
		option.setDummy(dummy);

		if (dummy) {			
			option.setShortOpt(shortOption);
		}

		//super.addOption(option);
		this.addOption(option);

		return option;
	}

	/**
	 * A rather complex function with the possibility to set most
	 * of the available configuration options at once.<br />
	 * This just adds a regular option.<br />
	 * The option is not required by default.
	 * 
	 * @param shortOption
	 *            	a one letter flag
	 * @param longOption
	 *            	long flag
	 * @param description
	 *            	the description of the cCLI option
	 * @param numArgs
	 *            	specifies whether the option has arguments and how many
	 * @param argName
	 * 				sets the argument name to be displayed
	 * @param valueSeparator
	 * 				sets the value separator
	 */
	public IdGenOption add(String shortOption, String longOption,
			String description, int numArgs, String argName, char valueSeparator) {
		return this.add(shortOption, longOption, description, numArgs, argName, valueSeparator, false, true, false);		
	}

	/**
	 * A simple function for adding just a regular option
	 * without argument.
	 * The option is not required by default.
	 * 
	 * @param shortOption
	 *            	a one letter flag
	 * @param longOption
	 *            	long flag
	 * @param description
	 *            	the description of the CLI option
	 */
	public IdGenOption add(String shortOption, String longOption,
			String description) {
		return this.add(shortOption, longOption, description, 0, "", ' ', false, true, false);		
	}

	
	/**
	 * A rather complex function with the possibility to set most
	 * of the available configuration options at once.<br />
	 * This adds a hidden option that will not be displayed in the help
	 * string but still be parsed when specified.<br />
	 * The option is not required by default.
	 * 
	 * @param shortOption
	 *            	a one letter flag
	 * @param longOption
	 *            	long flag
	 * @param numArgs
	 *            	specifies whether the option has arguments and how many
	 * @param argName
	 * 				sets the argument name to be displayed
	 * @param valueSeparator
	 * 				sets the value separator
	 */
	public IdGenOption addInvisible(String shortOption, String longOption,
			int numArgs, String argName, char valueSeparator) {
		return this.add(shortOption, longOption, "", numArgs, argName, valueSeparator, false, false, false);
	}

	/**
	 * A rather complex function with the possibility to set most
	 * of the available configuration options at once.<br />
	 * This adds a dummy option that will be displayed in the help string
	 * but is <b>not</b> processed by the parser.<br />
	 * This is useful for creating options only for output in the help string
	 * that describe one or more hidden options in one. 
	 * 
	 * @param shortOption
	 *            	a one letter flag
	 * @param longOption
	 *            	long flag
	 * @param description
	 *            	the description of the cCLI option
	 * @param numArgs
	 *            	specifies whether the option has arguments and how many
	 * @param argName
	 * 				sets the argument name to be displayed
	 */
	public IdGenOption addDummy(String shortOption, String longOption,
			String description, int numArgs, String argName) {
		return this.add(shortOption, longOption, description, numArgs, argName, ' ', false, true, true);
	}


	/**
	 * Adds a single option object to this options object
	 * 
	 * @param option
	 * 			the option to be added
	 * @return the added option
	 */
	public IdGenOption addOption(IdGenOption option) {
		super.addOption(option);

		if (option.isDummy()) {
			this.dummyOptions.put(option.getShortOpt(), option);
		}
		return option;
	}

	/**
	 * builds a formatted help string for the IdGenerator
	 * 
	 * @return the formatted help string, ready for output
	 */
	public String getHelp(boolean longHelp) {
		IdGenHelpFormatter formatter = new IdGenHelpFormatter();
		return formatter.getHelpString(this, longHelp);
	}

	/**
	 * returns all stored options
	 * 
	 * @return a collection of all stored options
	 */
	public Collection<IdGenOption> getOptions() {
		/*
		Collection<IdGenOption> optionList = new HashSet<IdGenOption>();
		//optionList.addAll(this.dummyOptions);
		optionList.addAll(super.getOptions());
		return optionList;
		 */

		return (Collection<IdGenOption>)super.getOptions();
	}

	
	/**
	 * Fill the internal variable data by parsing a given
	 * array of command line options.
	 * 
	 * @param args
	 * 			the String array containing all command line options
	 * @return the data collection
	 * @throws ParseException
	 */
	public HashMap<String,String> parse(String[] args) 
		throws ParseException
	{
		// get a list of all stored option objects to be processed
		// excluding all dummy options
		Collection<IdGenOption> options = new HashSet<IdGenOption>();
		options.addAll(this.getOptions());
		options.removeAll(this.dummyOptions.values());
		Iterator<IdGenOption> iter = options.iterator();

		// init the parser
		BasicParser parser = new BasicParser();
		CommandLine commandLine = parser.parse(this, args);
		
		// iterate over all possible options
		while (iter.hasNext()) {
			IdGenOption currentOption = iter.next();
			//logger.trace("Processing option \"" + currentOption.getShortOpt() + "\"");

			if (commandLine.hasOption(currentOption.getShortOpt()))	{
				// option was specified
				String value = commandLine.getOptionValue(currentOption.getShortOpt());
				if (value != null) {
					// option has a specified value
					this.data.put(currentOption.getShortOpt(), value);
					logger.info(currentOption.getShortOpt() + " = " + value);
				}
				else if (currentOption.hasArg()){
					// option does NOT have a specified value
					logger.error(currentOption.getShortOpt() + " " + Messages.getString("IdGenOptions.MISSING_ARGUMENT"));
					System.out.println(this.getHelp(false));
					System.exit(170);
				}
				else {
					// at least put an entry with an empty string in the data array
					// to mark that the option was specified
					this.data.put(currentOption.getShortOpt(), "");
				}
			}
			else {
				// option was NOT specified, so use default if available
				if (currentOption.hasDefaultValue()) {
					// has default
					logger.info(currentOption.getShortOpt() + " " + Messages.getString("IdGenOptions.USING_DEFAULT") + " " + currentOption.getDefaultValue());
					this.data.put(currentOption.getShortOpt(), currentOption.getDefaultValue());
				}
			}
		}

		return this.data;
	}

	
	/**
	 * Sets an option value using the specified shortOpt and
	 * value string.
	 * If the value for this options was already set it will
	 * be overwritten without warning.
	 * 
	 * @param shortOpt
	 * 			the short option string to set a value for
	 * @param value
	 * 			the value to set
	 */
	public void setOptionValue(String shortOpt, String value) {
		this.data.put(shortOpt, value);
		logger.info(shortOpt + " = " + value);
	}
	
	
	/**
	 * returns the value of the requested option, if possible
	 * 
	 * @param shortOpt
	 * 			the option for which the value is requested
	 * @return the requested option's value
	 */
	public String getOptionValue(String shortOpt) {
		if (this.hasData()) {
			if (this.data.containsKey(shortOpt)) {
				return this.data.get(shortOpt);
			}
			else {
				logger.debug(Messages.getString("IdGenOptions.NO_DATA_ENTRY") + " " + shortOpt + ".\n");
				return null;
			}
		}
		else {
			logger.warn(Messages.getString("IdGenOptions.DATA_NOT_INITIALIZED"));
			return null;
		}
	}
	
	/**
	 * Checks if the requested short option was specified
	 * in the arguments string.
	 *  
	 * @param shortOpt
	 * 			short option name to check for
	 * @return true if the option was specified, false otherwise
	 */
	public boolean hasOptionValue(String shortOpt) {
		return (this.data.containsKey(shortOpt));
	}
	
	/**
	 * This is a option checking function inherited from
	 * the super class.
	 * It does not work with the added functionality of this
	 * class, so <b>do not use it</b>.
	 * You most likely want what the hasOptionValue() method
	 * does.
	 */
	public boolean hasOption(String shortOpt) {
		return super.hasOption(shortOpt);
	}
	
	/**
	 * returns the stored option data
	 * 
	 * @return stored option data
	 */
	public HashMap<String,String> getData() {
		if (this.hasData()) {
			return this.data;
		}
		else {
			logger.error(Messages.getString("IdGenOptions.DATA_NOT_INITIALIZED"));
			return null;			
		}
	}
	
	/**
	 * get state of the option data array
	 * 
	 * @return true if the array has been initialized, false otherwise
	 */
	public boolean hasData() {
		return (this.data != null);
	}
	
	/**
	 * returns a string representation of the stored options, 
	 * e.g. the help string
	 * 
	 * @return the (long) help string
	 */
	public String toString() {
		return this.getHelp(true);
	}
	
	public int getNum() {
		return this.data.size();
	}
}
