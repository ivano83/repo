package it.ivano.filecatalog.dto;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class Cartella implements Serializable {
	@Id
	@Column(name="id_cartella")
	private String idCartella;

	private String cartella;

	@Column(name="id_cartella_padre")
	private BigInteger idCartellaPadre;

	@Column(name="cartella_base")
	private byte cartellaBase;

	@Column(name="cartelle_precedenti")
	private String cartellePrecedenti;

	@ManyToOne
	@JoinColumn(name="id_archivio")
	private Archivio idArchivio;

	@OneToMany(mappedBy="idCartella")
	private Set<File> fileCollection;

	private static final long serialVersionUID = 1L;

	public Cartella() {
		super();
	}

	public String getIdCartella() {
		return this.idCartella;
	}

	public void setIdCartella(String idCartella) {
		this.idCartella = idCartella;
	}

	public String getCartella() {
		return this.cartella;
	}

	public void setCartella(String cartella) {
		this.cartella = cartella;
	}

	public BigInteger getIdCartellaPadre() {
		return this.idCartellaPadre;
	}

	public void setIdCartellaPadre(BigInteger idCartellaPadre) {
		this.idCartellaPadre = idCartellaPadre;
	}

	public byte getCartellaBase() {
		return this.cartellaBase;
	}

	public void setCartellaBase(byte cartellaBase) {
		this.cartellaBase = cartellaBase;
	}

	public String getCartellePrecedenti() {
		return this.cartellePrecedenti;
	}

	public void setCartellePrecedenti(String cartellePrecedenti) {
		this.cartellePrecedenti = cartellePrecedenti;
	}

	public Archivio getIdArchivio() {
		return this.idArchivio;
	}

	public void setIdArchivio(Archivio idArchivio) {
		this.idArchivio = idArchivio;
	}

	public Set<File> getFileCollection() {
		return this.fileCollection;
	}

	public void setFileCollection(Set<File> fileCollection) {
		this.fileCollection = fileCollection;
	}

}
