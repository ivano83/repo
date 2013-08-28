package it.fivano.symusic.backend.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the release database table.
 * 
 */
@Entity
@NamedQuery(name="Release.findAll", query="SELECT r FROM Release r")
public class Release implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_release")
	private String idRelease;

	private String author;

	private String crew;

	@Column(name="release_name")
	private String releaseName;

	@Column(name="song_name")
	private String songName;

	private String years;

	//bi-directional many-to-one association to ReleaseLink
	@OneToMany(mappedBy="release")
	private List<ReleaseLink> releaseLinks;

	//bi-directional many-to-one association to ReleaseRating
	@OneToMany(mappedBy="release")
	private List<ReleaseRating> releaseRatings;

	//bi-directional many-to-one association to ReleaseTrack
	@OneToMany(mappedBy="release")
	private List<ReleaseTrack> releaseTracks;

	//bi-directional many-to-one association to ReleaseVideo
	@OneToMany(mappedBy="release")
	private List<ReleaseVideo> releaseVideos;

	public Release() {
	}

	public String getIdRelease() {
		return this.idRelease;
	}

	public void setIdRelease(String idRelease) {
		this.idRelease = idRelease;
	}

	public String getAuthor() {
		return this.author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getCrew() {
		return this.crew;
	}

	public void setCrew(String crew) {
		this.crew = crew;
	}

	public String getReleaseName() {
		return this.releaseName;
	}

	public void setReleaseName(String releaseName) {
		this.releaseName = releaseName;
	}

	public String getSongName() {
		return this.songName;
	}

	public void setSongName(String songName) {
		this.songName = songName;
	}

	public String getYears() {
		return this.years;
	}

	public void setYears(String years) {
		this.years = years;
	}

	public List<ReleaseLink> getReleaseLinks() {
		return this.releaseLinks;
	}

	public void setReleaseLinks(List<ReleaseLink> releaseLinks) {
		this.releaseLinks = releaseLinks;
	}

	public ReleaseLink addReleaseLink(ReleaseLink releaseLink) {
		getReleaseLinks().add(releaseLink);
		releaseLink.setRelease(this);

		return releaseLink;
	}

	public ReleaseLink removeReleaseLink(ReleaseLink releaseLink) {
		getReleaseLinks().remove(releaseLink);
		releaseLink.setRelease(null);

		return releaseLink;
	}

	public List<ReleaseRating> getReleaseRatings() {
		return this.releaseRatings;
	}

	public void setReleaseRatings(List<ReleaseRating> releaseRatings) {
		this.releaseRatings = releaseRatings;
	}

	public ReleaseRating addReleaseRating(ReleaseRating releaseRating) {
		getReleaseRatings().add(releaseRating);
		releaseRating.setRelease(this);

		return releaseRating;
	}

	public ReleaseRating removeReleaseRating(ReleaseRating releaseRating) {
		getReleaseRatings().remove(releaseRating);
		releaseRating.setRelease(null);

		return releaseRating;
	}

	public List<ReleaseTrack> getReleaseTracks() {
		return this.releaseTracks;
	}

	public void setReleaseTracks(List<ReleaseTrack> releaseTracks) {
		this.releaseTracks = releaseTracks;
	}

	public ReleaseTrack addReleaseTrack(ReleaseTrack releaseTrack) {
		getReleaseTracks().add(releaseTrack);
		releaseTrack.setRelease(this);

		return releaseTrack;
	}

	public ReleaseTrack removeReleaseTrack(ReleaseTrack releaseTrack) {
		getReleaseTracks().remove(releaseTrack);
		releaseTrack.setRelease(null);

		return releaseTrack;
	}

	public List<ReleaseVideo> getReleaseVideos() {
		return this.releaseVideos;
	}

	public void setReleaseVideos(List<ReleaseVideo> releaseVideos) {
		this.releaseVideos = releaseVideos;
	}

	public ReleaseVideo addReleaseVideo(ReleaseVideo releaseVideo) {
		getReleaseVideos().add(releaseVideo);
		releaseVideo.setRelease(this);

		return releaseVideo;
	}

	public ReleaseVideo removeReleaseVideo(ReleaseVideo releaseVideo) {
		getReleaseVideos().remove(releaseVideo);
		releaseVideo.setRelease(null);

		return releaseVideo;
	}

}