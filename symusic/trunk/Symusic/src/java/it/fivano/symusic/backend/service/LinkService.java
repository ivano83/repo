package it.fivano.symusic.backend.service;

import it.fivano.symusic.backend.TransformerUtility;
import it.fivano.symusic.backend.dao.ReleaseLinkMapper;
import it.fivano.symusic.backend.model.ReleaseLink;
import it.fivano.symusic.backend.model.ReleaseLinkExample;
import it.fivano.symusic.exception.BackEndException;
import it.fivano.symusic.model.LinkModel;

import java.util.ArrayList;
import java.util.List;

public class LinkService extends RootService {

	public LinkService() {
		this.setLogger(getClass());
	}

	private ReleaseLinkMapper getLinkMapper() throws BackEndException {
		return this.apriSessione().getMapper(ReleaseLinkMapper.class);
	}
	
	public List<LinkModel> getLinks(Long idRelease) throws BackEndException {
		
		try {

			ReleaseLinkMapper linkDao = this.getLinkMapper();
						
			return this.getLinks(idRelease, linkDao);
			
		} finally {
			this.chiudiSessione();
		}
	}
	
	private List<LinkModel> getLinks(Long idRelease, ReleaseLinkMapper linkDao) throws BackEndException {
		
			ReleaseLinkExample input = new ReleaseLinkExample();
			input.createCriteria().andIdReleaseEqualTo(idRelease);
						
			List<ReleaseLink> res = linkDao.selectByExample(input);
			
			return TransformerUtility.transformLinksToModel(res);
			
	}

	
	public LinkModel saveLink(LinkModel link, Long idRelease) throws BackEndException {
		
		try {
			ReleaseLink linkIn = TransformerUtility.transformLink(link, idRelease);
			ReleaseLinkMapper videoDao = this.getLinkMapper();
			
			return this.saveLink(linkIn, videoDao);
		} finally {
			this.chiudiSessione();
		}
	}
	
	public List<LinkModel> saveLinks(List<LinkModel> links, Long idRelease) throws BackEndException {
		
		try {
			List<ReleaseLink> linkIn = TransformerUtility.transformLinks(links, idRelease);
			
			List<LinkModel> result = new ArrayList<LinkModel>();
			ReleaseLinkMapper videoDao = this.getLinkMapper();
			
			for(ReleaseLink v : linkIn) {
				result.add(this.saveLink(v, videoDao));
			}
			
			return result;
		} finally {
			this.chiudiSessione();
		}
	}
	
	private LinkModel saveLink(ReleaseLink linkIn, ReleaseLinkMapper linkDao) throws BackEndException {
		

		if(linkIn!=null && linkIn.getIdRelease()!=null && linkIn.getReleaseLink()!=null) {
			// controllo di esistenza del video sul DB
			List<LinkModel> linkList = getLinks(linkIn.getIdRelease(), linkDao);
			boolean isPresente = false;
			for(LinkModel v : linkList) {
				if(v.getLink().equalsIgnoreCase(linkIn.getReleaseLink())) {
					log.info("Il link e' gia' presente per la release ID_REL:"+linkIn.getIdRelease()+"  LINK:"+linkIn.getReleaseLink());
					isPresente = true;
					break;
				}
			}

			if(!isPresente) {
				linkDao.insert(linkIn);
				log.info("Il link e' stato salvato con ID:"+linkIn.getIdReleaseLink()+"  ID_REL:"+linkIn.getIdRelease()+"  LINK:"+linkIn.getReleaseLink());
			}

		}
		else {
			throw new BackEndException("Non ci sono dati sufficienti per salvare il link: "+linkIn);
		}
		return TransformerUtility.transformLinkToModel(linkIn);
	}
	
	public int deleteLink(Long idlink) throws BackEndException {
		
		try {
			
			ReleaseLinkMapper linkDao = this.getLinkMapper();
			
			int result = linkDao.deleteByPrimaryKey(idlink);
			
			return result;
		} finally {
			this.chiudiSessione();
		}
	}
	
	public int deleteReleaseLinks(Long idrelease) throws BackEndException {
		
		try {
			
			ReleaseLinkMapper linkDao = this.getLinkMapper();
			
			ReleaseLinkExample input = new ReleaseLinkExample();
			input.createCriteria().andIdReleaseEqualTo(idrelease);
			
			int result = 0;
			List<ReleaseLink> linkList = linkDao.selectByExample(input);
			for(ReleaseLink link : linkList) {
				linkDao.deleteByPrimaryKey(link.getIdReleaseLink());
				result++;
			}
			
			return result;
		} finally {
			this.chiudiSessione();
		}
	}
	
}
