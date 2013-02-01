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

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**********************************************************************************
 * CLASS: Results
 *
 * This class manages the creation of the HTML results file.
 *
 * @author Julio Cesar Serrano Ortuno
 * created January 26, 2008
 * updated February 10, 2008
 * license General Public License
 **********************************************************************************/
public class Results {
	
	private String filename;
	private PrintWriter out;
	private String startingPage;
	private String[] searchTerms;
	
	public Results(String filename, String startingPage, String[] searchTerms) {
		this.filename = filename;
		this.startingPage = startingPage;
		this.searchTerms = searchTerms;
		//System.arraycopy(searchTerms,0,this.searchTerms,0,searchTerms.length);
		
		firstOpen();
		out.println("<html><head>");
		out.println("<title>Results of searching throught "+startingPage+"</title></head><body>");
		//out.println("<meta http-equiv=\"refresh\" content=\"15\"></head><body>");
		out.println("<h2>W3S</h2><h3>Search start: "+startingPage+"</h3>");
		out.println("<h3>Search terms: </h3><ul>");		
		for (int i = 0; i < searchTerms.length; i++) {
			out.println("<li>"+searchTerms[i]+"</li>");
		}
		out.println("</ul><hr><table><tr><th>Links<th>Depth<th>Ocurrences</tr>");
		close();
	}
	
	private void firstOpen() {
		try {
			out = new PrintWriter(new FileWriter(filename,false));
		} catch (IOException e) {
			//e.printStackTrace();
		}
	}

	private void open() {
		try {
			out = new PrintWriter(new FileWriter(filename,true));
		} catch (IOException e) {
			//e.printStackTrace();
		}
	}
	
	private void close() {
		if (out != null) {
			out.close();
		}
	}
	
	public void openLink(String link, String title, int depth) {
		open();
		out.println("<tr><td><a target=_blank href=\""+link+"\">"+title+"</a></td><td>"+depth+"</td><td>");
		close();
	}
	
	public void addTerm(int term, int occurrences) {
		open();
		out.println(searchTerms[term]+": "+occurrences+"</br>");
		close();
	}
	
	public void closeLink() {
		open();
		out.println("</td></tr>");
		close();
	}
	
	public void add(String msg) {
		open();
		out.print(msg);
		close();
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String[] getSearchTerms() {
		return searchTerms;
	}

	public void setSearchTerms(String[] searchTerms) {
		this.searchTerms = searchTerms;
	}

	public String getStartingPage() {
		return startingPage;
	}

	public void setStartingPage(String startingPage) {
		this.startingPage = startingPage;
	}
	
	public void finishResults() {
		add("</table>");
		add("</br><hr><i>Powered by W3S</i></body></html>");
	}

}
