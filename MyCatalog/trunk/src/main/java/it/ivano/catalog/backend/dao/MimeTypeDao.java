package it.ivano.catalog.backend.dao;

import it.ivano.catalog.backend.dto.ConfigUtente;
import it.ivano.catalog.backend.dto.ConfigUtenteId;
import it.ivano.catalog.backend.dto.MimeType;
import it.ivano.filecatalog.exception.FileDataException;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.xmlbeans.impl.xb.xmlconfig.impl.ConfigDocumentImpl.ConfigImpl;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class MimeTypeDao extends BaseDao {
	
	private static final String CONFIG_MIME_TYPE = "MT";
	
	public MimeTypeDao() {
		super(MimeTypeDao.class);
	}
	
	@SuppressWarnings("unchecked")
	public List<MimeType> getAllMimeType() throws FileDataException {
//		EntityManager em = null;
		try {
//			em = em().createEntityManager();
			Query q = getEntityManager().createQuery("SELECT m FROM MimeType m");
			List<MimeType> res = q.getResultList();
			
			return res;
		} catch (Exception e) {
			throw logAndLaunchException("Errore nel recupero della lista dei mimeType", e, false);
		} finally {
//			emClose(entityManager);
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<MimeType> getListaMimeTypeByArea(String area) throws FileDataException {
//		EntityManager em = null;
		try {
//			em = em().createEntityManager();
			Query q = getEntityManager().createQuery("SELECT m FROM MimeType m where m.area = :area");
			q.setParameter("area", area);
			List<MimeType> res = q.getResultList();
			
			return res;
		} catch (Exception e) {
			throw logAndLaunchException("Errore nel recupero del mimeType cercato", e, false);
		} finally {
//			emClose(entityManager);
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<MimeType> getListaMimeTypeByFreeText(String text) throws FileDataException {
//		EntityManager em = null;
		try {
//			em = em().createEntityManager();
			Query q = getEntityManager().createQuery("SELECT m FROM MimeType m where m.area like :text or m.subArea like :text2 or m.listaEstensioni like :ext");
			q.setParameter("text", "%"+text+"%");
			q.setParameter("text2", "%"+text+"%");
			q.setParameter("ext", "%"+text+"%");
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
			Query q = getEntityManager().createQuery("SELECT m FROM MimeType m where m.mimeType = :name");
			q.setParameter("name", name);
			MimeType res = (MimeType)q.getSingleResult();
			
			return res;
		} catch (Exception e) {
			throw logAndLaunchException("Errore nel recupero del mimeType cercato", e, false);
		} finally {
//			emClose(entityManager);
		}
	}
	
	public List<MimeType> getMimeTypeByExtension(String ext) throws FileDataException {
		try {
			Query q = getEntityManager().createQuery("SELECT m FROM MimeType m where m.listaEstensioni like :ext");
			q.setParameter("ext", "%"+ext+"%");
			List<MimeType> res = q.getResultList();
			
			return res;
		} catch (Exception e) {
			throw logAndLaunchException("Errore nel recupero del mimeType cercato", e, false);
		} finally {
			
		}
	}
	
	public MimeType getMimeTypeByKey(Long key) throws FileDataException {
//		EntityManager em = null;
		try {
//			em = em().createEntityManager();
			MimeType m = getEntityManager().find(MimeType.class, key);

			return m;
		} catch (Exception e) {
			throw logAndLaunchException("Errore nel recupero del mimeType cercato", e, false);
		} finally {
//			emClose(entityManager);
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<MimeType> getListaMimeTypeByUtente(Long utente) throws FileDataException {
		
		try {
			Query q = getEntityManager().createQuery("SELECT c FROM ConfigUtente c where c.configType = "+CONFIG_MIME_TYPE+" and c.idUtente = :utente");
			q.setParameter("utente", utente);
			List<ConfigUtente> resTmp = q.getResultList();
			
			List<Long> listaMimeKey = new ArrayList<Long>();
			for(ConfigUtente cu : resTmp) {
				listaMimeKey.add(cu.getId().getIdConfig());
			}
			
			q = getEntityManager().createQuery("SELECT m FROM MimeType m where m.idMimeType IN :mime");
			q.setParameter("mime", listaMimeKey);
			
			List<MimeType> res = q.getResultList();
			
			return res;
		} catch (Exception e) {
			throw logAndLaunchException("Errore nel recupero del mimeType cercato", e, false);
		} finally {
		}
		
	}
	
	@SuppressWarnings("unchecked")
	public int addMimeTypeForUser(Long utente, List<MimeType> listMimeType) throws FileDataException {
		
		EntityManager em = getEntityManager();
		int count = 0;
		try {
			
//			em.getTransaction().begin();
			
			Query q = em.createQuery("SELECT idConfig FROM ConfigUtente c where c.configType = "+CONFIG_MIME_TYPE+" and c.idUtente = :utente");
			q.setParameter("utente", utente);
			List<Long> resTmp = q.getResultList();
			
			ConfigUtenteId cuid = new ConfigUtenteId();
			cuid.setConfigType(CONFIG_MIME_TYPE);
			cuid.setIdUtente(utente);
			
			ConfigUtente cu = null;
			for(MimeType mt : listMimeType) {
				
				if(resTmp.contains(mt.getIdMimeType()))
					continue;
				
				cu = new ConfigUtente();
				cuid.setIdConfig(mt.getIdMimeType());
				cu.setId(cuid);
				em.persist(cu);
				
				count++;
			}
			
			
//			em.getTransaction().commit();
			
			return count;
		} catch (Exception e) {
//			em.getTransaction().rollback();
			throw logAndLaunchException("Errore nel recupero del mimeType cercato", e, false);
		} finally {
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
