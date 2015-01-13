package it.ivano.catalog.backend.dto;
import java.io.Serializable;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="mime_type")
public class MimeType implements Serializable {
	@Id
	@Column(name="id_mime_type")
	private Long idMimeType;

	private String area;

	@Column(name="sub_area")
	private String subArea;

	@Column(name="mime_type")
	private String mimeType;

	@Column(name="lista_estensioni")
	private String listaEstensioni;

	@Column(name="flag_tipo_base")
	private int flagTipoBase;

	@OneToMany(mappedBy="idMimeType")
	private Set<File> fileCollection;

	private static final long serialVersionUID = 1L;

	public MimeType() {
		super();
	}

	public Long getIdMimeType() {
		return this.idMimeType;
	}

	public void setIdMimeType(Long idMimeType) {
		this.idMimeType = idMimeType;
	}

	public String getArea() {
		return this.area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getSubArea() {
		return this.subArea;
	}

	public void setSubArea(String subArea) {
		this.subArea = subArea;
	}

	public String getMimeType() {
		return this.mimeType;
	}

	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}

	public String getListaEstensioni() {
		return this.listaEstensioni;
	}

	public void setListaEstensioni(String listaEstensioni) {
		this.listaEstensioni = listaEstensioni;
	}

	public int getFlagTipoBase() {
		return this.flagTipoBase;
	}

	public void setFlagTipoBase(int flagTipoBase) {
		this.flagTipoBase = flagTipoBase;
	}

	public Set<File> getFileCollection() {
		return this.fileCollection;
	}

	public void setFileCollection(Set<File> fileCollection) {
		this.fileCollection = fileCollection;
	}

}
