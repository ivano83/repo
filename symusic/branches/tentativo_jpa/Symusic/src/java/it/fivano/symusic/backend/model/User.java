package it.fivano.symusic.backend.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the user database table.
 * 
 */
@Entity
@NamedQuery(name="User.findAll", query="SELECT u FROM User u")
public class User implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_user")
	private String idUser;

	private byte[] abilitato;

	private String mail;

	private String password;

	@Column(name="user_name")
	private String userName;

	//bi-directional many-to-one association to ReleaseRating
	@OneToMany(mappedBy="user")
	private List<ReleaseRating> releaseRatings;

	//bi-directional many-to-one association to UserPreference
	@ManyToOne
	@JoinColumn(name="id_user_preference")
	private UserPreference userPreference;

	//bi-directional many-to-one association to UserGenrePreference
	@OneToMany(mappedBy="user")
	private List<UserGenrePreference> userGenrePreferences;

	public User() {
	}

	public String getIdUser() {
		return this.idUser;
	}

	public void setIdUser(String idUser) {
		this.idUser = idUser;
	}

	public byte[] getAbilitato() {
		return this.abilitato;
	}

	public void setAbilitato(byte[] abilitato) {
		this.abilitato = abilitato;
	}

	public String getMail() {
		return this.mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUserName() {
		return this.userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public List<ReleaseRating> getReleaseRatings() {
		return this.releaseRatings;
	}

	public void setReleaseRatings(List<ReleaseRating> releaseRatings) {
		this.releaseRatings = releaseRatings;
	}

	public ReleaseRating addReleaseRating(ReleaseRating releaseRating) {
		getReleaseRatings().add(releaseRating);
		releaseRating.setUser(this);

		return releaseRating;
	}

	public ReleaseRating removeReleaseRating(ReleaseRating releaseRating) {
		getReleaseRatings().remove(releaseRating);
		releaseRating.setUser(null);

		return releaseRating;
	}

	public UserPreference getUserPreference() {
		return this.userPreference;
	}

	public void setUserPreference(UserPreference userPreference) {
		this.userPreference = userPreference;
	}

	public List<UserGenrePreference> getUserGenrePreferences() {
		return this.userGenrePreferences;
	}

	public void setUserGenrePreferences(List<UserGenrePreference> userGenrePreferences) {
		this.userGenrePreferences = userGenrePreferences;
	}

	public UserGenrePreference addUserGenrePreference(UserGenrePreference userGenrePreference) {
		getUserGenrePreferences().add(userGenrePreference);
		userGenrePreference.setUser(this);

		return userGenrePreference;
	}

	public UserGenrePreference removeUserGenrePreference(UserGenrePreference userGenrePreference) {
		getUserGenrePreferences().remove(userGenrePreference);
		userGenrePreference.setUser(null);

		return userGenrePreference;
	}

}