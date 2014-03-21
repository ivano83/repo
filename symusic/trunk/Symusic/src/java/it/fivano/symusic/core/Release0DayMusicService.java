package it.fivano.symusic.core;

import it.fivano.symusic.SymusicUtility;
import it.fivano.symusic.backend.service.GenreService;
import it.fivano.symusic.conf.ZeroDayMusicConf;
import it.fivano.symusic.core.thread.SupportObject;
import it.fivano.symusic.exception.BackEndException;
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

public class Release0DayMusicService extends ReleaseSiteService {
	
	private ZeroDayMusicConf conf;
	
	private boolean enableBeatportService;
	private boolean enableScenelogService;
	private boolean enableYoutubeService;
	
	public Release0DayMusicService(Long idUser) throws IOException {
		super();
		this.idUser = idUser;
		conf = new ZeroDayMusicConf();
		enableBeatportService = true;
		enableScenelogService = true;
		enableYoutubeService = true;
		this.setLogger(getClass());
	}
	
	
	public List<ReleaseModel> parse0DayMusicRelease(String genere, Date da, Date a) throws BackEndException, ParseReleaseException {
		
		List<ReleaseModel> listRelease = null;
		
		try {
			
			// PAGINA DI INIZIO
			String urlConn = conf.URL+conf.URL_ACTION+"?"+conf.PARAMS.replace("{0}", genere);
			
			// OGGETTO PER GESTIRE IL CARICAMENTO DELLE PAGINE SUCCESSIVE DEL SITO
			ZeroDayMusicInfo info = new ZeroDayMusicInfo();
			info.setProcessNextPage(true);
			
			// PROCESSA LE RELEASE DELLA PRIMA PAGINA
			listRelease = this.parse0DayMusic(urlConn, da, a, info);
			
			// SE C'È DA RECUPERARE ALTRE RELEASE, CAMBIA PAGINA
			while(info.isProcessNextPage()) {
				
				log.info("Andiamo alla pagina successiva...");
				// PROCESSA LE RELEASE DELLE PAGINE SUCCESSIVE
				listRelease.addAll(this.parse0DayMusic(info.getNextPage(), da, a, info));
				
			}
			
			// INIT OGGETTO DI SUPPORTO UNICO PER TUTTI I THREAD
			SupportObject supp = new SupportObject();
			supp.setEnableBeatportService(enableBeatportService);
			supp.setEnableScenelogService(enableScenelogService);
			supp.setEnableYoutubeService(enableYoutubeService);
			supp.setIdUser(idUser);
			
			listRelease = this.arricchimentoRelease(listRelease, supp);
			
		} catch (Exception e) {
			throw new ParseReleaseException("Errore nel parsing delle pagine",e);
		} 

		return listRelease;
		
	}
	
	
	
	private List<ReleaseModel> parse0DayMusic(String urlConn, Date da, Date a, ZeroDayMusicInfo info) throws BackEndException, ParseReleaseException, ParseException, IOException {
		
		List<ReleaseModel> listRelease = new ArrayList<ReleaseModel>();
		
		if(urlConn == null)
			return listRelease;

		// CONNESSIONE ALLA PAGINA
		log.info("Connessione in corso --> "+urlConn);
		Document doc = Jsoup.connect(urlConn).timeout(TIMEOUT).get();

		// SALVA LA URL DELLA PROSSIMA PAGINA (SE NECESSARIA)
		info.setNextPage(this.extractNextPage(doc));

		ReleaseModel release = null;
		Elements releaseGroup = doc.getElementsByAttributeValue("id",conf.ID_RELEASE_GROUP);
		// CICLO PER OGNI GIORNO NELLA PAGINA CORRENTE (RELEASE RAGGRUPPATE PER GIORNO)
		for(Element e : releaseGroup) {

			// DATE RELEASE
			String dateIn = this.getStandardDateFormat(e.parent().getElementById(conf.ID_DAY).text());
			Date dateInDate = SymusicUtility.getStandardDate(dateIn);
			Elements genres = e.getElementsByTag("span");

			// BISOGNA RECUPERARE ANCORA ALTRI GIORNI DI RELEASE?
			if(da.compareTo(dateInDate)>0)
				info.setProcessNextPage(false);

			// RANGE DATA, SOLO LE RELEASE COMPRESE DA - A
			if(!this.downloadReleaseDay(dateInDate, da, a)) {
				continue;
			}

			Elements links = e.getElementsByTag("a");

			// RECUPERA TUTTE LE RIGHE DELLE RELEASE DI UN DETERMINATO GIORNO
			log.info("####################################");
			int count = 0;
			for(Element linkDoc : links) {
				release = new ReleaseModel();

				// RELEASE NAME
				String title = linkDoc.attr("title");
				release.setName(title.replace("_", " "));
				release.setNameWithUnderscore(title.replace(" ", "_"));
				
				if(excludeRipRelease && this.isRadioRipRelease(release)) {
					continue;
				}
				
				if(excludeVA && this.isVARelease(release)) {
					continue;
				}

				// LINK
				release.addLink(SymusicUtility.popolateLink(linkDoc));

				// GENERE
				if(count<genres.size()) {
					Element genre = genres.get(count);
					GenreModel genere = new GenreModel();
					genere.setName(genre.text().replaceAll("[()]", ""));

					GenreService gServ = new GenreService();
					genere = gServ.saveGenre(genere);
					release.setGenre(genere);
				}

				// RELEASE DATE
				release.setReleaseDate(dateIn);
				
				// AGGIUNGE ULTERIORI INFO DELLA RELEASE A PARTIRE DAL NOME
				// ES. CREW E ANNO RELEASE
				SymusicUtility.processReleaseName(release);
				
				log.info("|"+release+"| acquisita");
				log.info("####################################");
								
				listRelease.add(release);
				
				count++;
			}
		}

		return listRelease;
		
		
	}
	



	private String extractNextPage(Document doc) {
		
		Element docSub = doc.getElementsByAttributeValue("id",conf.ID_NEXT_PAGES).get(0);
		String currPage = docSub.getElementsByTag("span").get(0).text();
		Elements nextPages = docSub.getElementsByTag("a");
		for(Element page : nextPages) {
			int pageNumb;
			try {
				pageNumb = Integer.parseInt(page.text());
			} catch (NumberFormatException e) {
				continue;
			}
			if(Integer.parseInt(currPage)+1 == pageNumb) {
				return conf.URL+page.attr("href");
			}
		}
		return null;
	}


	private boolean downloadReleaseDay(Date dateInDate, Date da, Date a) {
		boolean result = (dateInDate.compareTo(da)>=0) && (dateInDate.compareTo(a)<=0);
		return result;
	}


	private String getStandardDateFormat(String dateIn) throws ParseException {
		
		String zeroDayFormat = conf.DAY_FORMAT;
		
		return SymusicUtility.getStandardDateFormat(dateIn, zeroDayFormat);
	
	}

	public static void main(String[] args) throws IOException, ParseException, BackEndException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		Date da = sdf.parse("20130802");
		Date a = sdf.parse("20130803");
		
		Release0DayMusicService s = new Release0DayMusicService(1L);
//		List<ReleaseModel> res = s.parse0DayMusicRelease("trance",da,a);
//		for(ReleaseModel r : res)
//			System.out.println(r);
		
		System.out.println("fhfh( dewdef) fef".replaceAll("[()]", ""));
	}


	public boolean isEnableBeatportService() {
		return enableBeatportService;
	}


	public void setEnableBeatportService(boolean enableBeatportService) {
		this.enableBeatportService = enableBeatportService;
	}


	@Override
	protected String applyFilterSearch(String result) {
		return result;
	}
}

class ZeroDayMusicInfo {
	
	private String nextPage;
	private boolean processNextPage;
	
	public String getNextPage() {
		return nextPage;
	}
	public void setNextPage(String nextPage) {
		this.nextPage = nextPage;
	}
	public boolean isProcessNextPage() {
		return processNextPage;
	}
	public void setProcessNextPage(boolean processNextPage) {
		this.processNextPage = processNextPage;
	}
	
	
	
	
}
