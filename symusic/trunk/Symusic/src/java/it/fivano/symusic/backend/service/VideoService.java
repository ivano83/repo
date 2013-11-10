package it.fivano.symusic.backend.service;

import it.fivano.symusic.backend.TransformerUtility;
import it.fivano.symusic.backend.dao.ReleaseTrackMapper;
import it.fivano.symusic.backend.dao.ReleaseVideoMapper;
import it.fivano.symusic.backend.model.ReleaseTrack;
import it.fivano.symusic.backend.model.ReleaseTrackExample;
import it.fivano.symusic.backend.model.ReleaseVideo;
import it.fivano.symusic.backend.model.ReleaseVideoExample;
import it.fivano.symusic.exception.BackEndException;
import it.fivano.symusic.model.VideoModel;

import java.util.ArrayList;
import java.util.List;

public class VideoService extends RootService {
	
	public VideoService() {
		this.setLogger(getClass());
	}
	
	private ReleaseVideoMapper getVideoMapper() throws BackEndException {
		return this.apriSessione().getMapper(ReleaseVideoMapper.class);
	}
	
	public List<VideoModel> getVideos(Long idRelease) throws BackEndException {
		
		try {
			ReleaseVideoMapper videoDao = this.getVideoMapper();
						
			return this.getVideos(idRelease, videoDao);
			
		} finally {
			this.chiudiSessione();
		}
	}

	private List<VideoModel> getVideos(Long idRelease, ReleaseVideoMapper videoDao) throws BackEndException {
		
		ReleaseVideoExample input = new ReleaseVideoExample();
		input.createCriteria().andIdReleaseEqualTo(idRelease);

		List<ReleaseVideo> res = videoDao.selectByExample(input);

		return TransformerUtility.transformVideosToModel(res);

	}

	
	public VideoModel saveVideo(VideoModel video, Long idRelease) throws BackEndException {
		
		try {
			ReleaseVideo videoIn = TransformerUtility.transformVideo(video, idRelease);
			ReleaseVideoMapper videoDao = this.getVideoMapper();
			
			return this.saveVideo(videoIn, videoDao);
		} finally {
			this.chiudiSessione();
		}
	}
	
	public List<VideoModel> saveVideos(List<VideoModel> video, Long idRelease) throws BackEndException {
		
		try {
			List<ReleaseVideo> videoIn = TransformerUtility.transformVideos(video, idRelease);
			
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
			List<VideoModel> videoList = getVideos(videoIn.getIdRelease(), videoDao);
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
	
	public int deleteVideo(Long idvideo) throws BackEndException {
		
		try {
			
			ReleaseVideoMapper videoDao = this.getVideoMapper();
			int result = videoDao.deleteByPrimaryKey(idvideo);
			
			return result;
		} finally {
			this.chiudiSessione();
		}
	}
	
	public int deleteReleaseVideos(Long idrelease) throws BackEndException {
		
		try {
			
			ReleaseVideoMapper videoDao = this.getVideoMapper();
			ReleaseVideoExample input = new ReleaseVideoExample();
			input.createCriteria().andIdReleaseEqualTo(idrelease);
			
			int result = 0;
			List<ReleaseVideo> linkList = videoDao.selectByExample(input);
			for(ReleaseVideo track : linkList) {
				videoDao.deleteByPrimaryKey(track.getIdReleaseVideo());
				result++;
			}
			
			return result;
		} finally {
			this.chiudiSessione();
		}
	}
	
	
}
