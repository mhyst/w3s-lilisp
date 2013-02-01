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

import java.util.Hashtable;

public class Types implements Cloneable {	
	private String namespace;
	private Hashtable<String, Variables> members;
	private References references;
	
	public Types(String namespace, References refs) {
		this.namespace = namespace;
		references = refs;
		members = new Hashtable<String, Variables>();
	}
	
	public Object clone() {
		try {
			Types t = (Types) super.clone();
			
			t.members = new Hashtable<String, Variables>();
			for (Variables p : members.values()) {
				t.members.put(p.getNamespace(), p);
			}
			
			return t;
		} catch (Exception e) {
			return null;
		}
	}
		
	public Reference get(String name) {
		Reference ref = new Reference();
		ref.type = Type.type;
		ref.value = members.get(name);
		return ref;
	}
	
	public Variables getVars(String name) {
		return members.get(name);
	}

	public void set(String name, Variables vars) {
		vars.setNamespace(name);
		members.put(name,vars);
	}
	
	public Variables newType(String name) {
		Variables vars = new Variables(references,name);
		set(name, vars);
		return vars;
	}

	public boolean isEmpty() {
		return members.isEmpty();
	}

	public String getNamespace() {
		return namespace;
	}

	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}		
}
