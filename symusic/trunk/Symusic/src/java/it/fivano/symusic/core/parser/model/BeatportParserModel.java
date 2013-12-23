package it.fivano.symusic.core.parser.model;

public class BeatportParserModel {

	private String releaseName;
	private String artist;
	private String title;
	private Double levelSimilarity;
	private String urlReleaseDetails;
	
	
	public String getReleaseName() {
		return releaseName;
	}
	public void setReleaseName(String releaseName) {
		this.releaseName = releaseName;
	}
	public String getUrlReleaseDetails() {
		return urlReleaseDetails;
	}
	public void setUrlReleaseDetails(String urlReleaseDetails) {
		this.urlReleaseDetails = urlReleaseDetails;
	}
	public String getArtist() {
		return artist;
	}
	public void setArtist(String artist) {
		this.artist = artist;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public Double getLevelSimilarity() {
		return levelSimilarity;
	}
	public void setLevelSimilarity(Double levelSimilarity) {
		this.levelSimilarity = levelSimilarity;
	}
	
	
	@Override
	public String toString() {
		return ((releaseName!=null)?releaseName+" ":"")+"["+artist+" - "+title+"] "+"["+levelSimilarity+"] ";
	}
	
	
	
}
