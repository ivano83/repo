import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 * AzioneMenu: Classe che implementa ActionListener, gestisce le infomazioni del menu.
 * @author Fiorini Ivano
 * @version 0.1
 */
public class AzioneMenu implements ActionListener {

	private String nomeOggettoMenu;
	private InterfacciaGrafica interfaccia;
	
	private static JFileChooser fc = new JFileChooser();
	private Language language;

	/**
	 * Costruttore dell'azione Menu
	 * @param nomeOggettoMenu il nome dell'oggetto nel menu
	 */
	public AzioneMenu(String nomeOggettoMenu, InterfacciaGrafica i) {
		this.nomeOggettoMenu = nomeOggettoMenu;
		this.interfaccia = i;
		language = interfaccia.getLanguage();
	}

	/**
	 * Metodo che viene chiamato quando c'è un evento del componente a cui è associato questa classe
	 * @param e evento scatenante
	 */
	public void actionPerformed (ActionEvent e){

		//gestisce l'oggetto menu Info
		if(nomeOggettoMenu.equals(language.getText("8"))) {
			//nuovo frame con le informazioni riguardanti il programma stesso
			JFrame f1 = new JFrame(language.getText("17"));

			JTextArea area = new JTextArea();
			area.setEditable(false);
			JScrollPane scrollArea = new JScrollPane(area,
									JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
									JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

			area.append(language.getText("1000"));

			JOptionPane option = new JOptionPane (scrollArea, JOptionPane.INFORMATION_MESSAGE, JOptionPane.CLOSED_OPTION);
			JDialog dialog = option.createDialog(f1,"INFO!");
			dialog.setVisible(true);
		}
		
		//gestisce l'oggetto menu Esci
		if(nomeOggettoMenu.equals(language.getText("7"))) {
			System.exit(1);

		}

	}


}