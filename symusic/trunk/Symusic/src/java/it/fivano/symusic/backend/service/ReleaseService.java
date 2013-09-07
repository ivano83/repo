package it.fivano.symusic.backend.service;

import java.util.List;

import org.apache.log4j.Logger;

import it.fivano.symusic.backend.TransformerUtility;
import it.fivano.symusic.backend.dao.ReleaseMapper;
import it.fivano.symusic.backend.model.Release;
import it.fivano.symusic.backend.model.ReleaseExample;
import it.fivano.symusic.exception.BackEndException;
import it.fivano.symusic.model.LinkModel;
import it.fivano.symusic.model.ReleaseModel;
import it.fivano.symusic.model.VideoModel;

public class ReleaseService extends RootService {
	
	Logger log = Logger.getLogger(getClass());
	
	private ReleaseMapper getReleaseMapper() throws BackEndException {
		return this.apriSessione().getMapper(ReleaseMapper.class);
	}
	
	public ReleaseModel getRelease(String name) throws BackEndException {
		
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
				return TransformerUtility.transformReleaseToModel(res.get(0));
		} finally {
			this.chiudiSessione();
		}
	}

	public ReleaseModel getReleaseFull(String name) throws BackEndException {
		
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
			else {
				
				ReleaseModel relRes = TransformerUtility.transformReleaseToModel(res.get(0));
				Long idRel = relRes.getId();
				List<VideoModel> videos = new VideoService().getVideos(idRel);
				relRes.setVideos(videos);
				
				List<LinkModel> links = new LinkService().getLinks(idRel);
				relRes.setLinks(links);
				
				return relRes;
			}
		} finally {
			this.chiudiSessione();
		}
	}

	
	public ReleaseModel saveRelease(Release rel) throws BackEndException {
		
		try {
			if(rel!=null && rel.getReleaseName()!=null) {
				ReleaseModel r = getRelease(rel.getReleaseName());
				if(r!=null) {
					return r;
				}
				ReleaseMapper releaseDao = this.getReleaseMapper();
				releaseDao.insert(rel);
				
			}
			else {
				throw new BackEndException("Non ci sono dati sufficienti per salvare la release: "+rel);
			}
			return TransformerUtility.transformReleaseToModel(rel);
		} finally {
			this.chiudiSessione();
		}
	}
	
	
}
