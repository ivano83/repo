package it.fivano.symusic.core;

import it.fivano.symusic.SymusicUtility;
import it.fivano.symusic.backend.service.GenreService;
import it.fivano.symusic.backend.service.ReleaseService;
import it.fivano.symusic.conf.ZeroDayMp3Conf;
import it.fivano.symusic.core.parser.BeatportParser;
import it.fivano.symusic.core.parser.ScenelogParser;
import it.fivano.symusic.core.parser.YoutubeParser;
import it.fivano.symusic.core.parser.ZeroDayMp3Parser;
import it.fivano.symusic.core.parser.model.BaseReleaseParserModel;
import it.fivano.symusic.core.parser.model.BeatportParserModel;
import it.fivano.symusic.core.parser.model.ScenelogParserModel;
import it.fivano.symusic.core.parser.model.ZeroDayMp3ParserModel;
import it.fivano.symusic.core.thread.SupportObject;
import it.fivano.symusic.exception.BackEndException;
import it.fivano.symusic.exception.ParseReleaseException;
import it.fivano.symusic.model.GenreModel;
import it.fivano.symusic.model.ReleaseExtractionModel;
import it.fivano.symusic.model.ReleaseModel;
import it.fivano.symusic.model.VideoModel;
import it.fivano.symusic.model.ReleaseExtractionModel.AreaExtraction;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Release0DayMp3Service extends ReleaseSiteService {

	private ZeroDayMp3Conf conf;
	private String genre;

	private List<ReleaseModel> listRelease;



	public Release0DayMp3Service(Long idUser) throws IOException {
		super();
		this.idUser = idUser;
		conf = new ZeroDayMp3Conf();
		enableBeatportService = true;
		enableScenelogService = true;
		enableYoutubeService = true;
		excludeRipRelease = true;
		this.setLogger(getClass());
	}


	public List<ReleaseModel> parse0DayMp3Release(String genere, Date da, Date a) throws BackEndException, ParseReleaseException {

		this.genre = genere;
		listRelease = new ArrayList<ReleaseModel>();
		try {

			// PAGINA DI INIZIO
			String urlConn = conf.URL_CATEGORY+genere;

			// OGGETTO PER GESTIRE IL CARICAMENTO DELLE PAGINE SUCCESSIVE DEL SITO
			ScenelogInfo info = new ScenelogInfo();
			info.setProcessNextPage(true);
			info.setA(a);
			info.setDa(da);

			// PROCESSA LE RELEASE DELLA PRIMA PAGINA
			ZeroDayMp3Parser zero = new ZeroDayMp3Parser();
			List<ZeroDayMp3ParserModel> resZero = zero.parseFullPage(urlConn, da, a);
			this.checkProcessPage(resZero, info);

			// SE C'È DA RECUPERARE ALTRE RELEASE, CAMBIA PAGINA
			while(info.isProcessNextPage()) {

				// SALVA LA URL DELLA PROSSIMA PAGINA (SE NECESSARIA)
				info.changePage(); // AGGIORNA IL NUMERO PAGINA
				info.setNextPage(this.extractNextPage(info));

				log.info("Andiamo alla pagina successiva...");
				// PROCESSA LE RELEASE DELLE PAGINE SUCCESSIVE
				List<ZeroDayMp3ParserModel> resZeroTmp = zero.parseFullPage(info.getNextPage(), da, a);
				this.checkProcessPage(resZeroTmp, info);
				resZero.addAll(resZeroTmp);

			}

			// per ogni release scenelog recupera i dati da beatport
			BeatportParser beatport = new BeatportParser();
			List<BeatportParserModel> beatportRes = null;
			int count = 0;
			for(ZeroDayMp3ParserModel sc : resZero) {

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

				release = zero.parseReleaseDetails(sc, release);

				if(!this.verificaAnnoRelease(release,annoDa,annoAl)) {
					log.info(sc.getReleaseName()+" ignorata poichè l'anno non è all'interno del range.");
					continue;
				}

				if(enableBeatportService)
					beatportRes = beatport.searchRelease(sc.getReleaseName());
				else
					beatportRes = new ArrayList<BeatportParserModel>();

				if(!beatportRes.isEmpty() && enableBeatportService) {
					BeatportParserModel beatportCandidate = beatportRes.get(0);
					// SCARICA IL DETTAGLIO BEATPORT
					release = beatport.parseReleaseDetails(beatportCandidate, release);
				}

				// DETTAGLIO SCENELOG
				if(enableScenelogService) {
					ScenelogParser scenelog = new ScenelogParser();
					BaseReleaseParserModel releaseScenelog = scenelog.searchRelease(sc.getReleaseName());
					release = scenelog.parseReleaseDetails(releaseScenelog, release);
				}

				// YOUTUBE VIDEO
				YoutubeParser youtube = new YoutubeParser();
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
				GoogleService google = new GoogleService();
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


	private void checkProcessPage(List<ZeroDayMp3ParserModel> resScenelog, ScenelogInfo info) {
		Date max = null;
		Date min = null;
		if(!resScenelog.isEmpty()) {
			Iterator<ZeroDayMp3ParserModel> it = resScenelog.iterator();
			while(it.hasNext()) {
				ZeroDayMp3ParserModel sc = it.next();

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

			// la pagina ha superato il range scelto
			if(min.before(info.getDa())) {
				info.setProcessNextPage(false);
			}

		}

	}


	private void parse0DayMp3(String urlConn, Date da, Date a, ScenelogInfo info) throws Exception {

//		List<ReleaseModel> listRelease = new ArrayList<ReleaseModel>();

		if(urlConn == null)
			return;


		// CONNESSIONE ALLA PAGINA
		log.info("Connessione in corso --> "+urlConn);
		String userAgent = this.randomUserAgent();
		Document doc = Jsoup.connect(urlConn).timeout(TIMEOUT).userAgent(userAgent).ignoreHttpErrors(true).get();

		if(antiDDOS.isAntiDDOS(doc)) {
			doc = this.bypassAntiDDOS(doc, conf.URL, urlConn);
		}


		// SALVA LA URL DELLA PROSSIMA PAGINA (SE NECESSARIA)
		info.changePage(); // AGGIORNA IL NUMERO PAGINA
		info.setNextPage(this.extractNextPage(info));

		Elements releaseGroup = doc.getElementsByAttributeValue("id",conf.ID_CONTENT);
		if(releaseGroup.size()>0) {
			ReleaseModel release = null;
			// OGNI TABLE CONTIENE UNA RELEASE
			Elements tables = releaseGroup.get(0).getElementsByTag("table");
			log.info("####################################");
			for(Element relTable : tables) {

				Elements components = relTable.getElementsByTag("td");
				if(components.size()>=4) {
					// OK CI SONO TUTTI I PEZZI

					// IN QUARTA POSIZIONE C'E' LA DATA RELEASE
					Element dateComp = components.get(3);
					String date = this.genericFilter(dateComp.text());
					String dateIn = this.getStandardDateFormat(date);
					Date dateInDate = SymusicUtility.getStandardDate(dateIn);

					// BISOGNA RECUPERARE ANCORA ALTRI GIORNI DI RELEASE?
					if(da.compareTo(dateInDate)>0)
						info.setProcessNextPage(false);

					// RANGE DATA, SOLO LE RELEASE COMPRESE DA - A
					if(!this.downloadReleaseDay(dateInDate, da, a)) {
						continue;
					}

					release = new ReleaseModel();

					// IN PRIMA POSIZIONE C'È IL NOME RELEASE E IL LINK
					Element relComp = components.get(0);
					// RELEASE NAME
					String title = relComp.getElementsByTag("a").get(0).attr("title");
					title = title.replace("Permalink to ","");
					release.setName(title.replace("_", " "));
					release.setNameWithUnderscore(title.replace(" ", "_"));

					if(excludeRipRelease && this.isRadioRipRelease(release)) {
						continue;
					}

					// IN TERZA POSIZIONE C'È IL GENERE
					Element genreComp = components.get(2);
					String genre = this.genericFilter(genreComp.text());
					GenreModel genere = new GenreModel();
					genere.setName(genre);
					// RECUPERO/SALVATAGGIO DB DEL GENERE
					genere = new GenreService().saveGenre(genere);
					release.setGenre(genere);

					// DATE RELEASE
					release.setReleaseDate(dateIn);

					// AGGIUNGE ULTERIORI INFO DELLA RELEASE A PARTIRE DAL NOME
					// ES. CREW E ANNO RELEASE
					SymusicUtility.processReleaseName(release);

					// SE E' GIA' PRESENTE IL LISTA, PRENDE QUELLA
					ReleaseModel relInList = this.verificaPresenzaInLista(release);
					if(relInList!=null) {

						release = relInList;
						// LINK
						release.addLink(SymusicUtility.popolateLink(relComp.getElementsByTag("a").get(0)));

						log.info("|"+release+"| fusa con quella gia' presente");
						log.info("####################################");

					} else {

						// LINK
						release.addLink(SymusicUtility.popolateLink(relComp.getElementsByTag("a").get(0)));


						log.info("|"+release+"| acquisita");
						log.info("####################################");

						listRelease.add(release);

					}



				}

			}

		}

//		return listRelease;


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

		return conf.URL_CATEGORY+genre+conf.PARAMS_PAGE+info.getNumPagina();

	}


	private boolean downloadReleaseDay(Date dateInDate, Date da, Date a) {
		boolean result = (dateInDate.compareTo(da)>=0) && (dateInDate.compareTo(a)<=0);
		return result;
	}


	private String getStandardDateFormat(String dateIn) throws ParseException {

		String zeroDayFormat = conf.DAY_FORMAT;

		return SymusicUtility.getStandardDateFormat(dateIn, zeroDayFormat);

	}

	public static void main(String[] args) throws IOException, ParseException, BackEndException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		Date da = sdf.parse("20130802");
		Date a = sdf.parse("20130803");

		Release0DayMp3Service s = new Release0DayMp3Service(1L);
//		List<ReleaseModel> res = s.parse0DayMusicRelease("trance",da,a);
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

class ZeroDayMp3Info {

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
