package it.fivano.symusic.backend.model;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the release_video database table.
 * 
 */
@Entity
@Table(name="release_video")
@NamedQuery(name="ReleaseVideo.findAll", query="SELECT r FROM ReleaseVideo r")
public class ReleaseVideo implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_release_video")
	private String idReleaseVideo;

	@Column(name="video_link")
	private String videoLink;

	//bi-directional many-to-one association to Release
	@ManyToOne
	@JoinColumn(name="id_release")
	private Release release;

	public ReleaseVideo() {
	}

	public String getIdReleaseVideo() {
		return this.idReleaseVideo;
	}

	public void setIdReleaseVideo(String idReleaseVideo) {
		this.idReleaseVideo = idReleaseVideo;
	}

	public String getVideoLink() {
		return this.videoLink;
	}

	public void setVideoLink(String videoLink) {
		this.videoLink = videoLink;
	}

	public Release getRelease() {
		return this.release;
	}

	public void setRelease(Release release) {
		this.release = release;
	}

}