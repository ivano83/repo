package it.fivano.symusic.backend.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the user_preference database table.
 * 
 */
@Entity
@Table(name="user_preference")
@NamedQuery(name="UserPreference.findAll", query="SELECT u FROM UserPreference u")
public class UserPreference implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_user_preference")
	private String idUserPreference;

	@Column(name="enable_beatport")
	private byte enableBeatport;

	//bi-directional many-to-one association to User
	@OneToMany(mappedBy="userPreference")
	private List<User> users;

	public UserPreference() {
	}

	public String getIdUserPreference() {
		return this.idUserPreference;
	}

	public void setIdUserPreference(String idUserPreference) {
		this.idUserPreference = idUserPreference;
	}

	public byte getEnableBeatport() {
		return this.enableBeatport;
	}

	public void setEnableBeatport(byte enableBeatport) {
		this.enableBeatport = enableBeatport;
	}

	public List<User> getUsers() {
		return this.users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}

	public User addUser(User user) {
		getUsers().add(user);
		user.setUserPreference(this);

		return user;
	}

	public User removeUser(User user) {
		getUsers().remove(user);
		user.setUserPreference(null);

		return user;
	}

}