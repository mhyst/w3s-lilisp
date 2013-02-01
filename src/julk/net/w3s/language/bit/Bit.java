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
import java.util.Vector;

public enum Bit {
	CONTROL {public BitCommand getInternalBit(String cmd) {return FlowControlBit.valueOf(cmd);}},
	TYPES {public BitCommand getInternalBit(String cmd) {return TypesBit.valueOf(cmd.toLowerCase());}},
	INCLUDES {public BitCommand getInternalBit(String cmd) {return IncludeBit.valueOf(cmd.toLowerCase());}},
	IO {public BitCommand getInternalBit(String cmd) {return IOBit.valueOf(cmd.toLowerCase());}},
	OPERATORS {public BitCommand getInternalBit(String cmd) {return OperatorBit.getOperatorByCode(cmd);}},
	ARRAYS {public BitCommand getInternalBit(String cmd) {return ArraysBit.valueOf(cmd.toLowerCase());}},
	LISTS {public BitCommand getInternalBit(String cmd) {return ListBit.valueOf(cmd.toLowerCase());}},
	FILES {public BitCommand getInternalBit(String cmd) {return FilesBit.valueOf(cmd.toLowerCase());}},
	OBJHDLR {public BitCommand getInternalBit(String cmd) {return ObjectsHandlerBit.valueOf(cmd.toLowerCase());}},
	STRINGOPS {public BitCommand getInternalBit(String cmd) {return StringOperatorsBit.valueOf(cmd.toLowerCase());}},
	DEFMACRO {public BitCommand getInternalBit(String cmd) {return MacrosDefinitionBit.valueOf(cmd.toLowerCase());}},
	DEFUN {public BitCommand getInternalBit(String cmd) {return FunctionDefinitionBit.valueOf(cmd.toLowerCase());}};

	public abstract BitCommand getInternalBit(String cmd);
	public BitCommand getBit(String cmd) {
		try {
			return getInternalBit(cmd);
		} catch (Exception e) {
			return null;
		}
	}
	
	public static BitCommand locateBit (Instruction inst) {
		BitCommand btCmd = null;
		for(Bit bit : Bit.values()) {
			btCmd = bit.getBit(inst.getCmd().toUpperCase());
			if (btCmd != null) break;
		}
		return btCmd;
	}

	public static void error(Instruction inst, String msg) throws Exception {
		System.out.println("ERROR (file: "+inst.getFile()+" line: "+inst.getLine()+")> "+msg+" "+inst.getCode());
		throw new CodeException(msg);
	}
	
	public static void signalError(Instruction inst, String msg) {
		System.out.println("ERROR (file: "+inst.getFile()+" line: "+inst.getLine()+")> "+msg+" "+inst.getCode());
	}
	
	public static String evaluateBit (Program pgm, Variables vars, Instruction inst) 
	throws Exception {
		BitCommand btCmd = Bit.locateBit(inst);
		//valid cmd?
		if (btCmd == null) {
			error(inst,"Bad command.");
		}
		//Valid arguments number?
		if (!btCmd.isArgsNumberOK(inst.countArguments()-1)) {
			error(inst,"Arguments number doesn't match");
		}
		//For now I skip type control
		return btCmd.evaluate(pgm, vars, inst);
	}
	
	public static String evaluate(Program pgm, Variables var, Instruction orig, String code, boolean loop)
	throws Exception {
		try {
			if (code.startsWith("((")) {
				return pgm.execute(code, orig, loop);
			} else {
				return pgm.evaluate(new Instruction(code));
			}
		} catch (ContinueException ce) {
			return "continue";
		}
	}
	
	public static String getValue(Program pgm, Variables vars, Instruction orig, String value) 
	throws Exception {
		if (Variables.isVariable(value)) {
			Variable var = pgm.getVar(value);
			if (var == null) {
				Bit.error(orig,"Variable "+value+" not found");
			}
			return getValue(pgm, vars, orig, var.ref.value.toString());
		} else if (References.isReference(value)) {
			Reference ref = pgm.getReferences().get(value);
			if (ref == null) {
				Bit.error(orig, "Reference "+value+" not found");
			}
			return getValue(pgm, vars, orig, ref.value.toString());
		} else {
			return value.toString();
		}
	}
	
	public static String[] getValues(Program pgm, Variables vars, Instruction inst, int first) 
	throws Exception {
		int nargs = inst.countArguments();
		String[] values = new String[nargs-first];
		for (int i = first; i < nargs; i++) {
			Argument arg = inst.getArgs().argumentAt(i);
			if (arg.isQuoted())
				values[i-first] = arg.getValue();
			else
				values[i-first] = Bit.getValue(pgm, vars, inst, arg.getValue());
		}
		return values;
	}
	
	public static Object getObject(Program pgm, Variables vars, Instruction orig, String value) 
	throws Exception {	
		if (Variables.isVariable(value)) {
			Variable var = pgm.getVar(value);
			if (var == null) {
				Bit.error(orig,"Variable "+value+" not found");
			}
			if (var.ref.value == null) return null;
			if (var.ref.value.getClass().getName().equals("java.lang.String")) {
				return getObject(pgm, vars, orig, var.ref.value.toString());
			} else {
				return var.ref.value;
			}
		} else if (References.isReference(value)) {
			Reference ref = pgm.getReferences().get(value);
			if (ref == null) {
				Bit.error(orig, "Reference "+value+" not found");
			}
			if (ref.value == null) return null;
			if (ref.value.getClass().getName().equals("java.lang.String")) {
				return getObject(pgm, vars, orig, ref.value.toString());
			} else {
				return ref.value;
			}
		} else {
			return value;
		}
		
	}
	
	public static Object[] getObjects(Program pgm, Variables vars, Instruction inst, int first) 
	throws Exception {
		int nargs = inst.countArguments();
		Object[] values = new Object[nargs-first];
		for (int i = first; i < nargs; i++) {
			Argument arg = inst.getArgs().argumentAt(i);
			if(arg.isQuoted())
				values[i-first] = arg.getValue();
			else
				values[i-first] = Bit.getObject(pgm, vars, inst, arg.getValue());
		}
		return values;
	}
	
	private static Vector<Reference> genRefs = new Vector<Reference>();
	
	private static Reference getGeneratedReference(Object value) {
		for (Reference ref : genRefs) {
			if (ref.value.toString().equalsIgnoreCase(value.toString())) {
				return ref;
			}
		}
		return null;
	}
	
	public static Reference getReference(Program pgm, Variables vars, Instruction orig, String value) 
	throws Exception {	
		if (Variables.isVariable(value)) {
			Variable var = pgm.getVar(value);
			if (var == null) {
				Bit.error(orig,"Variable "+value+" not found");
			}
			return var.ref;
		} else if (References.isReference(value)) {
			Reference ref = pgm.getReferences().get(value);
			if (ref == null) {
				Bit.error(orig, "Reference "+value+" not found");
			}
			return ref;
		} else {
			//Reference ref = pgm.getReferences().getReferenceByValue(value);
			Reference ref = getGeneratedReference(value);
			if (ref == null) {
				ref = new Reference();
				ref.type = Type.determineTheType(value);
				ref.value = ref.type.getObject(value, null);
				pgm.getReferences().newReference(ref);
				genRefs.addElement(ref);
			}
			return ref;
		}
	}

	public static Reference[] getReferences(Program pgm, Variables vars, Instruction inst, int first) 
	throws Exception {
		int nargs = inst.countArguments();
		Reference[] values = new Reference[nargs-first];
		for (int i = first; i < nargs; i++) {
			Argument arg = inst.getArgs().argumentAt(i);
			if(arg.isQuoted()) {
				Reference ref = new Reference();
				ref.type = Type.string;
				ref.value = arg.getValue();
				pgm.getReferences().newReference(ref);
				values[i-first] = ref;
			} else
				values[i-first] = Bit.getReference(pgm, vars, inst, arg.getValue());
		}
		return values;
	}	
}
