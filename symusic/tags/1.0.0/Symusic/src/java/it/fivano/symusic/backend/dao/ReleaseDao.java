//package it.fivano.symusic.backend.dao;
//
//import java.util.List;
//
//import javax.persistence.EntityManager;
//import javax.persistence.TypedQuery;
//import javax.persistence.criteria.CriteriaBuilder;
//import javax.persistence.criteria.CriteriaQuery;
//import javax.persistence.criteria.Root;
//
//import it.fivano.symusic.backend.JpaConnectionFactory;
//import it.fivano.symusic.backend.model.Release;
//
//public class ReleaseDao {
//		
//	
//	public Release getRelease(String name) {
//		
////		Release r = new Release();
////		r.setReleaseName(name);
////		
////		Release res = connect().find(Release.class, r);
//		
//		/**
//		CriteriaBuilder crBuild = connect().getCriteriaBuilder();
//		CriteriaQuery<Release> crQuery = crBuild.createQuery(Release.class);
//		Root<Release> root = crQuery.from(Release.class);
//		
//		crQuery.select(root).where(crBuild.equal(root.get("releaseName"), name));
//		TypedQuery<Release> tQuery = connect().createQuery(crQuery);
//		List<Release> result = tQuery.getResultList();
//*/
//		TypedQuery<Release> query = connect().createQuery("SELECT r FROM Release r WHERE r.releaseName = ?", Release.class);
//		query.setParameter(1, name);
//		List<Release> result = query.getResultList();
//
//
//		if(result!=null && !result.isEmpty())
//			return result.get(0);
//		else 
//			return null;
//		
//	}
//	
//	public Release saveRelease(Release rel) {
//		
//		if(rel!=null && rel.getReleaseName()!=null) {
//			Release r = getRelease(rel.getReleaseName());
//			if(r!=null) {
//				return r;
//			}
//			connect().persist(rel);
//			
//		}
//		return rel;
//		
//	}
//	
//	private EntityManager connect() {
//		return JpaConnectionFactory.getConnection();
//	}
//
//}
