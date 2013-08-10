import java.awt.Checkbox;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author Ivano
 *
 * AzioneBackupRubrica. Operazioni necessarie per la creazione del file rubrica
 */
public class AzioneBackupRubrica implements ActionListener {

	private InterfacciaGrafica interfaccia;
	private Language language;
	
	public AzioneBackupRubrica(InterfacciaGrafica i) {
		this.interfaccia = i;
		language = interfaccia.getLanguage();
	}
	
	/**
	 * Aziona la creazione del file contenente l'insieme della rubrica
	 */
	public void actionPerformed(ActionEvent e) {
		RubricaCore rubrica = interfaccia.getGraficPublisher().getRubricaCore();
		Checkbox cb = interfaccia.getOpzioniRubrica().getCheckboxFormato().getSelectedCheckbox();
		
		//è stato scelto il formato txt
		if(cb.getLabel().contains("txt")) {
			rubrica.setFormato(RubricaCore.TXT);
			interfaccia.insertTextArea("-- "+language.getText("26")+": txt.\n");
		}
		//è stato scelto il formato xls
		else if(cb.getLabel().contains("xls")) {
			rubrica.setFormato(RubricaCore.XLS);
			interfaccia.insertTextArea("-- "+language.getText("26")+": xls.\n");
		}
		String OriginalPath = interfaccia.getTestoApri();
		String OutputPath = interfaccia.getTestoOutputRubrica();
		
		//se il campo di origine è vuoto, avverti
		if(OriginalPath.equals("")){
			new frameAlert(language.getText("81"));
		}
		else {
			String testo = rubrica.start(OriginalPath, OutputPath);
			interfaccia.insertTextArea("-- "+language.getText("83")+"\n");
		}
		
	}
}
