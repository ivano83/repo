package it.ivano.catalog.backend.dto;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class ConfigUtentePK implements Serializable {
	@Column(name="id_utente")
	private Long idUtente;

	@Column(name="id_config")
	private Long idConfig;

	@Column(name="config_type")
	private String configType;

	private static final long serialVersionUID = 1L;

	public ConfigUtentePK() {
		super();
	}

	public Long getIdUtente() {
		return this.idUtente;
	}

	public void setIdUtente(Long idUtente) {
		this.idUtente = idUtente;
	}

	public Long getIdConfig() {
		return this.idConfig;
	}

	public void setIdConfig(Long idConfig) {
		this.idConfig = idConfig;
	}

	public String getConfigType() {
		return this.configType;
	}

	public void setConfigType(String configType) {
		this.configType = configType;
	}

	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}
		if ( ! (o instanceof ConfigUtentePK)) {
			return false;
		}
		ConfigUtentePK other = (ConfigUtentePK) o;
		return this.idUtente.equals(other.idUtente)
			&& this.idConfig.equals(other.idConfig)
			&& this.configType.equals(other.configType);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.idUtente.hashCode();
		hash = hash * prime + this.idConfig.hashCode();
		hash = hash * prime + this.configType.hashCode();
		return hash;
	}

}
