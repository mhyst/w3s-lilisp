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

public class Variables implements Cloneable {	
	private String namespace;
	private References references;
	private Hashtable<String, Variable> variables;
	private static String prefix = "$";
	
	public Variables(References refs, String namespace) {
		this.namespace = namespace;
		references = refs;
		variables = new Hashtable<String, Variable>();
	}
	
	public Object clone() {
		try {
			Variables v = (Variables) super.clone();
			//v.references = (References) this.references.clone();
			v.references = references;
			
			v.variables = new Hashtable<String, Variable>();
			for (Variable var : variables.values()) {
				Reference ref = new Reference();
				ref.type = var.ref.type;
				ref.value = var.ref.value;
				v.references.newReference(ref);
				var.ref = ref;
				v.variables.put(var.name, var);
			}
			
			return v;
		} catch (Exception e) {
			return null;
		}
	}
	
	public Variables(References refs) {
		this(refs, "local");
	}
	
	public Variable get(String name) {
		return variables.get(name);
	}
	
	public void set(String name, Variable var) {
		variables.put(name,var);
		createHelp(var);
	}
	
	public Variable set(String name, Reference ref) {
		Variable aux = new Variable(name,ref);
		ref.addVariable(aux);
		set(name,aux);
		return aux;
	}	
	
	public void set(String name, String refaddress) {
		Reference ref = references.get(refaddress);
		Variable aux = new Variable(name,ref);
		ref.addVariable(aux);
		set(name,aux);
	}
	
	public Reference removeVariable(String name) {
		Variable var = get(name);
		if (var == null) return null;
		Reference ref = var.ref;
		ref.removeVariable(var);
		//variables.remove(var);
		variables.remove(name);
		removeHelp(name);
		return ref;
	}

	public boolean isEmpty() {
		return variables.isEmpty();
	}

	public String getNamespace() {
		return namespace;
	}

	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}
	
	public static boolean isVariable(String name) {
		return name.startsWith(prefix);
	}
	
	private synchronized void createHelp(Variable var) {
		//Class
		try {
			Reference refclass = new Reference();
			refclass.type = Type.string;
			refclass.value = var.ref.value.getClass().getName();
			references.newReference(refclass);
			String varname = var.name+".class";
			Variable varclass = new Variable(varname, refclass);
			variables.put(varname, varclass);
		} catch (Throwable t) {
		}
		try {
			//Type
			Reference reftype = new Reference();
			reftype.type = Type.string;
			reftype.value = var.ref.type.name();
			references.newReference(reftype);
			String varname = var.name+".type";
			Variable vartype = new Variable(varname, reftype);
			variables.put(varname, vartype);
		} catch (Throwable t) {}
		if (var.ref.type == Type.array) {
			int len = ((Object[])var.ref.value).length;
			//Type
			Reference reflen = new Reference();
			reflen.type = Type.numeric;
			reflen.value = len;
			references.newReference(reflen);
			String varname = var.name+".length";
			Variable varlen = new Variable(varname, reflen);
			variables.put(varname, varlen);			
		}
	}
	
	private void removeHelp(String name) {
		try {
			variables.remove(name+".class");
		} catch (Exception e) {}
		try {
			variables.remove(name+".type");
		} catch (Exception e) {}
		try {
			variables.remove(name+".length");
		} catch (Exception e) {}
		try {
			variables.remove(name+".id");
		} catch (Exception e) {}

	}
	
	public int getNumberOfVariables() {
		return variables.size();
	}
}
