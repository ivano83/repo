import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
/*
 * Created on 9-ott-2007
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

/**
 * @author Ivano
 *
 * AzioneOpzioniRubrica. Finestra opzioni rubrica
 */
public class AzioneOpzioniRubrica implements ActionListener {

	private InterfacciaGrafica interfaccia;
	private Language language;

	public AzioneOpzioniRubrica(InterfacciaGrafica i) {
		this.interfaccia = i;
		language = interfaccia.getLanguage();
	}
	
	/**
	 * Metodo che viene chiamato per scegliere le opzioni
	 * @param e evento scatenante
	 */
	public void actionPerformed(ActionEvent e) {
		JFrame f = new JFrame();
		JPanel pannello = new JPanel();
		JOptionPane option = new JOptionPane (interfaccia.getOpzioniRubrica().getPannelloOpzioniRubrica(), JOptionPane.INFORMATION_MESSAGE, JOptionPane.CLOSED_OPTION);
		JDialog dialog = option.createDialog(f,language.getText("14"));
		dialog.setVisible(true);
	}

}
