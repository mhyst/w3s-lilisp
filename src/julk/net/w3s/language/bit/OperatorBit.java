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
//TODO Revisar la clase OperatorBit
public enum OperatorBit implements BitCommand {
	//Arithmetic operators
	PLUS("+") {
		public String evaluate(Program pgm, Variables vars, Instruction inst) throws Exception {
			Object[] objs = Bit.getObjects(pgm, vars, inst, 1);
			double[] nums = Type.getNumbers(objs);
			if (nums == null) Bit.error(inst,"Wrong types numeric or decimal expected.");
			double add = 0;
			for (double d:nums) {
				add += d;
			}
			//return ""+add;
			Reference refs[] = Bit.getReferences(pgm, vars, inst, 1);
			return ""+Type.adjustResultValue(refs, add);
			//return Type.newNumericReference(pgm.getReferences(), Type.determineTheResultType(refs), add);
			/*Reference[] refs = Bit.getReferences(pgm, vars, inst, 1);
			double sum = 0;
			Type t = Type.numeric;
			for (Reference ref : refs) {
				switch (ref.type) {
				case numeric:
					sum += (Integer) ref.value;
					break;
				case decimal:
					t = Type.decimal;
					sum += (Double) ref.value;
					break;
				default:
					Bit.error(inst,"Wrong types. numeric or decimal expected.");
				}
			}
			Reference ref = new Reference();
			ref.type = t;
			if (t == Type.numeric) 
				ref.value = Math.round(sum);
			else
				ref.value = sum;
			return pgm.getReferences().newReference(ref);*/
		}
	},
	MINUS("-") {
		public String evaluate(Program pgm, Variables vars, Instruction inst) throws Exception {
			Object[] objs = Bit.getObjects(pgm, vars, inst, 1);
			double[] nums = Type.getNumbers(objs);
			if (nums == null) Bit.error(inst,"Wrong types numeric or decimal expected.");
			double sub = nums[0];
			for (int i = 1; i < nums.length; i++) {
				sub -= nums[i];
			}
			//return ""+sub;
			Reference refs[] = Bit.getReferences(pgm, vars, inst, 1);
			return ""+Type.adjustResultValue(refs, sub);
			//return Type.newNumericReference(pgm.getReferences(), Type.determineTheResultType(refs), sub);

		}
	},  // -
	MUL("*") {
		public String evaluate(Program pgm, Variables vars, Instruction inst) throws Exception {
			Object[] objs = Bit.getObjects(pgm, vars, inst, 1);
			double[] nums = Type.getNumbers(objs);
			if (nums == null) Bit.error(inst,"Wrong types numeric or decimal expected.");
			double sum = 1;
			for (double d:nums) {
				sum *= d;
			}
			//return ""+sum;
			Reference refs[] = Bit.getReferences(pgm, vars, inst, 1);
			return ""+Type.adjustResultValue(refs, sum);
			//return Type.newNumericReference(pgm.getReferences(), Type.determineTheResultType(refs), sum);

		}
	},  // *
	DIV("/") {
		public String evaluate(Program pgm, Variables vars, Instruction inst) throws Exception {
			Object[] objs = Bit.getObjects(pgm, vars, inst, 1);
			double[] nums = Type.getNumbers(objs);
			if (nums == null) Bit.error(inst,"Wrong types numeric or decimal expected.");
			double div = nums[0];
			for (int i = 1; i < nums.length; i++) {
				div /= nums[i];
			}
			//return ""+div;
			Reference refs[] = Bit.getReferences(pgm, vars, inst, 1);
			return ""+Type.adjustResultValue(refs, div);
			//return Type.newNumericReference(pgm.getReferences(), Type.determineTheResultType(refs), div);
		}
	},  // (/)
	REMAINDER("%") {
		public String evaluate(Program pgm, Variables vars, Instruction inst) throws Exception {
			Object[] objs = Bit.getObjects(pgm, vars, inst, 1);
			double[] nums = Type.getNumbers(objs);
			if (nums == null) Bit.error(inst,"Wrong types numeric or decimal expected.");
			double div = nums[0];
			for (int i = 1; i < nums.length; i++) {
				div %= nums[i];
			}
			//return ""+div;
			Reference refs[] = Bit.getReferences(pgm, vars, inst, 1);
			return ""+Type.adjustResultValue(refs, div);
			//return Type.newNumericReference(pgm.getReferences(), Type.determineTheResultType(refs), div);
		}
	},  // (/)
	SQRT("sqrt",1) {
		public String evaluate(Program pgm, Variables vars, Instruction inst) throws Exception {
			Object[] objs = Bit.getObjects(pgm, vars, inst, 1);
			double[] nums = Type.getNumbers(objs);
			if (nums == null) Bit.error(inst,"Wrong types numeric or decimal expected.");
			//return ""+Math.sqrt(nums[0]);
			Reference refs[] = Bit.getReferences(pgm, vars, inst, 1);
			return ""+Type.adjustResultValue(refs, Math.sqrt(nums[0]));
			//return Type.newNumericReference(pgm.getReferences(), Type.determineTheResultType(refs), Math.sqrt(nums[0]));
		}
	},  // Square Root
	//EQU("="),  // =
	PP("++",1) {
		public String evaluate(Program pgm, Variables vars, Instruction inst) throws Exception {
			Object[] objs = Bit.getObjects(pgm, vars, inst, 1);
			double[] nums = Type.getNumbers(objs);
			if (nums == null) Bit.error(inst,"Wrong types numeric or decimal expected.");
			/*String refvar =  inst.getArgs().argumentAt(1).getValue();
			Reference ref = null;
			if (Variables.isVariable(refvar)) {
				ref = pgm.recurseVar(refvar).ref;
			} else if(References.isReference(refvar)) {
				ref = pgm.getReferences().get(refvar);
			}
			if (ref == null) {
				Bit.error(inst, "The first argument should be a variable or reference");
			}*/
			Reference refs[] = Bit.getReferences(pgm, vars, inst, 1);
			Reference ref = refs[0];

			if (!ref.type.isNumeric()) {
				Bit.error(inst, "Incompatible types");
			}
			double add = nums[0]+1;
			
			if (ref.type == Type.numeric)
				ref.value = (int) add;
			else
				ref.value = add;
			return ref.address;

			//double data = nums[0]++;
			//return ""+ref.value;
		}
	},  // ++
	MM("--",1) {
		public String evaluate(Program pgm, Variables vars, Instruction inst) throws Exception {
			Object[] objs = Bit.getObjects(pgm, vars, inst, 1);
			double[] nums = Type.getNumbers(objs);
			if (nums == null) Bit.error(inst,"Wrong types numeric or decimal expected.");
			/*String refvar =  inst.getArgs().argumentAt(1).getValue();
			Reference ref = null;
			if (Variables.isVariable(refvar)) {
				ref = pgm.recurseVar(refvar).ref;
			} else if(References.isReference(refvar)) {
				ref = pgm.getReferences().get(refvar);
			}
			if (ref == null) {
				Bit.error(inst, "The first argument should be a variable or reference");
			}*/
			
			Reference refs[] = Bit.getReferences(pgm, vars, inst, 1);
			Reference ref = refs[0];

			if (!ref.type.isNumeric()) {
				Bit.error(inst, "Incompatible types");
			}
			double add = nums[0]-1;
			
			if (ref.type == Type.numeric)
				ref.value = (int) add;
			else
				ref.value = add;

			return ref.address;
			//return ref.value.toString();
		}
	},  // --
	PEQ("+=",2) {
		public String evaluate(Program pgm, Variables vars, Instruction inst) throws Exception {
			Object[] objs = Bit.getObjects(pgm, vars, inst, 1);
			double[] nums = Type.getNumbers(objs);
			if (nums == null) Bit.error(inst,"Wrong types numeric or decimal expected.");
			/*String refvar =  inst.getArgs().argumentAt(1).getValue();
			Reference ref = null;
			if (Variables.isVariable(refvar)) {
				ref = pgm.recurseVar(refvar).ref;
			} else if(References.isReference(refvar)) {
				ref = pgm.getReferences().get(refvar);
			}
			if (ref == null) {
				Bit.error(inst, "The first argument should be a variable or reference");
			}*/
			Reference refs[] = Bit.getReferences(pgm, vars, inst, 1);
			Reference ref = refs[0];

			if (!ref.type.isNumeric()) {
				Bit.error(inst, "Incompatible types");
			}
			double add = nums[0]+nums[1];
			
			if (ref.type == Type.numeric)
				ref.value = (int) add;
			else
				ref.value = add;

			return ref.address;
		}
	},  // +=
	MEQ("-=",2) {
		public String evaluate(Program pgm, Variables vars, Instruction inst) throws Exception {
			Object[] objs = Bit.getObjects(pgm, vars, inst, 1);
			double[] nums = Type.getNumbers(objs);
			if (nums == null) Bit.error(inst,"Wrong types numeric or decimal expected.");
			/*String refvar =  inst.getArgs().argumentAt(1).getValue();
			Reference ref = null;
			if (Variables.isVariable(refvar)) {
				ref = pgm.recurseVar(refvar).ref;
			} else if(References.isReference(refvar)) {
				ref = pgm.getReferences().get(refvar);
			}
			if (ref == null) {
				Bit.error(inst, "The first argument should be a variable or reference");
			}*/
			Reference refs[] = Bit.getReferences(pgm, vars, inst, 1);
			Reference ref = refs[0];

			if (!ref.type.isNumeric()) {
				Bit.error(inst, "Incompatible types");
			}
			double add = nums[0]-nums[1];
			
			if (ref.type == Type.numeric)
				ref.value = (int) add;
			else
				ref.value = add;

			return ref.address;
		}
	},  // -=
	MULEQ("*=",2) {
		public String evaluate(Program pgm, Variables vars, Instruction inst) throws Exception {
			Object[] objs = Bit.getObjects(pgm, vars, inst, 1);
			double[] nums = Type.getNumbers(objs);
			if (nums == null) Bit.error(inst,"Wrong types numeric or decimal expected.");
			/*String refvar =  inst.getArgs().argumentAt(1).getValue();
			Reference ref = null;
			if (Variables.isVariable(refvar)) {
				ref = pgm.recurseVar(refvar).ref;
			} else if(References.isReference(refvar)) {
				ref = pgm.getReferences().get(refvar);
			}
			if (ref == null) {
				Bit.error(inst, "The first argument should be a variable or reference");
			}*/
			Reference refs[] = Bit.getReferences(pgm, vars, inst, 1);
			Reference ref = refs[0];

			if (!ref.type.isNumeric()) {
				Bit.error(inst, "Incompatible types");
			}
			double add = nums[0]*nums[1];
			
			if (ref.type == Type.numeric)
				ref.value = (int) add;
			else
				ref.value = add;

			return ref.address;
		}
	}, // *=
	DIVEQ("/=",2) {
		public String evaluate(Program pgm, Variables vars, Instruction inst) throws Exception {
			Object[] objs = Bit.getObjects(pgm, vars, inst, 1);
			double[] nums = Type.getNumbers(objs);
			if (nums == null) Bit.error(inst,"Wrong types numeric or decimal expected.");
			/*String refvar =  inst.getArgs().argumentAt(1).getValue();
			Reference ref = null;
			if (Variables.isVariable(refvar)) {
				ref = pgm.recurseVar(refvar).ref;
			} else if(References.isReference(refvar)) {
				ref = pgm.getReferences().get(refvar);
			}
			if (ref == null) {
				Bit.error(inst, "The first argument should be a variable or reference");
			}*/
			Reference refs[] = Bit.getReferences(pgm, vars, inst, 1);
			Reference ref = refs[0];

			if (!ref.type.isNumeric()) {
				Bit.error(inst, "Incompatible types");
			}
			double add = nums[0]/nums[1];
			
			if (ref.type == Type.numeric)
				ref.value = (int) add;
			else
				ref.value = add;

			return ref.address;
		}
	}, // /=
	
	//Logical operators
	EQ("==",2) {
		public String evaluate(Program pgm, Variables vars, Instruction inst) throws Exception {
			Object[] objs = Bit.getObjects(pgm, vars, inst, 1);
			double v1 = Double.parseDouble(objs[0].toString());
			double v2 = Double.parseDouble(objs[1].toString());
			return v1 == v2 ? "true" : "false";
		}
	},	 // ==
	GT(">",2) {
		public String evaluate(Program pgm, Variables vars, Instruction inst) throws Exception {
			Object[] objs = Bit.getObjects(pgm, vars, inst, 1);
			double[] nums = Type.getNumbers(objs);
			if (nums == null) Bit.error(inst,"Wrong types numeric or decimal expected.");
			return nums[0] > nums[1] ? "true" : "false";
		}
	},   // >
	LT("<",2) {
		public String evaluate(Program pgm, Variables vars, Instruction inst) throws Exception {
			Object[] objs = Bit.getObjects(pgm, vars, inst, 1);
			double[] nums = Type.getNumbers(objs);
			if (nums == null) Bit.error(inst,"Wrong types numeric or decimal expected.");
			return nums[0] < nums[1] ? "true" : "false";		}
	},   // <
	GE(">=",2) {
		public String evaluate(Program pgm, Variables vars, Instruction inst) throws Exception {
			Object[] objs = Bit.getObjects(pgm, vars, inst, 1);
			double[] nums = Type.getNumbers(objs);
			if (nums == null) Bit.error(inst,"Wrong types numeric or decimal expected.");
			return nums[0] >= nums[1] ? "true" : "false";
		}
	},   // >=
	LE("<=",2) {
		public String evaluate(Program pgm, Variables vars, Instruction inst) throws Exception {
			Object[] objs = Bit.getObjects(pgm, vars, inst, 1);
			double[] nums = Type.getNumbers(objs);
			if (nums == null) Bit.error(inst,"Wrong types numeric or decimal expected.");
			return nums[0] <= nums[1] ? "true" : "false";		}
	},   // <=
	NE("!=",2) {
		public String evaluate(Program pgm, Variables vars, Instruction inst) throws Exception {
			Object[] objs = Bit.getObjects(pgm, vars, inst, 1);
			double[] nums = Type.getNumbers(objs);
			if (nums == null) Bit.error(inst,"Wrong types numeric or decimal expected.");
			return nums[0] != nums[1] ? "true" : "false";		}
	},   // !=
	NOT("!",1) {
		public String evaluate(Program pgm, Variables vars, Instruction inst) throws Exception {
			Object[] objs = Bit.getObjects(pgm, vars, inst, 1);
			Type t = Type.determineTheType(objs[0]);
			if (t != Type.logic) {
				Bit.error(inst, "Argument must be logic");
			}
			Boolean b = Boolean.parseBoolean(objs[0].toString());
			return b.booleanValue() ? "false" : "true";
		}
	},   // !
	AND("&&") {
		public String evaluate(Program pgm, Variables vars, Instruction inst) throws Exception {
			Object[] objs = Bit.getObjects(pgm, vars, inst, 1);
			Boolean[] values = new Boolean[objs.length];
			int i = 0;
			for (Object o: objs) {
				Type t = Type.determineTheType(o);
				if (t != Type.logic)
					Bit.error(inst, "Arguments must be logic");
				values[i] = (Boolean) t.getObject(o.toString(),null);
				if (!values[i]) return "false";
				i++;
			}
			return "true";
		}
	},   // &&
	OR("||") {
		public String evaluate(Program pgm, Variables vars, Instruction inst) throws Exception {
			Object[] objs = Bit.getObjects(pgm, vars, inst, 1);
			Boolean[] values = new Boolean[objs.length];
			int i = 0;
			for (Object o: objs) {
				Type t = Type.determineTheType(o);
				if (t != Type.logic)
					Bit.error(inst, "Arguments must be logic");
				values[i] = (Boolean) t.getObject(o.toString(),null);
				if (values[i]) return "true";
				i++;
			}
			return "false";
		}
	};   // ||;

	public String opcode;
	public int[] numArgs;
	
	private OperatorBit(String _op, int... num) {
		opcode = _op;
		numArgs = num;
	}
	
	public static BitCommand getOperatorByCode(String code) {
		for (OperatorBit ob : values()) {
			if (ob.opcode.equalsIgnoreCase(code)) {
				return ob;
			}
		}
		return null;
	}
	
	public abstract String evaluate(Program pgm, Variables vars, Instruction inst) throws Exception;

	public boolean isArgsNumberOK(int num) {
		if (numArgs == null || numArgs.length == 0) return true;
		for (int na : numArgs) {
			if (na == num) return true;
		}
		return false;
	}

	public boolean isLazy() {
		return false;
	}
	
	public int getInteger(Object obj) {
		return ((Integer) obj).intValue();
	}

	public double getDouble(Object obj) {
		return ((Double) obj).doubleValue();
	}	
}
