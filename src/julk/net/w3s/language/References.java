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

import java.util.Enumeration;
import java.util.Hashtable;

import julk.net.w3s.annotations.ClassInfo;

@ClassInfo (
		id = 2,
		name = "References",
		creationDate = "April 03, 2008",
		description = "References management"
)
public class References implements Cloneable{
	private Hashtable<String,Reference> references;
	private long cont = 0;
	private static String prefix = "&";
	
	public References() {
		references = new Hashtable<String, Reference>();
	}
	
	public Object clone() {
		try {
			References ref = new References();
			ref.references = new Hashtable<String, Reference>();
			for (Reference r: references.values()) {
				ref.references.put(r.address, r);
				ref.cont++;
			}
			return ref;
		} catch (Exception e) {
			return null;
		}
	}
	
	public Reference get(String address) {
		return references.get(address);
	}
	
	public synchronized String newReference(Reference ref) { 
		ref.address = prefix+cont;
		cont++;
		references.put(ref.address, ref);		
		return ref.address;
	}
	
	public synchronized void clean() {
		Enumeration<Reference> e = references.elements();
		while (e.hasMoreElements()) {
			Reference ref = e.nextElement();
			if (ref.isUnassigned()) {
				references.remove(ref.address);
			}
		}
	}
	
	public synchronized void assignVariable(String address, Variable var) {
		Reference ref = references.get(address);
		if (ref != null) ref.addVariable(var);
	}
	
	public synchronized void unassignVariable(String address, Variable var) {
		Reference ref = references.get(address);
		if (ref != null) ref.removeVariable(var);
	}
	
	public static boolean isReference(String address) {
		return address.startsWith(prefix);
	}
	
	public int getNumberOfReferences() {
		return references.size();
	}
	
	public Reference getReferenceByValue(Object value) {
		for (Reference ref : references.values()) {
			if (ref.value.toString().equals(value.toString()))
				return ref;
		}
		return null;
	}
}
