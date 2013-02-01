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

import java.io.BufferedReader;
//import java.io.FileWriter;
//import java.io.PrintWriter;
//import java.util.Properties;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Iterator;

/**********************************************************************************
 * CLASS: AbstractAlgorithm
 * 
 * Base class for searching algorithms. This allows W3S to have
 * more than one searching algorithm. Also, this class contains
 * a basic API for any searching algorithm.
 *
 * @author Julio Cesar Serrano Ortuno
 * created November 26, 2007
 * updated February 10, 2008
 * license General Public License
 **********************************************************************************/
public abstract class AbstractAlgorithm {
	//Properties
	protected String seed;			//The search will start on this URL
	protected String[] searchTerms;			//the search terms
	
	protected long cont;						//internal counter
	
	protected LinksManager lm;				//Reference of our LinksManager
	
	protected boolean cutBranches = false;	//An option flag
	protected boolean firstTime = true;		//Is this the first call to processPage?
	protected int maxDepth;
	protected Calendar startDate;
	protected Calendar endDate;
	protected ExcludedElement[] excluded;
	
	protected Config setup;				//Search configuration properties
	
	protected String filename;				//Filaname of the results file
	protected Results results;				//Results generator
	protected Results results2;
	private boolean initialized = false;
	protected ExcludedElement currentLink;
	
	private Output op;

	/**
	 * Instantiates an object of the class LinksExplorer
	 * in order to take account of every data related to the search.
	 * 
	 * @param link initial URL where starting the search
	 * @param terms search terms
	 * @param setup configuration properties (fill affect the search)
	 * @throws Exception
	 * 
	 * For the setup param, it must be created a Properties object
	 * and the following properties must be set using the method setProperty
	 * of the class Property:
	 * 	DEPTH: máx depth to which read links (a string containing a int)
	 *  MAXENTRIES: más entries in the results page (a string containing a int)
	 *  ONLYINTERNAL: read only internal links ("TRUE" or "FALSE") 
	 *  ONLYEXTERNAL: read only external links ("TRUE" or "FALSE")
	 *  PRIORIZE: use priority to change the processing order ("TRUE" or "FALSE")
	 *  CUTBRANCHES: discards links from pages where the search terms were not found
	 *  
	 *  Example:
	 *  
	 *  Properties setup = new Properties();
	 *  setup.setProperty("PRIORIZE","TRUE");
	 *  
	 *  String[] terms = {"foo", "bar"};
	 *  LinksExplorer le = new LinksExplorer("http://www.foo.org", terms, setup);
	 *  
	 *  //All properties are set by default to false or 0
	 *  //so if you want to modify it, you'll have to
	 *  //add more "setProperty" lines.
	 * 
	 */
	public void Init (String link, String[] terms, Config setup, Output op) throws Exception {
		Init(link,terms,null,setup,op);
	}
	
	public void Init (String link, String[] terms, ExcludedElement[] excludedLinks, Config setup, Output op) throws Exception {
		seed = link;
		searchTerms = terms;
		excluded = excludedLinks;
		cont = 0;
		this.setup = setup;
		lm = new LinksManager(setup);
		filename = setup.getProperty("FILENAME","salida.htm");
		cutBranches = setup.getBoolProperty("CUTBRANCHES");
		maxDepth = setup.getIntProperty("DEPTH");
		String from = setup.getProperty("FROM");
		String to = setup.getProperty("TO");
		startDate = Calendar.getInstance();
		endDate = Calendar.getInstance();
		try {
			startDate.setTime(DateFormat.getInstance().parse(from));
		} catch (Exception e) {
			startDate = null;
		}
		try {
			endDate.setTime(DateFormat.getInstance().parse(to));
		} catch (Exception e) {
			endDate = null;
		}
		
		results2 = new Results(filename,seed,searchTerms);
		results = new Results("everything.htm",seed,searchTerms);
		this.op = op;
		setInitialized(true);
	}
	
	protected void getCurrentLinkInfo(Link lnk) {
		currentLink = null;
		if (firstTime || excluded == null || excluded.length == 0)
			return;
		
		int i;
		String link = lnk.getLink();
		for (i = 0; i < excluded.length; i++) {
			if (link.indexOf(excluded[i].getURLfragment()) != -1) {
				currentLink = excluded[i];
			}
		}
	}
	
	protected boolean isExcluded(Link lnk) {
		if (currentLink == null)
			return false;
		
		int linkMaxDepth = currentLink.getDepth();
		if (linkMaxDepth == -1 ||
		   (linkMaxDepth < lnk.getDepth())) {
			return true;
		}
		return false;
	}
	
	protected int getAlterMaxDepth(Link lnk) {
		if (currentLink == null)
			return maxDepth;

		int myDepth = currentLink.getDepth(); 
		if (myDepth == -1) {
			return maxDepth;
		} else {
			return myDepth;
		}
	}
	
	protected boolean isInTimeFrame(Calendar c) {
		boolean ev1, ev2;
		if (startDate != null) {
			ev1 = startDate.before(c);
		} else {
			ev1 = true;
		}
		if (endDate != null) {
			ev2 = endDate.after(c);
		} else {
			ev2 = true;
		}
		return (ev1 && ev2);
	}
		
	//This method allows the arranging of the dots in the screen
	//using the latter commented internal counter
	protected boolean paginate() {
		if (cont > 180) {
			cont = 0;
			return true;
		} else {
			cont++;
			return false;
		}
	}	
	
	//Public Getters and Setters
	public boolean isCutBranches() {
		return cutBranches;
	}

	public String[] getSearchTerms() {
		return searchTerms;
	}

	public void setSearchTerms(String[] searchTerms) {
		this.searchTerms = searchTerms;
	}

	public String getSeed() {
		return seed;
	}
	
	//protected getters and setters
	protected LinksManager getLinkManager() {
		return lm;
	}

	protected boolean isFirstTime() {
		return firstTime;
	}

	protected void setFirstTime(boolean firstTime) {
		this.firstTime = firstTime;
	}	

	/**
	 * Search for links in one line of text
	 * in case of finding, it submits the possible links
	 * to the LinksManager database
	 * 
	 * @param father father page
	 * @param line line on which search for links
	 */
	protected void searchLinksInLine (Link father, String line) {
		int idx, idi, idf;
		
		String HREF = "href=";
		String link, title;
		String lowerCaseLine = line.toLowerCase();
		
		idx = lowerCaseLine.indexOf(HREF);
		while (idx != -1) {
				
			idf = line.indexOf(">",idx);
			if (idf == -1) {
				link = line.substring(idx+5);
				return;
			} else {
				link = line.substring(idx+5, idf);
			}

			if (link.startsWith("\"")) {
				link = link.substring(1,link.length()-1);
				int idef = link.indexOf("\"");
				if (idef != -1)
					link = link.substring(0,idef);
			} else if (link.startsWith("\'")) {
				link = link.substring(1,link.length()-1);
				int idef = link.indexOf("\'");
				if (idef != -1)
					link = link.substring(0,idef);
			}
			
			idi = idf;
			idf = lowerCaseLine.indexOf("</a>",idi);
			if (idf == -1) idf = lowerCaseLine.length();
			try {
				title = line.substring(idi+1, idf);
			} catch (Exception e) {
				idx = lowerCaseLine.indexOf(HREF, idx+1);
				continue;
			}
			if (title != null && title.length() == 0)
				title = "[Problem with the title]";

			lm.createLink(title, link.trim(), father);
			
			idx = lowerCaseLine.indexOf(HREF, idf);
		}
	}
	
	/**
	 * Search for links in a web page
	 * It reads line by line and calls the previous function
	 * for each line
	 * @param hf HttpToFile oject represents the current page we're searching in
	 * @throws Exception
	 */
	/*protected void searchForLinks(HttpToFile hf) throws Exception {
		BufferedReader in = hf.openFile();
		String line;
		while ((line = in.readLine())!= null) {
			try {
				searchLinksInLine(hf.getLink(), line);
			} catch (Exception e) {
				//out.println("Error: "+e.getMessage());
			}
		}
		hf.closeFile();
	}*/
	
	protected void searchForLinks(HttpToFile hf) throws Exception {
		//This is a new version
		//I pretend to try if a String object
		//can keep the entire file content.
		//This way we remove CRLF and maybe
		//it will improve the links search. 
		BufferedReader in = hf.openFile();
		String line, content = "";
		while ((line = in.readLine())!= null) {
			content += line;
		}
		hf.closeFile();
		try {
			searchLinksInLine(hf.getLink(), content);
		} catch (Exception e) {
			//out.println("Error: "+e.getMessage());
		}
	}
		
	/**
	 * This method is called everytime a page is to be processed
	 * For earch call, the method tries to locate the search terms
	 * in the page and then harvests links for later processing.
	 * 
	 * @param lnk the link to the page we're processing
	 * @param terms the search terms
	 * @throws Exception
	 */
	protected void processPage(Link lnk, String[] terms) throws Exception {
		getCurrentLinkInfo(lnk);
		if (isExcluded(lnk))
			return;
		
		HttpToFile hf = new HttpToFile(lnk);
		int amount = 0;
		
		DateFormat df = DateFormat.getInstance();
		String date = df.format(hf.getLastModified().getTime());
		//Search for terms in the current page
		int[] occurrences = SearchingTools.getOcurrences(hf,terms);
		results.openLink(lnk.getLink(), lnk.getTitle()+" "+date, lnk.getDepth());
		for (int i = 0; i < terms.length; i++) {
			if (occurrences[i] > 0) {
				amount++;
			}
			results.addTerm(i, occurrences[i]);
		}
		results.closeLink();

		//Adjust priority
		if (amount > 0) {
			//Is this document in the desired time frame?
			if (isInTimeFrame(hf.getLastModified())) {
				results2.openLink(lnk.getLink(), lnk.getTitle()+" "+date, lnk.getDepth());
				for (int i = 0; i < terms.length; i++) {
					results2.addTerm(i, occurrences[i]);
				}
				results2.closeLink();
				//Log.log("intime.log", "processPage", lnk.getLink()+" ==> "+date);
			} else {
				Log.log("outtime.log", "processPage", lnk.getLink()+" ==> "+date);
			}
			lnk.incPriority(amount);
		} else {
			lnk.decPriority(1);
		}
		
		//Search for links in the current page
		if (lnk.getDepth() == getAlterMaxDepth(lnk))
			return;
		if (isFirstTime() || amount > 0 || !isCutBranches()) {
			String ext = hf.extension();
			if (ext != null	&& 
			    (ext.endsWith("htm") ||
			     ext.endsWith("html") ||
			     ext.startsWith("php") ||
			     ext.equalsIgnoreCase("pl") ||
			     ext.equalsIgnoreCase("jsp"))) {
				
				searchForLinks(hf);
			}
		}
		hf.delete();
	}	
		
	/**
	 * Main searching method
	 * raises a search.
	 * 
	 * @param sw SwingWorker for communication with the GUI
	 * @throws Exception
	 */
	public abstract void recursiveSearch () throws Exception;

	public boolean isInitialized() {
		return initialized;
	}

	public void setInitialized(boolean initialized) {
		this.initialized = initialized;
	}

	public Output getOutput() {
		return op;
	}
	
	//New functions for w3s language
	public static void searchLinksInLineExt (Link father, String line, LinksManager lm) {
		int idx, idi, idf;
		
		String HREF = "href=";
		String link, title;
		String lowerCaseLine = line.toLowerCase();
		
		idx = lowerCaseLine.indexOf(HREF);
		while (idx != -1) {
				
			idf = line.indexOf(">",idx);
			if (idf == -1) {
				link = line.substring(idx+5);
				return;
			} else {
				link = line.substring(idx+5, idf);
			}

			if (link.startsWith("\"")) {
				link = link.substring(1,link.length()-1);
				int idef = link.indexOf("\"");
				if (idef != -1)
					link = link.substring(0,idef);
			} else if (link.startsWith("\'")) {
				link = link.substring(1,link.length()-1);
				int idef = link.indexOf("\'");
				if (idef != -1)
					link = link.substring(0,idef);
			}
			
			idi = idf;
			idf = lowerCaseLine.indexOf("</a>",idi);
			if (idf == -1) idf = lowerCaseLine.length();
			try {
				title = line.substring(idi+1, idf);
			} catch (Exception e) {
				idx = lowerCaseLine.indexOf(HREF, idx+1);
				continue;
			}
			if (title != null && title.length() == 0)
				title = "[Problem with the title]";

			lm.createLink(title, link.trim(), father);
			
			idx = lowerCaseLine.indexOf(HREF, idf);
		}
	}
	
	public static void searchForLinksExt(HttpToFile hf, LinksManager lnkM) throws Exception {
		//This is a new version
		//I pretend to try if a String object
		//can keep the entire file content.
		//This way we remove CRLF and maybe
		//it will improve the links search. 
		BufferedReader in = hf.openFile();
		String line, content = "";
		while ((line = in.readLine())!= null) {
			content += line;
		}
		hf.closeFile();
		try {
			searchLinksInLineExt(hf.getLink(), content, lnkM);
		} catch (Exception e) {
			//out.println("Error: "+e.getMessage());
		}
	}
		
	/**
	 * This method is called everytime a page is to be processed
	 * For earch call, the method tries to locate the search terms
	 * in the page and then harvests links for later processing.
	 * 
	 * @param lnk the link to the page we're processing
	 * @param terms the search terms
	 * @throws Exception
	 */
	public static LinksManager getLinksFromPage(Link lnk, boolean extOnly, boolean intOnly) throws Exception {
		/*getCurrentLinkInfo(lnk);
		if (isExcluded(lnk))
			return;*/
		
		HttpToFile hf = new HttpToFile(lnk);
		//Config cfg = setup;
		Config cfg = new Config();
		
		if (intOnly && extOnly) {
			intOnly = extOnly = false;
		}
		if (intOnly && !extOnly) {
			cfg.setProperty("ONLYINTERNAL", "TRUE");
			cfg.setProperty("ONLYEXTERNAL", "FALSE");
		} else {
			cfg.setProperty("ONLYINTERNAL", "FALSE");
			cfg.setProperty("ONLYEXTERNAL", "TRUE");			
		}
		cfg.setProperty("DEPTH", ""+(lnk.getDepth()+1));

		LinksManager lnkM = new LinksManager(cfg);
		searchForLinksExt(hf, lnkM);
		hf.delete();
		return lnkM;
	}
	
	public static LinksManager getAllLinksFromPage(Link lnk) throws Exception {
		/*getCurrentLinkInfo(lnk);
		if (isExcluded(lnk))
			return;*/
		
		HttpToFile hf = new HttpToFile(lnk);
		//Config cfg = setup;
		Config cfg = new Config();
		
		cfg.setProperty("DEPTH", ""+(lnk.getDepth()+1));

		LinksManager lnkM = new LinksManager(cfg);
		searchForLinksExt(hf, lnkM);
		hf.delete();
		return lnkM;
	}

	public static LinksManager getLinksFromPage(Link lnk) 
	throws Exception {
		return getLinksFromPage(lnk,false,false);
	}
	
	public static LinksManager getLinksFromPage(Link lnk, boolean interior) 
	throws Exception {
		if (interior) {
			return getLinksFromPage(lnk,false,true);
		} else {
			return getLinksFromPage(lnk,true,false);
		}
	}
	
	public static LinksManager getLinksFromSet(LinksManager lnkM, boolean interior) {
		Iterator<Link> i = lnkM.iterator();
		LinksManager aux, res = null;
		while (i.hasNext()) {
			Link lnk = i.next();
			try {
				aux = getLinksFromPage(lnk,interior);
				if (res == null) {
					res = aux;
				} else {
					res.union(aux);
				}
			} catch (Exception e) {
				Log.log("error.log", "getLinksFromSet", "Cant reach "+lnk.getLink());
			}
		}
		return res;
	}
	
	public static LinksManager getAllLinksFromSet(LinksManager lnkM) {
		Iterator<Link> i = lnkM.iterator();
		LinksManager aux, res = null;
		while (i.hasNext()) {
			Link lnk = i.next();
			try {
				aux = getAllLinksFromPage(lnk);
				if (res == null) {
					res = aux;
				} else {
					res.union(aux);
				}
			} catch (Exception e) {
				Log.log("error.log", "getLinksFromSet", "Cant reach "+lnk.getLink());
			}
		}
		return res;
	}

}
