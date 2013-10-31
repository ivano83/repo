package it.fivano.symusic.model;

public class TrackModel {
	
	private Integer trackNumber;
	private String trackName;
	private String artist;
	private String time;
	private String bpm;
	private String genere;
	private Long idTrack;
	
	public String getTrackName() {
		return trackName;
	}
	public void setTrackName(String trackName) {
		this.trackName = trackName;
	}
	public String getArtist() {
		return artist;
	}
	public void setArtist(String artist) {
		this.artist = artist;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getBpm() {
		return bpm;
	}
	public void setBpm(String bpm) {
		this.bpm = bpm;
	}
	public String getGenere() {
		return genere;
	}
	public void setGenere(String genere) {
		this.genere = genere;
	}
	
	@Override
	public String toString() {
		
		return (trackName!=null? trackName+" | " : "") + 
					(artist!=null? artist+" | " : "") + 
						(time!=null? time+" | " : "") + 
								(genere!=null? genere+" | " : "") + 
										(bpm!=null? bpm+" | " : "") ;
	}
	public Integer getTrackNumber() {
		return trackNumber;
	}
	public void setTrackNumber(Integer trackNumber) {
		this.trackNumber = trackNumber;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(this.trackNumber!=null && this.trackName!=null) {
			
			return this.trackNumber.equals(((TrackModel)obj).getTrackNumber()) &&
					this.trackName.equalsIgnoreCase(((TrackModel)obj).getTrackName());
		}
		else 
			return super.equals(obj);
	}
	public Long getIdTrack() {
		return idTrack;
	}
	public void setIdTrack(Long idTrack) {
		this.idTrack = idTrack;
	}

}
