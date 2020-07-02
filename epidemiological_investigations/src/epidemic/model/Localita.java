
package epidemic.model;

/**
 * Rappresenta una Località geografica caratterizzata da nome e superficie in km^2
 * 
 * @author Cristiano Di Bari
 * @author Matteo Cavaliere
 * @author Enrico Lonardi
 *
 */
public abstract class Localita {
	private int id;
	private String nome;
	private double superficie;
	
	/**
	 * Crea una località
	 * 
	 * @param nome nome della località
	 * @param superficie superficie in km^2
	 */
	public Localita(String nome, double superficie) {
		if(!isValid(nome) || superficie < 0)
			throw new IllegalArgumentException();
		
		
			
		this.nome = nome;
		this.superficie = superficie;
	}
	
	/**
	 * Verifica che il nome della località non contenga cifre, non sia vuoto o null.
	 * Converte il primo carattere da minuscolo a maiuscolo
	 * 
	 * @param nome nome della località
	 * @return true se il nome è valido
	 */
	public boolean isValid(String nome) {
		if(nome != null) {
			Character.toUpperCase(nome.charAt(0));
			boolean hasDigit = nome.matches(".*\\d+.*");
			return !(nome.isBlank() || hasDigit);
		}
		
		return false;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}
	
	public String getNome() {
		return nome;
	}
	
	public void setSuperficie(double superficie) {
		if(superficie >= 0)
			this.superficie = superficie;
	}
	
	public double getSuperficie() {
		return superficie;
	}
}