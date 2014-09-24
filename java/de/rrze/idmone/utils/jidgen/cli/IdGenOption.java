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

import org.apache.commons.cli.Option;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.rrze.idmone.utils.jidgen.Messages;

/**
 * This class is an extension to the org.apache.commons.cli.Option
 * class with some modifications to make it fit to the needs
 * jidgen.
 * 
 * @see <a href="http://commons.apache.org/cli/api-release/org/apache/commons/cli/Option.html">http://commons.apache.org/cli/api-release/org/apache/commons/cli/Option.html</a>
 * 
 * @author unrza249
 */
public class IdGenOption 
	extends Option 
{
	/**
	 *  The class logger
	 */
	private static final Log logger = LogFactory.getLog(IdGenOption.class);
	
	private boolean visible = true;
	
	private boolean dummy = false;

	private String shortOpt = "";
	
	private String defaultValue;
	
	/**
	 * A rather complex constructor with the possibility to set most
	 * of the available configuration options at once
	 * 
	 * @param shortOption
	 *            	a one letter flag
	 * @param longOption
	 *            	long flag
	 * @param description
	 *            	the description of the CLI option
	 * @param numArgs
	 *            	specifies whether the option has arguments and how many
	 * @param argName
	 * 				sets the argument name to be displayed
	 * @param valueSeparator
	 * 				sets the value separator
	 * @param required
	 *            	specifies whether the option is required
	 */
	public IdGenOption(String shortOption, String longOption,
			String description, int numArgs, String argName, char valueSeparator, 
			boolean required) { 
		super(shortOption, description);
		this.setLongOpt(longOption);

		this.setArgs(numArgs);
		this.setArgName(argName);
		this.setValueSeparator(valueSeparator);

		this.setRequired(required);
	}
	
	public void setDummy(boolean dummy) {
		this.dummy = dummy;
	}
	
	public boolean isDummy() {
		return this.dummy;
	}
	
	public void setVisible(boolean visible) {
		this.visible = visible;
	}
	
	public boolean isVisible() {
		return this.visible;
	}
	
	public void setShortOpt(String shortOpt) {
		if (this.isDummy()) {
			this.shortOpt = shortOpt;
		}
		else {
			logger.warn(Messages.getString("IdGenOption.NO_SET_SHORT_OPT"));
		}
	}
	
	public String getShortOpt() {
		if (this.isDummy()) {
			return this.shortOpt;
		}
		else {
			return super.getOpt();
		}
	}
	
	public String getOpt() {
		return this.getShortOpt();
	}

	public boolean hasDefaultValue() {
		return (this.defaultValue != null);
	}
	
	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}
	
	public String getDefaultValue() {
		if (this.hasDefaultValue()) {
			return this.defaultValue;
		}
		else {
			logger.error(this.getShortOpt() + " " + Messages.getString("IdGenOption.NO_DEFAULT_VALUE"));
			return null;
		}
	}

}
