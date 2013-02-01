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

/*
*
* @(#) TextAreaOutputStream.java
*
*/

import java.io.IOException;
import java.io.OutputStream;
import javax.swing.JTextArea;


/**
 * This class is not used by now
 * An output stream that writes its output to a javax.swing.JTextArea
 * control.
 *
 * @author  Ranganath Kini
 * @see      javax.swing.JTextArea
 */
public class TextAreaOutputStream extends OutputStream {
    private JTextArea textControl;
    
    /**
     * Creates a new instance of TextAreaOutputStream which writes
     * to the specified instance of javax.swing.JTextArea control.
     *
     * @param control   A reference to the javax.swing.JTextArea
     *                  control to which the output must be redirected
     *                  to.
     */
    public TextAreaOutputStream( JTextArea control ) {
        textControl = control;
    }
    
    /**
     * Writes the specified byte as a character to the 
     * javax.swing.JTextArea.
     *
     * @param   b   The byte to be written as character to the 
     *              JTextArea.
     */
    public void write( int b ) throws IOException {
        // append the data as characters to the JTextArea control
        textControl.append( String.valueOf( ( char )b ) );
    }   
}