package it.ivano.filecatalog;

import it.ivano.filecatalog.utils.CommonUtils;

import java.io.File;
import java.util.List;

public class FileCatalogCore extends BaseCatalog {

	public FileCatalogCore() {
		super(FileCatalogCore.class);
	}

	public ScanResult scanFiles(List<String> listFiles) {
		ScanResult res = new ScanResult();
		
		CommonUtils utils = new CommonUtils();
		for(String f : listFiles) {
			
			// check estensione
			String ext = utils.getEstensioneFile(f);
			
			File file = new File(f);
			
			// extract metadati, check file type
			
			if(file.isDirectory()) {
				res.addNextFiles(file.list());
			}
			
		}
		return res;
	}
	
}
