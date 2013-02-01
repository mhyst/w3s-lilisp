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
 * CLASS: TestAlgorithm 
 *
 * Extends AbstractAlgorithm, allowing w3s to consider
 * it as a searching algorithm. It only serves to 
 * testing purposes.
 *
 * @author Julio Cesar Serrano Ortuno
 * created January 26, 2008
 * updated February 10, 2008
 * license General Public License
 **********************************************************************************/
public class TestAlgorithm extends AbstractAlgorithm {

	@Override
	public void recursiveSearch() throws Exception {
		getOutput().setWorkingLink(0,"No working link whatsoever!");
		getOutput().setProgressBar(100,100);
		getOutput().addToOutput("\nThis is only a test search algorithm and does nothing. Thanks!\n");
		getOutput().addToOutput("Seed: "+seed+"\n");
		getOutput().addToOutput("Search terms:\n");
		for (int i = 0; i < searchTerms.length; i++)
			getOutput().addToOutput(searchTerms[i]+"\n");
		getOutput().addToOutput("Excluded URL fragments:\n");
		for (int i = 0; i < excluded.length; i++)
			getOutput().addToOutput(excluded[i]+"\n");
	}
}
