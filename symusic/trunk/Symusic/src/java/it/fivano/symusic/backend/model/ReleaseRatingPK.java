package it.fivano.symusic.backend.model;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the release_rating database table.
 * 
 */
@Embeddable
public class ReleaseRatingPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="id_user", insertable=false, updatable=false)
	private String idUser;

	@Column(name="id_release", insertable=false, updatable=false)
	private String idRelease;

	public ReleaseRatingPK() {
	}
	public String getIdUser() {
		return this.idUser;
	}
	public void setIdUser(String idUser) {
		this.idUser = idUser;
	}
	public String getIdRelease() {
		return this.idRelease;
	}
	public void setIdRelease(String idRelease) {
		this.idRelease = idRelease;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof ReleaseRatingPK)) {
			return false;
		}
		ReleaseRatingPK castOther = (ReleaseRatingPK)other;
		return 
			this.idUser.equals(castOther.idUser)
			&& this.idRelease.equals(castOther.idRelease);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.idUser.hashCode();
		hash = hash * prime + this.idRelease.hashCode();
		
		return hash;
	}
}