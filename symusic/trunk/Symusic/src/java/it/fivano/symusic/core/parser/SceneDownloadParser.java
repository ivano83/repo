package it.fivano.symusic.core.parser;

import it.fivano.symusic.SymusicUtility;
import it.fivano.symusic.SymusicUtility.LevelSimilarity;
import it.fivano.symusic.conf.PreDbConf;
import it.fivano.symusic.conf.PresceneConf;
import it.fivano.symusic.conf.SceneDownloadConf;
import it.fivano.symusic.core.parser.model.BaseReleaseParserModel;
import it.fivano.symusic.core.parser.model.BaseMusicParserModel;
import it.fivano.symusic.exception.ParseReleaseException;
import it.fivano.symusic.model.GenreModel;
import it.fivano.symusic.model.LinkModel;
import it.fivano.symusic.model.GenreModel;
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

public class SceneDownloadParser extends GenericParser {

	private SceneDownloadConf conf;


	public SceneDownloadParser() throws IOException {
		conf = new SceneDownloadConf();
		this.setLogger(getClass());
	}

	public ReleaseModel parseReleaseDetails(ReleaseModel release) throws ParseReleaseException {


		try {
			String urlPage = conf.URL+"?"+conf.PARAMS_SEARCH.replace("{0}", release.getNameWithUnderscore());


			// CONNESSIONE ALLA PAGINA
			String userAgent = this.randomUserAgent();
			log.info("Connessione in corso --> "+urlPage);
			Document doc = Jsoup.connect(urlPage).timeout(TIMEOUT).userAgent(userAgent).ignoreHttpErrors(true).get();

			if(antiDDOS.isAntiDDOS(doc)) {
				doc = this.bypassAntiDDOS(doc, conf.BASE_URL, urlPage, userAgent);
			}
//			else if(doc.text().contains("wait 4 seconds then reload page") ||
//					doc.text().contains("preparing https://prescene.tk")) {
//				Thread.sleep(4100);
//				doc = Jsoup.connect(urlPage).timeout(TIMEOUT).userAgent(userAgent).ignoreHttpErrors(true).get();
//			}

			Elements releaseGroup = doc.getElementsByClass(conf.CLASS_RELEASE_ITEM);

			if(releaseGroup.size()>0) {

				Elements releaseRow = releaseGroup.get(0).children();

				if(releaseRow.size()>3) {

					GenreModel genre = new GenreModel();
					genre.setName(releaseRow.get(1).text().split("\\|")[0]);
					release.setGenre(genre);

					Element dl = releaseRow.get(2).select("a").get(0);
					release.addLink(this.popolateLink(dl));
				}


			}
			else {
				log.info("[SCENEDOWNLAOD] Release non trovata: "+release);
			}
		} catch (IOException e) {
			log.error("Errore IO", e);
			throw new ParseReleaseException("Errore IO",e);
		} catch (ParseException e) {
			log.error("Errore nel parsing", e);
			throw new ParseReleaseException("Errore nel parsing",e);
		} catch (Exception e) {
			log.error("Errore generico", e);
			throw new ParseReleaseException("Errore generico",e);
		}


		return release;

	}

	private ReleaseModel popolaRelease(ReleaseModel release, BaseMusicParserModel musicDLModel) throws ParseException {
		if(musicDLModel.getReleaseName()!=null) {
			release.setNameWithUnderscore(musicDLModel.getReleaseName());

			if(release.getArtist()!=null && release.getSong()!=null) {
				release.setName(release.getArtist()+" - "+release.getSong());
			} else {
				release.setName(musicDLModel.getReleaseName().replace("_", " "));
			}
		}
		release.setReleaseDate(SymusicUtility.getStandardDate(musicDLModel.getReleaseDate()));

		// AGGIUNGE ULTERIORI INFO DELLA RELEASE A PARTIRE DAL NOME
		// ES. CREW E ANNO RELEASE
		SymusicUtility.processReleaseName(release);

		return release;
	}



	private String standardFormatRelease(String text) {
		// TODO Auto-generated method stub
		return text;
	}

	private String getStandardDateFormat(String dateIn) throws ParseException {

		String zeroDayFormat = conf.DATE_FORMAT;

		return SymusicUtility.getStandardDateFormat(dateIn, zeroDayFormat);

	}

	private String createSearchString(String releaseName) {
		if(releaseName.contains("_"))
			return releaseName;
		else
			return this.applyFilterSearch(releaseName);
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

	public static void main(String[] args) throws IOException, ParseReleaseException {
		SceneDownloadParser p = new SceneDownloadParser();
		ReleaseModel rel = new ReleaseModel();
		rel.setNameWithUnderscore("Karel_Ullner_-_We_Get_High-(425123_4332641)-WEB-2017-ZzZz");
		rel = p.parseReleaseDetails(rel);
		System.out.println(rel);
	}

}
