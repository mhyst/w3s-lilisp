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

//import julk.net.w3s.language.*;
import julk.net.w3s.language.bit.Bit;
import julk.net.w3s.language.bit.BitCommand;

public class Functions implements BitCommand{
	private String library;
	private Hashtable<String, Function> functions;
	//private Variables global;
	private Program pgm;
	//private int cont = 0;
	//private Commands commands;
	
	public Functions(Program pgm, String library) {
		this.pgm = pgm;
		this.library = library;
		functions = new Hashtable<String, Function>();
		//commands = new Commands();
		/*numArgs = new int[100];
		numArgs2 = null;
		validType = null;*/
	}
	
	public void createFunction(String name, Arguments args, Instruction inst, String code) {
		Function fun = new Function(name,args,inst,this,code);
		set(name,fun);
		
		/*Command aux = new Command(cont, name);
		aux.addNumArgs(args.countArgs());
		aux.setArgsTypes(null);
		aux.setLazy(false);
		commands.addCommand(aux);*/

		/*commands.addElement(name);
		if (cont > numArgs.length-1) {
			int[] aux = (int[])numArgs.clone();
			numArgs = new int[cont+50];
			System.arraycopy(aux, 0, numArgs, 0, aux.length);
		}
		numArgs[cont] = args.countArgs();*/
		//cont++;
	}	
	
	public Functions(Program pgm) {
		this(pgm, "default");
	}

	/*public Variable getGlobal(String name) {
		return global.get(name);
	}*/
	
	public Function get(String name) {
		return functions.get(name);
	}
	
	public void set(String name, Function func) {
		functions.put(name,func);
	}
	
	public boolean isEmpty() {
		return functions.isEmpty();
	}

	public String getLibrary() {
		return library;
	}

	public void setLibrary(String library) {
		this.library = library;
	}

	/*public boolean belongs(String cmd) {
		return functions.containsKey(cmd);
	}*/

	public String evaluate(Program pgm, Variables vars, Instruction inst) 
	throws Exception {
		Function fun = get(inst.getCmd());
		if (fun == null) {
			Bit.error(inst,"function "+inst.getCmd()+" not declared");
		}
		return fun.call(inst);
	}

	public Program getProgram() {
		return pgm;
	}

	public boolean isArgsNumberOK(int num) {
		return true;
	}

	public boolean isLazy() {
		return false;
	}	
}
