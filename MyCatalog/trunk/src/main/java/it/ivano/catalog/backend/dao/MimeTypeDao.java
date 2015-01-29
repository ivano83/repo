package it.ivano.catalog.backend.dao;

import it.ivano.catalog.backend.dto.ConfigUtente;
import it.ivano.catalog.backend.dto.MimeType;
import it.ivano.filecatalog.exception.FileDataException;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;

@Repository
public class MimeTypeDao extends BaseDao implements MimeTypeDaoInt {
	
	private static final String CONFIG_MIME_TYPE = "MT";
	
	
	private EntityManager entityManager;
	
	public EntityManager getEntityManager() {
        return entityManager;
    }

	@PersistenceContext(unitName="MyCatalog")
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
	
    
	public MimeTypeDao() {
		super(MimeTypeDao.class);
	}
	
	public List<MimeType> getAllMimeType() throws FileDataException {
//		EntityManager em = null;
		try {
//			em = em().createEntityManager();
			Query q = entityManager.createQuery("SELECT m FROM MimeType m");
			List<MimeType> res = q.getResultList();
			
			return res;
		} catch (Exception e) {
			throw logAndLaunchException("Errore nel recupero della lista dei mimeType", e, false);
		} finally {
//			emClose(entityManager);
		}
	}
	
	public List<MimeType> getListaMimeTypeByArea(String area) throws FileDataException {
//		EntityManager em = null;
		try {
//			em = em().createEntityManager();
			Query q = entityManager.createQuery("SELECT m FROM MimeType m where m.area = :area");
			q.setParameter("area", area);
			List<MimeType> res = q.getResultList();
			
			return res;
		} catch (Exception e) {
			throw logAndLaunchException("Errore nel recupero del mimeType cercato", e, false);
		} finally {
//			emClose(entityManager);
		}
	}
	
	public MimeType getMimeTypeByName(String name) throws FileDataException {
//		EntityManager em = null;
		try {
//			em = em().createEntityManager();
			Query q = entityManager.createQuery("SELECT m FROM MimeType m where m.mimeType = :name");
			q.setParameter("name", name);
			MimeType res = (MimeType)q.getSingleResult();
			
			return res;
		} catch (Exception e) {
			throw logAndLaunchException("Errore nel recupero del mimeType cercato", e, false);
		} finally {
//			emClose(entityManager);
		}
	}
	
	public MimeType getMimeTypeByKey(Long key) throws FileDataException {
//		EntityManager em = null;
		try {
//			em = em().createEntityManager();
			MimeType m = entityManager.find(MimeType.class, key);

			return m;
		} catch (Exception e) {
			throw logAndLaunchException("Errore nel recupero del mimeType cercato", e, false);
		} finally {
//			emClose(entityManager);
		}
	}
	
	public List<MimeType> getListaMimeTypeByUtente(Long utente) throws FileDataException {
		
//		EntityManager em = null;
		try {
//			em = em().createEntityManager();
			Query q = entityManager.createQuery("SELECT c FROM ConfigUtente c where c.configType = "+CONFIG_MIME_TYPE+" and c.idUtente = :utente");
			q.setParameter("utente", utente);
			List<ConfigUtente> resTmp = q.getResultList();
			
			List<Long> listaMimeKey = new ArrayList<Long>();
			for(ConfigUtente cu : resTmp) {
				listaMimeKey.add(cu.getPk().getIdConfig());
			}
			
			q = entityManager.createQuery("SELECT m FROM MimeType m where m.idMimeType IN :mime");
			q.setParameter("mime", listaMimeKey);
			
			List<MimeType> res = q.getResultList();
			
			return res;
		} catch (Exception e) {
			throw logAndLaunchException("Errore nel recupero del mimeType cercato", e, false);
		} finally {
//			emClose(entityManager);
		}
		
	}

    public static void main(String[] args) throws FileDataException {
        
         MimeTypeDao dao = new MimeTypeDao();
         List<MimeType> res = dao.getAllMimeType();
         
//         for (MimeType user : res) {
//              System.out.println(user.getMimeType());
//         }
         System.out.println("Size: " + res.size());

         List<MimeType> m = dao.getListaMimeTypeByArea("Video");
         for (MimeType user : m) {
        	 System.out.println(user.getMimeType());
         }

         MimeType mm = dao.getMimeTypeByName("video/x-matroska");
         System.out.println("-- "+mm.getMimeType());

    }
}
