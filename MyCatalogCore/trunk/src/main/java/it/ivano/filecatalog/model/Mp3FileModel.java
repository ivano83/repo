package it.ivano.filecatalog.model;

public class Mp3FileModel extends FileModel {

	private String artista;
	private String traccia;
	private String album;
	private String genere;
	private String anno;
	private String numeroTraccia;
	
	public String getArtista() {
		return artista;
	}
	public void setArtista(String artista) {
		this.artista = artista;
	}
	public String getTraccia() {
		return traccia;
	}
	public void setTraccia(String traccia) {
		this.traccia = traccia;
	}
	public String getAlbum() {
		return album;
	}
	public void setAlbum(String album) {
		this.album = album;
	}
	public String getGenere() {
		return genere;
	}
	public void setGenere(String genere) {
		this.genere = genere;
	}
	public String getAnno() {
		return anno;
	}
	public void setAnno(String anno) {
		this.anno = anno;
	}
	public String getNumeroTraccia() {
		return numeroTraccia;
	}
	public void setNumeroTraccia(String numeroTraccia) {
		this.numeroTraccia = numeroTraccia;
	}
	
	
}
