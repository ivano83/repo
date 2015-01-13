package it.ivano.filecatalog.model;

public class AudioComponent {
	
	private String descrizione;
	private String bitrate;
	private String linguaggio;
	private String canali;
	private String codec;
	
	public String getDescrizione() {
		return descrizione;
	}
	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}
	public String getBitrate() {
		return bitrate;
	}
	public void setBitrate(String bitrate) {
		this.bitrate = bitrate;
	}
	public String getLinguaggio() {
		return linguaggio;
	}
	public void setLinguaggio(String linguaggio) {
		this.linguaggio = linguaggio;
	}
	public String getCanali() {
		return canali;
	}
	public void setCanali(String canali) {
		this.canali = canali;
	}
	public void setCodec(String codec) {
		this.codec = codec;
	}
	public String getCodec() {
		return codec;
	}
	
}
