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

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Iterator;

public class List implements Iterable<String>, Iterator<String>{
	public String value;
	public int line=1;
	public int iLine=1;
	private List it;
	private static Runtime rnt = Runtime.getRuntime();
	private static long freemem = rnt.freeMemory();
	//private Arguments args;
	public List(String value) throws Exception {
		if (value == null)
			throw new CodeException("Null list ");
		
		if (!testBlocks(value))
			throw new CodeException("Brackets don't match");
		this.value = value;
		
		fix();
		//System.out.println(value);
	}
	
	public List(String value, int line, boolean tail) throws Exception{
		if (value == null)
			throw new Exception("Null list ");
		this.value = value;
		this.line = line;
	}
	
	public String toString() {
		return value;
	}
	
	public Pointer getPointer(String value, boolean head) {
		char c;
		int begin = -1, end = -1;
		long blocks = 0;
		boolean quotes = false, firstParenthesis = false;
		boolean shcomment = false, sccomment = false;
		for (int i = 0; i < value.length(); i++) {
			c = value.charAt(i);
			if (c == '\"' && !shcomment && ! sccomment) {
				if (!quotes)
					quotes = true;
				else {
					quotes = false;
					continue;
				}
				//continue;
			}
			if (c == '#' && !quotes && !sccomment) {
				shcomment = !shcomment;
				continue;
			}
			if (c == ';' && !quotes && !shcomment) {
				sccomment = true;
				continue;
			}
			/*if (quotes)
				continue;*/
			if (c == '\'' && !shcomment && ! sccomment)
				continue;
			if (c == '(' && !quotes && !shcomment && ! sccomment) {
				if (!firstParenthesis) {
					firstParenthesis = true;
					continue;
				} else { 
					blocks++;
				}
			}
			if (c == ')' && !quotes && !shcomment && ! sccomment)
				if (!firstParenthesis)
					firstParenthesis = true;
				else {
					if (begin != -1 && blocks == 0) {
						end = i;
						break;
					}
					blocks--;
				}
			//Atenci�n, probando
			if (c == '\r' || c == '\n' || c == '\t') {
				if (c == '\n') {
					if (head)
						line++;
					if (sccomment)
						sccomment = false;
				}
				c = ' ';
			}
			if (c != ' ' && (!quotes || c == '\"') && !shcomment && ! sccomment)
				if (begin == -1) {
					begin = i;
					iLine = line;
				}
			if (c == ' ' && blocks == 0 && !quotes && !shcomment && ! sccomment)
				if (begin != -1) {
					end = i;
					break;
				}
		}
		if (begin == -1 || end == -1)
			return null;
		else 
			return new Pointer(begin,end);
	}
	
	private void memoryControl() {
		long current = rnt.freeMemory();
		long spent = freemem-current;
		if (spent > 33554432) {
			System.gc();
			freemem = rnt.freeMemory();
		}
	}
	
	public String head() {
		memoryControl();
		Pointer p = getPointer(value, true);
		if (p == null)
			return null;
		else 
			return value.substring(p.begin,p.end);
	}

	/*public String headExt() {
		Pointer p = getPointer(value, false);
		if (p == null)
			return null;
		else 
			return value.substring(p.begin,p.end);
	}*/

	public List tail() throws Exception {
		Pointer p = getPointer(value, false);
		if (p == null)
			return null;
		else
			value = value.substring(p.end+1);
			if (value.length() == 0) {
				return new List("'()",line,true);
			} else {
				return new List("'("+value,line, true);
			}
	}
	
	public boolean needsFixing(String value) throws Exception {
		char c;
		long blocks = 0;
		boolean quotes = false, comment = false, scomment = false;
		boolean hadBlocks = false;
		for (int i = 0; i < value.length(); i++) {
			c = value.charAt(i);
			if (c == '\"' && !comment && !scomment) {
				quotes = !quotes;
			}
			if (c == '#') {
				comment = !comment;
				continue;
			}
			if (c == ';' && !comment && !scomment && !quotes)
				scomment = true;
			if (c == '\n' && scomment) {
				scomment = false;
			}
			if (comment || scomment)
				continue;
			if (c == '\'')
				continue;
			if (c == '(' && !quotes) {
				blocks++;
				hadBlocks = true;
			}
			if (c == ')' && !quotes) 
				blocks--;
			
			if (blocks == 0 && hadBlocks) {
				String s = value.substring(i+1);
				if (s.trim().length() > 0) {
					return true;
				}
			}
			
		}
		return false;
	}

	
	private void fix() throws Exception{
		if (needsFixing(value)) {
			value = "("+value+")";
		}
	}
	
	public long calcClosingMarks(String value) throws Exception {
		char c;
		//int line = 1;
		long cblocks = 0;
		boolean quotes = false, comment = false, scomment = false;

		for (int i = 0; i < value.length(); i++) {
			c = value.charAt(i);
			//Flags modificators analysis
			switch (c) {
			case '\n':
				line++;
				if (scomment)
					scomment = false;
				break;
			case '#':
				if (!scomment && ! quotes)
					comment = !comment;
				break;
			case ';':
				if (!comment && !scomment && !quotes)
					scomment = true;
				break;
			case '\"':
				if (!comment && !scomment)
					quotes = !quotes;
				break;	
			}
			if (comment || scomment || quotes)
				continue;
			switch (c) {
			case ')':
				cblocks++;
				break;
			}
		}
		
		return cblocks;
	}

	/**
	 * This function
	 * @param value
	 * @return
	 * @throws Exception
	 */
	public boolean testBlocks(String value) throws Exception {
		char c;
		long blocks = 0;
		int line = 1, zeroLine = 0;
		boolean quotes = false, comment = false, scomment = false;
		boolean zero = false;

		long om = 0; //Open marks (
		//We calculate how many closing marks we have
		//in our list.
		long cm = calcClosingMarks(value); //Closing marks )
		
		lfor: for (int i = 0; i < value.length(); i++) {
			c = value.charAt(i);
			//Flags modificators analysis
			switch (c) {
			case '\n':
				line++;
				if (scomment)
					scomment = false;
				break;
			case '#':
				if (!scomment && ! quotes)
					comment = !comment;
				break;
			case ';':
				if (!comment && !scomment && !quotes)
					scomment = true;
				break;
			case '\"':
				if (!comment && !scomment)
					quotes = !quotes;
				break;	
			}
			if (comment || scomment || quotes)
				continue;
			
			//Brackets analysis
			switch (c) {
			case '(':
				blocks++;
				om++;
				if (cm < om) {
					break lfor;
				}
				if (zero) {
					line = zeroLine;
					blocks = -1;
					break lfor;
				}
				break;
			case ')':
				blocks--;
				if (cm < om) {
					break lfor;
				}
				if (blocks == 0) {
					zero = true;
					zeroLine = line;
				}
				if (blocks < 0)
					break lfor;
				break;
			}
		}
		
		if (blocks > 0) {
			System.out.println("Code error: end of file reached, parenthesys missing. Error located around line "+line+".");
			return false;
		} else if (blocks < 0) {
			System.out.println("Code error: Spare parenthesys around line "+line);
			return false;
		}
		return true;
	}
	
	public void makeMultiList() {
		String head = head();
		if (!head.startsWith("(")) {
			value = "("+value+")";
		}
	}
	
	public static void main(String[] args) 
	throws Exception {
		//List l = new List("((1 2) (3 4) \"esto es una prueba\")");
		//List l = new List("(\"hola\" 2 3 \"file.txt\")");
		BufferedReader bin = new BufferedReader(new FileReader("c:\\lilisp\\if.lsp"));
		String code = "";
		int c;
		while ((c = bin.read()) != -1) {
			code += (char)c;
		}
		bin.close();
		List l = new List(code);
		String head = null;
		while ((head = l.head()) != null) {
			System.out.println(head);
			l = l.tail();
		}
	}
	
	public class Pointer {
		public int begin;
		public int end;
		
		public Pointer(int begin, int end) {
			this.begin = begin;
			this.end = end;
		}
	}

	public boolean hasNext() {
		if (it == null) {
			it = this;
		}
		return it.head() != null;
	}

	public String next() {
		// TODO Auto-generated method stub
		String o = it.head();
		try {
			it = it.tail();
		} catch (Throwable t) {
			return null;
		}
		return o;
	}

	public void remove() {
	}

	public Iterator<String> iterator() {
		return this;
	}
}
