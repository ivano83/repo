package it.fivano.symusic.backend.model;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the user_genre_preference database table.
 * 
 */
@Entity
@Table(name="user_genre_preference")
@NamedQuery(name="UserGenrePreference.findAll", query="SELECT u FROM UserGenrePreference u")
public class UserGenrePreference implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private UserGenrePreferencePK id;

	private int rank;

	//bi-directional many-to-one association to User
	@ManyToOne
	@JoinColumn(name="id_user")
	private User user;

	//bi-directional many-to-one association to Genre
	@ManyToOne
	@JoinColumn(name="id_genre")
	private Genre genre;

	public UserGenrePreference() {
	}

	public UserGenrePreferencePK getId() {
		return this.id;
	}

	public void setId(UserGenrePreferencePK id) {
		this.id = id;
	}

	public int getRank() {
		return this.rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	public User getUser() {
		return this.user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Genre getGenre() {
		return this.genre;
	}

	public void setGenre(Genre genre) {
		this.genre = genre;
	}

}