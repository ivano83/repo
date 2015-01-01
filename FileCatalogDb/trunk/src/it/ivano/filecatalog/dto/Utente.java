package it.ivano.filecatalog.dto;

import java.io.Serializable;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Utente implements Serializable {
	@Id
	@Column(name="id_utente")
	private String idUtente;

	private String nome;

	private String cognome;

	private String email;

	private String password;

	@OneToMany(mappedBy="idUtente")
	private Set<Archivio> archivioCollection;

	private static final long serialVersionUID = 1L;

	public Utente() {
		super();
	}

	public String getIdUtente() {
		return this.idUtente;
	}

	public void setIdUtente(String idUtente) {
		this.idUtente = idUtente;
	}

	public String getNome() {
		return this.nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getCognome() {
		return this.cognome;
	}

	public void setCognome(String cognome) {
		this.cognome = cognome;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Set<Archivio> getArchivioCollection() {
		return this.archivioCollection;
	}

	public void setArchivioCollection(Set<Archivio> archivioCollection) {
		this.archivioCollection = archivioCollection;
	}

}
