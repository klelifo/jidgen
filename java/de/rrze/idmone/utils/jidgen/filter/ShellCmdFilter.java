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

package de.rrze.idmone.utils.jidgen.filter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * A filter that calls the given shell command
 * with the id or subsequently with all ids, if there
 * are more than one, to be tested and
 * filters the id on exit code 0 (success).
 * Any exit code other than 0 will not filter the id.
 * 
 * @author unrza249
 */
public class ShellCmdFilter
extends AbstractFilter
implements 	IFilter
{
	/**
	 *  The class logger
	 */
	private static final Log logger = LogFactory.getLog(ShellCmdFilter.class);

	/**
	 * The command to run 
	 */
	private String cmdTemplate = "./filter.sh %s";

	/**
	 * Default construct.
	 */
	public ShellCmdFilter() {
	}

	public ShellCmdFilter(String id) {
		super(id);
	}

	public ShellCmdFilter(String id, String description) {
		super(id, description);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.rrze.idmone.utils.pwgen.IFilter#filter(int,
	 *      java.lang.String)
	 */
	public String apply(String id)	{
		String cmd = this.cmdTemplate.replace("%s", id);

		logger.trace("Executing command: " + cmd);
		
		Runtime run = Runtime.getRuntime();
		try {
			Process proc = run.exec(cmd);
			
	/*		BufferedWriter commandLine = new BufferedWriter(
					new OutputStreamWriter(proc.getOutputStream())
			);
			commandLine.write(cmd);
			commandLine.flush();
*/
			// read stdout and log it to the debug level
			BufferedReader stdOut = new BufferedReader(
					new InputStreamReader(proc.getInputStream())
			);
			String stdOutput = "";
			while( (stdOutput = stdOut.readLine()) != null) {
				logger.debug("STDOUT: " + stdOutput);
			}
			stdOut.close();
			

			// read stderr and log it to the error level
			BufferedReader stdErr = new BufferedReader(
					new InputStreamReader(proc.getErrorStream())
			);
			String errOutput = "";
			while( (errOutput = stdErr.readLine()) != null) {
				logger.error("STDERR: " + errOutput);
			}
			stdErr.close();

			
			int exitCode = proc.waitFor();
			proc.destroy();
			
			if (exitCode == 0) {
				logger.trace("Filtered!");
				return null;
			}
			else {
				return id;
			}
			
			

		}
		catch (IOException e) {
			logger.fatal(e.toString());
			System.exit(120);
		}
		catch (InterruptedException e) {
			logger.fatal(e.toString());
			System.exit(121);			
		}

		return null;
	}

	/**
	 * Get the command that is set to be executed.
	 * 
	 * @return the command to be executed 
	 */
	public String getCmd() {
		return cmdTemplate;
	}

	/**
	 * Set the command to be executed
	 * 
	 * @param cmd
	 * 			the command to be executed
	 */
	public void setCmd(String cmd) {
		this.cmdTemplate = cmd;
	}
}
