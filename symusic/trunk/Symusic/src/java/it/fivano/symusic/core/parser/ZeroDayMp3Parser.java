package it.fivano.symusic.core.parser;

import it.fivano.symusic.SymusicUtility;
import it.fivano.symusic.conf.ZeroDayMp3Conf;
import it.fivano.symusic.core.parser.model.ZeroDayMp3ParserModel;
import it.fivano.symusic.exception.ParseReleaseException;
import it.fivano.symusic.model.GenreModel;
import it.fivano.symusic.model.ReleaseModel;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ZeroDayMp3Parser extends GenericParser {

	private ZeroDayMp3Conf conf;


	public ZeroDayMp3Parser() throws IOException {
		conf = new ZeroDayMp3Conf();
		this.setLogger(getClass());
	}

	public List<ZeroDayMp3ParserModel> parseFullPage(String urlPage, Date da, Date a) throws ParseReleaseException {
		this.dataDa = da;
		this.dataA = a;

		return this.parseFullPage(urlPage);

	}

	public List<ZeroDayMp3ParserModel> parseFullPage(String urlPage) throws ParseReleaseException {

		List<ZeroDayMp3ParserModel> result = new ArrayList<ZeroDayMp3ParserModel>();

		if(urlPage == null)
			return result;


		try {
			// CONNESSIONE ALLA PAGINA
			String userAgent = this.randomUserAgent();
			log.info("Connessione in corso --> "+urlPage);
			Document doc = Jsoup.connect(urlPage).timeout(TIMEOUT).userAgent(userAgent).ignoreHttpErrors(true).get();

			if(antiDDOS.isAntiDDOS(doc)) {
				doc = this.bypassAntiDDOS(doc, conf.URL, urlPage, userAgent);
			}

			Elements releaseGroup = doc.getElementsByAttributeValue("id",conf.ID_CONTENT);
			if(releaseGroup.size()==0) {
				releaseGroup = doc.getElementsByClass(conf.ID_CONTENT);
			}
			if(releaseGroup.size()>0) {
				ZeroDayMp3ParserModel release = null;
				log.info("####################################");
				for(Element tmp : releaseGroup) {

					release = this.popolaZeroDayMp3Release(tmp);

					result.add(release);
				}

			}
		} catch (IOException e) {
			log.error("Errore IO", e);
			throw new ParseReleaseException("Errore IO",e);
		} catch (ParseException e) {
			log.error("Errore nel parsing", e);
			throw new ParseReleaseException("Errore nel parsing",e);
		}  catch (Exception e) {
			log.error("Errore generico", e);
			throw new ParseReleaseException("Errore generico",e);
		}

		return result;

	}


	public List<ZeroDayMp3ParserModel> searchRelease(String releaseName) throws ParseReleaseException {

		List<ZeroDayMp3ParserModel> result = new ArrayList<ZeroDayMp3ParserModel>();

		if(releaseName == null)
			return result;


		try {

			int tentativi = 0;
			boolean trovato = false;

			String userAgent = this.randomUserAgent();

			Document doc = null;
			do  {
				try {

					// pagina di inizio
					String releaseNameSearch = this.createSearchString(releaseName);
					String urlConn = conf.URL+conf.SEARCH_ACTION.replace("{0}", releaseNameSearch);
					log.info("Connessione in corso --> "+urlConn);
					doc = Jsoup.connect(urlConn).timeout((tentativi+1)*TIMEOUT).userAgent(userAgent).ignoreHttpErrors(true).get();

					if(antiDDOS.isAntiDDOS(doc)) {
						doc = this.bypassAntiDDOS(doc,conf.URL, urlConn, userAgent);
					}

					Elements releaseGroup = doc.getElementsByAttributeValue("id",conf.ID_CONTENT);
					if(releaseGroup.size()==0) {
						releaseGroup = doc.getElementsByClass(conf.ID_CONTENT);
					}
					if(releaseGroup.size()>0) {
						ZeroDayMp3ParserModel release = null;
						log.info("####################################");
						for(Element tmp : releaseGroup) {

							release = this.popolaZeroDayMp3Release(tmp);

							result.add(release);
						}

					}

					trovato = true;
				} catch (Exception e1) {
					log.error(e1.getMessage(),e1);
					userAgent = this.randomUserAgent(); // proviamo un nuovo user agent
					tentativi++;
				}

			} while(tentativi<1 && !trovato);

		} catch (Exception e) {
			log.error("Errore nel parsing", e);
			throw new ParseReleaseException("Errore nel parsing",e);
		}

		if(result.isEmpty())
			log.info("[0DAYMP3] Nessun risultato ottenuto per la release = "+releaseName);
		else
			log.info("[0DAYMP3] Trovati "+result.size()+" risultati per la release = "+releaseName);

		return result;

	}


	public ReleaseModel parseReleaseDetails(ZeroDayMp3ParserModel zeroDayModel, ReleaseModel release) throws ParseReleaseException {

		try {

			if(zeroDayModel==null)
				return release;

			// SE RELEASE ANCORA NON PRESENTE SI CREA L'OGGETTO
			if(release==null)
				release = new ReleaseModel();

			release = this.popolaRelease(release, zeroDayModel);


		} catch(Exception e) {
			log.error("Errore nel parsing", e);
//			throw new ParseReleaseException("Errore nel parsing",e);
		}

		return release;

	}


	private ReleaseModel popolaRelease(ReleaseModel release, ZeroDayMp3ParserModel zeroDayModel) throws ParseException {
		if(zeroDayModel.getReleaseName()!=null) {
			release.setNameWithUnderscore(zeroDayModel.getReleaseName());
			release.setName(zeroDayModel.getReleaseName().replace("_", " "));
		}
		release.setReleaseDate(SymusicUtility.getStandardDate(zeroDayModel.getReleaseDate()));

		// AGGIUNGE ULTERIORI INFO DELLA RELEASE A PARTIRE DAL NOME
		// ES. CREW E ANNO RELEASE
		SymusicUtility.processReleaseName(release);

		release.setGenre(zeroDayModel.getReleaseGenre());

		release.addLink(zeroDayModel.getReleaseLink());

		return release;
	}

	private ZeroDayMp3ParserModel popolaZeroDayMp3Release(Element tmp) throws ParseException {

		ZeroDayMp3ParserModel release = new ZeroDayMp3ParserModel();

		// IN QUARTA POSIZIONE C'E' LA DATA RELEASE
		Element dateComp = tmp.getElementsByClass(conf.RELEASE_DATE).get(0);
		String date = dateComp.text();
		String dateIn = this.getStandardDateFormat(date);
		if(dateIn.contains("/1970"))
			dateIn = dateIn.replace("/1970", "/"+new SimpleDateFormat("yyyy").format(new Date()));
		Date dateInDate = SymusicUtility.getStandardDate(dateIn);

		// RANGE DATA, SOLO LE RELEASE COMPRESE DA - A
		if(dataDa!=null && dataA!=null) {
			release.setDateInRange(this.downloadReleaseDay(dateInDate, dataDa, dataA));
		}

		// IN PRIMA POSIZIONE C'È IL NOME RELEASE E IL LINK
		Element relComp = tmp.getElementsByClass(conf.RELEASE_LINK).get(0);
		// RELEASE NAME
		String releaseName = tmp.getElementsByClass(conf.RELEASE_NAME).get(0).text();
//		releaseName = releaseName.replace("Permalink to ","");
		release.setReleaseName(releaseName.replace(" – ", " ").replace("–", " "));
		// LINK
		release.setReleaseLink(SymusicUtility.popolateLink(relComp));

		// CONTROLLA SE E' UN RADIO/SAT RIP
		release.setRadioRip(this.isRadioRipRelease(releaseName));

		// IN TERZA POSIZIONE C'È IL GENERE
		Element genreComp = tmp.getElementsByClass(conf.RELEASE_GENRE).get(0);
		String genre = this.genericFilter(genreComp.text());
		GenreModel genreModel = new GenreModel();
		genreModel.setName(genre);
		release.setReleaseGenre(genreModel);

		// DATE RELEASE
		release.setReleaseDate(dateInDate);

		log.info("|"+release+"| acquisita");

		return release;
	}

		private String genericFilter(String text) {
			if(text!=null) {
				text = text.replaceAll("[()]", "").trim();
				if(!String.valueOf(text.toCharArray()[0]).matches("[a-zA-Z0-9]")) {
					return text.substring(1);
				}
			}
			return text;
		}

	private String getStandardDateFormat(String dateIn) throws ParseException {

		String zeroDayFormat = conf.DAY_FORMAT;

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
		t = t.replaceAll("[-,!?&']", " ").replace(" feat ", " ").replace(" ft ", " ")
				.replace("  ", " ").replace(" and ", " ").replace(" ", "+");
		return t;
	}

	public static void main(String[] args) throws IOException, ParseReleaseException {
		ZeroDayMp3Parser p = new ZeroDayMp3Parser();
		ZeroDayMp3ParserModel m = p.parseFullPage("http://0daymp3.com/category/trance/").get(0);
		ReleaseModel mm = p.parseReleaseDetails(m, null);

		System.out.println(mm);
	}

}
