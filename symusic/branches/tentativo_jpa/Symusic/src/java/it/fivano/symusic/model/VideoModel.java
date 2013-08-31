package it.fivano.symusic.model;

public class VideoModel {
	
	private String link;
	private String name;
	private String eta;
	
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEta() {
		return eta;
	}
	public void setEta(String eta) {
		this.eta = eta;
	}
	
	@Override
	public String toString() {
		return name!=null? name+" | " : ""+
				link!=null? link+" | " : ""+
						eta!=null? eta+" | " : "";
	}
	
}
