package it.fivano.symusic.core;

import it.fivano.symusic.SymusicUtility;
import it.fivano.symusic.conf.ZeroDayMusicConf;
import it.fivano.symusic.exception.ParseReleaseException;
import it.fivano.symusic.model.LinkModel;
import it.fivano.symusic.model.ReleaseModel;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Release0DayMusicService {
	
	private ZeroDayMusicConf conf;
	
	Logger log = Logger.getLogger(getClass());
	
	public Release0DayMusicService() throws IOException {
		conf = new ZeroDayMusicConf();
	}
	
	
	public List<ReleaseModel> parse0DayMusicRelease(String genere, Date da, Date a) {
		
		List<ReleaseModel> listRelease = null;
		
		try {
			
			// pagina di inizio
			String urlConn = conf.URL+conf.URL_ACTION+"?"+conf.PARAMS.replace("{0}", genere);
			ZeroDayMusicInfo info = new ZeroDayMusicInfo();
			info.setProcessNextPage(true);
			
			listRelease = this.parse0DayMusic(urlConn, da, a, info);
			
			// se c'� da recuperare altre release, cambia pagina
			while(info.isProcessNextPage()) {
				
				log.info("Andiamo alla pagina successiva...");
				
				listRelease.addAll(this.parse0DayMusic(info.getNextPage(), da, a, info));
				
			}
			
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return listRelease;
		
	}
	
	
	
	private List<ReleaseModel> parse0DayMusic(String urlConn, Date da, Date a, ZeroDayMusicInfo info) throws IOException, ParseException {
		
		List<ReleaseModel> listRelease = new ArrayList<ReleaseModel>();
		
		try {
			
			if(urlConn == null)
				return listRelease;
			
			ReleaseModel release = null;
			LinkModel link = null;
			
			log.info("Connessione in corso --> "+urlConn);
			Document doc = Jsoup.connect(urlConn).get();
			
			info.setNextPage(this.extractNextPage(doc));

			Elements releaseGroup = doc.getElementsByAttributeValue("id",conf.ID_RELEASE_GROUP);
			for(Element e : releaseGroup) {
				
				// date release
				String dateIn = this.getStandardDateFormat(e.parent().getElementById(conf.ID_DAY).text());
				Date dateInDate = SymusicUtility.getStandardDate(dateIn);
				
				// bisogna recuperare ancora altri giorni di release
				if(da.compareTo(dateInDate)>0)
					info.setProcessNextPage(false);
				
				// range data, solo le release comprese da - a
				if(!this.downloadReleaseDay(dateInDate, da, a)) {
					continue;
				}
				
				
				
				Elements links = e.getElementsByTag("a");
				for(Element linkDoc : links) {
					release = new ReleaseModel();
					link = new LinkModel();
					
					// name
					String title = linkDoc.attr("title");
					release.setName(title.replace("_", " "));
					release.setNameWithUnderscore(title.replace(" ", "_"));
					
					// link
					link.setLink(linkDoc.attr("href"));
					release.addLink(link);
					
					// release date
					release.setReleaseDate(dateIn);
					
					SymusicUtility.processReleaseName(release);
					log.info("#####################");
					log.info("|"+release+"|");
					
					try {
						// recupera dati da beatport per il dettaglio della release
						BeatportService beatport = new BeatportService();
						beatport.parseBeatport(release);
						
					} catch (ParseReleaseException e1) {
						log.warn("BeatportService fallito!");
					}
					
					try {
						// recupera dati da beatport per il dettaglio della release
						YoutubeService youtube = new YoutubeService();
						youtube.extractYoutubeVideo(release);
						
					} catch (ParseReleaseException e1) {
						log.warn("YoutubeService fallito!");
					}
					
					listRelease.add(release);
				}
				
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		}

		return listRelease;
		
		
	}
	
	private String extractNextPage(Document doc) {
		
		Element docSub = doc.getElementsByAttributeValue("id",conf.ID_NEXT_PAGES).get(0);
		String currPage = docSub.getElementsByTag("span").get(0).text();
		Elements nextPages = docSub.getElementsByTag("a");
		for(Element page : nextPages) {
			int pageNumb;
			try {
				pageNumb = Integer.parseInt(page.text());
			} catch (NumberFormatException e) {
				continue;
			}
			if(Integer.parseInt(currPage)+1 == pageNumb) {
				return conf.URL+page.attr("href");
			}
		}
		return null;
	}


	private boolean downloadReleaseDay(Date dateInDate, Date da, Date a) {
		boolean result = (dateInDate.compareTo(da)>=0) && (dateInDate.compareTo(a)<=0);
		return result;
	}


	private String getStandardDateFormat(String dateIn) throws ParseException {
		
		String zeroDayFormat = conf.DAY_FORMAT;
		
		return SymusicUtility.getStandardDateFormat(dateIn, zeroDayFormat);
	
	}

	public static void main(String[] args) throws IOException, ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		Date da = sdf.parse("20130802");
		Date a = sdf.parse("20130803");
		
		Release0DayMusicService s = new Release0DayMusicService();
		List<ReleaseModel> res = s.parse0DayMusicRelease("trance",da,a);
		for(ReleaseModel r : res)
			System.out.println(r);
	}
}

class ZeroDayMusicInfo {
	
	private String nextPage;
	private boolean processNextPage;
	
	public String getNextPage() {
		return nextPage;
	}
	public void setNextPage(String nextPage) {
		this.nextPage = nextPage;
	}
	public boolean isProcessNextPage() {
		return processNextPage;
	}
	public void setProcessNextPage(boolean processNextPage) {
		this.processNextPage = processNextPage;
	}
	
	
	
	
}
