package it.fivano.symusic.model;

import java.util.ArrayList;
import java.util.List;

public class ReleaseModel {
	
	private Long id;
	private String name;
	private String nameWithUnderscore;
	private String year;
	private String crew;
	private String artist;
	private String song;
	private String releaseDate;
	private String recordLabel;
	private GenreModel genre;
	private ReleaseExtractionModel releaseExtraction;
	
	private Integer voteValue;
	private Double voteAverage;
	private boolean voted;
	
	private List<LinkModel> links;
	private List<TrackModel> tracks;
	private List<VideoModel> videos;
	
	public ReleaseModel() {
		voteAverage = 0.;
		voteValue = 0;
	}
	
	
	public String getName() {
		if(name==null && nameWithUnderscore!=null) {
			name = nameWithUnderscore.replace("_", " ");
		}
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
		
		if(!links.contains(link))
			links.add(link);
	}
	
	 @Override
	public String toString() {
		return name + " ["+releaseDate+"]";
	}
	public List<TrackModel> getTracks() {
		if(this.tracks==null)
			tracks = new ArrayList<TrackModel>();
		return tracks;
	}
	public void setTracks(List<TrackModel> tracks) {
		this.tracks = tracks;
	}
	public void addTrack(TrackModel tr) {
		if(this.tracks==null)
			tracks = new ArrayList<TrackModel>();
		
		if(!tracks.contains(tr))
			tracks.add(tr);
	}
	public List<VideoModel> getVideos() {
		if(this.videos==null)
			videos = new ArrayList<VideoModel>();
		return videos;
	}
	public void setVideos(List<VideoModel> videos) {
		this.videos = videos;
	}
	public void addVideo(VideoModel tr) {
		if(this.videos==null)
			videos = new ArrayList<VideoModel>();
		
		if(!videos.contains(tr))
			videos.add(tr);
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Integer getVoteValue() {
		return voteValue;
	}
	public void setVoteValue(Integer voteValue) {
		this.voteValue = voteValue;
	}
	public boolean isVoted() {
		return voted;
	}
	public void setVoted(boolean voted) {
		this.voted = voted;
	}
	public Double getVoteAverage() {
		return voteAverage;
	}
	public void setVoteAverage(Double voteAverage) {
		this.voteAverage = voteAverage;
	}


	public GenreModel getGenre() {
		return genre;
	}


	public void setGenre(GenreModel genre) {
		this.genre = genre;
	}


	public ReleaseExtractionModel getReleaseExtraction() {
		return releaseExtraction;
	}


	public void setReleaseExtraction(ReleaseExtractionModel releaseExtraction) {
		this.releaseExtraction = releaseExtraction;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(this.nameWithUnderscore!=null) {
			ReleaseModel rel = (ReleaseModel)obj;
			return this.nameWithUnderscore.equalsIgnoreCase(rel.getNameWithUnderscore());
		}
		else if(name!=null) {
			ReleaseModel rel = (ReleaseModel)obj;
			return this.name.equalsIgnoreCase(rel.getName());
		}
		return super.equals(obj);
	}

}
