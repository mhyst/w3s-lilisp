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

import java.io.BufferedReader;
import java.io.InputStreamReader;

import julk.net.w3s.language.*;

public enum IOBit implements BitCommand {
	print {
		public String evaluate(Program pgm, Variables vars, Instruction inst) throws Exception {
			Object[] objs = Bit.getObjects(pgm, vars, inst, 1);
			String output = "";
			//int numargs = inst.countArguments();
			for (Object obj : objs) {
				if (obj != null)
					output += obj.toString();
			}
			System.out.print(output);
		
			if (output.length() == 1) {
				return ""+output.length()+" byte printed.";
			} else {
				return ""+output.length()+" bytes printed.";
			}
		}
	},
	println {
		public String evaluate(Program pgm, Variables vars, Instruction inst) throws Exception {
			Object[] objs = Bit.getObjects(pgm, vars, inst, 1);
			String output = "";
			//int numargs = inst.countArguments();
			for (Object obj : objs) {
				if (obj != null)
					output += obj.toString();
			}
			System.out.println(output);
		
			if (output.length() == 1) {
				return ""+output.length()+" byte printed.";
			} else {
				return ""+output.length()+" bytes printed.";
			}
		}
	},
	getreferencesnumber {
		public String evaluate(Program pgm, Variables vars, Instruction inst) throws Exception {
			return ""+pgm.getReferences().getNumberOfReferences();		
		}
	},
	getvariablesnumber {
		public String evaluate(Program pgm, Variables vars, Instruction inst) throws Exception {
			return ""+pgm.getGlobal().getNumberOfVariables();		
		}
	},
	getfreemem {
		public String evaluate(Program pgm, Variables vars, Instruction inst) throws Exception {
			Runtime r = Runtime.getRuntime();
			return ""+r.freeMemory();		
		}
	},
	getmaxmem {
		public String evaluate(Program pgm, Variables vars, Instruction inst) throws Exception {
			Runtime r = Runtime.getRuntime();
			return ""+r.maxMemory();		
		}
	},
	input {
		public String evaluate(Program pgm, Variables vars, Instruction inst) throws Exception {
			Object[] objs = Bit.getObjects(pgm, vars, inst, 1);
			String type = objs[0].toString();
			String msg = "";
			for (int i = 1; i < objs.length; i++) {
				msg += objs[i].toString();
			}
			Type t = null;
			try {
				t = Type.valueOf(type);
			} catch (Exception e) {
				Bit.error(inst, "Invalid type, first parameter must be the type");
			}
			if (t == null)
				Bit.error(inst, "First parameter must be the type");

			String i;
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			System.out.print(msg);
			i = br.readLine();
			while (!t.isValidValue(i)) {
				System.out.println("The value must be a valid "+t.name());
				System.out.print(msg);
				i = br.readLine();				
			}
				
			Reference ref = new Reference();
			ref.type = t;
			ref.value = t.getObject(i,null);
			return pgm.getReferences().newReference(ref);
		}
	},
	inputw {
		public String evaluate(Program pgm, Variables vars, Instruction inst) throws Exception {
			Object[] objs = Bit.getObjects(pgm, vars, inst, 1);
			
			String title = objs[0].toString();
			String msg = objs[1].toString();
			String btnlabel = objs[2].toString();  
			int width = Integer.parseInt(objs[3].toString());
			String type = objs[4].toString();
			Type t = Type.valueOf(type);
			if (t == null)
				Bit.error(inst, "Fifth parameter must be the type");
			RequestInput ri = pgm.getInput();
			Reference ref = ri.getInput(title, msg, width, btnlabel, t);
			ref.value = ref.value;
			return pgm.getReferences().newReference(ref);
		}		
	};

	public abstract String evaluate(Program pgm, Variables vars, Instruction inst) throws Exception;

	public boolean isArgsNumberOK(int num) {
		switch (this) {
		case print:
			return num > 0;
		case println:
			return true;
		case getreferencesnumber:
		case getvariablesnumber:
		case getfreemem:
		case getmaxmem:
			return true;
		case inputw:
			return num>= 5;
		case input:
			return num >= 2;
		}
		return false;
	}

	public boolean isLazy() {
		return false;
	}
}
