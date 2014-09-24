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
 * This element implementation manages an internal counter
 * that is increased after every output request.
 * The output string represents the value of the internal counter
 * with the characters given in the data string as the basis.
 * The character position in the data string is used as its ordinal
 * value in the transformation.<br />
 * Given the data string 'abc' (a=0, b=1, c=2) and a requested length of 
 * 2 the number 2 would generate 'ac' and the number 3 therefore 'ba'.
 * 
 * @author unrza249
 *
 */
public class CounterElement
	extends AbstractElement
	implements IElement 
{
	
	/**
	 *  The class logger
	 */
	//private static final Log logger = LogFactory.getLog(CounterElement.class);
	
	/**
	 * The length of the counter element to be returned.
	 */
	private int length = 1;
	
	/**
	 * The internal counter, starting with 0.
	 */
	private int counter = 0;
	
	/**
	 * Number of combinations possible, given
	 * the number of different characters in the data string
	 * and the length of the target string.
	 */
	private double numAlternatives = 0;
	
	/**
	 * Internal flag whether to recalculate the number
	 * of possible alternatives.<br />
	 * <b>used only internally</b>
	 */
	private boolean updateAlternatives = true;
	
	
	/**
	 * Simple default constructor
	 */
	public CounterElement() {
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
	 */
	public CounterElement(String element,  String key) {
		// call parent constructor for the basic stuff
		super(element, key);
		// enable the need for external data (if needed ;)
		super.needsExternalData(true);
		super.hasAlternatives(true);
		
		// do our own thing here
		// ...
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
	 * @param length
	 * 			the target length of the output string
	 */
	public CounterElement(String element,  String key, int length) {
		// call parent constructor for the basic stuff
		super(element, key);
		// enable the need for external data (if needed ;)
		super.needsExternalData(true);
		super.hasAlternatives(true);
		
		this.setLength(length);
	}
	
	
	/* (non-Javadoc)
	 * @see de.rrze.idmone.utils.jidgen.template.IElement#isComplete()
	 */
	public boolean isComplete() {
		// Add more complex tests here, if needed.
		// Element and data are tested by the AbstractElement class
		// implementation.
		return (super.isComplete()
				&& this.length > 0);
	}
	
	/**
	 * Returns the number of possible alternate value combinations
	 * for the given target string length based on the number of
	 * different symbols available in the data string.
	 * <i>If necessary the numAlternatives variable is updated automatically</i>
	 * 
	 * @return maximum number of possible alternatives
	 */
	public double getNumAlternatives() {
		if (this.updateAlternatives) {
			this.numAlternatives = Math.pow(super.getData().length(),this.getLength());
			this.updateAlternatives = false;
		}
		return this.numAlternatives;
	}
	
	
	/* (non-Javadoc)
	 * @see de.rrze.idmone.utils.jidgen.template.IElement#toString()
	 */
	public String toString() {
		
		if (this.updateAlternatives) {
			this.numAlternatives = this.getNumAlternatives();
			this.updateAlternatives = false;
			this.hasAlternatives(true);
		}
		
		/*
		 * build the string
		 */
		
		// init
		String dummy = "";
		String data = super.getData();
		for (int i = 0; i < this.getLength(); i++) dummy += data.charAt(0);
		char[] retArr = dummy.toCharArray();

		int pos = retArr.length - 1;
		int number = this.counter;
		int numberPoolSize = data.length();
		
		// start transformation
		while (number > 0) {
			// get the residuum and translate it to a character from the data string
			int residuum = number % numberPoolSize;
			retArr[pos] = data.charAt(residuum);
			
			// update the number 
			number = (int)Math.floor(number / numberPoolSize);
			pos--;
		}
		
		// increment the internal counter
		this.counter = ++this.counter % (int)this.numAlternatives;
		//this.counter++;
		
		if (this.counter == this.numAlternatives) {
			// set to false, when we return our last alternative 
			// This states to the outside that all alternatives of this
			// element were returned.
			super.hasAlternatives(false);
		}
		
		return new String(retArr);
	}

	/**
	 * Sets a new data string and manages automatic
	 * update of the numAlternatives count.
	 * 
	 * @param data
	 * 			new data string
	 */
	public void setData(String data) {
		this.updateAlternatives = true;
		super.setData(data);
	}
	
	/**
	 * Get the target length of the counter string.
	 * 
	 * @return target length of the counter string
	 */
	public int getLength() {
		return length;
	}

	/**
	 * Set the target length of the counter string.
	 * 
	 * @param length
	 * 			target length of the counter string
	 */
	public void setLength(int length) {
		this.updateAlternatives = true;
		this.length = length;
	}
}
