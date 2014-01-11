package it.fivano.symusic.core.parser;

import it.fivano.symusic.SymusicUtility;
import it.fivano.symusic.SymusicUtility.LevelSimilarity;
import it.fivano.symusic.conf.BeatportConf;
import it.fivano.symusic.core.parser.model.BeatportParserModel;
import it.fivano.symusic.core.parser.model.ScenelogParserModel;
import it.fivano.symusic.exception.BackEndException;
import it.fivano.symusic.exception.ParseReleaseException;
import it.fivano.symusic.model.GenreModel;
import it.fivano.symusic.model.ReleaseModel;
import it.fivano.symusic.model.TrackModel;
import it.fivano.symusic.model.ReleaseExtractionModel.AreaExtraction;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;

public class BeatportParser extends GenericParser {
	
	private BeatportConf conf;
	
	public BeatportParser() throws IOException {
		conf = new BeatportConf();
		this.setLogger(getClass());
	}
	
	public List<BeatportParserModel> searchRelease(String releaseName) throws ParseReleaseException {
		
		List<BeatportParserModel> result = new ArrayList<BeatportParserModel>();
		try {
			int tentativi = 0;
			boolean trovato = false;
			String originalReleaseName = releaseName;
			
			Element releaseGroup = null;
			Document doc = null;
			releaseName = this.getReleaseNameWithoutUnderscore(releaseName);
			do  {
				try {
					String query = this.formatQueryString(releaseName,tentativi);
					
					// pagina di inizio
					String urlConn = conf.URL+conf.URL_ACTION+"?"+conf.PARAMS.replace("{0}", query);
					log.info("Connessione in corso --> "+urlConn);
					doc = Jsoup.connect(urlConn).timeout(TIMEOUT).get();
					
					try {
						releaseGroup = doc.getElementsByAttributeValue(conf.ATTR_RELEASE_NAME,conf.ATTR_RELEASE_VALUE).get(0);
					} catch (Exception e1) {
						releaseGroup = doc.getElementsByClass(conf.CLASS_RELEASE_LIST).get(0);
					}

					trovato = true;
				} catch (Exception e1) {
					tentativi++;
				}
				
			} while(tentativi<2 && !trovato);
			
			if(releaseGroup==null) {
				log.warn("[BEATPORT] Nessun risultato ottenuto per la release = "+originalReleaseName);
				return result;
			}
			
			Map<Double,BeatportParserModel> mappaSimilarity = new TreeMap<Double,BeatportParserModel>(); // TODO ordinare risultati similarita e prendere il piu vicino
			Elements releaseList = releaseGroup.getElementsByClass(conf.CLASS_RELEASE_ITEM);
			BeatportParserModel tmp = null;
			for(Element e : releaseList) {
				tmp = this.popolaBeatport(e, originalReleaseName);

				if(tmp!=null) {
					mappaSimilarity.put(tmp.getLevelSimilarity(), tmp);
					log.warn("[BEATPORT] \tRelease candidata = "+tmp);
				}
					
				
			}
			
			result = new ArrayList<BeatportParserModel>(mappaSimilarity.values());
			Collections.reverse(result);
			
			log.info("[BEATPORT] Trovati "+result.size()+" risultati per la release '"+releaseName+"'");
			return result;
			
		} catch(Exception e) {
			log.error("Errore nel parsing", e);
			throw new ParseReleaseException("Errore nel parsing",e);
		}
		
	}
	
	public ReleaseModel parseReleaseDetails(BeatportParserModel beatportModel, ReleaseModel release) throws ParseReleaseException {
		
		Document doc = null;
		try {

			if(beatportModel==null)
				return release;

			// SE RELEASE ANCORA NON PRESENTE SI CREA L'OGGETTO
			if(release==null)
				release = new ReleaseModel();
			
			release = this.popolaRelease(release, beatportModel);

			// release trovata
			doc = Jsoup.connect(beatportModel.getUrlReleaseDetails()).get();
			
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
			
//			if(!releaseTracks.isEmpty()) { // reset tracks se presenti su beatport (sono più dettagliate)
//				release.setTracks(new ArrayList<TrackModel>());
//			}
			
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
					log.warn("[BEATPORT] Errore nel recupero dei dati durata e bpm");
				}
				
				
				listTrack.add(currTrack);
				log.info("[BEATPORT] \t TRACK:  "+numTr+"."+currTrack);
				
				numTr++;
			}
			
//			System.out.println("\tSCENELOG_TRACK: "+release.getTracks().size()+" "+release.getTracks());
//			System.out.println("\tBEATPORT_TRACK: "+listTrack.size()+" "+listTrack);

			release.setTracks(SymusicUtility.chooseTrack(release.getTracks(), listTrack, false));
			
			// prova a recuperare il genere dalle tracce, se e' stato recuperato da beatport
			if(release.getGenre()==null)
				this.popolaGenre(release);
			
			SymusicUtility.updateReleaseExtraction(release.getReleaseExtraction(),true,AreaExtraction.BEATPORT);

		} catch(Exception e) {
			log.error("Errore nel parsing", e);
			SymusicUtility.updateReleaseExtraction(release.getReleaseExtraction(),false,AreaExtraction.BEATPORT);
//			throw new ParseReleaseException("Errore nel parsing",e);
		}
		
		return release;
		
	}
	
	public Map<String,String> getAllGenre() throws ParseReleaseException {
		
		Document doc = null;
		try {
			
			String urlConn = conf.URL;
			log.info("Connessione in corso --> "+urlConn);
			doc = Jsoup.connect(urlConn).timeout(TIMEOUT).get();
			
			Map<String,String> result = new TreeMap<String, String>();
			Elements genreGroup = doc.getElementsByClass(conf.GENRE_LIST).get(0).getElementsByTag("a");
			
			for(Element el : genreGroup) {
				
				result.put(el.text(), el.attr("href"));
			}
			
			return result;
			
		} catch(Exception e) {
			log.error("Errore nel parsing", e);
			throw new ParseReleaseException("Errore nel parsing",e);
		}
		
		
	}
	
	public List<BeatportParserModel> searchNewReleases(String url) throws ParseReleaseException {
		
		Document doc = null;
		List<BeatportParserModel> result = new ArrayList<BeatportParserModel>();
		try {
			
			log.info("Connessione in corso --> "+url);
			doc = Jsoup.connect(url).timeout(TIMEOUT).get();
			
			
			Elements genreGroup = doc.select(conf.GENRE_NEW_RELEASE);
			
			BeatportParserModel tmp = null;
			for(Element el : genreGroup) {
				
				String title = el.getElementsByClass(conf.GENRE_NEW_RELEASE_ITEM_TITLE).get(0).text();
				String urlRelease = el.getElementsByClass(conf.GENRE_NEW_RELEASE_ITEM_TITLE).get(0).attr("href");
				if(urlRelease.startsWith("/") && conf.URL.endsWith("/")) 
					urlRelease = urlRelease.substring(1);
				urlRelease = conf.URL+urlRelease;
				String author = el.getElementsByClass(conf.GENRE_NEW_RELEASE_ITEM_AUTHOR).get(0).text();
				
				tmp = new BeatportParserModel();
				tmp.setArtist(author);
				tmp.setTitle(title);
				tmp.setUrlReleaseDetails(urlRelease);
				result.add(tmp);
			}
			
			return result;
			
		} catch(Exception e) {
			log.error("Errore nel parsing", e);
			throw new ParseReleaseException("Errore nel parsing",e);
		}
	}
	
	private BeatportParserModel popolaBeatport(Element e, String releaseName) {
		
		Element title = e.getElementsByClass(conf.CLASS_RELEASE_TITLE).get(0);
		// concatena titolo release con l'autore ed applica la similarità
		String author = e.getElementsByClass(conf.CLASS_RELEASE_AUTHOR).get(0).text();
		String titleString = author+"-"+title.text();
		
		String releaseNameSearch = this.formatQueryString(releaseName,0).replace("_", " ");
		double simil = SymusicUtility.getStringSimilarity(releaseNameSearch,titleString,LevelSimilarity.ALTO);
		String releaseLink = title.attr("href");
		if(simil!=0) {
			BeatportParserModel res = new BeatportParserModel();
			
			res.setArtist(author);
			res.setTitle(title.text());
			res.setLevelSimilarity(simil);
			res.setUrlReleaseDetails(releaseLink);
			res.setReleaseName(releaseName);
			
			return res;
			
		} else
			return null;

	}
	
	private ReleaseModel popolaRelease(ReleaseModel release, BeatportParserModel beatportModel) throws ParseException {
		release.setArtist(beatportModel.getArtist());
		release.setSong(beatportModel.getTitle());
		if(release.getNameWithUnderscore()==null && beatportModel.getReleaseName()!=null) {
			release.setNameWithUnderscore(beatportModel.getReleaseName());
			release.setName(beatportModel.getReleaseName().replace("_", " "));
			
			// AGGIUNGE ULTERIORI INFO DELLA RELEASE A PARTIRE DAL NOME
			// ES. CREW E ANNO RELEASE
			SymusicUtility.processReleaseName(release);
		}

		return release;
	}
	
	private void popolaGenre(ReleaseModel release) {
		Map<String,Integer> result = new HashMap<String, Integer>();
		String trackGenre = null;
		for(TrackModel track : release.getTracks()) {
			trackGenre = track.getGenere().trim();
			if(trackGenre!=null) {
				Integer count = result.remove(trackGenre);
				if(count == null) count = 1;
				else count++;
				result.put(trackGenre, count);
			}
		}
		if(!result.isEmpty()) {
			
			String genre = null;
			Integer count = 0;
			Set<String> keyset = result.keySet();
			log.info("[BEATPORT] Lista Generi: "+keyset);
			for(String currGenre : keyset) {
				if(result.get(currGenre)>count) {
					count = result.get(currGenre);
					genre = currGenre;
				}
			}
			log.info("[BEATPORT] Genere candidato: "+genre+" per la release "+release);
			
			GenreModel g = new GenreModel();
			g.setName(genre.trim());
			release.setGenre(g);
		}
	}

	@Override
	protected String applyFilterSearch(String result) {
		return result.replace(" ", "+");
	}

	
	public static void main(String[] args) throws Exception {
		
		
		/**
		ScenelogParser p = new ScenelogParser();
				
		List<ScenelogParserModel> m = p.parseFullPage("http://scenelog.eu/music/page/4/");
		
		for(ScenelogParserModel tt : m) {
			
			ReleaseModel mm = p.parseReleaseDetails(tt, null);
			
			System.out.println(mm);
			
			BeatportParser p2 = new BeatportParser();
			List<BeatportParserModel> m2 = p2.searchRelease(mm.getNameWithUnderscore());
			
			for(BeatportParserModel tmp : m2) {
				System.out.println("TROVATO: "+tmp);
			}
			if(!m2.isEmpty()) {
				mm = p2.parseReleaseDetails(m2.get(0), mm);
				System.out.println("\t"+mm.getTracks());
			}
			
			
		}
		*/
		
		BeatportParser p2 = new BeatportParser();
		List<BeatportParserModel> res = p2.searchNewReleases("http://www.beatport.com/genre/trance/7");
		System.out.println(res);

	}
}
