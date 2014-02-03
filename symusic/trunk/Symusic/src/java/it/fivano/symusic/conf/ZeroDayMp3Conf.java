package it.fivano.symusic.conf;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ZeroDayMp3Conf {
	
	public String URL;
	public String URL_CATEGORY;
	public String PARAMS_PAGE;
	public String SEARCH_ACTION;
	public String ID_CONTENT;
	public String RELEASE_NAME;
	public String RELEASE_LINK;
	public String RELEASE_GENRE;
	public String RELEASE_DATE;
	public String ID_PAGE_NUMBERS;
	public String DAY_FORMAT;
	
	
	public ZeroDayMp3Conf() throws IOException {
		InputStream in = this.getClass().getClassLoader().getResourceAsStream("0daymp3.properties");
		Properties props = new Properties();
		props.load(in);
		
		URL = props.getProperty("url");
		URL_CATEGORY = props.getProperty("url_category");
		PARAMS_PAGE = props.getProperty("params_page");
		SEARCH_ACTION = props.getProperty("search_action");
		ID_CONTENT = props.getProperty("id_content");
		RELEASE_NAME = props.getProperty("release_name");
		RELEASE_LINK = props.getProperty("release_link");
		RELEASE_GENRE = props.getProperty("release_genre");
		RELEASE_DATE = props.getProperty("release_date");
		ID_PAGE_NUMBERS = props.getProperty("id_page_numbers");
		DAY_FORMAT = props.getProperty("day_format");
		
	}

}
