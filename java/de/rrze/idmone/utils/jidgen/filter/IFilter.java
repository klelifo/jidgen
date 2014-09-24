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

import java.util.List;

/**
 * Interface representing the basic functionality that should be supported by
 * an id filter class. Implementations of this class can be registered
 * for usage by the FilterChain class.
 * 
 * @author unrza249
 * @author unrz205
 */
public interface IFilter {

	/**
	 * This method must return the unique identifier of the filter. A unique
	 * identifier is needed for correct registration of the filter.
	 * 
	 * @return the filter identifier
	 */
	public abstract String getID();

	/**
	 * Sets the identifier of this filter. A filter should have a predefined
	 * identifier. A good idea is to use the class.getName() method.
	 * 
	 * @param id
	 */
	public abstract void setID(String id);

	/**
	 * This method returns a short description of what the filter is doing and
	 * how.
	 * 
	 * @return description
	 */
	public abstract String getDescription();

	/**
	 * This method sets the description of the filter.
	 * 
	 * @param description
	 */
	public abstract void setDescription(String description);

	/**
	 * This method does the actual filtering. It implements the main logic of
	 * the filter.
	 * 
	 * @param id
	 *            the id to be checked
	 * @return <em>null</em> if the id should be filtered and the
	 *         id if it satisfies the rules.
	 */
	public abstract String apply(String id);

	/**
	 * This method checks a whole list of ids. It should return a list of
	 * suitable ids or an empty list if none of the ids fits the
	 * rules.
	 * 
	 * @param ids
	 *            a list of ids to be checked
	 * @return the list with filtered ids
	 */
	public abstract List<String> apply(List<String> ids);
	
	
	/**
	 * Returns the filter type (simple class name)
	 * 
	 * @return filter type
	 */
	public abstract String getType();
}
