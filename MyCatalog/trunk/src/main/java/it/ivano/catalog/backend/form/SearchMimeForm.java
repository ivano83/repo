package it.ivano.catalog.backend.form;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class SearchMimeForm {
	
	@NotNull
	@Size(min=2, message="Inserire almeno una parola da 2 lettere")
	private String text;

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
	
	

}
