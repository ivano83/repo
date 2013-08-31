package it.fivano.symusic.conf;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ZeroDayMusicConf {
	
	public String URL;
	public String URL_ACTION;
	public String PARAMS;
	public String ID_NEXT_PAGES;
	public String ID_DAY_GROUP;
	public String ID_DAY;
	public String ID_RELEASE_GROUP;
	public String DAY_FORMAT;
	
	public ZeroDayMusicConf() throws IOException {
		InputStream in = this.getClass().getClassLoader().getResourceAsStream("0daymusic.properties");
		Properties props = new Properties();
		props.load(in);
		
		URL = props.getProperty("url");
		URL_ACTION = props.getProperty("url_action");
		PARAMS = props.getProperty("params");
		ID_NEXT_PAGES = props.getProperty("id_next_pages");
		ID_DAY_GROUP = props.getProperty("id_day_group");
		ID_DAY = props.getProperty("id_day");
		ID_RELEASE_GROUP = props.getProperty("id_release_group");
		DAY_FORMAT = props.getProperty("day_format");
		
	}

}
