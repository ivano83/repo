import java.util.Iterator;
import java.util.List;

/**
 * @author Ivano
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class SmsCreatorTxt implements SmsCreator {
	
	private StringBuffer output = new StringBuffer();
	private Language language;
	
	/**
	 * @param language
	 */
	public SmsCreatorTxt(Language language) {
		this.language = language;
	}

	public String preparaStampa(List messaggi) {
		Messaggio m;
		Iterator i = messaggi.iterator();
		while(i.hasNext()){
			m = (Messaggio)i.next();
			if(m.getInviato()==true)
				output.append("-----"+language.getText("55")+"-----\n");
			else
				output.append("-----"+language.getText("56")+"-----\n");
			output.append("NUM: " + m.getMittente() + "\n");
			output.append(language.getText("51")+": " + m.getData() + "\n");
			output.append(m.getMessaggio() + "\n\n");
		}
		return output.toString();
	
	}
	
}
