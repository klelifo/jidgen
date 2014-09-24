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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.rrze.idmone.utils.jidgen.Messages;


/**
 * Parser class for the IdGenerator's template
 * system.<br />
 * All parsing and conversion to element objects
 * is done here.
 * 
 * @author unrza249
 *
 */
public class Parser {

	/**
	 *  The class logger
	 */
	private static final Log logger = LogFactory.getLog(Parser.class);

	/**
	 * The delimiter string that marks the end of one
	 * and the beginning of another template element in
	 * the template string.
	 */
	private static final String ELEMENT_DELIMITER = ":";
	
	/**
	 * This is a flag indicating whether or not the
	 * already parsed elements contain a counter element.
	 * It is used to limit the number of counter elements to one.
	 * <b>Only used internally. Will be removed in future versions</b>
	 */
	private static boolean hasCounterElement;
	
	/**
	 * Processes the given template string by splitting it into its
	 * parts and parse each part to compile a list of according 
	 * element objects.<br/>
	 * Those are used by the template class to finally build the
	 * result string.
	 * 
	 * @param template
	 * 			the template string to process
	 * @return	a list of elements representing the element parts inside
	 * 			the template string
	 */
	public static ArrayList<IElement> getElements(String template) {
		
		// reset the hasCounter flag
		hasCounterElement = false;
		
		// init elements array
		ArrayList<IElement> elements = new ArrayList<IElement>();
		
		// split into parts (which later become elements)
		ArrayList<String> parts = new ArrayList<String>(Arrays.asList(template.split(ELEMENT_DELIMITER)));
		//logger.debug(parts.toString());
		
		boolean isResolver;
		
		// process the parts - here starts the fun ;)
		for (Iterator<String> iter = parts.iterator(); iter.hasNext();) {
			String currentPart = iter.next();
			
			/*
			 * ALTERNATIVE / RESOLVER 
			 */
			Matcher m = getMatcher("^\\[(.*)\\]$", currentPart);
			if (m.matches()) {
				logger.debug(Messages.getString("Parser.MATCHED_PATTERN") + m.pattern() + " => ALTERNATIVE/RESOLVER");
				isResolver = true;
				currentPart = m.group(1);
			}
			else {
				isResolver = false;
			}
			
			// parse and get the element instance
			IElement element = Parser.parse(currentPart);
			
			// set the resolver status
			element.setResolver(isResolver);
			
			// add the element
			elements.add(element);
		}
		
		return elements;
	}
	
	
	/**
	 * Helper class for the getElements() method that
	 * matches the given element part string against
	 * all known element patterns to determine which
	 * element implementation to use.
	 *  
	 * @param part
	 * 			the element part string to match against
	 * @return	a matching element object, representing the
	 * 			given element part string
	 */
	private static IElement parse(String part) {	
		logger.debug(Messages.getString("Parser.PROCESSING_PART") + part);
		
		Matcher m;
		
		/*
		 * BASIC
		 */

		// Basic (e.g. a)
		m = getMatcher("^([a-zA-Z])$", part);
		if (m.matches()) {
			logger.debug(Messages.getString("Parser.MATCHED_PATTERN") + m.pattern() + " => BASIC");
			return new BasicElement(
					m.group(1), 
					m.group(1)
			);	
		}
		
		
		/*
		 * STATIC
		 */

		// Static (e.g. =my_prefix)
		m = getMatcher("^(=([a-zA-Z_0-9]*))$", part);
		if (m.matches()) {
			logger.debug(Messages.getString("Parser.MATCHED_PATTERN") + m.pattern() + " => STATIC");
			return new StaticElement(
					m.group(1), 
					m.group(2)
			);	
		}
		
		
		/*
		 * RANDOM
		 */
		
		// Random - exactly one (e.g. a+) or as many as specified by repetation (e.g. aaa+)
		m = getMatcher("^(([a-zA-Z])(\\2*)\\+)$", part);
		if (m.matches()) {
			int length = m.group(3).length() + 1;
			logger.debug(Messages.getString("Parser.MATCHED_PATTERN") + m.pattern() + " => RANDOM (length=" + length + ")");
			return new RandomElement(
					m.group(1), 
					m.group(2),
					length
			);	
		}

		// Random - as many as specified by number (e.g. a3+) - zero or leading zeros are forbidden!
		m = getMatcher("^(([a-zA-Z])([1-9][0-9]*)\\+)$", part);
		if (m.matches()) {
			int length = Integer.parseInt(m.group(3));
			logger.debug(Messages.getString("Parser.MATCHED_PATTERN") + m.pattern() + " => RANDOM (length=" + length + ")");
			return new RandomElement(
					m.group(1), 
					m.group(2),
					length
			);	
		}
	
		
		/*
		 * SUBSTRING
		 */
		
		// Substring (e.g. 1a)
		m = getMatcher("^(([1-9])([a-zA-Z]))$", part);
		if (m.matches()) {
			logger.debug(Messages.getString("Parser.MATCHED_PATTERN") + m.pattern() + " => SUBSTRING (first x characters)");
			return new SubstringElement(
					m.group(1), 
					m.group(3),
					1,
					Integer.parseInt(m.group(2))
			);	
		}

		// Substring (e.g. a1)
		m = getMatcher("^(([a-zA-Z])([1-9]))$", part);
		if (m.matches()) {
			logger.debug(Messages.getString("Parser.MATCHED_PATTERN") + m.pattern() + " => SUBSTRING (last x characters)");
			return new SubstringElement(
					m.group(1),
					m.group(2),
					-1,			// this is special and indicates that
								// the last x (stored in the end parameter) characters
								// are to be used
					Integer.parseInt(m.group(3))
			);	
		}
		
		// Substring (e.g. 1a5)
		m = getMatcher("^(([1-9])([a-zA-Z])([1-9]))$", part);
		if (m.matches()) {
			logger.debug(Messages.getString("Parser.MATCHED_PATTERN") + m.pattern() + " => SUBSTRING (start, end)");
			return new SubstringElement(
					m.group(1),
					m.group(3),
					Integer.parseInt(m.group(2)), 
					Integer.parseInt(m.group(4)) 
			);	
		}
		
		// Substring with start and end (e.g. a1,5)
		m = getMatcher("^(([a-zA-Z])([1-9]),([1-9]))$", part);
		if (m.matches()) {
			logger.debug(Messages.getString("Parser.MATCHED_PATTERN") + m.pattern() + " => SUBSTRING (start, end)");
			int end = Integer.parseInt(m.group(4));
			return new SubstringElement(
					m.group(1),
					m.group(2),
					Integer.parseInt(m.group(3)), 
					(end<=0)?-1:end  	// this is just to be as kind as possible to the user
										// -> even negative end values are interpreted
			);	
		}	
		
		// Substring (e.g. a1,)
		m = getMatcher("^(([a-zA-Z])([1-9]),)$", part);
		if (m.matches()) {
			logger.debug(Messages.getString("Parser.MATCHED_PATTERN") + m.pattern() + " => SUBSTRING (start at x until end of the string)");
			return new SubstringElement(
					m.group(1),
					m.group(2),
					Integer.parseInt(m.group(3)), 
					-1
			);	
		}	
		
		// Substring (e.g. a,1)
		m = getMatcher("^(([a-zA-Z]),([1-9]))$", part);
		if (m.matches()) {
			logger.debug(Messages.getString("Parser.MATCHED_PATTERN") + m.pattern() + " => SUBSTRING (first x characters)");
			return new SubstringElement(
					m.group(1),
					m.group(2),
					1,
					Integer.parseInt(m.group(3))
			);	
		}	
		
		
		/*
		 * COUNTER
		 */	
		
		// Counter - exactly one (e.g. a++) or as many as specified by repetation (e.g. aaa++)
		m = getMatcher("^(([a-zA-Z])(\\2*)\\+\\+)$", part);
		if (m.matches()) {
			
			// limit number of counter elements to one
			if (Parser.hasCounterElement == true) {
					logger.fatal(Messages.getString("Parser.ONLY_ONE_COUNTER_ELEMENT_ALLOWED"));
					System.exit(171);
			}
			Parser.hasCounterElement = true;
			
			
			int length = m.group(3).length() + 1;
			logger.debug(Messages.getString("Parser.MATCHED_PATTERN") + m.pattern() + " => COUNTER (length=" + length + ")");
			return new CounterElement(
					m.group(1), 
					m.group(2),
					length
			);	
		}

		// Counter - as many as specified by number (e.g. a3++) - zero or leading zeros are forbidden!
		m = getMatcher("^(([a-zA-Z])([1-9][0-9]*)\\+\\+)$", part);
		if (m.matches()) {
			int length = Integer.parseInt(m.group(3));
			logger.debug(Messages.getString("Parser.MATCHED_PATTERN") + m.pattern() + " => COUNTER (length=" + length + ")");
			return new CounterElement(
					m.group(1), 
					m.group(2),
					length
			);	
		}
		
		
		
		/*
		 * IF WE ARE HERE, NONE OF THE ABOVE PATTERNS HAS MATCHED!
		 */
		
		logger.fatal(Messages.getString("Parser.NO_MATCHING_ELEMENT_FOUND") + part);
		System.exit(170);
		return null;
	}
	
	private static Matcher getMatcher(String pattern, String matchee) {
		Pattern p = Pattern.compile(pattern);
		Matcher m = p.matcher(matchee);
		return m;
	}
	
}
