package it.fivano.symusic.backend.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import it.fivano.symusic.backend.TransformerUtility;
import it.fivano.symusic.backend.dao.ReleaseMapper;
import it.fivano.symusic.backend.dao.ReleaseVideoMapper;
import it.fivano.symusic.backend.model.Release;
import it.fivano.symusic.backend.model.ReleaseExample;
import it.fivano.symusic.backend.model.ReleaseVideo;
import it.fivano.symusic.backend.model.ReleaseVideoExample;
import it.fivano.symusic.exception.BackEndException;
import it.fivano.symusic.model.VideoModel;

public class VideoService extends RootService {
	
	Logger log = Logger.getLogger(getClass());
	
	private ReleaseVideoMapper getVideoMapper() throws BackEndException {
		return this.apriSessione().getMapper(ReleaseVideoMapper.class);
	}
	
	public List<VideoModel> getVideos(Long idRelease) throws BackEndException {
		
		try {
			ReleaseVideoExample input = new ReleaseVideoExample();
			input.createCriteria().andIdReleaseEqualTo(idRelease);
			
			ReleaseVideoMapper videoDao = this.getVideoMapper();
			
			List<ReleaseVideo> res = videoDao.selectByExample(input);
			
			return TransformerUtility.transformVideosToModel(res);
			
		} finally {
			this.chiudiSessione();
		}
	}

	
	public VideoModel saveVideo(ReleaseVideo videoIn) throws BackEndException {
		
		try {
			
			ReleaseVideoMapper videoDao = this.getVideoMapper();
			
			return this.saveVideo(videoIn, videoDao);
		} finally {
			this.chiudiSessione();
		}
	}
	
	public List<VideoModel> saveVideos(List<ReleaseVideo> videoIn) throws BackEndException {
		
		try {
			List<VideoModel> result = new ArrayList<VideoModel>();
			ReleaseVideoMapper videoDao = this.getVideoMapper();
			
			for(ReleaseVideo v : videoIn) {
				result.add(this.saveVideo(v, videoDao));
			}
			
			return result;
		} finally {
			this.chiudiSessione();
		}
	}
	
	private VideoModel saveVideo(ReleaseVideo videoIn, ReleaseVideoMapper videoDao) throws BackEndException {
		

		if(videoIn!=null && videoIn.getIdRelease()!=null && videoIn.getVideoLink()!=null) {
			// controllo di esistenza del video sul DB
			List<VideoModel> videoList = getVideos(videoIn.getIdRelease());
			boolean isPresente = false;
			for(VideoModel v : videoList) {
				if(v.getLink().equalsIgnoreCase(videoIn.getVideoLink())) {
					log.info("Il video e' gia' presente per la release ID_REL:"+videoIn.getIdRelease()+"  LINK:"+videoIn.getVideoLink());
					isPresente = true;
					break;
				}
			}

			if(!isPresente) {
				videoDao.insert(videoIn);
				log.info("Il video e' stato salvato con ID:"+videoIn.getIdReleaseVideo()+"  ID_REL:"+videoIn.getIdRelease()+"  LINK:"+videoIn.getVideoLink());
			}

		}
		else {
			throw new BackEndException("Non ci sono dati sufficienti per salvare il video: "+videoIn);
		}
		return TransformerUtility.transformVideoToModel(videoIn);
	}
	
}
