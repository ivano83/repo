package it.fivano.symusic.conf;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PreDbConf {


	public String BASE_URL;
	public String URL;
	public String PARAMS_PAGE;
	public String PARAMS_GENRE;
	public String PARAMS_CREW;
	public String CLASS_RELEASE_ITEM;
	public String CLASS_RELEASE_TIME;
	public String CLASS_RELEASE_NAME;

	public String DATE_FORMAT;

	public PreDbConf() throws IOException {
		InputStream in = this.getClass().getClassLoader().getResourceAsStream("predb.properties");
		Properties props = new Properties();
		props.load(in);

		BASE_URL = props.getProperty("baseUrl");
		URL = props.getProperty("url");
		PARAMS_PAGE = props.getProperty("params_page");
		PARAMS_GENRE = props.getProperty("params_genre");
		PARAMS_CREW = props.getProperty("params_crew");
		CLASS_RELEASE_ITEM = props.getProperty("class_release_item");
		CLASS_RELEASE_TIME = props.getProperty("class_release_time");
		CLASS_RELEASE_NAME = props.getProperty("class_release_name");

		DATE_FORMAT = props.getProperty("date_format");
	}

}
