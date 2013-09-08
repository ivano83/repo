package it.fivano.symusic.backend.service;

import it.fivano.symusic.backend.TransformerUtility;
import it.fivano.symusic.backend.dao.ReleaseLinkMapper;
import it.fivano.symusic.backend.dao.ReleaseTrackMapper;
import it.fivano.symusic.backend.model.ReleaseLink;
import it.fivano.symusic.backend.model.ReleaseLinkExample;
import it.fivano.symusic.backend.model.ReleaseTrack;
import it.fivano.symusic.backend.model.ReleaseTrackExample;
import it.fivano.symusic.exception.BackEndException;
import it.fivano.symusic.model.LinkModel;
import it.fivano.symusic.model.TrackModel;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

public class TrackService extends RootService {
	
	Logger log = Logger.getLogger(getClass());
	
	private ReleaseTrackMapper getTrackMapper() throws BackEndException {
		return this.apriSessione().getMapper(ReleaseTrackMapper.class);
	}
	
	public List<TrackModel> getTracks(Long idRelease) throws BackEndException {
		
		try {

			ReleaseTrackMapper trackDao = this.getTrackMapper();
						
			return this.getTracks(idRelease,trackDao);
			
		} finally {
			this.chiudiSessione();
		}
	}

	private List<TrackModel> getTracks(Long idRelease, ReleaseTrackMapper trackDao) throws BackEndException {
		
		ReleaseTrackExample input = new ReleaseTrackExample();
		input.createCriteria().andIdReleaseEqualTo(idRelease);

		List<ReleaseTrack> res = trackDao.selectByExample(input);

		return TransformerUtility.transformTracksToModel(res);

	}

	
	public TrackModel saveTrack(TrackModel track, Long idRelease) throws BackEndException {
		
		try {
			ReleaseTrack trackIn = TransformerUtility.transformTrack(track,idRelease);
			ReleaseTrackMapper trackDao = this.getTrackMapper();
			
			return this.saveTrack(trackIn, trackDao);
		} finally {
			this.chiudiSessione();
		}
	}
	
	public List<TrackModel> saveTracks(List<TrackModel> tracks, Long idRelease) throws BackEndException {
		
		try {
			List<ReleaseTrack> trackIn = TransformerUtility.transformTracks(tracks,idRelease);
			
			List<TrackModel> result = new ArrayList<TrackModel>();
			ReleaseTrackMapper videoDao = this.getTrackMapper();
			
			for(ReleaseTrack v : trackIn) {
				result.add(this.saveTrack(v, videoDao));
			}
			
			return result;
		} finally {
			this.chiudiSessione();
		}
	}
	
	private TrackModel saveTrack(ReleaseTrack trackIn, ReleaseTrackMapper trackDao) throws BackEndException {
		

		if(trackIn!=null && trackIn.getIdRelease()!=null && trackIn.getTrackName()!=null) {
			// controllo di esistenza del video sul DB
			List<TrackModel> trackList = getTracks(trackIn.getIdRelease(), trackDao);
			boolean isPresente = false;
			for(TrackModel v : trackList) {
				if(v.getTrackName().equalsIgnoreCase(trackIn.getTrackName())) {
					log.info("La track e' gia' presente per la release ID_REL:"+trackIn.getIdRelease()+"  TRACK:"+trackIn.getTrackName());
					isPresente = true;
					break;
				}
			}

			if(!isPresente) {
				trackDao.insert(trackIn);
				log.info("Il link e' stato salvato con ID:"+trackIn.getIdReleaseTrack()+"  ID_REL:"+trackIn.getIdRelease()+"  TRACK:"+trackIn.getTrackName());
			}

		}
		else {
			throw new BackEndException("Non ci sono dati sufficienti per salvare la track: "+trackIn);
		}
		return TransformerUtility.transformTrackToModel(trackIn);
	}
	
}
