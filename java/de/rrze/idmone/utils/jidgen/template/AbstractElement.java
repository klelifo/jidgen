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
 * This class just provides the bare minimum functionality
 * to implement the IElement interface and is meant to be extended
 * by a few more sophisticated classes with added functionality.
 * 
 * @author unrza249
 *
 */
public abstract class AbstractElement 
	implements IElement 
{
	/**
	 * The element string.<br/>
	 * The whole part of the template string this element
	 * stands for.
	 */
	private String element;
	
	/**
	 * The contained <b>data definition</b> that is contained
	 * in the element string.<br/>
	 * In case of a reference to a specified command line value
	 * this is the variable name (key of the value) of the specified
	 * data.<br/>
	 * Some element implementations might also use this as real raw
	 * data, though.
	 */
	private String key;
	
	/**
	 * The data string to be used in this element.
	 * All modifications are made to this string and then returned
	 * by the toString() method of every element implementation.
	 */
	private String data;
		
	/**
	 * Specifies whether this object needs to be fed
	 * external data from the template class via the
	 * setData() method.
	 */
	private boolean needsExternalData;

	/**
	 * Specifies whether this object has some kind of
	 * alternation in its result,
	 * e.g. some kind of counter or randomness, so that
	 * it is likely or sure to return another value 
	 * than before when invoked again with the same input.<br />
	 * <i> This is not necessarily an exact value nor does it
	 * protect the element from beeing called again for a
	 * string, even if it states that it has no alternatives
	 * left.</i><br />
	 * <b>If set to false the element states to the outside that
	 * all alternatives were returned.<br />
	 * If set to true the element states that it might have more
	 * alternative values left to return.</b>
	 */
	private boolean hasAlternatives;
	
	/**
	 * This specifies if the element is a resolver.
	 */
	private boolean resolver;
	
	
	/**
	 * Simple default constructor
	 */
	public AbstractElement() {
		this.needsExternalData = false;
		this.hasAlternatives = true;
		this.resolver = false;
	}
	
	/**
	 * Basic constructor
	 * 
	 * @param element
	 * 			the template string's element
	 * @param key
	 * 			the key value used in this element
	 */
	public AbstractElement(String element, String key) {
		this.setElement(element);
		this.setKey(key);
	}


	/* (non-Javadoc)
	 * @see de.rrze.idmone.utils.jidgen.template.IElement#isComplete()
	 */
	public boolean isComplete() {
		return ((this.element != null) && 
				(this.data != null));
	}
	
	/* (non-Javadoc)
	 * @see de.rrze.idmone.utils.jidgen.template.IElement#needsExternalData()
	 */
	public boolean needsExternalData() {
		return this.needsExternalData;
	}
	

	/**
	 * Sets the needsExternalData flag
	 * 
	 * @param needsExternalData
	 * 			should be true if external data is needed, false otherwise
	 */
	public void needsExternalData(boolean needsExternalData) {
		this.needsExternalData = needsExternalData;
	}
	
	/* This function needs to be implemented by some extending
	 * subclass.
	 * 
	 * (non-Javadoc)
	 * @see de.rrze.idmone.utils.jidgen.template.IElement#toString()
	 */
	public abstract String toString();

	
	/* (non-Javadoc)
	 * @see de.rrze.idmone.utils.jidgen.template.IElement#getData()
	 */
	public String getKey() {
		return key;
	}

	/* (non-Javadoc)
	 * @see de.rrze.idmone.utils.jidgen.template.IElement#setData(java.lang.String)
	 */
	public void setKey(String key) {
		this.key = key;
	}

	/* (non-Javadoc)
	 * @see de.rrze.idmone.utils.jidgen.template.IElement#getElement()
	 */
	public String getElement() {
		return element;
	}

	/* (non-Javadoc)
	 * @see de.rrze.idmone.utils.jidgen.template.IElement#setElement(java.lang.String)
	 */
	public void setElement(String element) {
		this.element = element;
	}

	/* (non-Javadoc)
	 * @see de.rrze.idmone.utils.jidgen.template.IElement#getData()
	 */
	public String getData() {
		return data;
	}

	/* (non-Javadoc)
	 * @see de.rrze.idmone.utils.jidgen.template.IElement#setData(java.lang.String)
	 */
	public void setData(String data) {
		this.data = data;
	}


	/* (non-Javadoc)
	 * @see de.rrze.idmone.utils.jidgen.template.IElement#hasAlternatives()
	 */
	public boolean hasAlternatives() {
		return hasAlternatives;
	}

	/**
	 * Sets the alternatives flag
	 * 
	 * @param alternatives
	 * 			should be true if alternatives are available, false otherwise
	 */
	public void hasAlternatives(boolean alternatives) {
		this.hasAlternatives = alternatives;
	}
	
	
	/* (non-Javadoc)
	 * @see de.rrze.idmone.utils.jidgen.template.IElement#isResolver()
	 */
	public boolean isResolver() {
		return this.resolver;
	}	
	
	/* (non-Javadoc)
	 * @see de.rrze.idmone.utils.jidgen.template.IElement#setResolver(boolean)
	 */
	public void setResolver(boolean resolver) {
		this.resolver = resolver;
	}
}
