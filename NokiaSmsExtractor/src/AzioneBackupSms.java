import java.awt.Checkbox;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * @author Ivano
 *
 * AzioneBackupSms. Operazioni necessarie per la creazione del file sms
 */
public class AzioneBackupSms implements ActionListener {

	private InterfacciaGrafica interfaccia;
	private Language language;
	
	public AzioneBackupSms(InterfacciaGrafica i) {
		this.interfaccia = i;
		language = interfaccia.getLanguage();
	}
	
	/**
	 * Aziona la creazione del file contenente la lista degli sms
	 */
	public void actionPerformed(ActionEvent e) {
		SmsCore sms = interfaccia.getGraficPublisher().getSmsCore();
		Checkbox cb = interfaccia.getOpzioniSms().getCheckboxFormato().getSelectedCheckbox();
		
		//è stato scelto il formato txt
		if(cb.getLabel().contains("txt")) {
			sms.setFormato(SmsCore.TXT);
			interfaccia.insertTextArea("-- "+language.getText("26")+": txt.\n");
		}
		//è stato scelto il formato xls
		else if(cb.getLabel().contains("xls")) {
			sms.setFormato(SmsCore.XLS);
			interfaccia.insertTextArea("-- "+language.getText("26")+": xls.\n");
		}
		
		String OriginalPath = interfaccia.getTestoApri();
		String OutputPath = interfaccia.getTestoOutputSms();
		
		//se il campo di origine è vuoto, avverti
		if(OriginalPath.equals("")){
			new frameAlert(language.getText("81"));
		}
		//se il campo di destinazione è vuoto, avverti
		else if(OutputPath.equals("")){
			new frameAlert(language.getText("82"));
		}
		else {
			//si è scelto di associare la rubrica
			if(interfaccia.getOpzioniSms().getCheckBoxAssociaRubrica().getState()) {
				RubricaCore rubrica = interfaccia.getGraficPublisher().getRubricaCore();
				this.gestisciAssociazioneRubrica(OriginalPath, OutputPath, rubrica, sms);
				interfaccia.insertTextArea("-- "+ language.getText("87") +"\n");
			}
			//non si è scelto di associare la rubrica
			else
				sms.start(OriginalPath, OutputPath);
			
			interfaccia.insertTextArea("-- "+language.getText("84")+"\n");
		}
	}

	/**
	 * Metodo che crea la lista dei messaggi gestendo i nomi della rubrica.
	 * Quindi ogni numero di mettente di un messaggio viene sostituito, 
	 * se presente, con il nome corrispondente.
	 */
	private void gestisciAssociazioneRubrica(String OriginalPath, String OutputPath, RubricaCore rubrica, SmsCore sms) {
		
		Set r = rubrica.startNoPrint(OriginalPath, OutputPath);
		List s = sms.startNoPrint(OriginalPath, OutputPath);
		
		/* Per ogni messaggio della lista controlla l'insieme della rubrica.
		 * Se il numero del messaggio è uguale ad un numero in rubrica
		 * allora viene sostituito con il nome associato al numero.
		 */
		Iterator is = s.iterator();
		String mittente = "";
		Messaggio m;
		while(is.hasNext()) {
			m = (Messaggio)is.next();
			mittente = m.getMittente();
			
			Iterator ir = r.iterator();
			String numeroCorrente = "";
			boolean fatto = false;
			while(ir.hasNext() && !fatto) {
				Contatto c = (Contatto)ir.next();
				numeroCorrente = c.getNumero();
				numeroCorrente = this.controllaNumero(numeroCorrente);
				if(numeroCorrente.equals(mittente)) {
					m.setMittente(c.getNome());
					fatto = true;
				}
			}
			
		}
		sms.endPrint(s);  //termina l'operazione di stampa su file
	}

	/**
	 * Controlla se nella stringa è presente il carattere + 
	 * (che indica l'inizio del prefisso internazionale di un paese).
	 * Se non è presente lo inserisce.
	 * @param n - Stringa da controllare
	 * @return una stringa che rappresenta il numero di telefono comprensivo di prefisso internazionale
	 */
	private String controllaNumero(String n) {
		if(!n.contains("+")) {
			return "+39" + n;  //prefisso internazionale italia
		}
		else 
			return n;
	}
}
