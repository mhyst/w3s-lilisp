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
 * CLASS: ExcludedElement
 *
 * Stores a excluded URL fragment.
 * All the links matching URLfragment with a depth = -1
 * will be excluded from the search.
 * There can be from 0 to many ExcludedElement instances.
 *
 * @author Julio Cesar Serrano Ortuno
 * created January 18, 2008
 * updated February 10, 2008
 * license General Public License
 **********************************************************************************/
public class ExcludedElement {
	private String URLfragment;
	private int depth; //If depth is -1 then it's excluded
	
	public ExcludedElement(String url, int depth) {
		this.URLfragment = url;
		this.depth = depth;
	}
	
	public ExcludedElement(String url, String depth) {
		this.URLfragment = url;
		try {
			this.depth = Integer.parseInt(depth);
		} catch (Exception e) {
			this.depth = -1;
		}
	}
	
	public ExcludedElement(String[] parts) {
		if (parts != null) {
			URLfragment = parts[0];
			if (parts.length>1) {
				try {
					this.depth = Integer.parseInt(parts[1]);
				} catch (Exception e) {
					this.depth = -1;
				}
			} else {
				depth = -1;
			}
		}
	}
	
	public ExcludedElement(String line) {
		this(line.split(" "));
	}
	
	public int getDepth() {
		return depth;
	}
	public void setDepth(int depth) {
		this.depth = depth;
	}
	public String getURLfragment() {
		return URLfragment;
	}
	public void setURLfragment(String lfragment) {
		URLfragment = lfragment;
	}
	
	public String toString() {
		return URLfragment+" "+depth;
	}
}
