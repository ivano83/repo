package it.fivano.symusic.backend.model;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the release_link database table.
 * 
 */
@Entity
@Table(name="release_link")
@NamedQuery(name="ReleaseLink.findAll", query="SELECT r FROM ReleaseLink r")
public class ReleaseLink implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_release_link")
	private String idReleaseLink;

	@Column(name="release_link")
	private String releaseLink;

	//bi-directional many-to-one association to Release
	@ManyToOne
	@JoinColumn(name="id_release")
	private Release release;

	public ReleaseLink() {
	}

	public String getIdReleaseLink() {
		return this.idReleaseLink;
	}

	public void setIdReleaseLink(String idReleaseLink) {
		this.idReleaseLink = idReleaseLink;
	}

	public String getReleaseLink() {
		return this.releaseLink;
	}

	public void setReleaseLink(String releaseLink) {
		this.releaseLink = releaseLink;
	}

	public Release getRelease() {
		return this.release;
	}

	public void setRelease(Release release) {
		this.release = release;
	}

}