package it.fivano.symusic.core.parser;

import it.fivano.symusic.SymusicUtility;
import it.fivano.symusic.SymusicUtility.LevelSimilarity;
import it.fivano.symusic.conf.PresceneConf;
import it.fivano.symusic.core.parser.model.BaseReleaseParserModel;
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

public class PresceneParser extends GenericParser {

	private PresceneConf conf;


	public PresceneParser() throws IOException {
		conf = new PresceneConf();
		this.setLogger(getClass());
	}

	public List<BaseReleaseParserModel> parseFullPage(String urlPage, Date da, Date a) throws ParseReleaseException {
		this.dataDa = da;
		this.dataA = a;

		return this.parseFullPage(urlPage);

	}

	public List<BaseReleaseParserModel> parseFullPage(String urlPage) throws ParseReleaseException {

		List<BaseReleaseParserModel> result = new ArrayList<BaseReleaseParserModel>();

		if(urlPage == null)
			return result;


		try {
			// CONNESSIONE ALLA PAGINA
			String userAgent = this.randomUserAgent();
			log.info("Connessione in corso --> "+urlPage);
			Document doc = Jsoup.connect(urlPage).timeout(TIMEOUT).userAgent(userAgent).ignoreHttpErrors(true).get();

			if(antiDDOS.isAntiDDOS(doc)) {
				doc = this.bypassAntiDDOS(doc, conf.BASE_URL, urlPage, userAgent);
			}
			else if(doc.text().contains("wait 4 seconds then reload page") ||
					doc.text().contains("preparing https://prescene.tk")) {
				Thread.sleep(4100);
				doc = Jsoup.connect(urlPage).timeout(TIMEOUT).userAgent(userAgent).ignoreHttpErrors(true).get();
			}

			Elements releaseGroup = doc.getElementsByClass(conf.CLASS_RELEASE_GROUP);

			if(releaseGroup.size()>0) {

				Elements releaseList = releaseGroup.get(0).getElementsByClass(conf.CLASS_RELEASE_ITEM);
				BaseReleaseParserModel release = null;
				log.info("####################################");
				boolean isPrimo = true;
				for(Element tmp : releaseList) {
					if(isPrimo) {
						isPrimo = false;
						continue;
					}

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

	/**
	public MusicDLParserModel searchRelease(String releaseName) throws ParseReleaseException {

		MusicDLParserModel result = null;

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


		} catch (ParseException e) {
			log.error("Errore nel parsing", e);
			throw new ParseReleaseException("Errore nel parsing",e);
		}

		return result;

	}


	public ReleaseModel parseReleaseDetails(MusicDLParserModel musicDLModel, ReleaseModel release) throws ParseReleaseException {

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
				doc = this.bypassAntiDDOS(doc, conf.URL, musicDLModel.getUrlReleaseDetails());
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
*/

	private ReleaseModel popolaRelease(ReleaseModel release, BaseMusicParserModel musicDLModel) throws ParseException {
		if(musicDLModel.getReleaseName()!=null) {
			release.setNameWithUnderscore(musicDLModel.getReleaseName());

			if(release.getArtist()!=null && release.getSong()!=null) {
				release.setName(release.getArtist()+" - "+release.getSong());
			} else {
				release.setName(musicDLModel.getReleaseName().replace("_", " "));
			}
		}
		release.setReleaseDate(SymusicUtility.getStandardDate(musicDLModel.getReleaseDate()));

		// AGGIUNGE ULTERIORI INFO DELLA RELEASE A PARTIRE DAL NOME
		// ES. CREW E ANNO RELEASE
		SymusicUtility.processReleaseName(release);

		return release;
	}

	private BaseReleaseParserModel popolaMusicDLRelease(Element tmp) throws ParseException {

		BaseReleaseParserModel release = null;

		Elements cells = tmp.children();

		if(cells.size()>=5) {
			release = new BaseReleaseParserModel();

			// RELEASE DATE
			Element relDate = cells.get(1).getElementsByTag("a").get(0);
			String dateIn = relDate.text();
			dateIn = this.getStandardDateFormat(dateIn);
			Date dateInDate = SymusicUtility.getStandardDate(dateIn);
			release.setReleaseDate(dateInDate);

			// RELEASE NAME
			String releaseName = this.standardFormatRelease(cells.get(2).text());
			release.setReleaseName(releaseName);

			// GENRE
			release.setGenre(cells.get(5).text());

			// CREW
			release.setCrew(cells.get(6).text());

			// RANGE DATA, SOLO LE RELEASE COMPRESE DA - A
			if(dataDa!=null && dataA!=null) {
				release.setDateInRange(this.downloadReleaseDay(dateInDate, dataDa, dataA));
			}

			// CONTROLLA SE E' UN RADIO/SAT RIP
			release.setRadioRip(this.isRadioRipRelease(releaseName));

			log.info("|"+release+"| acquisita");
		}

		return release;
	}

	private String standardFormatRelease(String text) {
		// TODO Auto-generated method stub
		return text;
	}

	private String getStandardDateFormat(String dateIn) throws ParseException {

		String zeroDayFormat = conf.DATE_FORMAT;

		return SymusicUtility.getStandardDateFormat(dateIn, zeroDayFormat);

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

	public static void main(String[] args) throws IOException, ParseReleaseException {
		PresceneParser p = new PresceneParser();
		List<BaseReleaseParserModel> l = p.parseFullPage("https://prescene.tk/index.php?group=ZzZz");
		for(BaseReleaseParserModel m : l)
			System.out.println(m);
	}

}
