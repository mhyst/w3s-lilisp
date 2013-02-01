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
 * CLASS: CrawlingAlgorithm
 *
 * A searching algorithms manager.
 * 
 * @author Julio Cesar Serrano Ortuno
 * created January 20, 2008
 * updated February 10, 2008
 * license General Public License
 **********************************************************************************/
public class CrawlingAlgorithm {
	private String sAlgorithm;
	private AbstractAlgorithm alg = null;
	private static final String defaultAlgorithm = "julk.net.w3s.LinksExplorer";
	
	public void setAlgorithm(String salg) {
		sAlgorithm = salg;
	}
	
	public static String getDefaultAlgorithm() {
		return defaultAlgorithm;
	}
	
	public String getCurrentAlgorithm() {
		if (sAlgorithm == null || sAlgorithm.length() == 0) {
			return defaultAlgorithm;
		} else {
			return sAlgorithm;
		}
	}
	
	public static String[] getAvailableAlgorithms() {
		String[] algos = {"julk.net.w3s.LinksExplorer",
				          "julk.net.w3s.TestAlgorithm"}; 
		//TODO: Cargar algoritmos de un archivo
		return  algos;
	}
	
	public void Init(String seed, String[] terms, ExcludedElement[] excluded, Config setup, Output op)
	throws Exception {
		
		if (sAlgorithm == null || sAlgorithm.trim().length() == 0) {
			sAlgorithm = defaultAlgorithm;
		}
		try {
			alg = (AbstractAlgorithm) Class.forName(sAlgorithm).newInstance();
			alg.Init(seed, terms, excluded, setup, op);
		} catch (Exception e) {
			throw new Exception("Could not instantiate the searching algorithm.");
		}
	}
		
	public void Call() throws Exception	{
		if (alg == null) {
			throw new Exception("Searching algorithm not defined.");
		}
		alg.recursiveSearch();
	}
}
