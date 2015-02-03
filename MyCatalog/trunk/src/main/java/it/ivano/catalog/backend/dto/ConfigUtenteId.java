package it.ivano.catalog.backend.dto;

// Generated 3-feb-2015 21.53.09 by Hibernate Tools 4.3.1

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * ConfigUtenteId generated by hbm2java
 */
@Embeddable
public class ConfigUtenteId implements java.io.Serializable {

	private long idUtente;
	private long idConfig;
	private String configType;

	public ConfigUtenteId() {
	}

	public ConfigUtenteId(long idUtente, long idConfig, String configType) {
		this.idUtente = idUtente;
		this.idConfig = idConfig;
		this.configType = configType;
	}

	@Column(name = "id_utente", nullable = false)
	public long getIdUtente() {
		return this.idUtente;
	}

	public void setIdUtente(long idUtente) {
		this.idUtente = idUtente;
	}

	@Column(name = "id_config", nullable = false)
	public long getIdConfig() {
		return this.idConfig;
	}

	public void setIdConfig(long idConfig) {
		this.idConfig = idConfig;
	}

	@Column(name = "config_type", nullable = false, length = 10)
	public String getConfigType() {
		return this.configType;
	}

	public void setConfigType(String configType) {
		this.configType = configType;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof ConfigUtenteId))
			return false;
		ConfigUtenteId castOther = (ConfigUtenteId) other;

		return (this.getIdUtente() == castOther.getIdUtente())
				&& (this.getIdConfig() == castOther.getIdConfig())
				&& ((this.getConfigType() == castOther.getConfigType()) || (this
						.getConfigType() != null
						&& castOther.getConfigType() != null && this
						.getConfigType().equals(castOther.getConfigType())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + (int) this.getIdUtente();
		result = 37 * result + (int) this.getIdConfig();
		result = 37
				* result
				+ (getConfigType() == null ? 0 : this.getConfigType()
						.hashCode());
		return result;
	}

}
