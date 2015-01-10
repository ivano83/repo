package it.fivano.symusic.model;

public class LinkModel {
	
	private String name;
	private String link;
	private String serverLink;
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}
	public String getServerLink() {
		return serverLink;
	}
	public void setServerLink(String serverLink) {
		this.serverLink = serverLink;
	}
	public String getName() {
		if(name==null && link!=null) {
			name = (link.length()>70)? link.substring(0,70)+"..." : link;
		}
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public boolean equals(Object obj) {
		return this.getLink().equals(((LinkModel)obj).getLink());
	}

}
