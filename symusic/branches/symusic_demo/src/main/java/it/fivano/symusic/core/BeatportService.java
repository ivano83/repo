package it.fivano.symusic.core;

import it.fivano.symusic.conf.BeatportConf;
import it.fivano.symusic.exception.ParseReleaseException;
import it.fivano.symusic.model.ReleaseModel;
import it.fivano.symusic.model.TrackModel;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class BeatportService {

	private BeatportConf conf;
	
	Logger log = Logger.getLogger(getClass());
	
	public BeatportService() throws IOException {
		conf = new BeatportConf();
	}
	
	public boolean parseBeatport(ReleaseModel release) throws IOException, ParseReleaseException {
		
		try {
			int tentativi = 0;
			boolean trovato = false;
			
			Element releaseGroup = null;
			Document doc = null;
			do  {
				try {
					String query = this.formatQueryString(release.getName(),tentativi);
					
					// pagina di inizio
					String urlConn = conf.URL+conf.URL_ACTION+"?"+conf.PARAMS.replace("{0}", query);
					log.info("Connessione in corso --> "+urlConn);
					doc = Jsoup.connect(urlConn).get();
					
					releaseGroup = doc.getElementsByAttributeValue(conf.ATTR_RELEASE_NAME,conf.ATTR_RELEASE_VALUE).get(0);
				
					trovato = true;
				} catch (Exception e1) {
					tentativi++;
				}
				
			} while(tentativi<=2 && !trovato);
			
			if(releaseGroup==null) {
				throw new ParseReleaseException("Nessun risultato ottenuto per la release = "+release);
			}
			
			Elements releaseList = releaseGroup.getElementsByClass(conf.CLASS_RELEASE_ITEM);
			String releaseLink = null;
			for(Element e : releaseList) {
				Element title = e.getElementsByClass(conf.CLASS_RELEASE_TITLE).get(0);
				String titleString = title.text();
				if(release.getName().contains(titleString)) {
					releaseLink = title.attr("href");
					log.info("[BEATPORT] Trovata la release: "+titleString+" - "+releaseLink+"\n");
					break;
				}
				
			}
			
			
			if(releaseLink != null) {
				// release trovata
				doc = Jsoup.connect(releaseLink).get();
				
				Element releaseDetail = doc.getElementsByClass(conf.RELEASE_DETAIL).get(0);
				if(releaseDetail.children().size()>2) {
					
					String songName = releaseDetail.child(0).text();
					release.setSong(songName);
					String artistName = releaseDetail.child(1).text();
					release.setArtist(artistName);
				}
				
//				log.info("\n");
				TrackModel currTrack = null;
				Elements releaseTracks = doc.getElementsByClass(conf.TABLE_RELEASE_TRACK);
				for(Element track : releaseTracks) {
					currTrack = new TrackModel();
					// artista
					currTrack.setArtist(release.getArtist());
					// genere
					String genre = track.getElementsByClass(conf.RELEASE_TRACK_GENRE).get(0).text();
					currTrack.setGenere(genre);
					//traccia
					String trackName = "";
					for(Element ee : track.getElementsByClass(conf.RELEASE_TRACK_NAME).get(0).children()) {
						trackName += ee.text()+" ";
					}
					currTrack.setTrackName(trackName.trim());
					
					// durata e bpm
					try {
						Element timeBpm = track.getElementsByTag("td").get(2);
						String[] tb = timeBpm.child(0).text().split("/");
						currTrack.setTime(tb[0].trim());
						currTrack.setBpm(tb[1].trim());
						
					} catch (Exception e1) {
						log.warn("Errore nel recupero dei dati durata e bpm");
					}
					
					
					release.addTrack(currTrack);
					log.info("    DETTAGLIO:  "+currTrack);
				}

			}
			else {
				log.warn("[BEATPORT] Release non trovata = "+release);
				return false;
			}

			
		} catch (IOException e) {
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
			return name.substring(0,index).replace(" ", "+");
		
		String[] split = name.split("-");
		
		if(split.length==5) {
			
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
		
		return result.replace(" ", "+");
	}
	
	public static void main(String[] args) throws IOException, ParseReleaseException {
		BeatportService s = new BeatportService();
		ReleaseModel r = new ReleaseModel();
		r.setName("Houseshaker Feat. Amanda Blush-Light the Sky-(7000042629)-WEB-2013-DWM");
//		r.setName("Cyberfactory - Into The Light-WEB-2013-ZzZz");
//		r.setName("Pepe and Shehu feat Morgana - Summer Love-(SYLIFE 167)-WEB-2013-ZzZz");
		s.parseBeatport(r);

	}
	
}
