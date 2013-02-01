/*
    W3S Web Personal Spider
    Copyright (C) 2008  Julio Cesar Serrano Ortuno

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package julk.net.w3s;

import java.util.Hashtable;

/**********************************************************************************
 * CLASS: W3SDOS 
 *
 * This class is only a DOS user interface that
 * allows a user to make searches.
 *
 * It can be enterely rewriten and discarded without
 * danger.
 *
 * @author Julio Cesar Serrano Ortuno
 * created January 26, 2008
 * updated February 10, 2008
 * license General Public License
 **********************************************************************************/
public class W3SDOS {

	/*private static String[] PARMS = {"seed","terms","max","cut",
			                  		 "depth","internal","external",
			                  		 "priorize"};*/
	private static String CMDSIGN = "--";
	/**
	 * Given a command returns the command id
	 * @param command A command
	 * @return The command id
	 */
	/*private static int getIdCommand(String command) {
		int i;
		for (i = 0; i < PARMS.length && PARMS[i] != command; i++);
		if (i > PARMS.length) return -1;
		return i;
	}*/
	
	private static void showMan() {
		String man = Config.getFullName()+"\n"+
					 "Searching Web Spider\n\n"+
					 "Parameters description:\n"+
					 "\t--seed:websiteaddress\n"+
					 "\t\tSet the starting page.\n\n"+
					 "\t--terms:term1,term2,...,termn\n"+
					 "\t\tSearch terms.\n\n"+
					 "\t--depth:n\n"+
					 "\t\tMax depth the bot can reach.\n\n"+
					 "\t--max:n\n"+
					 "\t\tMáximum number of entries the bot should process.\n"+
					 "\t\tIf 0 this limitation is ignored.\n\n"+
					 "\t--internal\n"+
					 "\t\tAsks the bot to follow only internal links.\n\n"+
					 "\t--external\n"+
					 "\t\tAsks the bot to follow only external links.\n\n"+
					 "\t--cut\n"+
					 "\t\tDiscards links from pages which don't have the search terms.\n\n"+
					 "\t--priorize\n"+
					 "\t\tWhen selecting the next link to be processed,\n"+
					 "\t\tgo first to the most close to search terms.\n";
					 System.out.println(man);
	}
	
	private static boolean isCommand(String arg) {
		return arg.startsWith(CMDSIGN) ? true : false;
	}
	
	
	private static int searchForCommand(String[] args, int begin) {
		int i;
		
		if (begin >= args.length) return -1;
		for (i = begin; i < args.length && !isCommand(args[i]); i++);
		if (i < args.length) return i;
		else return -1;
	}
	
	private static String[] splitCommand(String parm) {
		int i = parm.indexOf(":");
		String[] parts = new String[2]; 
		if (i == -1) {
			parts[0] = parm.substring(2);
			parts[1] = "TRUE";
		} else {
			parts[0] = parm.substring(2,i);
			parts[1] = parm.substring(i+1);
		}
		return parts;
	}
	
	private static Hashtable<String,String> getParameters(String[] args) {
		Hashtable<String, String> parameters = new Hashtable<String, String>();
		int idi = 0, idf = 0;
		String parm;
		
		idi = searchForCommand(args,0);
		if (idi == -1) {
			showMan();
			System.exit(-1);
		}
		do {
			idf = searchForCommand(args,idi+1);
			if (idf == -1) 
				idf = args.length;
			parm = args[idi];
			for (int i = idi+1; i < idf; i++) {
				parm += " "+args[i];
			}

			String[] parts = splitCommand(parm);
			parameters.put(parts[0], parts[1]);
			/*System.out.println(parm);
			System.out.println("Parte1: "+parts[0]+" Parte2: "+parts[1]);*/
			idi = idf;
		} while(idf < args.length);
		return parameters;
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Hashtable<String, String> parms;
		
		if (args.length == 0) {
			showMan();
			System.exit(-1);
		}
		//We retrieve user parameters 
		parms = getParameters(args);
		if (parms == null) {
			showMan();
			System.exit(-1);			
		}
		String seed = parms.get("seed");
		if (seed == null || seed.length() == 0) {
			System.out.println("You must enter a seed via parameter --seed:http://websiteaddress");
			showMan();
			System.exit(-1);
		}
		String sterms = parms.get("terms");
		if (sterms == null || sterms.length() == 0) {
			System.out.println("You must enter search terms via parameter --terms:term1,term2, ..., termn");
			showMan();
			System.exit(-1);
		}		
		String sdepth = parms.get("depth");
		if (sdepth == null || sdepth.length() == 0) {
			sdepth = "2";
		}
		String smax = parms.get("max");
		if (smax == null || smax.length() == 0) {
			smax = "0";
		}
		String internal = parms.get("internal");
		if (internal == null || internal.length() == 0) {
			internal = "FALSE";
		}
		String external = parms.get("external");
		if (external == null || external.length() == 0) {
			external = "FALSE";
		}
		String cut = parms.get("cut");
		if (cut == null || cut.length() == 0) {
			cut = "FALSE";
		}
		String priorize = parms.get("priorize");
		if (priorize == null || priorize.length() == 0) {
			priorize = "FALSE";
		}
		String filename = parms.get("filename");
		if (filename == null || filename.length() == 0) {
			filename = "output.htm";
		}

		
		
		//Parameters processing
		String[] terms = sterms.split(",");
		int depth = 2, max = 0;
		try {
			depth = Integer.parseInt(sdepth);
		} catch (Exception e) {
			depth = 2;
		}
		try {
			max = Integer.parseInt(smax);
		} catch (Exception e) {
			max = 0;
		}
		
		System.out.println(Config.getFullName());
		System.out.println("----------------------------------------");
		System.out.println("Searching: " + seed);
		System.out.println("Search terms: " + sterms);
		System.out.println("This operation might take some minutes. Please wait.");
		
		Config setup = new Config();
		setup.setProperty("DEPTH",""+depth);
		setup.setProperty("MAXENTRIES",""+max);
		setup.setProperty("ONLYINTERNAL", internal);
		setup.setProperty("ONLYEXTERNAL", external);
		setup.setProperty("PRIORIZE", priorize);
		setup.setProperty("CUTBRANCHES", cut);
		setup.setProperty("FILENAME", filename);
				
		try {				
			CrawlingAlgorithm calg = new CrawlingAlgorithm();
			calg.setAlgorithm("julk.net.w3s.LinksExplorer");
			calg.Init(seed,terms,null,setup,new SWOutput(null));
			calg.Call();
			
		} catch (Exception e) {	
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		try {
			System.out.println("\nSearching finished. Now you can browse the "+filename+" file.");
			System.out.println("Thank you.");
			String curDir = System.getProperty("user.dir");
			
			Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler "+curDir+"\\"+filename);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
}
