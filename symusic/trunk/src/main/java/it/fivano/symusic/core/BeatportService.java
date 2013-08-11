package it.fivano.symusic.core;

import java.io.IOException;
import java.text.ParseException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import it.fivano.symusic.SymusicUtility;
import it.fivano.symusic.conf.BeatportConf;
import it.fivano.symusic.model.ReleaseModel;

public class BeatportService {

	private BeatportConf conf;
	
	public BeatportService() throws IOException {
		conf = new BeatportConf();
	}
	
	public void parseBeatport(ReleaseModel release) {
		
		

		try {
			
			String query = this.formatQueryString(release.getName());
			
			// pagina di inizio
			String urlConn = conf.URL+conf.URL_ACTION+"?"+conf.PARAMS.replace("{0}", query);
			Document doc = Jsoup.connect(urlConn).get();
			
			Element releaseGroup = doc.getElementsByAttributeValue(conf.ATTR_RELEASE_NAME,conf.ATTR_RELEASE_VALUE).get(0);
			Elements releaseList = releaseGroup.getElementsByClass(conf.CLASS_RELEASE_ITEM);
			String releaseLink = null;
			for(Element e : releaseList) {
				Element title = e.getElementsByClass(conf.CLASS_RELEASE_TITLE).get(0);
				String titleString = title.text();
				if(release.getName().contains(titleString)) {
					releaseLink = title.attr("href");
					break;
				}
				
			}
			
			
			if(releaseLink != null) {
				// release trovata
				doc = Jsoup.connect(releaseLink).get();
				
				Element releaseDetail = doc.getElementsByClass(conf.RELEASE_DETAIL).get(0);
				if(releaseDetail.children().size()>2) {
					
					String songName = releaseDetail.child(0).text();
					release.setSong(songName);
					String artistName = releaseDetail.child(1).text();
					release.setArtist(artistName);
				}
				
				Elements releaseTrack = releaseGroup.getElementsByClass(conf.TABLE_RELEASE_TRACK);
				
			}
			
			
			
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
//		} catch (ParseException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
		}

		
	}

	private String formatQueryString(String name) {
		
		int index = name.indexOf("(");
		if(index!=-1)
			return name.substring(0,index);
		
		String[] split = name.split("-");
		
		if(split.length==5) {
			
			return split[0]+"-"+split[1];
		}
		else if(split.length>5) {
			return split[0]+"-"+split[1]+"-"+split[2];
		}
		else {
			return split[0]+"-"+split[1];
		}
		
	}
	
	public static void main(String[] args) throws IOException {
		BeatportService s = new BeatportService();
		ReleaseModel r = new ReleaseModel();
		r.setName("Houseshaker Feat. Amanda Blush-Light the Sky-(7000042629)-WEB-2013-DWM");
//		r.setName("Cyberfactory - Into The Light-WEB-2013-ZzZz");
//		r.setName("Pepe and Shehu feat Morgana - Summer Love-(SYLIFE 167)-WEB-2013-ZzZz");
		s.parseBeatport(r);

	}
	
}
