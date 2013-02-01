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

public enum ArraysBit implements BitCommand {
	aref {		
		public String evaluate(Program pgm, Variables vars, Instruction inst) throws Exception {
			Object[] objs = Bit.getObjects(pgm, vars, inst, 1);
			if (!Type.array.isValidValue(objs[0])) {
				Bit.error(inst, "First argument must be an array");
			}
			Object[] a = (Object[]) objs[0];
			Type t = Type.determineTheType(objs[1]);
			if (!t.isNumeric())
				Bit.error(inst, "Type mismatch to aref: numeric expected");
			
			int idx = 0;
			try {
				idx = (int) Double.parseDouble(objs[1].toString());
			} catch (Exception e) {
				Bit.error(inst, "Type mismatch to aref: numeric expected");
			}
			Object m = a[idx];
			Reference ref = new Reference();
			ref.type = Type.determineTheType(m);
			ref.value = m;
			return pgm.getReferences().newReference(ref);
		}
	},
	sref {		
		public String evaluate(Program pgm, Variables vars, Instruction inst) throws Exception {
			Object[] objs = Bit.getObjects(pgm, vars, inst, 1);
			if (!Type.array.isValidValue(objs[0])) {
				Bit.error(inst, "First argument must be an array");
			}
			Object[] a = (Object[]) objs[0];
			Type t = Type.determineTheType(objs[1]);
			if (!t.isNumeric())
				Bit.error(inst, "Type mismatch to aref: numeric expected");
			
			int idx = 0;
			try {
				idx = (int) Double.parseDouble(objs[1].toString());
			} catch (Exception e) {
				Bit.error(inst, "Type mismatch to aref: numeric expected");
			}
			
			a[idx] = objs[2];
			Reference ref = new Reference();
			ref.type = Type.array;
			ref.value = a;
			return pgm.getReferences().newReference(ref);
			//return "";
		}
	};

	public abstract String evaluate(Program pgm, Variables vars, Instruction inst) throws Exception;

	public boolean isArgsNumberOK(int num) {
		switch (this) {
		case aref:
			return num == 2;
		case sref:
			return num == 3;
		}
		return false;
	}

	public boolean isLazy() {
		return false;
	}

}
