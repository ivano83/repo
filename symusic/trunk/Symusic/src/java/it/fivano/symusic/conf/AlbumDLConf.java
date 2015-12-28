package it.fivano.symusic.conf;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class AlbumDLConf {
	
	public String URL;
	public String URL_MUSIC;
	public String URL_ACTION;
	public String PARAMS;
	public String PARAMS_PAGE;
	public String CLASS_RELEASE_ITEM;
	public String CLASS_RELEASE_TITLE;
	public String CLASS_RELEASE_LINK;
	
	public String TABLE_INFO;
	public String TABLE_TRACKS;
	public String RELEASE_DOWNLOAD;
	public String RELEASE_NAME;
	
	public String CLASS_RELEASE_LIST_ITEM;
	public String CLASS_RELEASE_LIST_DATA;
	public String DATE_FORMAT;
	
	public AlbumDLConf() throws IOException {
		InputStream in = this.getClass().getClassLoader().getResourceAsStream("albumdl.properties");
		Properties props = new Properties();
		props.load(in);
		
		URL = props.getProperty("url");
		URL_MUSIC = props.getProperty("url_music");
		URL_ACTION = props.getProperty("url_action");
		PARAMS = props.getProperty("params");
		PARAMS_PAGE = props.getProperty("params_page");
		CLASS_RELEASE_ITEM = props.getProperty("class_release_item");
		CLASS_RELEASE_TITLE = props.getProperty("class_release_title");
		CLASS_RELEASE_LINK = props.getProperty("class_release_link");

		TABLE_INFO = props.getProperty("table_info");
		TABLE_TRACKS = props.getProperty("table_tracks");
		RELEASE_DOWNLOAD = props.getProperty("release_download");
		RELEASE_NAME = props.getProperty("release_name");
		
		CLASS_RELEASE_LIST_ITEM = props.getProperty("class_release_list_item");
		CLASS_RELEASE_LIST_DATA = props.getProperty("class_release_list_data");
		DATE_FORMAT = props.getProperty("date_format");
	}

}
