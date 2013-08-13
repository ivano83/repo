package it.fivano.symusic.core;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import it.fivano.symusic.conf.YoutubeConf;
import it.fivano.symusic.exception.ParseReleaseException;
import it.fivano.symusic.model.ReleaseModel;
import it.fivano.symusic.model.TrackModel;
import it.fivano.symusic.model.VideoModel;


public class YoutubeService {
	
	YoutubeConf conf;
	
	Logger log = Logger.getLogger(getClass());
	
	public YoutubeService() throws IOException {
		conf = new YoutubeConf();
	}

	
	public boolean extractYoutubeVideo(ReleaseModel release) throws ParseReleaseException {
		
		try {
			int tentativi = 0;
			boolean trovato = false;
			
			Elements videoGroup = null;
			Document doc = null;
			do  {
				try {
					String rel = release.getName();
					if(release.getArtist()!=null && release.getSong()!=null)
						rel = release.getArtist()+" "+release.getSong();
					String query = this.formatQueryString(rel,tentativi);

					// pagina di inizio
					String urlConn = conf.URL+conf.URL_ACTION+"?"+conf.PARAMS.replace("{0}", query);
					log.info("Connessione in corso --> "+urlConn);
					doc = Jsoup.connect(urlConn).get();
					
					videoGroup = doc.getElementsByClass(conf.CLASS_VIDEO);
					if(videoGroup.isEmpty())
						throw new ParseReleaseException();
				
					trovato = true;
				} catch (Exception e1) {
					tentativi++;
				}
				
			} while(tentativi<=2 && !trovato);
			
			if(videoGroup==null || videoGroup.isEmpty())
				throw new ParseReleaseException("Nessun risultato ottenuto per la release = "+release);
		
//			log.info("\n");
			VideoModel yt = new VideoModel();
			String title = "";
			int count = 0;
			for(Element video : videoGroup) {
				Element videoTitle = video.getElementsByClass(conf.CLASS_VIDEO_TITLE).get(0);
				title = videoTitle.text();
				yt.setName(title);
				
				String href = videoTitle.child(0).attr("href");
				yt.setLink(conf.URL+href.substring(1));
				
				try {
					String eta = video.getElementsByClass(conf.CLASS_VIDEO_META).get(0).getElementsByTag("li").get(1).text();
					yt.setEta(eta);
				} catch (Exception e) {
					log.warn("Impossibile recuperare il dato eta' del video = "+title);
				}
				
				
				log.info("    VIDEO:  "+yt);
				release.addVideo(yt);
				
				// salva solo MAX_VIDEO_EXTRACT video
				count++;
				if(count>=conf.MAX_VIDEO_EXTRACT)
					break;
			}

			
		} catch (ParseReleaseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
//		} catch (ParseException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
		}

		return true;
		
		
	}
	
	private String formatQueryString(String name, int wordToDelete) {
		
		int index = name.indexOf("(");
		String result = "";
		if(index!=-1)
			return applyFilterSearch(name.substring(0,index));
		
		String[] split = name.split("-");
		
		if(split.length==1) {
			
			result = name;
		}
		else if(split.length==5) {
			
			result = split[0]+"-"+split[1];
		}
		else if(split.length>5) {
			result = split[0]+"-"+split[1]+"-"+split[2];
		}
		else {
			result = split[0]+"-"+split[1];
		}
		
		if(wordToDelete>0) {
			for(int i=0;i<wordToDelete;i++)
				result = result.substring(0,result.lastIndexOf(" "));
		}
		
		return applyFilterSearch(result);
	}
	
	private String applyFilterSearch(String t) {
		t = t.replace("-", " ").replace(",", " ").replace("feat", " ").replace("ft", " ")
				.replace(".", " ").replace("  ", " ").replace(" and ", " ").replace(" ", "+");
		return t;
	}
	
}
