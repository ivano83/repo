package it.fivano.symusic.core.parser;

import it.fivano.symusic.SymusicUtility;
import it.fivano.symusic.SymusicUtility.LevelSimilarity;
import it.fivano.symusic.conf.MusicDLConf;
import it.fivano.symusic.conf.SceneMusicConf;
import it.fivano.symusic.core.parser.model.BaseReleaseParserModel;
import it.fivano.symusic.core.parser.model.BaseMusicParserModel;
import it.fivano.symusic.exception.ParseReleaseException;
import it.fivano.symusic.model.ReleaseExtractionModel.AreaExtraction;
import it.fivano.symusic.model.ReleaseModel;
import it.fivano.symusic.model.TrackModel;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;

public class SceneMusicParser extends GenericParser {

	private SceneMusicConf conf;


	public SceneMusicParser() throws IOException {
		conf = new SceneMusicConf();
		this.setLogger(getClass());
	}

	public ReleaseModel parseReleaseDetails(BaseReleaseParserModel musicModel, ReleaseModel release) throws ParseReleaseException {

		Document doc = null;
		try {

			if(musicModel==null)
				return release;

			// SE RELEASE ANCORA NON PRESENTE SI CREA L'OGGETTO
			if(release==null)
				release = new ReleaseModel();


			// release trovata
			String userAgent = this.randomUserAgent();
			doc = Jsoup.connect(musicModel.getUrlReleaseDetails()).userAgent(userAgent).ignoreHttpErrors(true).get();

			if(antiDDOS.isAntiDDOS(doc)) {
				doc = this.bypassAntiDDOS(doc, conf.URL, musicModel.getUrlReleaseDetails(), userAgent);
			}

			Elements releaseItems = doc.getElementsByClass(conf.CLASS_RELEASE_ITEM);

			if(releaseItems==null || releaseItems.isEmpty()) {
				throw new ParseReleaseException("[SCENE-MUSIC] Nessun risultato ottenuto per la release = "+musicModel.getReleaseName());
			}

			Elements linkList = releaseItems.get(0).select("h2 > a");

			for(Element e : linkList) {

				this.popolaRelease(release, e);
			}


		} catch(Exception e) {
			log.error("[SCENE-MUSIC] Nessun risultato ottenuto per la release = "+musicModel.getReleaseName()+"  --> "+e.getMessage());
		}

		return release;

	}

	public BaseMusicParserModel searchRelease(String releaseName, ReleaseModel release) throws ParseReleaseException {

		BaseMusicParserModel result = null;

		if(releaseName == null)
			return result;


		try {

			int tentativi = 0;
			boolean trovato = false;

			String userAgent = this.randomUserAgent();

			Elements releaseItems = null;
			Document doc = null;
			do  {
				try {

					// pagina di inizio
					String urlConn = this.getUrlRelease(releaseName, null);
					log.info("Connessione in corso --> "+urlConn);
					doc = Jsoup.connect(urlConn).timeout((tentativi+1)*TIMEOUT).userAgent(userAgent).ignoreHttpErrors(true).get();

					if(antiDDOS.isAntiDDOS(doc)) {
						doc = this.bypassAntiDDOS(doc, conf.URL, urlConn, userAgent);
					}

					releaseItems = doc.getElementsByClass(conf.CLASS_RELEASE_ITEM);

					if(releaseItems==null || releaseItems.isEmpty()) {
						throw new ParseReleaseException("Tentativo "+tentativi+" fallito!");
					}

					trovato = true;
				} catch (Exception e1) {
					log.error("[SCENE-MUSIC] Nessun risultato ottenuto per la release = "+releaseName+"  --> "+e1.getMessage());
					userAgent = this.randomUserAgent(); // proviamo un nuovo user agent
					tentativi++;
				}

			} while(tentativi<1 && !trovato);

			if(releaseItems==null) {
				log.warn("[SCENE-MUSIC] Nessun risultato ottenuto per la release = "+releaseName);
				return null;
//				throw new ParseReleaseException("[SCENE-MUSIC] Nessun risultato ottenuto per la release = "+releaseName);
			}


			Elements linkList = releaseItems.get(0).select("h2 > a");

			for(Element e : linkList) {

				this.popolaRelease(release, e);
			}


		} catch (Exception e) {
			log.error("Errore nel parsing", e);
//			throw new ParseReleaseException("Errore nel parsing",e);
		}

		return result;

	}


	private ReleaseModel popolaRelease(ReleaseModel release, Element el) throws ParseException {

		release.addLink(this.popolateLink(el));

		return release;
	}


//	private String standardFormatRelease(String text) {
//		// TODO Auto-generated method stub
//		return text;
//	}
//
//	private String getStandardDateFormat(String dateIn) throws ParseException {
//
//		String zeroDayFormat = conf.DATE_FORMAT;
//
//		dateIn = dateIn.replace("th,", "").replace("rd,", "").replace("st,", "").replace("nd,", "");
//
//		return SymusicUtility.getStandardDateFormat(dateIn, zeroDayFormat);
//
//	}

	private String createSearchString(String releaseName) {

		return releaseName.replaceAll("[^A-Za-z0-9_()]", "-").replace("_", "-");
//		return releaseName.replaceAll("[^A-Za-z0-9 -_]", "").replace("_", "-");

	}

	@Override
	protected String applyFilterSearch(String t) {
		t = t.replaceAll("[-,!?&']", " ").replace(" feat ", " ").replace(" feat. ", " ").replace(" ft ", " ").replace(" featuring ", " ")
				.replace(" presents ", " ").replace(" pres ", " ").replace(" pres. ", " ")
				.replace("  ", " ").replace(" and ", " ").replace(" ", "+");
		if(t.indexOf("(")!=-1) {
			t = t.substring(0,t.indexOf("("));
		}
		return t;
	}

	public String getUrlRelease(String releaseName, String genre) {
		return conf.URL+(createSearchString(releaseName).toLowerCase())+"/";
	}

	public static void main(String[] args) throws IOException, ParseReleaseException {
		SceneMusicParser p = new SceneMusicParser();
		BaseReleaseParserModel m = new BaseReleaseParserModel();
		m.setReleaseName("Mike Millrain–Work Me EP-(SOULR0018)-WEB-2015-dh");
		m.setUrlReleaseDetails(p.getUrlRelease(m.getReleaseName(), null));
		ReleaseModel res = p.parseReleaseDetails(m, null);

		System.out.println(res);
	}

}
