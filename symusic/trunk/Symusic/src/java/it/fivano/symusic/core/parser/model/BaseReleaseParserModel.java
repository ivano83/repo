package it.fivano.symusic.core.parser.model;

import java.text.SimpleDateFormat;
import java.util.Date;

public class BaseReleaseParserModel {

	private String releaseName;
	private String urlReleaseDetails;
	private Date releaseDate;

	private boolean radioRip;
	private boolean dateInRange;

	private String genre;
	private String crew;

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
	public Date getReleaseDate() {
		return releaseDate;
	}
	public void setReleaseDate(Date releaseDate) {
		this.releaseDate = releaseDate;
	}
	public boolean isRadioRip() {
		return radioRip;
	}
	public void setRadioRip(boolean radioRip) {
		this.radioRip = radioRip;
	}
	public boolean isDateInRange() {
		return dateInRange;
	}
	public void setDateInRange(boolean dateInRange) {
		this.dateInRange = dateInRange;
	}


	@Override
	public String toString() {
		// TODO Auto-generated method stub
		final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		return releaseName+" ["+sdf.format(releaseDate)+"] "+((genre!=null)?"["+genre+"] ":"")+((radioRip)?"[isRadioRip] ":"")+((dateInRange)?"[dataInRange] ":"");
	}
	public String getGenre() {
		return genre;
	}
	public void setGenre(String genre) {
		this.genre = genre;
	}
	public String getCrew() {
		return crew;
	}
	public void setCrew(String crew) {
		this.crew = crew;
	}

}
