package it.fivano.symusic.core;

import it.fivano.symusic.conf.ZeroDayMusicConf;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ReleaseService {
	
	public ReleaseService() {
		
	}
	
	
	public void parse0DayMusic(String genere) {
		
		
		
		try {
			ZeroDayMusicConf conf = new ZeroDayMusicConf();
			
			String urlConn = conf.getUrl()+"?"+conf.getParams().replace("{0}", genere);
			Document doc = Jsoup.connect(urlConn).get();
			
			Elements releaseGroup = doc.getElementsByAttributeValue("id","grupsarasas");
			for(Element e : releaseGroup) {
				Elements links = e.getElementsByTag("a");
				for(Element link : links) {
					System.out.println(link.attr("href"));
					System.out.println(link.text());
				}
				
			}
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		
		
	}

	public static void main(String[] args) {
		
		ReleaseService s = new ReleaseService();
		s.parse0DayMusic("trance");
		
	}
}
