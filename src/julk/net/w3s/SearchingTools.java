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

/**********************************************************************************
 * CLASS: SearchingTools
 *
 * Some static helping methods wich main purpose is
 * to take account of ocurrences of the search terms
 * on the target file.
 * 
 * Originally these functions were either in
 * LinksExplorer or HttpToFile. These methods
 * were moved in order to ensure understandability
 * and uniformity conceptually speacking.
 *
 * @author Julio Cesar Serrano Ortuno
 * created January 26, 2008
 * updated February 10, 2008
 * license General Public License
 **********************************************************************************/
public class SearchingTools {
	/**
	 * Search a term in one line of text
	 * returns the number of times the term appears in the
	 * line of text.
	 * 
	 * @param line the line of text
	 * @param term the the search term
	 * @return the number of times the term appears in the line
	 */
	public static int getLineOcurrences(String line, String term) {
		int n = 0;
		int idx;
		
		//line and term must be already in lowercase
		idx = line.indexOf(term);
		while (idx != -1) {
			n++;
			idx = line.indexOf(term, idx+1);
		}
		return n;
	}
	
	/**
	 * Search several terms in the same line of text.
	 * Uses the avobe function for every term.
	 * 
	 * @param line the line of text
	 * @param terms the search terms array
	 * @return an array of ints containing the number of ocurrences of each term in the line
	 */
	public static int[] getLineOcurrences(String line, String[] terms) {
		int[] ocurrences = new int[terms.length];
		
		for (int i = 0; i < terms.length; i++) {
			ocurrences[i] = getLineOcurrences(line, terms[i]);
		}
		return ocurrences;
	}

	/**
	 * Search the entire file for the ocurrences
	 * of several search terms. 
	 * Uses the quicker method of searching several
	 * terms for one line at a time.
	 * 
	 * @param terms the search terms
	 * @return and array of int containing the number times each term appeared in the file
	 * @throws Exception
	 */
	public static int[] getOcurrences(HttpToFile hf, String[] terms) 
	throws Exception {
		
		int[] results = new int[terms.length];
		int[] ocurrences;
		BufferedReader in = hf.openFile();
		String line;
		//int count = 0;
		for (int i = 0; i < terms.length; i++) {
			terms[i]= terms[i].toLowerCase();
			results[i] = 0;
		}
		while ((line = in.readLine())!= null) {
			ocurrences = getLineOcurrences(line.toLowerCase(),
							 	 		   terms);
			for (int i = 0; i < results.length; i++) {
				results[i] += ocurrences[i];
			}
		}
		hf.closeFile();
		
		return results;
	}
}
