import java.io.File;

/**
 * @author Ivano
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Linguaggio {

	private String nome;
	private File file;

	public Linguaggio() {
		this.nome = "";
		this.file = null;
	}
	public Linguaggio(String nome, File file) {
		this.nome = nome;
		this.file = file;
	}
	public void setNome(String n) {
		this.nome = n;
	}
	public void setFile(File f) {
		this.file = f;
	}
	public String getNome() {
		return this.nome;
	}
	public File getFile() {
		return this.file;
	}
}
