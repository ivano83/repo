import java.util.Iterator;
import java.util.List;

/*
 * Created on 8-ott-2007
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
public class SmsCreatorXls implements SmsCreator {

	private StringBuffer output = new StringBuffer();
	private Language language;
	
	/**
	 * @param language
	 */
	public SmsCreatorXls(Language language) {
		this.language = language;
	}

	public String preparaStampa(List messaggi) {
		output.append("<TABLE border=1>\n");
		output.append("<TR>\n");
		output.append("<TD STYLE='vnd.ms-excel.numberformat:@'>"+language.getText("52")+"</TD>\n");
		output.append("<TD STYLE='vnd.ms-excel.numberformat:@'>"+language.getText("54")+"</TD>\n");
		output.append("<TD STYLE='vnd.ms-excel.numberformat:@'>"+language.getText("53")+"</TD>\n");
		output.append("<TD STYLE='vnd.ms-excel.numberformat:@'>"+language.getText("51")+"</TD>\n");
		output.append("</TR>");
		Messaggio m;
		Iterator i = messaggi.iterator();
		while(i.hasNext()){
			m = (Messaggio)i.next();
			output.append("<TR>\n");
			if(m.getInviato()==true)
				output.append("<TD STYLE='vnd.ms-excel.numberformat:@'>"+language.getText("55")+"</TD>\n");
			else
				output.append("<TD STYLE='vnd.ms-excel.numberformat:@'>"+language.getText("56")+"</TD>\n");
			output.append("<TD STYLE='vnd.ms-excel.numberformat:@'>" + m.getMittente() + "</TD>\n");
			output.append("<TD STYLE='vnd.ms-excel.numberformat:@'>" + m.getMessaggio() + "</TD>\n");
			output.append("<TD STYLE='vnd.ms-excel.numberformat:@'>" + m.getData() + "</TD>\n");
			output.append("</TR>");
		}
		return output.toString();
	
	}

}
