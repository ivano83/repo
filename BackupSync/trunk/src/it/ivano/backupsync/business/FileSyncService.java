package it.ivano.backupsync.business;

import it.ivano.backupsync.model.CompareItem;
import it.ivano.backupsync.model.FileSyncModel;
import it.ivano.backupsync.util.Messaggi;
import it.ivano.backupsync.util.UidUtility;

import java.io.File;

public class FileSyncService {

	
	public CompareItem segnalaDifferenza(File f1, FileSyncModel input) throws Exception {
		
		CompareItem curr = new CompareItem();
		curr.setFileName(f1.getName());
		curr.setParentFolder(f1.getParent().split("\\\\")[f1.getParent().split("\\\\").length-1]);
		curr.setFullPathFile(f1.getPath());
		curr.setSize(f1.length());
		curr.setFolder(f1.isDirectory());
		curr.setId(UidUtility.getInstance().getUid());
		curr.setRelativePath(f1.getParent().replace(input.getInitialPath(), ""));
		
		return curr;
	}
	
	public void comparazioneFile(FileSyncModel input) throws Exception {

		if(input==null)
			return;
		try {
			
			// INIZIO
			if(input.getPath()==null) {
				input.setPath(input.getInitialPath());
			}
			if(input.getPathBackup()==null) {
				input.setPathBackup(input.getInitialPathBackup());
			}
			
			String path = input.getPath();
			String pathBackup = input.getPathBackup();

			File f1 = new File(path);
			
			// eccezioni thumbs
			if(f1.getName().contains("Thumbs.db"))
				return;
			
			// caso cartelle
			if(f1.isDirectory()) {
				
				// se la cartella non esiste dall'altra parte lo segnalo
				File f2 = new File(f1.getPath().replace(path, pathBackup));
				if(!f2.exists()) {
					CompareItem c = segnalaDifferenza(f1, input);
					c.setDescrizioneDifferenza(Messaggi.NO_CARTELLA_IN_BACKUP);
					input.getListaDifferenze().add(c);
					// non vado avanti a cercare i file interni, non serve
				}
				else {
					// cerco file interni
					File[] listaFile = f1.listFiles();
					for(File fx : listaFile) {
						File fy = new File(fx.getPath().replace(input.getInitialPath(), input.getInitialPathBackup()));
						input.setPath(fx.getPath());
						input.setPathBackup(fy.getPath());
						comparazioneFile(input);
					}		
				}

			} else {
				// è un file
				// se la cartella non esiste dall'altra parte lo segnalo
				File f2 = new File(f1.getPath().replace(path, pathBackup));
				if(!f2.exists()) {
					CompareItem c = segnalaDifferenza(f1, input);
					c.setDescrizioneDifferenza(Messaggi.NO_FILE_IN_BACKUP);
					input.getListaDifferenze().add(c);
				}
				else if(!fileUguale(f1,f2)) {
					CompareItem c = segnalaDifferenza(f1, input);
					c.setDescrizioneDifferenza(Messaggi.FILE_DIFFERENTE);
					CompareItem c2 = segnalaDifferenza(f2, input);
					c2.setDescrizioneDifferenza(Messaggi.FILE_DIFFERENTE);
					c.setOtherItem(c2);
					input.getListaDifferenze().add(c);
				}

			}


		} catch (Exception e) {
			throw e;
		}

	}
	
	private boolean fileUguale(File f1, File f2) {
		if(f1.length()!=f2.length())
			return false;
		return true;
	}

	public static void main(String[] args) throws Exception {
		
		FileSyncService l = new FileSyncService();
		FileSyncModel fs = new FileSyncModel();
//		fs.setInitialPath("C:\\Users\\ivano\\Documents\\IVANO\\IVANO_PRIVATE\\Ivano Documents");
//		fs.setInitialPathBackup("G:\\IVANO\\Ivano Documents");
		
//		fs.setInitialPath("C:\\Users\\ivano\\Documents\\IVANO\\IVANO_PRIVATE\\Book Fotografico (salvati)");
//		fs.setInitialPathBackup("G:\\IVANO\\Book Fotografico");
		
		fs.setInitialPath("C:\\Users\\ivano\\Documents\\IVANO\\IVANO_PRIVATE\\Book Fotografico\\Alice");
		fs.setInitialPathBackup("G:\\IVANO\\Book Fotografico\\Alice");
		
		l.comparazioneFile(fs);
		
		System.out.println(fs.getListaDifferenze().size());
		for(CompareItem c : fs.getListaDifferenze()) {
			System.out.println(c);
		}
	}
}
