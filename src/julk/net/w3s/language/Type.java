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

import julk.net.w3s.Config;
import julk.net.w3s.Link;
import julk.net.w3s.LinksManager;

public enum Type {
	numeric(java.lang.Long.class) {
		public boolean isValidValue(Object value) {
			try {
				Long.parseLong(value.toString());
				return true;
			} catch (Exception e) {
				return false;
			}
		}
		
		public Object getObject(String value, Reference ref) {
			return new Long(value);
		}
	},
	decimal(java.lang.Double.class) {
		public boolean isValidValue(Object value) {
			try {
				Double.parseDouble(value.toString());
				return true;
			} catch (Exception e) {
				return false;
			}
		}
		
		public Object getObject(String value, Reference ref) {
			return new Double(value);
		}
	},
	logic(java.lang.Boolean.class) {
		public boolean isValidValue(Object value) {
			String b = value.toString();
			return b.equalsIgnoreCase("true") || b.equalsIgnoreCase("false") ? true : false;
		}
		
		public Object getObject(String value, Reference ref) {
			return new Boolean(value);
		}
	},
	character(java.lang.Character.class) {
		public boolean isValidValue(Object value) {
			if (value.toString().length() != 1) {
				return false;
			} else {
				return true;
			}
		}
		
		public Object getObject(String value, Reference ref) {
			return value.charAt(0);
		}
	},
	links(julk.net.w3s.LinksManager.class) {
		public boolean isValidValue(Object value) {
			return value.getClass().getName().equals("julk.net.w3s.LinksManager") ? true: false;
		}
		
		public Object getObject(String value, Reference ref) {
			return ref.value;
		}
	},
	string(java.lang.String.class) {
		public boolean isValidValue(Object value) {
			return value.getClass().getName().equals("java.lang.String") ? true: false;
		}
		
		public Object getObject(String value, Reference ref) {
			return value;
		}
	},
	array(java.lang.Object[].class) {
		public boolean isValidValue(Object value) {
			try {
				Object[] a = (Object[]) value;
				if (a.length>0)
					return true;
				else 
					return false;
			} catch (Exception e) {
				return false;
			}
		}
		
		public Object getObject(String value, Reference ref) {
			return ref.value;
		}
	},
	list(julk.net.w3s.language.List.class) {
		public boolean isValidValue(Object value) {
			try {
				List l = (List) value;
				l.head();
				return true;
			} catch (Exception e) {
				try {
					List l = new List(value.toString());
					l.head();
					return true;
				} catch (Exception ie) {
					return false;
				}
			}
		}
		
		public Object getObject(String value, Reference ref) {
			if (ref != null)
				return ref.value;
			else {
				try {
					List l = new List(value);
					return l;
				} catch (Exception e) {
					return null;
				}
			}
		}
	},
	type(julk.net.w3s.language.Variables.class) {		
		public boolean isValidValue(Object value) {
			try {
				Variables v = (Variables) value;
				v.getNamespace();
				return true;
			} catch (Exception e) {
				return false;
			}
		}
		
		public Object getObject(String value, Reference ref) {
			return ref.value;
		}
	},
	function(julk.net.w3s.language.Function.class) {		
		public boolean isValidValue(Object value) {
			try {
				Function f = (Function) value;
				f.getCreationInstruction();
				return true;
			} catch (Exception e) {
				return false;
			}
		}
		
		public Object getObject(String value, Reference ref) {
			return ref.value;
		}
	},
	file(julk.net.w3s.language.File.class) {		
		public boolean isValidValue(Object value) {
			try {
				File f = (File) value;
				f.nop();
				return true;
			} catch (Exception e) {
				return false;
			}
		}
		
		public Object getObject(String value, Reference ref) {
			return ref.value;
		}
	},
	link(julk.net.w3s.Link.class) {
		public boolean isValidValue(Object value) {
			return value.getClass().getName().equals("julk.net.w3s.Link") ? true: false;
		}
		
		public Object getObject(String value, Reference ref) {
			return ref.value;
		}
	},
	object(java.lang.Object.class) {
		public boolean isValidValue(Object value) {
			return true;
		}
		
		public Object getObject(String value, Reference ref) {
			return ref.value;
		}
	},
	nil(java.lang.Object.class) {
		public boolean isValidValue(Object value) {
			return value == null;
		}
		
		public Object getObject(String value, Reference ref) {
			return null;
		}
	};

	
	@SuppressWarnings("rawtypes")
	public Class _class;
	
	private Type(@SuppressWarnings("rawtypes") Class _class) {
		this._class = _class;
	}
	
	public static Type getType(String type) {
		try {
			Type t = Type.valueOf(type);
			return t;
		} catch (Exception e) {
			return null;
		}
	}
	
	public abstract boolean isValidValue(Object value);
	
	public abstract Object getObject(String value, Reference ref);
	
	public static Type determineTheType(Object value) {
		if (value == null) return null;
		for (Type t : Type.values()) {
			if (t.isValidValue(value))
				return t;
		}
		return null;
	}
	
	@SuppressWarnings("rawtypes")
	public boolean isTypeValid(Class c) {
		return c == _class;
	}
	
	public static void main(String[] args) {
		Link lnk = new Link("test","http://www.google.es",null);
		String s = "Esto es una cadena";
		String i = "2";
		String d = "3.1";
		String b = "false";
		String c = "c";
		LinksManager lm = new LinksManager(new Config());
		Object obj = new Object();
		Object[] objs = new Object[8];
		objs[0] = lnk;
		objs[1] = s;
		objs[2] = i;
		objs[3] = d;
		objs[4] = b;
		objs[5] = c;
		objs[6] = lm;
		objs[7] = obj;
		for (int j = 0; j < objs.length; j++) {
			System.out.println("Objeto: "+j+" tipo: "+Type.determineTheType(objs[j]));
		}
	}
	
	public Type classOf(String _class) {
		for (Type t : Type.values()) {
			if (t._class.equals(_class)) {
				return t;
			}
		}
		return null;
	}
	
	public static boolean isNumeric(Type t) {
		switch (t) {
		case numeric:
		case decimal:
			return true;
		default:
			return false;
		}
	}

	public boolean isNumeric() {
		switch (this) {
		case numeric:
		case decimal:
			return true;
		default:
			return false;
		}
	}
	
	public static double[] getNumbers(Object[] objs) {
		double[] nums = new double[objs.length];
		int i = 0;
		for (Object o : objs) {
			Type t = Type.determineTheType(o);
			if (!t.isNumeric()) return null;
			try {
				nums[i++] = Double.parseDouble(o.toString());
			} catch (Exception e) {
				return null;
			}
		}
		return nums;
	}
	
	/**
	 * This is only for numbers
	 * @param refs
	 * @return
	 */
	public static Type determineTheResultType(Reference[] refs) {
		for (Reference ref : refs) {
			if (ref.type == decimal) 
				return decimal;
		}
		return numeric;
	}
	
	public static String newNumericReference(References references, Type type, double num) {
		Reference ref = new Reference();
		ref.type = type;
		if (type == numeric) 
			ref.value = Math.round(num);
		else
			ref.value = num;
		return references.newReference(ref);
	}
	
	public static Object adjustResultValue(Reference[] refs, double value) {
		if (Type.determineTheResultType(refs) == Type.numeric)
			return Math.round(value);
		else
			return value;
	}
}
