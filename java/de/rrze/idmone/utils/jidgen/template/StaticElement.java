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
 * This element implementation is for handling static
 * strings that should not be modified in any way and
 * are directly supplied inside the template string without
 * the use of variables.
 * 
 * @author unrza249
 *
 */
public class StaticElement
	extends AbstractElement
	implements IElement 
{
	
	/**
	 *  The class logger
	 */
	//private static final Log logger = LogFactory.getLog(StaticElement.class);
	
	
	/**
	 * Simple default constructor
	 */
	public StaticElement() {
		super.needsExternalData(true);
		super.hasAlternatives(true);
	}
	
	/**
	 * Conveniently complex constructor for direct usage by the Parser
	 * class
	 * 
	 * @param element
	 * 			the whole part of the template string this object describes
	 * @param data
	 * 			the data string for this element
	 */
	public StaticElement(String element,  String data) {
		super();
		
		this.setElement(element);
		this.setData(data);
		
		super.hasAlternatives(true);
		
		// do our own thing here
		// ...
	}

	/* (non-Javadoc)
	 * @see de.rrze.idmone.utils.jidgen.template.IElement#isComplete()
	 */
	public boolean isComplete() {
		// Add more complex tests here, if needed.
		// Element and data are tested by the AbstractElement class
		// implementation.
		return (super.isComplete());
	}
	
	
	/* (non-Javadoc)
	 * @see de.rrze.idmone.utils.jidgen.template.IElement#toString()
	 */
	public String toString() {
		// we only got one alternative
		super.hasAlternatives(false);
		return this.getData();
	}
}
