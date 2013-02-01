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
 * INTERFACE: Output 
 *
 * User interface communication methods.
 * These methods are implemented in any class
 * responsible to notify the searching progress
 * to the user interface.
 *
 * @author Julio Cesar Serrano Ortuno
 * created January 26, 2008
 * updated February 10, 2008
 * license General Public License
 **********************************************************************************/
public interface Output {
	///Communication with the GUI
	public boolean isStopping();
	
	public void setWorkingLink(int depth, String link);
	
	public void addToOutput(String msg);
	
	public void setProgressBar(int remaining, int total);
	//End of communication with the GUI
}
