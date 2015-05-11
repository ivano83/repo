package it.ivano.utility.app;

import it.ivano.utility.file.FileUtility;

import java.io.File;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SpostaFileMp3eNfo {

	
	/**
	 * Metodo per scrivere un file all'interno della cartella della release
	 * con nome uguale alla data di uscita del disco.
	 * Viene preso dal file .nfo se presente
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		
//		String folderInput = "C:\\Users\\ivano\\Desktop\\JDOWNLOAD\\DOWNL\\2004";
//		String folderFileNfo = "C:\\Users\\ivano\\Documents\\IVANO\\ARCHIVIO_IVANO\\info mp3";
//		String folderFileMp3 = "C:\\Users\\ivano\\Desktop\\JDOWNLOAD\\DOWNL\\2004";
		
		String folderInput = "C:\\Users\\ivano\\ROOT\\IVANO\\ARCHIVIO_IVANO\\Mp3\\TMP marzo\\Dance & House";
		String folderFileNfo = "C:\\Users\\ivano\\ROOT\\IVANO\\ARCHIVIO_IVANO\\info mp3";
		String folderFileMp3 = "C:\\Users\\ivano\\ROOT\\IVANO\\ARCHIVIO_IVANO\\Mp3\\TMP marzo\\Dance & House";
		
		// Trance
		// Dance & House
		
		List<File> l = FileUtility.caricaFile(folderInput);
		
//		ricercaDataRelease(l, folderFileMp3);
		
		spostaMp3(l, folderFileMp3);
		spostaNfo(l, folderFileNfo);

//		String path = "C:\\Users\\ivano\\Desktop\\JDOWNLOAD\\DOWNL\\2007";
		
//		String path = "C:\\Users\\ivano\\Desktop\\JDOWNLOAD\\DOWNL\\2004\\2004-01,02,03";
//		String path = "C:\\Users\\ivano\\Desktop\\JDOWNLOAD\\DOWNL\\2004\\2004-04,05,06";
//		String path = "C:\\Users\\ivano\\Desktop\\JDOWNLOAD\\DOWNL\\2004\\2004-07,08,09";
//		String path = "C:\\Users\\ivano\\Desktop\\JDOWNLOAD\\DOWNL\\2004\\2004-10,11,12";
//		impostaIdTagMp3(path);
			
	}


	
	private static void spostaMp3(List<File> l, String folderFileMp3) {
		
		for(File x : l) {
			if(x.getName().endsWith(".mp3")) {
				String pathOutput = folderFileMp3+"\\"+x.getName();
				
				x.renameTo(new File(pathOutput));
				System.out.println("Spostamento in - "+pathOutput);
			}
		}
			
	}
	
	private static void spostaNfo(List<File> l, String folderFileNfo) {
		
		for(File x : l) {
			if(x.getName().endsWith(".nfo")) {
				String pathOutput = folderFileNfo+"\\"+x.getName();
				
				x.renameTo(new File(pathOutput));
				System.out.println("Spostamento in - "+pathOutput);
			}
		}
			
	}


	private static void ricercaDataRelease(List<File> l, String path) throws Exception {
		
		int delay = 5;
		for(File x : l) {
			String pathPlus = path+"\\"+x.getParentFile().getName().replace("_", " ");
//			System.out.println(pathPlus);
//			if(x.isDirectory()) {
			if(x.getName().endsWith("nfo")) {
//				System.out.println(x.getName());
			
				
				Set<String> gg = FileUtility.estraiRigheDaFile(x);
				for(String testo : gg) {
					
					Integer i = null;
//					Pattern p = Pattern.compile("a*b");
//					Matcher m = p.matcher("aaaaab");
//					boolean b = m.matches();
					boolean trovato = false;
					Matcher m = Pattern.compile("(.*)(\\d\\d[/]\\d\\d\\d\\d)(.*)").matcher(testo);
					if(!trovato && m.matches()) {
						i = m.start(2);
						trovato = true;
					}
					m = Pattern.compile("(.*)(\\d\\d\\\\d\\d\\d\\d)(.*)").matcher(testo);
					if(!trovato && m.matches()) {
						i = m.start(2);
						trovato = true;
					}
					m = Pattern.compile("(.*)(\\d\\d-\\d\\d\\d\\d)(.*)").matcher(testo);
					if(!trovato && m.matches()) {
						i = m.start(2);
						trovato = true;
					}
					m = Pattern.compile("(.*)(\\d\\d\\.\\d\\d\\d\\d)(.*)").matcher(testo);
					if(!trovato && m.matches()) {
						i = m.start(2);
						trovato = true;
					}
//					if(Pattern.compile(".*\\d\\d\\\\d\\d\\d\\d.*").matcher(testo).matches()) {
////					else if(testo.contains("\2003 ")) {
//						i = Pattern.compile("\\d\\d\\\\d\\d\\d\\d").matcher(testo).start();
//					}
//					if(Pattern.compile(".*\\d\\d-\\d\\d\\d\\d.*").matcher(testo).matches()) {
////					else if(testo.contains("-2003 ")) {
//						i = Pattern.compile("\\d\\d-\\d\\d\\d\\d").matcher(testo).start();
//					}
//					if(Pattern.compile(".*\\d\\d\\.\\d\\d\\d\\d.*").matcher(testo).matches()) {
////						else if(testo.contains("-2003 ")) {
//						i = Pattern.compile("\\d\\d\\.\\d\\d\\d\\d").matcher(testo).start();
//					}
					
					if(i!=null) {
						
//						System.out.println(testo.substring((i-delay>0)?i-delay:0,i+7));
						String data = testo.substring((i-delay>0)?i-delay:0,i+7);
						File newf = new File(pathPlus+" - "+data.replace(":", "").replace("\\", "-").replace("/", "-")+".txt");
						newf.createNewFile();
						break;
					}
					
				}
//				System.out.println(x.getParent());
				
			}

			
		}
		
	}
	
	
	
}
