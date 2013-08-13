package it.ivano.utility.filtri;

import java.io.File;
import java.io.FileFilter;

public class CustomFileFilter implements FileFilter {

	
	private String[] okFileExtensions;

	public CustomFileFilter(String... listaFiltri) throws NullPointerException {

		if(listaFiltri == null) {
			throw new NullPointerException("Nessun Filtro Definito!");
		}
		okFileExtensions = listaFiltri;
	}


	public boolean accept(File file) {
		for (String extension : okFileExtensions) {
			if (file.getName().toLowerCase().endsWith(extension)) {
				return true;
			}
		}
		return false;
	}
	
}
