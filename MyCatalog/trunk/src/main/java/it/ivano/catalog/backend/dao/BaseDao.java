package it.ivano.catalog.backend.dao;

import it.ivano.filecatalog.exception.FileDataException;
import it.ivano.utility.logging.MyLogger;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;

public class BaseDao {

	protected MyLogger logger;
//	private static final String PERSISTENCE_UNIT_NAME = "FileCatalogDb";
//	private static EntityManagerFactory factory;
	private EntityManager entityManager;
	
	public BaseDao(Class<?> clazz) {
		logger = new MyLogger(clazz);
	}
	
	public EntityManager getEntityManager() {
        return entityManager;
    }

	@PersistenceContext(unitName="MyCatalog")
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
	
//	public EntityManagerFactory em() {
//		if(factory==null) {
//			factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
//		}
//		return factory;
//	}
	
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
	
	protected void emClose(EntityManager em) {
		if(em!=null)
			em.close();
	}
	
}
