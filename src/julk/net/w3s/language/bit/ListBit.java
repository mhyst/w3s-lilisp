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

public enum ListBit implements BitCommand {
	head {		
		public String evaluate(Program pgm, Variables vars, Instruction inst) throws Exception {
			Object[] objs = Bit.getObjects(pgm, vars, inst, 1);
			if (!Type.list.isValidValue(objs[0])) {
				if (objs[0] == null) {
					Reference ref = new Reference();
					ref.type = Type.list;
					ref.value = null;
					return pgm.getReferences().newReference(ref);					
				}
				//return "null";
			}
			List l = (List) objs[0];
			Reference ref = new Reference();
			ref.type = Type.string;
			ref.value = l.head();
			return pgm.getReferences().newReference(ref);
		}
	},
	tail {		
		public String evaluate(Program pgm, Variables vars, Instruction inst) throws Exception {
			Object[] objs = Bit.getObjects(pgm, vars, inst, 1);
			if (!Type.list.isValidValue(objs[0])) {
				if (objs[0] == null) {
					Reference ref = new Reference();
					ref.type = Type.list;
					ref.value = null;
					return pgm.getReferences().newReference(ref);					
				}
				//Bit.error(inst, "First argument must be a list reference");
			}
			List l = (List) objs[0];
			Reference ref = new Reference();
			ref.type = Type.list;
			ref.value = l.tail();
			return pgm.getReferences().newReference(ref);
		}
	},
	eval {
		public String evaluate(Program pgm, Variables vars, Instruction inst) throws Exception {
			Object[] objs = Bit.getObjects(pgm, vars, inst, 1);
			if (!Type.list.isValidValue(objs[0])) {
				if (objs[0] == null) {
					Reference ref = new Reference();
					ref.type = Type.list;
					ref.value = null;
					return pgm.getReferences().newReference(ref);					
				}
				//Bit.error(inst, "First argument must be a list reference");
			}
			List l = (List) objs[0];
			inst.setCode(l.toString());
			return pgm.evaluate(inst);
		}		
	};

	public abstract String evaluate(Program pgm, Variables vars, Instruction inst) throws Exception;

	public boolean isArgsNumberOK(int num) {
		switch (this) {
		case head:
			return num == 1;
		case tail:
			return num == 1;
		case eval:
			return num == 1;
		}
		return false;
	}

	public boolean isLazy() {
		return false;
	}
}
