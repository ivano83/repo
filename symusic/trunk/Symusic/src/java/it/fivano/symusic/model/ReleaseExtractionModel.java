package it.fivano.symusic.model;

import java.util.Date;

public class ReleaseExtractionModel {
	
	private Long idRelease;
	private boolean youtube;
	private boolean beatport;
	private boolean scenelog;
	private Date youtubeDate;
	private Date beatportDate;
	private Date scenelogDate;
	
	public enum AreaExtraction {
		YOUTUBE, BEATPORT, SCENELOG;
	}

	public Long getIdRelease() {
		return idRelease;
	}

	public void setIdRelease(Long idRelease) {
		this.idRelease = idRelease;
	}

	public boolean getYoutube() {
		return youtube;
	}

	public void setYoutube(boolean youtube) {
		this.youtube = youtube;
	}

	public boolean getBeatport() {
		return beatport;
	}

	public void setBeatport(boolean beatport) {
		this.beatport = beatport;
	}

	public boolean getScenelog() {
		return scenelog;
	}

	public void setScenelog(boolean scenelog) {
		this.scenelog = scenelog;
	}

	public Date getYoutubeDate() {
		return youtubeDate;
	}

	public void setYoutubeDate(Date youtubeDate) {
		this.youtubeDate = youtubeDate;
	}

	public Date getBeatportDate() {
		return beatportDate;
	}

	public void setBeatportDate(Date beatportDate) {
		this.beatportDate = beatportDate;
	}

	public Date getScenelogDate() {
		return scenelogDate;
	}

	public void setScenelogDate(Date scenelogDate) {
		this.scenelogDate = scenelogDate;
	}
	
	@Override
	public String toString() {
		return idRelease+" [SCENELOG:"+scenelog+", YOUTUBE:"+youtube+", BEATPORT:"+beatport+"]";
	}

}
