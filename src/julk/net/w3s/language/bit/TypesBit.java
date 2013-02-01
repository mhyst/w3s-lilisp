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

import julk.net.w3s.Link;
import julk.net.w3s.language.*;

public enum TypesBit implements BitCommand {
	numeric(0, 1, false) {
		public String evaluate(Program pgm, Variables vars, Instruction inst) 
		throws Exception {
			Reference ref = new Reference();
			ref.type = Type.numeric;
			if (inst.countArguments()-1 == 0) {
				ref.value = new Long(0);
			} else {
				ref.value = new Long(inst.getArgs().argumentAt(1).getValue());
			}
			return pgm.getReferences().newReference(ref);
		}
	},
	decimal(0, 1, false) {
		public String evaluate(Program pgm, Variables vars, Instruction inst) 
		throws Exception {
			Reference ref = new Reference();
			ref.type = Type.decimal;
			if (inst.countArguments()-1 == 0) {
				ref.value = new Double(0);
			} else {
				ref.value = new Double(inst.getArgs().argumentAt(1).getValue());
			}
			return pgm.getReferences().newReference(ref);
		}
	},
	logic(0, 1, false) {
		public String evaluate(Program pgm, Variables vars, Instruction inst) 
		throws Exception {
			Reference ref = new Reference();
			ref.type = Type.logic;
			if (inst.countArguments()-1 == 0) {
				ref.value = new Boolean(false);
			} else {
				ref.value = new Boolean(inst.getArgs().argumentAt(1).getValue());
			}
			return pgm.getReferences().newReference(ref);
		}
	},
	character(0, 1, false) {
		public String evaluate(Program pgm, Variables vars, Instruction inst) 
		throws Exception {
			Reference ref = new Reference();
			ref.type = Type.character;
			if (inst.countArguments()-1 == 0) {
				ref.value = ' ';
			} else {
				if (inst.getArgs().argumentAt(1).getValue() != null) {
					ref.value = inst.getArgs().argumentAt(1).getValue().charAt(0);
				} else {
					ref.value = ' ';
				}
			}
			return pgm.getReferences().newReference(ref);
		}
	},
	string(0, 1, false) {
		public String evaluate(Program pgm, Variables vars, Instruction inst) 
		throws Exception {
			Reference ref = new Reference();
			ref.type = Type.string;
			if (inst.countArguments()-1 == 0) {
				ref.value = new String("");
			} else {
				ref.value = inst.getArgs().argumentAt(1).getValue();
			}
			return pgm.getReferences().newReference(ref);
		}
	},
	link(2,3,false) {
		public String evaluate(Program pgm, Variables vars, Instruction inst) 
		throws Exception {
			Reference ref = new Reference();
			ref.type = Type.link;
			String title = inst.getArgs().argumentAt(1).getValue();
			String url = inst.getArgs().argumentAt(2).getValue();
			ref.value = new Link(title, url, null);
			if (inst.countArguments()-1 == 3) {
				String refaddr = inst.getArgs().argumentAt(4).getValue();
				if (!References.isReference(refaddr)) {
					Bit.error(inst, "Third arg should be a reference");
				}
				Reference fref = pgm.getReferences().get(refaddr);
				//TODO: Arreglar este codigo muerto
				if (fref == null) {
					Bit.error(inst, "Third arg should be an existing reference");
				}
				((Link) ref.value).setFather((Link)fref.value);
			}
			return pgm.getReferences().newReference(ref);
		}
	},
	list(1,true) {
		public String evaluate(Program pgm, Variables vars, Instruction inst) throws Exception {
			String lst = inst.getArgs().argumentAt(1).getValue();
			List l = null;
			try {
				l = new List(lst);
			} catch (Throwable t) {
				Bit.error(inst, "Invalid list");
			}
			Reference ref = new Reference();
			ref.type = Type.list;
			ref.value = l;
			return pgm.getReferences().newReference(ref);
		}
	},
	array(1,false) {		
		public String evaluate(Program pgm, Variables vars, Instruction inst) throws Exception {
			Object[] objs = Bit.getObjects(pgm, vars, inst, 1);
			int size=0;
			try {
				size = Integer.parseInt(objs[0].toString());
			} catch (Throwable tw) {
				Bit.error(inst, "Bad arguments to array creation: first argument must be the size");
			}
			Object[] a = new Object[size];
			Reference ref = new Reference();
			ref.type = Type.array;
			ref.value = a;
			return pgm.getReferences().newReference(ref);
		}
	},
	nil(0, 1,false) {		
		public String evaluate(Program pgm, Variables vars, Instruction inst) throws Exception {
			Object[] objs = Bit.getObjects(pgm, vars, inst, 1);
			String type = null;
			try {
				type = (String) objs[0];
			} catch (Throwable tw) {
				//Bit.error(inst, "Bad arguments to nil");
			}
			if (type == null)
				type = "object";
			Reference ref = new Reference();
			ref.type = Type.valueOf(type);
			ref.value = null;
			return pgm.getReferences().newReference(ref);
		}
	},
	deftype(1,false) {		
		public String evaluate(Program pgm, Variables vars, Instruction inst) throws Exception {
			Object[] objs = Bit.getObjects(pgm, vars, inst, 1);
			Variables vs = null;
			try {
				String tname = (String) objs[0];
				vs = pgm.getTypes().newType(tname);
			} catch (Throwable tw) {
				Bit.error(inst, "Bad arguments to get a function reference");
			}
			if (vs == null) {
				Bit.error(inst, "Function doesn't exist");
			}
			Reference ref = new Reference();
			ref.type = Type.type;
			ref.value = vs;
			return pgm.getReferences().newReference(ref);
		}
	},
	clonetype(1,false) {		
		public String evaluate(Program pgm, Variables vars, Instruction inst) throws Exception {
			Object[] objs = Bit.getObjects(pgm, vars, inst, 1);
			Variables vs = null;
			try {
				String tname = (String) objs[0];
				vs = pgm.getTypes().getVars(tname);
			} catch (Throwable tw) {
				Bit.error(inst, "Bad arguments to get a function reference");
			}
			if (vs == null) {
				Bit.error(inst, "Function doesn't exist");
			}
			Reference ref = new Reference();
			ref.type = Type.type;
			ref.value = vs.clone();
			return pgm.getReferences().newReference(ref);
		}
	},
	getmember {		
		public String evaluate(Program pgm, Variables vars, Instruction inst) throws Exception {
			Object obj = Bit.getObject(pgm, vars, inst, inst.getArgs().argumentAt(1).getValue());
			String name = inst.getArgs().argumentAt(2).getValue();
			
			Reference ref = null;
			Variable var = null;
			if (Type.type.isValidValue(obj)) {
				var = ((Variables) obj).get(name);
			} else {
				var = pgm.getTypes().getVars(obj.toString()).get(name);
			}
			if (var == null) {
				Bit.error(inst, "Member <"+name+"> not found");
			}
			
			ref = var.ref;
			//ref.type = Type.determineTheType(ref.value);
			return pgm.getReferences().newReference(ref);
		}		
	},
	setmember {		
		public String evaluate(Program pgm, Variables vars, Instruction inst) throws Exception {
			Object[] objs = Bit.getObjects(pgm, vars, inst, 3);
			Object type = Bit.getObject(pgm, vars, inst, inst.getArgs().argumentAt(1).getValue());
			String name = inst.getArgs().argumentAt(2).getValue();
			Reference ref = new Reference();
			ref.type = Type.determineTheType(objs[0]);
			ref.value = objs[0];
			if (Type.type.isValidValue(type)) {
				((Variables) type).set(name,ref);
			} else {
				pgm.getTypes().getVars(type.toString()).set(name,ref);
			}
			return pgm.getReferences().newReference(ref);
		}		
	},
	function(1,false) {		
		public String evaluate(Program pgm, Variables vars, Instruction inst) throws Exception {
			Object[] objs = Bit.getObjects(pgm, vars, inst, 1);
			Function f = null;
			try {
				String fname = (String) objs[0];
				f = pgm.getFunctions().get(fname);
			} catch (Throwable tw) {
				Bit.error(inst, "Bad arguments to get a function reference");
			}
			if (f == null) {
				Bit.error(inst, "Function doesn't exist");
			}
			Reference ref = new Reference();
			ref.type = Type.function;
			ref.value = f;
			return pgm.getReferences().newReference(ref);
		}
	},
	clonefunction(1,false) {		
		public String evaluate(Program pgm, Variables vars, Instruction inst) throws Exception {
			Object[] objs = Bit.getObjects(pgm, vars, inst, 1);
			Function f = null;
			try {
				String fname = (String) objs[0];
				f = pgm.getFunctions().get(fname);
			} catch (Throwable tw) {
				Bit.error(inst, "Bad arguments to get a function reference");
			}
			if (f == null) {
				Bit.error(inst, "Function doesn't exist");
			}
			Reference ref = new Reference();
			ref.type = Type.function;
			ref.value = f.clone();
			return pgm.getReferences().newReference(ref);
		}
	},
	file(1,false) {		
		public String evaluate(Program pgm, Variables vars, Instruction inst) throws Exception {
			Object[] objs = Bit.getObjects(pgm, vars, inst, 1);
			File f = null;
			try {
				String filename = (String) objs[0];
				f = new File(filename);
			} catch (Throwable tw) {
				Bit.error(inst, "Bad arguments to file creation: first argument must be the filename");
			}
			if (f == null) {
				Bit.error(inst, "File creation failed");
			}
			Reference ref = new Reference();
			ref.type = Type.file;
			ref.value = f;
			return pgm.getReferences().newReference(ref);
		}
	},
	set(2, false) {
		public String evaluate(Program pgm, Variables vars, Instruction inst) 
		throws Exception {
			String name = inst.getArgs().argumentAt(1).getValue();
			//String value = inst.getArgs().argumentAt(2).getValue();
			//Reference ref = null;
			Reference refs[] = Bit.getReferences(pgm, vars, inst, 2);
			Reference ref = refs[0];

			
			//ref = new Reference();
			//ref.value = Bit.getObject(pgm,vars,inst,value);
			//ref.type = Type.determineTheType(ref.value);
			//if (ref == null) {
			//	Bit.error(inst,"Invalid reference "+value);
			//}
			ref.addVariable(vars.set(name, ref));
			//return pgm.getReferences().newReference(ref);
			return ref.address;
		}
	},
	isset(1, false) {
		public String evaluate(Program pgm, Variables vars, Instruction inst) 
		throws Exception {
			String name = inst.getArgs().argumentAt(1).getValue();
			Variable var = vars.get(name);
			if (var == null) {
				//Bit.error(inst,"Variable "+name+" not set");
				return "false";
			}
			return "true";
		}
	},
	unset(1, false) {
		public String evaluate(Program pgm, Variables vars, Instruction inst) 
		throws Exception {
			String name = inst.getArgs().argumentAt(1).getValue();
			Reference ref = vars.removeVariable(name);
			if (ref == null) {
				Bit.error(inst,"Variable "+name+" not set");
			}
			pgm.getReferences().clean();
			return ref.address;
		}
	};


	private int numArgs;
	private int alternativeNumArgs;
	private boolean lazy;
	
	private TypesBit(int numArgs, int alternativeNumArgs, boolean lazy) {
		this.numArgs = numArgs;
		this.alternativeNumArgs = alternativeNumArgs;
		this.lazy = lazy;
	}
		
	private TypesBit(int numArgs, boolean lazy) {
		this(numArgs, -1, lazy);
	}

	/*private TypesBit(int numArgs) {
		this(numArgs, -1, false);
	}*/

	private TypesBit() {
		this(-1, -1, false);
	}

	public boolean isLazy() {return lazy;}
	
	public boolean isArgsNumberOK(int num) {
		if (numArgs == -1) return true;
		if (num == numArgs) return true;
		if (num == alternativeNumArgs) return true;
		return false;
	}
	
	public abstract String evaluate(Program pgm, Variables vars, Instruction inst) throws Exception;

	public int getAlternativeNumArgs() {
		return alternativeNumArgs;
	}

	public int getNumArgs() {
		return numArgs;
	}
}
