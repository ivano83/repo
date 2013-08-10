import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.*;

/**
 * @author Ivano
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Language {

	private Map mappaParole;
	private List listaLinguaggi;
	private Linguaggio linguaggioCorrente;


	public Language() {
		this.listaLinguaggi = this.creaListaLinguaggi();
		this.caricaLinguaggioCorrente();
		this.mappaParole = new TreeMap();
		this.caricaParole();
		
		
	}
	
	/**
	 * Carica tutte le parole nella mappa opportuna, 
	 * dato il linguaggio corrente.
	 */
	private void caricaParole() {
		FileReader fr;
		try {
			fr = new FileReader(this.linguaggioCorrente.getFile());
			this.mappaParole = this.creaMappa(fr);
			
		} catch (FileNotFoundException e) {	e.printStackTrace(); }
			
	}

	/**
	 * Crea la mappa con tutte le parole della lingua corrente.
	 * La chiave è un numero in formato stringa, 
	 * mentre l'oggetto referenziato è la frase corrispondente
	 * @param fr FileReader del file corrente
	 * @return la mappa con tutte le parole
	 */
	private Map creaMappa(FileReader fr) {
		Map mappa = new TreeMap();
		Scanner sc = new Scanner(fr);
		String line;
		int i;
		String numero;
		String parole;
		while(sc.hasNext()) {
			line = sc.nextLine();
			//se la linea corrente non è vuota si va avanti
			if(line.length()!=0) {
				//tutto ciò che non inizia # e con lo spazio, è un comando
				if(line.charAt(0)!='#' && line.charAt(0)!=' ') {
					i = line.indexOf("=");
					numero = line.substring(0,i); //parte sinistra dell'=
					parole = line.substring(i+1); //parte destra dell'=
					mappa.put(numero, parole);  //inserisco nella mappa
				}
			}
		}
		return mappa;
	}

	/**
	 * Carica il linguaggio scelto, che è descritto nel file CONF.
	 * Alla fine imposterà la variabile linguaggioCorrente
	 */
	public void caricaLinguaggioCorrente() {
		try {
			File fileConf = new File("CONF");
			//se il file non esiste (è stato cancellato x errore) lo crea di nuovo con default lingua inglese
			if(!fileConf.exists()) {
				this.linguaggioCorrente = new Linguaggio("English", new File("language/ENG.lang"));
				FileOutputStream discoFileStream = new FileOutputStream(fileConf);
				PrintStream disco = new PrintStream(discoFileStream); //Oggetto che punta al File
				disco.println("language=English");
				discoFileStream.close();
			}
			//legge il linguaggio fissato tramite il file CONF
			FileReader fr = new FileReader(fileConf);
			Scanner sc = new Scanner(fr);
			String line;
			while(sc.hasNext()) {
				line = sc.nextLine();
				if(line.contains("language")) {
					String l = line.substring(9);
					//carico il linguaggio come corrente
					this.caricaLinguaggio(l);
				}
			}
			fr.close();
		} catch(FileNotFoundException e) {System.exit(1);}
		  catch (IOException e1) {e1.printStackTrace();	}
	}
	/**
	 * Data la stringa rappresentante il nome del linguaggio,
	 * carica il linguaggio corrente.
	 */
	public void caricaLinguaggio(String l) {
		Iterator it = listaLinguaggi.iterator();
		Linguaggio lc = new Linguaggio();
		while(it.hasNext()) {
			lc = (Linguaggio)it.next();
			//se trovo nella lista il nome del linguaggio, imposto il linguaggio corrente
			if(l.equals(lc.getNome()))
				this.linguaggioCorrente = lc;
			else  //altrimenti imposto come dafault inglese
				this.linguaggioCorrente = new Linguaggio("English", new File("language/ENG.lang"));
		}
	}
	
	/**
	 * Crea e restituisce la lista con tutti i linguaggi disponibili
	 */
	private List creaListaLinguaggi() {
		File dir = new File("language");
		List listaL = new LinkedList();
		if (dir.isDirectory()) {
			File[] lista = dir.listFiles();
			int i;
			FileReader fr;
			String primaLinea;
			Scanner sc;
			Linguaggio lingCorrente;
			for(i=0; i<lista.length; i++) {
				try {
					fr = new FileReader(lista[i]);
					sc = new Scanner(fr);
					//nella prima riga di ogni file è specificato il nome del linguaggio
					primaLinea = sc.nextLine().substring(1); //tolgo il primo carattere (#)
					lingCorrente = new Linguaggio(primaLinea, lista[i]);
					listaL.add(lingCorrente); //aggiungo il linguaggio alla lista
				
				} catch (FileNotFoundException e) { e.printStackTrace(); }
			}
		}
		return listaL;
	}
		
	/**
	 * Restituisce la mappa con tutte le frasi del linguaggio corrente
	 */
	public Map getMappa() {
		return this.mappaParole;
	}
	
	/**
	 * Restituisce la frase dato il numero (in formato stringa) corrispondente
	 */
	public String getText(String numero) {
		String s = (String)this.mappaParole.get(numero);
		if(s==null)
			return "";
		else
			return s;
	}

	/**
	 * Restituisce il linguaggio corrente
	 */
	public Linguaggio getLinguaggioCorrente() {
		return this.linguaggioCorrente;
	}
	
	/**
	 * Restituisce la lista con tutti i linguaggi disponibili
	 */
	public List getListaLinguaggi() {
		return this.listaLinguaggi;
	}
	
	
//	public static void main(String[] args) {
//		Language l = new Language();
//		Map m = new TreeMap();
//		m = l.getMappa();
//		Map m = new TreeMap();
//		String line = "23=ciaociaociao";
//		int i = line.indexOf("=");
//		String numero = line.substring(0,i); //parte sinistra dell'=
//		String parole = line.substring(i+1); //parte destra dell'=
//		m.put(numero, parole);  //inserisco nella mappa
//		Set s = m.keySet();
//		Iterator it = s.iterator();
//		while(it.hasNext()) {
//			String ss = (String)it.next();
//			System.out.println(ss+" - "+m.get(ss));
//		}
//		System.out.println(l.getText("85"));
//	}
	
}
