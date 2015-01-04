package it.ivano.filecatalog.dto;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class File implements Serializable {
	@Id
	@Column(name="id_file")
	private String idFile;

	@Column(name="id_archivio")
	private BigInteger idArchivio;

	@Column(name="file_name")
	private String fileName;

	@Column(name="id_cartella")
	private BigInteger idCartella;

	private String estensione;

	@Column(name="ultima_modifica")
	private Date ultimaModifica;

	private String dimensione;

	@Column(name="data_inserimento")
	private Date dataInserimento;

	@Column(name="data_ultimo_aggiornamento")
	private Date dataUltimoAggiornamento;

	@Column(name="flag_cancellazione")
	private byte flagCancellazione;

	@ManyToOne
	@JoinColumn(name="id_mime_type")
	private MimeType idMimeType;

	private static final long serialVersionUID = 1L;

	public File() {
		super();
	}

	public String getIdFile() {
		return this.idFile;
	}

	public void setIdFile(String idFile) {
		this.idFile = idFile;
	}

	public BigInteger getIdArchivio() {
		return this.idArchivio;
	}

	public void setIdArchivio(BigInteger idArchivio) {
		this.idArchivio = idArchivio;
	}

	public String getFileName() {
		return this.fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public BigInteger getIdCartella() {
		return this.idCartella;
	}

	public void setIdCartella(BigInteger idCartella) {
		this.idCartella = idCartella;
	}

	public String getEstensione() {
		return this.estensione;
	}

	public void setEstensione(String estensione) {
		this.estensione = estensione;
	}

	public Date getUltimaModifica() {
		return this.ultimaModifica;
	}

	public void setUltimaModifica(Date ultimaModifica) {
		this.ultimaModifica = ultimaModifica;
	}

	public String getDimensione() {
		return this.dimensione;
	}

	public void setDimensione(String dimensione) {
		this.dimensione = dimensione;
	}

	public Date getDataInserimento() {
		return this.dataInserimento;
	}

	public void setDataInserimento(Date dataInserimento) {
		this.dataInserimento = dataInserimento;
	}

	public Date getDataUltimoAggiornamento() {
		return this.dataUltimoAggiornamento;
	}

	public void setDataUltimoAggiornamento(Date dataUltimoAggiornamento) {
		this.dataUltimoAggiornamento = dataUltimoAggiornamento;
	}

	public byte getFlagCancellazione() {
		return this.flagCancellazione;
	}

	public void setFlagCancellazione(byte flagCancellazione) {
		this.flagCancellazione = flagCancellazione;
	}

	public MimeType getIdMimeType() {
		return this.idMimeType;
	}

	public void setIdMimeType(MimeType idMimeType) {
		this.idMimeType = idMimeType;
	}

}
