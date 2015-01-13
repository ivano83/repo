package it.ivano.catalog.backend.dto;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="file_video_audio")
public class FileVideoAudio implements Serializable {
	@Id
	@Column(name="id_file_video_audio")
	private int idFileVideoAudio;

	private String descrizione;

	private String codec;

	private String bitrate;

	private String canali;

	private String linguaggio;

	@ManyToOne
	@JoinColumn(name="id_file_video")
	private FileVideo idFileVideo;

	private static final long serialVersionUID = 1L;

	public FileVideoAudio() {
		super();
	}

	public int getIdFileVideoAudio() {
		return this.idFileVideoAudio;
	}

	public void setIdFileVideoAudio(int idFileVideoAudio) {
		this.idFileVideoAudio = idFileVideoAudio;
	}

	public String getDescrizione() {
		return this.descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public String getCodec() {
		return this.codec;
	}

	public void setCodec(String codec) {
		this.codec = codec;
	}

	public String getBitrate() {
		return this.bitrate;
	}

	public void setBitrate(String bitrate) {
		this.bitrate = bitrate;
	}

	public String getCanali() {
		return this.canali;
	}

	public void setCanali(String canali) {
		this.canali = canali;
	}

	public String getLinguaggio() {
		return this.linguaggio;
	}

	public void setLinguaggio(String linguaggio) {
		this.linguaggio = linguaggio;
	}

	public FileVideo getIdFileVideo() {
		return this.idFileVideo;
	}

	public void setIdFileVideo(FileVideo idFileVideo) {
		this.idFileVideo = idFileVideo;
	}

}
