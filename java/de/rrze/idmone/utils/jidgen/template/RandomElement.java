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

import java.util.Random;

import de.rrze.idmone.utils.jidgen.random.IRandomFactory;
import de.rrze.idmone.utils.jidgen.random.RandomFactory;


/**
 * This element implementation chooses a random character
 * from the specified string.
 * 
 * @author unrza249
 *
 */
public class RandomElement
	extends AbstractElement
	implements IElement 
{
	
	/**
	 *  The class logger
	 */
	//private static final Log logger = LogFactory.getLog(RandomElement.class);
	
	/**
	 * This defines how many times the numAlternatives value
	 * random strings should be returned until we state that there
	 * are no more alternatives left.
	 * <b>This is not an exact science but much less expensive than
	 * tracking every result ever returned.</b>
	 */
	private static final int timesOvercoverage = 5;
	
	
	/**
	 * If true, the class will always claim to have
	 * alternatives left.
	 * <b>currently this is only for internal use</b>
	 */
	private boolean alwaysAlternatives = false;
	
	/**
	 * An instance of the Random number that would be used during the generation 
	 * process
	 */
	private Random random;
	
	/**
	 * The random number generator factory
	 */
	private IRandomFactory randomFactory;
	
	/**
	 * The length of the random string to be returned. 
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
	public RandomElement() {
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
	 * @param length
	 * 			the target length of the output string
	 */
	public RandomElement(String element,  String key, int length) {
		// call parent constructor for the basic stuff
		super(element, key);
		// enable the need for external data (if needed ;)
		super.needsExternalData(true);
		super.hasAlternatives(true);
		
		this.setLength(length);
		
		// create random number generator
		this.randomFactory = RandomFactory.getInstance();
		this.random = randomFactory.getRandom();
/*
		// if you want secure random for some obscure reason:
		// enable this and comment out the line above
		try
		{		
			// try to get a secure random number generator instance
			random = randomFactory.getSecureRandom();
		} catch (NoSuchAlgorithmException e)
		{
			e.printStackTrace();
			// fall back to "unsecure" generator
			random = randomFactory.getRandom();
		} catch (NoSuchProviderException e)
		{
			e.printStackTrace();
			// fall back to "unsecure" generator
			random = randomFactory.getRandom();
		}
*/
	}

	/* (non-Javadoc)
	 * @see de.rrze.idmone.utils.jidgen.template.IElement#isComplete()
	 */
	public boolean isComplete() {
		// Add more complex tests here, if needed.
		// Element and data are tested by the AbstractElement class
		// implementation.
		return (super.isComplete()
				&& this.random != null
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
	
		
	/**
	 * Returns a string with target length characters using
	 * the specified data string as character repository.
	 * 
	 * @param targetLength
	 * 			length of the desired random string
	 * @return a random string with the requested length
	 */
	public String buildRandomString(int targetLength) {
		String data = this.getData();
		int dataLength = data.length();
		String ret = "";
		
		for (int i = 0; i < targetLength; i++) {
			ret += data.charAt(this.random.nextInt(dataLength));
		}
		
		return ret;
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
		
		String result = this.buildRandomString(this.getLength());
		
		if (!this.alwaysAlternatives) {
			if (this.counter == (this.numAlternatives * timesOvercoverage)) {
				// set to false, when we return our last alternative
				// This states to the outside that all alternatives of this
				// element were returned.
				super.hasAlternatives(false);				
			}
			else {
				// increment the internal counter
				this.counter++;			
			}
		}
		
		return result;
	}

	/**
	 * Get the target length of the random string.
	 * 
	 * @return target length of the random string
	 */
	public int getLength() {
		return length;
	}

	/**
	 * Set the target length of the random string.
	 * 
	 * @param length
	 * 			target length of the random string
	 */
	public void setLength(int length) {
		this.updateAlternatives = true;
		this.length = length;
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

}
