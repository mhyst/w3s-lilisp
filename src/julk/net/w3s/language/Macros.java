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

import julk.net.w3s.language.bit.Bit;
import julk.net.w3s.language.bit.BitCommand;

public class Macros implements BitCommand {
	private String library;
	private Hashtable<String, Macro> macros;
	private Program program;

	public Macros(Program pgm, String library) {
		program = pgm;
		this.library = library;
		macros = new Hashtable<String, Macro>();
	}
	
	public Macros(Program pgm) {
		this(pgm, "default");
	}

	
	public Macro get(String name) {
		return macros.get(name);
	}
	
	public void set(String name, Macro mac) {
		macros.put(name,mac);
	}
	
	public boolean isEmpty() {
		return macros.isEmpty();
	}

	public void createMacro(String name, Arguments args, Instruction inst, String code) {
		Macro mac = new Macro(name,args,inst,this,code);
		set(name,mac);
	}	
	
	public String evaluate(Program pgm, Variables vars, Instruction inst)
			throws Exception {
		Macro mac = get(inst.getCmd());
		if (mac == null) {
			Bit.error(inst,"macro "+inst.getCmd()+" not declared");
		}
		return mac.call(inst);
	}

	public boolean isArgsNumberOK(int num) {
		return false;
	}

	public boolean isLazy() {
		return false;
	}

	public String getLibrary() {
		return library;
	}

	public void setLibrary(String library) {
		this.library = library;
	}

	public Hashtable<String, Macro> getMacros() {
		return macros;
	}

	public void setMacros(Hashtable<String, Macro> macros) {
		this.macros = macros;
	}

	public Program getProgram() {
		return program;
	}

	public void setProgram(Program program) {
		this.program = program;
	}
}
