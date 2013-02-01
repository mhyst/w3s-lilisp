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

import julk.net.w3s.language.*;

public enum FunctionDefinitionBit implements BitCommand {
	call {		
		public String evaluate(Program pgm, Variables vars, Instruction inst) throws Exception {
			Object[] objs = Bit.getObjects(pgm, vars, inst, 1);
			int pos = inst.getCode().indexOf(" ");
			if (pos == -1) Bit.error (inst,"Bad call");
			String code = "("+inst.getCode().substring(pos+1);
			Instruction i = new Instruction(code);
			if (Type.function.isValidValue(objs[0])) {
				return ((Function) objs[0]).call(i);
			} else {
				return pgm.getFunctions().evaluate(pgm, vars, i);
			}
		}		
	},
	getproperty {		
		public String evaluate(Program pgm, Variables vars, Instruction inst) throws Exception {
			Object obj = Bit.getObject(pgm, vars, inst, inst.getArgs().argumentAt(1).getValue());
			String name = inst.getArgs().argumentAt(2).getValue();
			
			Reference ref = null;
			if (Type.function.isValidValue(obj)) {
				ref = ((Function) obj).getProperty(name);
			} else {
				ref = pgm.getFunctions().get(obj.toString()).getProperty(name);
			}
			return pgm.getReferences().newReference(ref);
		}		
	},
	setproperty {		
		public String evaluate(Program pgm, Variables vars, Instruction inst) throws Exception {
			Object[] objs = Bit.getObjects(pgm, vars, inst, 3);
			Object fun = Bit.getObject(pgm, vars, inst, inst.getArgs().argumentAt(1).getValue());
			String name = inst.getArgs().argumentAt(2).getValue();
			Reference ref = new Reference();
			ref.type = Type.determineTheType(objs[0]);
			ref.value = objs[0];
			if (Type.function.isValidValue(fun)) {
				((Function) fun).setProperty(name,ref);
			} else {
				pgm.getFunctions().get(fun.toString()).setProperty(name,ref);
			}
			return pgm.getReferences().newReference(ref);
		}		
	},
	defun {
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
				Bit.error(inst,"defun bad definition");
			}
			pgm.getFunctions().createFunction(fname, fargs, inst, fcode);
			
			//return "defun "+fname;
			return fname;
		}		
	};

	public abstract String evaluate(Program pgm, Variables vars, Instruction inst) throws Exception;

	public boolean isArgsNumberOK(int num) {
		switch (this) {
		case call:
			return num > 0;
		case defun:
			return num == 3;
		case getproperty:
			return num == 2;
		case setproperty:
			return num == 3;
		}
		return false;
	}

	public boolean isLazy() {
		return true;
	}
}
