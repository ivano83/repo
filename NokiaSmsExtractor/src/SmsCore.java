import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.PrintStream;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.List;

/*
 * Created on 27-set-2007
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
public class SmsCore {
	
	public static final int TXT = 0;
	public static final int XLS = 1;
	private FileReader fileAperto;
	private Scanner sc;
	private int formato;
	private List messaggi;
	private Language language;
	private String outputPath;
	
	public SmsCore(Language language){
		this.language = language;
		formato = 0; //default: formato txt
		this.messaggi = new LinkedList();
	}
	
	public String start(String input, String output) {
		this.outputPath = output;
		String testo = this.filtraInformazioni(input);
		this.SistemaTesto(testo);
		SmsCreator sc = this.scegliCreatore();
		testo = sc.preparaStampa(messaggi);
		this.stampaSuFile(testo);
		this.clear();
		return testo;
	}
	
	public List startNoPrint(String input, String output) {
		this.outputPath = output;
		String testo = this.filtraInformazioni(input);
		this.SistemaTesto(testo);
		return this.messaggi;
	}
	public void endPrint(List m) {
		SmsCreator sc = this.scegliCreatore();
		String testo = sc.preparaStampa(messaggi);
		this.stampaSuFile(testo);
		this.clear();
	}
	
	/* Sceglie a seconda del tipo di formato il creatore di sms apposito */
	private SmsCreator scegliCreatore(){
		SmsCreator sc;
		if(formato==SmsCore.TXT) {
			sc = new SmsCreatorTxt(language);
			if(!outputPath.contains(".txt"))
				outputPath += ".txt";
		}
		else if(formato==SmsCore.XLS) {
			sc = new SmsCreatorXls(language);
			if(!outputPath.contains(".xls"))
				outputPath += ".xls";
		}
		else {
			sc = new SmsCreatorTxt(language);
		}
		return sc;
	}
	
	
	public String filtraInformazioni(String input){
		
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
				if(line.contains("azïÑª¾¡")) {
					go = true;
				}
				/* Trova la fine dei messaggi */
				if(line.contains("ÀáX-­e") && endMsg) {
					stop = true;
				}
				/* prende solo l'intervallo di testo dei messaggi */
				if(go) {
					char[] riga = line.toCharArray();
					int i = 0;
					String lettereBuone = "";
					/* Prende solo i caratteri dispari (quelli che ci interessano) */
					while(i<riga.length) {
						if((i%2)!=0)
							lettereBuone = lettereBuone + riga[i];
						i++;
					}
					sb.append(lettereBuone + "\n");
					
					/* valore usato per decifrare la fine dei messaggi */
					if(lettereBuone.contains("END:VMSG")) {
						endMsg = true;
					}
				}
			}
			fileAperto.close();
		} catch (Exception e) {}
		return sb.toString();
	}
	
	public void SistemaTesto(String s) {
		
		String line;
		try {
			Messaggio m = new Messaggio();
			sc = new Scanner(s);  //prende il testo di input
			while(sc.hasNextLine()){
				line = sc.nextLine();
				

				if(line.contains("X-IRMC-BOX:SENT")) {
					//sb.append("-----INVIATO-----\n");
					m.setInviato(true);
				}
				if(line.contains("X-IRMC-BOX:INBOX")) {
					//sb.append("-----RICEVUTO-----\n");
					m.setInviato(false);
				}
				if(line.contains("TEL:")) {
					m.setMittente(line.substring(4));
					//sb.append(line);
					//sb.append("\n");
				}
				if(line.contains("Date:")) {
					//modifica il formato della data
					String data = this.modificaFormatoData(line);
					m.setData(data);
					line = sc.nextLine();
					
					//copia il corpo del messaggio
					String msg = "";
					while(!(line.contains("END:VBODY"))) {
						msg += line + " ";
						line = sc.nextLine();
					} 
					m.setMessaggio(msg);
					messaggi.add(m);
					m = new Messaggio();
					//sb.append("\n\n");
				}
			}
		} catch (Exception e) {}
	}
	
	/* Modifica il formato della data secondo la mia esigenza */
	private String modificaFormatoData(String line) {
		String newLine = "";

		line = line.substring(5,line.length());
		
		String[] temp = line.split(" ");
		String[] data = temp[0].split("\\.");
		String[] time = temp[1].split(":");
		if(data[0].length()==1)
			data[0] = "0" + data[0];
		if(data[1].length()==1)
			data[1] = "0" + data[1];
		if(time[0].length()==1)
			time[0] = "0" + time[0];
		if(time[1].length()==1)
			time[1] = "0" + time[1];
		if(time[2].length()==1)
			time[2] = "0" + time[2];
		
		newLine = data[0] + "/" + data[1] + "/" + data[2] + "  " + 
				time[0] + "." + time[1] + "." + time[2];
		System.out.println(newLine);
		return newLine;
		
//		char[] riga = line.toCharArray();
//		int i;
//		for(i=0; i<riga.length; i++) {
//			if(riga[i]=='.')  //trasforma il . in /
//				newLine = newLine + "/";
//			else if(riga[i]==':')  //trasforma i : in .
//				newLine = newLine + ".";
//			else
//				newLine = newLine + riga[i];
//		}
//		return newLine;
	}

	
	public void stampaSuFile(String testo) {
		try {
			FileOutputStream discoFileStream;
			PrintStream disco; //Oggetto che punta al File

			discoFileStream = new FileOutputStream(outputPath);
			disco = new PrintStream(discoFileStream);
			disco.println(testo);
		} catch (Exception e) {}
	}

	public void setFormato(int i) {
		this.formato = i;
	}
	/* Svuota la lista dei messaggi, per una nuova ricerca */
	public void clear() {
		this.messaggi = new LinkedList();
	}
	public List getMessaggi() {
		return this.messaggi;
	}
}
