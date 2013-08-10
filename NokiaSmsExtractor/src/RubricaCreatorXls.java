import java.util.Iterator;
import java.util.Set;
//import jxl.*;
//import jxl.read.biff.BiffException;
//import jxl.write.*;
/*
 * Created on 4-ott-2007
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
public class RubricaCreatorXls implements RubricaCreator{

	private StringBuffer output = new StringBuffer();
	private Language language;
	private String outputFile;
	
	/**
	 * @param language
	 * @param output
	 */
	public RubricaCreatorXls(Language language, String output) {
		this.language = language;
		this.outputFile = output;
	}

	public String preparaStampa(Set rubrica) {
		output.append("<TABLE border=1>\n");
		
		Contatto c;
		String nome;
		Iterator i = rubrica.iterator();
		while(i.hasNext()){
			c = (Contatto)i.next();
			nome = c.getNome();
			//controlla che non ci siano entrambi i simboli su un nome
			if(nome.contains("<") || nome.contains(">"))
				nome = this.trovaSimboliMinoreMaggiore(nome.toCharArray());
			output.append("<TR>\n");
			output.append("<TD STYLE='vnd.ms-excel.numberformat:@'>"+nome+"</TD>\n");
			output.append("<TD STYLE='vnd.ms-excel.numberformat:@'>"+c.getNumero()+"</TD>\n");
			output.append("</TR>");
		}
		
		output.append("</TABLE>\n");
		return output.toString();
	}
	/*
	public String preparaStampa(Set rubrica) {
		try {
			WritableWorkbook workbook = Workbook.createWorkbook(new File(outputFile));
			
			WritableSheet sheet = workbook.createSheet("ciao", 0); 
			
			Contatto c;
			String nome;
			int riga = 0;
			Label label;
			Iterator i = rubrica.iterator();
			while(i.hasNext()){
				c = (Contatto)i.next();
				nome = c.getNome();
				//controlla che non ci siano entrambi i simboli su un nome
				if(nome.contains("<") || nome.contains(">"))
					nome = this.trovaSimboliMinoreMaggiore(nome.toCharArray());
				label = new Label(0, riga, nome); 
				sheet.addCell(label);
				label = new Label(1, riga, c.getNumero()); 
				sheet.addCell(label);
				riga++;
			}
			workbook.write(); 
			workbook.close(); 
		} catch (Exception e) {
			e.printStackTrace();
		}

		return output.toString();
	}	
	*/
	/**
	 * @param cs
	 * @return
	 */
	private String trovaSimboliMinoreMaggiore(char[] cs) {
		int i;
		StringBuffer result = new StringBuffer();
		for(i=0; i<cs.length; i++) {
			if(cs[i]=='<')
				result.append("&lt;");
			else if(cs[i]=='>')
				result.append("&gt;");
			else 
				result.append(cs[i]);
		}
		return result.toString();
	}
}
