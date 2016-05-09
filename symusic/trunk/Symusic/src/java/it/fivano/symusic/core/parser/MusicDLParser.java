package it.fivano.symusic.core.parser;

import it.fivano.symusic.SymusicUtility;
import it.fivano.symusic.SymusicUtility.LevelSimilarity;
import it.fivano.symusic.conf.MusicDLConf;
import it.fivano.symusic.core.parser.model.BaseMusicParserModel;
import it.fivano.symusic.exception.ParseReleaseException;
import it.fivano.symusic.model.ReleaseExtractionModel.AreaExtraction;
import it.fivano.symusic.model.ReleaseModel;
import it.fivano.symusic.model.TrackModel;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;

public class MusicDLParser extends GenericParser {

	private MusicDLConf conf;


	public MusicDLParser() throws IOException {
		conf = new MusicDLConf();
		this.setLogger(getClass());
	}

	public List<BaseMusicParserModel> parseFullPage(String urlPage, Date da, Date a) throws ParseReleaseException {
		this.dataDa = da;
		this.dataA = a;

		return this.parseFullPage(urlPage);

	}

	public List<BaseMusicParserModel> parseFullPage(String urlPage) throws ParseReleaseException {

		List<BaseMusicParserModel> result = new ArrayList<BaseMusicParserModel>();

		if(urlPage == null)
			return result;


		try {
			// CONNESSIONE ALLA PAGINA
			String userAgent = this.randomUserAgent();
			Document doc = null;
			try {
				log.info("Connessione in corso --> "+urlPage);
				doc = Jsoup.connect(urlPage).timeout(TIMEOUT).userAgent(userAgent).ignoreHttpErrors(true).get();
			} catch (IOException e) {
				doc = Jsoup.connect(urlPage).timeout(TIMEOUT).userAgent(userAgent).ignoreHttpErrors(true).get();
			}

			if(antiDDOS.isAntiDDOS(doc)) {
				doc = this.bypassAntiDDOS(doc, conf.URL, urlPage, userAgent);
			}

			Elements releaseGroup = doc.getElementsByClass(conf.CLASS_RELEASE_LIST_ITEM);
			if(releaseGroup.size()==0) {
				releaseGroup = doc.getElementsByAttributeValue("id",conf.CLASS_RELEASE_LIST_ITEM);

			}
			if(releaseGroup.size()>0) {
				BaseMusicParserModel release = null;
				log.info("####################################");
				for(Element tmp : releaseGroup) {

					release = this.popolaMusicDLRelease(tmp);

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

	public BaseMusicParserModel searchRelease(String releaseName) throws ParseReleaseException {

		BaseMusicParserModel result = null;

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
						doc = this.bypassAntiDDOS(doc, conf.URL, urlConn, userAgent);
					}

					releaseItems = doc.getElementsByClass(conf.CLASS_RELEASE_ITEM);

					if(releaseItems==null || releaseItems.isEmpty()) {
						throw new ParseReleaseException("Tentativo "+tentativi+" fallito!");
					}

					trovato = true;
				} catch (Exception e1) {
					log.error("[MUSICDL] Nessun risultato ottenuto per la release = "+releaseName+"  --> "+e1.getMessage());
					userAgent = this.randomUserAgent(); // proviamo un nuovo user agent
					tentativi++;
				}

			} while(tentativi<2 && !trovato);

			if(releaseItems==null) {
				log.info("[MUSICDL] Nessun risultato ottenuto per la release = "+releaseName);
				return null;
//				throw new ParseReleaseException("[MUSICDL] Nessun risultato ottenuto per la release = "+releaseName);
			}

			String releaseLinkGood = null;
			for(Element e : releaseItems) {
				Element title = e.getElementsByClass(conf.CLASS_RELEASE_TITLE).get(0);
				Element relCandidate = title.select("h1 > a").get(0);

				if(SymusicUtility.compareStringSimilarity(releaseName, relCandidate.text(), LevelSimilarity.ALTO)) {
					releaseLinkGood = relCandidate.attr("href");
					log.info("[MUSICDL] Trovata la release: "+relCandidate.text()+" - "+releaseLinkGood);

					result = this.popolaMusicDLRelease(e);

					break;
				}

			}

			if(result == null) {
				log.info("[MUSICDL] Release non trovata tra tutti i risultati ottenuti: "+releaseName);
			}


		} catch (Exception e) {
			log.error("Errore nel parsing", e);
//			throw new ParseReleaseException("Errore nel parsing",e);
		}

		return result;

	}


	public ReleaseModel parseReleaseDetails(BaseMusicParserModel musicDLModel, ReleaseModel release) throws ParseReleaseException {

		Document doc = null;
		try {

			if(musicDLModel==null)
				return release;

			// SE RELEASE ANCORA NON PRESENTE SI CREA L'OGGETTO
			if(release==null)
				release = new ReleaseModel();


			// release trovata
			String userAgent = this.randomUserAgent();
			doc = Jsoup.connect(musicDLModel.getUrlReleaseDetails()).userAgent(userAgent).ignoreHttpErrors(true).get();

			if(antiDDOS.isAntiDDOS(doc)) {
				doc = this.bypassAntiDDOS(doc, conf.URL, musicDLModel.getUrlReleaseDetails(), userAgent);
			}

			Element releaseInfo = doc.getElementsByClass(conf.TABLE_INFO).get(0);
			Elements listInfo = releaseInfo.select("tbody > tr");

			release.setArtist(listInfo.get(0).select("td").get(1).text());
			release.setSong(listInfo.get(1).select("td").get(1).text());
			release.setGenre(SymusicUtility.creaGenere(listInfo.get(2).select("td").get(1).text()));

			release = this.popolaRelease(release, musicDLModel);


			TrackModel currTrack = null;
			Element releaseTrack = doc.getElementsByClass(conf.TABLE_TRACKS).get(0);

			Elements listTrack = releaseTrack.select("table > tbody > tr");


			List<TrackModel> tracks = new ArrayList<TrackModel>();
			for(Element tx : listTrack) {

				Elements row = tx.select("td");
				if(row.size()==4) {
					currTrack = new TrackModel();
					currTrack.setTrackNumber(Integer.parseInt(row.get(0).text()));
					currTrack.setTrackName(row.get(1).text());
					currTrack.setArtist(row.get(2).text());
					currTrack.setTime(row.get(3).text());
					tracks.add(currTrack);
					log.info("[MUSICDL] \t TRACK:  "+currTrack.getTrackNumber()+". "+currTrack);

				}

			}

			if(release.getTracks().isEmpty()) {
				release.setTracks(tracks);
			} else {
				// PRIORITA' ALLE TRACCE MUSICDL
				release.setTracks(SymusicUtility.chooseTrack(tracks, release.getTracks(), true));
			}


			Element releaseDownloads = doc.getElementsByClass(conf.RELEASE_DOWNLOAD).get(0);

			Elements downloads = releaseDownloads.getElementsByTag("a");
			for(Element dl : downloads) {
				release.addLink(this.popolateLink(dl));
			}

//			SymusicUtility.updateReleaseExtraction(release.getReleaseExtraction(),true,AreaExtraction.SCENELOG);

		} catch(Exception e) {
			log.error("Errore nel parsing", e);
//			SymusicUtility.updateReleaseExtraction(release.getReleaseExtraction(),false,AreaExtraction.SCENELOG);
//			throw new ParseReleaseException("Errore nel parsing",e);
		}

		return release;

	}


	private ReleaseModel popolaRelease(ReleaseModel release, BaseMusicParserModel musicDLModel) throws ParseException {
		if(musicDLModel.getReleaseName()!=null) {
			release.setNameWithUnderscore(musicDLModel.getReleaseName());

			if(release.getArtist()!=null && release.getSong()!=null) {
				release.setName(release.getArtist()+" - "+release.getSong());
			} else {
				release.setName(musicDLModel.getReleaseName().replace("_", " "));
			}
		}

		if(release.getReleaseDate()==null && musicDLModel.getReleaseDate()!=null)
			release.setReleaseDate(SymusicUtility.getStandardDate(musicDLModel.getReleaseDate()));

		// AGGIUNGE ULTERIORI INFO DELLA RELEASE A PARTIRE DAL NOME
		// ES. CREW E ANNO RELEASE
		SymusicUtility.processReleaseName(release);

		return release;
	}

	private BaseMusicParserModel popolaMusicDLRelease(Element tmp) throws ParseException {

		BaseMusicParserModel release = new BaseMusicParserModel();

		Element releaseItem = tmp.getElementsByClass(conf.CLASS_RELEASE_TITLE).get(0).getElementsByTag("a").get(0);
		String releaseName = this.standardFormatRelease(releaseItem.text());
		String releaseUrl = releaseItem.attr("href");

		Element relDate = tmp.getElementsByClass(conf.CLASS_RELEASE_LIST_DATA).get(0).getElementsByTag("time").get(0);
		String dateIn = relDate.attr("datetime");
		dateIn = this.getStandardDateFormat(dateIn);
		Date dateInDate = SymusicUtility.getStandardDate(dateIn);

		// RELEASE NAME
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

	private String standardFormatRelease(String text) {
		// TODO Auto-generated method stub
		return text;
	}

	private String getStandardDateFormat(String dateIn) throws ParseException {

		String zeroDayFormat = conf.DATE_FORMAT;

		dateIn = dateIn.replace("th,", "").replace("rd,", "").replace("st,", "").replace("nd,", "");

		return SymusicUtility.getStandardDateFormat(dateIn, zeroDayFormat);

	}

	private String createSearchString(String releaseName) {
		return releaseName.replaceAll("[^A-Za-z0-9 -_]", "").replace("_", "-");
//		if(releaseName.contains("_"))
//			return releaseName;
//		else
//			return this.applyFilterSearch(releaseName);
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

	public String getUrlRelease(String releaseName, String genre) {
		return conf.URL+(genre.toLowerCase())+"/"+(createSearchString(releaseName).toLowerCase())+"/";
	}

	public static void main(String[] args) throws IOException, ParseReleaseException {
		MusicDLParser p = new MusicDLParser();
		BaseMusicParserModel m = p.parseFullPage("http://musicdl.net/trance").get(0);
		ReleaseModel mm = p.parseReleaseDetails(m, null);

		System.out.println(mm);
	}

}
