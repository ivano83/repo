package it.fivano.symusic.core;

public abstract class BaseService {
	
	
	
	protected String formatQueryString(String name, int wordToDelete) {
		
		int index = name.indexOf("(");
		String result = "";
		if(index!=-1)
			return applyFilterSearch(name.substring(0,index));
		
		String[] split = name.split("-");
		
		if(split.length==1) {
			
			result = name;
		}
		else if(split.length==5) {
			
			result = split[0]+"-"+split[1];
		}
		else if(split.length>5) {
			result = split[0]+"-"+split[1]+"-"+split[2];
		}
		else {
			result = split[0]+"-"+split[1];
		}
		
		if(wordToDelete>0) {
			for(int i=0;i<wordToDelete;i++)
				result = result.substring(0,result.lastIndexOf(" "));
		}
		
		return applyFilterSearch(result);
	}
	
	protected abstract String applyFilterSearch(String result);

}
