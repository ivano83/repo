import java.util.Iterator;
import java.util.Set;

/*
 * Created on 3-ott-2007
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

/**
 * @author Ivano
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class RubricaCreatorTxt implements RubricaCreator {

	private StringBuffer output = new StringBuffer();
	private Language language;
	private String outputFile;
	
	/**
	 * @param language
	 * @param output
	 */
	public RubricaCreatorTxt(Language language, String output) {
		this.language = language;
		this.outputFile = output;
	}

	public String preparaStampa(Set rubrica) {
		
		Contatto c;
		Iterator i = rubrica.iterator();
		while(i.hasNext()){
			c = (Contatto)i.next();
			output.append(c.getNome()+" - "+c.getNumero()+"\n");
		}
		return output.toString();
	}
}
