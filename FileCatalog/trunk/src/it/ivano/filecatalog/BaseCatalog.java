package it.ivano.filecatalog;

import it.ivano.utility.logging.MyLogger;

public class BaseCatalog {
	
	protected MyLogger logger;
	
	public BaseCatalog(Class<?> clazz) {
		logger = new MyLogger(clazz);
	}

}
