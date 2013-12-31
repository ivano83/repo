package it.fivano.symusic.core.parser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import it.fivano.symusic.SymusicUtility;
import it.fivano.symusic.SymusicUtility.LevelSimilarity;
import it.fivano.symusic.backend.service.VideoService;
import it.fivano.symusic.conf.YoutubeConf;
import it.fivano.symusic.exception.BackEndException;
import it.fivano.symusic.exception.ParseReleaseException;
import it.fivano.symusic.model.ReleaseModel;
import it.fivano.symusic.model.VideoModel;

public class YoutubeParser extends GenericParser {
	
	private YoutubeConf conf;
	
	public YoutubeParser() throws IOException {
		this.conf = new YoutubeConf();
		this.setLogger(getClass());
	}
	
	public List<VideoModel> searchYoutubeVideos(String releaseName) throws ParseReleaseException {
		
		try {
			int tentativi = 0;
			boolean trovato = false;
			
			List<VideoModel> result = new ArrayList<VideoModel>();
			releaseName = this.getReleaseNameWithoutUnderscore(releaseName);
			
			Elements videoGroup = null;
			Document doc = null;
			String urlConn = null;
			do  {
				try {
					
					// pagina di inizio
					urlConn = this.getUrlConnection(releaseName, tentativi);
					log.info("Connessione in corso --> "+urlConn);
					String userAgent = this.randomUserAgent();
					doc = Jsoup.connect(urlConn).timeout(TIMEOUT).userAgent(userAgent).get();
					
					videoGroup = doc.getElementsByClass(conf.CLASS_VIDEO);
					if(videoGroup.isEmpty()) {
						trovato = false;
						tentativi++;
						continue;
//						throw new ParseReleaseException();
					}

					
					
					VideoModel yt = null;
					String title = "";
					int count = 0;
					for(Element video : videoGroup) {
						yt = new VideoModel();
						Element videoTitle = video.getElementsByClass(conf.CLASS_VIDEO_TITLE).get(0);
						title = videoTitle.text();
						
						String relName = this.formatQueryString(releaseName,tentativi);
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
						
						result.add(yt);

						// salva solo MAX_VIDEO_EXTRACT video
						count++;
						if(count>=conf.MAX_VIDEO_EXTRACT)
							break;
					}
					
					if(result.isEmpty()) {
						trovato = false;
						tentativi++;
					} else
						trovato = true;

					
				} catch (Exception e1) {
					tentativi++;
				}
				
			} while(tentativi<2 && !trovato);
			
//			if(videoGroup==null || videoGroup.isEmpty())
//				throw new ParseReleaseException("Nessun risultato ottenuto per la release = "+releaseName);
			
			
			return result;
			
		} catch(Exception e) {
			log.error("Errore nel parsing", e);
			throw new ParseReleaseException("Errore nel parsing",e);
		}

	}
	
	public void addManualSearchLink(ReleaseModel release) {
		
		VideoModel yt = new VideoModel();
		yt.setLink(this.getUrlConnection(release.getName(), 0));
		yt.setName("[......CERCA SU YOUTUBE......]");
		release.addVideo(yt);
		
	}
	
	private String getUrlConnection(String releaseName, int tentativi) {
//		String rel = release.getName();
//		if(release.getArtist()!=null && release.getSong()!=null)
//			rel = release.getArtist()+" "+release.getSong();
		String query = this.formatQueryString(releaseName,tentativi);

		// pagina di inizio
		return conf.URL+conf.URL_ACTION+"?"+conf.PARAMS.replace("{0}", query);
	}

	@Override
	protected String applyFilterSearch(String t) {
		t = t.replace("-", " ").replace(",", " ").replace(" feat ", " ").replace(" ft ", " ")
				.replace(".", " ").replace("  ", " ").replace(" and ", " ").replace(" ", "+");
		return t;
	}
	
	public static void main(String[] args) throws Exception {
		YoutubeParser p = new YoutubeParser();
		List<VideoModel> v = p.searchYoutubeVideos("Crystal_Lake_Feat_Barbie_G_-_Darkness-(HUMF001)-WEB-2013-FMC");
		System.out.println(v);
	}

}
