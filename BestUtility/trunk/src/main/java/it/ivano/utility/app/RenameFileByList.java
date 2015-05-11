package it.ivano.utility.app;

import it.ivano.utility.file.FileUtility;

import java.io.File;
import java.util.List;
import java.util.Set;

public class RenameFileByList {

	

	public static void main(String[] args) {
		
		try {
			String pathFolder = "C:\\Users\\Ivano\\Downloads\\Death Note";
			String pathFileList = "C:\\Users\\ivano\\Desktop\\ranma.txt";
			
			Set<String> fileList = FileUtility.estraiRigheDaFile(FileUtility.caricaFile(pathFileList).get(0));
			List<File> files = FileUtility.caricaFile(pathFolder);

			
			
			for(String s : fileList) {
				if(!s.isEmpty()) {
					String numb = s.substring(0,2);
					
					for(File f : files) {
						if(f.getName().startsWith(numb)) {
							String ext = f.getName().split("\\.")[f.getName().split("\\.").length-1];
							String newFileName = s + "."+ext;
							File fileNew = new File(f.getParent()+"\\"+newFileName);
							f.renameTo(fileNew);
							System.out.println("replace file " + f.getName()+ " --> "+fileNew.getPath());
						}
						
					}
				}
				
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
