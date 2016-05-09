package it.fivano.symusic.core.parser;

import it.fivano.symusic.conf.Mp3TrackzConf;
import it.fivano.symusic.exception.ParseReleaseException;
import it.fivano.symusic.model.ReleaseModel;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Mp3TrackzParser extends GenericParser {

	private Mp3TrackzConf conf;


	public Mp3TrackzParser() throws IOException {
		conf = new Mp3TrackzConf();
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
			log.info("[MP3_TRACKZ] \t connecting to:  "+url);
			doc = Jsoup.connect(url).userAgent(userAgent).ignoreHttpErrors(true).get();

			if(antiDDOS.isAntiDDOS(doc)) {
				doc = this.bypassAntiDDOS(doc, conf.URL, url,userAgent);
			}

			Element releaseInfo = doc.getElementsByClass(conf.CLASS_RELEASE).get(0);
			Elements listInfo = releaseInfo.select("a");

			for(Element e : listInfo) {
				if(e.text().contains(conf.TITLE_LINK)) {
					release.addLink(this.popolateLink(e));
				}
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
		return conf.URL_RELEASE+(createSearchString(releaseName))+".html";
	}


}
