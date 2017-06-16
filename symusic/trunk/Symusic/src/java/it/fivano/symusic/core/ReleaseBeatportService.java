package it.fivano.symusic.core;

import it.fivano.symusic.SymusicUtility;
import it.fivano.symusic.backend.service.GenreService;
import it.fivano.symusic.backend.service.ReleaseExtractionService;
import it.fivano.symusic.backend.service.ReleaseService;
import it.fivano.symusic.backend.service.TrackService;
import it.fivano.symusic.backend.service.VideoService;
import it.fivano.symusic.core.parser.BeatportParser;
import it.fivano.symusic.core.parser.ScenelogParser;
import it.fivano.symusic.core.parser.YoutubeParser;
import it.fivano.symusic.core.parser.ZeroDayMp3Parser;
import it.fivano.symusic.core.parser.model.BaseReleaseParserModel;
import it.fivano.symusic.core.parser.model.BeatportParserModel;
import it.fivano.symusic.core.parser.model.ScenelogParserModel;
import it.fivano.symusic.core.parser.model.ZeroDayMp3ParserModel;
import it.fivano.symusic.exception.BackEndException;
import it.fivano.symusic.exception.ParseReleaseException;
import it.fivano.symusic.model.GenreModel;
import it.fivano.symusic.model.ReleaseExtractionModel;
import it.fivano.symusic.model.ReleaseExtractionModel.AreaExtraction;
import it.fivano.symusic.model.ReleaseModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ReleaseBeatportService extends ReleaseSiteService {


	private List<ReleaseModel> listRelease;

	public ReleaseBeatportService(Long idUser) throws IOException {
		this.idUser = idUser;
		enableBeatportService = true;
		enableScenelogService = true;
		enableYoutubeService = true;
		this.setLogger(getClass());
	}


	public List<ReleaseModel> parseBeatportRelease(String urlGenrePage, String genre) throws ParseReleaseException, BackEndException {

			listRelease = new ArrayList<ReleaseModel>();
			try {
				YoutubeParser youtube = new YoutubeParser();
				ScenelogParser scenelog = new ScenelogParser();
				BeatportParser beatport = new BeatportParser();
				ZeroDayMp3Parser zeroDay = new ZeroDayMp3Parser();
				List<BeatportParserModel> parserModel = beatport.searchNewReleases(urlGenrePage);

				ReleaseModel release = null;
				for(BeatportParserModel currBeatport : parserModel) {

					release = new ReleaseModel();
					boolean releaseTrovata = false;

					// IMPOSTA IL GENERE
					GenreModel genreMod = new GenreModel();
					genreMod.setName(genre);
					release.setGenre(genreMod);

					enableScenelogService = true;
					enableYoutubeService = true;
					enableBeatportService = true;
					ReleaseExtractionModel extr = new ReleaseExtractionModel();
					release.setReleaseExtraction(extr);

					// PRENDE I DETTAGLI DA BEATPORT
					release = beatport.parseReleaseDetails(currBeatport, release);

					// CERCA LA RELEASE SU SCENELOG, TRAMITE AUTORE E TITOLO DELLA RELEASE
					String author = release.getArtist().split(",")[0].trim();
					if(author.equalsIgnoreCase("various artists"))
						author = "VA";
					String pseudoRelName = author.toLowerCase()+" - "+release.getSong().toLowerCase();
					BaseReleaseParserModel sc = scenelog.searchRelease(pseudoRelName);
					System.out.println(pseudoRelName);


					// CONTROLLA SE LA RELEASE E' GIA' PRESENTE
					boolean isRecuperato = false;
					ReleaseService relServ = new ReleaseService();
					ReleaseModel relDb = null;
					if(sc!=null) {
						relDb = relServ.getReleaseFull(sc.getReleaseName(), idUser);
					}
					if(relDb!=null) {
						log.info(sc.getReleaseName()+" e' gia' presente nel database con id = "+relDb.getId());

						// controlla sul db se le varie estrazioni hanno avuto successo
						extr = relDb.getReleaseExtraction();

						if(extr!=null ){
							enableScenelogService = !extr.getScenelog();
							enableYoutubeService = !extr.getYoutube();
							enableBeatportService = !extr.getBeatport();
						}
						isRecuperato = true;
						release = relDb; // SOSTITUISCE I DATI FINO AD ORA ESTRATTI CON QUELLI DEL DB
						releaseTrovata = true;
					}


					// RECUPERA IL DETTAGLIO DELLA RELEASE DA SCENELOG
					if(sc!=null && enableScenelogService) {
						release = scenelog.parseReleaseDetails(sc, release);
						pseudoRelName = release.getName();
						System.out.println("release trovata: "+release.getNameWithUnderscore());
//						SymusicUtility.updateReleaseExtraction(extr,true,AreaExtraction.SCENELOG);
						releaseTrovata = true;

					}

					if(!releaseTrovata) {
						// PROVA CON 0DAYMP3
						List<ZeroDayMp3ParserModel> zeroRes = zeroDay.searchRelease(release.getNameWithUnderscore());
						if(!zeroRes.isEmpty()) {
							release = zeroDay.parseReleaseDetails(zeroRes.get(0), release);
						}
					}

					// RECUPERA I VIDEO DA YOUTUBE
					if(enableYoutubeService) {
						release.setVideos(youtube.searchYoutubeVideos(pseudoRelName));
						System.out.println(release.getVideos());
						if(release.getVideos().isEmpty())
							SymusicUtility.updateReleaseExtraction(extr,false,AreaExtraction.YOUTUBE);
						else
							SymusicUtility.updateReleaseExtraction(extr,true,AreaExtraction.YOUTUBE);

					}

					// FIX SE LA RELEASE NON E' STATA TROVATA
					// SETTA IL NOME E NOME CON _ FITTIZI
					if(release.getNameWithUnderscore()==null && sc!=null) {
						release.setNameWithUnderscore(sc.getReleaseName());
					} else if(release.getNameWithUnderscore()==null) {
						release.setNameWithUnderscore(pseudoRelName);
					}


					listRelease.add(release);

					if(releaseTrovata) {
						// AGGIORNAMENTI DEI DATI SUL DB
						this.saveOrUpdateRelease(release, isRecuperato);
					}
					else {
						log.info(release+" completa non e' stata trovata, non verra' salvata sul db");
					}

					// AGGIUNGE I LINK DI RICERCA MANUALE (DIRETTAMENTE SU GOOGLE E YOUTUBE)
					GoogleService google = new GoogleService();
					google.addManualSearchLink(release);
					youtube.addManualSearchLink(release); // link a youtube per la ricerca manuale

				}

				/**
				// PAGINA DI INIZIO
				String urlConn = conf.URL_MUSIC;

				// OGGETTO PER GESTIRE IL CARICAMENTO DELLE PAGINE SUCCESSIVE DEL SITO
				ScenelogInfo info = new ScenelogInfo();
				info.setProcessNextPage(true);
				info.setA(a);
				info.setDa(da);

				// PROCESSA LE RELEASE DELLA PRIMA PAGINA
				ScenelogParser scenelog = new ScenelogParser();
				List<ScenelogParserModel> resScenelog = scenelog.parseFullPage(urlConn, da, a);
				this.checkProcessPage(resScenelog, info);

				// SE C'È DA RECUPERARE ALTRE RELEASE, CAMBIA PAGINA
				while(info.isProcessNextPage()) {

					// SALVA LA URL DELLA PROSSIMA PAGINA (SE NECESSARIA)
					info.changePage(); // AGGIORNA IL NUMERO PAGINA
					info.setNextPage(this.extractNextPage(info));

					log.info("Andiamo alla pagina successiva...");
					// PROCESSA LE RELEASE DELLE PAGINE SUCCESSIVE
					List<ScenelogParserModel> resScenelogTmp = scenelog.parseFullPage(info.getNextPage(), da, a);
					this.checkProcessPage(resScenelogTmp, info);
					resScenelog.addAll(resScenelogTmp);

				}


				// per ogni release scenelog recupera i dati da beatport
				BeatportParser beatport = new BeatportParser();
				List<BeatportParserModel> beatportRes = null;
				for(ScenelogParserModel sc : resScenelog) {

					ReleaseModel release = new ReleaseModel();

					release.setNameWithUnderscore(sc.getReleaseName());
					if(excludeRipRelease && this.isRadioRipRelease(release)) {
						continue;
					}

					// CONTROLLA SE LA RELEASE E' GIA' PRESENTE
					boolean isRecuperato = false;
					ReleaseService relServ = new ReleaseService();
					ReleaseModel relDb = relServ.getReleaseFull(sc.getReleaseName());
					ReleaseExtractionModel extr = null;
					if(relDb!=null) {
						log.info(sc.getReleaseName()+" e' gia' presente nel database con id = "+relDb.getId());

						// controlla sul db se le varie estrazioni hanno avuto successo
						extr = relDb.getReleaseExtraction();

						if(extr!=null ){
							enableScenelogService = !extr.getScenelog();
							enableYoutubeService = !extr.getYoutube();
							enableBeatportService = !extr.getBeatport();
						}
						isRecuperato = true;
						release = relDb; // SOSTITUISCE I DATI FINO AD ORA ESTRATTI CON QUELLI DEL DB
					}
					else {
						extr = new ReleaseExtractionModel();
						release.setReleaseExtraction(extr);
					}

					if(enableBeatportService)
						beatportRes = beatport.searchRelease(sc.getReleaseName());
					else
						beatportRes = new ArrayList<BeatportParserModel>();


					if(!beatportRes.isEmpty() || !enableBeatportService) {

						if(enableBeatportService) {
							BeatportParserModel beatportCandidate = beatportRes.get(0);
							// SCARICA IL DETTAGLIO BEATPORT
							release = beatport.parseReleaseDetails(beatportCandidate, release);

							SymusicUtility.updateReleaseExtraction(extr,true,AreaExtraction.BEATPORT);
						}

						// SE NON E' UN GENERE INTERESSATO, NON VIENE SCARICATO IL DETTAGLIO SCENELOG
						boolean extractDetails = true;
						if(genreFilter != null && release.getGenre()!=null && !genreFilter.contains(release.getGenre().getName().toLowerCase())) {
							extractDetails = false;
						}

						if(extractDetails) {

							// DETTAGLIO SCENELOG
							if(enableScenelogService) {
								release = scenelog.parseReleaseDetails(sc, release);

								SymusicUtility.updateReleaseExtraction(extr,true,AreaExtraction.SCENELOG);
							}

							// YOUTUBE VIDEO
							YoutubeParser youtube = new YoutubeParser();
							if(enableYoutubeService) {
								List<VideoModel> youtubeVideos = youtube.searchYoutubeVideos(release.getName());
								release.setVideos(youtubeVideos);

								SymusicUtility.updateReleaseExtraction(extr,true,AreaExtraction.YOUTUBE);
							}

							// AGGIUNGE I LINK DI RICERCA MANUALE (DIRETTAMENTE SU GOOGLE E YOUTUBE)
							GoogleService google = new GoogleService();
							google.addManualSearchLink(release);
							youtube.addManualSearchLink(release); // link a youtube per la ricerca manuale

							listRelease.add(release);


							if(!isRecuperato) {
								// SALVA O RECUPERA IL GENERE
								if(release.getGenre()!=null)
									release.setGenre(new GenreService().saveGenre(release.getGenre()));

								ReleaseModel r = relServ.saveRelease(release);
								release.setId(r.getId());
								log.info(release+" e' stata salvata sul database con id = "+r.getId());
							}
							if(enableYoutubeService) {
								VideoService vidServ = new VideoService();
								vidServ.saveVideos(release.getVideos(), release.getId());
							} if(enableScenelogService) {
								TrackService traServ = new TrackService();
								traServ.saveTracks(release.getTracks(), release.getId());
							}

							// AGGIORNA/SALVA I FLAG DI ESTRAZIONE
							extr.setIdRelease(release.getId());
							new ReleaseExtractionService().saveReleaseExtraction(extr);
							log.info("Salvataggio del release extraction con idRelease="+release.getId());


						}

					}
				}

				*/
			} catch (Exception e) {
				throw new ParseReleaseException("Errore nel parsing delle pagine",e);
			}


			return listRelease;

	}

	public Map<String,String> getGenreList() throws ParseReleaseException {


		try {

			BeatportParser parser = new BeatportParser();
			return parser.getAllGenre();

		} catch (Exception e) {
			log.error("[BEATPORT] Errore nel recupero della lista dei generi");
			throw new ParseReleaseException("[BEATPORT] Errore nel recupero della lista dei generi",e);
		}
	}

	protected String applyFilterSearch(String t) {
		return t.replace(" ", "+");
	}

	public static void main(String[] args) throws IOException, ParseReleaseException, BackEndException {
		ReleaseBeatportService s = new ReleaseBeatportService(1L);
		ReleaseModel r = new ReleaseModel();
		r.setName("Modana Feat. Tay Edwards-Dance The Night Away-WEB-2013-UKHx");
		r.setNameWithUnderscore("Modana_Feat._Tay_Edwards-Dance_The_Night_Away-WEB-2013-UKHx");
//		r.setName("Cyberfactory - Into The Light-WEB-2013-ZzZz");
//		r.setName("Pepe and Shehu feat Morgana - Summer Love-(SYLIFE 167)-WEB-2013-ZzZz");
//		s.parseBeatport(r);

		List<ReleaseModel> res = s.parseBeatportRelease("http://www.beatport.com//genre/trance/7", "Trance");
		System.out.println(res);
	}

}
