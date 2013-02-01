/*  This file is part of Lilisp.
 *  Copyright (C) 2008  Julio Cesar Serrano Ortuno
 * 
 *  Lilisp is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Lilisp is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with Lilisp.  If not, see <http://www.gnu.org/licenses/>.
 */

package julk.net.w3s.language;

import java.util.Vector;

import julk.net.w3s.annotations.ClassInfo;

@ClassInfo (
	id = 1,
	name = "Reference",
	creationDate = "April 03, 2008",
	description = "Data holder class"
)
public class Reference {
	public String address;
	public Type type;
	public Object value;
	public Vector<Variable> variables;
	
	public Reference() {
		variables = new Vector<Variable>();
	}
	
	public boolean isUnassigned() {
		variables.trimToSize();
		return variables.size() == 0;
	}
	
	public void addVariable(Variable var) {
		variables.addElement(var);
	}
	
	public void removeVariable(Variable var) {
		variables.remove(var);
	}
}
