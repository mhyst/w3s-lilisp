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

package julk.net.w3s.language.bit;

import julk.net.w3s.language.Arguments;
import julk.net.w3s.language.Instruction;
import julk.net.w3s.language.Program;
import julk.net.w3s.language.Variables;

public enum MacrosDefinitionBit implements BitCommand {
	defmacro {
		public String evaluate(Program pgm, Variables vars, Instruction inst) throws Exception {
			String fname = inst.getArgs().argumentAt(1).getValue();
			Instruction aux = null;
			Arguments fargs = null;
			try {
				aux = new Instruction(inst.getArgs().argumentAt(2).getValue());
				fargs = aux.convertToArguments();
			} catch (Exception e) {
				//error(inst,"defun error: "+e.getMessage());
				//return "defun error: "+e.getMessage();
				fargs = new Arguments();
				//return "";
			}
			
			String fcode = inst.getArgs().argumentAt(3).getValue();
			if(fcode.charAt(0) != '(' || fcode.charAt(fcode.length()-1) != ')') {
				Bit.error(inst,"defmacro bad definition");
			}
			pgm.getMacros().createMacro(fname, fargs, inst, fcode);
			
			//return "defun "+fname;
			return fname;
		}		
	};

	public abstract String evaluate(Program pgm, Variables vars, Instruction inst) throws Exception;

	public boolean isArgsNumberOK(int num) {
		switch (this) {
		case defmacro:
			return num == 3;
		}
		return false;
	}

	public boolean isLazy() {
		return true;
	}
}