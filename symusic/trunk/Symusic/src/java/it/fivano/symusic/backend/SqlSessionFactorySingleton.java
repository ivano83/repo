package it.fivano.symusic.backend;

import it.fivano.symusic.exception.BackEndException;

import java.io.IOException;
import java.io.Reader;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

public class SqlSessionFactorySingleton {
	private volatile static SqlSessionFactorySingleton singleton;
	private static final String resource = "SqlMapper.xml";
	private SqlSessionFactory ssf;
	private static String env;

	/**
	 * Costruttore privato per istanziare le SqlSessionFactory di interesse
	 */
	private SqlSessionFactorySingleton() throws Exception {
		Reader reader;
		try {
			reader = Resources.getResourceAsReader(resource);
			if(env==null)
				ssf = new SqlSessionFactoryBuilder().build(reader);
			else
				ssf = new SqlSessionFactoryBuilder().build(reader, env.trim());
		} catch (IOException e) {
			throw new BackEndException("Errore nell'inizializzazione della sessione myBatis.",e);
		}
		return;
	}

	public static SqlSessionFactorySingleton getInstance() throws Exception {
		if (singleton == null) {
			synchronized (SqlSessionFactorySingleton.class) {
				if (singleton == null) {
					singleton = new SqlSessionFactorySingleton();
				}
			}
		}
		return singleton;
	}
	
	public static SqlSessionFactorySingleton getInstance(String envDataSource) throws Exception {
		env = envDataSource;
		return getInstance();
	}

	public SqlSessionFactory getSqlSessionFactory() {
		return ssf;
	}
}
