package it.ivano.filecatalog.dto;

import java.io.Serializable;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="file_video")
public class FileVideo implements Serializable {
	@Id
	@Column(name="id_file_video")
	private String idFileVideo;

	private String codec;

	private String anno;

	private String larghezza;

	private String altezza;

	@ManyToOne
	@JoinColumn(name="id_file")
	private File idFile;

	@OneToMany(mappedBy="idFileVideo")
	private Set<FileVideoAudio> fileVideoAudioCollection;

	private static final long serialVersionUID = 1L;

	public FileVideo() {
		super();
	}

	public String getIdFileVideo() {
		return this.idFileVideo;
	}

	public void setIdFileVideo(String idFileVideo) {
		this.idFileVideo = idFileVideo;
	}

	public String getCodec() {
		return this.codec;
	}

	public void setCodec(String codec) {
		this.codec = codec;
	}

	public String getAnno() {
		return this.anno;
	}

	public void setAnno(String anno) {
		this.anno = anno;
	}

	public String getLarghezza() {
		return this.larghezza;
	}

	public void setLarghezza(String larghezza) {
		this.larghezza = larghezza;
	}

	public String getAltezza() {
		return this.altezza;
	}

	public void setAltezza(String altezza) {
		this.altezza = altezza;
	}

	public File getIdFile() {
		return this.idFile;
	}

	public void setIdFile(File idFile) {
		this.idFile = idFile;
	}

	public Set<FileVideoAudio> getFileVideoAudioCollection() {
		return this.fileVideoAudioCollection;
	}

	public void setFileVideoAudioCollection(Set<FileVideoAudio> fileVideoAudioCollection) {
		this.fileVideoAudioCollection = fileVideoAudioCollection;
	}

}
