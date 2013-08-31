package it.fivano.symusic.core;

import it.fivano.symusic.SymusicUtility;
import it.fivano.symusic.conf.ScenelogConf;
import it.fivano.symusic.exception.ParseReleaseException;
import it.fivano.symusic.model.LinkModel;
import it.fivano.symusic.model.ReleaseModel;
import it.fivano.symusic.model.TrackModel;

import java.io.IOException;
import java.util.List;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;

public class ScenelogService extends BaseService {
	
	private ScenelogConf conf;
	
	Logger log = Logger.getLogger(getClass());
	
	public ScenelogService() throws IOException {
		conf = new ScenelogConf();
	}
	
	public boolean parseScenelog(ReleaseModel release) throws ParseReleaseException {
		
		try {
			int tentativi = 0;
			boolean trovato = false;
			String userAgent = "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:23.0) Gecko/20100101 Firefox/23.0";
			
			Elements releaseItems = null;
			Document doc = null;
			do  {
				try {
				
					// pagina di inizio
					String urlConn = this.getUrlConnection(release, tentativi);
					log.info("Connessione in corso --> "+urlConn);
					doc = Jsoup.connect(urlConn).userAgent(userAgent).get();
					
					releaseItems = doc.getElementsByClass(conf.CLASS_RELEASE_ITEM);
					
					if(releaseItems==null || releaseItems.isEmpty()) {
						throw new ParseReleaseException();
					}
				
					trovato = true;
				} catch (Exception e1) {
					log.error(e1.getMessage());
					tentativi++;
				}
				
			} while(tentativi<=2 && !trovato);
			
			if(releaseItems==null) {
				throw new ParseReleaseException("[SCENELOG] Nessun risultato ottenuto per la release = "+release);
			}
			
			String releaseLinkGood = null;
			for(Element e : releaseItems) {
				Element title = e.getElementsByClass(conf.CLASS_RELEASE_TITLE).get(0);
				Element relCandidate = title.select("h1 > a").get(0);
				
				if(SymusicUtility.compareStringSimilarity(release.getName(), relCandidate.text())) {
					releaseLinkGood = relCandidate.attr("href");
					log.info("[SCENELOG] Trovata la release: "+relCandidate.text()+" - "+releaseLinkGood);
					break;
				}

			}
			
			
			if(releaseLinkGood != null) {
				// release trovata
				doc = Jsoup.connect(releaseLinkGood).userAgent(userAgent).get();
				
				TrackModel currTrack = null;
				Element releaseTrack = doc.getElementsByClass(conf.RELEASE_TRACK_NAME).get(0);
				
				List<TextNode> textnodes = releaseTrack.textNodes();
				for(TextNode tx : textnodes) {
					currTrack = new TrackModel();
					currTrack.setTrackName(tx.text());
					release.addTrack(currTrack);
					log.info("    DETTAGLIO:  "+currTrack);
					
				}
				
				Element releaseDownloads = doc.getElementsByClass(conf.RELEASE_DOWNLOAD).get(0);
				
				Elements downloads = releaseDownloads.getElementsByTag("a");
				LinkModel currLink = null;
				for(Element dl : downloads) {
					currLink = new LinkModel();
					currLink.setLink(dl.attr("href"));
					currLink.setName((dl.attr("href").length()>70)? dl.attr("href").substring(0,70)+"..." : dl.attr("href"));
					release.addLink(currLink);
				}
				
			}
			else {
				log.warn("[SCENELOG] Release non trovata = "+release);
				return false;
			}

			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			log.error("[SCENELOG] Errore nel parsing",e);
			throw new ParseReleaseException("[SCENELOG] Errore nel parsing",e);
		}

		return true;
		
	}
	
	private String getUrlConnection(ReleaseModel release, int tentativi) {
		String query = this.formatQueryString(release.getName(),tentativi);
		
		// pagina di inizio
		return conf.URL+conf.URL_ACTION+"?"+conf.PARAMS.replace("{0}", query);
	}
	
	protected String applyFilterSearch(String t) {
		return t.replace(" ", "+");
	}
	
	public static void main(String[] args) throws IOException, ParseReleaseException {
		
		ScenelogService s = new ScenelogService();
		ReleaseModel r = new ReleaseModel();
		r.setName("Houseshaker Feat. Amanda Blush-Light the Sky-(7000042629)-WEB-2013-DWM");
		s.parseScenelog(r);
		
	}

}
