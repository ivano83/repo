import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.JFrame;

/**
 * @author Ivano
 *
 * AzioneApri. Apre la finestra per la scelta dell'input
 */
public class AzioneApri implements ActionListener {

	private JFileChooser fc = new JFileChooser();
	private InterfacciaGrafica interfaccia;
	private Language language;

	public AzioneApri(InterfacciaGrafica i) {
		this.interfaccia = i;
		language = interfaccia.getLanguage();
	}
	
	/**
	 * Aziona la creazione della finestra in cui si può selezionare l'input
	 */
	public void actionPerformed(ActionEvent e) {
		JFrame f1 = new JFrame(language.getText("22"));
		ExampleFileFilter nbu = new ExampleFileFilter("nbu","Nokia Backup File");
		fc.addChoosableFileFilter(nbu);
		int scelta = fc.showOpenDialog(f1);
		
		if (scelta == JFileChooser.APPROVE_OPTION) {
			File fileScelto = fc.getSelectedFile();
			if(fileScelto.exists() && !fileScelto.isDirectory()) {
				interfaccia.insertTextArea(language.getText("23")+": "+ fileScelto.getPath() +"\n");
				interfaccia.setTestoApri(fileScelto.getPath());
				interfaccia.insertTextArea(language.getText("24")+": "+ fileScelto.getParent()+"\\sms_output\n");
				interfaccia.setTestoOutputSms(fileScelto.getParent()+"\\sms_output");
				interfaccia.insertTextArea(language.getText("25")+": "+ fileScelto.getParent()+"\\rubrica_output\n");
				interfaccia.setTestoOutputRubrica(fileScelto.getParent()+"\\rubrica_output");
				fc.resetChoosableFileFilters(); //resetta i tipi di formato da filtrare
			}
			else {
				fc.resetChoosableFileFilters();
				new frameAlert(language.getText("80"));
			}
		}
		else if (scelta == JFileChooser.CANCEL_OPTION) {
			fc.resetChoosableFileFilters();
		}
	}

}
