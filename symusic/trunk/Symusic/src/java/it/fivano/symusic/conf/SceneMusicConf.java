package it.fivano.symusic.conf;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class SceneMusicConf {
	
	public String URL;
	public String CLASS_RELEASE_ITEM;

	
	public SceneMusicConf() throws IOException {
		InputStream in = this.getClass().getClassLoader().getResourceAsStream("scenemusic.properties");
		Properties props = new Properties();
		props.load(in);
		
		URL = props.getProperty("url");
		CLASS_RELEASE_ITEM = props.getProperty("class_release_item");
	}

}
