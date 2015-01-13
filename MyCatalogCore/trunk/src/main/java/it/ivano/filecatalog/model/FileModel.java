package it.ivano.filecatalog.model;

import java.util.Date;

public class FileModel {

	private String nome;
	private String estensione;
	private String dimensione;
	private Date dataModifica;
	private MimeTypeModel mimeType;
	
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getEstensione() {
		return estensione;
	}
	public void setEstensione(String estensione) {
		this.estensione = estensione;
	}
	public String getDimensione() {
		return dimensione;
	}
	public void setDimensione(String dimensione) {
		this.dimensione = dimensione;
	}
	public Date getDataModifica() {
		return dataModifica;
	}
	public void setDataModifica(Date dataModifica) {
		this.dataModifica = dataModifica;
	}
	public MimeTypeModel getMimeType() {
		return mimeType;
	}
	public void setMimeType(MimeTypeModel mimeType) {
		this.mimeType = mimeType;
	}
	
	
}
