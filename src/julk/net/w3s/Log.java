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

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Calendar;

/**********************************************************************************
 * CLASS: Link
 * An easy way to create log files and so register errors
 * happened during program execution.
 *
 * @author Julio Cesar Serrano Ortuno
 * created November 26, 2007
 * updated December 15, 2007
 * license General Public License
 **********************************************************************************/

public class Log {
	
	/**
	 * This method is static and can be invoqued without
	 * creating an instance of the class Log. Writes a line
	 * of text into the desired log file.
	 * 
	 * @param logname filename for the log
	 * @param source  module that generated the error
	 * @param message message to write
	 */
	public static void log(String logname, String source, String message) {
		try {
			PrintWriter out = new PrintWriter(new FileWriter(logname, true));
			Calendar c = Calendar.getInstance();
			out.println(c.getTime()+" "+source+": "+message);
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
