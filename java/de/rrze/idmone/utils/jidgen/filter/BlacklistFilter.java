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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.rrze.idmone.utils.jidgen.Messages;


/**
 * This class is used for filtering ids from a blacklist. If the proposed
 * id is contained within the blacklist, <em>null</em> is returned to
 * indicate the password is not suitable. Otherwise the password itself is
 * returned.
 * 
 * @author unrza249
 * @author unrz205
 */
public class BlacklistFilter 
	extends AbstractFilter
	implements IFilter
{
	/**
	 *  The class logger
	 */
	private static final Log logger = LogFactory.getLog(BlacklistFilter.class);

	/**
	 *  A list that stores the forbidden words
	 */
	private List<String> blacklist = new ArrayList<String>();
	
	
	/**
	 * Default constructor.
	 */
	public BlacklistFilter() {
	}

	public BlacklistFilter(String id) {
		super(id);
	}
	
	public BlacklistFilter(String id, String description) {
		super(id, description);
	}	
	
	public BlacklistFilter(String id, String description, List<String> blacklist) {
		super(id, description);
		this.setBlacklist(blacklist);
	}
	
	public BlacklistFilter(List<String> blacklist) {
		this.setBlacklist(blacklist);
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see de.rrze.idmone.utils.pwgen.IPassowrdFilter#filter(int,
	 *      java.lang.String)
	 */
	public String apply(String id) {
		// Iterate over the list and check whether it contains the word
		for (Iterator<String> iter = blacklist.iterator(); iter.hasNext();)	{
			String blackword = iter.next();

			// filter on match
			if (id.contains(blackword)) {
				logger.trace(Messages.getString("BlacklistFilter.TRACE_ID") 
						+ " \"" + this.getID() + "\" "
						+ Messages.getString("BlacklistFilter.TRACE_BLACKLIST_ENTRY") 
						+ " \"" + blackword
						+ "\"");
		
				return null;
			}
		}

		return id;
	}

	/**
	 * Returns a reference of the blacklist used by this filter and
	 * <em>null</em> if the filters is purely procedural and checks
	 * ids against rule.
	 * 
	 * @return the blacklist of the filter or <em>null</em> if one is not
	 *         used.
	 */
	public List<String> getBlacklist() {
		return this.blacklist;
	}

	/**
	 * Sets the blacklist of the filter.
	 * 
	 * @param blacklist
	 */
	public void setBlacklist(List<String> blacklist) {
		this.blacklist = blacklist;
	}

	/**
	 * Adds a password to the list of forbidden ids.
	 * 
	 * @param blackWord
	 *            the forbidden word
	 * @return <em>true</em> on successful inclusion, <em>false</me>
	 *         otherwise
	 */
	public boolean addToBlacklist(String blackWord) {
		if (blackWord == null || blackWord.isEmpty()) {
			return false;
		}
		else {
			logger.trace("Added blackword: \"" + blackWord + "\"");
			return this.blacklist.add(blackWord);
		}
	}

	/**
	 * Removes a word from the blacklist.
	 * 
	 * @param blackWord
	 *            the word to be removed from the blacklist
	 * @return <em>true</em> on successful removal, <em>false</em>
	 *         otherwise
	 */
	public boolean removeFromBlacklist(String blackWord) {
		return this.blacklist.remove(blackWord);
	}

}
