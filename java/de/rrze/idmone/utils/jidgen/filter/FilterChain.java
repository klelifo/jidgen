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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.rrze.idmone.utils.jidgen.Messages;


/**
 * This is essentially a filter manager class which
 * includes an array of filters (the chain) all of which
 * implement the IFilter interface and a bunch of methods
 * to add/remove filter objects and apply the filter chain.
 *  
 * @author unrza249
 *
 */
public class FilterChain {

	/**
	 *  The class logger
	 */
	private static final Log logger = LogFactory.getLog(FilterChain.class);
	
	/**
	 * List of id filters to apply
	 */
	private HashMap<String, IFilter> chain = new HashMap<String, IFilter>();
	
	
	/**
	 * Default constructor
	 */
	public FilterChain() {
	}
	
	
	/**
	 * Adds a filter to the chain
	 * 
	 * @param filter
	 *            the filter instance to be registered
	 * @return the registered instance
	 */
	public IFilter addFilter(IFilter filter) {
		chain.put(filter.getID(), filter);
		logger.trace(Messages.getString("FilterChain.FILTER_ADDED") + filter.getType() + " - " + filter.getDescription());
		return filter;
	}

	
	/**
	 * Removes a filter from the chain by instance search
	 * 
	 * @param filter
	 *            the instance of the filter
	 * @return the removed instance
	 */
	public IFilter removeFilter(IFilter filter)	{
		return chain.remove(filter.getID());
	}

	
	/**
	 * Removes a filter from the chain by identifier search
	 * 
	 * @param id
	 *            the identifier of the filter
	 * @return the removed instance
	 */
	public IFilter removeFilter(String id) {
		return chain.remove(id);
	}

	
	/**
	 * This method does the actual filtering. It implements the main logic of
	 * the filter.
	 * 
	 * @param id
	 *            the id to be checked
	 * @return <em>null</em> if the id should be filtered and the
	 *         id if it satisfies the rules.
	 */
	public String apply(String id) {
		Set<String> filterKeys = chain.keySet();
	
		for (Iterator<String> iter = filterKeys.iterator(); iter.hasNext();) {
			IFilter filter = chain.get(iter.next());
			
			if (filter.apply(id) == null)
				return null;
		}
		return id;
	}
	
	
	/**
	 * This method checks a whole list of ids. It should return a list of
	 * suitable ids or an empty list if none of the ids fits the
	 * rules.
	 * 
	 * @param ids
	 *            a list of ids to be checked
	 * @return the list with filtered ids
	 */
	public List<String> apply(List<String> ids) {
		List<String> suitable = new ArrayList<String>();
		
		for (Iterator<String> iter = ids.iterator(); iter.hasNext();) {
			String element = iter.next();
			
			// add to suitable array if not filtered
			if (apply(element) != null)
				suitable.add(element);
		}
		return suitable;
	}
	
	/**
	 * Clears the filter chain
	 */
	public void clear() {
		this.chain.clear();
	}
	
}
