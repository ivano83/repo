import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 * @author Ivano
 *
 * AzioneOutputRubrica. Finestra output sms
 */
public class AzioneOutputSms implements ActionListener {

	private JFileChooser fc = new JFileChooser();
	private InterfacciaGrafica interfaccia;
	private Language language;

	public AzioneOutputSms(InterfacciaGrafica i) {
		this.interfaccia = i;
		language = interfaccia.getLanguage();
	}
	
	/**
	 * Metodo che viene chiamato per scegliere l'output
	 * @param e evento scatenante
	 */
	public void actionPerformed(ActionEvent e) {
			
		//nuovo frame 
		JFrame f2 = new JFrame("Output file");
		int scelta = fc.showOpenDialog(f2);
		
		fc.approveSelection();
		if (scelta==JFileChooser.APPROVE_OPTION) {
			File fileOut = fc.getSelectedFile();
			int n = 0;
			/* Se il file esiste apre una finestra di dialogo per scegliere se sovrascrivere o no */
			if(fileOut.exists()) {
				n = JOptionPane.showConfirmDialog (new JFrame(),
						language.getText("85"), language.getText("79"),
					JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
			}
			
			/* Se si è scelto di sovrascrivere, oppure il file non esiste, allora si va avanti */
			if(n==0) {
				//System.out.println(fileOut.getPath());
				interfaccia.insertTextArea(language.getText("24")+": "+ fileOut.getPath() +"\n");
				interfaccia.setTestoOutputSms(fileOut.getPath());
			}
		}
	}

}
