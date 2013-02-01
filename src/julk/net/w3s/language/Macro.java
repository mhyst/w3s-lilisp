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

package julk.net.w3s.language;


public class Macro {
	private String filename;
	private int line;
	private String name;
	private Arguments args;
	private Macros container;
	private String code;
	
	public Macro(String name, Arguments args, Instruction inst, Macros container, String code) {
		this.name = name;
		this.args = args;
		this.filename = inst.getFile();
		this.line = inst.getLine();
		this.container = container;
		this.code = code;
	}
	
	private boolean checkArguments(Arguments actargs) {
		if (args.countArgs() != actargs.countArgs()-1) {
			return false;
		}
		return true;
	}
	
	public String call(Instruction inst) throws Exception {
		Arguments actargs = inst.getArgs();
		if (!checkArguments(actargs)) {
			String res =  "function call "+name+": bad arguments : "+actargs.getCode();
			return res;
		}
		String code = this.code;
		for (int i = 0; i < args.countArgs(); i++) {
			code = code.replace(args.argumentAt(i).getValue(),
								actargs.argumentAt(i+1).getValue());
		}
		return code;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public int getLine() {
		return line;
	}

	public void setLine(int line) {
		this.line = line;
	}
	
	public Macros getContainer() {
		return container;
	}
}
