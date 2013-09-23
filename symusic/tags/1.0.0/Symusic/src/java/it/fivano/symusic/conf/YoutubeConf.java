package it.fivano.symusic.conf;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class YoutubeConf {
	
	public String URL;
	public String URL_ACTION;
	public String PARAMS;

	public String CLASS_VIDEO;
	public String CLASS_VIDEO_TITLE;
	public String CLASS_VIDEO_META;
	
	public Integer MAX_VIDEO_EXTRACT;

	
	public YoutubeConf() throws IOException {
		InputStream in = this.getClass().getClassLoader().getResourceAsStream("youtube.properties");
		Properties props = new Properties();
		props.load(in);
		
		URL = props.getProperty("url");
		URL_ACTION = props.getProperty("url_action");
		PARAMS = props.getProperty("params");
		CLASS_VIDEO = props.getProperty("class_video");
		CLASS_VIDEO_TITLE = props.getProperty("class_video_title");
		CLASS_VIDEO_META = props.getProperty("class_video_meta");
		
		MAX_VIDEO_EXTRACT = Integer.parseInt(props.getProperty("max_video_extract"));
		
	}

}
