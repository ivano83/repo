package it.fivano.symusic.conf;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Mp3TrackzConf {
	
	public String URL;
	public String URL_RELEASE;
	public String CLASS_RELEASE;
	public String TITLE_LINK;
	
	
	public Mp3TrackzConf() throws IOException {
		InputStream in = this.getClass().getClassLoader().getResourceAsStream("mp3trackz.properties");
		Properties props = new Properties();
		props.load(in);
		
		URL = props.getProperty("url");
		URL_RELEASE = props.getProperty("url_release");
		CLASS_RELEASE = props.getProperty("class_release");
		TITLE_LINK = props.getProperty("title_link");
		
	}

}
