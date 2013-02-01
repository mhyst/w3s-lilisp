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

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import julk.net.w3s.language.*;

public enum ObjectsHandlerBit implements BitCommand {
	method {
		public String evaluate(Program pgm, Variables vars, Instruction inst) throws Exception {
			Object[] iniobjs = Bit.getObjects(pgm, vars, inst, 1);
			Class c = null;
			try {
				c = iniobjs[0].getClass();
			} catch (Exception e) {
				Bit.error(inst, "First argument must be an object");
			}
			
			if (c == null) {
				Bit.error(inst, "First argument must be an existant object");
			}
			Method[] ms = null;
			Method m = null;
			Class[] cargs = null;
			Object resultObj = null;
			
			/*Object obj = null;
			Variable var = null;
			Class[] cargs = null;
			Object resultObj = null;
			String query = null;
			String result = "";
			Field f;*/

			Object[] objs = Bit.getObjects(pgm, vars, inst, 3);
			String query = inst.getArgs().argumentAt(2).getValue();
			try {
				//Class[] types = new Class[1];
				//types[0] = Integer.class;
				//ms = c.getDeclaredMethods();
				ms = c.getMethods();
				for (int i = 0; i < ms.length; i++) {
					cargs = ms[i].getParameterTypes();
					if (query.equalsIgnoreCase(ms[i].getName()) &&
							cargs.length == objs.length &&
							validMethod(cargs, objs)) {
						m = ms[i];
						break;
					}
				}
			} catch (Exception e) {
				Bit.error(inst,"The method "+query+" doesn't exist");
			}
			
			if (m == null)
				Bit.error(inst, "method "+query+" not found");
			//cargs = m.getParameterTypes();
			//falta extrart los argumentos

			try {
				if (cargs == null || cargs.length== 0) {
					resultObj = m.invoke(iniobjs[0], (Object[]) null);
				} else {
					if (cargs.length > objs.length)
						Bit.error(inst, "Wrong number of arguments");
					resultObj = m.invoke(iniobjs[0], objs);
				}
			} catch (Exception e) {
				Bit.signalError(inst,"Method call failed "+e.toString());
				throw e;
			}
			
			//if (resultObj != null) {
			if (!m.getReturnType().equals(void.class)) {
				Reference ref = new Reference();
				ref.type = Type.determineTheType(resultObj);
				ref.value = resultObj;
				return pgm.getReferences().newReference(ref);
			} else {
				return "";
			}
		}
	},
	staticmethod {
		public String evaluate(Program pgm, Variables vars, Instruction inst) throws Exception {
			Object[] objs = Bit.getObjects(pgm, vars, inst, 3);
			Class c = null;
			Method[] ms = null;
			Method m = null;
			Class[] cargs = null;
			Object resultObj = null;
			Object obj = null;
			
			String query = inst.getArgs().argumentAt(2).getValue();
			try {
				c = Class.forName(inst.getArgs().argumentAt(1).getValue());
			} catch (Exception e) {
				Bit.error (inst, "Class "+inst.getArgs().argumentAt(1).getValue()+" not found");
			}
			try {
				//Class[] types = new Class[1];
				//types[0] = Integer.class;
				//ms = c.getDeclaredMethods();
				ms = c.getMethods();
				
				for (int i = 0; i < ms.length; i++) {
					cargs = ms[i].getParameterTypes();
					if (query.equalsIgnoreCase(ms[i].getName()) &&
						cargs.length == objs.length &&
						validMethod(cargs, objs)) {
						m = ms[i];
						break;
					}
				}
			} catch (Exception e) {
				Bit.error(inst,"The method "+query+" doesn't exist");
			}
			
			if (m == null)
				Bit.error(inst, "static method "+query+" not found");

			//cargs = m.getParameterTypes();
			//falta extrart los argumentos

			try {
				if (cargs == null || cargs.length== 0) {
					resultObj = m.invoke(obj, (Object[]) null);
				} else {
					if (cargs.length > objs.length)
						Bit.error(inst, "Wrong number of arguments");
					resultObj = m.invoke(obj, objs);
				}
			} catch (Exception e) {
				Bit.signalError(inst,"Method call failed - "+e.toString());
				throw e;
			}
			//if (resultObj != null) {
			if (!m.getReturnType().equals(void.class)) {
				Reference ref = new Reference();
				ref.type = Type.determineTheType(resultObj);
				ref.value = resultObj;
				return pgm.getReferences().newReference(ref);
			} else {
				return "";
			}
		}
	},
	getfield {
		public String evaluate(Program pgm, Variables vars, Instruction inst) throws Exception {
			Object[] objs = Bit.getObjects(pgm, vars, inst, 1);
			Class c = null;
			try {
				c = objs[0].getClass();
			} catch (Exception e) {
				Bit.error(inst, "First argument must be an object");
			}
			
			if (c == null) {
				Bit.error(inst, "First argument must be an existant object");
			}
			Object resultObj = null;
			//Object obj = null;
			Field fd = null;
			String query = inst.getArgs().argumentAt(2).getValue();
			try {
				fd = c.getField(query);
			} catch (Exception e) {
				Bit.error(inst, "The field "+query+" doesn't exist");
			}
			resultObj = fd.get(objs[0]);
			
			//if (resultObj != null) {
				Reference ref = new Reference();
				ref.type = Type.determineTheType(resultObj);
				ref.value = resultObj;
				return pgm.getReferences().newReference(ref);
			/*} else {
				return "";
			}*/
		}
	},
	setfield {
		public String evaluate(Program pgm, Variables vars, Instruction inst) throws Exception {
			Object[] objs = Bit.getObjects(pgm, vars, inst, 1);
			Class c = null;
			try {
				c = objs[0].getClass();
			} catch (Exception e) {
				Bit.error(inst, "First argument must be an object");
			}
			
			if (c == null) {
				Bit.error(inst, "First argument must be an existant object");
			}
			Field fd = null;
			//String query = inst.getArgs().argumentAt(2).getValue();
			String query = objs[1].toString();
			if (objs.length == 0) {
				Bit.error(inst,"Second argument must be an object reference");
			}
			
			try {
				fd = c.getField(query);
			} catch (Exception e) {
				Bit.error(inst, "The field "+query+" doesn't exist");
			}
			fd.set(objs[0], objs[2]);
			return "";
		}
	},
	create {
		public String evaluate(Program pgm, Variables vars, Instruction inst) throws Exception {
			Object[] objs = Bit.getObjects(pgm, vars, inst, 2);
			Class c = null;
			String clase = inst.getArgs().argumentAt(1).getValue();
			try {
				c = Class.forName(clase);
			} catch (Exception e) {
				Bit.error (inst, "Class "+clase+" not found");
			}
	
			if (c == null) {
				Bit.error(inst, "First argument must be an existant object");
			}
			Class[] cargs = null;
			Object resultObj = null;
			
			//Get class
			Constructor[] csts = null;
			Constructor cst = null;
			try {
				csts = c.getConstructors();
				for (int i = 0; i < csts.length; i++) {
					cargs = csts[i].getParameterTypes();
					if ((cargs.length == objs.length) &&
						 validMethod(cargs, objs)) {
						cst = csts[i];
						break;
					}
				}
			} catch (Exception e) {
				Bit.error (inst, "Conctructor not found");
			}
			try {
				if (cargs == null || cargs.length== 0) {
					resultObj = cst.newInstance((Object[]) null);
				} else {
					if (cargs.length > objs.length)
						Bit.error(inst, "Wrong number of arguments");
					resultObj = cst.newInstance(objs);
				}
			} catch (Exception e) {
				Bit.signalError(inst,"Constructor call failed "+e.toString());
				throw e;
			}
			//if (resultObj != null) {
				Reference ref = new Reference();
				ref.type = Type.determineTheType(resultObj);
				ref.value = resultObj;
				return pgm.getReferences().newReference(ref);
			/*} else {
				return "";
			}*/
		}
	},
	isnull {
		public String evaluate(Program pgm, Variables vars, Instruction inst) throws Exception {
			//Object[] objs = Bit.getObjects(pgm, vars, inst, 1);
			Reference[] refs = Bit.getReferences(pgm, vars, inst, 1);
			if (refs[0].value == null) {
				return "true";
			} else {
				if (refs[0].value.toString().equalsIgnoreCase("null"))
					return "true";
				else 
					return "false";
			}
		}
	},
	isnotnull {
		public String evaluate(Program pgm, Variables vars, Instruction inst) throws Exception {
			/*Integer i = (Integer) vars.get("$total").ref.value; 
			if (i.intValue() == 1022) {
				System.out.println("Aqui");
			}*/
			//Object[] objs = Bit.getObjects(pgm, vars, inst, 1);
			Reference[] refs = Bit.getReferences(pgm, vars, inst, 1);
			if (refs[0].value == null) {
				return "false";
			} else {
				if (refs[0].value.toString().equalsIgnoreCase("null"))
					return "false";
				else 
					return "true";
			}
		}
	};


	public abstract String evaluate(Program pgm, Variables vars, Instruction inst) throws Exception;

	public boolean isArgsNumberOK(int num) {
		switch(this) {
		case getfield:
			return num == 2;
		case setfield:
			return num == 3;
		case isnull:
			return num == 1;
		default:
			return true;	
		}
	}

	public boolean isLazy() {
		return false;
	}
	
	private static boolean validMethod(Class[] cs, Object[] objs) {
		for (int i = 0; i < cs.length; i++) {
			if (objs[i] != null && objs[i].getClass() != cs[i] && !cs[i].getName().contains("Object")) return false;
		}
		return true;
	}
}
