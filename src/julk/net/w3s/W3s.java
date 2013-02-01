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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
//import java.io.PrintWriter;
import java.util.List;
//import java.util.Properties;
import java.util.StringTokenizer;
import java.util.Enumeration;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**********************************************************************************
 * CLASS: W3s 
 *
 * This class is only a graphic user interface that
 * allows a user to make searches.
 *
 * It can be enterely rewriten and discarded without
 * danger.
 *
 * @author Julio Cesar Serrano Ortuno
 * created November 26, 2007
 * updated February 10, 2008
 * license General Public License
 **********************************************************************************/
public class W3s {
	//Search tab
	private JTextField txtURL;
	private JTextField txtTerms;
	private JLabel lDepth;
	private JSlider sldDepth;
	private JLabel lMax;
	private JSlider sldMax;	
	private JCheckBox chkExtOnly;
	private JCheckBox chkIntOnly;
	private JCheckBox chkCutBranches;
	private JCheckBox chkPriority;
	private JTextArea txtOut;
	private JButton btn;
	private JTextField txtWorking;
	private JProgressBar progress;
	private JTextField prgDetails;
	private JTextField txtStartDate;
	private JTextField txtEndDate;

	//Exclude tab
	private JList<ExcludedElement> lstExcluded;
	private DefaultListModel<ExcludedElement> mdlExcluded;
	private JTextField txtExcluding;
	private JTextField txtDepth;
	private JButton btnExcluding;
	private JButton btnExClear;
	private JTextField txtFile;
	private JButton btnSave;
	private JButton btnLoad;
	private JFileChooser fc;
	
	//Algorithms tab
	private JTextField txtCurrentAlgorithm;
	private DefaultListModel<String> mdlAlgorithms;
	private JList<String> lstAlgorithms;
	
	//General
	public JFrame frm;
	private W3sWorker task;
	//public final String version = "1.03";

	public W3s() {
		/// *** Tab: Search
		JPanel searchPanel, jp1, jp2, jp3;
		searchPanel = new JPanel(new BorderLayout());
		jp1 = new JPanel(new BorderLayout());
		jp2 = new JPanel(new GridBagLayout());
		jp3 = new JPanel();
		jp1.add(jp2,BorderLayout.NORTH);
		jp1.add(jp3,BorderLayout.CENTER);
		searchPanel.add(jp1,BorderLayout.CENTER);
		JPanel pn, pe, pw, ps;
		pn = new JPanel();
		pn.setSize(400,10);
		pe = new JPanel();
		pe.setSize(10,400);
		pw = new JPanel();
		pw.setSize(10,140);
		ps = new JPanel();
		ps.setSize(400,10);
		searchPanel.add(pn, BorderLayout.NORTH);
		searchPanel.add(pe, BorderLayout.EAST);
		searchPanel.add(pw, BorderLayout.WEST);
		searchPanel.add(ps, BorderLayout.SOUTH);
		
		GridBagConstraints c = new GridBagConstraints();
		c.gridx=0; c.gridy=0;
		c.weightx=1; c.weighty=1;
		c.fill=GridBagConstraints.HORIZONTAL;

		jp2.add(new JLabel("URL:"),c);
		txtURL = new JTextField(40);
		txtURL.setToolTipText("Enter a correct URL. Example: http://www.searchlores.org");
		
		c.gridx=1; c.gridy=0;
		c.weightx=4; c.weighty=1;
		c.fill=GridBagConstraints.HORIZONTAL;
		jp2.add(txtURL,c);
		
		c.gridx=0; c.gridy=1;
		c.weightx=1; c.weighty=1;
		c.fill=GridBagConstraints.HORIZONTAL;
		jp2.add(new JLabel("Search terms:"),c);
		
		txtTerms = new JTextField(40);
		txtTerms.setToolTipText("Enter one or more words. (Comma separated)");
		c.gridx=1; c.gridy=1;
		c.weightx=4; c.weighty=1;
		c.fill=GridBagConstraints.HORIZONTAL;
		jp2.add(txtTerms,c);

		c.gridx=0; c.gridy=2;
		c.weightx=1; c.weighty=1;
		c.fill=GridBagConstraints.HORIZONTAL;
		lDepth = new JLabel("Max depth: 2");
		jp2.add(lDepth,c);
		
		sldDepth = new JSlider(1,19,2);
		sldDepth.setToolTipText("Control search depth. This may help to shorten the search time.");
		sldDepth.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				lDepth.setText("Max depth: "+sldDepth.getValue());
			}
		});
		c.gridx=1; c.gridy=2;
		c.weightx=4; c.weighty=1;
		c.fill=GridBagConstraints.HORIZONTAL;
		jp2.add(sldDepth,c);
		
		c.gridx=0; c.gridy=3;
		c.weightx=1; c.weighty=1;
		c.fill=GridBagConstraints.HORIZONTAL;
		lMax = new JLabel("Max entries limit: 0");
		jp2.add(lMax,c);
		
		sldMax = new JSlider(0,1000,0);
		sldMax.setToolTipText("Highest numer of results. Use it to avoid very long searches. Select '0' for no limit.");
		sldMax.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				lMax.setText("Max entries limit: "+sldMax.getValue());
			}
		});
		c.gridx=1; c.gridy=3;
		c.weightx=4; c.weighty=1;
		c.fill=GridBagConstraints.HORIZONTAL;
		jp2.add(sldMax,c);

		//Here start and end dates
		JPanel dates = new JPanel();
		
		dates.add(new JLabel("Start date:"));
		txtStartDate = new JTextField(15);
		txtStartDate.setToolTipText("Earliest last modification date allowed.");
		dates.add(txtStartDate);
		dates.add(new JLabel("End Date:"));
		txtEndDate = new JTextField(15);
		txtEndDate.setToolTipText("Lastest modification date allowed.");
		dates.add(txtEndDate);
		c.gridx=0; c.gridy=4;
		c.weightx=4; c.weighty=1;
		c.gridwidth=5;
		c.fill=GridBagConstraints.HORIZONTAL;
		jp2.add(dates,c);
		c.gridwidth=1;
		//dates finished
		
		
		c.gridx=0; c.gridy=5;
		c.weightx=1; c.weighty=1;
		c.fill=GridBagConstraints.HORIZONTAL;
		jp2.add(new JLabel("Working on:"),c);
		
		txtWorking = new JTextField();
		txtWorking.setEditable(false);
		
		c.gridx=1; c.gridy=5;
		c.weightx=4; c.weighty=1;
		c.fill=GridBagConstraints.HORIZONTAL;
		jp2.add(txtWorking,c);
		
		//Progress Bar
		prgDetails = new JTextField();
		prgDetails.setMaximumSize(new Dimension(90,21));
		prgDetails.setSize(90,21);
		prgDetails.setPreferredSize(new Dimension(90,21));		
		prgDetails.setEditable(false);
		c.gridx=0; c.gridy=6;
		c.weightx=1; c.weighty=1;
		c.fill=GridBagConstraints.HORIZONTAL;
		jp2.add(prgDetails,c);
		
		progress = new JProgressBar();
		progress.setMaximum(100);
		progress.setValue(0);
		progress.setStringPainted(true);
		c.gridx=1; c.gridy=6;
		c.weightx=4; c.weighty=1;
		c.fill=GridBagConstraints.HORIZONTAL;
		jp2.add(progress,c);

		chkExtOnly = new JCheckBox("Follow external links only",false);
		chkExtOnly.setToolTipText("Only process links starting with \"http://\".");
		chkIntOnly = new JCheckBox("Follow internal links only",false);
		chkIntOnly.setToolTipText("Only process links not starting with \"http://\".");
		chkCutBranches = new JCheckBox("Cut low branches",false);
		chkCutBranches.setToolTipText("Doesn't take account of links from pages not matching search terms.");
		chkPriority = new JCheckBox("Priorize",false);
		chkPriority.setToolTipText("The more ocurrences in the father, the more priority for its links");
		jp3.add(chkIntOnly);
		jp3.add(chkExtOnly);
		jp3.add(chkCutBranches);
		jp3.add(chkPriority);
		
		txtOut = new JTextArea(12,55);
		txtOut.setToolTipText("Here you will see what's doing the program. Each dot represents a page being processed.");
		txtOut.setEditable(false);
		txtOut.setAutoscrolls(false);
		/*txtOut.setMaximumSize(new Dimension(650,900));
		txtOut.setSize(650,900);
		txtOut.setPreferredSize(new Dimension(650,900));*/
		JScrollPane sp = new JScrollPane(txtOut);
		
		
		txtOut.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		
		
		jp1.add(sp,BorderLayout.SOUTH);

		btn = new JButton("Search");
		btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				if (e.getActionCommand().equals("Search")) {
					frm.setCursor(Cursor
							.getPredefinedCursor(Cursor.WAIT_CURSOR));
					//btn.setEnabled(false);
					btn.setText("-Stop-");
					txtOut.setText("");
					try {
						txtOut.append(Config.getFullName()+"\n");
						txtOut.append("----------------------------------------\n");
						txtOut.append("Searching: " + txtURL.getText() + "\n");
						txtOut.append("Search terms: " + txtTerms.getText()
								+ "\n");
						txtOut.append("This operation might take some minutes. Please wait.\n");
						frm.update(frm.getGraphics());
						task = new W3sWorker();
						
						task.execute();
						/*LinkExplorer.recursiveSearch(txtURL.getText(),getCadenas(),frm,txtOut,chkIntOnly.isSelected(),chkExtOnly.isSelected(),sldDepth.getValue(),sldMax.getValue());
						 txtOut.append("\nSearching finished. Now you can browse the salida.htm file.\n");
						 //txtOut.append("Le recomiendo abrirlo en su navegador habitual.\n");
						 txtOut.append("Thank you.\n");
						 //String ejemplo = "<a target=_top href=\"/url?sa=p&pref=ig&pval=3&q=http://www.google.es/ig%3Fhl%3Des&usg=AFQjCNH25tZa9pK_qlkku2QH55RuJCamdw\">iGoogle</a> | <a href=\"https://www.google.com/accounts/ManageAccount\">Mi cuenta</a> | <a target=_top href=\"/accounts/ClearSID?continue=http://www.google.com/accounts/Logout%3Fcontinue%3Dhttp://www.google.es/\">Salir</a>";
						 //LinkSearch.procesarLinea(ejemplo);
						 String curDir = System.getProperty("user.dir");
						 //Runtime.getRuntime().exec("explorer "+curDir+"\\salida.htm");
						 Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler "+curDir+"\\salida.htm");
						 */
					} catch (Exception e1) {
						txtOut.append(e1.getMessage());
					}
					/*btn.setEnabled(true);
					 frm.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));*/
				} else {
					//canceled = true;
					//task.cancel(true);
					btn.setEnabled(false);
					task.setStopping(true);
					//btn.setText("Search");
					//frm.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
				}
			}
		});
		jp3.add(btn);
		
		/// *** Tab: excluded
		fc = new JFileChooser();
		fc.setCurrentDirectory(new File(System.getProperty("user.dir")));
		mdlExcluded = new DefaultListModel<ExcludedElement>();
		lstExcluded = new JList<ExcludedElement>(mdlExcluded);
		lstExcluded.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent arg0) {
				int sel = lstExcluded.getSelectedIndex(); 
				if (sel != -1) {
					ExcludedElement val = (ExcludedElement) lstExcluded.getSelectedValue();
					txtExcluding.setText(val.getURLfragment());
					txtDepth.setText(""+val.getDepth());
					mdlExcluded.removeElementAt(sel);
				}				
			}
		});

		lstExcluded.setToolTipText("These URL fragments are going to be excluded from the spider web frontier. That's cool to crawl on SERP's.");
		JPanel excludedPanel = new JPanel(new BorderLayout()); 
		JScrollPane sExcludedPanel = new JScrollPane(lstExcluded);
		excludedPanel.setMaximumSize(new Dimension(300,300));
		excludedPanel.setSize(300,300);
		excludedPanel.setPreferredSize(new Dimension(300,300));
		excludedPanel.add(sExcludedPanel,BorderLayout.CENTER);
		
		//Exluded links
		JPanel pexclude = new JPanel();
		pexclude.add(new JLabel("Excluding:"));
		txtExcluding = new JTextField(30);
		txtExcluding.setToolTipText("Enter a URL fragment to be excluded from the spider frontier. Then press Exclude button.");
		pexclude.add(txtExcluding);
		pexclude.add(new JLabel("Depth:"));
		txtDepth = new JTextField(5);
		txtDepth.setToolTipText("Enter a different depth for URL matching this fragment.");
		pexclude.add(txtDepth);
		btnExcluding = new JButton("Exclude");
		btnExcluding.setToolTipText("Press here to add a URL fragment to the excluded list.");
		btnExcluding.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String excluding = txtExcluding.getText();
				String depth = txtDepth.getText();
				if (depth == null || depth.length() == 0) {
					depth = "-1";
				}
				if (excluding == null || excluding.length() == 0) {
					/*int sel = lstExcluded.getSelectedIndex(); 
					if (sel != -1) {
						String val = (String) lstExcluded.getSelectedValue();
						txtExcluding.setText(val);
						mdlExcluded.removeElementAt(sel);
					}*/
				} else {
					mdlExcluded.addElement(new ExcludedElement(excluding,depth));
					txtExcluding.setText("");
					txtDepth.setText("");
				}
			}
		});
		pexclude.add(btnExcluding);
		btnExClear = new JButton("Clear");
		btnExClear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mdlExcluded.clear();
			}
		});
		pexclude.add(btnExClear);
		excludedPanel.add(pexclude,BorderLayout.NORTH);
		
		JPanel pload = new JPanel();
		txtFile = new JTextField(40);
		txtFile.setToolTipText("The file where/from wich you want to store/load the current excluded list");
		btnSave = new JButton("Save");
		btnSave.setToolTipText("Press here to save the excluded list. But first especify a filename.");
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String filename = txtFile.getText();
				if (filename == null || filename.length() == 0)
					return;
				try {
					PrintWriter pwr = new PrintWriter(new BufferedWriter(new FileWriter(filename)));
					Enumeration<ExcludedElement> ex = mdlExcluded.elements();
					while (ex.hasMoreElements()) {
						pwr.println(((ExcludedElement) ex.nextElement()).toString());
					}
					pwr.close();
				} catch(Exception ioe) {
				}
			}
		});
		btnLoad = new JButton("Load");
		btnLoad.setToolTipText("This button will load a excluded list from the file named in File.");
		btnLoad.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String filename = txtFile.getText();
				if (filename == null || filename.length() == 0) {
					int returnVal = fc.showOpenDialog(frm);
		            if (returnVal == JFileChooser.APPROVE_OPTION) {
		                txtFile.setText(fc.getSelectedFile().getAbsolutePath());
		            }
				}
				filename = txtFile.getText();
				if (filename == null || filename.length() == 0) {
					return;
				}
				try {
					BufferedReader br = new BufferedReader(new FileReader(filename));
					mdlExcluded.clear();
					String line;
					while ((line = br.readLine()) != null) {
						ExcludedElement ee = new ExcludedElement(line);
						mdlExcluded.addElement(ee);
					}
					br.close();
				} catch(Exception ioe) {
				}
			}
		});
		pload.add(new JLabel("File:"));
		pload.add(txtFile);
		pload.add(btnLoad);
		pload.add(btnSave);
		excludedPanel.add(pload,BorderLayout.SOUTH);
		//excluded links finished
		
		/// *** Tab: Search Algorithms
		JPanel searchAlgos = new JPanel(new BorderLayout());
		JPanel saNorth = new JPanel();
		saNorth.add(new JLabel("Current Algorithm:"));
		txtCurrentAlgorithm = new JTextField(50);
		txtCurrentAlgorithm.setText(CrawlingAlgorithm.getDefaultAlgorithm());
		txtCurrentAlgorithm.setEditable(false);
		saNorth.add(txtCurrentAlgorithm);
		mdlAlgorithms = new DefaultListModel<String>();
		loadAlgorithms(mdlAlgorithms);
		lstAlgorithms = new JList<String>(mdlAlgorithms);
		lstAlgorithms.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent arg0) {
				int sel = lstAlgorithms.getSelectedIndex(); 
				if (sel != -1) {
					String val = (String) lstAlgorithms.getSelectedValue();
					txtCurrentAlgorithm.setText(val);
				}				
			}
		});
		searchAlgos.add(saNorth,BorderLayout.NORTH);
		searchAlgos.add(new JScrollPane(lstAlgorithms),BorderLayout.CENTER);

		//Main panel
		JTabbedPane tabpane = new JTabbedPane();
		tabpane.addTab("Search", searchPanel);
		tabpane.addTab("Exclude", excludedPanel);
		tabpane.addTab("Algorithms", searchAlgos);
		
		//Frame
		frm = new JFrame(Config.getFullName());
		frm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//frm.setSize(800,600);
		frm.setLocationRelativeTo(null);
		frm.setResizable(true);
		frm.setContentPane(tabpane);
		frm.getRootPane().setDefaultButton(btnExcluding);		
	}
	
	private void loadAlgorithms(DefaultListModel<String> dlm) {
		String[] algos = CrawlingAlgorithm.getAvailableAlgorithms();
		for (int i = 0; i < algos.length; i++) {
			dlm.addElement(algos[i]);
		}
	}
	
	private String[] getCadenas() {
		String cadena = txtTerms.getText();
		StringTokenizer st = new StringTokenizer(cadena,",");
		String[] cadenas = new String[st.countTokens()];
		for(int i = 0; i < cadenas.length; i++) {
			cadenas[i] = st.nextToken();
		}
		return cadenas;
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
	        UIManager.setLookAndFeel(
	            UIManager.getCrossPlatformLookAndFeelClassName());
	    } catch (Exception e) { }
	    W3s w3s = new W3s();
		w3s.frm.pack();
		Toolkit tk = Toolkit.getDefaultToolkit();
	    Dimension screenSize = tk.getScreenSize();
	    Dimension w3sSize = w3s.frm.getSize();
	    w3s.frm.setLocation(screenSize.width/2 - w3sSize.width/2, screenSize.height/2 - w3sSize.height/2);
		w3s.frm.setVisible(true);
	}

	class W3sWorker extends SwingWorker<List<String>, String> {
		private boolean cancel = false;
		private Config setup;
		
		private ExcludedElement[] excludedLinks() {
			mdlExcluded.trimToSize();
			ExcludedElement[] excluded = new ExcludedElement[mdlExcluded.size()];
			Enumeration<ExcludedElement> e = mdlExcluded.elements();
			for (int i = 0; e.hasMoreElements(); i++) {
				excluded[i] = (ExcludedElement) e.nextElement();
			}
			return excluded;
		}
		
		public List<String> doInBackground() {
			setStopping(false);
			CrawlingAlgorithm calg = new CrawlingAlgorithm();
			calg.setAlgorithm(txtCurrentAlgorithm.getText());
			//LinksExplorer le = null;
			setup = new Config();
			setup.setProperty("DEPTH",""+sldDepth.getValue());
			setup.setProperty("MAXENTRIES",""+sldMax.getValue());
			setup.setProperty("ONLYINTERNAL", chkIntOnly.isSelected() ? "TRUE" : "FALSE");
			setup.setProperty("ONLYEXTERNAL", chkExtOnly.isSelected() ? "TRUE" : "FALSE");
			setup.setProperty("PRIORIZE", chkPriority.isSelected() ? "TRUE" : "FALSE");
			setup.setProperty("CUTBRANCHES", chkCutBranches.isSelected() ? "TRUE" : "FALSE");
			setup.setProperty("FROM", txtStartDate.getText());
			setup.setProperty("TO", txtEndDate.getText());
			setup.setProperty("FILENAME", "output.htm");
			try {
				calg.Init(txtURL.getText(),getCadenas(),excludedLinks(),setup,new SWOutput(this));
				calg.Call();
			} catch (Exception e) {	
				/*if (le != null) {
					PrintWriter out = le.getOut();
					try {
						out.close();
					} catch (Exception e2) {}
				}*/
				txtOut.append(e.getMessage());
				e.printStackTrace();
			}
			return null;
	    }
		
		protected void process(List<String> chunks) {
			for (String s :  chunks) {
				if (s.startsWith("ERR:")) {
					txtOut.append(s.substring(4));
				} else if (s.startsWith("Bar/")) {
					String[] data = s.split("/");
					try {
						int val = Integer.parseInt(data[1]);
						int max = Integer.parseInt(data[2]);
						val = max - val;
						prgDetails.setText(""+val+"/"+max);
						progress.setMaximum(max);
						progress.setValue(val);
					} catch (Exception e ) {
					}
				} else {
					txtWorking.setText(s);
				}
			}
		}
		
		public void doPublish(String s) {
			publish(s);
		}

		protected void done() {
			String filename = setup.getProperty("FILENAME", "salida.htm");
			try {
				txtOut.append("\nSearching finished. Now you can browse the "+filename+" file.\n");
				txtOut.append("Thank you.\n");
				String curDir = System.getProperty("user.dir");
				
				Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler "+curDir+"\\"+filename);
			} catch (Exception e) {
				txtOut.append(e.getMessage());
			}
			btn.setText("Search");
			btn.setEnabled(true);
			frm.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		}

		public boolean isStopping() {
			return cancel;
		}

		public void setStopping(boolean cancel) {
			this.cancel = cancel;
		}	
	}	
}
