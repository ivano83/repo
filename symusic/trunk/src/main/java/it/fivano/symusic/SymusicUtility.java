package it.fivano.symusic;

import it.fivano.symusic.model.ReleaseModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class SymusicUtility {
	
	public static final String STANDARD_DATE_FORMAT = "dd/MM/yyyy";
	
	
	public static String getStandardDateFormat(String dateIn, String format) throws ParseException {
		
		
		Date date = new SimpleDateFormat(format, Locale.ENGLISH).parse(dateIn);
		
		return new SimpleDateFormat(STANDARD_DATE_FORMAT).format(date);
		
	}
	
	public static Date getStandardDate(String dateIn) throws ParseException {
			
		return new SimpleDateFormat(STANDARD_DATE_FORMAT).parse(dateIn);
		
	}
	
	public static void processReleaseName(ReleaseModel rel) {
		
		String name = rel.getNameWithUnderscore();
		String[] nameSplit = name.replace("-","_").split("_");
		int size = nameSplit.length;
		String crew = nameSplit[size-1];
		String year = nameSplit[size-2];
		
		rel.setCrew(crew);
		rel.setYear(year);
		
	}

}
