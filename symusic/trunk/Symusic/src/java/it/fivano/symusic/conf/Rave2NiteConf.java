package it.fivano.symusic.conf;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Rave2NiteConf {

	public String URL;
	public String URL_ACTION;
	public String URL_PARAM;
	public String CLASS_RELEASE;
	public String TITLE_LINK;


	public Rave2NiteConf() throws IOException {
		InputStream in = this.getClass().getClassLoader().getResourceAsStream("rave2nite.properties");
		Properties props = new Properties();
		props.load(in);

		URL = props.getProperty("url");
		URL_ACTION = props.getProperty("url_action");
		URL_PARAM = props.getProperty("params");
		CLASS_RELEASE = props.getProperty("class_release");
		TITLE_LINK = props.getProperty("title_link");

	}

}
