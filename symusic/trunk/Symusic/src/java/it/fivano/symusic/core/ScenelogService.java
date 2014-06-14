package it.fivano.symusic.core;

import it.fivano.symusic.SymusicUtility;
import it.fivano.symusic.SymusicUtility.LevelSimilarity;
import it.fivano.symusic.backend.service.LinkService;
import it.fivano.symusic.backend.service.TrackService;
import it.fivano.symusic.conf.ScenelogConf;
import it.fivano.symusic.exception.BackEndException;
import it.fivano.symusic.exception.ParseReleaseException;
import it.fivano.symusic.model.ReleaseModel;
import it.fivano.symusic.model.TrackModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;

public class ScenelogService extends BaseService {
	
	private ScenelogConf conf;
	
		
	public ScenelogService() throws IOException {
		conf = new ScenelogConf();
		this.setLogger(getClass());
	}
	
	public boolean parseScenelog(ReleaseModel release) throws ParseReleaseException, BackEndException {
		
		try {
			int tentativi = 0;
			boolean trovato = false;
			
			String userAgent = this.randomUserAgent();
			
			Elements releaseItems = null;
			Document doc = null;
			do  {
				try {
				
					// pagina di inizio
					String urlConn = this.getUrlConnection(release, tentativi);
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
//					log.info("USER_AGENT_ERROR = "+userAgent);
					log.error(e1.getMessage(),e1);
					userAgent = this.randomUserAgent(); // proviamo un nuovo user agent
					tentativi++;
				}
				
			} while(tentativi<2 && !trovato);
			
			if(releaseItems==null) {
				throw new ParseReleaseException("[SCENELOG] Nessun risultato ottenuto per la release = "+release);
			}
			
//			log.info("USER_AGENT = "+userAgent);
			
			String releaseLinkGood = null;
			for(Element e : releaseItems) {
				Element title = e.getElementsByClass(conf.CLASS_RELEASE_TITLE).get(0);
				Element relCandidate = title.select("h1 > a").get(0);
				
				if(SymusicUtility.compareStringSimilarity(release.getName(), relCandidate.text(), LevelSimilarity.ALTO)) {
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
				int numTr = 1;
				for(TextNode tx : textnodes) {
					currTrack = new TrackModel();
					currTrack.setTrackNumber(numTr);
					String text = tx.text().replaceFirst("\\d+\\.",""); // se c'e' il numero di track, lo elimina
//					System.out.println(text);
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
				
				// salva sul db
				LinkService lserv = new LinkService();
				lserv.saveLinks(release.getLinks(), release.getId());

				TrackService tserv = new TrackService();
				tserv.saveTracks(release.getTracks(), release.getId());
				
			}
			else {
				log.warn("[SCENELOG] Release non trovata = "+release);
				return false;
			}

			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			log.error("[SCENELOG] Errore nel parsing",e);
			throw new ParseReleaseException("[SCENELOG] Errore nel parsing",e);
		} catch (BackEndException e) {
			log.error("[SCENELOG] Errore di backend durante il salvataggio dei dati estratti");
			throw e;
		}

		return true;
		
	}
	


	private String getUrlConnection(ReleaseModel release, int tentativi) {
		String query = release.getNameWithUnderscore();
		
		// pagina di inizio
		return conf.URL+conf.URL_ACTION+"?"+conf.PARAMS.replace("{0}", query);
	}
	
	protected String applyFilterSearch(String t) {
		return t.replace(" ", "+");
	}
	
	public static void main(String[] args) throws IOException, ParseReleaseException, BackEndException {
		
		ScenelogService s = new ScenelogService();
		ReleaseModel r = new ReleaseModel();
		r.setName("Houseshaker Feat. Amanda Blush-Light the Sky-(7000042629)-WEB-2013-DWM");
		s.parseScenelog(r);
		
	}

}
