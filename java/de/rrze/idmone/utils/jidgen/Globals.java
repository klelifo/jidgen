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

/**
 * This is just a convenience class to have a central
 * point for specifying global program options and defaults.
 * 
 * @author unrza249
 * @author unrz205
 * 
 */
public class Globals
{
	/**
	 * The newline character to use
	 */
	public static String NEWLINE = Messages.getString("IdGenerator.NEW_LINE");
	
	
	/**
	 * This is meant to be the emergency exit if the
	 * id generation loop does not exit.
	 * If this happens the loop is broken after MAX_ATTEMPTS
	 * loops and a proper error message is displayed.
	 */
	public static final int MAX_ATTEMPTS = 10000;
	
	/**
	 * Number of id proposals to be outputted after one invokation of jidgen. 
	 */
	public static final int DEFAULT_NUM_IDs = 1;
	public static int NUM_IDs = DEFAULT_NUM_IDs; 
	
	
	/**
	 * Terminal width in characters
	 */
	public static final int DEFAULT_TERM_WIDTH = 80;
	public static int TERM_WIDTH = DEFAULT_TERM_WIDTH;
	
	/**
	 * Default blacklist file
	 */
	//public static final String DEFAULT_BLACKLIST_FILE = "src/main/config/blacklist";
	public static final String DEFAULT_BLACKLIST_FILE = "blacklist";
	public static String BLACKLIST_FILE = DEFAULT_BLACKLIST_FILE;
	
	/**
	 * Default passwd file
	 */
	public static final String DEFAULT_PASSWD_FILE = "/etc/passwd";
	public static String PASSWD_FILE = DEFAULT_PASSWD_FILE;
	
	
	/**
	 * Default shell command
	 */
	public static final String DEFAULT_SHELLCMD = "./filter.sh %s";
	public static String SHELLCMD = DEFAULT_SHELLCMD;
	
	/**
	 * Default shell
	 */
	
	
	/**
	 * Enables output in columns
	 */
	public static final boolean DEFAULT_ENABLE_COLUMN_OUTPUT = false;
	public static boolean ENABLE_COLUMN_OUTPUT = DEFAULT_ENABLE_COLUMN_OUTPUT;
	
	/**
	 * Special characters that can be included.
	 */
	//public static final String SPECIAL_SYMBOLS = "!\"#$%&'()*+,-./:;<=>?@[\\]^_`{|}~";


	/**
	 * List of ambiguous characters that can look alike and can confuse users.
	 */
	//public static final String AMBIGUOUS_SYMBOLS = "B8G6I1l0OQDS5Z2";
	
}
