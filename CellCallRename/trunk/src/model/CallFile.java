package model;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class CallFile {

	private String originalPath;
	private String number;
	private String name;
	private String inOut;
	private Calendar data;
	
	public CallFile() {
		
		
	}

	public String getOriginalPath() {
		return originalPath;
	}

	public void setOriginalPath(String originalPath) {
		this.originalPath = originalPath;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getInOut() {
		return inOut;
	}

	public void setInOut(String inOut) {
		this.inOut = inOut;
	}

	public Calendar getData() {
		return data;
	}

	public void setData(Calendar data) {
		this.data = data;
	}
	public void setData(int anno, int mese, int giorno, int ora, int minuto, int secondo) {
		this.data = new GregorianCalendar();
		this.data.set(anno, mese-1, giorno, ora, minuto, secondo);
	}

}
