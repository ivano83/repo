package it.ivano.catalog.backend.dao;

import it.ivano.catalog.backend.dto.Utente;
import it.ivano.filecatalog.exception.FileDataException;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

@Repository
public class UtenteDao extends BaseDao {
	
	
	public UtenteDao() {
		super(UtenteDao.class);
	}
	
	
	public Utente getUtente(String mail) throws FileDataException {
		try {
			Query q = getEntityManager().createQuery("SELECT u FROM Utente u where u.email = :mail");
			q.setParameter("mail", mail);
			Utente res = (Utente)q.getSingleResult();
			
			return res;
		} catch (Exception e) {
			throw logAndLaunchException("Errore nel recupero dell'utente cercato", e, false);
		} 
	}
	
	public Utente getUtenteByKey(Long key) throws FileDataException {
		try {
			Utente m = getEntityManager().find(Utente.class, key);

			return m;
		} catch (Exception e) {
			throw logAndLaunchException("Errore nel recupero dell'utente cercato", e, false);
		} 
	}
	
}
