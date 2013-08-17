package it.ivano.FBchat;

import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JTextField;

public class Generator {

	private String textInput;
	private String textOutput;
	private String textYourName;
	private String textOtherName;
	private FileReader fileAperto;
	private ConversationRow cr;
	private String oraCorrente;
	private String nomeCorrente;
	private String giornoCorrente;
	private List<ConversationRow> listaConv;
	private boolean isPrimoGiorno = true;
	
	private Properties prop;
	
	public Generator(String textInput, String textOutput, String textYourName, String textOtherName ) {
		this.textInput=textInput;
		this.textOutput=textOutput;
		this.textYourName=PropertiesUtility.primoCarattereMaiuscolo(textYourName);
		this.textOtherName=PropertiesUtility.primoCarattereMaiuscolo(textOtherName);
		cr = new ConversationRow();
		listaConv = new LinkedList<ConversationRow>();
		this.giornoCorrente = "";
	}

	public boolean generateCode() {
		
		StringBuffer sb = new StringBuffer(); //verranno inserite le lettere buone
		String line; //linea corrente
		
		try {
			fileAperto = new FileReader(textInput);
			Scanner sc = new Scanner(fileAperto);
			boolean isInizializzato = false;
			boolean isNuovoGiorno = false;
			
			ConversationRow cr = null;
			while(sc.hasNextLine()) {
				line = sc.nextLine(); //prossima riga del file
				
				line = line.replace("Segnala · ", "");
				
				// verifica riga data
				if(isRigaData(line)) {
					// SE È LA DATA, CI SARÀ INIZIALIZZATO SOLO IL TESTO
					cr = new ConversationRow();
					cr.setTesto(line);
					listaConv.add(cr);
					cr = new ConversationRow();

				}
				else if(isRigaNome(line)){
					// verifica riga nome
					cr.setNome(this.recuperaNome(line));
					
				}
				else if(isRigaOra(line)) {
					// verifica riga ora
					cr.setOra(line);
				}
				else {
					// verifica riga testo
					if(line.length()>0 && !cr.getNome().equals("") && !cr.getOra().equals("") && !line.contains("--------")) {
						line = this.eliminaSmileDoppi(line);
						line = this.inserisciImmagini(line);
						cr.setTesto(line);
						listaConv.add(cr);
						cr = new ConversationRow(cr.getNome(), cr.getOra(), null);
					}

				
				}
				
				
				/*
				
				if(this.isRigaOra(line)) {
					isInizializzato = true;
				}
				else {
					if(isInizializzato) {
						//char[] riga = line.toCharArray();
						if(line.length()>0) {
							cr = new ConversationRow();
							if(nomeCorrente.equalsIgnoreCase("Io") || nomeCorrente.equalsIgnoreCase("Me") || nomeCorrente.equalsIgnoreCase("Tu")) 
								cr.setNome(textYourName);
							else 
								cr.setNome(nomeCorrente); // PARTE STATICA DA CAMBIARE
							cr.setOra(oraCorrente);
							line = this.eliminaSmileDoppi(line);
							line = this.inserisciImmagini(line);
							cr.setTesto(line);
							listaConv.add(cr);
							
						}
					}
				}
				*/
			}
			
			

			fileAperto.close();
			
			this.creaCodiceHTML();
			
		} catch (Exception e) {}
				
		return true;
	}

	private String recuperaNome(String line) {
		String result = line.replace("[", "").replace("]", "");
		if(result.equalsIgnoreCase("Io") || result.equalsIgnoreCase("Me") || result.equalsIgnoreCase("Tu")) 
			return textYourName;
		else 
			return result;
	}

	private void creaCodiceHTML() {

		String linea;
		try {
			FileOutputStream discoFileStream;
			PrintStream disco; //Oggetto che punta al File

			discoFileStream = new FileOutputStream(textOutput);
			disco = new PrintStream(discoFileStream);
			
			disco.println(this.inserisciCodiceStandard());

			
			String you = prop.getProperty(textYourName.toLowerCase());
			String other = prop.getProperty(textOtherName.toLowerCase());
			if(you==null) you = textYourName;
			if(other==null) other = textOtherName;
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
//			if(giornoCorrente.split(" ").length==3)
//				giornoCorrente = giornoCorrente +" "+ sdf.format(new Date());
//			System.out.println(giornoCorrente);
//			// PARTE STATICA DA CAMBIARE
//			disco.println("\n<div class=\"mplsession\" id=\"Session_2010-04-15T12-36-15\">");
//			disco.println("<h2>Inizio sessione: "+giornoCorrente+" (Conversazione su Facebook)</h2>");
//			disco.println("<ul>\n\t<li class=\"in\">"+you+"</li>");
//			disco.println("\t<li>"+other+"</li>\n</ul>");
//			disco.println("<table cellspacing=\"0\">\n<tbody>");
			
			boolean inizio = true;
			for(ConversationRow c : listaConv) {
				
				// È UNA DATA
				if(c.getOra().equals("") && c.getNome().equals("")) {
					
					String line = c.getTesto();
					if(!inizio) {
						disco.println("</tbody>\n</table>\n</div>\n");
					}
					
					if(line.split(" ").length==3)
						line = line +" "+ sdf.format(new Date());
					
					disco.println("<div class=\"mplsession\" id=\"Session_2010-04-15T12-36-15\">");
					disco.println("<h2>Inizio sessione: "+line+" (Conversazione su Facebook)</h2>");
					disco.println("<ul>\n\t<li class=\"in\">"+you+"</li>");
					disco.println("\t<li>"+other+"</li>\n</ul>");
					disco.println("<table cellspacing=\"0\">\n<tbody>");
					
					inizio = false;
				}
				// È UNA RIGA DI CONVERSAZIONE
				else {
					
					linea = "<tr><th><span class=\"time\">("+ c.getOra() +")</span> <span>"+ c.getNome() +"</span>:</th><td>"+ c.getTesto() +"</td></tr>";
					disco.println(linea);
					
				}
				
				
				
				/*
				
				String line = c.getTesto();
				if(line.contains("lunedì") || line.contains("martedì") ||
				   line.contains("mercoledì") || line.contains("giovedì") ||
				   line.contains("venerdì") || line.contains("sabato") || line.contains("domenica")){
					
					if(line.contains("gennaio") || line.contains("febbraio") ||
					   line.contains("marzo") || line.contains("aprile") ||
					   line.contains("maggio") || line.contains("giugno") || 
					   line.contains("luglio") || line.contains("agosto") ||
					   line.contains("settembre") || line.contains("ottobre") ||
					   line.contains("novembre") || line.contains("dicembre")){
						
						System.out.println(line.split(" ").length);
						if(line.split(" ").length==3)
							line = line +" "+ sdf.format(new Date());
						
						// PARTE STATICA DA CAMBIARE
						disco.println("</tbody>\n</table>\n</div>\n");

						disco.println("<div class=\"mplsession\" id=\"Session_2010-04-15T12-36-15\">");
						disco.println("<h2>Inizio sessione: "+line+" (Conversazione su Facebook)</h2>");
//						disco.println("<ul>\n\t<li class=\"in\">IVANO&nbsp;&nbsp;(*) in the net&nbsp;&nbsp;(*) <span>(ivano83@hotmail.it)</span></li>");
//						disco.println("\t<li>Cristina <span>(crixy@live.it)</span></li>\n</ul>");
						disco.println("<ul>\n\t<li class=\"in\">"+you+"</li>");
						disco.println("\t<li>"+other+"</li>\n</ul>");
						disco.println("<table cellspacing=\"0\">\n<tbody>");
					}
				}
				
				else {
					if(!line.contains("-----")) {
						linea = "<tr><th><span class=\"time\">("+ c.getOra() +")</span> <span>"+ c.getNome() +"</span>:</th><td>"+ c.getTesto() +"</td></tr>";
						disco.println(linea);
					}
				}
				
				*/
			}
			
			
			
			disco.println("</tbody>\n</table>\n</div>\n");
			discoFileStream.close();
			disco.close();
		} catch (Exception e) {}
		
		
	}

	private String inserisciCodiceStandard() {
		StringBuffer sb = new StringBuffer();
		sb.append("<div class=\"mplsession\" id=\"Session_2009-06-08T20-07-14\">\n");
		sb.append("<h2>Inizio sessione: lunedì 8 giugno 2009 (Conversazione su Facebook)</h2>\n");
		sb.append("<ul>\n");
		sb.append("	<li class=\"in\">AAAAAAA<span>(AAAA@hotmail.it)</span></li>\n");
		sb.append("	<li>BBBBBBBB<span>(BBBB@hotmail.it)</span></li>\n");
		sb.append("</ul>\n");
		sb.append("<table cellspacing=\"0\">\n");
		sb.append("<tbody>\n");
		sb.append("\n\n\n");
		sb.append("</tbody>\n</table>\n</div>\n\n\n");
		
		sb.append("<tr><th><span class=\"time\">(--:--)</span> <span></span>:</th><td style=\"color: red;\">[PARTE PERDUTA]</td></tr>\n");
		sb.append("<tr><th><span class=\"time\">(--:--)</span> <span></span>:</th><td></td></tr>\n\n\n\n");
		return sb.toString();
	}

	public List<ConversationRow> getListaConversazioni() {
		return listaConv;
	}

	private boolean isRigaData(String line) {
		
//		if(isPrimoGiorno) {
			if(line.contains("lunedì") || line.contains("martedì") ||
					line.contains("mercoledì") || line.contains("giovedì") ||
					line.contains("venerdì") || line.contains("sabato") || line.contains("domenica")){

				if(line.contains("gennaio") || line.contains("febbraio") ||
						line.contains("marzo") || line.contains("aprile") ||
						line.contains("maggio") || line.contains("giugno") || 
						line.contains("luglio") || line.contains("agosto") ||
						line.contains("settembre") || line.contains("ottobre") ||
						line.contains("novembre") || line.contains("dicembre")){

					this.giornoCorrente = line;
//					isPrimoGiorno = false;
					return true;
				}
			}
//		}

		
		return false;
	}

	private boolean isRigaNome(String line) {
		char[] riga = line.toCharArray();
	
		if(riga.length>0 && riga[0]=='[' && line.contains("]")) {
			nomeCorrente = line.replace("[", "").replace("]", "");
			return true;
		}

		return false;
	}
	
	private boolean isRigaOra(String line) {
		
		Pattern p = Pattern.compile("[0-9]+[0-9]+:+[0-9]+[0-9]");
		Matcher m = p.matcher(line);
		if(m.find()) {
			oraCorrente = m.group();
			return true;
		}

			
		/* OBSOLETO
		 
		Pattern p = Pattern.compile("[0-9]+[0-9]+:+[0-9]+[0-9]");
		Matcher m = p.matcher(line);
		if(m.find()) {
			ora = m.group();
			
			int i=0;
			while(riga[i]!=':') {
				i++;
			}
			i=i+3;
			while(i<riga.length) {  // prendo il nome
				nome = nome + riga[i];
				i++;
			}
			
//			System.out.println(ora);
//			System.out.println(nome);
			oraCorrente = ora;
			nomeCorrente = nome;
			
			return true;
		}
		
		 */

		return false;
	}

	public String eliminaSmileDoppi(String testo) {
		String[] smileDoppi = new String[]{
				":):)",
				":-):-)",
				":(:(",
				":-(:-(",
				"^_^^_^",
				"<3<3",
				";);)",
				";-);-)",
				":*:*",
				":-*:-*",
				":O:O",
				":-O:-O",
				":D:D",
				":-D:-D",
				"o.Oo.O",
				"O.oO.o",
				":P:P",
				":-P:-P",
				"3:)3:)",
				"-_--_-",
				"8-)8-)",
				"8-|8-|",
				":/:/",
				":\\:\\",
				":'(:'(",
		};
		
		for(String s : smileDoppi) {
			String ss = s.substring(0, s.length()/2);
			testo = testo.replace(s, ss);
		}
		return testo;
	}
	
	public String inserisciImmagini(String testo) {
		Map<String,String> smile = new HashMap<String,String>();
		smile.put(":)", "sorriso.png");
		smile.put(":-)", "sorriso.png");
		smile.put(":(", "triste.png");
		smile.put(":-(", "triste.png");
		smile.put(":P", "linguaccia.png");
		smile.put(":-P", "linguaccia.png");
		smile.put("^_^", "felicissimo.png");
		smile.put("<3", "cuore.png");
		smile.put(";)", "occhiolino.png");
		smile.put(";-)", "occhiolino.png");
		smile.put(":*", "bacio.png");
		smile.put(":-*", "bacio.png");
		smile.put(":O", "sorpreso.png");
		smile.put(":-O", "sorpreso.png");
		smile.put(":D", "sorriso_big.png");
		smile.put(":-D", "sorriso_big.png");
		smile.put("o.O", "stupito.png");
		smile.put("O.o", "stupito.png");
		smile.put("O:)", "angelo.png");
		smile.put("3:)", "diavolo.png");
		smile.put(":'(", "pianto.png");
		smile.put(":/", "incerto.png");
		smile.put(":\\", "incerto.png");
		smile.put("-_-", "nerd.png");
		smile.put("8-)", "secchione.png");
		smile.put("8-|", "coatto.png");
		
		Set<String> key = smile.keySet();
		String urlName;
		for(String s : key) {
			urlName = smile.get(s);
			
			if(s.equals(":/") && testo.contains("http://")){
				continue;
			}
			testo = testo.replace(s, "<img src=\"./Images/fb_img/"+urlName+"\" alt=\""+s+"\"/>");
		}
		return testo;
	}

	public Properties getProperties() {
		return prop;
	}

	public void setProperties(Properties prop) {
		this.prop = prop;
	}

	public static void main(String[] args) {
		
		System.out.println(Character.getNumericValue('7'));
//		Pattern p = Pattern.compile("[0-9]+[0-9]+:+[0-9]+[0-9]");
//		Matcher m = p.matcher("15:55Maria55:14");				
//		
//		while (m.find()) {
//			//System.out.println(m.find());
//			System.out.println(m.group());
//			
//		}
		Generator g = new Generator(null, null, null, null);
		g.isRigaOra("17:30Cristina");
		
		System.out.println(";-);-)".substring(0, ";-);-)".length()/2));
		System.out.println("GFERGRE G;);)EG ER GE gfre ;) geg e grg et;);)h thth r;):)hrt hr h".replace(";);)", ";)"));
	}
	
	
	
	
}
