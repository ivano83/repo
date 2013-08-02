package it.ivano.utility.image;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.logging.Logger;

public class FileUtilities {
	
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
	
	private static Logger log = Logger.getLogger(FileUtilities.class.getName());

	public static File modificaNomeFile(File outFile, int progressivo) throws IOException {
		
		String nome = outFile.getName();
		String estensione = nome.substring(nome.lastIndexOf("."),nome.length());
		String nomeFile = nome.substring(0,nome.lastIndexOf("."))+"_"+progressivo;
		File res = new File(outFile.getParent()+"\\"+nomeFile+estensione);
		return res;
	}

	public static String generaNomeFile(String prefix, String suffix, String extension) {
		
		
		String data = sdf.format(new Date(System.currentTimeMillis()));
		int substringIndex = 10;
		if(suffix!=null && suffix.length()!=0) 
			substringIndex = 5;
		
		String random = (new Random().nextInt(Integer.parseInt(data.substring(0,substringIndex))))+"";
		
		if(prefix==null)
			prefix = "";
		else 
			prefix = prefix.replace(" ", "_")
						.substring(0,(prefix.contains("."))? prefix.lastIndexOf(".") : prefix.length());
		if(prefix.length()>10)
			prefix = prefix.substring(0,10);
		
		if(suffix==null)
			suffix = "";
		else 
			suffix = suffix.replace(" ", "_")
						.substring(0,(suffix.contains("."))? suffix.lastIndexOf(".") : suffix.length());
		if(suffix.length()>10)
			suffix = suffix.substring(0,10);
		
		if(extension==null)
			extension = "";
		else if(!extension.startsWith("."))
			extension = "."+extension;
		
		return prefix+data+"-"+random+suffix+extension;
	}
	
	public static void main(String[] args) throws InterruptedException {
		System.out.println(generaNomeFile("", "","jpg"));
		System.out.println(generaNomeFile(null, "-","jpg"));
		System.out.println(generaNomeFile("T", "IMG 232.jpg",".jpg"));
		System.out.println(generaNomeFile(null, "",null));
		System.out.println(generaNomeFile("", null,".jpg"));
		System.out.println(generaNomeFile("", "io e il mio amore",".jpg"));
		log.info("ciaoooo");
	}
	
}
