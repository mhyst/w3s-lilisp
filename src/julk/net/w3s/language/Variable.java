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

/**
 * This class represents a variable in w3s language
 * @author Mhyst
 *
 * @param <E>
 */
public class Variable {
	public String name;
	public Reference ref;
	
	public Variable(String name, Reference ref) {
		this.name = name;
		this.ref = ref;
	}
	
	public Type getType() {
		return ref.type;
	}
	
	public Object getValue() {
		return ref.value;
	}
}
