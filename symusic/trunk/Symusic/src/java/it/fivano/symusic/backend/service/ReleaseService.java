package it.fivano.symusic.backend.service;

import it.fivano.symusic.backend.TransformerUtility;
import it.fivano.symusic.backend.dao.ReleaseMapper;
import it.fivano.symusic.backend.model.Release;
import it.fivano.symusic.backend.model.ReleaseExample;
import it.fivano.symusic.exception.BackEndException;
import it.fivano.symusic.model.GenreModel;
import it.fivano.symusic.model.LinkModel;
import it.fivano.symusic.model.ReleaseExtractionModel;
import it.fivano.symusic.model.ReleaseFlagModel;
import it.fivano.symusic.model.ReleaseModel;
import it.fivano.symusic.model.TrackModel;
import it.fivano.symusic.model.VideoModel;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ReleaseService extends RootService {

	public ReleaseService() {
		this.setLogger(getClass());
	}

	private ReleaseMapper getReleaseMapper() throws BackEndException {
		return this.apriSessione().getMapper(ReleaseMapper.class);
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

	public List<String> getCrewList() throws BackEndException {

		try {

			ReleaseMapper releaseDao = this.getReleaseMapper();

			List<String> res = releaseDao.selectDistinctCrew();

			return res;

		} catch (Exception e) {
			throw new BackEndException(e);
		} finally {
			this.chiudiSessione();
		}
	}

	public Date getLastReleaseDate(Integer idGenre) throws BackEndException {
		try {

			ReleaseMapper releaseDao = this.getReleaseMapper();

			java.sql.Date res = releaseDao.selectLastReleaseDate(idGenre);

			return new Date(res.getTime());

		} catch (Exception e) {
			throw new BackEndException(e);
		} finally {
			this.chiudiSessione();
		}

	}

	public List<ReleaseModel> getListRelease(String genre, Date initDate, Date endDate, Long idUser) throws BackEndException {

		List<ReleaseModel> result = new ArrayList<ReleaseModel>();
		Set<String> resTmp = new HashSet<String>();
		try {

			ReleaseMapper releaseDao = this.getReleaseMapper();

			ReleaseExample input = new ReleaseExample();
			ReleaseExample.Criteria cr = input.createCriteria();
			if(genre!=null) {
				cr.andIdGenreEqualTo(new GenreService().getGenreByName(genre).getId());
			}
			cr.andReleaseDateBetween(initDate, endDate);
			input.setOrderByClause("release_date asc");

			List<Release> res = releaseDao.selectByExample(input);

			for(Release r : res) {
				resTmp.add(r.getReleaseName());
				log.warn("Recuperata da DB la release = '"+r.getReleaseName()+"' - ID Genere = "+r.getIdGenre());
			}

		} catch (Exception e) {
			throw new BackEndException(e);
		} finally {
			this.chiudiSessione();
		}

		for(String releaseName : resTmp) {
			result.add(this.getReleaseFull(releaseName, idUser));
		}

		return result;
	}

	public List<ReleaseModel> getListSimilarRelease(String likeString, String field, Long idUser) throws BackEndException {

		List<ReleaseModel> result = new ArrayList<ReleaseModel>();
		List<Release> res = new ArrayList<Release>();
		try {

			likeString = "%"+likeString+"%";

			ReleaseMapper releaseDao = this.getReleaseMapper();

			ReleaseExample input = new ReleaseExample();
			ReleaseExample.Criteria cr = input.createCriteria();
			if("author".equalsIgnoreCase(field)) {
				cr.andAuthorLike(likeString);
			}
			else if("song".equalsIgnoreCase(field)) {
				cr.andSongNameLike(likeString);
			}
			else {
				cr.andReleaseNameLike(likeString);
			}
			input.setOrderByClause("release_date desc limit 5");

			res = releaseDao.selectByExample(input);


		} catch (Exception e) {
			throw new BackEndException(e);
		} finally {
			this.chiudiSessione();
		}

		ReleaseModel relModel = null;
		for(Release releaseName : res) {
			relModel = this.getReleaseFull(releaseName.getReleaseName(), idUser);
			if(relModel.getReleaseFlag()!=null && relModel.getReleaseFlag().getDownloaded()!=null && relModel.getReleaseFlag().getDownloaded()) {
				result.add(relModel);
				log.info("Recuperata da DB la release = "+releaseName.getReleaseName());
			}
		}

		return result;
	}

	public ReleaseModel getReleaseFull(String name, Long idUser) throws BackEndException {

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

				if(res.get(0).getIdGenre()!=null) {
					GenreModel genere = new GenreService().getGenre(res.get(0).getIdGenre());
					relRes.setGenre(genere);
				}

				ReleaseExtractionModel extr = new ReleaseExtractionService().getReleaseExtraction(idRel);
				relRes.setReleaseExtraction(extr);

				ReleaseFlagModel flagRel = new ReleaseOptionService().getReleaseOption(idRel, idUser);
				relRes.setReleaseFlag(flagRel);

				return relRes;
			}
		} catch (ParseException e) {
			throw new BackEndException(e);
		}
	}

	public void deleteReleaseFull(Long idRelease) throws BackEndException {


		try {

			ReleaseMapper releaseDao = this.getReleaseMapper();

			Release res = releaseDao.selectByPrimaryKey(idRelease);

			if(res == null) {
				log.error("Impossibile eliminare la release con ID '"+idRelease+"' in quanto non esiste");
				throw new BackEndException("Impossibile eliminare la release con ID '"+idRelease+"' in quanto non esiste");
			}

			int count = new VideoService().deleteReleaseVideos(idRelease);
			log.info("RELEASE "+idRelease+" - Eliminati "+count+" Video");

			count = new LinkService().deleteReleaseLinks(idRelease);
			log.info("RELEASE "+idRelease+" - Eliminati "+count+" Link");

			count = new TrackService().deleteReleaseTracks(idRelease);
			log.info("RELEASE "+idRelease+" - Eliminati "+count+" Track");

			count = new ReleaseExtractionService().deleteReleaseExtraction(idRelease);
			log.info("RELEASE "+idRelease+" - Eliminati "+count+" ReleaseExtraction");

			count = releaseDao.deleteByPrimaryKey(idRelease);
			log.info("RELEASE "+idRelease+" - Eliminata la release "+res.getReleaseName());


		} finally {
			this.chiudiSessione();
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


}
