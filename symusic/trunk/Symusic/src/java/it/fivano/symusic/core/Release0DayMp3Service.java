package it.fivano.symusic.core;

import it.fivano.symusic.SymusicUtility;
import it.fivano.symusic.backend.service.GenreService;
import it.fivano.symusic.conf.ZeroDayMp3Conf;
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
import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Release0DayMp3Service extends ReleaseSiteService {
	
	private ZeroDayMp3Conf conf;
	private String genre;
	
	private List<ReleaseModel> listRelease;
	

	
	public Release0DayMp3Service(Long idUser) throws IOException {
		super();
		this.idUser = idUser;
		conf = new ZeroDayMp3Conf();
		enableBeatportService = true;
		enableScenelogService = true;
		enableYoutubeService = true;
		excludeRipRelease = true;
		this.setLogger(getClass());
	}
	
	
	public List<ReleaseModel> parse0DayMp3Release(String genere, Date da, Date a) throws BackEndException, ParseReleaseException {
		
		this.genre = genere;
		listRelease = new ArrayList<ReleaseModel>();
		try {
			
			// PAGINA DI INIZIO
			String urlConn = conf.URL_CATEGORY+genere;
			
			// OGGETTO PER GESTIRE IL CARICAMENTO DELLE PAGINE SUCCESSIVE DEL SITO
			ScenelogInfo info = new ScenelogInfo();
			info.setProcessNextPage(true);
			
			// PROCESSA LE RELEASE DELLA PRIMA PAGINA
			this.parse0DayMp3(urlConn, da, a, info);
			
			// SE C'È DA RECUPERARE ALTRE RELEASE, CAMBIA PAGINA
			while(info.isProcessNextPage()) {
				
				log.info("Andiamo alla pagina successiva...");
				// PROCESSA LE RELEASE DELLE PAGINE SUCCESSIVE
				this.parse0DayMp3(info.getNextPage(), da, a, info);
				
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
	
	private Document bypassAntiDDOS(Document doc, String urlToRedirect) throws IOException {
		String jschl_vc = doc.getElementsByAttributeValue("name", "jschl_vc").get(0).attr("value");
//		System.out.println(jschl_vc);
		Elements scriptElements = doc.getElementsByTag("script");
		String numberCalcLine = null;
		for (Element element :scriptElements ){                
			for (DataNode node : element.dataNodes()) {
				String text = node.getWholeData();
				String[] lines = text.split("\n");
				for(String scriptLine : lines) {
					if(scriptLine.trim().startsWith("a.value = ")) {
						numberCalcLine = scriptLine.replace(";", "").replace("a.value = ", "").trim();
						break;
					}
				}

			}
//			System.out.println(numberCalcLine);            
		}
		
		int jschl_answer = 0;
		if(numberCalcLine!=null) {
			String[] addizioni = numberCalcLine.split("\\+");
			int i1,i2,i3;
			i1 = Integer.parseInt(addizioni[0]);
			String[] moltipl = addizioni[1].split("\\*");
			i2 = Integer.parseInt(moltipl[0]);
			i3 = Integer.parseInt(moltipl[1]);
			
			jschl_answer = ((i2*i3)+i1)+11;
		}
		
		String urlPage = conf.URL+"cdn-cgi/l/chk_jschl";
		String userAgent = this.randomUserAgent();
		doc = Jsoup.connect(urlPage).timeout(TIMEOUT).header("Referer", urlToRedirect).userAgent(userAgent).data("jschl_vc", jschl_vc).data("jschl_answer", jschl_answer+"").ignoreHttpErrors(true).get();
		

		return doc;
	}
	
	private boolean isAntiDDOS(Document doc) {
		Elements res = doc.getElementsByClass("cf-browser-verification");
		log.info("DDOS protection: "+(res.size()==0 ? false : true));
		return res.size()==0 ? false : true;
	}
	
	private void parse0DayMp3(String urlConn, Date da, Date a, ScenelogInfo info) throws BackEndException, ParseReleaseException, ParseException, IOException {
		
//		List<ReleaseModel> listRelease = new ArrayList<ReleaseModel>();
		
		if(urlConn == null)
			return;
		
		
		// CONNESSIONE ALLA PAGINA
		log.info("Connessione in corso --> "+urlConn);
		String userAgent = this.randomUserAgent();
		Document doc = Jsoup.connect(urlConn).timeout(TIMEOUT).userAgent(userAgent).ignoreHttpErrors(true).get();
				
		if(this.isAntiDDOS(doc)) {
			doc = this.bypassAntiDDOS(doc, urlConn);
		}


		// SALVA LA URL DELLA PROSSIMA PAGINA (SE NECESSARIA)
		info.changePage(); // AGGIORNA IL NUMERO PAGINA
		info.setNextPage(this.extractNextPage(info));

		Elements releaseGroup = doc.getElementsByAttributeValue("id",conf.ID_CONTENT);
		if(releaseGroup.size()>0) {
			ReleaseModel release = null;
			// OGNI TABLE CONTIENE UNA RELEASE
			Elements tables = releaseGroup.get(0).getElementsByTag("table");
			log.info("####################################");
			for(Element relTable : tables) {
				
				Elements components = relTable.getElementsByTag("td");
				if(components.size()>=4) {
					// OK CI SONO TUTTI I PEZZI
					
					// IN QUARTA POSIZIONE C'E' LA DATA RELEASE
					Element dateComp = components.get(3);
					String date = this.genericFilter(dateComp.text());
					String dateIn = this.getStandardDateFormat(date);
					Date dateInDate = SymusicUtility.getStandardDate(dateIn);
					
					// BISOGNA RECUPERARE ANCORA ALTRI GIORNI DI RELEASE?
					if(da.compareTo(dateInDate)>0)
						info.setProcessNextPage(false);

					// RANGE DATA, SOLO LE RELEASE COMPRESE DA - A
					if(!this.downloadReleaseDay(dateInDate, da, a)) {
						continue;
					}
					
					release = new ReleaseModel();
					
					// IN PRIMA POSIZIONE C'È IL NOME RELEASE E IL LINK
					Element relComp = components.get(0);
					// RELEASE NAME
					String title = relComp.getElementsByTag("a").get(0).attr("title");
					title = title.replace("Permalink to ","");
					release.setName(title.replace("_", " "));
					release.setNameWithUnderscore(title.replace(" ", "_"));
					
					if(excludeRipRelease && this.isRadioRipRelease(release)) {
						continue;
					}
					
					// IN TERZA POSIZIONE C'È IL GENERE
					Element genreComp = components.get(2);
					String genre = this.genericFilter(genreComp.text());
					GenreModel genere = new GenreModel();
					genere.setName(genre);
					// RECUPERO/SALVATAGGIO DB DEL GENERE
					genere = new GenreService().saveGenre(genere);
					release.setGenre(genere);
					
					// DATE RELEASE
					release.setReleaseDate(dateIn);
					
					// AGGIUNGE ULTERIORI INFO DELLA RELEASE A PARTIRE DAL NOME
					// ES. CREW E ANNO RELEASE
					SymusicUtility.processReleaseName(release);
					
					// SE E' GIA' PRESENTE IL LISTA, PRENDE QUELLA
					ReleaseModel relInList = this.verificaPresenzaInLista(release);
					if(relInList!=null) {
						
						release = relInList;
						// LINK
						release.addLink(SymusicUtility.popolateLink(relComp.getElementsByTag("a").get(0)));
						
						log.info("|"+release+"| fusa con quella gia' presente");
						log.info("####################################");
						
					} else {
						
						// LINK
						release.addLink(SymusicUtility.popolateLink(relComp.getElementsByTag("a").get(0)));
						
						
						log.info("|"+release+"| acquisita");
						log.info("####################################");
										
						listRelease.add(release);
						
					}
										
					
					
				} 

			}
			
		}
			
//		return listRelease;

		
	}


	private ReleaseModel verificaPresenzaInLista(ReleaseModel release) {
		for(ReleaseModel r : listRelease) {
			if(r.equals(release)) {
				log.info("Release "+release+" e' gia' presente in lista, si effettuera' la fusione");
				return r;
			}
		}
		return null;
	}


	private String genericFilter(String text) {
		if(text!=null) {
			text = text.replaceAll("[()]", "").trim();
			if(!String.valueOf(text.toCharArray()[0]).matches("[a-zA-Z0-9]")) {
				return text.substring(1);
			}
		}
		return null;
	}


	private String extractNextPage(ScenelogInfo info) {
		
		return conf.URL_CATEGORY+genre+conf.PARAMS_PAGE+info.getNumPagina();

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
		
		Release0DayMp3Service s = new Release0DayMp3Service(1L);
//		List<ReleaseModel> res = s.parse0DayMusicRelease("trance",da,a);
//		for(ReleaseModel r : res)
//			System.out.println(r);
		System.out.println(s.genericFilter("fhfh( dewdef) fef"));
		System.out.println(s.genericFilter("fhfh( dewdef) fef"));
		System.out.println("fhfh( dewdef) fef".replaceAll("[()]", ""));
	}



	@Override
	protected String applyFilterSearch(String result) {
		return result;
	}


}

class ZeroDayMp3Info {
	
	private int numPagina = 1;
	private String nextPage;
	private boolean processNextPage;
	
	public void changePage() {
		numPagina = numPagina + 1;
	}
	
	public int getNumPagina() {
		return numPagina;
	}
	
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
