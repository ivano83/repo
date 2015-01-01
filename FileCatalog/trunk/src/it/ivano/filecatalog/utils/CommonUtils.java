package it.ivano.filecatalog.utils;

import it.ivano.filecatalog.BaseCatalog;
import it.ivano.filecatalog.model.FileInterface;
import it.ivano.filecatalog.model.Mp3FileModel;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;

public class CommonUtils extends BaseCatalog {
	
	public CommonUtils() {
		super(CommonUtils.class);
	}

	public void estraiMetadatiStandard(File file, Object input) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, SecurityException, NoSuchMethodException {
		Method metodo = input.getClass().getMethod("setNome", String.class);
		metodo.invoke(input, file.getName());
		metodo = input.getClass().getMethod("setEstensione", String.class);
		metodo.invoke(input, getEstensioneFile(file.getName()));
		metodo = input.getClass().getMethod("setDataModifica", Date.class);
		metodo.invoke(input, new Date(file.lastModified()));
		metodo = input.getClass().getMethod("setDimensione", String.class);
		metodo.invoke(input, file.length()+"");
	}
	
	public String getEstensioneFile(String fileName) {
		String[] fileNameSplit = fileName.split("\\.");
		if(fileNameSplit.length>1) {
			return fileNameSplit[fileNameSplit.length-1];
		}
		return null;
	}

}
