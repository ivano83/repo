package it.fivano.symusic.backend;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

public class MyBatisSimpleClient {
	
	public enum EnvironmentType {
		TESTING("testing");
		
		private String env;
		private EnvironmentType(String env) {
			this.env = env;
		}
		public String getEnv() {
			return env;
		}
	}
	
	private String env;
	
	public MyBatisSimpleClient() {
		
	}
	
	public MyBatisSimpleClient(EnvironmentType envType) {
		this.env = envType.getEnv();
	}

	
	public SqlSession apriSessione() throws Exception {
		
		SqlSession session = null;
		try {
			SqlSessionFactory factory = SqlSessionFactorySingleton.getInstance(env).getSqlSessionFactory();
			session = factory.openSession();
			
			return session;
			
		} catch (Exception e) {
			if (session != null)
				session.close();
			throw e;
		}
	}
	
	public void chiudiSessione(SqlSession session) {
		
		if (session != null)
			session.close();
	}
	
	public static void main(String[] args) throws Exception {
		
		MyBatisSimpleClient client = new MyBatisSimpleClient(EnvironmentType.TESTING);
		SqlSession session = client.apriSessione();
		
//		TipoAnimaleExample an = new TipoAnimaleExample();
//		TipoAnimaleMapper mapper = session.getMapper(TipoAnimaleMapper.class);
//		List<TipoAnimale> result = mapper.selectByExample(an);
//		
//		for(TipoAnimale t : result) {
//			System.out.println(t.getNome());
//		}
//
//		RazzeViewExample vr = new RazzeViewExample();
//		vr.createCriteria().andIdCategoriaAnimaleEqualTo(1L);
//		
//		RazzeViewMapper mapper2 = session.getMapper(RazzeViewMapper.class);
//		List<RazzeView> result2 = mapper2.selectByExample(vr);
//		
//		for(RazzeView t : result2) {
//			System.out.println(t.getNomeRazza());
//		}

//		@SuppressWarnings("unchecked")
//		Class<GenericExample> theClass  = (Class<GenericExample>) Class.forName(RazzeViewExample.class.getName());
//		GenericExample ex = theClass.newInstance();
		
		
		session.close();
		
	}
	
}
