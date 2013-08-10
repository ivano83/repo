import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Scanner;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 * @author Ivano
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class AzioneCambiaLinguaggio implements ActionListener {

	private String nomeLinguaggio;
	private InterfacciaGrafica interfaccia;
	private Language linguaggio;


	public AzioneCambiaLinguaggio(String nomeLinguaggio, InterfacciaGrafica interfaccia) {
		this.nomeLinguaggio = nomeLinguaggio;
		this.interfaccia = interfaccia;
		this.linguaggio = interfaccia.getLanguage();
	}

	
	public void actionPerformed(ActionEvent arg0) {
		try {
			File fileConf = new File("CONF");
			//legge il linguaggio fissato
			FileReader fr = new FileReader(fileConf);
			Scanner sc = new Scanner(fr);
			String line;
			StringBuffer testoCompleto = new StringBuffer();;
			while(sc.hasNext()) {
				line = sc.nextLine();
				if(line.contains("language")) {
					line = "language=" + this.nomeLinguaggio;
				}
				testoCompleto.append(line + "\n");
			}
			FileOutputStream discoFileStream = new FileOutputStream(fileConf);
			PrintStream disco = new PrintStream(discoFileStream); //Oggetto che punta al File
			disco.println(testoCompleto.toString());
			
			discoFileStream.close();
			fr.close();
		} catch(FileNotFoundException e) {System.exit(1);}
		  catch (IOException e1) {e1.printStackTrace();	}
		  
		interfaccia.getLanguage().caricaLinguaggio(nomeLinguaggio);
		interfaccia.insertTextArea("-- "+linguaggio.getText("9")+": "+ nomeLinguaggio +"\n");
	
		JOptionPane option = new JOptionPane (linguaggio.getText("88"), JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION);
		JDialog dialog = option.createDialog(new JFrame(),linguaggio.getText("79"));
		dialog.setVisible(true);
	
	}

}
