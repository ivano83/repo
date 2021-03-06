package it.ivano.catalog.backend.dto;

// Generated 3-feb-2015 21.02.36 by Hibernate Tools 4.3.1

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * ConfigUtente generated by hbm2java
 */
@Entity
@Table(name = "config_utente", catalog = "filecatalog")
public class ConfigUtente implements java.io.Serializable {

	private ConfigUtenteId id;
	private Utente utente;

	public ConfigUtente() {
	}

	public ConfigUtente(ConfigUtenteId id, Utente utente) {
		this.id = id;
		this.utente = utente;
	}

	@EmbeddedId
	@AttributeOverrides({
			@AttributeOverride(name = "idUtente", column = @Column(name = "id_utente", nullable = false)),
			@AttributeOverride(name = "idConfig", column = @Column(name = "id_config", nullable = false)),
			@AttributeOverride(name = "configType", column = @Column(name = "config_type", nullable = false, length = 10)) })
	public ConfigUtenteId getId() {
		return this.id;
	}

	public void setId(ConfigUtenteId id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_utente", nullable = false, insertable = false, updatable = false)
	public Utente getUtente() {
		return this.utente;
	}

	public void setUtente(Utente utente) {
		this.utente = utente;
	}

}
