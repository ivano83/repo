
/**
 * @author Ivano
 *
 * Gestore di varie componenti: smsCore, rubricaCore e language.
 * Aggiorna anche il linguaggio.
 */
public class GraficPublisher {
	
	private SmsCore sms;
	private RubricaCore rubrica;
	private Language language;
	
	public GraficPublisher() {
		//this.aggiornaLinguaggio();
		language = new Language();
		sms = new SmsCore(language);
		rubrica = new RubricaCore(language);
	}
	
	/**
	 * Metodo che restituisce smsCore
	 */
	public SmsCore getSmsCore() {
		return this.sms;
	}
	
	/**
	 * Metodo che restituisce RubricaCore
	 */
	public RubricaCore getRubricaCore() {
		return this.rubrica;
	}
	
	/**
	 * Metodo che restituisce il linguaggio
	 */
	public Language getLanguage() {
		return this.language;
	}

	
}
