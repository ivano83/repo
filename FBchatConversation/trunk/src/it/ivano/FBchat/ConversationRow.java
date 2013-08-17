package it.ivano.FBchat;

public class ConversationRow {

	private String nome;
	private String ora;
	private String testo;
	
	public ConversationRow() {
		nome = "";
		ora = "";
		testo = "";
	}

	public ConversationRow(String nome, String ora, String testo) {
		this.nome = nome;
		this.ora = ora;
		this.testo = testo;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getOra() {
		return ora;
	}

	public void setOra(String ora) {
		this.ora = ora;
	}

	public String getTesto() {
		return testo;
	}

	public void setTesto(String testo) {
		this.testo = testo;
	}
	
	public String toString() {
		
		return this.nome + " - " + this.ora + " - " + this.testo;
	}
	
	
	
}
