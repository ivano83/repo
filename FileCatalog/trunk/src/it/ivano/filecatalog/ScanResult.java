package it.ivano.filecatalog;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ScanResult {

	public List<Object> result;
	public List<String> nextFiles;
	
	public ScanResult() {
		result = new ArrayList<Object>();
		nextFiles = new ArrayList<String>();
	}
	
	public List<Object> getResult() {
		return result;
	}
	public void setResult(List<Object> result) {
		this.result = result;
	}
	public void addResult(String result) {
		this.result.add(result);
	}
	public List<String> getNextFiles() {
		return nextFiles;
	}
	public void setNextFiles(List<String> nextFiles) {
		this.nextFiles = nextFiles;
	}
	public void addNextFile(String nextFile) {
		this.nextFiles.add(nextFile);
	}
	public void addNextFiles(File file) {
		for(String nextFile : file.list()) {
			this.nextFiles.add(file.getPath()+"\\"+nextFile);
		}
	}
	
	
}
