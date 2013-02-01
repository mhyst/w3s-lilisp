/*  This file is part of Lilisp.
 *  Copyright (C) 2008  Julio Cesar Serrano Ortuno
 * 
 *  Lilisp is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Lilisp is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with Lilisp.  If not, see <http://www.gnu.org/licenses/>.
 */
package julk.net.w3s.language;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class RequestInput {
	JFrame frm;
	JLabel lbl;
	JTextField txtInput;
	JButton send;
	boolean finished = false;
	
	public RequestInput() {
		frm = new JFrame();
		frm.setLayout(new FlowLayout());
		lbl = new JLabel();
		txtInput = new JTextField();
		send = new JButton();
		send.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				finished = true;
			}
		});
		frm.getContentPane().add(lbl);
		frm.getContentPane().add(txtInput);
		frm.getContentPane().add(send);
		frm.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
	}
	
	public Reference getInput(String title, String label, int width, String button, Type type) {
		//frm.getContentPane().removeAll();
		frm.setTitle(title);
		lbl.setText(label);
		txtInput.setPreferredSize(new Dimension(width*10, 20));
		txtInput.setText("");
		send.setText(button);
		
		Reference ref = new Reference();
		ref.type = type;
		do {
			frm.pack();
			frm.getRootPane().setDefaultButton(send);
			frm.setVisible(true);
			while (!finished) {
				try {
					Thread.sleep(500);
				} catch (Exception e) {}
				if (!frm.isShowing()) {
					frm.dispose();
					return null;
				}
			}
			finished = false;
			frm.setVisible(false);
			ref.value = txtInput.getText();
			lbl.setText("You must especify a "+type.name());
		} while (!type.isValidValue(ref.value));
		frm.dispose();
		return ref;
	}
	
	public void finalize() {
		frm.dispose();
	}
	
	public static void main(String[] args) {
		RequestInput ri = new RequestInput();
		Reference ref = ri.getInput("Testing", "Intruduzca un numero:", 10, "Aceptar", Type.numeric);
		if (ref == null) {
			System.out.println("Ref es null");
		} else {
			System.out.println("Tipo: "+ref.type.name());
			System.out.println("Valor: "+ref.value.toString());
		}
	}
}
