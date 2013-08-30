package it.fivano.symusic.backend.model;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the release_rating database table.
 * 
 */
@Entity
@Table(name="release_rating")
@NamedQuery(name="ReleaseRating.findAll", query="SELECT r FROM ReleaseRating r")
public class ReleaseRating implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private ReleaseRatingPK id;

	@Column(name="number_of_change")
	private int numberOfChange;

	private int star;

	//bi-directional many-to-one association to User
	@ManyToOne
	@JoinColumn(name="id_user", insertable=false, updatable=false)
	private User user;

	//bi-directional many-to-one association to Release
	@ManyToOne
	@JoinColumn(name="id_release", insertable=false, updatable=false)
	private Release release;

	public ReleaseRating() {
	}

	public ReleaseRatingPK getId() {
		return this.id;
	}

	public void setId(ReleaseRatingPK id) {
		this.id = id;
	}

	public int getNumberOfChange() {
		return this.numberOfChange;
	}

	public void setNumberOfChange(int numberOfChange) {
		this.numberOfChange = numberOfChange;
	}

	public int getStar() {
		return this.star;
	}

	public void setStar(int star) {
		this.star = star;
	}

	public User getUser() {
		return this.user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Release getRelease() {
		return this.release;
	}

	public void setRelease(Release release) {
		this.release = release;
	}

}