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
import java.util.Iterator;

import org.apache.commons.cli.HelpFormatter;

import de.rrze.idmone.utils.jidgen.Globals;
import de.rrze.idmone.utils.jidgen.Messages;
import de.rrze.idmone.utils.jidgen.template.Template;

/**
 * This class is an extension to the org.apache.commons.cli.HelpFormatter
 * class with some modifications to make it fit to the needs
 * jidgen.
 * 
 * @see <a href="http://commons.apache.org/cli/api-release/org/apache/commons/cli/HelpFormatter.html">http://commons.apache.org/cli/api-release/org/apache/commons/cli/HelpFormatter.html</a>
 * 
 * @author unrza249
 */
public class IdGenHelpFormatter 
extends HelpFormatter 
{
	/**
	 * The target length for the string left of the
	 * description. If it is shorter appropriate padding will be
	 * inserted to make it fit.
	 */
	private final int TARGET_LENGTH = 45;

	/**
	 * Default constructor with some basic
	 * initializations
	 */
	public IdGenHelpFormatter() {
		super();
		this.setNewLine(Globals.NEWLINE);
		this.setLeftPadding(3);
		this.setDescPadding(0);
		this.setWidth(Globals.TERM_WIDTH);
	}

	/**
	 * Renders one single option and applies the correct padding
	 * to the description text, so that all descriptions are
	 * perfectly aligned.
	 * 
	 * @param option
	 * 			option object to be rendered
	 * @return the rendered string for this options object
	 */
	protected String renderOption(IdGenOption option) {		
		// fix the broken padding function of the commons cli class
		// by calculating and setting our own padding
		// for each element
		int length = 0;
		length += option.getShortOpt().length() + 1;
		length += option.getLongOpt().length() + 3;
		length += option.hasArgName()?option.getArgName().length() + 3:0;
		this.setDescPadding(TARGET_LENGTH - length);

		// render the current option
		StringBuffer sb = new StringBuffer();
		//System.out.println("Processing: " + option.toString());
		IdGenOptions options = new IdGenOptions();
		options.addOption(option);
		super.renderOptions(sb, this.getWidth(), options, this.getLeftPadding(), this.getDescPadding());
		sb.append(this.getNewLine());

		return sb.toString();
	}

	/**
	 * Renders all options contained in an Options object
	 * by subsequently calling renderOption() with each option
	 * object.
	 * 
	 * @param options
	 * 			the options object to process
	 * @return the rendered string of all options
	 */
	protected String renderOptions(IdGenOptions options) {
		StringBuffer sb = new StringBuffer();
		Collection<IdGenOption> optionList = options.getOptions();
		Iterator<IdGenOption> iter = optionList.iterator();

		while (iter.hasNext()) {
			IdGenOption currentOption = iter.next();
			if (currentOption.isVisible()) {
				sb.append(this.renderOption(currentOption));
			}
		}

		return sb.toString();
	}

	/**
	 * Compile the fully formatted help message that should be 
	 * reachable via the --help option and returns it as a string.
	 * 
	 * @param options
	 * 			options that should be included in the help message
	 * @return the formatted help message as a string 
	 */
	public String getHelpString(IdGenOptions options, boolean longHelp) {
		StringBuffer sb = new StringBuffer();

		// usage string
		sb.append(Messages.getString("IdGenerator.HELP_USAGE"));	

		if (!longHelp) {
			sb.append(Globals.NEWLINE);
			sb.append(Messages.getString("IdGenerator.HELP_SHORT_EXAMPLE"));
			sb.append(Globals.NEWLINE);
		}
		
		if (longHelp) {
			sb.append(Globals.NEWLINE);
			sb.append(Messages.getString("IdGenerator.HELP_INTRO"));
		}

		if (longHelp) {
			// predefined data
			sb.append(Globals.NEWLINE);
			HashMap<String,String> presets = Template.getPredefinedData();
			if (!presets.isEmpty()) {
				sb.append(format(Messages.getString("IdGenereator.HELP_PREDEFINED_STRINGS")));
				for(Iterator<String> iter = presets.keySet().iterator(); iter.hasNext(); ) {
					String key = iter.next();
					sb.append(format(key + "\t" + Messages.getString("Template.HELP_DATA_PRESET_" + key.toUpperCase()) + " (" + presets.get(key)  + ")" + Messages.getString("IdGenerator.NEW_LINE")));		
				}
				sb.append(Globals.NEWLINE);
			}
		}


		if (longHelp) {
			// template syntax
			sb.append(Globals.NEWLINE);
			sb.append(format(Messages.getString("IdGenerator.HELP_TEMPLATE_SYNTAX")));
		}		


		// command line options
		sb.append(Globals.NEWLINE);
		sb.append(format(Messages.getString("IdGenerator.HELP_CLI_OPTIONS")));
		sb.append(this.renderOptions(options));


		if (longHelp) {
			// example string
			sb.append(Globals.NEWLINE);
			sb.append(Messages.getString("IdGenerator.HELP_LONG_EXAMPLE"));
		}

		return sb.toString();
	}

	/**
	 * Returns a string with correct left padding regardless
	 * of contained newline characters.
	 * 
	 * @param st
	 * 			the string that should be formatted
	 * @return	the formatted string
	 */
	public String format(String st) {
		String padding = createPadding(getLeftPadding());
		//padding = "----";
		String ret = padding + st;
		ret = ret.replaceAll(Messages.getString("IdGenerator.NEW_LINE"), Messages.getString("IdGenerator.NEW_LINE") + padding);
		if (st.endsWith("\n"))
			ret = ret.substring(0,ret.length() - padding.length());

		return ret;
	}
}
