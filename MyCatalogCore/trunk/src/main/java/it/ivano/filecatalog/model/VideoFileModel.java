package it.ivano.filecatalog.model;

import java.util.ArrayList;
import java.util.List;

public class VideoFileModel extends FileModel {
	
	private String anno;
	private String codec;
	private String altezza;
	private String larghezza;
	private List<AudioComponent> audio;

	public VideoFileModel() {
		this.audio = new ArrayList<AudioComponent>();
	}
	
	public String getAnno() {
		return anno;
	}

	public void setAnno(String anno) {
		this.anno = anno;
	}

	public String getCodec() {
		return codec;
	}

	public void setCodec(String codec) {
		this.codec = codec;
	}

	public String getAltezza() {
		return altezza;
	}

	public void setAltezza(String altezza) {
		this.altezza = altezza;
	}

	public String getLarghezza() {
		return larghezza;
	}

	public void setLarghezza(String larghezza) {
		this.larghezza = larghezza;
	}

	public List<AudioComponent> getAudio() {
		return audio;
	}

	public void setAudio(List<AudioComponent> audio) {
		this.audio = audio;
	}
	
	public void addAudio(AudioComponent audio) {
		this.audio.add(audio);
	}
 
}
