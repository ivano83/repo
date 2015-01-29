package it.ivano.catalog.backend.dao;

import it.ivano.catalog.backend.dto.MimeType;
import it.ivano.filecatalog.exception.FileDataException;

import java.util.List;

import javax.persistence.EntityManager;

public interface MimeTypeDaoInt {
	
	public List<MimeType> getAllMimeType() throws FileDataException;
	
	public EntityManager getEntityManager();
	
	public void setEntityManager(EntityManager entityManager);

}
