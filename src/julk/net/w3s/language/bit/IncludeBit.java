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

public enum IncludeBit implements BitCommand {
	include {
		public String evaluate(Program pgm, Variables vars, Instruction inst)
		throws Exception {
	    	String filename = inst.getArgs().argumentAt(1).getValue(); 
	    	if (!pgm.executeFile(filename)) {
	    		Bit.error(inst, "Included file not found!");
	    	} 
	    	return "included::"+filename;
		}
	};

	public boolean isLazy() {return false;}
	public int getNumArgs() {return 1;};
	public int getAlternativeNumArgs() {return -1;};
	public boolean isArgsNumberOK(int num) {return num==1;}
	public abstract String evaluate(Program pgm, Variables vars, Instruction inst) throws Exception;
}
