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

import julk.net.w3s.language.bit.Bit;

public class Instruction {
	private String file;
	private int line;
	private String cmd;
	private Arguments args;
	private String code;
	private Variable returnVal;
	
	public Instruction(String code) throws Exception {
		code = code.trim(); 
		if (!code.startsWith("(")) {
			code = "("+code+")";
		}
		this.code = code;
		args = new Arguments();
		try {
			decode();
		} catch (Throwable t) {
			Bit.error(this,"Invalid list for new instruction");
		}
		if (args.countArgs() > 0) {
			cmd = args.argumentAt(0).getValue();
		} else {
			cmd = code;
		}
	}
	
	public Instruction(String file, int line, String code) throws Exception {
		this(code);
		this.file = file;
		this.line = line;
	}
	
	public Arguments convertToArguments() {
		return args;
	}
	
	public int countArguments() {
		return args.countArgs();
	}
	
	/*private void decodeOld() throws Exception{
		int len;
		if (code == null|| (len = code.length()) == 0) {
			throw new Exception("No code to decode");
		}
		
		
		if (code.charAt(0) != '(' || code.charAt(code.length()-1) != ')') {
			cmd = null;
			return;
		}
		//code = code.substring(1,code.length()-1);
		int blocks = 0, pos = 1;
		boolean quotes = false;
		int c, pc = 'X';
		for (int i = 1; i < len; i++) {
			c = code.charAt(i);
			if (c == '(' && !quotes) blocks++;
			if (c == ')' && !quotes) blocks--;
			if (blocks == 0) {
				if (c == ' ' && pc == ' ')
					continue;
			}
			if (c == '\"') {
				quotes = !quotes;
			}
			if (quotes) continue;
			if (blocks == 0 && c == ' ') {
				args.add(new Argument(code.substring(pos,i)));
				pos = i+1;
			}
			pc = c;
		}
		
		args.add(new Argument(code.substring(pos,len-1)));
	}*/
	
	private void decode() throws Exception {
		List l = new List(code);
		
		String head = null;
		while ((head = l.head()) != null) {
			args.add(new Argument(head));
			l = l.tail();
		}
	}

	public Arguments getArgs() {
		return args;
	}

	public void setArgs(Arguments args) {
		this.args = args;
	}

	public String getCmd() {
		return cmd;
	}

	public void setCmd(String cmd) {
		this.cmd = cmd;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) throws Exception {
		code = code.trim(); 
		if (!code.startsWith("(")) {
			code = "("+code+")";
		}
		this.code = code;
		args = new Arguments();
		try {
			decode();
		} catch (Throwable t) {
			Bit.error(this,"Invalid list for new instruction");
		}
		if (args.countArgs() > 0) {
			cmd = args.argumentAt(0).getValue();
		} else {
			cmd = code;
		}
	}

	public Variable getReturnVal() {
		return returnVal;
	}

	public void setReturnVal(Variable returnVal) {
		this.returnVal = returnVal;
	}

	public String getFile() {
		return file;
	}

	public void setFile(String file) {
		this.file = file;
	}

	public int getLine() {
		return line;
	}

	public void setLine(int line) {
		this.line = line;
	}
}
