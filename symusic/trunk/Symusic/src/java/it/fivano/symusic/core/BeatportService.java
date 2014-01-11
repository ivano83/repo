package it.fivano.symusic.core;

import it.fivano.symusic.SymusicUtility;
import it.fivano.symusic.SymusicUtility.LevelSimilarity;
import it.fivano.symusic.backend.service.TrackService;
import it.fivano.symusic.conf.BeatportConf;
import it.fivano.symusic.core.parser.BeatportParser;
import it.fivano.symusic.exception.BackEndException;
import it.fivano.symusic.exception.ParseReleaseException;
import it.fivano.symusic.model.ReleaseModel;
import it.fivano.symusic.model.TrackModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class BeatportService extends BaseService {

	private BeatportConf conf;
	
	
	public BeatportService() throws IOException {
		conf = new BeatportConf();
		this.setLogger(getClass());
	}
	
	public boolean parseBeatport(ReleaseModel release) throws ParseReleaseException, BackEndException {
		
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
					doc = Jsoup.connect(urlConn).timeout(TIMEOUT).get();
					
					releaseGroup = doc.getElementsByAttributeValue(conf.ATTR_RELEASE_NAME,conf.ATTR_RELEASE_VALUE).get(0);
				
					trovato = true;
				} catch (Exception e1) {
					tentativi++;
				}
				
			} while(tentativi<2 && !trovato);
			
			if(releaseGroup==null) {
				throw new ParseReleaseException("Nessun risultato ottenuto per la release = "+release);
			}
			
			Map<Double,String> mappaSimilarity = new TreeMap<Double,String>(); // TODO ordinare risultati similarita e prendere il piu vicino
			Elements releaseList = releaseGroup.getElementsByClass(conf.CLASS_RELEASE_ITEM);
			String releaseLink = null;
			for(Element e : releaseList) {
				Element title = e.getElementsByClass(conf.CLASS_RELEASE_TITLE).get(0);
				// concatena titolo release con l'autore ed applica la similarità
				String author = e.getElementsByClass(conf.CLASS_RELEASE_AUTHOR).get(0).text();
				String titleString = author+"-"+title.text();
				
				double simil = SymusicUtility.getStringSimilarity(release.getName(),titleString,LevelSimilarity.ALTO);
				releaseLink = title.attr("href");
				if(simil!=0)
					mappaSimilarity.put(simil, releaseLink);
				
//				if(SymusicUtility.compareStringSimilarity(release.getName(),titleString)) {
//					releaseLink = title.attr("href");
//					log.info("[BEATPORT] Trovata la release: "+titleString+" - "+releaseLink+"\n");
//					break;
//				}
				
			}
			
			List<String> listaSimilarity = new ArrayList<String>(mappaSimilarity.values());
			Collections.reverse(listaSimilarity);
			
			if(listaSimilarity.isEmpty())
				releaseLink = null;
			else {
				releaseLink = listaSimilarity.get(0);
				log.info("[BEATPORT] Trovata la release... LINK: "+releaseLink+"\n");
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

				TrackModel currTrack = null;
				List<TrackModel> listTrack = new ArrayList<TrackModel>();
				Elements releaseTracks = doc.getElementsByClass(conf.TABLE_RELEASE_TRACK);
				
//				if(!releaseTracks.isEmpty()) { // reset tracks se presenti su beatport (sono più dettagliate)
//					release.setTracks(new ArrayList<TrackModel>());
//				}
				
				int numTr = 1;
				for(Element track : releaseTracks) {
					currTrack = new TrackModel();
					// track number
					currTrack.setTrackNumber(numTr);
					// artista
					currTrack.setArtist(release.getArtist());
					// genere
					String genre = track.getElementsByClass(conf.RELEASE_TRACK_GENRE).get(0).text();
					currTrack.setGenere(genre);
					//traccia
					String trackName = "";
					for(Element ee : track.getElementsByClass(conf.RELEASE_TRACK_NAME).get(0).children()) {
						if(ee.attr("class")!= null && ee.attr("class").contains(conf.RELEASE_TRACK_RMX)) {
							trackName += "("+ee.text()+") ";
						}
						else {
							trackName += ee.text()+" ";
						}
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
					
					
					listTrack.add(currTrack);
					log.info("ID_RELEASE="+release.getId()+"\t TRACK:  "+numTr+"."+currTrack);
					
					numTr++;
				}
				
				// TODO controllare se e' il caso di sostituire le traccie o meno
				TrackService lserv = new TrackService();
				if(release.getTracks()!=null && release.getTracks().isEmpty()){
					// scelta tra quelle gia' presenti e quelle recuperate
					release.setTracks(SymusicUtility.chooseTrack(release.getTracks(), listTrack, false));
					lserv.saveTracks(release.getTracks(), release.getId());
				}
				else {
					// scelta tra quelle gia' presenti e quelle recuperate
					release.setTracks(SymusicUtility.chooseTrack(release.getTracks(), listTrack, true));
					lserv.updateTracks(release.getTracks(), release.getId());
					log.info("Aggiornamento track:  ID_RELEASE="+release.getId()+"\t TRACK:  "+numTr+"."+currTrack);
					
				}
				
				
				

			}
			else {
				log.warn("[BEATPORT] Release non trovata = "+release);
				return false;
			}

			
		} catch (IOException e) {
			log.error("[BEATPORT] Errore di parsing");
			throw new ParseReleaseException("[BEATPORT] Errore nel parsing",e);
		} catch (BackEndException e) {
			log.error("[BEATPORT] Errore di backend durante il salvataggio dei dati estratti");
			throw e;
		}

		return true;
		
	}
	
	public Map<String,String> getGenreList() throws ParseReleaseException {
		

		try {
		
			BeatportParser parser = new BeatportParser();
			return parser.getAllGenre();
			
		} catch (Exception e) {
			log.error("[BEATPORT] Errore nel recupero della lista dei generi");
			throw new ParseReleaseException("[BEATPORT] Errore nel recupero della lista dei generi",e);
		} 
	}
	
	protected String applyFilterSearch(String t) {
		return t.replace(" ", "+");
	}
	
	public static void main(String[] args) throws IOException, ParseReleaseException, BackEndException {
		BeatportService s = new BeatportService();
		ReleaseModel r = new ReleaseModel();
		r.setName("Modana Feat. Tay Edwards-Dance The Night Away-WEB-2013-UKHx");
		r.setNameWithUnderscore("Modana_Feat._Tay_Edwards-Dance_The_Night_Away-WEB-2013-UKHx");
//		r.setName("Cyberfactory - Into The Light-WEB-2013-ZzZz");
//		r.setName("Pepe and Shehu feat Morgana - Summer Love-(SYLIFE 167)-WEB-2013-ZzZz");
		s.parseBeatport(r);

	}
	
}
