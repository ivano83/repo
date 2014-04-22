package it.ivano.filecatalog.model;

import java.util.Date;

public interface FileInterface {
	
	public String getNome();
	public String getEstensione();
	public String getDimensione();
	public Date getDataModifica();

	public String setNome(String nome);
	public String setEstensione(String estensione);
	public String setDimensione(String dimensione);
	public Date setDataModifica(Date dataModifica);

}
