package it.ivano.catalog.backend.dto;

// Generated 3-feb-2015 21.02.36 by Hibernate Tools 4.3.1

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Cartella generated by hbm2java
 */
@Entity
@Table(name = "cartella", catalog = "filecatalog")
public class Cartella implements java.io.Serializable {

	private Long idCartella;
	private Archivio archivio;
	private String cartella;
	private Long idCartellaPadre;
	private Boolean cartellaBase;
	private String cartellePrecedenti;
	private int eliminato;
	private Date dataUltimaLettura;
	private Set<File> files = new HashSet<File>(0);

	public Cartella() {
	}

	public Cartella(Archivio archivio, String cartella, int eliminato) {
		this.archivio = archivio;
		this.cartella = cartella;
		this.eliminato = eliminato;
	}

	public Cartella(Archivio archivio, String cartella, Long idCartellaPadre,
			Boolean cartellaBase, String cartellePrecedenti, int eliminato,
			Date dataUltimaLettura, Set<File> files) {
		this.archivio = archivio;
		this.cartella = cartella;
		this.idCartellaPadre = idCartellaPadre;
		this.cartellaBase = cartellaBase;
		this.cartellePrecedenti = cartellePrecedenti;
		this.eliminato = eliminato;
		this.dataUltimaLettura = dataUltimaLettura;
		this.files = files;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id_cartella", unique = true, nullable = false)
	public Long getIdCartella() {
		return this.idCartella;
	}

	public void setIdCartella(Long idCartella) {
		this.idCartella = idCartella;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_archivio", nullable = false)
	public Archivio getArchivio() {
		return this.archivio;
	}

	public void setArchivio(Archivio archivio) {
		this.archivio = archivio;
	}

	@Column(name = "cartella", nullable = false)
	public String getCartella() {
		return this.cartella;
	}

	public void setCartella(String cartella) {
		this.cartella = cartella;
	}

	@Column(name = "id_cartella_padre")
	public Long getIdCartellaPadre() {
		return this.idCartellaPadre;
	}

	public void setIdCartellaPadre(Long idCartellaPadre) {
		this.idCartellaPadre = idCartellaPadre;
	}

	@Column(name = "cartella_base")
	public Boolean getCartellaBase() {
		return this.cartellaBase;
	}

	public void setCartellaBase(Boolean cartellaBase) {
		this.cartellaBase = cartellaBase;
	}

	@Column(name = "cartelle_precedenti", length = 2000)
	public String getCartellePrecedenti() {
		return this.cartellePrecedenti;
	}

	public void setCartellePrecedenti(String cartellePrecedenti) {
		this.cartellePrecedenti = cartellePrecedenti;
	}

	@Column(name = "eliminato", nullable = false)
	public int getEliminato() {
		return this.eliminato;
	}

	public void setEliminato(int eliminato) {
		this.eliminato = eliminato;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "data_ultima_lettura", length = 19)
	public Date getDataUltimaLettura() {
		return this.dataUltimaLettura;
	}

	public void setDataUltimaLettura(Date dataUltimaLettura) {
		this.dataUltimaLettura = dataUltimaLettura;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "cartella")
	public Set<File> getFiles() {
		return this.files;
	}

	public void setFiles(Set<File> files) {
		this.files = files;
	}

}
