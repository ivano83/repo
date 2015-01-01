package it.ivano.filecatalog.dto;

import java.io.Serializable;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class Archivio implements Serializable {
	@Id
	@Column(name="id_archivio")
	private String idArchivio;

	private String nome;

	private String descrizione;

	@Column(name="path_iniziale")
	private String pathIniziale;

	@Column(name="dimensione_archivio")
	private String dimensioneArchivio;

	@Column(name="spazio_utilizzato")
	private String spazioUtilizzato;

	@Column(name="spazio_libero")
	private String spazioLibero;

	@ManyToOne
	@JoinColumn(name="id_utente")
	private Utente idUtente;

	@OneToMany(mappedBy="idArchivio")
	private Set<Cartella> cartellaCollection;

	@OneToMany(mappedBy="idArchivio")
	private Set<File> fileCollection;

	private static final long serialVersionUID = 1L;

	public Archivio() {
		super();
	}

	public String getIdArchivio() {
		return this.idArchivio;
	}

	public void setIdArchivio(String idArchivio) {
		this.idArchivio = idArchivio;
	}

	public String getNome() {
		return this.nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getDescrizione() {
		return this.descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public String getPathIniziale() {
		return this.pathIniziale;
	}

	public void setPathIniziale(String pathIniziale) {
		this.pathIniziale = pathIniziale;
	}

	public String getDimensioneArchivio() {
		return this.dimensioneArchivio;
	}

	public void setDimensioneArchivio(String dimensioneArchivio) {
		this.dimensioneArchivio = dimensioneArchivio;
	}

	public String getSpazioUtilizzato() {
		return this.spazioUtilizzato;
	}

	public void setSpazioUtilizzato(String spazioUtilizzato) {
		this.spazioUtilizzato = spazioUtilizzato;
	}

	public String getSpazioLibero() {
		return this.spazioLibero;
	}

	public void setSpazioLibero(String spazioLibero) {
		this.spazioLibero = spazioLibero;
	}

	public Utente getIdUtente() {
		return this.idUtente;
	}

	public void setIdUtente(Utente idUtente) {
		this.idUtente = idUtente;
	}

	public Set<Cartella> getCartellaCollection() {
		return this.cartellaCollection;
	}

	public void setCartellaCollection(Set<Cartella> cartellaCollection) {
		this.cartellaCollection = cartellaCollection;
	}

	public Set<File> getFileCollection() {
		return this.fileCollection;
	}

	public void setFileCollection(Set<File> fileCollection) {
		this.fileCollection = fileCollection;
	}

}
