package it.fivano.symusic.core;

import it.fivano.symusic.SymusicUtility;
import it.fivano.symusic.backend.service.ReleaseService;
import it.fivano.symusic.conf.MusicDLConf;
import it.fivano.symusic.core.parser.BeatportParser;
import it.fivano.symusic.core.parser.MusicDLParser;
import it.fivano.symusic.core.parser.ScenelogParser;
import it.fivano.symusic.core.parser.YoutubeParser;
import it.fivano.symusic.core.parser.model.BeatportParserModel;
import it.fivano.symusic.core.parser.model.BaseMusicParserModel;
import it.fivano.symusic.core.parser.model.BaseReleaseParserModel;
import it.fivano.symusic.core.parser.model.ScenelogParserModel;
import it.fivano.symusic.core.thread.SupportObject;
import it.fivano.symusic.exception.BackEndException;
import it.fivano.symusic.exception.ParseReleaseException;
import it.fivano.symusic.model.ReleaseExtractionModel;
import it.fivano.symusic.model.ReleaseExtractionModel.AreaExtraction;
import it.fivano.symusic.model.ReleaseModel;
import it.fivano.symusic.model.VideoModel;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class ReleaseMusicDLService extends ReleaseSiteService {

	private MusicDLConf conf;
	private String genre;

	private static int MAX_CONSECUTIVE_FAILS = 20;

	private List<ReleaseModel> listRelease;



	public ReleaseMusicDLService(Long idUser) throws IOException {
		super();
		this.idUser = idUser;
		conf = new MusicDLConf();
		enableBeatportService = true;
		enableScenelogService = true;
		enableYoutubeService = true;
		excludeRipRelease = true;
		this.setLogger(getClass());
	}


	public List<ReleaseModel> parseMusicDLRelease(String genere, Date da, Date a) throws BackEndException, ParseReleaseException {

		this.genre = genere;
		listRelease = new ArrayList<ReleaseModel>();
		try {

			// PAGINA DI INIZIO
			String urlConn = conf.URL_MUSIC+genere;

			// OGGETTO PER GESTIRE IL CARICAMENTO DELLE PAGINE SUCCESSIVE DEL SITO
			ScenelogInfo info = new ScenelogInfo();
			info.setProcessNextPage(true);
			info.setA(a);
			info.setDa(da);

			// PROCESSA LE RELEASE DELLA PRIMA PAGINA
			MusicDLParser music = new MusicDLParser();
			List<BaseMusicParserModel> resZero = music.parseFullPage(urlConn, da, a);
			this.checkProcessPage(resZero, info);

			// SE C'È DA RECUPERARE ALTRE RELEASE, CAMBIA PAGINA
			while(info.isProcessNextPage()) {

				// SALVA LA URL DELLA PROSSIMA PAGINA (SE NECESSARIA)
				info.changePage(); // AGGIORNA IL NUMERO PAGINA
				info.setNextPage(this.extractNextPage(info));

				log.info("Andiamo alla pagina successiva...");
				// PROCESSA LE RELEASE DELLE PAGINE SUCCESSIVE
				List<BaseMusicParserModel> resZeroTmp = music.parseFullPage(info.getNextPage(), da, a);
				this.checkProcessPage(resZeroTmp, info);
				resZero.addAll(resZeroTmp);

			}

			// init dei parser
			BeatportParser beatport = new BeatportParser();
			ScenelogParser scenelog = new ScenelogParser();
			YoutubeParser youtube = new YoutubeParser();
			GoogleService google = new GoogleService();
			List<BeatportParserModel> beatportRes = null;
			int count = 0;
			for(BaseMusicParserModel sc : resZero) {

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

				release = music.parseReleaseDetails(sc, release);

				if(!this.verificaAnnoRelease(release,annoDa,annoAl)) {
					log.info(sc.getReleaseName()+" ignorata poichè l'anno non è all'interno del range.");
					continue;
				}

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

					BaseReleaseParserModel releaseScenelog = scenelog.searchRelease(release.getName());
					release = scenelog.parseReleaseDetails(releaseScenelog, release);
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

	protected boolean isRadioRipRelease(ReleaseModel release) {

		ReleaseModel r = new ReleaseModel();
		r.setNameWithUnderscore(release.getNameWithUnderscore().replace(" ", "-"));

		return super.isRadioRipRelease(r);
	}

	private boolean verificaAnnoRelease(ReleaseModel release, String annoDa, String annoAl) {
		if(release.getYear()!=null) {

			try {
				Integer annoRel = Integer.parseInt(release.getYear());
				Integer da = Integer.parseInt(annoDa);
				Integer a = Integer.parseInt(annoAl);

				if(annoRel<=a && annoRel>=da) {
					return true;
				}
			} catch(Exception e) {
				log.error("Errore nella verifica dell'anno release. "+e.getMessage());
				return true; // anno non recuperato... per defualt la release è considerata
			}

			return false;
		}
		else
			return true; // anno non recuperato... per defualt la release è considerata


	}


	private void checkProcessPage(List<BaseMusicParserModel> resScenelog, ScenelogInfo info) {
		Date max = null;
		Date min = null;
		if(!resScenelog.isEmpty()) {
			Iterator<BaseMusicParserModel> it = resScenelog.iterator();
			while(it.hasNext()) {
				BaseMusicParserModel sc = it.next();

				if(max == null) max = sc.getReleaseDate();
				if(min == null) min = sc.getReleaseDate();

				if(sc.getReleaseDate().after(max)) {
					max = sc.getReleaseDate();
				}
				if(sc.getReleaseDate().before(min)) {
					min = sc.getReleaseDate();
				}

				if(!sc.isDateInRange()) {
					it.remove();
				}


			}

			Calendar c = Calendar.getInstance();
			c.setTime(info.getA());
			c.add(Calendar.DATE, 5);  // number of days to add
			if(c.getTime().before(min)) {
				info.changePage(4);
			}

			// la pagina ha superato il range scelto
			if(min.before(info.getDa())) {
				info.setProcessNextPage(false);
			}

		}

	}



	private ReleaseModel verificaPresenzaInLista(ReleaseModel release) {
		for(ReleaseModel r : listRelease) {
			if(r.equals(release)) {
				log.info("Release "+release+" e' gia' presente in lista, si effettuera' la fusione");
				return r;
			}
		}
		return null;
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


	private String extractNextPage(ScenelogInfo info) {

		return conf.URL_MUSIC+genre+conf.PARAMS_PAGE+info.getNumPagina();

	}


	private boolean downloadReleaseDay(Date dateInDate, Date da, Date a) {
		boolean result = (dateInDate.compareTo(da)>=0) && (dateInDate.compareTo(a)<=0);
		return result;
	}


	public static void main(String[] args) throws IOException, ParseException, BackEndException, ParseReleaseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		Date da = sdf.parse("20141117");
		Date a = sdf.parse("20141118");

		ReleaseMusicDLService s = new ReleaseMusicDLService(1L);
//		List<ReleaseModel> res = s.parseMusicDLRelease("trance",da,a);
//		for(ReleaseModel r : res)
//			System.out.println(r);
		System.out.println(s.genericFilter("fhfh( dewdef) fef"));
		System.out.println(s.genericFilter("fhfh( dewdef) fef"));
		System.out.println("fhfh( dewdef) fef".replaceAll("[()]", ""));
	}



	@Override
	protected String applyFilterSearch(String result) {
		return result;
	}


}

class MusicDLInfo {

	private int numPagina = 1;
	private String nextPage;
	private boolean processNextPage;

	public void changePage() {
		numPagina = numPagina + 1;
	}

	public int getNumPagina() {
		return numPagina;
	}

	public String getNextPage() {
		return nextPage;
	}
	public void setNextPage(String nextPage) {
		this.nextPage = nextPage;
	}
	public boolean isProcessNextPage() {
		return processNextPage;
	}
	public void setProcessNextPage(boolean processNextPage) {
		this.processNextPage = processNextPage;
	}




}
