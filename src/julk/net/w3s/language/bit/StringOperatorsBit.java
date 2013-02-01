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

public enum StringOperatorsBit implements BitCommand {
	equals {
		public String evaluate(Program pgm, Variables vars, Instruction inst) throws Exception {
			String[] values = Bit.getValues(pgm, vars, inst, 1);
			if (values[0].equals(values[1])) return "true";
			else return "false";
		}		
	},
	equalsignorecase {
		public String evaluate(Program pgm, Variables vars, Instruction inst) throws Exception {
			String[] values = Bit.getValues(pgm, vars, inst, 1);
			if (values[0].equalsIgnoreCase(values[1])) return "true";
			else return "false";
		}
	},
	contains {
		public String evaluate(Program pgm, Variables vars, Instruction inst) throws Exception {
			String[] values = Bit.getValues(pgm, vars, inst, 1);
			if (values[0].contains(values[1])) return "true";
			else return "false";
		}
	},
	replace {
		public String evaluate(Program pgm, Variables vars, Instruction inst) throws Exception {
			String[] values = Bit.getValues(pgm, vars, inst, 1);
			return values[0].replace(values[1], values[2]);
		}
	},	
	cat {		
		public String evaluate(Program pgm, Variables vars, Instruction inst) throws Exception {
			String[] values = Bit.getValues(pgm, vars, inst, 1);
			String result = values[0];
			for (int i = 1; i < values.length; i++) {
				result += values[i];
			}
			return result;
		}
	},
	tonumericarray {
		public String evaluate(Program pgm, Variables vars, Instruction inst) throws Exception {
			String[] values = Bit.getValues(pgm, vars, inst, 1);
			byte[] a = values[0].getBytes();
			Integer[] c = new Integer[a.length];
			int i = 0;
			for (byte b : a) {
				c[i] = (int) b;
				i++;
			}
			Reference ref = new Reference();
			ref.type = Type.array;
			ref.value = c;
			return pgm.getReferences().newReference(ref);
		}
	},
	tochararray {
		public String evaluate(Program pgm, Variables vars, Instruction inst) throws Exception {
			String[] values = Bit.getValues(pgm, vars, inst, 1);
			byte[] a = values[0].getBytes();
			Character[] c = new Character[a.length];
			int i = 0;
			for (byte b : a) {
				c[i] = (char) b;
				i++;
			}
			Reference ref = new Reference();
			ref.type = Type.array;
			ref.value = c;
			return pgm.getReferences().newReference(ref);
		}
	},
	tochar {		
		public String evaluate(Program pgm, Variables vars, Instruction inst) throws Exception {
			Object[] objs = Bit.getObjects(pgm, vars, inst, 1);
			Integer a = null;
			try {
				a = Integer.parseInt(objs[0].toString());
			} catch (Exception e) {
				Bit.error(inst, "First argument must be a numeric value");
			}
			Character c = (char) a.intValue();
			Reference ref = new Reference();
			ref.type = Type.character;
			ref.value = c;
			return pgm.getReferences().newReference(ref);
		}
	},
	tonumber {		
		public String evaluate(Program pgm, Variables vars, Instruction inst) throws Exception {
			String[] values = Bit.getValues(pgm, vars, inst, 1);
			Integer n = null;
			try {
				n = Integer.parseInt(values[0]);
			} catch (Exception e) {
				Bit.error(inst, "First argument must be a character");
			}
			Reference ref = new Reference();
			ref.type = Type.numeric;
			ref.value = n;
			return pgm.getReferences().newReference(ref);
		}
	};

	public abstract String evaluate(Program pgm, Variables vars, Instruction inst) throws Exception;

	public boolean isArgsNumberOK(int num) {
		switch (this) {
		case equals:
			return num == 2;
		case equalsignorecase:
			return num == 2;
		case contains:
			return num == 2;
		case replace:
			return num == 3;
		case cat:
			return num > 1;
		case tonumericarray:
			return num == 1;
		case tochararray:
			return num == 1;
		case tochar:
			return num == 1;
		case tonumber:
			return num == 1;
		}
		return false;
	}

	public boolean isLazy() {
		return false;
	}

}
