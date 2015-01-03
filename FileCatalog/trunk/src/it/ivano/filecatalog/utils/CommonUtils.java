package it.ivano.filecatalog.utils;

import it.ivano.filecatalog.BaseCatalog;
import it.ivano.filecatalog.model.FileInterface;
import it.ivano.filecatalog.model.Mp3FileModel;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;

import org.apache.tika.config.TikaConfig;
import org.apache.tika.exception.TikaException;
import org.apache.tika.io.TikaInputStream;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.mime.MediaType;

public class CommonUtils extends BaseCatalog {
	
	private static TikaConfig tika;
	
	static {
		try {
			tika = new TikaConfig();
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
	
	public static String getEstensioneFile(String fileName) {
		String[] fileNameSplit = fileName.split("\\.");
		if(fileNameSplit.length>1) {
			return fileNameSplit[fileNameSplit.length-1];
		}
		return null;
	}
	
	public static String detectMimeType(File file) throws FileNotFoundException, IOException {
		Metadata metadata = new Metadata();
		metadata.set(Metadata.RESOURCE_NAME_KEY, file.getName());
		MediaType mimetype = tika.getDetector().detect(TikaInputStream.get(file), metadata);
		return (mimetype!=null)? mimetype.getBaseType().toString() : null;
	}

}
