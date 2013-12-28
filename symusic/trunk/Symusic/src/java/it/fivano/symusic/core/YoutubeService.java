package it.fivano.symusic.core;

import it.fivano.symusic.SymusicUtility;
import it.fivano.symusic.SymusicUtility.LevelSimilarity;
import it.fivano.symusic.backend.service.VideoService;
import it.fivano.symusic.conf.YoutubeConf;
import it.fivano.symusic.exception.BackEndException;
import it.fivano.symusic.exception.ParseReleaseException;
import it.fivano.symusic.model.ReleaseModel;
import it.fivano.symusic.model.VideoModel;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class YoutubeService extends BaseService {
	
	YoutubeConf conf;
		
	public YoutubeService() throws IOException {
		conf = new YoutubeConf();
		this.setLogger(getClass());
	}

	
	public boolean extractYoutubeVideo(ReleaseModel release) throws ParseReleaseException, BackEndException {
		
		try {
			int tentativi = 0;
			boolean trovato = false;
			
			Elements videoGroup = null;
			Document doc = null;
			String urlConn = null;
			do  {
				try {
					
					Long idRel = release.getId();

					// pagina di inizio
					urlConn = this.getUrlConnection(release, tentativi);
					log.info("Connessione in corso --> "+urlConn);
					doc = Jsoup.connect(urlConn).timeout(TIMEOUT).get();
					
					videoGroup = doc.getElementsByClass(conf.CLASS_VIDEO);
					if(videoGroup.isEmpty())
						throw new ParseReleaseException();
					
					
					VideoModel yt = null;
					String title = "";
					int count = 0;
					for(Element video : videoGroup) {
						yt = new VideoModel();
						Element videoTitle = video.getElementsByClass(conf.CLASS_VIDEO_TITLE).get(0);
						title = videoTitle.text();
						
						String relName = this.formatQueryString(release.getName(),tentativi);
						boolean similarity = SymusicUtility.compareStringSimilarity(relName, title, LevelSimilarity.ALTO);
						if(!similarity) {
							continue;
						}
						
						yt.setName(title);
						
						String href = videoTitle.child(0).attr("href");
						yt.setLink(conf.URL+href.substring(1));
						
						try {
							String eta = video.getElementsByClass(conf.CLASS_VIDEO_META).get(0).getElementsByTag("li").get(1).text();
							yt.setEta(eta);
						} catch (Exception e) {
							log.warn("Impossibile recuperare il dato eta' del video = "+title);
						}
						
						
						// AGGIUNGE IL VIDEO ALLA LISTA SOLO SE NON E' PRESENTE
						// (NEL CASO DI RECUPERO DA DB POTREBBE GIA' ESSERCI)
						log.info("ID_RELEASE="+idRel+"\tVIDEO:  "+yt);
						release.addVideo(yt);
						
						// salva solo MAX_VIDEO_EXTRACT video
						count++;
						if(count>=conf.MAX_VIDEO_EXTRACT)
							break;
					}

					if(release.getVideos().isEmpty()) {
						trovato = false;
						tentativi++;
					} else
						trovato = true;
				} catch (Exception e1) {
					tentativi++;
				}
				
			} while(tentativi<2 && !trovato);
			
			if(videoGroup==null || videoGroup.isEmpty())
				throw new ParseReleaseException("Nessun risultato ottenuto per la release = "+release);
			
			
			// salva sul db
			VideoService vserv = new VideoService();
			vserv.saveVideos(release.getVideos(), release.getId());
			
			
		} catch (ParseReleaseException e) {
			log.error("Errore di parsing durante il salvataggio dei dati estratti");
			throw e;
		} catch (BackEndException e) {
			log.error("Errore di backend durante il salvataggio dei dati estratti");
			throw e;
		}

		return true;
		
		
	}
	
	private String getUrlConnection(ReleaseModel release, int tentativi) {
		String rel = release.getName();
		if(release.getArtist()!=null && release.getSong()!=null)
			rel = release.getArtist()+" "+release.getSong();
		String query = this.formatQueryString(rel,tentativi);

		// pagina di inizio
		return conf.URL+conf.URL_ACTION+"?"+conf.PARAMS.replace("{0}", query);
	}


	public void addManualSearchLink(ReleaseModel release) {
		
		VideoModel yt = new VideoModel();
		yt.setLink(this.getUrlConnection(release, 0));
		yt.setName("[......CERCA SU YOUTUBE......]");
		release.addVideo(yt);
		
	}
	
	protected String applyFilterSearch(String t) {
		t = t.replace("-", " ").replace(",", " ").replace(" feat ", " ").replace(" ft ", " ")
				.replace(".", " ").replace("  ", " ").replace(" and ", " ").replace(" ", "+");
		return t;
	}
	
}
