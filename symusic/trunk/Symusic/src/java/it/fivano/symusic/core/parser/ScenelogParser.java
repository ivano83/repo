package it.fivano.symusic.core.parser;

import it.fivano.symusic.SymusicUtility;
import it.fivano.symusic.conf.ScenelogConf;
import it.fivano.symusic.core.BaseService;
import it.fivano.symusic.core.parser.model.ScenelogParserModel;
import it.fivano.symusic.exception.ParseReleaseException;
import it.fivano.symusic.model.ReleaseModel;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ScenelogParser extends GenericParser {
	
	private ScenelogConf conf;
	
	
	public ScenelogParser() throws IOException {
		conf = new ScenelogConf();
		this.setLogger(getClass());
	}
	
	public List<ScenelogParserModel> parseFullPage(String urlPage) throws ParseReleaseException {
		
		List<ScenelogParserModel> result = new ArrayList<ScenelogParserModel>();
		
		if(urlPage == null)
			return result;

		
		try {
			// CONNESSIONE ALLA PAGINA
			String userAgent = this.randomUserAgent();
			log.info("Connessione in corso --> "+urlPage);
			Document doc = Jsoup.connect(urlPage).timeout(TIMEOUT).userAgent(userAgent).get();

			Elements releaseGroup = doc.getElementsByClass(conf.CLASS_RELEASE_LIST_ITEM);
			if(releaseGroup.size()>0) {
				ScenelogParserModel release = null;
				log.info("####################################");
				for(Element tmp : releaseGroup) {
					
					Element releaseItem = tmp.getElementsByTag("h1").get(0).getElementsByTag("a").get(0);
					String releaseName = releaseItem.text();
					String releaseUrl = releaseItem.attr("href");
					
					Element relDate = tmp.getElementsByClass(conf.CLASS_RELEASE_LIST_DATA).get(0);
					String dateIn = relDate.text();
					dateIn = this.getStandardDateFormat(dateIn);
					Date dateInDate = SymusicUtility.getStandardDate(dateIn);
					
					release = new ScenelogParserModel();
					release.setReleaseName(releaseName.replace("_", " "));
					
					System.out.println(dateIn+" - "+ releaseUrl);
					
					release.setUrlReleaseDetails(releaseUrl);
					
					// RELEASE DATE
					release.setReleaseDate(dateInDate);
					
					log.info("|"+release+"| acquisita");
					log.info("####################################");
					
					result.add(release);
				}
				
			}
		} catch (IOException e) {
			log.error("Errore IO", e);
			throw new ParseReleaseException("Errore IO",e);
		} catch (ParseException e) {
			log.error("Errore nel parsing", e);
			throw new ParseReleaseException("Errore nel parsing",e);
		}
		
		return result;
						
	}


	private String getStandardDateFormat(String dateIn) throws ParseException {

		String zeroDayFormat = conf.DATE_FORMAT;

		dateIn = dateIn.replace("th,", "").replace("rd,", "").replace("st,", "");

		return SymusicUtility.getStandardDateFormat(dateIn, zeroDayFormat);

	}
	
	@Override
	protected String applyFilterSearch(String result) {
		return result;
	}
	
	public static void main(String[] args) throws IOException, ParseReleaseException {
		ScenelogParser p = new ScenelogParser();
		p.parseFullPage("http://scenelog.eu/music/");
	}

}
