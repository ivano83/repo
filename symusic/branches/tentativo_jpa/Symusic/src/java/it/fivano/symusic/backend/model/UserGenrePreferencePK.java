package it.fivano.symusic.backend.model;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the user_genre_preference database table.
 * 
 */
@Embeddable
public class UserGenrePreferencePK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="id_genre", insertable=false, updatable=false)
	private int idGenre;

	@Column(name="id_user", insertable=false, updatable=false)
	private String idUser;

	public UserGenrePreferencePK() {
	}
	public int getIdGenre() {
		return this.idGenre;
	}
	public void setIdGenre(int idGenre) {
		this.idGenre = idGenre;
	}
	public String getIdUser() {
		return this.idUser;
	}
	public void setIdUser(String idUser) {
		this.idUser = idUser;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof UserGenrePreferencePK)) {
			return false;
		}
		UserGenrePreferencePK castOther = (UserGenrePreferencePK)other;
		return 
			(this.idGenre == castOther.idGenre)
			&& this.idUser.equals(castOther.idUser);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.idGenre;
		hash = hash * prime + this.idUser.hashCode();
		
		return hash;
	}
}