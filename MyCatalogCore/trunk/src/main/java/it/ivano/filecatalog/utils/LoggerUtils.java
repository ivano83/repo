package it.ivano.filecatalog.utils;

import it.ivano.filecatalog.exception.FileDataException;
import it.ivano.utility.logging.MyLogger;

public class LoggerUtils {

	
	public static FileDataException logAndLaunchException(MyLogger logger, String msg, Throwable e, boolean logException) {
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
