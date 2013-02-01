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

import java.util.Iterator;
import java.util.Vector;

public class Arguments {
	private Vector<Argument> arguments;
	private Iterator<Argument> i;
	private int numargs;
	
	public Arguments() {
		arguments = new Vector<Argument>();
		i = null;
		numargs = 0;
	}
	
	public String getCode() {
		String res = "(";
		for (int i = 0; i < countArgs(); i++) {
			res += arguments.elementAt(i).getValue();
			if (i < countArgs()-1) {
				res += " ";
			}
		}
		res += ')';
		return res;
	}
		
	public Argument argumentAt(int n) {
		return arguments.elementAt(n);
	}
	
	public void setArgumentAt(Argument arg, int n) {
		arguments.setElementAt(arg, n);
	}
	
	public Argument next() {
		if (i == null) {
			i = arguments.iterator();
		}
		if (i.hasNext()) {
			return i.next();
		} else {
			return null;
		}
	}
	
	public void add(Argument arg) {
		arguments.addElement(arg);
		numargs++;
	}
	
	public boolean isEmpty() {
		return arguments.isEmpty();
	}
	
	public boolean fits(Arguments args) {
		if (numargs == args.numargs) {
			Iterator<Argument> a1 = arguments.iterator();
			Iterator<Argument> a2 = args.arguments.iterator();
			while (a1.hasNext() && a2.hasNext()) {
				Argument arg1 = a1.next();
				Argument arg2 = a2.next();
				if (!arg1.fits(arg2)) {
					return false;
				}
			}
		}
		return true;
	}

	public int countArgs() {
		arguments.trimToSize();
		return arguments.size();
	}
	
	public static void main(String[] args) {
		Arguments arg1 = new Arguments();
		Arguments arg2 = new Arguments();
		
		arg1.add(new Argument("link", "lnk"));
		arg1.add(new Argument("link", "father"));
		if (arg1.fits(arg2)) {
			System.out.println("Ajusta erroneamente");
		}
		arg2.add(new Argument("link", "lnk"));
		arg2.add(new Argument("link", "father"));
		if (arg1.fits(arg2)) {
			System.out.println("Ajusta bien");
		}
	}
}
