package it.fivano.symusic.core.parser;

import it.fivano.symusic.SymusicUtility;
import it.fivano.symusic.backend.service.LinkService;
import it.fivano.symusic.backend.service.TrackService;
import it.fivano.symusic.conf.ScenelogConf;
import it.fivano.symusic.core.BaseService;
import it.fivano.symusic.core.parser.model.ScenelogParserModel;
import it.fivano.symusic.exception.ParseReleaseException;
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
			Document doc = Jsoup.connect(urlPage).timeout(TIMEOUT).userAgent(userAgent).get();

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
					String urlConn = conf.URL+conf.URL_ACTION+"?"+conf.PARAMS.replace("{0}", releaseName);
					log.info("Connessione in corso --> "+urlConn);
					doc = Jsoup.connect(urlConn).timeout((tentativi+1)*TIMEOUT).userAgent(userAgent).get();
					
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
				throw new ParseReleaseException("[SCENELOG] Nessun risultato ottenuto per la release = "+releaseName);
			}
						
			String releaseLinkGood = null;
			for(Element e : releaseItems) {
				Element title = e.getElementsByClass(conf.CLASS_RELEASE_TITLE).get(0);
				Element relCandidate = title.select("h1 > a").get(0);
								
				if(SymusicUtility.compareStringSimilarity(releaseName, relCandidate.text())) {
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
			doc = Jsoup.connect(scenelogModel.getUrlReleaseDetails()).userAgent(userAgent).get();

			TrackModel currTrack = null;
			Element releaseTrack = doc.getElementsByClass(conf.RELEASE_TRACK_NAME).get(0);

			List<TextNode> textnodes = releaseTrack.textNodes();
			int numTr = 1;
			for(TextNode tx : textnodes) {
				currTrack = new TrackModel();
				currTrack.setTrackNumber(numTr);
				String text = tx.text().replaceFirst("\\d+\\.",""); // se c'e' il numero di track, lo elimina
//				System.out.println(text);
				currTrack.setTrackName(text);
				release.addTrack(currTrack);
				log.info("ID_RELEASE="+release.getId()+"\t TRACK:  "+numTr+"."+currTrack);
				numTr++;

			}

			Element releaseDownloads = doc.getElementsByClass(conf.RELEASE_DOWNLOAD).get(0);

			Elements downloads = releaseDownloads.getElementsByTag("a");
			for(Element dl : downloads) {
				release.addLink(this.popolateLink(dl));
			}

		} catch(Exception e) {
			log.error("Errore nel parsing", e);
			throw new ParseReleaseException("Errore nel parsing",e);
		}
		
		return release;

	}


	private ReleaseModel popolaRelease(ReleaseModel release, ScenelogParserModel scenelogModel) throws ParseException {
		release.setNameWithUnderscore(scenelogModel.getReleaseName());
		release.setName(scenelogModel.getReleaseName().replace("_", " "));
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
	
	@Override
	protected String applyFilterSearch(String result) {
		return result;
	}
	
	public static void main(String[] args) throws IOException, ParseReleaseException {
		ScenelogParser p = new ScenelogParser();
		ScenelogParserModel m = p.parseFullPage("http://scenelog.eu/music/").get(0);
		ReleaseModel mm = p.parseReleaseDetails(m, null);
		
		System.out.println(mm);
	}

}
