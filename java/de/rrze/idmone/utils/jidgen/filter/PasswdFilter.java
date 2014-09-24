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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.rrze.idmone.utils.jidgen.File;
import de.rrze.idmone.utils.jidgen.Messages;


/**
 * A filter for ids that are already in use 
 * within the system's passwd file. 
 * 
 * @author unrza249
 */
public class PasswdFilter 
extends AbstractFilter
implements 	IFilter
{
	/**
	 *  The class logger
	 */
	private static final Log logger = LogFactory.getLog(PasswdFilter.class);

	/**
	 * The location of the passwd file.
	 * This should usually be /etc/passwd, at least for
	 * the local system
	 */
	private String fileLocation = "/etc/passwd";


	/**
	 * Default constructor
	 */
	public PasswdFilter() {
	}

	public PasswdFilter(String id) {
		super(id);
	}

	public PasswdFilter(String id, String description) {
		super(id, description);
	}



	/**
	 * Sets the location of the passwd file.
	 * Defaults to /etc/passwd
	 * 
	 * @param fileLocation
	 * 			location of the passwd file
	 */
	public void setFile(String fileLocation) {
		this.fileLocation = fileLocation;
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see de.rrze.idmone.utils.pwgen.IFilter#filter(int,
	 *      java.lang.String)
	 */
	public String apply(String id)	{
		// get a reader for the passwd file
		File file = new File(this.fileLocation);

		String line;
		while ((line = file.getLine()) != null) {
			String userID = line.substring(0, line.indexOf(':'));

			if (id.equals(userID)) {
				logger.trace(Messages.getString("PasswdFilter.TRACE_ID") 
						+ " \"" + this.getID() + "\" "
						+ Messages.getString("PasswdFilter.TRACE_SKIPPED_ENTRY") 
						+ " \"" + userID 
						+ "\"");

				return null;
			}
		}	
		file.close();

		return id;
	}
}
