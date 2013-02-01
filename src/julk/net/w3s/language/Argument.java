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

public class Argument {
	private Type type = null;
	private String name = null;
	private String value;
	private Variable var = null;
	private boolean quoted = false;
	
	public Argument(String value) throws Exception {
		value = value.trim();
		if (/*value.startsWith("((") ||*/ value.startsWith("\"")) {
			value = value.substring(1,value.length()-1);
			quoted = true;
		}
		this.value = value;
		type = Type.determineTheType(value);
		//adjustType();
		/*if (this.value.charAt(0) == '\"') {
			this.value = this.value.substring(1, value.length()-1);
		}*/
	}
	
	/*private void adjustType() throws Exception {
		String[] aux;// = new String[2];
		aux = DataTypes.adjustType(type,value);
		if (aux != null) {
			this.type = aux[0];
			this.value = aux[1];
		}
	}*/

	
	/*private void adjustType() {
		if (type != null) return;
		try {
			doubleValue = Double.parseDouble(value);
			type = "double";
		} catch (Exception e) {
			//Es cadena
			type = "string";
		}
	}*/
	
	/*public boolean isDouble() {
		return type.contains("double");
	}*/
	
	public boolean isVariable() {
		return var != null;
	}
	
	public Argument(String type, String name, Variable var) {
		this.type = Type.getType(type);
		this.name = name;
		this.var = var;
		this.value = var.getValue().toString();
	}
	
	public Argument (String type, String name) {
		this(type,name,null);
	}

	public Type getType() {
		return type;
	}

	public void setType(String type) {
		this.type = Type.getType(type);
	}
	
	public void setType(Type type) {
		this.type = type;
	}

	public Variable getVar() {
		return var;
	}

	public void setVar(Variable var) {
		this.var = var;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public boolean fits(Argument arg) {
		if (this.type.equals(arg.type)) {// &&
			//this.name.equals(arg.name)) {
			return true;
		} else {
			return false;
		}
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
		//adjustType();
	}

	public void setValue(Variable var) {
		this.type = var.getType();
		this.name = var.name;
		this.value = var.getValue().toString();
		this.var = var;
		//adjustType();
	}

	public boolean isQuoted() {
		return quoted;
	}	
}
