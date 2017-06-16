package it.fivano.symusic.core;

import it.fivano.symusic.SymusicUtility;
import it.fivano.symusic.backend.service.GenreService;
import it.fivano.symusic.backend.service.LinkService;
import it.fivano.symusic.backend.service.ReleaseExtractionService;
import it.fivano.symusic.backend.service.ReleaseService;
import it.fivano.symusic.backend.service.TrackService;
import it.fivano.symusic.backend.service.VideoService;
import it.fivano.symusic.conf.ScenelogConf;
import it.fivano.symusic.conf.ZeroDayMp3Conf;
import it.fivano.symusic.core.parser.BeatportParser;
import it.fivano.symusic.core.parser.ScenelogParser;
import it.fivano.symusic.core.parser.YoutubeParser;
import it.fivano.symusic.core.parser.model.BaseReleaseParserModel;
import it.fivano.symusic.core.parser.model.BeatportParserModel;
import it.fivano.symusic.core.parser.model.ScenelogParserModel;
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
import java.util.Locale;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ReleaseScenelogService extends ReleaseSiteService {

	private ScenelogConf conf;
	private List<String> genreFilter;

	private List<ReleaseModel> listRelease;



	public ReleaseScenelogService(Long idUser) throws IOException {
		super();
		conf = new ScenelogConf();
		this.idUser = idUser;
		enableBeatportService = true;
		enableScenelogService = true;
		enableYoutubeService = true;
		excludeRipRelease = true;
		this.setLogger(getClass());
	}

	public ReleaseScenelogService(List<String> genreFilter, Long idUser) throws IOException {
		this(idUser);
		this.genreFilter = genreFilter;
	}


	public List<ReleaseModel> parseScenelogRelease(Date da, Date a) throws BackEndException, ParseReleaseException {

		listRelease = new ArrayList<ReleaseModel>();
		try {


			// PAGINA DI INIZIO
			String urlConn = conf.URL_MUSIC;

			// OGGETTO PER GESTIRE IL CARICAMENTO DELLE PAGINE SUCCESSIVE DEL SITO
			ScenelogInfo info = new ScenelogInfo();
			info.setProcessNextPage(true);
			info.setA(a);
			info.setDa(da);

			// PROCESSA LE RELEASE DELLA PRIMA PAGINA
			ScenelogParser scenelog = new ScenelogParser();
			List<BaseReleaseParserModel> resScenelog = scenelog.parseFullPage(urlConn, da, a);
			this.checkProcessPage(resScenelog, info);

			// SE C'È DA RECUPERARE ALTRE RELEASE, CAMBIA PAGINA
			while(info.isProcessNextPage()) {

				// SALVA LA URL DELLA PROSSIMA PAGINA (SE NECESSARIA)
				info.changePage(); // AGGIORNA IL NUMERO PAGINA
				info.setNextPage(this.extractNextPage(info));

				log.info("Andiamo alla pagina successiva...");
				// PROCESSA LE RELEASE DELLE PAGINE SUCCESSIVE
				List<BaseReleaseParserModel> resScenelogTmp = scenelog.parseFullPage(info.getNextPage(), da, a);
				this.checkProcessPage(resScenelogTmp, info);
				resScenelog.addAll(resScenelogTmp);

			}


			// per ogni release scenelog recupera i dati da beatport
			BeatportParser beatport = new BeatportParser();
			List<BeatportParserModel> beatportRes = null;
			for(BaseReleaseParserModel sc : resScenelog) {

				ReleaseModel release = new ReleaseModel();

				release.setNameWithUnderscore(sc.getReleaseName());
				if(excludeRipRelease && this.isRadioRipRelease(release)) {
					continue;
				}

				enableScenelogService = true;
				enableYoutubeService = true;
				enableBeatportService = true;
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
						enableBeatportService = !extr.getBeatport();
					}
					isRecuperato = true;
					release = relDb; // SOSTITUISCE I DATI FINO AD ORA ESTRATTI CON QUELLI DEL DB
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
					}

					// SE NON E' UN GENERE INTERESSATO, NON VIENE SCARICATO IL DETTAGLIO SCENELOG
					boolean extractDetails = true;
					if(genreFilter != null && release.getGenre()!=null && !genreFilter.contains(release.getGenre().getName().toLowerCase())) {
						extractDetails = false;
					}

					YoutubeParser youtube = new YoutubeParser();
					if(extractDetails) {

						// DETTAGLIO SCENELOG
						if(enableScenelogService) {
							release = scenelog.parseReleaseDetails(sc, release);

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

					}

					// AGGIUNGE I LINK DI RICERCA MANUALE (DIRETTAMENTE SU GOOGLE E YOUTUBE)
					GoogleService google = new GoogleService();
					google.addManualSearchLink(release);
					youtube.addManualSearchLink(release); // link a youtube per la ricerca manuale

				}
			}


			// INIT OGGETTO DI SUPPORTO UNICO PER TUTTI I THREAD
			SupportObject supp = new SupportObject();
			supp.setEnableBeatportService(enableBeatportService);
			supp.setEnableScenelogService(enableScenelogService);
			supp.setEnableYoutubeService(enableYoutubeService);

//			listRelease = this.arricchimentoRelease(listRelease, supp);

		} catch (Exception e) {
			throw new ParseReleaseException("Errore nel parsing delle pagine",e);
		}

		return listRelease;

	}

	private void checkProcessPage(List<BaseReleaseParserModel> resScenelog, ScenelogInfo info) {
		Date max = null;
		Date min = null;
		if(!resScenelog.isEmpty()) {
			Iterator<BaseReleaseParserModel> it = resScenelog.iterator();
			while(it.hasNext()) {
				BaseReleaseParserModel sc = it.next();

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
//				System.out.println(min + "   "+max);
			}


			// la pagina ha superato il range scelto
			if(min.before(info.getDa())) {
				info.setProcessNextPage(false);
			}

			// ancora non si e' arrivati al range scelto, si prosegue con pagine successive
//			if(max.before(info.getA())) {
//				info.setProcessNextPage(true);
//			}

		}


	}


	private void parseScenelog(String urlConn, Date da, Date a, ScenelogInfo info) throws BackEndException, ParseReleaseException, ParseException, IOException {

//		List<ReleaseModel> listRelease = new ArrayList<ReleaseModel>();

		if(urlConn == null)
			return;

		// CONNESSIONE ALLA PAGINA
		String userAgent = this.randomUserAgent();
		log.info("Connessione in corso --> "+urlConn);
		Document doc = Jsoup.connect(urlConn).timeout(TIMEOUT).userAgent(userAgent).get();

		// SALVA LA URL DELLA PROSSIMA PAGINA (SE NECESSARIA)
		info.changePage(); // AGGIORNA IL NUMERO PAGINA
		info.setNextPage(this.extractNextPage(info));

		Elements releaseGroup = doc.getElementsByClass(conf.CLASS_RELEASE_LIST_ITEM);
		if(releaseGroup.size()>0) {
			ReleaseModel release = null;
			log.info("####################################");
			for(Element tmp : releaseGroup) {

				Element releaseItem = tmp.getElementsByTag("h1").get(0).getElementsByTag("a").get(0);
				String releaseName = releaseItem.text();
				String releaseUrl = releaseItem.attr("href");

				Element relDate = tmp.getElementsByClass(conf.CLASS_RELEASE_LIST_DATA).get(0);
				String dateIn = relDate.text();
				dateIn = this.getStandardDateFormat(dateIn);
				Date dateInDate = SymusicUtility.getStandardDate(dateIn);

				// BISOGNA RECUPERARE ANCORA ALTRI GIORNI DI RELEASE?
				if(da.compareTo(dateInDate)>0)
					info.setProcessNextPage(false);

				// RANGE DATA, SOLO LE RELEASE COMPRESE DA - A
				if(!this.downloadReleaseDay(dateInDate, da, a)) {
					continue;
				}


				release = new ReleaseModel();
				release.setName(releaseName.replace("_", " "));
				release.setNameWithUnderscore(releaseName.replace(" ", "_"));

				if(excludeRipRelease && this.isRadioRipRelease(release)) {
					continue;
				}

				System.out.println(dateIn+" - "+ releaseUrl);

				// RELEASE DATE
				release.setReleaseDate(dateIn);

				// AGGIUNGE ULTERIORI INFO DELLA RELEASE A PARTIRE DAL NOME
				// ES. CREW E ANNO RELEASE
				SymusicUtility.processReleaseName(release);

				log.info("|"+release+"| acquisita");
				log.info("####################################");

				listRelease.add(release);


//
//
//				// CONTROLLA SE LA RELEASE E' GIA' PRESENTE
//				boolean isRecuperato = false;
//				ReleaseService relServ = new ReleaseService();
//				ReleaseModel relDb = relServ.getReleaseFull(release.getNameWithUnderscore());
//				if(relDb!=null) {
//					log.info(release+" e' gia' presente nel database con id = "+relDb.getId());
//
//					// controlla sul db se le varie estrazioni hanno avuto successo
//					ReleaseExtractionModel extr = relDb.getReleaseExtraction();
//
//					if(extr!=null ){
//						enableScenelogService = !extr.getScenelog();
//						enableYoutubeService = !extr.getYoutube();
////						enableBeatportService = extr.getBeatport();
//					}
//					isRecuperato = true;
//					release = relDb; // SOSTITUISCE I DATI FINO AD ORA ESTRATTI CON QUELLI DEL DB
//				}
//
//				// OGGETTO CHE CONTIENE I FLAG PER ESTRARRE O MENO DETERMINATI DATI
//				ReleaseExtractionModel relExtr = (release.getReleaseExtraction()==null)? new ReleaseExtractionModel() : release.getReleaseExtraction();
//
//
//
//				// ########## BEATPORT ############
//				try {
//					if(enableBeatportService) {
//						// recupera dati da beatport per il dettaglio della release
//						BeatportService beatport = new BeatportService();
//						boolean res = beatport.parseBeatport(release);
//
//						SymusicUtility.updateReleaseExtraction(relExtr,res,AreaExtraction.BEATPORT);
//					}
//
//				} catch (ParseReleaseException e1) {
//					log.warn("BeatportService fallito! " + e1.getMessage());
//					relExtr.setBeatport(false);
//				}

				//

				/*
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
*/
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

		return conf.URL_MUSIC+"page/"+info.getNumPagina();

	}


	private boolean downloadReleaseDay(Date dateInDate, Date da, Date a) {
		boolean result = (dateInDate.compareTo(da)>=0) && (dateInDate.compareTo(a)<=0);
		return result;
	}


	private String getStandardDateFormat(String dateIn) throws ParseException {

		String zeroDayFormat = conf.DATE_FORMAT;

		dateIn = dateIn.replace("th,", "").replace("rd,", "").replace("st,", "");

		return SymusicUtility.getStandardDateFormat(dateIn, zeroDayFormat);

	}

	public static void main(String[] args) throws IOException, ParseException, BackEndException, ParseReleaseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		Date da = sdf.parse("20130802");
		Date a = sdf.parse("20130803");

		ReleaseScenelogService s = new ReleaseScenelogService(1L);
		s.parseScenelogRelease(da, a);
//		List<ReleaseModel> res = s.parse0DayMusicRelease("trance",da,a);
//		for(ReleaseModel r : res)
//			System.out.println(r);
		System.out.println(s.genericFilter("fhfh( dewdef) fef"));
		System.out.println(s.genericFilter("fhfh( dewdef) fef"));
		System.out.println("fhfh( dewdef) fef".replaceAll("[()]", ""));

		sdf = new SimpleDateFormat("MMMMM d yyyy", new Locale("English"));
		System.out.println(sdf.parse("December 12 2013"));


	}



	@Override
	protected String applyFilterSearch(String result) {
		return result;
	}


}

class ScenelogInfo {

	private int numPagina = 0;
	private String nextPage;
	private boolean processNextPage;
	private Date da;
	private Date a;

	public void changePage() {
		numPagina = numPagina + 1;
	}

	public void changePage(int gap) {
		numPagina = numPagina + gap;
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

	public Date getDa() {
		return da;
	}

	public void setDa(Date da) {
		this.da = da;
	}

	public Date getA() {
		return a;
	}

	public void setA(Date a) {
		this.a = a;
	}




}
