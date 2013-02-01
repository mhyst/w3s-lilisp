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
 * CLASS: SWOutput 
 *
 * Implements Output methods.
 *
 * @author Julio Cesar Serrano Ortuno
 * created January 26, 2008
 * updated February 10, 2008
 * license General Public License
 **********************************************************************************/
public class SWOutput implements Output {

	private W3s.W3sWorker sw;
	
	public SWOutput(W3s.W3sWorker swing_worker){
		sw = swing_worker;
	}
	
	///Communication with the GUI
	private void publish(String msg){
		if (sw != null) {
			sw.doPublish(msg);
		} else {
			System.out.println(msg);
		}
	}
	
	public boolean isStopping() {
		if (sw != null)
			return sw.isStopping();
		else
			return false;
	}
	
	public void setWorkingLink(int depth, String link) {
		publish(""+depth+"-"+link);
	}
	
	public void addToOutput(String msg) {
		publish("ERR:"+msg);
	}
	
	public void setProgressBar(int remaining, int total) {
		publish("Bar/"+remaining+"/"+total);
	}
	//End of communication with the GUI
}
