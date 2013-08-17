package it.ivano.FBchat;
import java.awt.*;
import java.io.File;
import java.util.Iterator;
import java.util.List;

import javax.swing.*;

/**
 * @author Ivano
 *
 * Interfaccia grafica
 */
public class InterfacciaGrafica extends JFrame {
	
	
	
	/* Barra del menu */
	private JMenuBar barra = new JMenuBar();
	private JMenu menu;
	private JMenu menuSms;
	private JMenu menuRubrica;
	private JMenuItem quit;
	private JMenuItem about;
	private JMenuItem apri;
	private JMenu linguaggio;
	private JMenuItem outSms;
	private JMenuItem outRubrica;
	private JMenuItem backupSms;
	private JMenuItem backupRubrica;
	
	//Inizializzazione oggetti pannello APRI
    private JPanel pannelloApri = new JPanel();
	private JButton bottoneApri;
	private JTextField testoApri = new JTextField(40);
	
	//Inizializzazione oggetti pannello SMS
	private JPanel pannelloSms = new JPanel();
    private JPanel pannelloOutputSms = new JPanel();
	private JButton bottoneOutputSms;
	private JButton bottoneBackupSms;
	private JButton bottoneOpzioniSms;
	private JTextField testoOutputSms = new JTextField(23);
	
	
	//Inizializzazione oggetti 3° pannello
	private JPanel pannelloRubrica = new JPanel();
    private JPanel pannelloOutputRubrica = new JPanel();
	private JButton bottoneOutputRubrica;
	private JButton bottoneBackupRubrica;
	private JButton bottoneOpzioniRubrica;
	private JTextField testoOutputRubrica = new JTextField(23);
	
	//parte bassa del frame (Area testo)
	private JTextArea areaTesto = new JTextArea();
	private JScrollPane scrollTesto = new JScrollPane(areaTesto,
			JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
			JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

	//altri elementi
	private CheckboxGroup cbgSms;
	private CheckboxGroup cbgRubrica;
	private Checkbox checkBoxAssociaRubrica;
	private JPanel pannelloOpzioniSms;
	private JPanel pannelloOpzioniRubrica;

	
	public InterfacciaGrafica() {
		super("NokiaBackupManager 0.5");
	}

	public void inizia() {
		

		//MENU
		this.inizializzaMenu();
		this.inizializzaBottoni();
		this.azioniMenu();
				
		//Inizializzazione contenitore principale (gridLayout)
		Container contenitoreGenerale = this.getContentPane();
		/*griglia divisa in 2 parti: una per la griglia secondaria e l'altra
		  per il campo testuale*/
		GridLayout grigliaEsterna = new GridLayout(2,1,5,5);
		contenitoreGenerale.setLayout(grigliaEsterna);
		//setFont(new Font("SansSerif", Font.PLAIN, 14));
		
		//PANNELLI
		this.inizializzaPannelli();
		
		JPanel pannelloSecondario = new JPanel();
		pannelloSecondario.setLayout(new BorderLayout(5,5));
		pannelloSecondario.add(pannelloApri, BorderLayout.NORTH);
		pannelloSecondario.add(pannelloSms, BorderLayout.EAST);
		pannelloSecondario.add(pannelloRubrica, BorderLayout.WEST);
	
		
		this.azioniBottoni();
		
		contenitoreGenerale.add(pannelloSecondario);
		areaTesto.setFont(new Font("SansSerif", Font.PLAIN, 12));
    	contenitoreGenerale.add(scrollTesto);
    	
		// associamo l'evento di chiusura al solito bottone di chiusura
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		this.setSize(620,450);
		this.setLocation(200,200);
		this.setResizable(true);
		//this.pack();
		this.setVisible(true);
	}
	
	/**
	 * 
	 */
	private void inizializzaBottoni() {
//		bottoneApri = new JButton(language.getText("6"),new ImageIcon("images/apri.png"));
//		bottoneOutputSms = new JButton(language.getText("18"), new ImageIcon("images/apri.png"));
//		bottoneBackupSms = new JButton(language.getText("15"));
//		bottoneOpzioniSms = new JButton(language.getText("14"));
//		bottoneOutputRubrica = new JButton(language.getText("18"),new ImageIcon("images/apri.png"));
//		bottoneBackupRubrica = new JButton(language.getText("16"));
//		bottoneOpzioniRubrica = new JButton(language.getText("14"));
	}

	/**
	 * 
	 */
	private void inizializzaMenu() {

//		menu = new JMenu(language.getText("0"));
//		menuSms = new JMenu(language.getText("1"));
//		menuRubrica = new JMenu(language.getText("2"));
//		quit = new JMenuItem(language.getText("7"));
//		about = new JMenuItem(language.getText("8"));
//		linguaggio = new JMenu(language.getText("9"));
//		apri = new JMenuItem(language.getText("6"));
//		outSms = new JMenuItem(language.getText("13"));
//		outRubrica = new JMenuItem(language.getText("13"));
//		backupSms = new JMenuItem(language.getText("15"));
//		backupRubrica = new JMenuItem(language.getText("16"));
		
		//creazione barra dei menu e le voci
		menu.add(apri);
		menu.add(linguaggio);
		menu.add(about);
		menu.add(quit);
		
//		int i;
//		List listaLinguaggi = language.getListaLinguaggi();
//		JMenuItem j;
//		//Set s = listaLinguaggi.keySet();
//		Iterator it = listaLinguaggi.iterator();
//		String ling;
//		File file;
//		while(it.hasNext()) {
//			Linguaggio lcorr = (Linguaggio)it.next();
//			ling = lcorr.getNome();
//			file = lcorr.getFile();
//			j = new JMenuItem(ling);
//			linguaggio.add(j);
//			j.addActionListener(new AzioneCambiaLinguaggio(ling,this));
//		}
		
		menuSms.add(outSms);
		menuSms.add(backupSms);
		menuRubrica.add(outRubrica);
		menuRubrica.add(backupRubrica);
		barra.add(menu);
		barra.add(menuRubrica);
		barra.add(menuSms);
		setJMenuBar(barra);
	}

	/**
	 * 
	 */
	private void azioniMenu() {
//		about.addActionListener(new AzioneMenu(about.getText(),this));
//		quit.addActionListener(new AzioneMenu(quit.getText(),this));
//		apri.addActionListener(new AzioneApri(this));
//		outSms.addActionListener(new AzioneOutputSms(this));
//		outRubrica.addActionListener(new AzioneOutputRubrica(this));
//		backupSms.addActionListener(new AzioneBackupSms(this));
//		backupRubrica.addActionListener(new AzioneBackupRubrica(this));
	}
	
	/**
	 * 
	 */
	private void azioniBottoni() {
//		bottoneApri.addActionListener(new AzioneApri(this));
//		bottoneOutputSms.addActionListener(new AzioneOutputSms(this));
//		bottoneOutputRubrica.addActionListener(new AzioneOutputRubrica(this));
//		bottoneBackupSms.addActionListener(new AzioneBackupSms(this));
//		bottoneBackupRubrica.addActionListener(new AzioneBackupRubrica(this));
//		bottoneOpzioniSms.addActionListener(new AzioneOpzioniSms(this));
//		bottoneOpzioniRubrica.addActionListener(new AzioneOpzioniRubrica(this));
//		
		//...
	}
	
	/**
	 * 
	 */
	private void inizializzaPannelli() {
	    
		//creazione pannello APRI (file da selezionare)
		pannelloApri.setLayout(new FlowLayout(java.awt.FlowLayout.LEFT,5,5));
		//pannelloApri.setBorder(BorderFactory.createLineBorder(Color.black));
		JLabel titolo = new JLabel("27", JLabel.CENTER);
		testoApri.setEditable(false);
		pannelloApri.add(titolo);
		pannelloApri.add(testoApri);
		pannelloApri.add(bottoneApri);
		
		ImageIcon ii = new ImageIcon("images/logo3.png");
		JLabel logo = new JLabel(ii);
		pannelloApri.add(logo);
		
		//creazione pannello SMS (gestione sms)
		pannelloSms.setLayout(new GridLayout(3,1,2,2));
		pannelloSms.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "20", 2, 0));
		//JLabel titoloSms = new JLabel("Gestione Sms", JLabel.CENTER);
		//titoloSms.setBorder(BorderFactory.createEtchedBorder());
		pannelloOutputSms.setLayout(new FlowLayout(java.awt.FlowLayout.LEFT,5,5));
		testoOutputSms.setEditable(false);
		pannelloOutputSms.add(testoOutputSms);
		pannelloOutputSms.add(bottoneOutputSms);
		pannelloSms.add(pannelloOutputSms);
		pannelloSms.add(bottoneOpzioniSms);
		pannelloSms.add(bottoneBackupSms);
		
		
		//creazione pannello RUBRICA (gestione rubrica)
		pannelloRubrica.setLayout(new GridLayout(3,1,2,2));
		pannelloRubrica.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "21", 2, 0));
		//JLabel titoloRubrica = new JLabel("Gestione Rubrica", JLabel.CENTER);
		//titoloRubrica.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Gestione"));
		pannelloOutputRubrica.setLayout(new FlowLayout(java.awt.FlowLayout.LEFT,5,5));
		testoOutputRubrica.setEditable(false);
		pannelloOutputRubrica.add(testoOutputRubrica);
		pannelloOutputRubrica.add(bottoneOutputRubrica);
		pannelloRubrica.add(pannelloOutputRubrica);
		pannelloRubrica.add(bottoneOpzioniRubrica);
		pannelloRubrica.add(bottoneBackupRubrica);
		
		//creazione pannelli delle opzioni
//		optSms = new OpzioniSms(language);
//		pannelloOpzioniSms = optSms.getPannelloOpzioniSms();
//		optRub = new OpzioniRubrica(language);
//		pannelloOpzioniRubrica = optRub.getPannelloOpzioniRubrica();
//		
		//parametri area di testo
		areaTesto.setEditable(false);
		areaTesto.setLineWrap(true);
		areaTesto.setWrapStyleWord(true);
	
	}
	
	/**
	 * 
	 */
	public void setTestoApri(String path) {
		this.testoApri.setText(path);
	}
	
	/**
	 * 
	 */
	public String getTestoApri() {
		return this.testoApri.getText();
	}
	
	/**
	 * 
	 */
	public void setTestoOutputSms(String path) {
		this.testoOutputSms.setText(path);
	}
	
	/**
	 * 
	 */
	public String getTestoOutputSms() {
		return this.testoOutputSms.getText();
	}
	
	/**
	 * 
	 */
	public void setTestoOutputRubrica(String path) {
		this.testoOutputRubrica.setText(path);
	}
	
	/**
	 * 
	 */
	public String getTestoOutputRubrica() {
		return this.testoOutputRubrica.getText();
	}
	
	/**
	 * 
	 */
	public void insertTextArea(String msg) {
		this.areaTesto.append(msg);
	}

	/**
	 * 
	 */
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			System.exit(0);
		}
		InterfacciaGrafica ig = new InterfacciaGrafica();
		ig.inizia();
	}


	
}
