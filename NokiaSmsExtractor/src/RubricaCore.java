import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.PrintStream;
import java.util.*;

/**
 * @author Ivano
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class RubricaCore {
	
	public static final int TXT = 0;
	public static final int XLS = 1;
	private FileReader fileAperto;
	private Scanner sc;
	private int formato;
	private Set rubrica;
	private Language language;
	private String outputPath;
	
	public RubricaCore(Language language) {
		this.language = language;
		this.formato = 0; //default: formato txt
		this.rubrica = new TreeSet();
		this.outputPath = "";
	}
	
	public String start(String input, String output) {
		this.outputPath = output;
		String testo = this.filtraInformazioni(input);
		this.SistemaTesto(testo);
		RubricaCreator rc = this.scegliCreatore();
		testo = rc.preparaStampa(rubrica);
		this.stampaSuFile(testo);
		this.clear();
		return testo;
	}
	public Set startNoPrint(String input, String output) {
		this.outputPath = output;
		String testo = this.filtraInformazioni(input);
		this.SistemaTesto(testo);
		RubricaCreator rc = this.scegliCreatore();
		testo = rc.preparaStampa(rubrica);
		return this.rubrica;
	}
	
	public void endPrint(Set m) {
		RubricaCreator rc = this.scegliCreatore();
		String testo = rc.preparaStampa(rubrica);
		this.stampaSuFile(testo);
		this.clear();
	}
	
	/* Sceglie a seconda del tipo di formato il creatore di rubrica apposito */
	private RubricaCreator scegliCreatore(){
		RubricaCreator rc;
		if(formato==RubricaCore.TXT) {
			if(!outputPath.contains(".txt"))
				outputPath += ".txt";
			rc = new RubricaCreatorTxt(language, outputPath);
		}
		else if(formato==RubricaCore.XLS) {
			if(!outputPath.contains(".xls"))
				outputPath += ".xls";
			rc = new RubricaCreatorXls(language, outputPath);
		}
		else {
			rc = new RubricaCreatorTxt(language, outputPath);
		}
		return rc;
	}
	
	/* tra tutto il testo seleziona e restituisce solo quello interessato */
	private String filtraInformazioni(String input){
		
		StringBuffer sb = new StringBuffer(); //verranno inserite le lettere buone
		String line; //linea corrente
		boolean go = false;  //da dove partire
		boolean stop = false;  //dove fermarsi
		boolean endMsg = false;  //aiuta a capire dove fermarsi
		
		try {
			fileAperto = new FileReader(input);
			Scanner sc = new Scanner(fileAperto);
			while(sc.hasNextLine() && !stop){
				line = sc.nextLine(); //prossima riga del file
				
				/* Trova l'inizio dei messaggi */
				if(line.contains("×0\\zðhÓ")) {
					go = true;
				}
				/* Trova la fine dei messaggi */
				//if(line.contains("Íøè#^ZN·5ÝßñH") && endMsg) {
				if(line.contains("azïÑª¾¡I") && endMsg) {
					stop = true;
				}
				/* prende solo l'intervallo di testo contenente i messaggi */
				if(go) {
					sb.append(line + "\n");
					
					/* valore usato per capire quando si è alla fine dei messaggi */
					if(line.contains("END:VCARD")) {
						endMsg = true;
					}
				}
			}
			fileAperto.close();
		} catch (Exception e) {}

		return sb.toString();
	}
	
	/* Prende il nome e il numero e li salva in un contatto */
	private Set SistemaTesto(String testo) {
		String numero = "";
		Contatto c = new Contatto();
		
		try {
			Scanner sc = new Scanner(testo);  //prende il testo di input
			String line;
			while(sc.hasNextLine()){
				line = sc.nextLine();
				char[] riga = line.toCharArray();
				/* trova la riga in cui è presente il nome del contatto */
				if(riga[0]=='N') {
					c = new Contatto();
					int i = riga.length-1;
					
					/** riga del tipo:
					 * N;ENCODING=QUOTED-PRINTABLE;CHARSET=UTF-8:NOME;;;; 
					 * N:NOME;;;; 
					 * */
					while(riga[i]==';') {
						i--;
					}
					StringBuffer nome = new StringBuffer();
					while(riga[i]!=';' && riga[i]!=':') {
						nome.append(riga[i]);
						i--;
					}
					nome = nome.reverse();
					c.setNome(nome.toString());
				}
				/* trova la riga in cui è presente il numero del contatto */
				if(line.contains("TEL;")) {
					int i = 0;
					
					/** Riga del tipo:
					 * TEL;CELL:+393333333333
					 * TEL;VOICE:+393333333333 
					 * */
					while(riga[i]!=':')
						i++;
					numero = line.substring(i+1,riga.length);
					c.setNumero(numero);
					rubrica.add(c);  //il numero viene sempre dopo il nome (nel testo)
				}
			}
		} catch (Exception e) {}
		
		return rubrica;
	}
	
	
	public void stampaSuFile(String testo) {
		try {
			FileOutputStream discoFileStream;
			PrintStream disco; //Oggetto che punta al File

			discoFileStream = new FileOutputStream(outputPath);
			disco = new PrintStream(discoFileStream);
			disco.println(testo);
			
			discoFileStream.close();
		} catch (Exception e) {}
		
	}
	/* modifica il formato scelto */
	public void setFormato(int i) {
		this.formato = i;
	}
	/* Svuota l'insieme dei contatti, per una nuova ricerca */
	public void clear() {
		this.rubrica = new TreeSet();
	}
	public Set getRubrica() {
		return this.rubrica;
	}
}
