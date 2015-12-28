package it.fivano.symusic.core.parser;

import it.fivano.symusic.SymusicUtility;
import it.fivano.symusic.SymusicUtility.LevelSimilarity;
import it.fivano.symusic.conf.ScenelogConf;
import it.fivano.symusic.core.parser.model.ScenelogParserModel;
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

public class ScenelogParser extends GenericParser {
	
	private ScenelogConf conf;
	private int countFailConnection;
	
	public ScenelogParser() throws IOException {
		conf = new ScenelogConf();
		countFailConnection = 0;
		this.setLogger(getClass());
	}
	
	public List<ScenelogParserModel> parseFullPage(String urlPage, Date da, Date a) throws ParseReleaseException {
		this.dataDa = da;
		this.dataA = a;
		
		return this.parseFullPage(urlPage);
		
	}
	
	public List<ScenelogParserModel> parseFullPage(String urlPage) throws ParseReleaseException {
		
		List<ScenelogParserModel> result = new ArrayList<ScenelogParserModel>();
		
		if(urlPage == null)
			return result;

		
		try {
			// CONNESSIONE ALLA PAGINA
			String userAgent = this.randomUserAgent();
			log.info("Connessione in corso --> "+urlPage);
			Document doc = Jsoup.connect(urlPage).timeout(TIMEOUT).userAgent(userAgent).ignoreHttpErrors(true).get();
			
			if(antiDDOS.isAntiDDOS(doc)) {
				doc = this.bypassAntiDDOS(doc, conf.URL, urlPage);
			}

			Elements releaseGroup = doc.getElementsByClass(conf.CLASS_RELEASE_LIST_ITEM);
			if(releaseGroup.size()>0) {
				ScenelogParserModel release = null;
				log.info("####################################");
				for(Element tmp : releaseGroup) {
					
					release = this.popolaScenelogRelease(tmp);
					
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

	public ScenelogParserModel searchRelease(String releaseName) throws ParseReleaseException {
		
		ScenelogParserModel result = null;
		
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
					
					result = this.popolaScenelogRelease(e);
					
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
	
	
	public ReleaseModel parseReleaseDetails(ScenelogParserModel scenelogModel, ReleaseModel release) throws ParseReleaseException {
		
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


	private ReleaseModel popolaRelease(ReleaseModel release, ScenelogParserModel scenelogModel) throws ParseException {
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

	private ScenelogParserModel popolaScenelogRelease(Element tmp) throws ParseException {
		
		ScenelogParserModel release = new ScenelogParserModel();
		
		Element releaseItem = tmp.getElementsByTag("h1").get(0).getElementsByTag("a").get(0);
		String releaseName = releaseItem.text();
		String releaseUrl = releaseItem.attr("href");
		
		Element relDate = tmp.getElementsByClass(conf.CLASS_RELEASE_LIST_DATA).get(0);
		String dateIn = relDate.text();
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

	private String getStandardDateFormat(String dateIn) throws ParseException {

		String zeroDayFormat = conf.DATE_FORMAT;

		dateIn = dateIn.replace("th,", "").replace("rd,", "").replace("st,", "").replace("nd,", "");

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
	
	public int getCountFailConnection() {
		return countFailConnection;
	}
	
	public void resetCountFailConnection() {
		countFailConnection = 0;
	}
	
	public String getUrlRelease(String releaseName) {
		return conf.URL_MUSIC+releaseName.toLowerCase()+"/";
	}
	
	public static void main(String[] args) throws IOException, ParseReleaseException {
		ScenelogParser p = new ScenelogParser();
		ScenelogParserModel m = p.parseFullPage("http://scenelog.eu/music/").get(0);
		ReleaseModel mm = p.parseReleaseDetails(m, null);
		
		System.out.println(mm);
	}




}
