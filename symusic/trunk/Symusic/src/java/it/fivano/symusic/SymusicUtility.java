package it.fivano.symusic;

import it.fivano.symusic.model.GenreModel;
import it.fivano.symusic.model.LinkModel;
import it.fivano.symusic.model.ReleaseExtractionModel;
import it.fivano.symusic.model.ReleaseExtractionModel.AreaExtraction;
import it.fivano.symusic.model.ReleaseModel;
import it.fivano.symusic.model.TrackModel;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.Random;

import org.jsoup.nodes.Element;

import com.wcohen.ss.Level2MongeElkan;
import com.wcohen.ss.MongeElkan;

public class SymusicUtility {

	public static final String STANDARD_DATE_FORMAT = "dd/MM/yyyy";

	public enum LevelSimilarity {
		MEDIO, ALTO, ALTISSIMO;

	}

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
		if(dateIn==null)
			return null;

		return new SimpleDateFormat(STANDARD_DATE_FORMAT).parse(dateIn);

	}

	public static String getStandardDate(Date dateIn) throws ParseException {
		if(dateIn==null)
			return null;

		return new SimpleDateFormat(STANDARD_DATE_FORMAT).format(dateIn);

	}

	public static void processReleaseName(ReleaseModel rel) {

		String name = rel.getNameWithUnderscore();
		String[] nameSplit = name.replace(" ","-").split("-");
//		String[] nameSplit = name.replace(" ","_").replace("-","_").split("_");
		int size = nameSplit.length;
		if(size<=2) return;

		String crew = nameSplit[size-1];
		String year = nameSplit[size-2];
		if(!year.matches("\\d\\d\\d\\d") && year.length()!=4) {
			year = nameSplit[size-3];
			crew = nameSplit[size-2] + "_" + nameSplit[size-1];
			if(!year.matches("\\d\\d\\d\\d") && year.length()!=4) {
				if(nameSplit[size-1].matches("\\d\\d\\d\\d") && nameSplit[size-1].length()!=4)
					year = nameSplit[size-1];
				else
					year = null; // meglio non salvare
			}
		}
		if(year!=null && year.matches("\\d\\d\\d\\d")) {
			rel.setCrew(crew);
			rel.setYear(year);
		}

		if(nameSplit.length>2) {
			rel.setArtist(nameSplit[0].replace("_", " "));
			rel.setSong(nameSplit[1].replace("_", " "));
		}

	}

	public static boolean compareStringSimilarity(String s1, String s2, LevelSimilarity levelSimil) {
		double res = getStringSimilarity(s1, s2, levelSimil);
		if(res == 0)
			return false;

		return true;

	}

	public static double getStringSimilarity(String s1, String s2, LevelSimilarity levelSimil) {
		s1 = s1.toLowerCase().replace("+", " ");
		s2 = s2.toLowerCase().replace("+", " ");

		double soglia = 0.85;
		if(LevelSimilarity.ALTO.toString().equals(levelSimil.toString())) {
			soglia = 0.9;
		}
		else if(LevelSimilarity.ALTISSIMO.toString().equals(levelSimil.toString())) {
			soglia = 0.95;
		}

		if(s1.contains(s2))
			return 1.2;

		if(s2.contains(s1))
			return 1.2;

		if(customCompare(s1,s2)) {
			return 1.1;
		}

		MongeElkan alg = new MongeElkan();
		double score = alg.score(s1, s2);
//		System.out.println(score);
		if(score > soglia)
			return score;

		Level2MongeElkan alg2 = new Level2MongeElkan();
		score = alg2.score(s1, s2);
//		System.out.println(score);
		if(score > soglia)
			return score;

		return 0;

	}

	private static boolean customCompare(String s1, String s2) {
		String pattern = "[ ,!?']";
		s1 = s1.replaceAll(pattern,"-");
		s2 = s2.replaceAll(pattern,"-");
		do { s1 = s1.replace("--", "-"); }while(s1.contains("--"));
		do { s2 = s2.replace("--", "-"); }while(s2.contains("--"));
		if(s1.endsWith("-")) s1 = s1.substring(0,s1.length()-1);
		if(s2.endsWith("-")) s2 = s2.substring(0,s2.length()-1);

		String[] parole1 = s1.split("-");
		int totParoleTrovate = 0;
		List<String> parole2 = Arrays.asList(s2.split("-"));
		for(String s : parole1) {
			if(parole2.contains(s)) {
				totParoleTrovate++;
			}
		}

		if(totParoleTrovate==parole1.length)
			return true;
		else
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

	public static LinkModel popolateLink(Element dl) {
		LinkModel currLink = new LinkModel();
		currLink.setLink(dl.attr("href"));
		currLink.setName((dl.attr("href").length()>70)? dl.attr("href").substring(0,70)+"..." : dl.attr("href"));

		return currLink;
	}

	public static List<TrackModel> chooseTrack(List<TrackModel> tracks1, List<TrackModel> tracks2, boolean primaListaPreferita) {
		if(!tracks1.isEmpty() && !tracks2.isEmpty()) {
			if(tracks1.size()!=tracks2.size())
				return tracks1;
			else {
				if(tracks1.get(0).toCount()<tracks2.get(0).toCount())
					return tracks2;
				else if(tracks1.get(0).toCount()>tracks2.get(0).toCount())
					return tracks1;
				if(primaListaPreferita)
					return tracks1;
				else
					return tracks2;
			}

		}

		if(tracks1.isEmpty() && !tracks2.isEmpty())
			return tracks2;
		else
			return tracks1;

	}

	public static void updateReleaseExtraction(ReleaseExtractionModel relExtr, boolean res, AreaExtraction area) {

		switch (area) {
		case BEATPORT:
			if(res) {
				relExtr.setBeatport(true);
				if(relExtr.getBeatportDate()==null)
					relExtr.setBeatportDate(new Date());
			}
			else {
				relExtr.setBeatport(false);
			}
			break;

		case SCENELOG:
			if(res) {
				relExtr.setScenelog(true);
				if(relExtr.getScenelogDate()==null)
					relExtr.setScenelogDate(new Date());
			}
			else {
				relExtr.setScenelog(false);
			}
			break;

		case YOUTUBE:
			if(res) {
				relExtr.setYoutube(true);
				if(relExtr.getYoutubeDate()==null)
					relExtr.setYoutubeDate(new Date());
			}
			else {
				relExtr.setYoutube(false);
			}
			break;

		default:
			break;
		}

	}

	public static void sleepRandom(long minMillis) {
		try {
			Thread.sleep(minMillis+(new Random().nextInt(200)));
		} catch (InterruptedException e) { }
	}

	public static void main(String[] args) {
		System.out.println(compareStringSimilarity("Houseshaker Feat. Amanda Blush","Houseshaker Feat Amanda Blush",LevelSimilarity.ALTO));
		System.out.println(compareStringSimilarity("Houseshaker Feat. Amanda Blush","Houseshaker",LevelSimilarity.ALTO));
		System.out.println(compareStringSimilarity("Houseshaker Feat. Amanda Blush","Houseshaker Amanda Blush",LevelSimilarity.ALTO));
		System.out.println(compareStringSimilarity("Houseshaker Feat. Amanda Blush","Amawfewnda Blasdewush Feat Houfeseshaker",LevelSimilarity.ALTO));

		sottraiData(new Date(), 2);

		customCompare("ciao-pippo e paperino, bene!!?!", "");
	}


	public static GenreModel creaGenere(String text) {
		GenreModel genre = new GenreModel();
		genre.setName(text);
		return genre;
	}
}

