package it.fivano.symusic.core;

import it.fivano.symusic.SymusicUtility;
import it.fivano.symusic.conf.ZeroDayMusicConf;
import it.fivano.symusic.model.LinkModel;
import it.fivano.symusic.model.ReleaseModel;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ReleaseService {
	
	private ZeroDayMusicConf conf;
	private boolean processNextPage;
	
	public ReleaseService() throws IOException {
		conf = new ZeroDayMusicConf();
	}
	
	
	public List<ReleaseModel> parse0DayMusicRelease(String genere, Date da, Date a) {
		
		List<ReleaseModel> listRelease = null;
		
		try {
			
			String urlConn = conf.URL+conf.URL_ACTION+"?"+conf.PARAMS.replace("{0}", genere);
			ZeroDayMusicInfo info = new ZeroDayMusicInfo();
			info.setProcessNextPage(true);
			
			listRelease = this.parse0DayMusic(urlConn, da, a, info);
			
			
			while(info.isProcessNextPage()) {
				
				System.out.println("is processing");
				
				listRelease.addAll(this.parse0DayMusic(info.getNextPage(), da, a, info));
				
			}
			
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return listRelease;
		
	}
	
	
	
	private List<ReleaseModel> parse0DayMusic(String urlConn, Date da, Date a, ZeroDayMusicInfo info) throws IOException, ParseException {
		
		List<ReleaseModel> listRelease = new ArrayList<ReleaseModel>();
		
		try {
			
			if(urlConn == null)
				return listRelease;
			
			ReleaseModel release = null;
			LinkModel link = null;
			
			Document doc = Jsoup.connect(urlConn).get();
			
			info.setNextPage(this.extractNextPage(doc));

			Elements releaseGroup = doc.getElementsByAttributeValue("id",conf.ID_RELEASE_GROUP);
			int totReleaseDayInPage = releaseGroup.size();
			int countReleaseDayInPage = 0;
			for(Element e : releaseGroup) {
				countReleaseDayInPage++;
				
				// date release
				String dateIn = this.getStandardDateFormat(e.parent().getElementById(conf.ID_DAY).text());
				Date dateInDate = SymusicUtility.getStandardDate(dateIn);
				
				// bisogna recuperare ancora altri giorni di release
				if(da.compareTo(dateInDate)>0)
					info.setProcessNextPage(false);
				
				// range data, solo le release comprese da - a
				if(!this.downloadReleaseDay(dateInDate, da, a)) {
					continue;
				}
				
				
				
				Elements links = e.getElementsByTag("a");
				for(Element linkDoc : links) {
					release = new ReleaseModel();
					link = new LinkModel();
					
					// name
					String title = linkDoc.attr("title");
					release.setName(title.replace("_", " "));
					release.setNameWithUnderscore(title.replace(" ", "_"));
					
					// link
					link.setLink(linkDoc.attr("href"));
					release.addLink(link);
					
					// release date
					release.setReleaseDate(dateIn);
					
					listRelease.add(release);
				}
				
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
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

	public static void main(String[] args) throws IOException, ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		Date da = sdf.parse("20130725");
		Date a = sdf.parse("20130802");
		
		ReleaseService s = new ReleaseService();
		List<ReleaseModel> res = s.parse0DayMusicRelease("trance",da,a);
		for(ReleaseModel r : res)
			System.out.println(r);
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
