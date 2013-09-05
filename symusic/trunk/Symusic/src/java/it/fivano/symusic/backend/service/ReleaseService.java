package it.fivano.symusic.backend.service;

import java.util.List;

import org.apache.log4j.Logger;

import it.fivano.symusic.backend.dao.ReleaseMapper;
import it.fivano.symusic.backend.model.Release;
import it.fivano.symusic.backend.model.ReleaseExample;
import it.fivano.symusic.exception.BackEndException;

public class ReleaseService extends RootService {
	
	Logger log = Logger.getLogger(getClass());
	
	private ReleaseMapper getReleaseMapper() throws BackEndException {
		return this.apriSessione().getMapper(ReleaseMapper.class);
	}
	
	public Release getRelease(String name) throws BackEndException {
		
		try {
			ReleaseExample input = new ReleaseExample();
			input.createCriteria().andReleaseNameEqualTo(name);
			
			ReleaseMapper releaseDao = this.getReleaseMapper();
			
			List<Release> res = releaseDao.selectByExample(input);
			
			if(res.size()>1) {
				log.warn("La ricerca per nome release = '"+name+"' ha restituito più di un valore");
			}
			if(res.size()==0)
				return null;
			else 
				return res.get(0);
		} finally {
			this.chiudiSessione();
		}
	}

	
	public Release saveRelease(Release rel) throws BackEndException {
		
		try {
			if(rel!=null && rel.getReleaseName()!=null) {
				Release r = getRelease(rel.getReleaseName());
				if(r!=null) {
					return r;
				}
				ReleaseMapper releaseDao = this.getReleaseMapper();
				releaseDao.insert(rel);
				
			}
			else {
				throw new BackEndException("Non ci sono dati sufficienti per salvare la release: "+rel);
			}
			return rel;
		} finally {
			this.chiudiSessione();
		}
	}
	
	
}
