package it.fivano.symusic.conf;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class SymusicConf {

	public String MAX_ACTIVE_THREAD;

	public SymusicConf() throws IOException {
		InputStream in = this.getClass().getClassLoader().getResourceAsStream("symusic.properties");
		Properties props = new Properties();
		props.load(in);
		
		MAX_ACTIVE_THREAD = props.getProperty("maxActiveThread");

	}
	
}
