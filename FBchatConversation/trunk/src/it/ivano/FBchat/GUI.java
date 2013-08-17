package it.ivano.FBchat;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.SoftBevelBorder;

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
public class GUI extends javax.swing.JFrame {


	
	private AbstractAction openOutputAction;
	private AbstractAction openFile;
	private AbstractAction generateAction;
	
	private JButton generate;
	private JButton openOutput;
	private JButton open;
	
	private JTextField textOutput;
	private JTextField textInput;
	private JTextField textYourName;
	private JTextField textOtherName;
	
	private JLabel labelYourName;
	private JLabel labelOtherName;
	private JLabel labelInfo;
	
	private JPanel jPanel;
	
	private JMenuBar jMenuBar1;

	private JMenu jMenu1;
	private JMenuItem exitMenuItem;
	private JSeparator jSeparator1;
	private JMenuItem closeFileMenuItem;
	private JMenuItem saveAsMenuItem;
	private JMenuItem saveMenuItem;
	private JMenuItem openFileMenuItem;
	private JMenuItem newFileMenuItem;
	
	private JMenu jMenu2;
	private JMenuItem deleteMenuItem;
	private JSeparator jSeparator2;
	private JMenuItem pasteMenuItem;
	private JMenuItem copyMenuItem;
	private JMenuItem cutMenuItem;
	
	private JMenu jMenu3;
	private JMenuItem helpMenuItem;
	
	private Properties prop = PropertiesUtility.caricaProprieta();
	
	/**
	* Auto-generated main method to display this JFrame
	*/
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			

			public void run() {
				try {
					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
								
				} catch (Exception e) {
					System.exit(0);
				}
				GUI inst = new GUI();
				inst.setLocationRelativeTo(null);
				inst.setVisible(true);
				inst.setTitle("Facebook chat --> MSN style Converter");
//				inst.setResizable(false);
			}
		});
	}
	
	public GUI() {
		super();
		initGUI();
	}
	
	private void initGUI() {
		try {
			{
				jPanel = new JPanel();
				getContentPane().add(jPanel, BorderLayout.CENTER);
				GridBagLayout jPanel1Layout = new GridBagLayout();
				jPanel1Layout.rowWeights = new double[] {0.1, 0.1, 0.1, 0.1};
				jPanel1Layout.rowHeights = new int[] {7, 7, 7, 7, 7, 7};
				jPanel1Layout.columnWeights = new double[] {0.0, 0.0, 0.1};
				jPanel1Layout.columnWidths = new int[] {7, 150, 100, 5, 65, 7};
				jPanel.setLayout(jPanel1Layout);
//				jPanel1.setBorder(BorderFactory.createLineBorder(Color.black));
//				jPanel1.setPreferredSize(new java.awt.Dimension(392, 140));
				
				GridBagConstraints gbc = new GridBagConstraints(1, 0, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0);
				
				{
					textInput = new JTextField();
					jPanel.add(textInput, gbc);
					textInput.setText("<seleziona il file sorgente>");
					textInput.setSize(79, 21);
					textInput.setEditable(false);
				}
				gbc.gridx = 4;
				gbc.gridy = 0;
				gbc.gridwidth = 1;
				gbc.fill = GridBagConstraints.NONE;
				{
					open = new JButton();
					jPanel.add(open, gbc);
					open.setText("Carica File");
					open.setAction(getOpenFile());
					open.setFont(new Font(Font.SANS_SERIF, Font.ITALIC, 12));
				}
				gbc.gridx = 1;
				gbc.gridy = 1;
				gbc.gridwidth = 2;
				gbc.fill = GridBagConstraints.HORIZONTAL;
				{
					textOutput = new JTextField();
					jPanel.add(textOutput, gbc);
					textOutput.setText("<seleziona la destinazione>");
					textOutput.setEditable(false);
				}
				gbc.gridx = 4;
				gbc.gridy = 1;
				gbc.gridwidth = 1;
				gbc.fill = GridBagConstraints.NONE;
				{
					openOutput = new JButton();
					jPanel.add(openOutput, gbc);
					openOutput.setText("Destinazione");
					openOutput.setAction(getOpenOutputAction());
					openOutput.setFont(new Font(Font.SANS_SERIF, Font.ITALIC, 12));
				}
				gbc.gridx = 1;
				gbc.gridy = 2;
				gbc.gridwidth = 1;
				gbc.fill = GridBagConstraints.HORIZONTAL;
				
				{
					labelYourName = new JLabel();
					jPanel.add(labelYourName, gbc);
					labelYourName.setText("Nome tuo");
					labelYourName.setFont(new Font(Font.SANS_SERIF, Font.ITALIC, 12));
					labelYourName.setHorizontalAlignment(JLabel.CENTER);
				}
				gbc.gridx = 2;
				gbc.gridy = 2;
				gbc.gridwidth = 3;
				{
					textYourName = new JTextField();
					jPanel.add(textYourName, gbc);
					textYourName.setText("");
					textYourName.setEditable(true);
				}
				gbc.gridx = 1;
				gbc.gridy = 3;
				gbc.gridwidth = 1;
				{
					labelOtherName = new JLabel();
					jPanel.add(labelOtherName, gbc);
					labelOtherName.setText("Nome controparte");
					labelOtherName.setFont(new Font(Font.SANS_SERIF, Font.ITALIC, 12));
					labelOtherName.setHorizontalAlignment(JLabel.CENTER);
				}
				gbc.gridx = 2;
				gbc.gridy = 3;
				gbc.gridwidth = 3;
				{
					textOtherName = new JTextField();
					jPanel.add(textOtherName, gbc);
					textOtherName.setText("");
					textOtherName.setEditable(true);
				}
				gbc.gridx = 1;
				gbc.gridy = 4;
				gbc.gridwidth = 1;
				
				{
					labelInfo = new JLabel();
					jPanel.add(labelInfo, gbc);
					labelInfo.setText("");
					labelInfo.setFont(new Font(Font.SANS_SERIF, Font.ITALIC, 12));
				}
				
				gbc.gridx = 4;
				gbc.gridy = 4;
				gbc.gridwidth = 1;
				gbc.fill = GridBagConstraints.NONE;
				{
					generate = new JButton();
					jPanel.add(generate, gbc);
					generate.setText("Genera");
					generate.setFont(new Font(Font.SANS_SERIF, Font.ITALIC, 12));
					generate.setAction(getGenerateAction());
				}
			}
			this.setSize(410, 300);
			
			// associamo l'evento di chiusura al solito bottone di chiusura
			this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			{
				jMenuBar1 = new JMenuBar();
				setJMenuBar(jMenuBar1);
				{
					jMenu1 = new JMenu();
					jMenuBar1.add(jMenu1);
					jMenu1.setText("File");
					{
						newFileMenuItem = new JMenuItem();
						jMenu1.add(newFileMenuItem);
						newFileMenuItem.setText("New");
					}
					{
						openFileMenuItem = new JMenuItem();
						jMenu1.add(openFileMenuItem);
						openFileMenuItem.setText("Open");
					}
					{
						saveMenuItem = new JMenuItem();
						jMenu1.add(saveMenuItem);
						saveMenuItem.setText("Save");
					}
					{
						saveAsMenuItem = new JMenuItem();
						jMenu1.add(saveAsMenuItem);
						saveAsMenuItem.setText("Save As ...");
					}
					{
						closeFileMenuItem = new JMenuItem();
						jMenu1.add(closeFileMenuItem);
						closeFileMenuItem.setText("Close");
					}
					{
						jSeparator1 = new JSeparator();
						jMenu1.add(jSeparator1);
					}
					{
						exitMenuItem = new JMenuItem();
						jMenu1.add(exitMenuItem);
						exitMenuItem.setText("Exit");
					}
				}
				{
					jMenu2 = new JMenu();
					jMenuBar1.add(jMenu2);
					jMenu2.setText("Edit");
					{
						cutMenuItem = new JMenuItem();
						jMenu2.add(cutMenuItem);
						cutMenuItem.setText("Cut");
					}
					{
						copyMenuItem = new JMenuItem();
						jMenu2.add(copyMenuItem);
						copyMenuItem.setText("Copy");
					}
					{
						pasteMenuItem = new JMenuItem();
						jMenu2.add(pasteMenuItem);
						pasteMenuItem.setText("Paste");
					}
					{
						jSeparator2 = new JSeparator();
						jMenu2.add(jSeparator2);
					}
					{
						deleteMenuItem = new JMenuItem();
						jMenu2.add(deleteMenuItem);
						deleteMenuItem.setText("Delete");
					}
				}
				{
					jMenu3 = new JMenu();
					jMenuBar1.add(jMenu3);
					jMenu3.setText("Help");
					{
						helpMenuItem = new JMenuItem();
						jMenu3.add(helpMenuItem);
						helpMenuItem.setText("Help");
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public JTextField getTextOutput() {
		return textOutput;
	}

	public void setTextOutput(JTextField textOutput) {
		this.textOutput = textOutput;
	}

	public JTextField getTextInput() {
		return textInput;
	}

	public void setTextInput(JTextField textInput) {
		this.textInput = textInput;
	}

	public JTextField getTextYourName() {
		return textYourName;
	}

	public void setTextYourName(JTextField textYourName) {
		this.textYourName = textYourName;
	}

	public JTextField getTextOtherName() {
		return textOtherName;
	}

	public void setTextOtherName(JTextField textOtherName) {
		this.textOtherName = textOtherName;
	}
	

	private AbstractAction getOpenFile() {
		if(openFile == null) {
			openFile = new AbstractAction("Carica File", null) {

				public void actionPerformed(ActionEvent evt) {
					String currDir = prop.getProperty("currentDir");
					JFileChooser fc = new JFileChooser(currDir);
					JFrame f1 = new JFrame("Apri");
					ExampleFileFilter nbu = new ExampleFileFilter("txt","File di testo");
					fc.addChoosableFileFilter(nbu);
					int scelta = fc.showOpenDialog(f1);
					
					if (scelta == JFileChooser.APPROVE_OPTION) {
						File fileScelto = fc.getSelectedFile();
						if(fileScelto.exists() && !fileScelto.isDirectory()) {
							textInput.setText(fileScelto.getPath());
							textOutput.setText(fileScelto.getParent()+"\\"+prop.getProperty("fileOutputName"));
							prop.setProperty("currentDir", fileScelto.getParent());
							PropertiesUtility.salvaProprieta(prop);
							fc.resetChoosableFileFilters(); //resetta i tipi di formato da filtrare
						}
						else {
							fc.resetChoosableFileFilters();
							//new frameAlert("attenzione");
						}
					}
					else if (scelta == JFileChooser.CANCEL_OPTION) {
						fc.resetChoosableFileFilters();
					}
				}
			};
		}
		return openFile;
	}
	
	private AbstractAction getOpenOutputAction() {
		if(openOutputAction == null) {
			openOutputAction = new AbstractAction("Destinazione", null) {
				public void actionPerformed(ActionEvent evt) {
					JFileChooser fc = new JFileChooser();
					JFrame f1 = new JFrame("Apri");
					ExampleFileFilter nbu = new ExampleFileFilter("txt","File di testo");
					fc.addChoosableFileFilter(nbu);
					int scelta = fc.showOpenDialog(f1);
					
					if (scelta == JFileChooser.APPROVE_OPTION) {
						File fileScelto = fc.getSelectedFile();
						if(fileScelto.exists() && !fileScelto.isDirectory()) {
							textOutput.setText(fileScelto.getParent()+"\\"+prop.getProperty("fileOutputName"));
							
							fc.resetChoosableFileFilters(); //resetta i tipi di formato da filtrare
						}
						else {
							fc.resetChoosableFileFilters();
							//new frameAlert("attenzione");
						}
					}
					else if (scelta == JFileChooser.CANCEL_OPTION) {
						fc.resetChoosableFileFilters();
					}
				}
			};
		}
		return openOutputAction;
	}
	
	private AbstractAction getGenerateAction() {
		if(generateAction == null) {
			generateAction = new AbstractAction("Genera", null) {
				public void actionPerformed(ActionEvent evt) {
					Generator g = new Generator(getTextInput().getText(), getTextOutput().getText(), getTextYourName().getText(), getTextOtherName().getText());
					g.setProperties(prop);
					generate.setEnabled(false);
					g.generateCode();
//					List<ConversationRow> lista = g.getListaConversazioni();
					
//					for(ConversationRow c : lista) {
//						System.out.println(c.getNome() + " - " + c.getOra() + " - " + c.getTesto());
//						
//					}
					generate.setEnabled(true);
					labelInfo.setForeground(Color.red);
					labelInfo.setText("Completato!");
				}
			};
		}
		return generateAction;
	}


}
