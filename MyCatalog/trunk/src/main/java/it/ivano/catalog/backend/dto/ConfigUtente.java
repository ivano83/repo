package it.ivano.catalog.backend.dto;

import java.io.Serializable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="config_utente")
public class ConfigUtente implements Serializable {
	@EmbeddedId
	private ConfigUtentePK pk;

	private static final long serialVersionUID = 1L;

	public ConfigUtente() {
		super();
	}

	public ConfigUtentePK getPk() {
		return this.pk;
	}

	public void setPk(ConfigUtentePK pk) {
		this.pk = pk;
	}

}
