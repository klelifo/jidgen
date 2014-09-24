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


/**
 * A filter that uses regular expressions to filter commonly
 * forbidden patterns in ids.
 * 
 * @author unrza249
 * @author unrz205
 */
public class RegExFilter
	extends AbstractFilter
	implements 	IFilter
{
	/**
	 *  The class logger
	 */
	//private static final Log logger = LogFactory.getLog(RegExFilter.class);

	/**
	 * Default construct.
	 */
	public RegExFilter() {
	}
	
	public RegExFilter(String id) {
		super(id);
	}
	
	public RegExFilter(String id, String description) {
		super(id, description);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.rrze.idmone.utils.pwgen.IFilter#filter(int,
	 *      java.lang.String)
	 */
	public String apply(String id)	{
		
		// TODO implement something :)
		
/*
		if ((idFlags & REGEX_STARTS_NO_SMALL_LETTER_FLAG) != 0)
		{
			Matcher matcher = REGEX_STARTS_NO_SMALL_LETTER_P.matcher(id);
			if (matcher.find())
			{
				logger
						.debug(Messages
								.getString("RegExFilter.TRACE_ID") + id //$NON-NLS-1$
								+ Messages
										.getString("DefaultRegExFilter.TRACE_STARTS_SMALL")); //$NON-NLS-1$
				return null;
			}
		}
*/

		return id;
	}
}
