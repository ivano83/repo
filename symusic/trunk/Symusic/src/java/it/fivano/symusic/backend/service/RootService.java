package it.fivano.symusic.backend.service;

import it.fivano.symusic.MyLogger;
import it.fivano.symusic.SymusicUtility.EnvironmentType;
import it.fivano.symusic.backend.SqlSessionFactorySingleton;
import it.fivano.symusic.exception.BackEndException;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

public class RootService {
	
	private SqlSession session;
	private EnvironmentType env;
	protected MyLogger log;
	

	protected void setLogger(Class<?> classe) {
		log = new MyLogger(classe);
	}
	
	public SqlSession apriSessione() throws BackEndException {
		
		try {
			
			// null prende il default, ovvero da jndi
			String envName = (env == null) ? null : env.getEnv();
			SqlSessionFactory factory = SqlSessionFactorySingleton.getInstance(envName).getSqlSessionFactory();
			session = factory.openSession();
			
			return session;
			
		} catch (Exception e) {
			if (session != null)
				session.close();
			throw new BackEndException("Errore nell'inizializzazione della sessione MyBatis",e);
		}
	}
	
	public void chiudiSessione() {
		
		if (session != null)
			session.close();
	}

	public void setEnv(EnvironmentType env) {
		this.env = env;
	}
	
	
	

}
