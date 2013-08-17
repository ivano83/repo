package it.ivano.utility.html;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.xml.sax.SAXException;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class HtmlParse {
	
	
	public static List<HtmlAnchor> estraiLink(String url) throws FailingHttpStatusCodeException, MalformedURLException, IOException {
		
		WebClient webClient = new WebClient();
		try {
			HtmlPage page = webClient.getPage(url);

			return page.getAnchors();
		} finally {
			webClient.closeAllWindows();
		}
	}

	public static Set<LinkItem> filtraLinkBuoniGoogle(List<HtmlAnchor> HtmlAnchor, Set<LinkItem> listaBuoni, String originalSearch) {
		if(listaBuoni==null)
			listaBuoni = new TreeSet<LinkItem>();
		
		LinkItem it = null;
		for(HtmlAnchor l : HtmlAnchor) {
			
			if(isLinkBuoniGoogle(l, originalSearch)) {
			
				it = new LinkItem();
				it.setLink(l.getHrefAttribute().replace("/url?q=", "").split("&")[0]);
				if(l.getFirstChild()!=null)
			    	it.setName(l.getFirstChild().asText());
				it.setOriginalSite(l.getPage().getUrl().toString());
				it.setAnchorObject(l);
			
				listaBuoni.add(it);
			}
			
		}
		
		return listaBuoni;
	}
	
	private static boolean isLinkBuoniGoogle(HtmlAnchor l, String originalSearch) {
//		for(String acc : )
		
		if(l.getHrefAttribute().startsWith("/url?q="))
			return true;
		
//		String part1 = originalSearch.replace("_", "").replace(" ", "").replace("-", "");
//		if(l.getHrefAttribute().replace("_", "").replace(" ", "").replace("-", "")
//				.contains(part1)) 
//			return true;
		return false;
	}
	
	public static Set<LinkItem> filtraLinkBuoni(List<HtmlAnchor> HtmlAnchor, Set<LinkItem> listaBuoni, String originalSearch) {
		if(listaBuoni==null)
			listaBuoni = new TreeSet<LinkItem>();
		
		LinkItem it = null;
		for(HtmlAnchor l : HtmlAnchor) {
			System.out.println("----- "+l.getHrefAttribute());
			if(isLinkBuoni(l, originalSearch)) {
			
				it = new LinkItem();
				it.setLink(l.getHrefAttribute());
				if(l.getFirstChild()!=null)
			    	it.setName(l.getFirstChild().asText());
				it.setOriginalSite(l.getPage().getUrl().toString());
				it.setAnchorObject(l);
			
				listaBuoni.add(it);
				System.out.println(it.getLink()+"  -  "+it.getName());
			}
			
		}
		
		return listaBuoni;
	}
	
	private static boolean isLinkBuoni(HtmlAnchor l, String originalSearch) {
		
		if(l.getHrefAttribute().contains("filemates") ||
				l.getHrefAttribute().contains("uploaded")||
				l.getHrefAttribute().contains("ul.to")||
				l.getHrefAttribute().contains("rapidgator")||
				l.getHrefAttribute().contains("zippyshare"))
			return true;
		
//		String part1 = originalSearch.replace("_", "").replace(" ", "").replace("-", "");
//		if(l.getHrefAttribute().replace("_", "").replace(" ", "").replace("-", "")
//				.contains(part1)) 
//			return true;
		return false;
	}
	
	public static Set<LinkItem> recuperaRicercaGoogle(String input) throws FailingHttpStatusCodeException, MalformedURLException, IOException {
		
		int page = 0;
		int maxPage = 3;
		
		Set<LinkItem> res = new TreeSet<LinkItem>();
		
		while(page<maxPage) {
		
			List<HtmlAnchor> l = null;
			if(page==0)
				l = estraiLink("http://www.google.it/#q="+input);
			else
				l = estraiLink("http://www.google.it/#q="+input+"&start="+page*10);
			
			page++;
		
			res = filtraLinkBuoniGoogle(l, res, input);
//			for(LinkItem i : res) {
//				System.out.println(i.getLink() + " - "+i.getName());
//			}
			
		}
		return res;
	}


	public static void main(String[] args) throws SAXException, IOException {
		
//		WebConversation wc = new WebConversation();
//		wc.setExceptionsThrownOnErrorStatus(false);
//		WebResponse resp = wc.getResponse("http://www.0daymusic.org/stilius.php?id=dance");
//		WebLink[] links = resp.getLinks();
//		
//		for(WebLink l : links)
//			System.out.println(l.getURLString());
		
//		URL url = new URL( "http://www.0daymusic.org/stilius.php?id=dance" );
//        InputStream is = url.openConnection().getInputStream();
//
//        BufferedReader reader = new BufferedReader( new InputStreamReader( is )  );
//
//        String line = null;
//        String regExp = ".*<a .* href=\"(.*)\".*";
//        Pattern p = Pattern.compile( regExp, Pattern.CASE_INSENSITIVE );
//
//        while( ( line = reader.readLine() ) != null )  {
//            Matcher m = p.matcher( line );  
//            if( m.matches() ) {
//                System.out.println( m.group(1) );
//            }
//        }
//        reader.close();

        
        
//        URL yahoo = new URL("http://www.0daymusic.org/stilius.php?id=dance"); 
//        URLConnection yc = yahoo.openConnection(); 
//        BufferedReader in = new BufferedReader( 
//                                new InputStreamReader( 
//                                yc.getInputStream())); 
//        String inputLine; 
//        int count = 0; 
//        while ((inputLine = in.readLine()) != null) { 
//            System.out.println (inputLine);                
//            }      
//        in.close(); 
  
		
//		final WebClient webClient = new WebClient();
//		webClient.setJavaScriptEnabled(true);
//	    final HtmlPage page = webClient.getPage("http://www.0daymusic.org/stilius.php?id=dance");
//
//	    for(HtmlAnchor l : page.getAnchors()) {
//	    	if(l.getHrefAttribute().length()>0 && l.getFirstChild()!=null)
//	    	System.out.println(l.getHrefAttribute() + " "+l.getFirstChild().asText());
////	    	System.out.println(l.getPage().getUrl());
//	    }
//
//	    webClient.closeAllWindows();
//		String input = "Alex Mica - Dalinda- Promo CDR -2012-ATRium";
		String input = "Alex_Mica_-_Dalinda-_Promo_CDR_-2012-ATRium";
		
		Set<LinkItem> res = recuperaRicercaGoogle(input);
		Set<LinkItem> linkBuoni = new TreeSet<LinkItem>();
		
		for(LinkItem i : res) {
			
//			HtmlPage page = i.getAnchorObject().click();
			
			try {
				linkBuoni = filtraLinkBuoni(estraiLink(i.getLink()), linkBuoni, input);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		for(LinkItem ii : linkBuoni) {
			System.out.println(ii.getLink()+"  -  "+ii.getName());
		}
			
//		WebClient wc = new WebClient();
//		URL url = new URL("http://www.google.com/search?q=" + input);
//		HtmlPage page = (HtmlPage) wc.getPage(url);
//		List<HtmlAnchor> anchors = page.getAnchors();
//		for (Iterator<HtmlAnchor> iter = anchors.iterator(); iter.hasNext();) {
//			HtmlAnchor anchor = (HtmlAnchor) iter.next();
//			if (isSkipLink(anchor)) {
//				continue;
//			}
//			System.out.println(anchor.getHrefAttribute());
//		}
		
	}
}

class LinkItem implements Comparable<LinkItem> {
	
	private String link;
	private String name;
	private String originalSite;
	private HtmlAnchor anchorObject;
	
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getOriginalSite() {
		return originalSite;
	}
	public void setOriginalSite(String originalSite) {
		this.originalSite = originalSite;
	}
	
	public boolean equals(LinkItem obj) {
		
		return this.getLink().equals(obj.getLink());
	}
	@Override
	public int compareTo(LinkItem o) {
		return this.getLink().compareTo(o.getLink());
	}
	public void setAnchorObject(HtmlAnchor anchorObject) {
		this.anchorObject = anchorObject;
	}
	public HtmlAnchor getAnchorObject() {
		return anchorObject;
	}
	
	
}
