package it.fivano.symusic.core.parser;

import it.fivano.symusic.SymusicUtility;
import it.fivano.symusic.SymusicUtility.LevelSimilarity;
import it.fivano.symusic.backend.service.TrackService;
import it.fivano.symusic.conf.BeatportConf;
import it.fivano.symusic.core.parser.model.BeatportParserModel;
import it.fivano.symusic.core.parser.model.ScenelogParserModel;
import it.fivano.symusic.exception.BackEndException;
import it.fivano.symusic.exception.ParseReleaseException;
import it.fivano.symusic.model.ReleaseModel;
import it.fivano.symusic.model.TrackModel;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
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
			
			Element releaseGroup = null;
			Document doc = null;
			releaseName = releaseName.replace("_", " ");
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
				log.warn("[BEATPORT] Nessun risultato ottenuto per la release = "+releaseName);
				return result;
			}
			
			Map<Double,BeatportParserModel> mappaSimilarity = new TreeMap<Double,BeatportParserModel>(); // TODO ordinare risultati similarita e prendere il piu vicino
			Elements releaseList = releaseGroup.getElementsByClass(conf.CLASS_RELEASE_ITEM);
			BeatportParserModel tmp = null;
			for(Element e : releaseList) {
				tmp = this.popolaBeatport(e, releaseName);

				if(tmp!=null) {
					mappaSimilarity.put(tmp.getLevelSimilarity(), tmp);
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
				release.setTracks(SymusicUtility.chooseTrack(release.getTracks(), listTrack));
				lserv.saveTracks(release.getTracks(), release.getId());
			}
			else {
				// scelta tra quelle gia' presenti e quelle recuperate
				release.setTracks(SymusicUtility.chooseTrack(release.getTracks(), listTrack));
				lserv.updateTracks(release.getTracks(), release.getId());
				log.info("Aggiornamento track:  ID_RELEASE="+release.getId()+"\t TRACK:  "+numTr+"."+currTrack);
				
			}

		} catch(Exception e) {
			log.error("Errore nel parsing", e);
			throw new ParseReleaseException("Errore nel parsing",e);
		}
		
		return release;
		
	}
	
	private BeatportParserModel popolaBeatport(Element e, String releaseName) {
		
		Element title = e.getElementsByClass(conf.CLASS_RELEASE_TITLE).get(0);
		// concatena titolo release con l'autore ed applica la similarità
		String author = e.getElementsByClass(conf.CLASS_RELEASE_AUTHOR).get(0).text();
		String titleString = author+"-"+title.text();
		
		double simil = SymusicUtility.getStringSimilarity(releaseName,titleString,LevelSimilarity.ALTO);
		String releaseLink = title.attr("href");
		if(simil!=0) {
			BeatportParserModel res = new BeatportParserModel();
			
			res.setArtist(author);
			res.setTitle(title.text());
			res.setLevelSimilarity(simil);
			res.setUrlReleaseDetails(releaseLink);
			
			return res;
			
		} else
			return null;

	}
	
	private ReleaseModel popolaRelease(ReleaseModel release, BeatportParserModel beatportModel) throws ParseException {
		release.setArtist(beatportModel.getArtist());
		release.setSong(beatportModel.getTitle());
		if(release.getNameWithUnderscore()==null) {
			release.setNameWithUnderscore(beatportModel.getReleaseName());
			release.setName(beatportModel.getReleaseName().replace("_", " "));
			
			// AGGIUNGE ULTERIORI INFO DELLA RELEASE A PARTIRE DAL NOME
			// ES. CREW E ANNO RELEASE
			SymusicUtility.processReleaseName(release);
		}

		return release;
	}

	@Override
	protected String applyFilterSearch(String result) {
		return result.replace(" ", "+");
	}

	
	public static void main(String[] args) throws Exception {
		ScenelogParser p = new ScenelogParser();
		List<ScenelogParserModel> m = p.parseFullPage("http://scenelog.eu/music/page/3/");
		
		for(ScenelogParserModel tt : m) {
			
			ReleaseModel mm = p.parseReleaseDetails(tt, null);
			
			System.out.println(mm);
			
			BeatportParser p2 = new BeatportParser();
			List<BeatportParserModel> m2 = p2.searchRelease(mm.getNameWithUnderscore());
			
			for(BeatportParserModel tmp : m2) {
				System.out.println(tmp);
			}
			
		}
		
		
		
		
	}
}
