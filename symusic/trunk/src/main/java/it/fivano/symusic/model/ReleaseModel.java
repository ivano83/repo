package it.fivano.symusic.model;

import java.util.ArrayList;
import java.util.List;

public class ReleaseModel {
	
	private String name;
	private String nameWithUnderscore;
	private String year;
	private String crew;
	private String artist;
	private String song;
	private String releaseDate;
	private String recordLabel;
	
	private List<LinkModel> links;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
	public String getCrew() {
		return crew;
	}
	public void setCrew(String crew) {
		this.crew = crew;
	}
	public String getArtist() {
		return artist;
	}
	public void setArtist(String artist) {
		this.artist = artist;
	}
	public String getSong() {
		return song;
	}
	public void setSong(String song) {
		this.song = song;
	}
	public String getReleaseDate() {
		return releaseDate;
	}
	public void setReleaseDate(String releaseDate) {
		this.releaseDate = releaseDate;
	}
	public String getRecordLabel() {
		return recordLabel;
	}
	public void setRecordLabel(String recordLabel) {
		this.recordLabel = recordLabel;
	}
	public String getNameWithUnderscore() {
		return nameWithUnderscore;
	}
	public void setNameWithUnderscore(String nameWithUnderscore) {
		this.nameWithUnderscore = nameWithUnderscore;
	}
	public List<LinkModel> getLinks() {
		return links;
	}
	public void setLinks(List<LinkModel> links) {
		this.links = links;
	}
	public void addLink(LinkModel link) {
		if(this.links==null)
			links = new ArrayList<LinkModel>();
		links.add(link);
	}
	
	 @Override
	public String toString() {
		return name + " ["+releaseDate+"]";
	}

}
