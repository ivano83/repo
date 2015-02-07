package it.ivano.catalog.controller;

import it.ivano.filecatalog.exception.FileDataException;
import it.ivano.utility.logging.MyLogger;

import javax.persistence.EntityManager;

public class BaseController {
	
	protected MyLogger logger;
	
	public BaseController(Class<?> clazz) {
		logger = new MyLogger(clazz);
	}
	
	public FileDataException logAndLaunchException(String msg, Throwable e, boolean logException) {
		if(logException && e!=null)
			logger.error(msg,e);
		else
			logger.error(msg);
		
		if(e!=null)
			return new FileDataException(msg,e);
		else
			return new FileDataException(msg);
	}

}
