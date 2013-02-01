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

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
//import java.util.Hashtable;
//import java.util.Properties;
import java.util.Vector;

/**********************************************************************************
 * CLASS: LinksManager
 *
 * This class is the links' master and because of that
 * it has to manage every harvested link.
 *  
 * Its responsabilities are:  
 *  -Add a link to the links database
 * 	-Provide the next link to be processed
 *  -Being aware of already visited links
 *  -Being aware of links already introduced in the database
 *  -Fix links to their absolute URL path
 * 
 * @author Julio Cesar Serrano Ortuno
 * 
 * created November 26, 2007
 * updated February 10, 2008
 * license General Public License
 **********************************************************************************/
public class LinksManager {

	//Links database
	//private Hashtable<String, Link> visited;
	private HashMap<String,Link> visited;
	private Vector<Link> pending;
	//private Vector<Link> visitados;
	
	//Management options
	private boolean onlyInternal = false;
	private boolean onlyExternal = false;
	private boolean priorize = false;
	private int depth;
	//private Config setup;
	private String[] allowedExtensions = {"htm", "html", "php", 
										  "pl", "jsp", "xml"};
	
	//Minimal priority to be processed for a link
	public final int MINPRIORITY = -3;
	private Config setup;
	
	/**
	 * This class has to be instantiated only once
	 * @param setup management options
	 */
	public LinksManager(Config setup) {
		this.setup = setup;
		//visited = new Hashtable<String, Link>();
		//visited = new Vector<String>();
		visited = new HashMap<String, Link>(500);
		pending = new Vector<Link>();
		//visitados = new Vector<Link>();
		//this.setup = setup;
		onlyInternal = setup.getBoolProperty("ONLYINTERNAL");
		onlyExternal = setup.getBoolProperty("ONLYEXTERNAL");
		depth = setup.getIntProperty("DEPTH");

		if (onlyInternal && onlyExternal)
			onlyInternal = onlyExternal = false;
		
		priorize = setup.getBoolProperty("PRIORIZE");
	}
	
	public Config getSetup() {
		return setup;
	}
	
	/**
	 * Creates a link and stores it in the
	 * link's database.
	 * 
	 * @param title a title for the new link
	 * @param link the URL for the new link
	 * @param father the father of the newly created link
	 */
	public synchronized void createLink(String title, String link, Link father) {
		
		//Is a external link?
		//boolean external = link.toLowerCase().startsWith("http://");
		/*if (link.equalsIgnoreCase("../../index.html")) {
			System.out.println("Mala leche tiene esto");
		}*/
		Link lnk = new Link(title,link,father);
		
		if (depth < lnk.getDepth()) {
			return;
		}
		
		boolean external = isExternal(lnk);
		
		if (!isAnAllowedExtension(lnk)) {
			return;
		}
		
		//It follow the current directives?
		if (onlyInternal && external) {
			lnk = null;
			return;
		}
		
		if (onlyExternal && !external) {
			lnk = null;
			return;
		}
		
		//If yes, then create and submit the link
		
		
		if (lnk.isValid()) {
			addLink(lnk);
		}			
	}
		
	/**
	 * Queries the main database to see if we've already
	 * seen this link before.
	 * @param lnk the link to be consulted
	 * @return true or false depending on if the link is already in the database
	 */
	private boolean alreadySeen(Link lnk) {
		return (visited.get(lnk.getLink()) != null);
		//return visited.contains(lnk.getLink());
	}
	
	/**
	 * Enters a link in the database
	 * @param lnk the link to be entered
	 * @return true if entered and false if not entered
	 */
	private synchronized boolean addLink(Link lnk) {
		
		if (!alreadySeen(lnk) && lnk.isValid()) {
			visited.put(lnk.getLink(), lnk);
			//visited.addElement(lnk.getLink());
			pending.addElement(lnk);
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * This method only provide a "next link to be
	 * processed" if the option "priorize" is activated.
	 * Search the database for the link with max 
	 * priority.
	 *
	 * @return the next link to be processed
	 */
	private Link prioritary() {
		Enumeration<Link> e = pending.elements();
		int majorPriority = MINPRIORITY;
		Link majorLink = null;
		
		while (e.hasMoreElements()) {
			Link lnk = e.nextElement();
			if (lnk.getPriority() < MINPRIORITY) {
				pending.removeElement(lnk);
				continue;
			}
			if (lnk.getPriority() > majorPriority &&
				!lnk.isAlreadyVisited()) {
				majorPriority = lnk.getPriority();
				majorLink = lnk;
			}
		}
		
		if (majorLink == null) return null;
		
		majorLink.setAlreadyVisited(true);
		//visitados.addElement(majorLink);
		pending.removeElement(majorLink);
		return majorLink;
	}
	
	/**
	 * This is the official method to obtain
	 * the next link that is going to be processed.
	 * 
	 * If the flag "priorize" is set, it will call
	 * the last method "prioritary()".
	 * Otherwise, it goes on and select the next
	 * link by harvesting order.
	 * 
	 * @return the next link to be processed.
	 */
	public synchronized Link next() {
		Enumeration<Link> e = pending.elements();
		Link lnk;
		
		if (priorize) {
			return prioritary();
		}
		if (e.hasMoreElements()) {
			//key = (String) e.nextElement();
			lnk = (Link) e.nextElement();
			while (lnk.isAlreadyVisited() && e.hasMoreElements()) {
				//key = (String) e.nextElement();
				lnk = e.nextElement();				
			}
			if (lnk.isAlreadyVisited()) {
				return null;
			} else {
				lnk.setAlreadyVisited(true);
				//visitados.addElement(lnk);
				pending.removeElement(lnk);
				return lnk;
			}
		} else {
			return null;
		}
	}
	
	private boolean isAnAllowedExtension(Link lnk) {
		for (int i = 0; i < allowedExtensions.length; i++) {
			if (extension(lnk).contains(allowedExtensions[i])) {
				return true;
			}
		}
		return false;
	}
	
	public int remainingLinks() {
		pending.trimToSize();
		return pending.size();
	}
	
	public int totalLinks() {
		return visited.size();
	}
	
	public Iterator<Link> iterator() {
		return pending.iterator();
	}
	
	///Links fixing functions
	
	/**
	 * 
	 * @return a string containing the URL of the server main page
	 * 
	 * <pre>
	 * Example:
	 * 		if provided: http://www.searchlores.org/basic.htm
	 * 		returns: http://www.searchlores.org
	 * </pre>
	 */
	private static String getRoot(Link lnk) {
		String location = lnk.getLocation();
		int id = location.indexOf("/",7);
		if (id == -1) {
			return location;
		} else {
			return location.substring(0, id);
		}
	}
	
	public static String getSite(Link lnk) {
		String link = lnk.getLink();
		if (!link.startsWith("http://")) {
			return lnk.getFather().getLink();
		}
		int id = link.indexOf("/",7);
		if (id == -1) {
			return link+"/";
		} else {
			return link.substring(0, id+1);
		}
	}

	public static boolean isExternal(Link lnk) {
		Link father = lnk.getFather();
		if (father == null || father.getLink().length() == 0)
			return false;
		 
		String linkSite = getSite(lnk);
		String fatherSite = getSite(father);
		if (linkSite.equalsIgnoreCase(fatherSite)) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * 
	 * @return the URL of the folder in which the current link is located
	 * 
	 * <pre>
	 * Example:
	 * 		provided: http://www.foo.org/folder/bar.htm
	 * 		returns: http://www.foo.org/folder
	 * </pre>
	 */
	private static String getLastFolder(Link lnk) {
		String location = lnk.getLocation();
		/*if ((location.charAt(location.length()-1) == '/') && 
			(getNumBars(location) <= 3)) {
			return location;
		}*/
		int id = location.substring(7).lastIndexOf("/");
		if (id == -1) {
			location = location + "/";
			return location;
		} else {
			return location.substring(0, location.lastIndexOf("/")+1);
		}
	}

	/**
	 * This method is slightly different from previous.
	 * besides attending to internal properties, it also
	 * takes accoubt of the given URL. 
	 * 
	 * @param URL URL to be processed
	 * @return the URL given, turned into a complete and valid URL.
	 * 
	 *  <pre>
	 *  Example:
	 *  	provided:
	 *  		site = http://www.foo.org/bar 
	 *  		URL = ../foo.htm
	 *  	returns
	 *  		http://www.foo.org/foo.htm
	 * 	<pre>
	 */
	private static String getLastFolder(Link lnk, String URL) {
		String location = lnk.getLocation();
		String finalURL = "";
		int n = 0;
		finalURL = URL;
		while (finalURL.startsWith("../")) {
			n++;
			finalURL = finalURL.substring(3);
		}
		if (n > 0) {
			String oldSite = location;
			location = getLastFolder(lnk);
			for (int i = 0; i < n; i++) {				
				location = location.substring(0,location.length()-1);
				location = getLastFolder(lnk);
			}
			String folder = location;
			location = oldSite;
			if (folder.indexOf("/",7) == -1)
				return folder+"/"+finalURL;
			else
				return folder+finalURL;
		} else {
			int id = location.lastIndexOf("/");
			if (id == -1) {
				return location+URL;
			} else {
				return location.substring(0, id+1)+URL;
			}
		}
	}

	/**
	 * Most links found in href tags are incomplete
	 * there are many web designiers that use relative
	 * links. That could be a nightmare for our
	 * harvested links, so before using a link, just in
	 * the momment of its creation he's able to fix himself
	 * with this method. For this task it uses all the 
	 * information at his scope: avobe all, parent link.
	 * 
	 * This method fix the link to turn it
	 * into a complete and valid URL. Invalid links
	 * are discarded by this method.
	 * 
	 * Notwithstanding, much work has still to be made
	 * to make this method perfect.
	 */
	public static void fixLink(Link lnk) {
		String link = lnk.getLink();
		int idx;
		
		//Desechar direcciones de correo electrónico
		if (link.startsWith("mailto:")) {
			link = null;
			lnk.setLink(link);
			return;
		}
				
		//Desechar referencias relativas #ref
		idx = link.indexOf("#");
		if (idx != -1) {
			link = link.substring(0,idx);
		}

		//Arreglar enlaces incompletos
		if (!link.toLowerCase().startsWith("http://")) {
			if (link.startsWith("/")) {
				link =  getRoot(lnk)+link;
			} else {
				link =  getLastFolder(lnk,link);
			}			
		} else {
			idx = link.substring(7).lastIndexOf("/"); 
			if (idx == -1) {
				link = link+"/";
			}
		}
		lnk.setLink(link);
	}	
	
	/**
	 * Gets the extension of the file
	 * @return the extension as a string
	 */
	public static String extension(Link lnk) {
		String link = lnk.getLink();
		String ln = link.toLowerCase();
		if (ln.startsWith("http://")) {
			ln = link.substring(7);
		}
		int idi = ln.lastIndexOf("/");
		if (idi == -1) {
			return "html";
		}
		String fn = link.substring(idi+6);
		int id = fn.lastIndexOf(".");
		if (id != -1) {
			return fn.substring(id+1);
		} else {
			return "html";
		}
	}
	
	public synchronized boolean contains(Link lnk) {
		return pending.contains(lnk);
	}

	public synchronized boolean intersects(LinksManager lm) {
		Iterator<Link> i= lm.pending.iterator();
		Link lnk;
		while (i.hasNext()) {
			lnk = i.next();
			if (pending.contains(lnk))
				return true;
		}
		return false;
	}
	
	public synchronized void union(LinksManager lm) {
		pending.addAll(lm.pending);
	}
	
	public synchronized boolean isEmpty() {
		return pending.isEmpty();
	}
}
