package it.fivano.symusic.core.parser;

import it.fivano.symusic.SymusicUtility;
import it.fivano.symusic.SymusicUtility.LevelSimilarity;
import it.fivano.symusic.backend.service.LinkService;
import it.fivano.symusic.backend.service.TrackService;
import it.fivano.symusic.conf.ScenelogConf;
import it.fivano.symusic.core.BaseService;
import it.fivano.symusic.core.parser.model.ScenelogParserModel;
import it.fivano.symusic.exception.ParseReleaseException;
import it.fivano.symusic.model.ReleaseModel;
import it.fivano.symusic.model.TrackModel;
import it.fivano.symusic.model.ReleaseExtractionModel.AreaExtraction;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;

public class ScenelogParser extends GenericParser {
	
	private ScenelogConf conf;
	
	
	public ScenelogParser() throws IOException {
		conf = new ScenelogConf();
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
			
			if(this.isAntiDDOS(doc)) {
				doc = this.bypassAntiDDOS(doc);
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
		}
		
		return result;
						
	}

	
	private Document bypassAntiDDOS(Document doc) throws IOException {
		String jschl_vc = doc.getElementsByAttributeValue("name", "jschl_vc").get(0).attr("value");
//		System.out.println(jschl_vc);
		Elements scriptElements = doc.getElementsByTag("script");
		String numberCalcLine = null;
		for (Element element :scriptElements ){                
			for (DataNode node : element.dataNodes()) {
				String text = node.getWholeData();
				String[] lines = text.split("\n");
				for(String scriptLine : lines) {
					if(scriptLine.trim().startsWith("a.value = ")) {
						numberCalcLine = scriptLine.replace(";", "").replace("a.value = ", "").trim();
						break;
					}
				}

			}
//			System.out.println(numberCalcLine);            
		}
		
		int jschl_answer = 0;
		if(numberCalcLine!=null) {
			String[] addizioni = numberCalcLine.split("\\+");
			int i1,i2,i3;
			i1 = Integer.parseInt(addizioni[0]);
			String[] moltipl = addizioni[1].split("\\*");
			i2 = Integer.parseInt(moltipl[0]);
			i3 = Integer.parseInt(moltipl[1]);
			
			jschl_answer = ((i2*i3)+i1)+11;
		}
		
		String urlPage = conf.URL+"cdn-cgi/l/chk_jschl";
		String userAgent = this.randomUserAgent();
		doc = Jsoup.connect(urlPage).timeout(TIMEOUT).userAgent(userAgent).data("jschl_vc", jschl_vc).data("jschl_answer", jschl_answer+"").ignoreHttpErrors(true).get();
		
		
		return doc;
	}

	private boolean isAntiDDOS(Document doc) {
		Elements res = doc.getElementsByClass("cf-browser-verification");
		log.info("DDOS protection: "+(res.size()==0 ? false : true));
		return res.size()==0 ? false : true;
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
					
					if(this.isAntiDDOS(doc)) {
						doc = this.bypassAntiDDOS(doc);
					}
					
					releaseItems = doc.getElementsByClass(conf.CLASS_RELEASE_ITEM);
					
					if(releaseItems==null || releaseItems.isEmpty()) {
						throw new ParseReleaseException("Tentativo "+tentativi+" fallito!");
					}
				
					trovato = true;
				} catch (Exception e1) {
					log.error(e1.getMessage(),e1);
					userAgent = this.randomUserAgent(); // proviamo un nuovo user agent
					tentativi++;
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

			// release trovata
			String userAgent = this.randomUserAgent();
			doc = Jsoup.connect(scenelogModel.getUrlReleaseDetails()).userAgent(userAgent).ignoreHttpErrors(true).get();
			
			if(this.isAntiDDOS(doc)) {
				doc = this.bypassAntiDDOS(doc);
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
				release.setTracks(SymusicUtility.chooseTrack(tracks, release.getTracks()));
			}
			

			Element releaseDownloads = doc.getElementsByClass(conf.RELEASE_DOWNLOAD).get(0);

			Elements downloads = releaseDownloads.getElementsByTag("a");
			for(Element dl : downloads) {
				release.addLink(this.popolateLink(dl));
			}
			
			SymusicUtility.updateReleaseExtraction(release.getReleaseExtraction(),true,AreaExtraction.SCENELOG);

		} catch(Exception e) {
			log.error("Errore nel parsing", e);
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
		t = t.replaceAll("[-,!?&']", " ").replace(" feat ", " ").replace(" ft ", " ")
				.replace("  ", " ").replace(" and ", " ").replace(" ", "+");
		return t;
	}
	
	public static void main(String[] args) throws IOException, ParseReleaseException {
		ScenelogParser p = new ScenelogParser();
		ScenelogParserModel m = p.parseFullPage("http://scenelog.eu/music/").get(0);
		ReleaseModel mm = p.parseReleaseDetails(m, null);
		
		System.out.println(mm);
	}

}
