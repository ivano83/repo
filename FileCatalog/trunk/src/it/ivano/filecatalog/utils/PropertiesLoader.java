package it.ivano.filecatalog.utils;

import it.ivano.filecatalog.exception.PropertiesException;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesLoader {
	
	private static Properties props;
	
	private static Properties getInstance() throws IOException {
		
		if(props == null) {
			InputStream in = PropertiesLoader.class.getClassLoader().getResourceAsStream("file_catalog.properties");
			props = new Properties();
			props.load(in);
		}
		
		return props;
	}
	
	public static String getValue(String name) throws PropertiesException {
		try {
			return getInstance().getProperty(name);
		} catch (IOException e) {
			throw new PropertiesException("Errore inizializzazione delle proprieta'", e);
		}
	}

}
