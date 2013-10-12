package it.fivano.symusic.backend.service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import it.fivano.symusic.backend.TransformerUtility;
import it.fivano.symusic.backend.dao.ReleaseExtractionMapper;
import it.fivano.symusic.backend.dao.ReleaseMapper;
import it.fivano.symusic.backend.model.Release;
import it.fivano.symusic.backend.model.ReleaseExample;
import it.fivano.symusic.backend.model.ReleaseExtraction;
import it.fivano.symusic.backend.model.ReleaseExtractionExample;
import it.fivano.symusic.exception.BackEndException;
import it.fivano.symusic.model.GenreModel;
import it.fivano.symusic.model.LinkModel;
import it.fivano.symusic.model.ReleaseExtractionModel;
import it.fivano.symusic.model.ReleaseModel;
import it.fivano.symusic.model.TrackModel;
import it.fivano.symusic.model.VideoModel;

public class ReleaseService extends RootService {
	
	public ReleaseService() {
		this.setLogger(getClass());
	}
	
	private ReleaseMapper getReleaseMapper() throws BackEndException {
		return this.apriSessione().getMapper(ReleaseMapper.class);
	}
	private ReleaseExtractionMapper getReleaseExtractionMapper() throws BackEndException {
		return this.apriSessione().getMapper(ReleaseExtractionMapper.class);
	}
	
	public ReleaseModel getRelease(String name) throws BackEndException {
		
		try {
			ReleaseMapper releaseDao = this.getReleaseMapper();

			return this.getRelease(name, releaseDao);
			
		} catch (ParseException e) {
			throw new BackEndException(e);
		} finally {
			this.chiudiSessione();
		}
	}
	
	private ReleaseModel getRelease(String name, ReleaseMapper releaseDao) throws BackEndException, ParseException {
		
		ReleaseExample input = new ReleaseExample();
		input.createCriteria().andReleaseNameEqualTo(name);

		List<Release> res = releaseDao.selectByExample(input);

		if(res.size()>1) {
			log.warn("La ricerca per nome release = '"+name+"' ha restituito più di un valore");
		}
		if(res.size()==0)
			return null;
		else 
			return TransformerUtility.transformReleaseToModel(res.get(0));

	}
	
	public ReleaseModel getRelease(Long idRel) throws BackEndException {
		
		try {

			ReleaseMapper releaseDao = this.getReleaseMapper();
			
			Release res = releaseDao.selectByPrimaryKey(idRel);

			return TransformerUtility.transformReleaseToModel(res);
			
		} catch (ParseException e) {
			throw new BackEndException(e);
		} finally {
			this.chiudiSessione();
		}
	}

	public ReleaseModel getReleaseFull(String name) throws BackEndException {
		
		List<Release> res = new ArrayList<Release>();
		
		try {
			ReleaseExample input = new ReleaseExample();
			input.createCriteria().andReleaseNameEqualTo(name);
			
			ReleaseMapper releaseDao = this.getReleaseMapper();
			
			res = releaseDao.selectByExample(input);
			
			if(res.size()>1) {
				log.warn("La ricerca per nome release = '"+name+"' ha restituito più di un valore");
			}
			
		} finally {
			this.chiudiSessione();
		}
		
		try {
		
			if(res.size()==0)
				return null;
			else {
				
				ReleaseModel relRes = TransformerUtility.transformReleaseToModel(res.get(0));
				Long idRel = relRes.getId();
				List<VideoModel> videos = new VideoService().getVideos(idRel);
				relRes.setVideos(videos);
				
				List<LinkModel> links = new LinkService().getLinks(idRel);
				relRes.setLinks(links);
				
				List<TrackModel> tracks = new TrackService().getTracks(idRel);
				relRes.setTracks(tracks);
				
				GenreModel genere = new GenreService().getGenre(res.get(0).getIdGenre());
				relRes.setGenre(genere);
				
				ReleaseExtractionModel extr = this.getReleaseExtraction(idRel);
				relRes.setReleaseExtraction(extr);
				
				return relRes;
			}
		} catch (ParseException e) {
			throw new BackEndException(e);
		}
	}

	
	public ReleaseModel saveRelease(ReleaseModel release) throws BackEndException {
		
		try {
			Release rel = TransformerUtility.transformRelease(release);
			
			if(rel!=null && rel.getReleaseName()!=null) {
				ReleaseMapper releaseDao = this.getReleaseMapper();
				ReleaseModel r = getRelease(rel.getReleaseName(), releaseDao);
				if(r!=null) {
					return r;
				}
				releaseDao.insert(rel);
				
			}
			else {
				throw new BackEndException("Non ci sono dati sufficienti per salvare la release: "+rel);
			}
			return TransformerUtility.transformReleaseToModel(rel);
		} catch (ParseException e) {
			throw new BackEndException(e);
		} finally {
			this.chiudiSessione();
		}
	}
	
	
	public ReleaseExtractionModel getReleaseExtraction(Long idRelease) throws BackEndException {
		
		try {
			ReleaseExtractionMapper releaseExtrDao = this.getReleaseExtractionMapper();
			
			return this.getReleaseExtraction(idRelease, releaseExtrDao);
			
		} catch (Exception e) {
			throw new BackEndException(e);
		} finally {
			this.chiudiSessione();
		}
	}
	
	private ReleaseExtractionModel getReleaseExtraction(Long idRelease, ReleaseExtractionMapper releaseExtrDao) throws BackEndException {
		
		ReleaseExtractionExample input = new ReleaseExtractionExample();
		input.createCriteria().andIdReleaseEqualTo(idRelease);

		ReleaseExtraction res = releaseExtrDao.selectByPrimaryKey(idRelease);

//		if(res==null) {
//			log.warn("La ricerca delle estrazioni per id release = '"+idRelease+"' non ha restituito niente");
//			ReleaseExtractionModel extr = new ReleaseExtractionModel();
//			extr.setIdRelease(idRelease);
//			extr = this.saveReleaseExtraction(extr);
//			return extr;
//		}

		return TransformerUtility.transformReleaseExtractionToModel(res);
			
	}
	
	public ReleaseExtractionModel saveReleaseExtraction(ReleaseExtractionModel releaseExtr) throws BackEndException {
		
		try {
			ReleaseExtraction rel = TransformerUtility.transformReleaseExtraction(releaseExtr);
			
			if(rel!=null && rel.getIdRelease()!=null) {
				ReleaseExtractionMapper releaseExtrDao = this.getReleaseExtractionMapper();
				
				ReleaseExtractionExample input = new ReleaseExtractionExample();
				input.createCriteria().andIdReleaseEqualTo(releaseExtr.getIdRelease());
				ReleaseExtraction inputRes = releaseExtrDao.selectByPrimaryKey(releaseExtr.getIdRelease());
				if(inputRes!=null) {
					releaseExtrDao.updateByPrimaryKeySelective(rel);
				}
				else {
					releaseExtrDao.insert(rel);
				}
				
			}
			else {
				throw new BackEndException("Non ci sono dati sufficienti per salvare la release: "+rel);
			}
			return TransformerUtility.transformReleaseExtractionToModel(rel);
		} catch (Exception e) {
			throw new BackEndException(e);
		} finally {
			this.chiudiSessione();
		}
	}
	
}
