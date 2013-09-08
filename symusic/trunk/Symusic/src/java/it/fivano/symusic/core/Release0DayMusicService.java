package it.fivano.symusic.core;

import it.fivano.symusic.SymusicUtility;
import it.fivano.symusic.backend.TransformerUtility;
import it.fivano.symusic.backend.service.LinkService;
import it.fivano.symusic.backend.service.ReleaseService;
import it.fivano.symusic.backend.service.VideoService;
import it.fivano.symusic.conf.ZeroDayMusicConf;
import it.fivano.symusic.exception.BackEndException;
import it.fivano.symusic.exception.ParseReleaseException;
import it.fivano.symusic.model.LinkModel;
import it.fivano.symusic.model.ReleaseModel;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Release0DayMusicService extends BaseService {
	
	private ZeroDayMusicConf conf;
	private boolean enableBeatportService;
	private boolean enableScenelogService;
	private boolean enableYoutubeService;
	
	Logger log = Logger.getLogger(getClass());
	
	public Release0DayMusicService() throws IOException {
		conf = new ZeroDayMusicConf();
		enableBeatportService = true;
		enableScenelogService = true;
		enableYoutubeService = true;
	}
	
	
	public List<ReleaseModel> parse0DayMusicRelease(String genere, Date da, Date a) throws BackEndException {
		
		List<ReleaseModel> listRelease = null;
		
		try {
			
			// pagina di inizio
			String urlConn = conf.URL+conf.URL_ACTION+"?"+conf.PARAMS.replace("{0}", genere);
			ZeroDayMusicInfo info = new ZeroDayMusicInfo();
			info.setProcessNextPage(true);
			
			listRelease = this.parse0DayMusic(urlConn, da, a, info);
			
			// se c'è da recuperare altre release, cambia pagina
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
	
	
	
	private List<ReleaseModel> parse0DayMusic(String urlConn, Date da, Date a, ZeroDayMusicInfo info) throws IOException, ParseException, BackEndException {
		
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
				
				
				int i = 0;
				Elements links = e.getElementsByTag("a");
				for(Element linkDoc : links) {
					release = new ReleaseModel();
					
					// name
					String title = linkDoc.attr("title");
					release.setName(title.replace("_", " "));
					release.setNameWithUnderscore(title.replace(" ", "_"));
					
					// link
					release.addLink(this.popolateLink(linkDoc));
					
					// release date
					release.setReleaseDate(dateIn);
					
					SymusicUtility.processReleaseName(release);
					log.info("#####################");
					log.info("|"+release+"|");
					
					enableYoutubeService = this.verificaAbilitazioneYoutube(release);
					
					boolean isRecuperato = false;
					ReleaseService relServ = new ReleaseService();
					ReleaseModel relDb = relServ.getReleaseFull(release.getNameWithUnderscore());
					if(relDb!=null) {
						enableScenelogService = false;
						enableYoutubeService = false;
						isRecuperato = true;
						release = relDb;
					}
					

					// salva sul db
					if(!isRecuperato) {
						release = relServ.saveRelease(release);
					}
					
					// recupero e inserimento dati sul DB
					// TODO recupero e inserimento dati sul DB
					double rounded = (double) Math.round(new Double(new Random().nextInt(5)*0.8) * 100) / 100;
					release.setVoteAverage(rounded);
					
					if(i==0 || i==3) {
						release.setVoted(true);
						release.setVoteValue(new Random().nextInt(4));
					}
					i++;
					
					
	
					// ########## SCENELOG ############
					try {
						ScenelogService scenelog = new ScenelogService();
						if(enableScenelogService) {
							// recupera dati da Scenelog per la tracklist e link download
							scenelog.parseScenelog(release);
						}
					} catch (ParseReleaseException e1) {
						log.warn("ScenelogService fallito!");
					}
					
					
					// ########## BEATPORT ############
					try {
						if(enableBeatportService) {
							// recupera dati da beatport per il dettaglio della release
							BeatportService beatport = new BeatportService();
							beatport.parseBeatport(release);
						}
						
					} catch (ParseReleaseException e1) {
						log.warn("BeatportService fallito!");
					}
					
					// ########## YOUTUBE ############
					YoutubeService youtube = new YoutubeService();
					try {
						if(enableYoutubeService) {
							// recupera dati da youtube per i video
							youtube.extractYoutubeVideo(release);
						}
						
					} catch (ParseReleaseException e1) {
						log.warn("YoutubeService fallito!");
					}

					
					GoogleService google = new GoogleService();
					google.addManualSearchLink(release);
					youtube.addManualSearchLink(release); // link a youtube per la ricerca manuale
					
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
	
	private boolean verificaAbilitazioneYoutube(ReleaseModel release) {
		
		String name = release.getName().toUpperCase();
		if(name.startsWith("VA-") || name.startsWith("VA -"))
			return false;
		
		return true;
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

	public static void main(String[] args) throws IOException, ParseException, BackEndException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		Date da = sdf.parse("20130802");
		Date a = sdf.parse("20130803");
		
		Release0DayMusicService s = new Release0DayMusicService();
		List<ReleaseModel> res = s.parse0DayMusicRelease("trance",da,a);
		for(ReleaseModel r : res)
			System.out.println(r);
	}


	public boolean isEnableBeatportService() {
		return enableBeatportService;
	}


	public void setEnableBeatportService(boolean enableBeatportService) {
		this.enableBeatportService = enableBeatportService;
	}


	@Override
	protected String applyFilterSearch(String result) {
		return result;
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
