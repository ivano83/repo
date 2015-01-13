package it.ivano.filecatalog.utils;

import it.ivano.filecatalog.BaseCatalog;
import it.ivano.filecatalog.exception.FileDataException;
import it.ivano.filecatalog.model.MimeTypeModel;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;

import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;
import org.apache.tika.io.TikaInputStream;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.mime.MediaType;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.parser.jpeg.JpegParser;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class CommonUtils extends BaseCatalog {
	
	private static Tika tika;
	
	static {
		try {
			tika = new Tika();
		} catch (Exception e) {	} 
	}
	
	public CommonUtils() {
		super(CommonUtils.class);
	}

	public static void estraiMetadatiStandard(File file, Object input) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, SecurityException, NoSuchMethodException {
		Method metodo = input.getClass().getMethod("setNome", String.class);
		metodo.invoke(input, file.getName());
		metodo = input.getClass().getMethod("setEstensione", String.class);
		metodo.invoke(input, getEstensioneFile(file.getName()));
		metodo = input.getClass().getMethod("setDataModifica", Date.class);
		metodo.invoke(input, new Date(file.lastModified()));
		metodo = input.getClass().getMethod("setDimensione", String.class);
		metodo.invoke(input, file.length()+"");
	}
	
	public void estractMetadata(File file, Object input, String mimeTypeArea) throws FileDataException {
		
		InputStream is = null;
		try {
			Metadata metadata = new Metadata();
			is = TikaInputStream.get(file);
			Reader r = tika.parse(is, metadata);
			
			if(mimeTypeArea.equalsIgnoreCase("Immagine")) {
				
			}
			
		} catch (Exception e) {
			throw logAndLaunchException("Errore nell'estrazione dei metadati", e, false);
		} finally {
			if(is!=null)
				try {
					is.close();
				} catch (IOException e) { }
		}
		
	}
	
	public static String getEstensioneFile(String fileName) {
		String[] fileNameSplit = fileName.split("\\.");
		if(fileNameSplit.length>1) {
			return fileNameSplit[fileNameSplit.length-1];
		}
		return null;
	}
	
	public String detectMimeType(File file) throws FileDataException {
		InputStream is = null;
		try {
			Metadata metadata = new Metadata();
			metadata.set(Metadata.RESOURCE_NAME_KEY, file.getName());
			is = TikaInputStream.get(file);
			MediaType mimetype = tika.getDetector().detect(is, metadata);
			return (mimetype!=null)? mimetype.getBaseType().toString() : null;
		} catch (Exception e) {
			throw logAndLaunchException("Errore nel detect del mime type", e, false);
		} finally {
			if(is!=null)
				try {
					is.close();
				} catch (IOException e) { }
		}
	}
	
	public void extractImageMetadata(File file) throws FileNotFoundException, IOException, SAXException, TikaException {
		
		Metadata metadata = new Metadata(); 
		ContentHandler handler = new DefaultHandler(); 
		Parser parser = new JpegParser(); 
		ParseContext context = new ParseContext(); 
		String mimeType = tika.detect(file); 
		metadata.set(Metadata.CONTENT_TYPE, mimeType); 
		parser.parse(TikaInputStream.get(file),handler,metadata,context); 
		
		
		
		metadata = new Metadata(); 
		Reader r = tika.parse(TikaInputStream.get(file), metadata);
		for (String key : metadata.names()) {
			String[] values = metadata.getValues(key);
			for (String val : values) {
				System.out.println(key+" = "+val);
			}
			
		}
		
//		System.out.println(metadata.get(Metadata.RESOLUTION_VERTICAL));
//		System.out.println(metadata.get(Metadata.CONTENT_ENCODING));
//		System.out.println(metadata.get(Metadata.CONTENT_LENGTH));
//		System.out.println(metadata.get(Metadata.IMAGE_LENGTH));
//		System.out.println(metadata);
	}
	
	public static void main(String[] args) throws Exception {
		CommonUtils u = new CommonUtils();
		System.out.println(u.detectMimeType(new File("C:\\Users\\ivano\\Downloads\\bolletta.pdf")));
		
		u.extractImageMetadata(new File("C:\\Users\\ivano\\Downloads\\nocciola_390.jpg"));
		
	}

}
