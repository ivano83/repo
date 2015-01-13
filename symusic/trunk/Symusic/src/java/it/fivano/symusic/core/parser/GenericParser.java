package it.fivano.symusic.core.parser;

import it.fivano.symusic.AntiDdosUtility;
import it.fivano.symusic.MyLogger;
import it.fivano.symusic.conf.SymusicConf;
import it.fivano.symusic.model.LinkModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.jsoup.Jsoup;
import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public abstract class GenericParser {
	
	protected MyLogger log;
	protected SymusicConf generalConf;
	protected static final int TIMEOUT = 10000;
	protected AntiDdosUtility antiDDOS;
	
	protected Date dataDa;
	protected Date dataA;
	
	public GenericParser() {
		try {
			generalConf = new SymusicConf();
			antiDDOS = new AntiDdosUtility();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	protected void setLogger(Class<?> classe) {
		log = new MyLogger(classe);
	}
	
	protected LinkModel popolateLink(Element dl) {
		LinkModel currLink = new LinkModel();
		currLink.setLink(dl.attr("href"));
		currLink.setName((dl.attr("href").length()>70)? dl.attr("href").substring(0,70)+"..." : dl.attr("href"));
		
		return currLink;
	}
	
	protected boolean isRadioRipRelease(String releaseName) {
		for(String rip : generalConf.RELEASE_EXCLUSION) {
			if(releaseName.contains(rip))
				return true;
		}
		return false;
	}
	
	protected String formatQueryString(String name, int wordToDelete) {
		
		int index = name.indexOf("(");
		String result = "";
		if(index!=-1)
			return applyFilterSearch(name.substring(0,index));
		
		String[] split = name.split("-");
		
		if(split.length==1) {
			
			result = name;
		}
		else if(split.length==5 || split.length==6) {
			
			result = split[0]+"-"+split[1];
		}
		else if(split.length>6) {
			result = split[0]+"-"+split[1]+"-"+split[2];
		}
		else {
			result = split[0]+"-"+split[1];
		}
		
		if(wordToDelete>0) {
			for(int i=0;i<wordToDelete;i++)
				result = result.substring(0,result.lastIndexOf("-"));
		}
		
		return this.applyFilterSearch(result);
	}
	
	private static List<String> getUserAgentList() {
		List<String> lista = new ArrayList<String>();
		lista.add("Mozilla/5.0 (Windows; U; MSIE 9.0; Windows NT 9.0; en-US)");
		lista.add("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:23.0) Gecko/20100101 Firefox/23.0");
		lista.add("Mozilla/5.0 (compatible; MSIE 8.0; Windows NT 6.1; Trident/4.0; GTB7.4; InfoPath.2; SV1; .NET CLR 3.3.69573; WOW64; en-US)");
		lista.add("Mozilla/5.0 (compatible; MSIE 10.0; Windows NT 6.1; WOW64; Trident/6.0)");
		lista.add("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:24.0) Gecko/20100101 Firefox/24.0");
		lista.add("Opera/12.80 (Windows NT 5.1; U; en) Presto/2.10.289 Version/12.02");
		lista.add("Opera/9.80 (Macintosh; Intel Mac OS X 10.6.8; U; fr) Presto/2.9.168 Version/11.52");
		
		return lista;
	}
	
	protected String randomUserAgent() {
		
		return getUserAgentList().get(new Random().nextInt(getUserAgentList().size()-1));
	}
	
	protected String getReleaseNameWithoutUnderscore(String releaseName) {
		return releaseName.replace("_", " ");
	}
	
	protected boolean downloadReleaseDay(Date dateInDate, Date da, Date a) {
		boolean result = (dateInDate.compareTo(da)>=0) && (dateInDate.compareTo(a)<=0);
		return result;
	}
	
	protected Document bypassAntiDDOS(Document doc, String baseUrl, String urlToRedirect) throws Exception {
		String jschl_vc = doc.getElementsByAttributeValue("name", "jschl_vc").get(0).attr("value");
//		System.out.println(jschl_vc);
		Elements scriptElements = doc.getElementsByTag("script");
		Integer numberCalcLine = null;
		for (Element element :scriptElements ){                
			for (DataNode node : element.dataNodes()) {
				String text = node.getWholeData();
				
				
				numberCalcLine = antiDDOS.calcolateAnswer(text);
				
				/**
				String[] lines = text.split("\n");
				for(String scriptLine : lines) {
					if(scriptLine.trim().startsWith("a.value = ")) {
						numberCalcLine = scriptLine.replace(";", "").replace("a.value = ", "").trim();
						break;
					}
				}
				*/

			}
//			System.out.println(numberCalcLine);            
		}
		
		int jschl_answer = 0;
		if(numberCalcLine!=null) {
			
			jschl_answer = numberCalcLine;
			/**
			String[] addizioni = numberCalcLine.split("\\+");
			int i1,i2,i3;
			i1 = Integer.parseInt(addizioni[0]);
			String[] moltipl = addizioni[1].split("\\*");
			i2 = Integer.parseInt(moltipl[0]);
			i3 = Integer.parseInt(moltipl[1]);
			
			jschl_answer = ((i2*i3)+i1)+11;
			
			*/
		}
		
		if(!baseUrl.endsWith("/"))
			baseUrl = baseUrl + "/";
		String urlPage = baseUrl+"cdn-cgi/l/chk_jschl";
		String userAgent = this.randomUserAgent();
		doc = Jsoup.connect(urlPage).header("Referer", urlToRedirect).timeout(TIMEOUT).userAgent(userAgent).data("jschl_vc", jschl_vc).data("jschl_answer", jschl_answer+"").ignoreHttpErrors(true).get();
		
		
		return doc;
	}
	
	protected abstract String applyFilterSearch(String result);

}
