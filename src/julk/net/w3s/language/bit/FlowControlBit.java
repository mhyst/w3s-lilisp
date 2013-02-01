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
import julk.net.w3s.LinksManager;
import julk.net.w3s.language.*;

public enum FlowControlBit implements BitCommand {
	DEBUG_ON(0,false) {
		public String evaluate(Program pgm, Variables vars, Instruction inst) 
		throws Exception {
			pgm.setDebug(true);
			return "debug: on";
		}
	},
	DEBUG_OFF(0,false) {
		public String evaluate(Program pgm, Variables vars, Instruction inst) 
		throws Exception {
			pgm.setDebug(false);
			return "debug: off";
		}
	},
	IF(2,3,true) {
		public String evaluate(Program pgm, Variables vars, Instruction inst) 
		throws Exception {
			String result = "";
			if (conditionTrue(pgm,inst.getArgs().argumentAt(1).getValue(),inst)) {
				//String doIf = sargs[1].substring(1,sargs[1].length()-1);
				String doIf = inst.getArgs().argumentAt(2).getValue();
				if (doIf.charAt(0) != '(') {
					result = doIf;
				} else {
					result = pgm.execute(doIf, inst, true);
				}
			} else  {
				if (inst.getArgs().countArgs()-1 == 3) {
					//String doElse = sargs[2].substring(1,sargs[2].length()-1);
					String doElse = inst.getArgs().argumentAt(3).getValue();
					if (doElse.charAt(0) != '(') {
						result = doElse;
					} else {
						result = pgm.execute(doElse, inst, true);
					}
				}	
			}
			return result;
		}
	},
	FOR(4,true) {
		public String evaluate(Program pgm, Variables vars, Instruction inst)
		throws Exception {
			String initialization = inst.getArgs().argumentAt(1).getValue();
			String condition = inst.getArgs().argumentAt(2).getValue();
			String increment = inst.getArgs().argumentAt(3).getValue();
			//code = sargs[3].substring(1,sargs[3].length()-1);
			String code = inst.getArgs().argumentAt(4).getValue();
			//FOR Initialization
			try {
				pgm.evaluate(new Instruction(inst.getFile(), inst.getLine()-1, initialization));
			} catch (Exception e1) {
				Bit.error(inst,"FOR: wrong condition");
			}
			String result = "";
			//FOR Condition evaluation
			try {
				while (conditionTrue(pgm,condition,inst)) {
					//FOR Execution
					//result = Bit.evaluate(pgm, vars, inst, code, true);
					try {
						result = pgm.execute(code, inst, true);
					} catch (ContinueException ce) {
					}
					/*if (result.equalsIgnoreCase("return") ||
						result.equalsIgnoreCase("break")) {
							break;
					}*/
					//FOR Icrement
					try {
						pgm.evaluate(new Instruction(inst.getFile(), inst.getLine()-1, increment));
					} catch (Exception e1) {
						Bit.error(inst,"FOR: wrong increment");
					}
				}
			} catch (BreakException be) {
				result = "break";
				return result;
			}
			return "";
		}
	}, 
	FORTIMES(4,true) {
		public String evaluate(Program pgm, Variables vars, Instruction inst)
		throws Exception {
			Object[] objs = Bit.getObjects(pgm, vars, inst, 2);
			int init = 0;
			try {
				init = (Integer) objs[0];
			} catch (Throwable t) {
				try {
					init = Integer.parseInt(objs[0].toString());
				} catch (Throwable t2) {
					Bit.error(inst, "First argument must be the initial value for the loop");
				}
			}
			int times = 0;
			try {
				times = (Integer) objs[1];
			} catch (Throwable t) {
				try {
					times = Integer.parseInt(objs[1].toString());
				} catch (Throwable t2) {
					Bit.error(inst, "First argument must be the number of times the loop will be executed");
				}
			}
			String varname = inst.getArgs().argumentAt(1).getValue();
			String code = inst.getArgs().argumentAt(4).getValue();

			Reference refid = new Reference();
			refid.type = Type.numeric;
			pgm.getReferences().newReference(refid);
			vars.set(varname, refid);

			String result = "";
			//FOR Condition evaluation
			try {
				int i = init;
				while (i < times) {
					refid.value = i;
					//FOR Execution
					try {
						result = pgm.execute(code, inst, true);
					} catch (ContinueException ce) {
					}
					//FOR Icrement
					i++;
				}
			} catch (BreakException be) {
				result = "break";
				return result;
			}
			return "";
		}
	}, 
	FOREACH(3,true){
		@SuppressWarnings("unchecked")
		public String evaluate(Program pgm, Variables vars, Instruction inst)
		throws Exception {
			Object[] objs = Bit.getObjects(pgm, vars, inst, 2);
			
			if (objs[0].toString().startsWith("(") && !objs[0].getClass().getName().equals("julk.net.w3s.language.List")) {
				String refaddr = pgm.evaluate(new Instruction(objs[0].toString()));
				Reference ref = pgm.getReferences().get(refaddr);
				objs[0] = ref.value;
			}
			if (Type.array.isValidValue(objs[0])) {
				return foreachArray(pgm,vars,inst,objs);
			} else if (Type.links.isValidValue(objs[0])) {
				return foreachLinks(pgm,vars,inst,objs);
			/*} else if (Type.list.isValidValue(objs[0])) {
				return foreachList(pgm,vars,inst,objs);*/
			} else {
				Iterable<Object> i = null;
				try {
					i = (Iterable<Object>) objs[0];
				} catch (Throwable t) {
					Bit.error(inst, "The foreach loop is valid only for arrays and lists");
				}
				return foreachIterable(pgm,vars,inst,i);
			}
		}
	}, 
	WHILE(2,true) {
		public String evaluate(Program pgm, Variables vars, Instruction inst)
		throws Exception {
			String condition = inst.getArgs().argumentAt(1).getValue();
			String code = inst.getArgs().argumentAt(2).getValue();
			String result = "";
			//WHILE Condition evaluation
			try {
				while (conditionTrue(pgm,condition,inst)) {
					//WHILE Execution
					//result = Bit.evaluate(pgm, vars, inst, code, true);
					try {
						result = pgm.execute(code, inst, true);
					} catch (ContinueException ce) {
					}
					/*if (result.equalsIgnoreCase("return") ||
						result.equalsIgnoreCase("break")) {
						break;
					}*/
				}
			} catch (BreakException be) {
				result = "break";
				return result;
			}
			return "";
		}
	},
	UNTILFILEEND(2,true) {
		public String evaluate(Program pgm, Variables vars, Instruction inst)
		throws Exception {
			Object[] objs = Bit.getObjects(pgm, vars, inst, 1);
			String filevar = inst.getArgs().argumentAt(1).getValue();
			String code = inst.getArgs().argumentAt(2).getValue();
			String result = "";
			Reference refchar = new Reference();
			refchar.type = Type.numeric;
			pgm.getReferences().newReference(refchar);
			vars.set(filevar+".byte", refchar);
			
			File f = null;
			try {
				f = (File) objs[0];
				f.exists();
			} catch (Throwable t) {
				Bit.error(inst, "Second argument must be an open file (for read)");
			}
			//WHILE Condition evaluation
			try {
				int c;
				long free1, free2, mem;
				Runtime r = Runtime.getRuntime();
				free1 = r.freeMemory();
				while ((c = f.read())!= -1) {
					refchar.value = c;
					//WHILE Execution
					
					try {
						result = pgm.execute(code, inst, true);
					} catch (ContinueException ce) {
					}
					free2 = r.freeMemory();
					mem = free1 - free2;
					if (mem > 8388608) {
						System.gc();
						free1 = r.freeMemory();
					}
					//System.out.println ("Se ha consumido: "+mem+" bytes de memoria");
				}
			} catch (BreakException be) {
				result = "break";
				return result;
			}
			return "";
		}
	},
	DO(2,true) {
		public String evaluate(Program pgm, Variables vars, Instruction inst)
		throws Exception {
			String condition = inst.getArgs().argumentAt(2).getValue();
			String code = inst.getArgs().argumentAt(1).getValue();
			String result = "";
			//WHILE Condition evaluation			
			try {
				do {
					//WHILE Execution
					//result = Bit.evaluate(pgm, vars, inst, code, true);
					try {
						result = pgm.execute(code, inst, true);
					} catch (ContinueException ce) {
					}
					/*if (result.equalsIgnoreCase("return") ||
						result.equalsIgnoreCase("break")) {
						break;
					}*/
				} while (conditionTrue(pgm,condition,inst));
			} catch (BreakException be) {
				result = "break";
				return result;
			}
			return "";
		}
	},
	THREAD (1,true) {
		public String evaluate(Program pgm, Variables vars, Instruction inst) 
		throws Exception {
			//String result = "";
			String code = inst.getArgs().argumentAt(1).getValue();
			SeparateThread st = new SeparateThread(pgm, inst, code);
			new Thread(st).start();
			return "";
		}
	},
	TRY (1,true) {
		public String evaluate(Program pgm, Variables vars, Instruction inst) 
		throws Exception {
			//String result = "";
			String code = inst.getArgs().argumentAt(1).getValue();
			try {
				pgm.execute(code, inst, true);
				return "true";
			}catch (BreakException be) {
				throw be;
			}catch (ContinueException ce) {
				throw ce;
			}catch (ReturnException re) {
				throw re;
			}catch (ThrowException te) {
				int level = 0;
				try {
					level = Integer.parseInt(te.getMessage());
				} catch (Exception te2) {}
				if (level > 0) {
					level--;
					throw te;
				} else {
					return "false";
				}
			}catch (CodeException codee) {
				//throw codee;
				//Bit.signalError(inst, codee.getMessage());
				return "false";
			}catch (Exception e) {
				//Bit.signalError(inst, e.getMessage());
				return "false";
			}
			//return result;
		}
	},
	RAISE {
		public String evaluate(Program pgm, Variables vars, Instruction inst)
		throws Exception {
			Object[] objs = Bit.getObjects(pgm, vars, inst, 1);
			if (objs.length > 0)
				throw new ThrowException(objs[0].toString());
			else
				throw new ThrowException("0");
		}
	},
	BREAK {
		public String evaluate(Program pgm, Variables vars, Instruction inst)
		throws Exception {
			throw new BreakException("break");
		}
	},
	CONTINUE {
		public String evaluate(Program pgm, Variables vars, Instruction inst)
		throws Exception {
			throw new ContinueException("continue");
		}
	},
	RETURN(1) {		
		public String evaluate(Program pgm, Variables vars, Instruction inst)
		throws Exception {
			String name = inst.getArgs().argumentAt(1).getValue();
			String returnVal = "";
			if (Variables.isVariable(name)) {
				returnVal = vars.get(name).ref.address;
			} else if (References.isReference(name)) {
				returnVal = name;
			} else {
				returnVal = name;
			}
			throw new ReturnException(returnVal);
		}
	};

	private int numArgs;
	private int alternativeNumArgs;
	private boolean lazy;
	
	private FlowControlBit(int numArgs, int alternativeNumArgs, boolean lazy) {
		this.numArgs = numArgs;
		this.alternativeNumArgs = alternativeNumArgs;
		this.lazy = lazy;
	}
		
	private FlowControlBit(int numArgs, boolean lazy) {
		this(numArgs, -1, lazy);
	}

	private FlowControlBit(int numArgs) {
		this(numArgs, -1, false);
	}

	private FlowControlBit() {
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

	public void setAlternativeNumArgs(int alternativeNumArgs) {
		this.alternativeNumArgs = alternativeNumArgs;
	}

	public int getNumArgs() {
		return numArgs;
	}

	public void setNumArgs(int numArgs) {
		this.numArgs = numArgs;
	}
	
	private static boolean conditionTrue(Program pgm, String condition, Instruction inst) throws Exception {
		//Simple condition 
		if (condition.charAt(0) != '(') {
			Type t = Type.determineTheType(condition);
			switch (t) {
			case numeric:
				return ((Long) t.getObject(condition, null)).longValue() > 0;
			case decimal:
				return ((Integer) t.getObject(condition, null)).intValue() > 0;
			case logic:
				return ((Boolean) t.getObject(condition, null)).booleanValue();
			default:
				return false;
			}		
		}
		//Complex condition
		Argument arg;
		try {
			arg = new Argument(pgm.evaluate(new Instruction(inst.getFile(), inst.getLine()-1, condition)));
			return conditionTrue(pgm,arg.getValue(),inst);
		} catch (Exception e) {
			Bit.error(inst,"CONTROL: wrong condition: ");
			e.printStackTrace();
			return false;
		}
	}
	
	public String foreachArray(Program pgm, Variables vars, Instruction inst, Object[] objs)
	throws Exception {
		//Object[] objs = Bit.getObjects(pgm, vars, inst, 2);
		//the variable name
		String varname = inst.getArgs().argumentAt(1).getValue();
		Object[] set = null;
		try {
			//The array
			set = (Object[]) objs[0];
		} catch (Throwable tb) {
			Bit.error(inst, "Second argument must be an array");
		}
		
		//Is a variable
		if (!Variables.isVariable(varname))
			Bit.error(inst, "First argument must be a variable name");
		
		Reference ref = new Reference();
		ref.type = Type.determineTheType(set[0]);
		vars.set(varname, ref);
		String result = "";
		String code = inst.getArgs().argumentAt(3).getValue();
		int id = 0;
		//Auxiliary help
		//$var.id
		Reference refid = new Reference();
		refid.type = Type.numeric;
		refid.value = id;
		pgm.getReferences().newReference(refid);
		vars.set(varname+".id", refid);
		//$array.length
		/*Reference reflen = new Reference();
		reflen.type = Type.numeric;
		reflen.value = set.length;
		vars.set(varname+".length", reflen);*/
		try {
			for (Object o : set){
				ref.value = o;
				refid.value = id;
				try {
					result = pgm.execute(code, inst, true);
				} catch (ContinueException ce) {
				}
				id++;
			}
		} catch (BreakException be) {
			result = "break";
		}
		return result;
	}

	public String foreachList(Program pgm, Variables vars, Instruction inst, Object[] objs)
	throws Exception {
		//Object[] objs = Bit.getObjects(pgm, vars, inst, 2);
		//the variable name
		String varname = inst.getArgs().argumentAt(1).getValue();
		List set = null;
		try {
			//The array
			set = (List) objs[0];
		} catch (Throwable tb) {
			Bit.error(inst, "Second argument must be a list");
		}
		
		//Is a variable
		if (!Variables.isVariable(varname))
			Bit.error(inst, "First argument must be a variable name");
		
		Reference ref = new Reference();
		ref.type = Type.string;
		vars.set(varname, ref);
		String result = "";
		String code = inst.getArgs().argumentAt(3).getValue();
		int id = 0;
		//Auxiliary help
		//$var.id
		Reference refid = new Reference();
		refid.type = Type.numeric;
		refid.value = id;
		pgm.getReferences().newReference(refid);
		vars.set(varname+".id", refid);
		//$array.length
		/*Reference reflen = new Reference();
		reflen.type = Type.numeric;
		reflen.value = set.length;
		vars.set(varname+".length", reflen);*/
		try {
			String head = "";
			while ((head = set.head()) != null) { 
				ref.value = head;
				refid.value = id;
				try {
					result = pgm.execute(code, inst, true);
				} catch (ContinueException ce) {
				}
				id++;
				set = set.tail();
			}
		} catch (BreakException be) {
			result = "break";
		}
		return result;
	}

	public String foreachLinks(Program pgm, Variables vars, Instruction inst, Object[] objs)
	throws Exception {
		//Object[] objs = Bit.getObjects(pgm, vars, inst, 2);
		//the variable name
		String varname = inst.getArgs().argumentAt(1).getValue();
		LinksManager set = null;
		try {
			//The array
			set = (LinksManager) objs[0];
		} catch (Throwable tb) {
			Bit.error(inst, "Second argument must be an links object");
		}
		
		//Is a variable
		if (!Variables.isVariable(varname))
			Bit.error(inst, "First argument must be a variable name");
		
		Reference ref = new Reference();
		ref.type = Type.links;
		vars.set(varname, ref);
		String result = "";
		String code = inst.getArgs().argumentAt(3).getValue();
		int id = 0;
		//Auxiliary help
		//$var.id
		Reference refid = new Reference();
		refid.type = Type.numeric;
		refid.value = id;
		pgm.getReferences().newReference(refid);
		vars.set(varname+".id", refid);
		//$array.length
		/*Reference reflen = new Reference();
		reflen.type = Type.numeric;
		reflen.value = set.length;
		vars.set(varname+".length", reflen);*/
		try {
			Link lnk = null;
			while ((lnk = set.next()) != null) { 
				ref.value = lnk;
				refid.value = id;
				try {
					result = pgm.execute(code, inst, true);
				} catch (ContinueException ce) {
				}
				id++;
			}
		} catch (BreakException be) {
			result = "break";
		}
		return result;
	}

	public String foreachIterable(Program pgm, Variables vars, Instruction inst, Iterable<Object> it)
	throws Exception {
		//Object[] objs = Bit.getObjects(pgm, vars, inst, 2);
		//the variable name
		String varname = inst.getArgs().argumentAt(1).getValue();
		
		//Is a variable
		if (!Variables.isVariable(varname))
			Bit.error(inst, "First argument must be a variable name");
		
		Reference ref = new Reference();
		vars.set(varname, ref);
		String result = "";
		String code = inst.getArgs().argumentAt(3).getValue();

		try {
			for(Object o : it) {
				ref.type = Type.determineTheType(o);
				ref.value = o;
				try {
					result = pgm.execute(code, inst, true);
				} catch (ContinueException ce) {
				}
			}
		} catch (BreakException be) {
			result = "break";
		}
		return result;
	}
}
