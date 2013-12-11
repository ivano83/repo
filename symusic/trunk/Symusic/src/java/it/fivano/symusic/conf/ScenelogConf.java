package it.fivano.symusic.conf;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ScenelogConf {
	
	public String URL;
	public String URL_MUSIC;
	public String URL_ACTION;
	public String PARAMS;
	public String CLASS_RELEASE_ITEM;
	public String CLASS_RELEASE_TITLE;
	
	public String RELEASE_TRACK_NAME;
	public String RELEASE_DOWNLOAD;
	
	public String ID_PAGE_NUMBER;
	public String CLASS_RELEASE_LIST_ITEM;
	public String CLASS_RELEASE_LIST_DATA;
	public String DATE_FORMAT;
	
	public ScenelogConf() throws IOException {
		InputStream in = this.getClass().getClassLoader().getResourceAsStream("scenelog.properties");
		Properties props = new Properties();
		props.load(in);
		
		URL = props.getProperty("url");
		URL_MUSIC = props.getProperty("url_music");
		URL_ACTION = props.getProperty("url_action");
		PARAMS = props.getProperty("params");
		CLASS_RELEASE_ITEM = props.getProperty("class_release_item");
		CLASS_RELEASE_TITLE = props.getProperty("class_release_title");
		

		RELEASE_TRACK_NAME = props.getProperty("release_track_name");
		RELEASE_DOWNLOAD = props.getProperty("release_download");
		
		ID_PAGE_NUMBER = props.getProperty("id_page_number");
		CLASS_RELEASE_LIST_ITEM = props.getProperty("class_release_list_item");
		CLASS_RELEASE_LIST_DATA = props.getProperty("class_release_list_data");
		DATE_FORMAT = props.getProperty("date_format");
	}

}
