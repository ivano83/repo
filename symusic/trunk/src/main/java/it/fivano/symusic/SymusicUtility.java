package it.fivano.symusic;

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

}
