package it.fivano.symusic.conf;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class SceneDownloadConf {


	public String BASE_URL;
	public String URL;
	public String PARAMS_PAGE;
	public String PARAMS_SEARCH;
	public String CLASS_RELEASE_ITEM;

	public String DATE_FORMAT;

	public SceneDownloadConf() throws IOException {
		InputStream in = this.getClass().getClassLoader().getResourceAsStream("scenedownload.properties");
		Properties props = new Properties();
		props.load(in);

		BASE_URL = props.getProperty("baseUrl");
		URL = props.getProperty("url");
		PARAMS_PAGE = props.getProperty("params_page");
		PARAMS_SEARCH = props.getProperty("params_search");
		CLASS_RELEASE_ITEM = props.getProperty("class_release_item");

		DATE_FORMAT = props.getProperty("date_format");
	}

}
