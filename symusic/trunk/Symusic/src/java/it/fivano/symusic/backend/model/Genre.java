package it.fivano.symusic.backend.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the genre database table.
 * 
 */
@Entity
@NamedQuery(name="Genre.findAll", query="SELECT g FROM Genre g")
public class Genre implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_genre")
	private int idGenre;

	private String name;

	//bi-directional many-to-one association to ReleaseTrack
	@OneToMany(mappedBy="genre")
	private List<ReleaseTrack> releaseTracks;

	//bi-directional many-to-one association to UserGenrePreference
	@OneToMany(mappedBy="genre")
	private List<UserGenrePreference> userGenrePreferences;

	public Genre() {
	}

	public int getIdGenre() {
		return this.idGenre;
	}

	public void setIdGenre(int idGenre) {
		this.idGenre = idGenre;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<ReleaseTrack> getReleaseTracks() {
		return this.releaseTracks;
	}

	public void setReleaseTracks(List<ReleaseTrack> releaseTracks) {
		this.releaseTracks = releaseTracks;
	}

	public ReleaseTrack addReleaseTrack(ReleaseTrack releaseTrack) {
		getReleaseTracks().add(releaseTrack);
		releaseTrack.setGenre(this);

		return releaseTrack;
	}

	public ReleaseTrack removeReleaseTrack(ReleaseTrack releaseTrack) {
		getReleaseTracks().remove(releaseTrack);
		releaseTrack.setGenre(null);

		return releaseTrack;
	}

	public List<UserGenrePreference> getUserGenrePreferences() {
		return this.userGenrePreferences;
	}

	public void setUserGenrePreferences(List<UserGenrePreference> userGenrePreferences) {
		this.userGenrePreferences = userGenrePreferences;
	}

	public UserGenrePreference addUserGenrePreference(UserGenrePreference userGenrePreference) {
		getUserGenrePreferences().add(userGenrePreference);
		userGenrePreference.setGenre(this);

		return userGenrePreference;
	}

	public UserGenrePreference removeUserGenrePreference(UserGenrePreference userGenrePreference) {
		getUserGenrePreferences().remove(userGenrePreference);
		userGenrePreference.setGenre(null);

		return userGenrePreference;
	}

}