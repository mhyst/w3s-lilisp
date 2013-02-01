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
/* 
 * Ideas para esta clase:
 * 
 * Cada función puede ser definida como un nuevo programa.
 * Para ello, cada programa debe devolver un valor que,
 * en el caso de las funciones será el valor de retorno.
 * 
 * Para llamar a una funcion, se pasan los parámetros como
 * variables locales y se instancia un objeto Program
 * con el código de la función, tras lo cual se llama al 
 * método execute.
 * 
 * Al leerse el código se van creando las instrucciones una
 * a una, almacenándose en un vector. Cada instrucción se
 * creará con el nombre del archivo de donde procede y su
 * número de línea. En caso de error deben mencionarse estos
 * datos y el código que provocó el error.
 * 
 * Sería interesante que las funciones pudiesen devolver 
 * código fuente para su posterior procesamiento. Esto
 * evitaría la necesidad de preprocesar los fuentes para
 * cargar fragmentos de código de otros archivos.
 */
import java.io.BufferedReader;
//import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Stack;

import julk.net.w3s.annotations.ClassInfo;
import julk.net.w3s.language.bit.*;

@ClassInfo (
		id = 3,
		name = "Program",
		creationDate = "March 10, 2008",
		description = "Main interpreter program"
)
public class Program {
	
	//private String code;
	private References references; //Contain the actual objects stored behind variable names
	private Variables global;	   //Variables (linked to references)
	private Types types;		   //User defined types (structures)
	private Stack<Variables> temp; //Calling stack for variables scope
	private Functions functions;   //User defined functions
	private Macros macros;	       //User defined macros
	private RequestInput input;    //Provides input to Lilisp programs

	private int line;
	
	//Debug mode (true = ON/ false = OFF)
	//public static final boolean DEBUG = true;
	private boolean DEBUG = false;
	
	public Program() {
		references = new References();
		global = new Variables(references);
		global.setNamespace("Global");
		types = new Types("Global", references);
		temp = new Stack<Variables>();
		temp.push(global);
		functions = new Functions(this);
		macros = new Macros(this);
		//input = new RequestInput();
	}
	
	/**
	 * This method is called to execute functions and flow
	 * control code.
	 * 
	 * @param code
	 * @param orig
	 * @param loop
	 * @return
	 * @throws Exception
	 */
	public String execute (String code, Instruction orig, boolean loop) 
	throws Exception {
		if (code == null || code.length() == 0)
			return "";
		String xcode = code.trim();
		if (!Type.list.isValidValue(xcode)) 
			return code;
		//if (code.charAt(0) != '(') return code;
		int instNumber = 1;
		String result = "", icode;
		List lcode = new List(xcode);
		lcode.makeMultiList();
		while ((icode = lcode.head()) != null) {
			
 			if (icode == null || icode.length() == 0) {
				break;
			}
 			if (icode.trim().length() == 0)
 				continue;
			Instruction instr = new Instruction(orig.getFile(), orig.getLine()+instNumber, icode);
			if (DEBUG) {
				System.out.println(instr.getFile()+"/"+instr.getLine()+":"+instr.getCode());
			}			
			result = evaluate(instr);
			instNumber++;			
			if (result.equalsIgnoreCase("error")) {
				return "error";
			}
			if (DEBUG) {
				System.out.println(result);
			}
			lcode = lcode.tail();
		}
		return result;
	}
	
	/**
	 * Nueva funcion de ejecución de archivos
	 * @param filename 
	 * @return
	 */
	public boolean executeFile(String filename) {
		//Testing filename
		if (filename == null || filename.length() == 0) {
			return false;
		}
		
		String retval = "";
		
		//We control the errors it may result it.
		try {
			//First we read all the file into a String (code)
			//That can be a problem in to future, for big programs.
			//A possible solution is to divide your program into several files
			//using the "include" primitive.
			BufferedReader in = new BufferedReader(new FileReader(filename), 4096);
			StringBuffer buff = new StringBuffer();
			
			String line = null;
			while((line = in.readLine()) != null) {
				buff.append(line+"\r\n");
				//code += (char) c;
			}
			in.close();
			
			String code = buff.toString();
			//Once all the code is in the String code
			//We turn it into a List (of instructions)
			List lcode = new List(code);
			
			String head = "";	//the current instruction
			Instruction inst;
			
			String result;	    //the result of instructions evaluation
			
			//We just read instruction by instruction using List's "head" method.
			//It will return the first element (instruction in this case) of the list
			while ((head = lcode.head()) != null) {
				
				//Now we have the code for a instruction
				//we create a instruction.
				//Instructions need to know: the file they're located in,
				//                           the number of line it's on,
				//							 and the code itself.
				inst = new Instruction(filename,lcode.iLine,head);
				
				//If on debug mode we print the instruction information
				if (DEBUG) {
					System.out.println(inst.getFile()+"/"+inst.getLine()+":"+inst.getCode());
				}
				
				//Instruction evaluation (execution)
				result = evaluate(inst);
				
				/*if (result.equalsIgnoreCase("error")) {
					break;
				}*/
				
				//If on debug mode, we also show the result.
				if (DEBUG) {
					System.out.println("Result: "+result);
				}
				
				//We retrieve the tail of the list
				//in order to reach the following instruction.
				lcode = lcode.tail();
			}
		} catch (ReturnException re) {
			//The program returned a value
			retval = re.getMessage();
		
			if (References.isReference(retval)) {
				Reference ref = references.get(retval);
				ref.type = Type.string;
				retval = ref.value.toString();				
			}
		} catch (CodeException ce) {
			//Coding error
			return false;
		} catch (Exception e) {
			//Unknown error
			//This should tend to disappear.
			e.printStackTrace();
			return false;
		}
		
		//If on debug mode, we signal the end of the program
		if (DEBUG) {
			//System.out.println("File "+filename+" read. "+getReturnValue());
			System.out.println("File "+filename+" read. "+retval);
		}
		
		//Return value... true if it worked well...
		//I'm not sure if this is neceesary... maybe in the future 
		return true;
	}
	
	/**
	 * This method evaluates a instruction recursively.
	 * It have also into account macros and functions.
	 * @param instr
	 * @return
	 * @throws Exception
	 */
	public String evaluate(Instruction instr) throws Exception {
		//We extract the instruction arguments
		Arguments args = instr.getArgs();
		String code;
		
		//Is it a macro?
		//Macros have to be evaluated first, because macros evaluation
		//modify the code.
		Macro m; 
		if ((m = macros.get(instr.getCmd())) != null) {
			Instruction inst = new Instruction(m.call(instr));
			inst.setFile(instr.getFile());
			inst.setLine(instr.getLine());
			
			return evaluate(inst);
		}

		//If it isn't a macro... perhaps it is a Bit.
		//Bits here are standard components of the language. 
		BitCommand bit = Bit.locateBit(instr);
		
		//If it's not a bit, or the bit is lazy...
		//If a bit is lazy, that means that it shouldn't have
		//its arguments evaluated. That's the case of control structures
		//or function definition instructions.
		if (bit == null || !bit.isLazy()) {
			//Here we have to see if some arguments
			//have to be evaluated before evaluating the entire instruction.
			//That's pretty common on Lilisp
			for (int i = 0; i < args.countArgs(); i++) {
				//We get an argument
				Argument arg = args.argumentAt(i);
				
				//If it's quoted... then it's not evaluable
				if (arg.isQuoted())
					continue;
				
				//We get the code of the argument to analyze it
				code = arg.getValue();
				
				//If it starts with '(' that's typically an embedded instruction
				if (code.length() > 0 && code.charAt(0) == '(') {
					Instruction aux = new Instruction(instr.getFile(), instr.getLine(), code);
					String result = evaluate(aux);
					/*if (result.equalsIgnoreCase("error")) {
						throw new Exception("Error: File "+instr.getFile()+
								            " line "+instr.getLine()+
								            " evaluation error.");
					}*/
					//We replace the argument with the result of its evaluation
					arg = new Argument(result);
					args.setArgumentAt(arg, i);
					if (i == 0) {	
						//Is it the first argument?
						//Then it's a primitive
						//We'll have to replace the instruction's command or primitive
						if (References.isReference(result)) {
							Reference ref = references.get(result);
							instr.setCmd(ref.value.toString());
						} else {
							instr.setCmd(result);
						}
						instr.getArgs().argumentAt(i).setValue(instr.getCmd());
						//We have also to replace the current bit
						//because the primitive was changed on the fly
						bit = Bit.locateBit(instr);
					}
				}
			}
		}
		//Now it's the time to execute our instruction
		if (bit != null)
			//If it is a bit... 
			return Bit.evaluateBit(this, getTemp(), instr);
		else {
			//If it is a function...
			return functions.evaluate(this, getTemp(), instr);
		}
	}
	
	/**
	 * This is the improved version of 
	 * readInstruction. It takes instructions
	 * from a file and calculates line numbers.
	 * Takes into account comments and spaces.
	 * 
	 * It's now deprecated because I wanted to be
	 * logic. Now a program is a list (of instructions),
	 * so, why not using a list to read the instructions?
	 * So I did. now executeFile loads instructions using
	 * a list. Though, this version of readInstruction
	 * works pretty well. If you're having problems
	 * with the list, and you want a quick solution,
	 * reuse this method.
	 * 
	 * By now I'll deprecate it to prevent using it.
	 * 
	 * @deprecated
	 * @param file
	 * @param in
	 * @return
	 */
	public Instruction readInstruction(String file, BufferedReader in) {
		
		//No hay buffer, no hay instrucciones
		if (in == null) return null;
		
		//Datos para procesar caracteres
		boolean quotes = false; //Entre comillas?
		boolean comment = false; //Es un comentario?
		char commentInit = ' ';
		String code = ""; //Contendrá el código resultante;
		int blocks = 0;
		boolean hasInstruction = false;
		boolean hasLine = false;
		char c,     //actual character 
		     pc = 'X';    //previous character
		int myLine = 0;  //Current instruction's line

		//If the line number 
		if (line == 0) {
			line = 1;
		}
		int ic;
		try {
			while ((ic = in.read()) != -1) {
				if (code.trim().length() > 0 && !hasLine) {
					/*if (code.charAt(0) == ';' ||
						code.charAt(0) == '#') {
						if (comment) continue;
						commentInit = code.charAt(0);
						comment = true;
						code = "";
						continue;
					}*/
					myLine = line;
					hasLine = true;
				}
				c = (char)ic;
				if (blocks < 0) {
					System.out.println("Code error: Spare parenthesys on file "+file+" line "+myLine);
					return null;
				}
				if (c == '\n')
					line++;
				if (c == '\"' && !comment) {
					quotes = !quotes;
					code += c;
					pc = c;
					continue;
				}
				if (quotes) {
					code += c;
					continue;
				}
				if (c == '(' && !comment && !quotes)  {
					hasInstruction = true;
					blocks++;
				}
				if (c == ')' && !comment && !quotes) blocks--;
				if (c == '#')  {
					if (!comment) commentInit = '#';
					comment = !comment;
					continue;
				}
				if (c == ';' && !comment) {
					commentInit = ';';
					comment = !comment;
				}
				if (c == '\n' && comment && commentInit == ';') {
					comment = false;
				}
				if (comment) 
					continue;
				if (c == '\t') c = ' ';
				if (c == 0x0d || c == 0x0a) { // || c == '\t') {
					continue;
				}
				if (c == ' ' && (pc == ' ' || pc == '(')) {
					pc = c;
					continue;
				}
				code += c;
				pc = c;
				if (blocks == 0 && hasInstruction) {
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		if (ic == -1) {
			if (blocks > 0 && hasInstruction ) {
				System.out.println("Code error: end of file reached, parenthesys missing. Error located in file "+file+" from line "+myLine+" on.");
				return null;
			} else if (!hasInstruction){
				return null;
			}
		}
		//return code;
		Instruction inst = null;
		try {
			inst = new Instruction(file,myLine,code.trim());
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return inst;
	}		

	/**
	 * From here on there's a set of service functions
	 * used mostly during sepatate instruction
	 * evaluation.
	 * 
	 */
	
	public Functions getFunctions() {
		return functions;
	}

	public void setFunctions(Functions functions) {
		this.functions = functions;
	}

	public Variables getGlobal() {
		return global;
	}

	public void setGlobal(Variables global) {
		this.global = global;
	}
	

	public References getReferences() {
		return references;
	}

	public Macros getMacros() {
		return macros;
	}

	public boolean isDebug() {
		return DEBUG;
	}

	public void setDebug(boolean debug) {
		DEBUG = debug;
	}

	public RequestInput getInput() {
		if (input == null) {
			input = new RequestInput();
		}
		return input;
	}

	public void setInput(RequestInput input) {
		this.input = input;
	}

	public Types getTypes() {
		return types;
	}

	public void setTypes(Types types) {
		this.types = types;
	}

	/**
	 * The following three methods
	 * are the core of the stack.
	 * This allows functions to be
	 * recursive.
	 */
	
	public synchronized void setTemp(Variables vars) {
		temp.push(vars);
	}
	
	public synchronized Variables setTemp() {
		return temp.pop();
	}	
	
	public Variables getTemp() {
		return temp.peek();
	}

	/**
	 * These functions deal with variables.
	 * 
	 * In case of doubt, just use recurseVar.
	 */
	
	/**
	 * Seek a variable along the stack
	 * starting from the nearest scope.
	 * This allows functions to "see"
	 * global variables.
	 */
	public Variable getVar(String name) {
		//Test local variables
		Variable var = getTemp().get(name);
		if (var == null) {
			//Test global variables
			var = getGlobal().get(name);
			if (var == null) {
				return null;
			} 
		}
		return var;
	}
	
	public Variable recurseVar(String name) {
		Variable var = getVar(name), aux = null;
		while (var != null) {
			aux = var;
			var = getVar(var.ref.value.toString());
		}
		return aux;
	}

	/*public String[] fetchArgs(Arguments args) {
		String[] str = new String[args.countArgs()-1];
		Variable var;
		Argument arg;
		for (int i = 1; i < args.countArgs(); i++) {
			arg = args.argumentAt(i);
			var = recurseVar(arg.getValue());
			if (var == null) {
				str[i-1] = arg.getValue();
			} else {
				str[i-1] = var.ref.value.toString();
			}
			if (str[i-1].length() > 0 && str[i-1].charAt(0) == '\"') {
				str[i-1] = str[i-1].substring(1, str[i-1].length()-1);
			}
		}
		return str;
	}*/
	
	/*public void fetchVars(Arguments args) {
		Variable var = null;
		
		for (int i = 1; i < args.countArgs(); i++) {
			var = recurseVar(args.argumentAt(i).getValue());
			if (var != null) {
				args.argumentAt(i).setVar(var);
			}
		}
		
	}*/
	
	/**
	 * ================================
	 * === M A I N      M E T H O D ===
	 * ================================    
	 * @param args Receives the program file name to be interpreted
	 */
	public static void main(String[] args) {
		//Programa real
		if (args.length < 1) {
			System.out.println("Usage: Program file");
			return;
		}
		Program  p = new Program();
		p.executeFile(args[0]);
	}
}
