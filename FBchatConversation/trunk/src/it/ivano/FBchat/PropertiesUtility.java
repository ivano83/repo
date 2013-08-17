package it.ivano.FBchat;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.StringTokenizer;

public class PropertiesUtility {

	private static final String PROP_URL = "resources/fbchat.properties";
	
	public static Properties caricaProprieta() {
		Properties properties = new Properties();
		try {
			FileInputStream i = new FileInputStream(PROP_URL);
			properties.load(i);
			i.close();
		} catch (IOException e) {
			e.printStackTrace();
		} 
		
		return properties;
	}
	
	
	public static void salvaProprieta(Properties properties) {
		try {
			FileOutputStream o = new FileOutputStream(PROP_URL);
			properties.store(o, null);
			o.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static String primoCarattereMaiuscolo(String s) {
		if(s==null || s.length()==0) return "";
		String result = "";
		StringTokenizer st = new StringTokenizer(s, " ");
		while(st.hasMoreTokens()) {
			String sTemp = st.nextToken();
			String iniziale = sTemp.substring(0, 1);
			String finale = sTemp.substring(1, sTemp.length());
			result += iniziale.toUpperCase() + finale.toLowerCase() + " ";
		}
		return result.substring(0, result.length()-1);
	}
}
