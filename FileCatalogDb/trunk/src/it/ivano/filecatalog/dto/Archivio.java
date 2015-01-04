package it.ivano.filecatalog.dto;

import java.io.Serializable;
import java.math.BigInteger;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Archivio implements Serializable {
	@Id
	@Column(name="id_archivio")
	private String idArchivio;

	@Column(name="id_utente")
	private BigInteger idUtente;

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

	public BigInteger getIdUtente() {
		return this.idUtente;
	}

	public void setIdUtente(BigInteger idUtente) {
		this.idUtente = idUtente;
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

}
