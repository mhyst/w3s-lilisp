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
 * CLASS: LinksExplorer
 *
 * Default searching algorithm.
 * Originally, this class contained all AbstractAlgorithms
 * attributes, since w3s only had one search algorithm.
 * It was changed later to its actual condition, due
 * to the need to manage several searching algorithms.
 * Since then this class turned into just one more 
 * algorithm, just like TestAlgorithm.
 *
 * @author Julio Cesar Serrano Ortuno
 * created January 26, 2008
 * updated February 15, 2008
 * license General Public License
 **********************************************************************************/
public class LinksExplorer extends AbstractAlgorithm {
	/*public LinksExplorer(String link, String[] terms, Config setup) throws Exception {
		this(link, terms, null, setup);
	}

	public LinksExplorer(String link, String[] terms, String[] excluded, Config setup) throws Exception {
		super(link,terms,excluded,setup);
	}*/
	/**
	 * Main searching method
	 * raises a search.
	 * 
	 * @param sw SwingWorker for communication with the GUI
	 * @throws Exception
	 */
	public void recursiveSearch () throws Exception {

		//Preparing to search the initial page
		//As it's the inicial page, doesn't have a father
		Link aux = new Link("Start page",seed,null);
		if (aux.getLink() == null) {
			getOutput().addToOutput("ERR:\nError processing start page.\n");
			return;
		}
		
		//Inform to the GUI of changes
		//So it can show the current page we're processing
		getOutput().setWorkingLink(aux.getDepth(),aux.getLink());
		
		//We process the initial page
		//as it's first time, we shouldn't discard its links
		//even if "cutBranches" is activated
		processPage(aux,searchTerms);
		setFirstTime(false);
		
		//We retrieve some setup data
		//generally it will come from the GUI
		int max = setup.getIntProperty("MAXENTRIES");
		
		int id = 0;	//Result id (counter)
		
		//Get the follow link to process
		Link lnk = getLinkManager().next();
		
		//If it's not null, the work starts
		while (lnk != null) {
			//Test stopping
			if (getOutput().isStopping()) {
				getOutput().addToOutput("\nStopped by the user!");
				break;
			}
			
			//Progress bar
			int total = lm.totalLinks();
			int remaining = lm.remainingLinks();
			getOutput().setProgressBar(remaining,total);
			
			//if (!isExcluded(lnk)) {
			//Print the dot in the textfield
			getOutput().addToOutput(".");
			
			//Helps dots to not go out from visual scope
			if (paginate()) {
				//getOutput().addToOutput((Runtime.getRuntime().freeMemory()/1024)+"K/"+(Runtime.getRuntime().totalMemory()/1024)+"K");
				getOutput().addToOutput("\n");
			}
			
			//Here goes the most impostant part
			//the processing
			try {
				getOutput().setWorkingLink(lnk.getDepth(),lnk.getLink());
				processPage(lnk,searchTerms);
				id++;
			} catch (Exception e) {
			}
			
			//Max entries limit control
			if (max > 0) {
				if (id >= max) {
					results.add("Entries limit exceeded: "+max);
					results.finishResults();
					results2.add("Entries limit exceeded: "+max);
					results2.finishResults();

					getOutput().addToOutput("\nEntries limit exceeded: "+max);
					break;
				}
			}
			//}
			//Again we get the next link in out database
			lnk = getLinkManager().next();
		}
		
		//End of results
		results.finishResults();
		results2.finishResults();
	}
}
