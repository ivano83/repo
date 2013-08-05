package it.fivano.symusic.conf;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ZeroDayMusicConf {
	
	private String url;
	private String params;
	private String id_day_group;
	private String id_day;
	private String id_release_group;
	
	public ZeroDayMusicConf() throws IOException {
		InputStream in = this.getClass().getClassLoader().getResourceAsStream("0daymusic.properties");
		Properties props = new Properties();
		props.load(in);
		
		url = props.getProperty("url");
		params = props.getProperty("params");
		id_day_group = props.getProperty("id_day_group");
		id_day = props.getProperty("id_day");
		id_release_group = props.getProperty("id_release_group");
		
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getParams() {
		return params;
	}

	public void setParams(String params) {
		this.params = params;
	}

	public String getId_day_group() {
		return id_day_group;
	}

	public void setId_day_group(String id_day_group) {
		this.id_day_group = id_day_group;
	}

	public String getId_day() {
		return id_day;
	}

	public void setId_day(String id_day) {
		this.id_day = id_day;
	}

	public String getId_release_group() {
		return id_release_group;
	}

	public void setId_release_group(String id_release_group) {
		this.id_release_group = id_release_group;
	}

}
