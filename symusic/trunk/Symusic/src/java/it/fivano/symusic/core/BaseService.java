package it.fivano.symusic.core;

import java.io.IOException;

import org.jsoup.nodes.Element;

import it.fivano.symusic.MyLogger;
import it.fivano.symusic.conf.SymusicConf;
import it.fivano.symusic.model.LinkModel;

public abstract class BaseService {
	
	protected MyLogger log;
	protected SymusicConf generalConf;
	
	public BaseService() {
		try {
			generalConf = new SymusicConf();
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
	
	protected abstract String applyFilterSearch(String result);

}
