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

import  julk.net.w3s.language.bit.*;


public class Function implements Cloneable{
	private String filename;
	private int line;
	private String name;
	private Arguments args;
	private Variables local;
	//private Variables properties;
	private Functions container;
	private String code;
	private Instruction inst;
	private int recursiveLevel;
	
	public Function(String name, Arguments args, Instruction inst, Functions container, String code) {
		this.name = name;
		this.args = args;
		this.filename = inst.getFile();
		this.line = inst.getLine();
		this.container = container;
		this.code = code;
		this.inst = inst;
		//local = new Variables(new References());
		local = new Variables(container.getProgram().getReferences());
		local.setNamespace("Local");		
		/*properties = new Variables(new References());
		properties.setNamespace("Properties");*/
		recursiveLevel = 0;
	}
	
	public Object clone() {
		try {
			Function f = (Function) super.clone();
			f.local = (Variables) this.local.clone();
			
			//f.properties = new Variables(new References());
			
			return f;
		} catch (Exception e) {
			return null;
		}
	}
	
	public Reference getProperty(String name) throws Exception {
		Variable var = local.get(name);
		if (var == null) {
			throw new CodeException("Property not found");
		}
		return var.ref;
	}
	
	public void setProperty(String name, Reference ref) throws Exception {
		Variable var = local.get(name);
		if (var == null) {
			var = new Variable(name, ref);
			local.set(name, var);
		} else {
			var.ref = ref;
		}
	}
	
	private boolean checkArguments(Arguments actargs) {
		if (args.countArgs() != actargs.countArgs()-1) {
			return false;
		}
		return true;
	}
	
	private void argsToVars(Arguments act, Program p, Variables vars, Instruction inst) throws Exception{
		Object[] objs = Bit.getObjects(p, vars, inst, 1);
		
		for (int i = 0; i < args.countArgs(); i++) {
			Reference ref = new Reference();
			ref.value = objs[i];
			ref.type = Type.determineTheType(ref.value);
			local.set(args.argumentAt(i).getValue(), ref);
		}
	}
	
	private void stackVars() {
		if (recursiveLevel > 0) {
			local = (Variables) local.clone();
		}
		container.getProgram().setTemp(local);
		recursiveLevel++;
	}
	
	private void unStackVars() {
		local = container.getProgram().setTemp();
		recursiveLevel--;
	}
	
	public String call(Instruction inst) throws Exception {
		Arguments actargs = inst.getArgs();
		if (!checkArguments(actargs)) {
			Bit.error(inst, "function call "+name+": bad arguments : "+actargs.getCode());
		}
		Program p = container.getProgram();
		stackVars();
		argsToVars(actargs, p, p.getTemp(), inst);
		inst.setFile(inst.getFile()+"->"+filename+"\\function "+inst.getCmd());
		inst.setLine(line);
		String retval = "", result = "";
		try {
			result = p.execute(code, inst, false);
		} catch (ReturnException re) {
			retval = re.getMessage();
			if (References.isReference(retval)) {
				Reference ref = p.getReferences().get(retval);
				retval = ref.address;
			}
		}
		unStackVars();
		return retval.length() > 0 ? retval : result;
	}

	public Arguments getArgs() {
		return args;
	}

	public void setArgs(Arguments args) {
		this.args = args;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Functions getContainer() {
		return container;
	}

	public void setContainer(Functions container) {
		this.container = container;
	}

	public Variables getLocal() {
		return local;
	}

	public void setLocal(Variables local) {
		this.local = local;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public Instruction getCreationInstruction() {
		return inst;
	}

	public void setCreationInstruction(Instruction inst) {
		this.inst = inst;
	}
}