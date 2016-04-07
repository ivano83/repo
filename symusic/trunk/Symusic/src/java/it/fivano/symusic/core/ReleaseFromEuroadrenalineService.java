package it.fivano.symusic.core;

import it.fivano.symusic.SymusicUtility;
import it.fivano.symusic.backend.service.ReleaseService;
import it.fivano.symusic.conf.EuroadrenalineConf;
import it.fivano.symusic.core.parser.BeatportParser;
import it.fivano.symusic.core.parser.EuroadrenalineParser;
import it.fivano.symusic.core.parser.ScenelogParser;
import it.fivano.symusic.core.parser.YoutubeParser;
import it.fivano.symusic.core.parser.model.BaseReleaseParserModel;
import it.fivano.symusic.core.parser.model.BeatportParserModel;
import it.fivano.symusic.core.thread.SupportObject;
import it.fivano.symusic.exception.BackEndException;
import it.fivano.symusic.exception.ParseReleaseException;
import it.fivano.symusic.model.ReleaseExtractionModel;
import it.fivano.symusic.model.ReleaseExtractionModel.AreaExtraction;
import it.fivano.symusic.model.ReleaseModel;
import it.fivano.symusic.model.VideoModel;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class ReleaseFromEuroadrenalineService extends ReleaseSiteService {
	
	private EuroadrenalineConf conf;
	private String genre;
	
	private static int MAX_CONSECUTIVE_FAILS = 20;
	
	private List<ReleaseModel> listRelease;
	
	private static int pageGap = 50;
	
	
	public ReleaseFromEuroadrenalineService(Long idUser) throws IOException {
		super();
		this.idUser = idUser;
		conf = new EuroadrenalineConf();
		enableBeatportService = true;
		enableScenelogService = true;
		enableYoutubeService = true;
		excludeRipRelease = true;
		this.setLogger(getClass());
	}
	
	
	public List<ReleaseModel> parsePresceneRelease(String genere, int numPagine) throws BackEndException, ParseReleaseException {
		
		this.genre = genere;
		listRelease = new ArrayList<ReleaseModel>();
		try {
			int count = 0;
			
			// PAGINA DI INIZIO
			String urlConn = null;
			EuroadrenalineParser music = new EuroadrenalineParser();
			List<BaseReleaseParserModel> resZero = new ArrayList<BaseReleaseParserModel>();
			while(count!=numPagine) {
				count++;
				
				if(count==1)
					urlConn = conf.URL+genere.toLowerCase();
				else
					urlConn = conf.URL+conf.PARAMS_PAGE.replace("{0}", genere.toLowerCase())+count;
				
				resZero.addAll(music.parseFullPage(urlConn,genere));
				
				
			}

			// init dei parser
			BeatportParser beatport = new BeatportParser();
			ScenelogParser scenelog = new ScenelogParser();
			YoutubeParser youtube = new YoutubeParser();
			GoogleService google = new GoogleService();
			ReleaseLinkService linkService = new ReleaseLinkService();
			List<BeatportParserModel> beatportRes = null;
			count = 0;
			for(BaseReleaseParserModel sc : resZero) {

				count++;
				ReleaseModel release = new ReleaseModel();
				
				release.setNameWithUnderscore(sc.getReleaseName());
				if(excludeRipRelease && this.isRadioRipRelease(release)) {
					log.info(sc.getReleaseName()+" ignorata poichè è un RIP");
					continue;
				}
				
				if(excludeVA && this.isVARelease(release)) {
					log.info(sc.getReleaseName()+" ignorata poichè è una VA");
					continue;
				}

				enableScenelogService = true;
				enableYoutubeService = true;
//				enableBeatportService = true;
				ReleaseExtractionModel extr = new ReleaseExtractionModel();
				release.setReleaseExtraction(extr);

				// CONTROLLA SE LA RELEASE E' GIA' PRESENTE
				boolean isRecuperato = false;
				ReleaseService relServ = new ReleaseService();
				ReleaseModel relDb = relServ.getReleaseFull(sc.getReleaseName(), idUser);
				if(relDb!=null) {
					log.info(sc.getReleaseName()+" e' gia' presente nel database con id = "+relDb.getId());

					// controlla sul db se le varie estrazioni hanno avuto successo
					extr = relDb.getReleaseExtraction();

					if(extr!=null ){
						enableScenelogService = !extr.getScenelog();
						enableYoutubeService = !extr.getYoutube();
						enableBeatportService = !extr.getBeatport() && enableBeatportService;
						log.info(sc.getReleaseName()+" dati estrazione --> "+extr);

					}
					isRecuperato = true;
					release = relDb; // SOSTITUISCE I DATI FINO AD ORA ESTRATTI CON QUELLI DEL DB
				}
				else {
					release = this.arricchisciRelease(sc, release);
					SymusicUtility.processReleaseName(release);
				}
				
//				if(!this.verificaAnnoRelease(release,annoDa,annoAl)) {
//					log.info(sc.getReleaseName()+" ignorata poichè l'anno non è all'interno del range.");
//					continue;
//				}

				if(enableBeatportService)
					beatportRes = beatport.searchRelease(release.getName());
				else
					beatportRes = new ArrayList<BeatportParserModel>();

				if(!beatportRes.isEmpty() && enableBeatportService) {
					BeatportParserModel beatportCandidate = beatportRes.get(0);
					// SCARICA IL DETTAGLIO BEATPORT
					release = beatport.parseReleaseDetails(beatportCandidate, release);
				}

				// DETTAGLIO SCENELOG
				if(scenelog.getCountFailConnection()>MAX_CONSECUTIVE_FAILS)
					log.warn("Il sito 'Scenelog' sembrerebbe al momento non raggiungibile... verra' di seguito disabilitata la ricerca.");
				if(enableScenelogService && scenelog.getCountFailConnection()<=MAX_CONSECUTIVE_FAILS) {
					
					release = linkService.searchlink(release);
					
				}

				// YOUTUBE VIDEO
				if(enableYoutubeService) {
					List<VideoModel> youtubeVideos = youtube.searchYoutubeVideos(release.getName());
					release.setVideos(youtubeVideos);

					if(release.getVideos().isEmpty()) 
						SymusicUtility.updateReleaseExtraction(extr,false,AreaExtraction.YOUTUBE);
					else
						SymusicUtility.updateReleaseExtraction(extr,true,AreaExtraction.YOUTUBE);
				}

				listRelease.add(release);

				// AGGIORNAMENTI DEI DATI SUL DB
				this.saveOrUpdateRelease(release, isRecuperato);


				// AGGIUNGE I LINK DI RICERCA MANUALE (DIRETTAMENTE SU GOOGLE E YOUTUBE)
				
				google.addManualSearchLink(release);
				youtube.addManualSearchLink(release); // link a youtube per la ricerca manuale
				
				log.info("********* Processate "+count+" release su "+resZero.size()+"*********");


			}


			// INIT OGGETTO DI SUPPORTO UNICO PER TUTTI I THREAD
			SupportObject supp = new SupportObject();
			supp.setEnableBeatportService(enableBeatportService);
			supp.setEnableScenelogService(enableScenelogService);
			supp.setEnableYoutubeService(enableYoutubeService);

		} catch (Exception e) {
			throw new ParseReleaseException("Errore nel parsing delle pagine",e);
		} 

		return listRelease;
		
	}
	
	private ReleaseModel arricchisciRelease(BaseReleaseParserModel sc, ReleaseModel release) throws ParseException {
		release.setCrew(sc.getCrew());
		release.setGenre(SymusicUtility.creaGenere(sc.getGenre()));
		if(release.getReleaseDate()==null)
			release.setReleaseDate(SymusicUtility.getStandardDate(sc.getReleaseDate()));
		return release;
	}


	protected boolean isRadioRipRelease(ReleaseModel release) {
		
		ReleaseModel r = new ReleaseModel();
		r.setNameWithUnderscore(release.getNameWithUnderscore().replace(" ", "-"));

		return super.isRadioRipRelease(r);
	}

	

	private String genericFilter(String text) {
		if(text!=null) {
			text = text.replaceAll("[()]", "").trim();
			if(!String.valueOf(text.toCharArray()[0]).matches("[a-zA-Z0-9]")) {
				return text.substring(1);
			}
		}
		return null;
	}


	public static void main(String[] args) throws IOException, ParseException, BackEndException, ParseReleaseException {
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
//		Date da = sdf.parse("20150215");
//		Date a = sdf.parse("20150216");
		
		ReleaseFromEuroadrenalineService s = new ReleaseFromEuroadrenalineService(1L);
		List<ReleaseModel> res = s.parsePresceneRelease("House",2);
//		List<ReleaseModel> res = s.parseMusicDLRelease("trance",da,a);
		for(ReleaseModel r : res)
			System.out.println(r);
//		System.out.println(s.genericFilter("fhfh( dewdef) fef"));
//		System.out.println(s.genericFilter("fhfh( dewdef) fef"));
//		System.out.println("fhfh( dewdef) fef".replaceAll("[()]", ""));
	}



	@Override
	protected String applyFilterSearch(String result) {
		return result;
	}

	
}
