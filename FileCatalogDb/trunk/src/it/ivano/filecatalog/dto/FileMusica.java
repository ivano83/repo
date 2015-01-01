package it.ivano.filecatalog.dto;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="file_musica")
public class FileMusica implements Serializable {
	@Id
	@Column(name="id_file_musica")
	private String idFileMusica;

	private String artista;

	private String traccia;

	private String album;

	private String anno;

	@Column(name="numero_traccia")
	private String numeroTraccia;

	private String genere;

	@ManyToOne
	@JoinColumn(name="id_file")
	private File idFile;

	private static final long serialVersionUID = 1L;

	public FileMusica() {
		super();
	}

	public String getIdFileMusica() {
		return this.idFileMusica;
	}

	public void setIdFileMusica(String idFileMusica) {
		this.idFileMusica = idFileMusica;
	}

	public String getArtista() {
		return this.artista;
	}

	public void setArtista(String artista) {
		this.artista = artista;
	}

	public String getTraccia() {
		return this.traccia;
	}

	public void setTraccia(String traccia) {
		this.traccia = traccia;
	}

	public String getAlbum() {
		return this.album;
	}

	public void setAlbum(String album) {
		this.album = album;
	}

	public String getAnno() {
		return this.anno;
	}

	public void setAnno(String anno) {
		this.anno = anno;
	}

	public String getNumeroTraccia() {
		return this.numeroTraccia;
	}

	public void setNumeroTraccia(String numeroTraccia) {
		this.numeroTraccia = numeroTraccia;
	}

	public String getGenere() {
		return this.genere;
	}

	public void setGenere(String genere) {
		this.genere = genere;
	}

	public File getIdFile() {
		return this.idFile;
	}

	public void setIdFile(File idFile) {
		this.idFile = idFile;
	}

}
