package it.ivano.filecatalog.model;


public class MimeTypeModel {

	private Long idMimeType;
	private String area;
	private String subArea;
	private String mimeType;
	private String listaEstensioni;
	
	
	public Long getIdMimeType() {
		return idMimeType;
	}
	public void setIdMimeType(Long idMimeType) {
		this.idMimeType = idMimeType;
	}
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	public String getSubArea() {
		return subArea;
	}
	public void setSubArea(String subArea) {
		this.subArea = subArea;
	}
	public String getMimeType() {
		return mimeType;
	}
	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}
	public String getListaEstensioni() {
		return listaEstensioni;
	}
	public void setListaEstensioni(String listaEstensioni) {
		this.listaEstensioni = listaEstensioni;
	}
	
	
	
}
