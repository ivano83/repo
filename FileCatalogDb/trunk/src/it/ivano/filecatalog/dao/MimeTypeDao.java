package it.ivano.filecatalog.dao;

import it.ivano.filecatalog.dto.File;
import it.ivano.filecatalog.dto.MimeType;
import it.ivano.filecatalog.exception.FileDataException;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

public class MimeTypeDao extends BaseDao {

	
	
	
	public MimeTypeDao() {
		super(MimeTypeDao.class);
	}
	
	public List<MimeType> getAllMimeType() throws FileDataException {
		EntityManager em = null;
		try {
			em = em().createEntityManager();
			Query q = em.createQuery("SELECT m FROM MimeType m");
			List<MimeType> res = q.getResultList();
			
			return res;
		} catch (Exception e) {
			throw logAndLaunchException("Errore nel recupero della lista dei mimeType", e, false);
		} finally {
			em.close();
		}
	}
	
	public List<MimeType> getMimeTypeByArea(String area) throws FileDataException {
		EntityManager em = null;
		try {
			em = em().createEntityManager();
			Query q = em.createQuery("SELECT m FROM MimeType m where m.area = :area");
			q.setParameter("area", area);
			List<MimeType> res = q.getResultList();
			
			return res;
		} catch (Exception e) {
			throw logAndLaunchException("Errore nel recupero del mimeType cercato", e, false);
		} finally {
			em.close();
		}
	}
	
	public MimeType getMimeTypeByKey(Long key) throws FileDataException {
		EntityManager em = null;
		try {
			em = em().createEntityManager();
			MimeType m = em.find(MimeType.class, key);

			return m;
		} catch (Exception e) {
			throw logAndLaunchException("Errore nel recupero del mimeType cercato", e, false);
		} finally {
			em.close();
		}
	}

    public static void main(String[] args) throws FileDataException {
        
         MimeTypeDao dao = new MimeTypeDao();
         List<MimeType> res = dao.getAllMimeType();
         
//         for (MimeType user : res) {
//              System.out.println(user.getMimeType());
//         }
         System.out.println("Size: " + res.size());

         List<MimeType> m = dao.getMimeTypeByArea("Video");
         for (MimeType user : m) {
        	 System.out.println(user.getMimeType());
         }

         MimeType mm = dao.getMimeTypeByKey(5L);
         System.out.println("-- "+mm.getMimeType());

    }
}
