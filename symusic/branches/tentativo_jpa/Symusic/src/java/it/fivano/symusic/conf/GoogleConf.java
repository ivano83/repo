package it.fivano.symusic.conf;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class GoogleConf {

	public String URL;
	public String URL_ACTION;
	public String PARAMS;
	
	public GoogleConf() throws IOException {
		InputStream in = this.getClass().getClassLoader().getResourceAsStream("google.properties");
		Properties props = new Properties();
		props.load(in);
		
		URL = props.getProperty("url");
		URL_ACTION = props.getProperty("url_action");
		PARAMS = props.getProperty("params");
	}
	
}
