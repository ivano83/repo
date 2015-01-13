package it.ivano.filecatalog;

import it.ivano.filecatalog.exception.FileDataException;
import it.ivano.utility.logging.MyLogger;

public class BaseCatalog {
	
	protected MyLogger logger;
	
	public BaseCatalog(Class<?> clazz) {
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
