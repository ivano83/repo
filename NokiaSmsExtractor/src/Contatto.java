
/**
 * @author Ivano
 *
 * Oggetto contatto, che contiene numero e nome
 */
public class Contatto implements Comparable {
	private String numero;
	private String nome;
	
	public Contatto() {
	}
	public Contatto(String numero, String nome) {
		this.numero = numero;
		this.nome = nome;
	}
	public void setNumero(String numero) {
		this.numero = numero;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getNumero() {
		return this.numero;
	}
	public String getNome() {
		return this.nome;
	}
	public int hashCode() {
		return this.nome.hashCode();
	}
	public boolean equals(Object o) {
		return this.nome.equals(((Contatto)o).getNome());
	}
	public int compareTo(Object o) {
		return this.nome.compareTo(((Contatto)o).getNome());
	}
}
