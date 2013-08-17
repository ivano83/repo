package it.ivano.utility.app;

import it.ivano.utility.file.FileUtility;

import java.util.Set;

public class RiempiFileCueMp3 {

	
	public static void main(String[] args) {
		
		try {
			String pathCue = "C:\\Users\\ivano\\Desktop\\Crixy House Compilation Vol.10\\Crixy House Compilation Vol.10.cue";
			String pathList = "C:\\Users\\ivano\\Desktop\\Crixy House Compilation Vol.10\\Tracklist.txt";
			
			String fileCue = FileUtility.estraiTestoDaFile(FileUtility.caricaFile(pathCue).get(0));
			Set<String> fileList = FileUtility.estraiRigheDaFile(FileUtility.caricaFile(pathList).get(0));
			

			for(String s : fileList) {
				if(!s.isEmpty()) {
					String track = s.substring(5,s.length());
					String numb = s.substring(0,2);
					fileCue = fileCue.replace("(Track "+numb+")", track);
				}
				
			}
			System.out.println(fileCue);
			String nome = pathCue.substring(0, pathCue.length()-4);
			String ext = pathCue.substring(pathCue.length()-4);
			FileUtility.salvaFile(nome+"+"+ext, fileCue);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
