package GUI;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.LineBorder;

import actions.RenameFile;

/**
* This code was edited or generated using CloudGarden's Jigloo
* SWT/Swing GUI Builder, which is free for non-commercial
* use. If Jigloo is being used commercially (ie, by a corporation,
* company or business for any purpose whatever) then you
* should purchase a license for each developer using Jigloo.
* Please visit www.cloudgarden.com for details.
* Use of Jigloo implies acceptance of these licensing terms.
* A COMMERCIAL LICENSE HAS NOT BEEN PURCHASED FOR
* THIS MACHINE, SO JIGLOO OR THIS CODE CANNOT BE USED
* LEGALLY FOR ANY CORPORATE OR COMMERCIAL PURPOSE.
*/
public class PrincipalGUI extends javax.swing.JFrame {

	{
		//Set Look & Feel
		try {
			javax.swing.UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	private JButton openButton;
	private JButton startButton;
	private JButton infoRubricaButton;
	private JButton rubricaButton;
	
	private JRadioButton radioButton1;
	private JRadioButton radioButton2;
	
	private JTabbedPane jTabbedPane;
	private JDialog infoRubricaWindow;
	
	private JTextField rubricaField;
	private JTextArea infoArea;
	private JTextArea logArea;
	private JTextField pathField;
	
	private JPanel panelManager;
	private JPanel panelRename;

	private JFileChooser fc = new JFileChooser();
	private File dirScelta;
	private File rubricaScelta;
	
	private AbstractAction infoRubricaAction;
	private AbstractAction rubricaAction;
	private AbstractAction openAction;
	private AbstractAction eseguiAction;
	
	/**
	* Auto-generated main method to display this JFrame
	*/
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				PrincipalGUI inst = new PrincipalGUI();
				inst.setLocationRelativeTo(null);
				inst.setVisible(true);
			}
		});
	}
	
	public PrincipalGUI() {
		super("Cell Call Rename");
		initGUI();
	}
	
//	public PrincipalGUI getGUI() {
//		return this;
//	}
	
	private void initGUI() {
		try {
			{
				getContentPane().add(getJTabbedPane(), BorderLayout.CENTER);
				// associamo l'evento di chiusura al solito bottone di chiusura
				this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			}
			
			this.setSize(650, 500);
			{
				JMenuItem helpMenuItem;
				JMenu jMenu5;
				JMenuItem deleteMenuItem;
				JSeparator jSeparator1;
				JMenuItem pasteMenuItem;
				JMenuItem copyMenuItem;
				JMenuItem cutMenuItem;
				JMenu jMenu4;
				JMenuItem exitMenuItem;
				JSeparator jSeparator2;
				JMenuItem closeFileMenuItem;
				JMenuItem saveAsMenuItem;
				JMenuItem saveMenuItem;
				JMenuItem openFileMenuItem;
				JMenuItem newFileMenuItem;
				JMenu jMenu3;
				JMenuBar jMenuBar1;
				
				jMenuBar1 = new JMenuBar();
				setJMenuBar(jMenuBar1);
				{
					jMenu3 = new JMenu();
					jMenuBar1.add(jMenu3);
					jMenu3.setText("File");
					{
						newFileMenuItem = new JMenuItem();
						jMenu3.add(newFileMenuItem);
						newFileMenuItem.setText("New");
					}
					{
						openFileMenuItem = new JMenuItem();
						jMenu3.add(openFileMenuItem);
						openFileMenuItem.setText("Open");
					}
					{
						saveMenuItem = new JMenuItem();
						jMenu3.add(saveMenuItem);
						saveMenuItem.setText("Save");
					}
					{
						saveAsMenuItem = new JMenuItem();
						jMenu3.add(saveAsMenuItem);
						saveAsMenuItem.setText("Save As ...");
					}
					{
						closeFileMenuItem = new JMenuItem();
						jMenu3.add(closeFileMenuItem);
						closeFileMenuItem.setText("Close");
					}
					{
						jSeparator2 = new JSeparator();
						jMenu3.add(jSeparator2);
					}
					{
						exitMenuItem = new JMenuItem();
						jMenu3.add(exitMenuItem);
						exitMenuItem.setText("Exit");
					}
				}
				{
					jMenu4 = new JMenu();
					jMenuBar1.add(jMenu4);
					jMenu4.setText("Edit");
					{
						cutMenuItem = new JMenuItem();
						jMenu4.add(cutMenuItem);
						cutMenuItem.setText("Cut");
					}
					{
						copyMenuItem = new JMenuItem();
						jMenu4.add(copyMenuItem);
						copyMenuItem.setText("Copy");
					}
					{
						pasteMenuItem = new JMenuItem();
						jMenu4.add(pasteMenuItem);
						pasteMenuItem.setText("Paste");
					}
					{
						jSeparator1 = new JSeparator();
						jMenu4.add(jSeparator1);
					}
					{
						deleteMenuItem = new JMenuItem();
						jMenu4.add(deleteMenuItem);
						deleteMenuItem.setText("Delete");
					}
				}
				{
					jMenu5 = new JMenu();
					jMenuBar1.add(jMenu5);
					jMenu5.setText("Help");
					{
						helpMenuItem = new JMenuItem();
						jMenu5.add(helpMenuItem);
						helpMenuItem.setText("Help");
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private AbstractAction getOpenAction() {
		if(openAction == null) {
			openAction = new AbstractAction("Apri", null) {
				
				
				public void actionPerformed(ActionEvent evt) {
					JFrame f1 = new JFrame();
					fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
					int scelta = fc.showOpenDialog(f1);
					
					if (scelta == JFileChooser.APPROVE_OPTION) {
						dirScelta = fc.getSelectedFile();
						pathField.setText(dirScelta.getPath());
					}

				}
			};
		}
		return openAction;
	}
	
	private JTabbedPane getJTabbedPane() {
		if(jTabbedPane == null) {
			jTabbedPane = new JTabbedPane();
			jTabbedPane.addTab("panelRename", null, getPanelRename(), null);
			jTabbedPane.addTab("panelManager", null, getPanelManager(), null);

		}
		return jTabbedPane;
	}
	
	private JPanel getPanelRename() {
		if(panelRename == null) {
			panelRename = new JPanel();
			GridBagLayout panelRenameLayout = new GridBagLayout();
			panelRenameLayout.rowWeights = new double[] {0.0, 0.0, 0.0, 0.0, 0.1};
			panelRenameLayout.rowHeights = new int[] {43, 40, 30, 10, 7, 7};
			panelRenameLayout.columnWeights = new double[] {0.0, 0.0, 0.0, 0.1};
			panelRenameLayout.columnWidths = new int[] {7, 119, 238, 7, 7, 4};
			panelRename.setLayout(panelRenameLayout);
			panelRename.setPreferredSize(new java.awt.Dimension(487, 425));
			panelRename.add(getPathField(), new GridBagConstraints(1, 0, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
			panelRename.add(getOpenButton(), new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 6, 0, 0), 0, 0));
			panelRename.add(getLogArea(), new GridBagConstraints(1, 4, 3, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
			panelRename.add(getStartButton(), new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
			panelRename.add(getInfoArea(), new GridBagConstraints(2, 3, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
			panelRename.add(getRubricaField(), new GridBagConstraints(1, 1, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
			panelRename.add(getRubricaButton(), new GridBagConstraints(3, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 6, 0, 0), 0, 0));
			panelRename.add(getInfoRubricaButton(), new GridBagConstraints(3, 1, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		
		
			 //Create the radio buttons.
		    radioButton1 = new JRadioButton("P[num.tel][in/out][gg-mm-aaaa]-[hh-mm-ss]");
		    radioButton1.setActionCommand("[][][][]");
		    radioButton1.setName("[][][][]");
		    radioButton1.setSelected(true);

		    radioButton2 = new JRadioButton("<nome/num>_<gg-mm-aaaa>_<hh-mm-ss>");
		    radioButton2.setActionCommand("__");
		    radioButton2.setName("__");

		    //Group the radio buttons.
		    ButtonGroup group = new ButtonGroup();
		    group.add(radioButton1);
		    group.add(radioButton2);

		    panelRename.add(radioButton1, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		    panelRename.add(radioButton2, new GridBagConstraints(2, 2, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
			
		
		}
		return panelRename;
	}
	
	private JPanel getPanelManager() {
		if(panelManager == null) {
			panelManager = new JPanel();
		}
		return panelManager;
	}
	
	public JTextField getPathField() {
		if(pathField == null) {
			pathField = new JTextField();
			pathField.setEditable(false);
			pathField.setText("<Select folder>");
		}
		return pathField;
	}
	
	private JButton getOpenButton() {
		if(openButton == null) {
			openButton = new JButton();
			openButton.setText("Apri");
			openButton.setAction(getOpenAction());
		}
		return openButton;
	}
	
	private JTextArea getLogArea() {
		if(logArea == null) {
			logArea = new JTextArea();
		}
		return logArea;
	}
	
	private JButton getStartButton() {
		if(startButton == null) {
			startButton = new JButton();
			startButton.setText("Esegui");
			startButton.setAction(this.getEseguiAction());
		}
		return startButton;
	}
	
	private JTextArea getInfoArea() {
		if(infoArea == null) {
			infoArea = new JTextArea();
			String info = " Verranno selezionati solo i file con il seguente pattern: \n\n  " +
					" P[num.tel][in/out][gg-mm-aaaa]-[hh-mm-ss] \n\n" +
					" Premendo il bottone \"Esegui\" verranno trasformati in questo modo: \n\n  " +
					" [aaaa-mm-gg] [hh-mm-ss] [num.tel] [in/out]";
			//Non-visual components.add(getInfoRubricaWindow());

			infoArea.setText(info);
			infoArea.setEditable(false);
			infoArea.setFont(new java.awt.Font("Tahoma",0,10));
			infoArea.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
			infoArea.setBackground(new java.awt.Color(192,192,192));
		}
		return infoArea;
	}
	
	private JTextField getRubricaField() {
		if(rubricaField == null) {
			rubricaField = new JTextField();
			rubricaField.setText("<Import rubrica>");
			rubricaField.setEditable(false);
		}
		return rubricaField;
	}
	
	private JButton getRubricaButton() {
		if(rubricaButton == null) {
			rubricaButton = new JButton();
			rubricaButton.setText("Importa");
			rubricaButton.setAction(getRubricaAction());
		}
		return rubricaButton;
	}
	
	private JButton getInfoRubricaButton() {
		if(infoRubricaButton == null) {
			infoRubricaButton = new JButton();
			infoRubricaButton.setText("?");
			infoRubricaButton.setAction(getInfoRubricaAction());

		}
		return infoRubricaButton;
	}
	
	private AbstractAction getRubricaAction() {
		if(rubricaAction == null) {
			rubricaAction = new AbstractAction("Importa", null) {
				

				public void actionPerformed(ActionEvent evt) {
					JFrame f2 = new JFrame();
					JFileChooser fc2 = new JFileChooser();
					//fc2 .setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
					int scelta = fc2.showOpenDialog(f2);
					
					if (scelta == JFileChooser.APPROVE_OPTION) {
						rubricaScelta = fc2.getSelectedFile();
						rubricaField.setText(rubricaScelta.getPath());
					}

				}
			};
		}
		return rubricaAction;
	}
	
	private AbstractAction getInfoRubricaAction() {
		if(infoRubricaAction == null) {
			infoRubricaAction = new AbstractAction("?", null) {
				public void actionPerformed(ActionEvent evt) {
					getInfoRubricaDialog();
				}
			};
		}
		return infoRubricaAction;
	}
	
	private JDialog getInfoRubricaDialog() {
		if(infoRubricaWindow == null) {
			infoRubricaWindow = new JDialog();
			String infoRubrica = "Importando una rubrica è possibile sostituire \n" +
						"il numero di telefono con il nome associato. \n" +
						"Il file rubrica dovrà essere necessariamente in \n" +
						"formato testuale, e deve contenere delle righe \n" +
						"formate in questo modo: \n" +
						"<nome_persona>=<num.tel>\n\n" +
						"Esempi:\nMario Rossi=3334567890\nJohn=3482345678\n";

			{
				JFrame f = new JFrame();
				//infoRubricaWindow.getContentPane().add(f, BorderLayout.CENTER);
				{
					JOptionPane option = new JOptionPane (infoRubrica, JOptionPane.INFORMATION_MESSAGE, JOptionPane.CLOSED_OPTION);
					f.getContentPane().add(option, BorderLayout.CENTER);
					infoRubricaWindow = option.createDialog(f, "Info");
					infoRubricaWindow.setVisible(true);
				}
			}
			
		}
		return infoRubricaWindow;
	}
	
	
	private AbstractAction getEseguiAction() {
		if(eseguiAction == null) {
			eseguiAction = new AbstractAction("Esegui", null) {
				
				
				public void actionPerformed(ActionEvent evt) {
					
					String urlRubrica = null;
					if(rubricaScelta!=null)
						urlRubrica = rubricaScelta.getPath();
					
					RenameFile ren = new RenameFile();
					System.out.println("getAbsolutePath " + dirScelta.getAbsolutePath());
					try {
						System.out.println("getCanonicalPath " + dirScelta.getCanonicalPath());
					} catch (IOException e) {
					}
					System.out.println("getPath " + dirScelta.getPath());
					System.out.println("getName " + dirScelta.getName());
					
					String tipoOp = null;
					if(radioButton1.isSelected())
						tipoOp = radioButton1.getActionCommand();
					else if(radioButton2.isSelected())
						tipoOp = radioButton2.getActionCommand();
					
//					ren.modificaNomiFile("[][][][]", dirScelta.getPath(), urlRubrica, dirScelta.getPath()+"\\MODIFICATE");
					ren.modificaNomiFile(tipoOp, dirScelta.getPath(), urlRubrica, dirScelta.getPath()+"\\MODIFICATE");

				}
			};
		}
		return eseguiAction;
	}
	
	

}
