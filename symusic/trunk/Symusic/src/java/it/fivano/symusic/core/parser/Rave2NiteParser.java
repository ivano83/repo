package it.fivano.symusic.core.parser;

import it.fivano.symusic.SymusicUtility;
import it.fivano.symusic.conf.Mp3TrackzConf;
import it.fivano.symusic.conf.Rave2NiteConf;
import it.fivano.symusic.core.parser.model.ScenelogParserModel;
import it.fivano.symusic.exception.ParseReleaseException;
import it.fivano.symusic.model.ReleaseModel;
import it.fivano.symusic.model.TrackModel;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;

public class Rave2NiteParser extends GenericParser {

	private Rave2NiteConf conf;


	public Rave2NiteParser() throws IOException {
		conf = new Rave2NiteConf();
		this.setLogger(getClass());
	}

	public ReleaseModel parseReleaseDetails(ReleaseModel release, String releaseName) throws ParseReleaseException {

		Document doc = null;
		try {

			// SE RELEASE ANCORA NON PRESENTE SI CREA L'OGGETTO
			if(release==null)
				release = new ReleaseModel();

			// release trovata
			String userAgent = this.randomUserAgent();
			String url = getUrlRelease(releaseName);
			log.info("[RAVE2NITE] \t connecting to:  "+url);
			doc = Jsoup.connect(url).userAgent(userAgent).ignoreHttpErrors(true).get();

			if(antiDDOS.isAntiDDOS(doc)) {
				doc = this.bypassAntiDDOS(doc, conf.URL, url,userAgent);
			}

			Element releaseInfo = doc.getElementsByClass(conf.CLASS_RELEASE).get(0);
			Elements listInfo = releaseInfo.select("a");

			for(Element e : listInfo) {
				if(e.hasClass(conf.TITLE_LINK)) {
					release.addLink(this.popolateLink(e));
				}
			}

			listInfo = releaseInfo.select("p");
			List<TrackModel> tracks = new ArrayList<TrackModel>();
			if(!listInfo.isEmpty()) {
				String[] tracksSplit = listInfo.get(0).html().split("<br />");
				TrackModel currTrack = null;
				int numTr = 1;
				for(String tx : tracksSplit) {
					if(tx.startsWith("\n"))
						break;
					currTrack = new TrackModel();
					currTrack.setTrackNumber(numTr);
					String text = tx.replaceFirst("\\d+\\.",""); // se c'e' il numero di track, lo elimina
//					System.out.println(text);
					currTrack.setTrackName(text.replaceAll("\n.*", "").trim());
					tracks.add(currTrack);
					log.info("[RAVE2NITE] \t TRACK:  "+numTr+". "+currTrack);
					numTr++;

				}
			}

			if(release.getTracks().isEmpty()) {
				release.setTracks(tracks);
			} else {
				// PRIORITA' ALLE TRACCE SCENELOG
				release.setTracks(SymusicUtility.chooseTrack(tracks, release.getTracks(), true));
			}


		} catch(IndexOutOfBoundsException e) {
			log.error("Nessun risultato ottenuto per la release "+releaseName);
//			SymusicUtility.updateReleaseExtraction(release.getReleaseExtraction(),false,AreaExtraction.SCENELOG);
//			throw new ParseReleaseException("Errore nel parsing",e);
		} catch(Exception e) {
			log.error("Errore nel parsing", e);
//			SymusicUtility.updateReleaseExtraction(release.getReleaseExtraction(),false,AreaExtraction.SCENELOG);
//			throw new ParseReleaseException("Errore nel parsing",e);
		}

		return release;

	}

	private String createSearchString(String releaseName) {
		return releaseName.replaceAll("[^A-Za-z0-9 -_]", "").toLowerCase();

	}

	@Override
	protected String applyFilterSearch(String result) {
		// TODO Auto-generated method stub
		return null;
	}

	public String getUrlRelease(String releaseName) {
		return conf.URL+conf.URL_ACTION+"?"+conf.URL_PARAM.replace("{0}", releaseName);
	}

	public static void main(String[] args) throws IOException, ParseReleaseException {
		Rave2NiteParser p = new Rave2NiteParser();
		ReleaseModel m = p.parseReleaseDetails(new ReleaseModel(), "Armin_Van_Buuren_vs_Vini_Vici_ft_Hilight_Tribe-Great_Spirit-WEB-2016-UKHx");

		System.out.println(m.getLinks());
		System.out.println(m.getTracks());
	}


}
