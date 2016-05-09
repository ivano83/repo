package it.fivano.symusic.conf;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PresceneConf {


	public String BASE_URL;
	public String URL;
	public String PARAMS_PAGE;
	public String PARAMS_GENRE;
	public String PARAMS_CREW;
	public String CLASS_RELEASE_ITEM;
	public String CLASS_RELEASE_GROUP;

	public String DATE_FORMAT;

	public PresceneConf() throws IOException {
		InputStream in = this.getClass().getClassLoader().getResourceAsStream("prescene.properties");
		Properties props = new Properties();
		props.load(in);

		BASE_URL = props.getProperty("baseUrl");
		URL = props.getProperty("url");
		PARAMS_PAGE = props.getProperty("params_page");
		PARAMS_GENRE = props.getProperty("params_genre");
		PARAMS_CREW = props.getProperty("params_crew");
		CLASS_RELEASE_GROUP = props.getProperty("class_release_group");
		CLASS_RELEASE_ITEM = props.getProperty("class_release_item");

		DATE_FORMAT = props.getProperty("date_format");
	}

}
