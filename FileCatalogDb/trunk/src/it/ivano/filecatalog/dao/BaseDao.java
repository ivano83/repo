package it.ivano.filecatalog.dao;

import it.ivano.filecatalog.exception.FileDataException;
import it.ivano.utility.logging.MyLogger;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class BaseDao {

	protected MyLogger logger;
	private static final String PERSISTENCE_UNIT_NAME = "FileCatalogDb";
	private static EntityManagerFactory factory;
	
	public BaseDao(Class<?> clazz) {
		logger = new MyLogger(clazz);
	}
	
	public EntityManagerFactory em() {
		if(factory==null) {
			factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
		}
		return factory;
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
