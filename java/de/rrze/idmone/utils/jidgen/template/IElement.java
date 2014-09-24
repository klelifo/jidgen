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

package de.rrze.idmone.utils.jidgen.template;

/**
 * This interface needs to be implemented by all classes that
 * should be used is element objects to represent parts of the
 * template string.<br />
 * The actual instantiation of/conversion from template string
 * to element objects is done within the parser class. So if you
 * want to add a new implementation of this interface you will
 * need to add an appropriate piece of code there. 
 * 
 * @author unrza249
 *
 */
public interface IElement {

	/**
	 * Checks whether this element object has all data
	 * that it needs to produce a reasonable output via
	 * the toString() method.
	 * 
	 * @return true if all data is present, false otherwise
	 */
	public abstract boolean isComplete();

	/**
	 * Returns the stored key string
	 * 
	 * @return stored key
	 */
	public abstract String getKey();

	/**
	 * Sets the key string
	 * 
	 * @param data
	 * 			the new key string
	 */
	public abstract void setKey(String data);

	/**
	 * Returns the stored data string
	 * 
	 * @return stored data
	 */
	public abstract String getData();

	/**
	 * Sets the data string
	 * 
	 * @param data
	 * 			the new data string
	 */
	public abstract void setData(String data);
	
	/**
	 * Returns the stored element string
	 * 
	 * @return stored element string
	 */
	public abstract String getElement();

	/**
	 * Sets the element string
	 * 
	 * @param element
	 * 			the new element string
	 */
	public abstract void setElement(String element);
	
	/**
	 * Checks whether this object needs to be fed external data
	 * via the setData() method.
	 * 
	 * @return true if external data is needed, false otherwise
	 */
	public abstract boolean needsExternalData();

	/**
	 * Checks if this data object has alternate data available.
	 * 
	 * @return true if alternate data is available, false otherwise
	 */
	public abstract boolean hasAlternatives();
	
	/**
	 * Processes the stored data string and returns the
	 * result.
	 * 
	 * @return the processed data string
	 */
	public abstract String toString();

	/**
	 * Returns true if this element should only be processed
	 * as an resolver.
	 * That means as long as there are enough alternatives left
	 * this element will be ignored. 
	 * 
	 * @return true if this element is a resolver, false otherwise
	 */
	public abstract boolean isResolver();
	
	/**
	 * Set the resolver flag for this element.
	 * 
	 * @param resolver
	 * 			true if this element should be treated as an resolver, false otherwise
	 */
	public abstract void setResolver(boolean resolver);
	
}