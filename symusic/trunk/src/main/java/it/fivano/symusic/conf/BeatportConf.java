package it.fivano.symusic.conf;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class BeatportConf {
	
	public String URL;
	public String URL_ACTION;
	public String PARAMS;
	public String ATTR_RELEASE_NAME;
	public String ATTR_RELEASE_VALUE;
	public String CLASS_RELEASE_ITEM;
	public String CLASS_RELEASE_TITLE;
	public String RELEASE_DETAIL;
	public String TABLE_RELEASE_TRACK;
	public String RELEASE_TRACK_NAME;
	public String RELEASE_TRACK_GENRE;
	
	public BeatportConf() throws IOException {
		InputStream in = this.getClass().getClassLoader().getResourceAsStream("beatport.properties");
		Properties props = new Properties();
		props.load(in);
		
		URL = props.getProperty("url");
		URL_ACTION = props.getProperty("url_action");
		PARAMS = props.getProperty("params");
		ATTR_RELEASE_NAME = props.getProperty("attr_release_name");
		ATTR_RELEASE_VALUE = props.getProperty("attr_release_value");
		CLASS_RELEASE_ITEM = props.getProperty("class_release_item");
		CLASS_RELEASE_TITLE = props.getProperty("class_release_title");
		RELEASE_DETAIL = props.getProperty("release_detail");
		TABLE_RELEASE_TRACK = props.getProperty("table_release_track");
		RELEASE_TRACK_NAME = props.getProperty("release_track_name");
		RELEASE_TRACK_GENRE = props.getProperty("release_track_genre");
	}

}
