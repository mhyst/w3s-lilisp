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
public enum FilesBit implements BitCommand {
	openread {		
		public String evaluate(Program pgm, Variables vars, Instruction inst) throws Exception {
			Reference refs[] = Bit.getReferences(pgm, vars, inst, 1);
			Reference ref = refs[0];

			if (ref.type != Type.file)
				Bit.error(inst, "First argument to openread must be a file");
				
			File f = (File) ref.value;
			f.openRead();
			return ref.address;
		}
	},
	openwrite {		
		public String evaluate(Program pgm, Variables vars, Instruction inst) throws Exception {
			Reference refs[] = Bit.getReferences(pgm, vars, inst, 1);
			Reference ref = refs[0];

			if (ref.type != Type.file)
				Bit.error(inst, "First argument to openread must be a file");
				
			File f = (File) ref.value;
			f.openWrite();
			return ref.address;
			/*Object[] objs = Bit.getObjects(pgm, vars, inst, 1);
			if (!Type.file.isValidValue(objs[0]))
				Bit.error(inst, "First argument to openread must be a file");
				
			File f = (File) objs[0];
			f.openWrite();
			Reference ref = new Reference();
			ref.type = Type.file;
			ref.value = f;
			return pgm.getReferences().newReference(ref);*/
		}
	},
	openappend {		
		public String evaluate(Program pgm, Variables vars, Instruction inst) throws Exception {
			Reference refs[] = Bit.getReferences(pgm, vars, inst, 1);
			Reference ref = refs[0];

			if (ref.type != Type.file)
				Bit.error(inst, "First argument to openread must be a file");
				
			File f = (File) ref.value;
			f.openAppend();
			return ref.address;
			/*Object[] objs = Bit.getObjects(pgm, vars, inst, 1);
			if (!Type.file.isValidValue(objs[0]))
				Bit.error(inst, "First argument to openread must be a file");
				
			File f = (File) objs[0];
			f.openAppend();
			Reference ref = new Reference();
			ref.type = Type.file;
			ref.value = f;
			return pgm.getReferences().newReference(ref);*/
		}
	},
	readline {
		public String evaluate(Program pgm, Variables vars, Instruction inst) throws Exception {
			Reference refs[] = Bit.getReferences(pgm, vars, inst, 1);
			Reference ref = refs[0], refline = refs[1];
			

			if (ref.type != Type.file)
				Bit.error(inst, "First argument to openread must be a file");
			
			if (refline.type != Type.string)
				Bit.error(inst, "Second argument to openread must be a string");
			File f = (File) ref.value;
			refline.value = f.readLine();
			return refline.address;

			/*Object[] objs = Bit.getObjects(pgm, vars, inst, 1);
			if (!Type.file.isValidValue(objs[0]))
				Bit.error(inst, "First argument to openread must be a file");
				
			File f = (File) objs[0];
			String line = f.readLine();
			Reference ref = new Reference();
			ref.type = Type.string;
			ref.value = line;
			return pgm.getReferences().newReference(ref);*/
		}
	},
	read {		
		public String evaluate(Program pgm, Variables vars, Instruction inst) throws Exception {
			Reference refs[] = Bit.getReferences(pgm, vars, inst, 1);
			Reference ref = refs[0], refdata = refs[1];
			

			if (ref.type != Type.file)
				Bit.error(inst, "First argument to openread must be a file");
			
			if (refdata.type != Type.numeric)
				Bit.error(inst, "Second argument to openread must be numeric");
			File f = (File) ref.value;
			refdata.value = f.read();
			return ref.address;

			/*Object[] objs = Bit.getObjects(pgm, vars, inst, 1);
			if (!Type.file.isValidValue(objs[0]))
				Bit.error(inst, "First argument to openread must be a file");
				
			File f = (File) objs[0];
			int c = f.read();
			Reference ref = new Reference();
			ref.type = Type.numeric;
			ref.value = c;
			return pgm.getReferences().newReference(ref);*/
		}
	},
	writeline {		
		public String evaluate(Program pgm, Variables vars, Instruction inst) throws Exception {
			Object[] objs = Bit.getObjects(pgm, vars, inst, 1);
			if (!Type.file.isValidValue(objs[0]))
				Bit.error(inst, "First argument to writeLine must be a file");
			if (!Type.string.isValidValue(objs[1])) {
				Bit.error(inst, "Second argument to openread must be a string");
			}
			File f = (File) objs[0];
			String line = (String) objs[1];
			f.writeLine(line);
			return "";
		}
	},
	write {		
		public String evaluate(Program pgm, Variables vars, Instruction inst) throws Exception {
			Object[] objs = Bit.getObjects(pgm, vars, inst, 1);
			if (!Type.file.isValidValue(objs[0]))
				Bit.error(inst, "First argument to writeLine must be a file");
			if (!Type.numeric.isValidValue(objs[1])) {
				Bit.error(inst, "Second argument to openread must be numeric");
			}
			File f = (File) objs[0];
			int c;
			if (objs[1].getClass().getName().contains("Integer"))
				c =  ((Integer)objs[1]).intValue();
			else
				c =  ((Long)objs[1]).intValue();
			f.write(c);
			return "";
		}
	},
	mark {		
		public String evaluate(Program pgm, Variables vars, Instruction inst) throws Exception {
			Reference refs[] = Bit.getReferences(pgm, vars, inst, 1);
			Reference ref = refs[0], refc = refs[1];

			if (ref.type != Type.file)
				Bit.error(inst, "First argument to openread must be a file");
				
			File f = (File) ref.value;
			f.mark(((Long)refc.value).intValue());
			return ref.address;
			/*Object[] objs = Bit.getObjects(pgm, vars, inst, 1);
			if (!Type.file.isValidValue(objs[0]))
				Bit.error(inst, "First argument to writeLine must be a file");
			if (!Type.numeric.isValidValue(objs[1])) {
				Bit.error(inst, "Second argument to openread must be numeric");
			}
			File f = (File) objs[0];
			int c =  ((Integer)objs[1]).intValue();
			f.mark(c);
			Reference ref = new Reference();
			ref.type = Type.file;
			ref.value = f;
			return pgm.getReferences().newReference(ref);*/
		}
	},
	reset {		
		public String evaluate(Program pgm, Variables vars, Instruction inst) throws Exception {
			Reference refs[] = Bit.getReferences(pgm, vars, inst, 1);
			Reference ref = refs[0];

			if (ref.type != Type.file)
				Bit.error(inst, "First argument to openread must be a file");
				
			File f = (File) ref.value;
			f.reset();
			return ref.address;
			/*Object[] objs = Bit.getObjects(pgm, vars, inst, 1);
			if (!Type.file.isValidValue(objs[0]))
				Bit.error(inst, "First argument to writeLine must be a file");
			File f = (File) objs[0];
			f.reset();
			Reference ref = new Reference();
			ref.type = Type.file;
			ref.value = f;
			return pgm.getReferences().newReference(ref);*/
		}
	},
	marksupported {		
		public String evaluate(Program pgm, Variables vars, Instruction inst) throws Exception {
			Object[] objs = Bit.getObjects(pgm, vars, inst, 1);
			if (!Type.file.isValidValue(objs[0]))
				Bit.error(inst, "First argument to writeLine must be a file");
			File f = (File) objs[0];
			Reference ref = new Reference();
			ref.type = Type.logic;
			ref.value = f.markSupported();
			return pgm.getReferences().newReference(ref);
		}
	},
	dumpfile {		
		public String evaluate(Program pgm, Variables vars, Instruction inst) throws Exception {
			Object[] objs = Bit.getObjects(pgm, vars, inst, 1);
			if (!Type.file.isValidValue(objs[0]))
				Bit.error(inst, "First argument to openread must be a file");
			
			File src = (File) objs[0];
			File dest = (File) objs[1];
			int c, i = 0;
			while ((c = src.read()) != -1) {
				dest.write(c);
				i++;
			}
			Reference ref = new Reference();
			ref.type = Type.numeric;
			ref.value = i; 
			return pgm.getReferences().newReference(ref);

		}
	},
	close {		
		public String evaluate(Program pgm, Variables vars, Instruction inst) throws Exception {
			Object[] objs = Bit.getObjects(pgm, vars, inst, 1);
			if (!Type.file.isValidValue(objs[0]))
				Bit.error(inst, "First argument to writeLine must be a file");
			File f = (File) objs[0];
			f.close();
			Reference ref = new Reference();
			ref.type = Type.file;
			ref.value = f;
			return pgm.getReferences().newReference(ref);
		}
	},
	exists {		
		public String evaluate(Program pgm, Variables vars, Instruction inst) throws Exception {
			Object[] objs = Bit.getObjects(pgm, vars, inst, 1);
			if (!Type.file.isValidValue(objs[0]))
				Bit.error(inst, "First argument to writeLine must be a file");
			File f = (File) objs[0];
			
			Reference ref = new Reference();
			ref.type = Type.logic;
			ref.value = f.exists();
			return pgm.getReferences().newReference(ref);
		}
	};

	public abstract String evaluate(Program pgm, Variables vars, Instruction inst) throws Exception;

	public boolean isArgsNumberOK(int num) {
		switch (this) {
		case openread:
		case openwrite:
		case openappend:
		case close:
		case exists:
		case reset:
		case marksupported:
			return num == 1;		
		case readline:
		case read:
		case writeline:
		case write:
		case mark:
		case dumpfile:
			return num == 2;
		}
		return false;
	}

	public boolean isLazy() {
		return false;
	}	
}
