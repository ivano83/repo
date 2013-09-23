package it.fivano.symusic.backend;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class JpaConnectionFactory {
	
	private static final String PERSISTENCE_UNIT_NAME = "Symusic";
    private static EntityManagerFactory factory;
    private static EntityManager em;
    
    
    public static EntityManager getConnection() {
    	
    	if(factory==null)
    		factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
    	
    	if(em==null || !em.isOpen())
    		em = factory.createEntityManager();

        return em;
        
    }

}
