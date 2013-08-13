package it.ivano.utility.file;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;

public class FileUtility {

	
	public static List<File> getFilesByPattern(List<File> lista, String pattern) throws Exception {
		
		List<File> result = new ArrayList<File>();
		
		String nomeFile = null;
		char[] patt = pattern.toCharArray();
		for(File f : lista) {
			
			nomeFile = f.getName();
			int lunghezzaFile = nomeFile.length();
			int lunghezzaCorrente = 0;
			int i = 0;
			boolean trovato = false;
			while(i<patt.length && lunghezzaCorrente<=lunghezzaFile) {
				String[] tmp = null;
				if(patt[i]=='[' || patt[i]==']')
					tmp = nomeFile.split("\\"+patt[i]);
				else
					tmp = nomeFile.split(""+patt[i]);
				if(tmp.length>1) { // vuol dire che esiste perchè ha splittato la stringa
					nomeFile = nomeFile.substring(tmp[0].length()+1, nomeFile.length());
					lunghezzaCorrente += tmp[0].length()+1;
					if(i==patt.length-1) //siamo alla fine della stringa
						trovato = true;
				}
				
				i++;
			}
			
			if(trovato) { // è stato trovato il pattern
				result.add(f);
			}
			
		}
		
		return result;
	}
	
	
	public static List<File> caricaFile(String url) throws Exception {

		List<File> result = new ArrayList<File>();
		if(url==null)
			return result;
		try {

			// SE IL FILE NON ESISTE, VIENE CREATO
//			if(!new File(path.replaceAll("file:/", "")).exists()) {
//				this.stampaSuFile(dir, file, "");
//			} 
			
			File f = new File(url);
			if(f.isDirectory()) {
				
//				File[] tmp = f.listFiles(new AudioFileFilter());
				File[] tmp = f.listFiles();
				for(File x : tmp) {
					result.addAll(caricaFile(x.getPath()));
				}
				
			} else {
				result.add(f);
				return result;
			}
			
			return result;

		} catch (Exception e) {
			throw e;
		}

	}
	
	public static List<File> caricaFile(String url, FileFilter filtro) throws Exception {

		List<File> result = new ArrayList<File>();
		if(url==null)
			return result;
		try {

			File f = new File(url);
			if(f.isDirectory()) {
				
				File[] tmp = (filtro==null)? f.listFiles() : f.listFiles(filtro);
				for(File x : tmp) {
					result.addAll(caricaFile(x.getPath()));
				}
				
			} else {
				result.add(f);
				return result;
			}
			
			return result;

		} catch (Exception e) {
			throw e;
		}

	}

	public static void main(String[] args) throws Exception {
		
		
		List<File> ff = FileUtility.caricaFile("C:/Users/ivano/Documents/IVANO/xx");
//		File fi = new File("C:/Users/ivano/Documents/IVANO/xx/ciaooo.txt");
//		ff.get(0).renameTo(fi);
		
		ff = FileUtility.getFilesByPattern(ff, "]");
		System.out.println(ff.size());
		
	}
	
	public static Set<String> estraiRigheDaFile(File f) throws Exception {

		Set<String> result = new TreeSet<String>();
		
		if(f==null)
			return result;
		
		FileReader fileAperto = null;
		try {

			fileAperto = new FileReader(f);
			Scanner sc = new Scanner(fileAperto);

			String line;
			while(sc.hasNextLine()) {
				line = sc.nextLine(); // CARICO LA PROSSIMA RIGA DEL FILE
				//AGGIUNGO ALLA LISTA TUTTE LE RIGHE NON VUOTE
				if(line.length()!=0)
					result.add(line);
			}
			fileAperto.close();

			return result;

		} catch (Exception e) {
			throw e;
		}
		finally {
			if(fileAperto!=null)
				fileAperto.close();
		}

	}
	public static String estraiTestoDaFile(File f) throws Exception {

		
		if(f==null)
			return null;
		
		StringBuffer sb = new StringBuffer();
		FileReader fileAperto = null;
		try {

			fileAperto = new FileReader(f);
			Scanner sc = new Scanner(fileAperto);

			String line;
			while(sc.hasNextLine()) {
				line = sc.nextLine(); // CARICO LA PROSSIMA RIGA DEL FILE
				//AGGIUNGO ALLA LISTA TUTTE LE RIGHE NON VUOTE
				if(line.length()!=0)
					sb.append(line+"\n");
			}
			fileAperto.close();

			return sb.toString();

		} catch (Exception e) {
			throw e;
		}
		finally {
			if(fileAperto!=null)
				fileAperto.close();
		}

	}
	
	public Properties caricaFileProperties(String path) throws IOException {
		
		Properties properties = new Properties();

		InputStream i = getClass().getResourceAsStream(path);
		properties.load(i);
		
		return properties;
		
	}
	
//	public static void copiaFile(File inputFile, File outputFile) throws IOException {
//
//	    FileReader in = new FileReader(inputFile);
//	    FileWriter out = new FileWriter(outputFile);
//	    int c;
//
//	    while ((c = in.read()) != -1)
//	      out.write(c);
//
//	    in.close();
//	    out.close();
//	    
//	}
	
	public static void copiaFile(File inputFile, File outputFile) throws IOException {

		FileChannel in = (new FileInputStream(inputFile)).getChannel();
		FileChannel out = (new FileOutputStream(outputFile)).getChannel();

		in.transferTo(0, inputFile.length(), out);
		in.close();
		out.close();
	}
	
	public static void salvaFile(String fileName, String fileContent) throws IOException {
		
		BufferedWriter out = null;
		try {
			FileWriter fstream = new FileWriter(fileName);
			out = new BufferedWriter(fstream);
			out.write(fileContent);
		} finally {
			if(out!=null)
				out.close();
		}
	}
	
}
