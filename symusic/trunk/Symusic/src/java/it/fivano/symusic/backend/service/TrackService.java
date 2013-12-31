package it.fivano.symusic.backend.service;

import it.fivano.symusic.backend.TransformerUtility;
import it.fivano.symusic.backend.dao.ReleaseLinkMapper;
import it.fivano.symusic.backend.dao.ReleaseTrackMapper;
import it.fivano.symusic.backend.model.ReleaseLink;
import it.fivano.symusic.backend.model.ReleaseLinkExample;
import it.fivano.symusic.backend.model.ReleaseTrack;
import it.fivano.symusic.backend.model.ReleaseTrackExample;
import it.fivano.symusic.exception.BackEndException;
import it.fivano.symusic.model.TrackModel;

import java.util.ArrayList;
import java.util.List;

public class TrackService extends RootService {
	
	public TrackService() {
		this.setLogger(getClass());
	}
	
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
			
			List<TrackModel> trackList = getTracks(idRelease, trackDao);
			
			return this.saveTrack(trackIn, trackDao, trackList);
		} finally {
			this.chiudiSessione();
		}
	}
	
	public List<TrackModel> saveTracks(List<TrackModel> tracks, Long idRelease) throws BackEndException {
		
		try {
			List<ReleaseTrack> trackIn = TransformerUtility.transformTracks(tracks,idRelease);
			
			List<TrackModel> result = new ArrayList<TrackModel>();
			ReleaseTrackMapper trackDao = this.getTrackMapper();
			
			List<TrackModel> trackList = getTracks(idRelease, trackDao);
			
			for(ReleaseTrack v : trackIn) {
				result.add(this.saveTrack(v, trackDao, trackList));
			}
			
			return result;
		} finally {
			this.chiudiSessione();
		}
	}
	
	private TrackModel saveTrack(ReleaseTrack trackIn, ReleaseTrackMapper trackDao, List<TrackModel> trackList) throws BackEndException {
		

		if(trackIn!=null && trackIn.getIdRelease()!=null && trackIn.getTrackName()!=null) {
			// controllo di esistenza della traccia sul DB
			
			boolean isPresente = false;
			for(TrackModel v : trackList) {
				if(v.getTrackName().equalsIgnoreCase(trackIn.getTrackName()) && 
						(v.getTrackNumber()+"").equals(trackIn.getTrackNumber())) {
					log.info("La track e' gia' presente per la release ID_REL:"+trackIn.getIdRelease()+"  TRACK:"+trackIn.getTrackName());
					isPresente = true;
					break;
				}
			}

			if(!isPresente) {
				trackDao.insert(trackIn);
				log.info("La track e' stata salvata con ID:"+trackIn.getIdReleaseTrack()+"  ID_REL:"+trackIn.getIdRelease()+"  TRACK:"+trackIn.getTrackName());
			}
			else {
				this.updateTrack(trackIn, trackDao, trackList);
			}

		}
		else {
			throw new BackEndException("Non ci sono dati sufficienti per salvare la track: "+trackIn);
		}
		return TransformerUtility.transformTrackToModel(trackIn);
	}
	
	public List<TrackModel> updateTracks(List<TrackModel> tracks, Long idRelease) throws BackEndException {
		
		try {
			List<ReleaseTrack> trackIn = TransformerUtility.transformTracks(tracks,idRelease);
			
			List<TrackModel> result = new ArrayList<TrackModel>();
			ReleaseTrackMapper trackDao = this.getTrackMapper();
			
			List<TrackModel> trackList = getTracks(idRelease, trackDao);
			
			for(ReleaseTrack v : trackIn) {
				result.add(this.updateTrack(v, trackDao, trackList));
			}
			
			return result;
		} finally {
			this.chiudiSessione();
		}
	}
	
	private TrackModel updateTrack(ReleaseTrack trackIn, ReleaseTrackMapper trackDao, List<TrackModel> trackList) throws BackEndException {
		

		if(trackIn!=null && trackIn.getIdRelease()!=null && trackIn.getTrackName()!=null) {
			// controllo di esistenza della traccia sul DB
			
			for(TrackModel v : trackList) {
				if((v.getTrackNumber()+"").equals(trackIn.getTrackNumber())) {
					trackIn.setIdReleaseTrack(v.getIdTrack());
					trackDao.updateByPrimaryKeySelective(trackIn);
					log.info("La track e' stata aggiornata - ID:"+trackIn.getIdReleaseTrack()+"  ID_REL:"+trackIn.getIdRelease()+"  TRACK:"+trackIn.getTrackName());
					break;
				}
			}

		}
		else {
			throw new BackEndException("Non ci sono dati sufficienti per salvare la track: "+trackIn);
		}
		return TransformerUtility.transformTrackToModel(trackIn);
	}
	
	public int deleteTrack(Long idtrack) throws BackEndException {
		
		try {
			
			ReleaseTrackMapper trackDao = this.getTrackMapper();
			int result = trackDao.deleteByPrimaryKey(idtrack);
			
			return result;
		} finally {
			this.chiudiSessione();
		}
	}
	
	public int deleteReleaseTracks(Long idrelease) throws BackEndException {
		
		try {
			
			ReleaseTrackMapper trackDao = this.getTrackMapper();
			ReleaseTrackExample input = new ReleaseTrackExample();
			input.createCriteria().andIdReleaseEqualTo(idrelease);
			
			int result = 0;
			List<ReleaseTrack> linkList = trackDao.selectByExample(input);
			for(ReleaseTrack track : linkList) {
				trackDao.deleteByPrimaryKey(track.getIdReleaseTrack());
				result++;
			}
			
			return result;
		} finally {
			this.chiudiSessione();
		}
	}
	
}
