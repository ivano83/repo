/*
 * Created on 8-ott-2007
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

/**
 * @author Ivano
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Messaggio {
	private String mittente;
	private String msg;
	private String data;
	private boolean inviato; //se è falso, allora è stato ricevuto!
	
	public Messaggio() {
		//this.numero = "";
		//this.msg = "";
		//this.data = "";
		//this.inviato = false;
	}
	public Messaggio(String mittente, String msg, String data, boolean inviato) {
		this.mittente = mittente;
		this.msg = msg;
		this.data = data;
		this.inviato = inviato;
	}
	public void setMittente(String mittente) {
		this.mittente = mittente;
	}
	public void setMessaggio(String msg) {
		this.msg = msg;
	}
	public void setData(String data) {
		this.data = data;
	}
	public void setInviato(boolean b) {
		this.inviato = b;
	}
	public String getMittente() {
		return this.mittente;
	}
	public String getMessaggio() {
		return this.msg;
	}
	public String getData() {
		return this.data;
	}
	public boolean getInviato() {
		return this.inviato;
	}
/*	public int hashCode() {
		return this.msg.hashCode() + this.data.hashCode();
	}
	public boolean equals(Object o) {
		return this.msg.equals(((Messaggio)o).getMessaggio()) && 
		this.data.equals(((Messaggio)o).getData());
	}
	public int compareTo(Object o) {
		return this.msg.compareTo(((Messaggio)o).getMessaggio());
	}*/
}
