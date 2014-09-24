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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.rrze.idmone.utils.jidgen.Messages;

public class SubstringElement
	extends AbstractElement
	implements IElement 
{
	
	/**
	 *  The class logger
	 */
	private static final Log logger = LogFactory.getLog(SubstringElement.class);
	
	/**
	 * The start of the substring.
	 */
	private int start = -1;
	
	/**
	 * The end of the substring
	 */
	private int end = -1;

	/**
	 * Specifies if the substring should return
	 * the last x characters only.
	 */
	private boolean fromRight = false;
	
	/**
	 * Specifies if the substring should return
	 * all the characters from start to the
	 * end of the string.
	 */
	private boolean tillEnd = false;
	
	/**
	 * Simple default constructor
	 */
	public SubstringElement() {
		super.needsExternalData(true);
		super.hasAlternatives(true);
	}
	
	/**
	 * Conveniently complex constructor for direct usage by the Parser
	 * class
	 * 
	 * @param element
	 * 			the whole part of the template string this object describes
	 * @param key
	 * 			variable name for external data retrieval (the command line
	 * 			parameter that holds the data)
	 * @param start
	 * 			the begin-index of the substring starting with 1 or
	 * 			-1 to indicate that only the last end characters should 
	 * 			be returned 
	 * @param end
	 * 			the end-index of the substring or the number of characters
	 * 			from the end of the string if start = -1
	 */
	public SubstringElement(String element,  String key, int start, int end) {
		// call parent constructor for the basic stuff
		super(element, key);
		// enable the need for external data
		super.needsExternalData(true);
		super.hasAlternatives(true);
	
		// do our own thing here
		this.setEnd(end);
		this.setStart(start);
	}

	/* (non-Javadoc)
	 * @see de.rrze.idmone.utils.jidgen.template.IElement#isComplete()
	 */
	public boolean isComplete() {
		return (super.isComplete() &&
				(this.start > -1) && 
				(this.end > -1));
	}
	
	
	/* (non-Javadoc)
	 * @see de.rrze.idmone.utils.jidgen.template.IElement#toString()
	 */
	public String toString() {
		try {
			String ret;
			// little hack to allow the parser to specify
			// the last x characters without actually knowing how
			// long the data word is
			if (this.fromRight()) {
				// returns the last x (end in this case) characters
				ret =  this.getData().substring(this.getData().length() - this.end);
			}
			else if (this.tillEnd()) {
				// returns all characters from start to the end of the string
				ret = this.getData().substring(this.start);
			}
			else {
				// returns characters from start to end
				ret = this.getData().substring(this.start, this.end);
			}
			
			// we only got one alternative
			super.hasAlternatives(false);
			return ret;
		}
		catch (StringIndexOutOfBoundsException e) {
			logger.fatal(Messages.getString("SubstringElement.OUT_OF_BOUNDS") + this.getElement() + " (start=" + this.start + ", end=" + this.end  + ", data=" + this.getData() + ", key=" + this.getKey() + ")");
			System.exit(180);
		}
		
		return null;
	}

	
	/**
	 * Returns the start index
	 * 
	 * @return	start index
	 */
	public int getStart() {
		return start;
	}

	/**
	 * Sets the start index
	 * 
	 * @param start	start index starting with 1
	 */
	public void setStart(int start) {
		if (start == -1) {
			// start=-1 is short for "we want to have the last x (stored in end)
			// characters returned"
			this.start = 0;					// set to 0 to indicate it was initialized
			this.setFromRight(true);		// enable use last
		}
		else {
			this.start = start - 1;		// we start at 1 at the commandline
										// but at 0 internally
		}
	}

	/**
	 * Returns the end index
	 * 
	 * @return	end index
	 */
	public int getEnd() {
		return end;
	}

	/**
	 * Sets the end index
	 * 
	 * @param end	end index
	 */
	public void setEnd(int end) {
		// end=-1 is short for "we want to have the all characters from
		// start to end of the string returned"
		if (end == -1) {
			this.end = 0;				// set to 0 to indicate it was initialized
			this.setTillEnd(true);		// enable tillEnd
		}
		else {
			this.end = end;			// we start at 1 at the commandline
									// but at 0 internally
		}
	}

	/**
	 * Specifies whether to return only the last end characters
	 * 
	 * @return	true if only the last end characters are to be returned, false otherwise
	 */
	public boolean fromRight() {
		return fromRight;
	}

	/**
	 * Sets whether to return only the last end characters
	 * 
	 * @param fromRight	true if only the last end characters are to be returned, false otherwise
	 */
	public void setFromRight(boolean fromRight) {
		this.fromRight = fromRight;
	}

	/**
	 * Specifies whether to return all characters from
	 * start to the end of the string.
	 * 
	 * @return true if all character until the end of the string should be returned, false otherwise
	 */
	public boolean tillEnd() {
		return tillEnd;
	}

	/**
	 * Specifies whether to return all characters from
	 * start to the end of the string.
	 * 
	 * @param tillEnd
	 * 			true if all character until the end of the string should be returned, false otherwise
	 */
	public void setTillEnd(boolean tillEnd) {
		this.tillEnd = tillEnd;
	}

}
