package it.fivano.symusic;

import it.fivano.symusic.model.ReleaseModel;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Properties;

import com.wcohen.ss.Level2MongeElkan;
import com.wcohen.ss.MongeElkan;

public class SymusicUtility {
	
	public static final String STANDARD_DATE_FORMAT = "dd/MM/yyyy";
	
	public enum EnvironmentType {
		TESTING("testing");
		
		private String env;
		private EnvironmentType(String env) {
			this.env = env;
		}
		public String getEnv() {
			return env;
		}
	}
	
	public static Properties getProps(String propsName) throws IOException {
		InputStream in = SymusicUtility.class.getClassLoader().getResourceAsStream(propsName);
		Properties props = new Properties();
		props.load(in);
		return props;
	}
	
	
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
		if(size<=2) return;
		
		String crew = nameSplit[size-1];
		String year = nameSplit[size-2];
		if(!year.matches("\\d\\d\\d\\d")) {
			year = nameSplit[size-3];
			crew = nameSplit[size-2] + "_" + nameSplit[size-1];
			
		}
		if(year.matches("\\d\\d\\d\\d"))
			rel.setCrew(crew);
		rel.setYear(year);
		
	}
	
	public static boolean compareStringSimilarity(String s1, String s2) {
		
		if(s1.contains(s2))
			return true;
		
		if(s2.contains(s1))
			return true;
		
		MongeElkan alg = new MongeElkan();
		double score = alg.score(s1, s2);
		System.out.println(score);
		if(score > 0.85)
			return true;
		
		Level2MongeElkan alg2 = new Level2MongeElkan();
		score = alg2.score(s1, s2);
		System.out.println(score);
		if(score > 0.85)
			return true;
		
		return false;

	}
	
	public static Date sottraiData(Date data, int numGiorniDaSottrarre) {
		
		Long millisDaSottrarre = (60*60*24*numGiorniDaSottrarre)*1000L;
		
		return new Date(data.getTime()-millisDaSottrarre);
		
	}
	
	public static Date aggiungiData(Date data, int numGiorniDaSottrarre) {
		
		Long millisDaSottrarre = (60*60*24*numGiorniDaSottrarre)*1000L;
		
		return new Date(data.getTime()+millisDaSottrarre);
		
	}

	public static void main(String[] args) {
		System.out.println(compareStringSimilarity("Houseshaker Feat. Amanda Blush","Houseshaker Feat Amanda Blush"));
		System.out.println(compareStringSimilarity("Houseshaker Feat. Amanda Blush","Houseshaker"));
		System.out.println(compareStringSimilarity("Houseshaker Feat. Amanda Blush","Houseshaker Amanda Blush"));
		System.out.println(compareStringSimilarity("Houseshaker Feat. Amanda Blush","Amanda Blush Feat Houseshaker"));

		sottraiData(new Date(), 2);
	}
}

