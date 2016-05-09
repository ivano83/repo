package it.fivano.symusic.core.parser;

import it.fivano.symusic.SymusicUtility;
import it.fivano.symusic.SymusicUtility.LevelSimilarity;
import it.fivano.symusic.conf.EuroadrenalineConf;
import it.fivano.symusic.conf.ScenelogConf;
import it.fivano.symusic.core.parser.model.BaseReleaseParserModel;
import it.fivano.symusic.core.parser.model.ScenelogParserModel;
import it.fivano.symusic.exception.ParseReleaseException;
import it.fivano.symusic.model.ReleaseExtractionModel.AreaExtraction;
import it.fivano.symusic.model.ReleaseModel;
import it.fivano.symusic.model.TrackModel;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;

public class EuroadrenalineParser extends GenericParser {

	private EuroadrenalineConf conf;
	private int countFailConnection;

	public EuroadrenalineParser() throws IOException {
		conf = new EuroadrenalineConf();
		countFailConnection = 0;
		this.setLogger(getClass());
	}


	public List<BaseReleaseParserModel> parseFullPage(String urlPage, String genere) throws ParseReleaseException {

		List<BaseReleaseParserModel> result = new ArrayList<BaseReleaseParserModel>();

		if(urlPage == null)
			return result;


		try {
			// CONNESSIONE ALLA PAGINA
			String userAgent = this.randomUserAgent();
			log.info("Connessione in corso --> "+urlPage);
			Document doc = Jsoup.connect(urlPage).timeout(TIMEOUT).userAgent(userAgent).ignoreHttpErrors(true).get();

			if(antiDDOS.isAntiDDOS(doc)) {
				doc = this.bypassAntiDDOS(doc, conf.URL, urlPage,userAgent);
			}

			Elements releaseGroup = doc.getElementsByClass(conf.CLASS_RELEASE_LIST).get(0).getElementsByClass(conf.CLASS_RELEASE_ITEM);
			if(releaseGroup.size()>0) {
				BaseReleaseParserModel release = null;
				log.info("####################################");
				for(Element tmp : releaseGroup) {

					release = this.popolaEuroadrenalineRelease(tmp);

					release.setGenre(genere);

					// se contiene spazi, allora non è una release ma una singola versione di una traccia
					int count = release.getReleaseName().length() - release.getReleaseName().replace(" ", "").length();
					if(count<1)
						result.add(release);
				}

			}
		} catch (IOException e) {
			log.error("Errore IO", e);
			throw new ParseReleaseException("Errore IO",e);
		} catch (ParseException e) {
			log.error("Errore nel parsing", e);
			throw new ParseReleaseException("Errore nel parsing",e);
		} catch (Exception e) {
			log.error("Errore generico", e);
			throw new ParseReleaseException("Errore generico",e);
		}


		return result;

	}

	/**
	public BaseReleaseParserModel searchRelease(String releaseName) throws ParseReleaseException {

		BaseReleaseParserModel result = null;

		if(releaseName == null)
			return result;


		try {

			int tentativi = 0;
			boolean trovato = false;

			String userAgent = this.randomUserAgent();

			Elements releaseItems = null;
			Document doc = null;
			do  {
				try {

					// pagina di inizio
					String releaseNameSearch = this.createSearchString(releaseName);
					String urlConn = conf.URL+conf.URL_ACTION+"?"+conf.PARAMS.replace("{0}", releaseNameSearch);
					log.info("Connessione in corso --> "+urlConn);
					doc = Jsoup.connect(urlConn).timeout((tentativi+1)*TIMEOUT).userAgent(userAgent).ignoreHttpErrors(true).get();

					if(antiDDOS.isAntiDDOS(doc)) {
						doc = this.bypassAntiDDOS(doc, conf.URL, urlConn);
					}

					releaseItems = doc.getElementsByClass(conf.CLASS_RELEASE_ITEM);

					if(releaseItems==null || releaseItems.isEmpty()) {
						throw new ParseReleaseException("Tentativo "+tentativi+" fallito!");
					}

					trovato = true;
					countFailConnection = 0; // reset contatore di fail
				} catch (Exception e1) {
					log.error("[SCENELOG] Nessun risultato ottenuto per la release = "+releaseName+"  --> "+e1.getMessage());
					userAgent = this.randomUserAgent(); // proviamo un nuovo user agent
					tentativi++;
					if("Read timed out".equalsIgnoreCase(e1.getMessage())) {
						countFailConnection++;
					}
				}

			} while(tentativi<2 && !trovato);

			if(releaseItems==null) {
				log.info("[SCENELOG] Nessun risultato ottenuto per la release = "+releaseName);
				return null;
//				throw new ParseReleaseException("[SCENELOG] Nessun risultato ottenuto per la release = "+releaseName);
			}

			String releaseLinkGood = null;
			for(Element e : releaseItems) {
				Element title = e.getElementsByClass(conf.CLASS_RELEASE_TITLE).get(0);
				Element relCandidate = title.select("h1 > a").get(0);

				if(SymusicUtility.compareStringSimilarity(releaseName, relCandidate.text(), LevelSimilarity.ALTO)) {
					releaseLinkGood = relCandidate.attr("href");
					log.info("[SCENELOG] Trovata la release: "+relCandidate.text()+" - "+releaseLinkGood);

					result = this.popolaEuroadrenalineRelease(e);

					break;
				}

			}

			if(result == null) {
				log.info("[SCENELOG] Release non trovata tra tutti i risultati ottenuti: "+releaseName);
			}


		} catch (ParseException e) {
			log.error("Errore nel parsing", e);
			throw new ParseReleaseException("Errore nel parsing",e);
		}

		return result;

	}



	public ReleaseModel parseReleaseDetails(BaseReleaseParserModel scenelogModel, ReleaseModel release) throws ParseReleaseException {

		Document doc = null;
		try {

			if(scenelogModel==null)
				return release;

			// SE RELEASE ANCORA NON PRESENTE SI CREA L'OGGETTO
			if(release==null)
				release = new ReleaseModel();

			release = this.popolaRelease(release, scenelogModel);

			log.info("[SCENELOG] \t connecting to:  "+scenelogModel.getUrlReleaseDetails());
			// release trovata
			String userAgent = this.randomUserAgent();
			doc = Jsoup.connect(scenelogModel.getUrlReleaseDetails()).userAgent(userAgent).ignoreHttpErrors(true).get();

			if(antiDDOS.isAntiDDOS(doc)) {
				doc = this.bypassAntiDDOS(doc, conf.URL, scenelogModel.getUrlReleaseDetails());
			}

			TrackModel currTrack = null;
			Element releaseTrack = doc.getElementsByClass(conf.RELEASE_TRACK_NAME).get(0);

			List<TextNode> textnodes = releaseTrack.textNodes();
			int numTr = 1;
			List<TrackModel> tracks = new ArrayList<TrackModel>();
			for(TextNode tx : textnodes) {
				currTrack = new TrackModel();
				currTrack.setTrackNumber(numTr);
				String text = tx.text().replaceFirst("\\d+\\.",""); // se c'e' il numero di track, lo elimina
//				System.out.println(text);
				currTrack.setTrackName(text);
				tracks.add(currTrack);
				log.info("[SCENELOG] \t TRACK:  "+numTr+". "+currTrack);
				numTr++;

			}

			if(release.getTracks().isEmpty()) {
				release.setTracks(tracks);
			} else {
				// PRIORITA' ALLE TRACCE SCENELOG
				release.setTracks(SymusicUtility.chooseTrack(tracks, release.getTracks(), true));
			}


			Element releaseDownloads = doc.getElementsByClass(conf.RELEASE_DOWNLOAD).get(0);

			Elements downloads = releaseDownloads.getElementsByTag("a");
			for(Element dl : downloads) {
				release.addLink(this.popolateLink(dl));
			}

			SymusicUtility.updateReleaseExtraction(release.getReleaseExtraction(),true,AreaExtraction.SCENELOG);
			countFailConnection = 0;
		} catch(Exception e) {
			log.error("Nessun risultato ottenuto oppure errore nel parsing. "+e.getMessage());
			countFailConnection++;
			SymusicUtility.updateReleaseExtraction(release.getReleaseExtraction(),false,AreaExtraction.SCENELOG);
//			throw new ParseReleaseException("Errore nel parsing",e);
		}

		return release;

	}
*/

	private ReleaseModel popolaRelease(ReleaseModel release, BaseReleaseParserModel scenelogModel) throws ParseException {
		if(scenelogModel.getReleaseName()!=null) {
			release.setNameWithUnderscore(scenelogModel.getReleaseName());
			release.setName(scenelogModel.getReleaseName().replace("_", " "));
		}
		if(release.getReleaseDate()==null && scenelogModel.getReleaseDate()!=null)
			release.setReleaseDate(SymusicUtility.getStandardDate(scenelogModel.getReleaseDate()));

		// AGGIUNGE ULTERIORI INFO DELLA RELEASE A PARTIRE DAL NOME
		// ES. CREW E ANNO RELEASE
		SymusicUtility.processReleaseName(release);

		return release;
	}

	private BaseReleaseParserModel popolaEuroadrenalineRelease(Element tmp) throws ParseException {

		BaseReleaseParserModel release = new BaseReleaseParserModel();

		Element releaseItem = tmp.getElementsByClass(conf.CLASS_RELEASE_TITLE).get(0);
		String releaseName = releaseItem.text();
		String releaseUrl = releaseItem.attr("href");

		Element relDate = tmp.getElementsByClass(conf.CLASS_RELEASE_DATA).get(0);
		String dateIn = (relDate.text().split(",")[1].replace(" ", "")).trim().substring(0,10);
		dateIn = this.getStandardDateFormat(dateIn);
		Date dateInDate = SymusicUtility.getStandardDate(dateIn);

		// RELEASE NAME
		if(releaseName.startsWith("["))
			releaseName = releaseName.substring(releaseName.indexOf("] ")+2);
		release.setReleaseName(releaseName);

		// URL
		release.setUrlReleaseDetails(releaseUrl);

		// RELEASE DATE
		release.setReleaseDate(dateInDate);

		// RANGE DATA, SOLO LE RELEASE COMPRESE DA - A
		if(dataDa!=null && dataA!=null) {
			release.setDateInRange(this.downloadReleaseDay(dateInDate, dataDa, dataA));
		}

		// CONTROLLA SE E' UN RADIO/SAT RIP
		release.setRadioRip(this.isRadioRipRelease(releaseName));

//		System.out.println(release);
		log.info("|"+release+"| acquisita");

		return release;
	}

	private String getStandardDateFormat(String dateIn) throws ParseException {

		if(dateIn.contains("Yesterday")) {
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.DATE, -1);
			dateIn = new SimpleDateFormat(conf.DATE_FORMAT).format(cal.getTime());
		}
		else if(dateIn.contains("Today")) {
			dateIn = new SimpleDateFormat(conf.DATE_FORMAT).format(new Date());
		}

		return SymusicUtility.getStandardDateFormat(dateIn, conf.DATE_FORMAT);

	}

	private String createSearchString(String releaseName) {
		if(releaseName.contains("_"))
			return releaseName;
		else
			return this.applyFilterSearch(releaseName);
	}

	@Override
	protected String applyFilterSearch(String t) {
		t = t.replaceAll("[-,!?&']", " ").replace(" feat ", " ").replace(" feat. ", " ").replace(" ft ", " ").replace(" featuring ", " ")
				.replace(" presents ", " ").replace(" pres ", " ").replace(" pres. ", " ")
				.replace("  ", " ").replace(" and ", " ").replace(" ", "+");
		if(t.indexOf("(")!=-1) {
			t = t.substring(0,t.indexOf("("));
		}
		return t;
	}

	public int getCountFailConnection() {
		return countFailConnection;
	}

	public void resetCountFailConnection() {
		countFailConnection = 0;
	}

//	public String getUrlRelease(String releaseName) {
//		return conf.URL_MUSIC+releaseName.toLowerCase()+"/";
//	}

	public static void main(String[] args) throws IOException, ParseReleaseException {
		EuroadrenalineParser p = new EuroadrenalineParser();
		List<BaseReleaseParserModel> lm = p.parseFullPage("http://www.euroadrenaline.com/house/","house");
		for(BaseReleaseParserModel m : lm) {
			System.out.println(m);
		}
//		ReleaseModel mm = p.parseReleaseDetails(lm.get(0), null);
//
//		System.out.println(mm);
	}




}
