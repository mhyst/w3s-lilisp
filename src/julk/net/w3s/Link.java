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

/**********************************************************************************
 * CLASS: Link
 *
 * This class represents a link with all its relative
 * information. Once created every Link object gets fixed
 * via calling the method fixMe. That method turns the link in a
 * valid URL.
 * 
 * Originally this class was responsible for fixing the link
 * to its absolute URL path. But this was considered a waste
 * of memory and all that code was moved into LinksManager.
 *
 * @author Julio Cesar Serrano Ortuno
 * created November 26, 2007
 * updated January 15, 2008
 * license General Public License
 **********************************************************************************/
public class Link {
	private String location;	//site to wich belongs the link
							//This is not exact
	private String title;	//link title
	private String link;	//link itself
	private Link father;	//page from which this link
							//was extracted.
	private boolean alreadyVisited; //was this link already visited? -> LinksManager
	private int priority;	//link priority
							//Only used when the option priorize is checked
	

	/**
	 * CONSTRUCTOR
	 * 
	 * In order to create an object of the Link class
	 * you have to give the arguments: title, link and father.
	 * 
	 * This constructor generally is invoqued from
	 * LinksManager.createLink method.
	 * 
	 * @param title		title extracted from the tags <a href> 
	 * @param link	 	the URL corresponding to the link you want to create
	 * @param father	a Link object that represents the page from which this link was taken.
	 * 					Usefull to calculate the link depth.
	 * @return a new object of the Link class
	 * @see LinksManager#createLink(String, String, Link)
	 * 
	 * <pre>
	 * 
	 * Example:
	 * 		Reading some far page...
	 * 		...<a href="http://www.searchlores.org">Search Lores</a>...
	 * 
	 * 		Link lnk = new Link("Search Lores", "http://www.searchlores.org",null);
	 * 
	 * </pre>
	 */
	public Link(String title, String link, Link father) {
		if (father == null) {
			location = "";
		} else {
			location = father.getLink();
		}
		this.title = title;
		this.link = link;
		this.father = father;
		
		if (father != null)
			priority = father.getPriority();
		else
			priority = 0;
		
		if (isValid()) LinksManager.fixLink(this);
	}
	
	//All this getters and setters are of help
	//when the "priorize" option is checked
	//In such case, the property "priority"
	//is used to determine the importance
	//of a page in the current search.
	
	//Getter for the property "priority"
	public int getPriority() {
		return priority;
	}

	//Setter for the property "priority"
	public void setPriority(int priority) {
		this.priority = priority;
	}

	//Setter for the property "priority"
	//Incremets "priority" by 1
	public void incPriority() {
		this.priority++;
	}

	//Setter for the property "priority"
	//Increments "priority" by amount
	public void incPriority(int amount) {
		this.priority+=amount;
	}
	
	//Setter for the property "priority"
	//Decrements "priority" by 1
	public void decPriority() {
		this.priority--;
	}

	//Setter for the property "priority"
	//Decrements "priority" by amount
	public void decPriority(int amount) {
		this.priority-=amount;
	}
		
	/**
	 * 
	 * @param url URL to be processed
	 * @return the number of bars(/) in the given URL
	 */
	public static int getNumBars(String url) {
		byte[] c = url.getBytes();
		int cont = 0;
		for (int i = 0; i < c.length; i++) {
			if (c[i] == '/') {
				cont++;
			}
		}
		return cont;
	}
	
	/**
	 * Uses the parents informations (working as a real list)
	 * to calculate the depth to be considered for this link.
	 * 
	 * @return the depth of this link
	 */
	public int getDepth() {
		Link lnk = getFather();
		int level = 0;
		while (lnk != null) {
			level++;
			lnk = lnk.getFather();
		}
		return level;
	}
	
	/**
	 * @return property link
	 */
	public String getLink() {
		return link;
	}
		
	/**
	 * @return property location
	 */
	public String getLocation() {
		return location;
	}
		
	/**
	 * @return property title
	 */
	public String getTitle() {
		return title;
	}
	
	/**
	 * @return property father
	 */
	public Link getFather() {
		return father;
	}

	/**
	 * @return property alreadyVisited
	 */
	public boolean isAlreadyVisited() {
		return alreadyVisited;
	}

	/**
	 * Setter for already visited
	 * @param alreadyVisited
	 */
	public void setAlreadyVisited(boolean alreadyVisited) {
		this.alreadyVisited = alreadyVisited;
	}
	
	/**
	 * @return true if the link is valid
	 */
	public boolean isValid() {
		return link == null ? false : true;
	}

	public void setFather(Link father) {
		this.father = father;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public void setTitle(String title) {
		this.title = title;
	}
}
