package it.fivano.symusic.backend.service;

import it.fivano.symusic.backend.TransformerUtility;
import it.fivano.symusic.backend.dao.ReleaseLinkMapper;
import it.fivano.symusic.backend.model.ReleaseLink;
import it.fivano.symusic.backend.model.ReleaseLinkExample;
import it.fivano.symusic.exception.BackEndException;
import it.fivano.symusic.model.LinkModel;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

public class LinkService extends RootService {
	
	Logger log = Logger.getLogger(getClass());
	
	private ReleaseLinkMapper getLinkMapper() throws BackEndException {
		return this.apriSessione().getMapper(ReleaseLinkMapper.class);
	}
	
	public List<LinkModel> getLinks(Long idRelease) throws BackEndException {
		
		try {
			ReleaseLinkExample input = new ReleaseLinkExample();
			input.createCriteria().andIdReleaseEqualTo(idRelease);
			
			ReleaseLinkMapper videoDao = this.getLinkMapper();
			
			List<ReleaseLink> res = videoDao.selectByExample(input);
			
			return TransformerUtility.transformLinksToModel(res);
			
		} finally {
			this.chiudiSessione();
		}
	}

	
	public LinkModel saveLink(ReleaseLink videoIn) throws BackEndException {
		
		try {
			
			ReleaseLinkMapper videoDao = this.getLinkMapper();
			
			return this.saveLink(videoIn, videoDao);
		} finally {
			this.chiudiSessione();
		}
	}
	
	public List<LinkModel> saveLinks(List<ReleaseLink> videoIn) throws BackEndException {
		
		try {
			List<LinkModel> result = new ArrayList<LinkModel>();
			ReleaseLinkMapper videoDao = this.getLinkMapper();
			
			for(ReleaseLink v : videoIn) {
				result.add(this.saveLink(v, videoDao));
			}
			
			return result;
		} finally {
			this.chiudiSessione();
		}
	}
	
	private LinkModel saveLink(ReleaseLink linkIn, ReleaseLinkMapper videoDao) throws BackEndException {
		

		if(linkIn!=null && linkIn.getIdRelease()!=null && linkIn.getReleaseLink()!=null) {
			// controllo di esistenza del video sul DB
			List<LinkModel> videoList = getLinks(linkIn.getIdRelease());
			boolean isPresente = false;
			for(LinkModel v : videoList) {
				if(v.getLink().equalsIgnoreCase(linkIn.getReleaseLink())) {
					log.info("Il link e' gia' presente per la release ID_REL:"+linkIn.getIdRelease()+"  LINK:"+linkIn.getIdReleaseLink());
					isPresente = true;
					break;
				}
			}

			if(!isPresente) {
				videoDao.insert(linkIn);
				log.info("Il link e' stato salvato con ID:"+linkIn.getIdReleaseLink()+"  ID_REL:"+linkIn.getIdRelease()+"  LINK:"+linkIn.getIdReleaseLink());
			}

		}
		else {
			throw new BackEndException("Non ci sono dati sufficienti per salvare il link: "+linkIn);
		}
		return TransformerUtility.transformLinkToModel(linkIn);
	}
	
}
