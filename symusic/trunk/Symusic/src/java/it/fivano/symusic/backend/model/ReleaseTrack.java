package it.fivano.symusic.backend.model;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the release_track database table.
 * 
 */
@Entity
@Table(name="release_track")
@NamedQuery(name="ReleaseTrack.findAll", query="SELECT r FROM ReleaseTrack r")
public class ReleaseTrack implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_release_track")
	private String idReleaseTrack;

	private String tag;

	@Column(name="track_bpm")
	private String trackBpm;

	@Column(name="track_genere")
	private String trackGenere;

	@Column(name="track_name")
	private String trackName;

	@Column(name="track_number")
	private String trackNumber;

	@Column(name="track_time")
	private String trackTime;

	//bi-directional many-to-one association to Release
	@ManyToOne
	@JoinColumn(name="id_release")
	private Release release;

	//bi-directional many-to-one association to Genre
	@ManyToOne
	@JoinColumn(name="id_genre")
	private Genre genre;

	public ReleaseTrack() {
	}

	public String getIdReleaseTrack() {
		return this.idReleaseTrack;
	}

	public void setIdReleaseTrack(String idReleaseTrack) {
		this.idReleaseTrack = idReleaseTrack;
	}

	public String getTag() {
		return this.tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public String getTrackBpm() {
		return this.trackBpm;
	}

	public void setTrackBpm(String trackBpm) {
		this.trackBpm = trackBpm;
	}

	public String getTrackGenere() {
		return this.trackGenere;
	}

	public void setTrackGenere(String trackGenere) {
		this.trackGenere = trackGenere;
	}

	public String getTrackName() {
		return this.trackName;
	}

	public void setTrackName(String trackName) {
		this.trackName = trackName;
	}

	public String getTrackNumber() {
		return this.trackNumber;
	}

	public void setTrackNumber(String trackNumber) {
		this.trackNumber = trackNumber;
	}

	public String getTrackTime() {
		return this.trackTime;
	}

	public void setTrackTime(String trackTime) {
		this.trackTime = trackTime;
	}

	public Release getRelease() {
		return this.release;
	}

	public void setRelease(Release release) {
		this.release = release;
	}

	public Genre getGenre() {
		return this.genre;
	}

	public void setGenre(Genre genre) {
		this.genre = genre;
	}

}