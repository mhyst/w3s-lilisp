/*
    W3S Web Personal Spider
    Copyright (C) 2008  Julio Cesar Serrano Ortuno

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package julk.net.w3s;

/**********************************************************************************
 * CLASS: Config
 *
 * Manages the bot configuration.
 *
 * @author Julio Cesar Serrano Ortuno
 * created January 20, 2008
 * updated February 10, 2008
 * license General Public License
 **********************************************************************************/
public class Config extends java.util.Properties {

	private static final long serialVersionUID = -6195502642269375716L;
	private static final String author = "Mhyst";
	public static final String name = "W3S";
	private static final String version = "1.42 beta";
	private static final String botName = name+"/"+version+" Java 6 by "+author;
	
	public static String getUserAgent() {
		return Config.botName;
	}

	public static String getVersion() {
		return Config.version;
	}
	
	public static String getAuthor() {
		return Config.author;
	}
	
	public static String getName() {
		return Config.name;
	}
	
	public static String getFullName() {
		return Config.name+" "+Config.version+" by "+Config.author;
	}

	/**
	 * Helps to retrieve properties 
	 * returning boolean values instead of strings
	 * 
	 * @param name the name of the desired property
	 * @return if the value is "FALSE" it returns false otherwise it returns true.
	 */
	public boolean getBoolProperty(final String name) {
		return getProperty(name,"FALSE").equals("TRUE") ? true : false;
	}
	
	/**
	 * Helps to retrieve properties 
	 * returning int values instead of strings
	 * 
	 * @param name the name of the desired property
	 * @return the string property value parsed to int
	 */
	public int getIntProperty(final String name) {
		final String sValue = getProperty(name,"0");
		int value;
		try {
			value = Integer.parseInt(sValue);
			return value;
		} catch (final Exception e) {
			return 0;
		}
	}
}
