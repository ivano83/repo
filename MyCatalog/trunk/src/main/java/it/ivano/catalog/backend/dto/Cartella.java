package it.ivano.catalog.backend.dto;

import java.io.Serializable;
import java.math.BigInteger;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Cartella implements Serializable {
	@Id
	@Column(name="id_cartella")
	private String idCartella;

	private String cartella;

	@Column(name="id_cartella_padre")
	private BigInteger idCartellaPadre;

	@Column(name="id_archivio")
	private BigInteger idArchivio;

	@Column(name="cartella_base")
	private byte cartellaBase;

	@Column(name="cartelle_precedenti")
	private String cartellePrecedenti;

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

	public BigInteger getIdArchivio() {
		return this.idArchivio;
	}

	public void setIdArchivio(BigInteger idArchivio) {
		this.idArchivio = idArchivio;
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

}
