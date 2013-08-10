import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 * @author Ivano
 *
 * AzioneOpzioniSms. Finestra opzioni rubrica
 */
public class AzioneOpzioniSms implements ActionListener {

	private InterfacciaGrafica interfaccia;
	private Language language;

	public AzioneOpzioniSms(InterfacciaGrafica i) {
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
		JOptionPane option = new JOptionPane (interfaccia.getOpzioniSms().getPannelloOpzioniSms(), JOptionPane.INFORMATION_MESSAGE, JOptionPane.CLOSED_OPTION);
		JDialog dialog = option.createDialog(f,language.getText("14"));
		dialog.setVisible(true);
	}

}
