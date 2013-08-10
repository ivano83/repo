package it.ivano.backupsync.model;

import java.util.ArrayList;
import java.util.List;

public class FileSyncModel {

	private String initialPath;
	private String initialPathBackup;
	
	private String path;
	private String pathBackup;
	
	private List<CompareItem> listaDifferenze;

	
	public String getInitialPath() {
		return initialPath;
	}

	public void setInitialPath(String initialPath) {
		this.initialPath = initialPath;
	}

	public String getInitialPathBackup() {
		return initialPathBackup;
	}

	public void setInitialPathBackup(String initialPathBackup) {
		this.initialPathBackup = initialPathBackup;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getPathBackup() {
		return pathBackup;
	}

	public void setPathBackup(String pathBackup) {
		this.pathBackup = pathBackup;
	}

	public List<CompareItem> getListaDifferenze() {
		if(listaDifferenze==null)
			listaDifferenze = new ArrayList<CompareItem>();
		return listaDifferenze;
	}

	public void setListaDifferenze(List<CompareItem> listaDifferenze) {
		this.listaDifferenze = listaDifferenze;
	}
	
	
}
