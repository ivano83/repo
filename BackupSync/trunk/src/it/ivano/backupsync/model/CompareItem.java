package it.ivano.backupsync.model;

import it.ivano.backupsync.util.Messaggi;

public class CompareItem {

	private int id;
	private String fileName;
	private boolean isFolder;
	private String parentFolder;
	private String relativePath;
	private long size;
	
	private String fullPathFile;

	private CompareItem otherItem;
	
	private String descrizioneDifferenza;
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public boolean isFolder() {
		return isFolder;
	}

	public void setFolder(boolean isFolder) {
		this.isFolder = isFolder;
	}

	public String getParentFolder() {
		return parentFolder;
	}

	public void setParentFolder(String parentFolder) {
		this.parentFolder = parentFolder;
	}

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}

	public String getFullPathFile() {
		return fullPathFile;
	}

	public void setFullPathFile(String fullPathFile) {
		this.fullPathFile = fullPathFile;
	}

	public void setRelativePath(String relativePath) {
		this.relativePath = relativePath;
	}

	public String getRelativePath() {
		return relativePath;
	}

	public void setOtherItem(CompareItem otherItem) {
		this.otherItem = otherItem;
	}

	public CompareItem getOtherItem() {
		return otherItem;
	}

	public void setDescrizioneDifferenza(String descrizioneDifferenza) {
		this.descrizioneDifferenza = descrizioneDifferenza;
	}

	public String getDescrizioneDifferenza() {
		return descrizioneDifferenza;
	}
	
	public String toString() {
		
		
		String res = fileName+" --> "+descrizioneDifferenza;
		if(descrizioneDifferenza!=null && descrizioneDifferenza.equals(Messaggi.FILE_DIFFERENTE))
			res += " [SIZE="+size+"] VS [SIZE_BCK="+otherItem.size+"]";
		
		res += "   {"+fullPathFile+"}";
		
		return res;
	}
	
}
