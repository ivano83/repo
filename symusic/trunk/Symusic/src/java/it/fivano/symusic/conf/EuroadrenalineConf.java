package it.fivano.symusic.conf;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;


public class EuroadrenalineConf {

	public String URL;
	public String PARAMS_PAGE;
	public String CLASS_RELEASE_LIST;
	public String CLASS_RELEASE_ITEM;
	public String CLASS_RELEASE_TITLE;
	public String CLASS_RELEASE_DATA;

	public String RELEASE_TRACK_NAME;
	public String RELEASE_DOWNLOAD;

	public String ID_PAGE_NUMBER;
	public String DATE_FORMAT;

	public EuroadrenalineConf() throws IOException {
		
		InputStream in = null;
		try {
			in = this.getClass().getClassLoader().getResourceAsStream("euroadrenaline.properties");
			Properties props = new Properties();
			props.load(in);
			
			URL = props.getProperty("url");
			PARAMS_PAGE = props.getProperty("params_page");
			CLASS_RELEASE_LIST = props.getProperty("class_release_list");
			CLASS_RELEASE_ITEM = props.getProperty("class_release_item");
			CLASS_RELEASE_TITLE = props.getProperty("class_release_title");
			CLASS_RELEASE_DATA = props.getProperty("class_release_data");
	
			RELEASE_TRACK_NAME = props.getProperty("release_track_name");
			RELEASE_DOWNLOAD = props.getProperty("release_download");
			
			ID_PAGE_NUMBER = props.getProperty("id_page_number");
			DATE_FORMAT = props.getProperty("date_format");
		
		} finally {
			try { if(in!=null) in.close(); } catch (Exception e) { }
		}

	}
	
}
