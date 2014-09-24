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

package de.rrze.idmone.utils.jidgen;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Loads the ResourceBundle associated with the IdGenerator. This ResourceBundle
 * contains the localized(externalized) strings used by the classes.
 * 
 * @author unrz205
 * 
 */
public class Messages
{
	
	/**
	 *  The class logger
	 */
	private static final Log logger = LogFactory.getLog(Messages.class);
	
	/**
	 * The identifier of the ResourceBundle.
	 */
	public static final String BUNDLE_NAME = "messages";

	// The ResourceBundle instance
	// FIXME use default locale (e.g. en_EN) if the appropriate file cannot be found -> MissingResourceException
	private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);

	/**
	 * Returns the localized message for this key. If the key is not found its
	 * value is returned surrounded by exclamation marks.
	 * 
	 * @param key the key to be searched for in the resource bundle
	 * @return the localized value for the key
	 */
	public static String getString(String key)
	{
		try
		{
			String ret = RESOURCE_BUNDLE.getString(key);
			return ret;
		} catch (MissingResourceException e)
		{
			logger.debug(e.getLocalizedMessage());
			return '!' + key + '!';
		}
	}
}
