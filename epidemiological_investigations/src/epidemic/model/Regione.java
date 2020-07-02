package epidemic.model;

/**
 * Rappresenta una Regione italiana con uno specifico capoluogo
 * 
 * @author Cristiano Di Bari
 * @author Matteo Cavaliere
 * @author Enrico Lonardi
 *
 */
public class Regione extends Localita{
	private String capoluogo;
	
	/**
	 * Crea una regione
	 * 
	 * @param nome nome della regione
	 * @param superficie superficie in km^2
	 * @param capoluogo nome del capoluogo di regione
	 */
	public Regione(String nome, double superficie, String capoluogo) {
		super(nome, superficie);
		if(!isValid(capoluogo))
			throw new IllegalArgumentException();
		this.capoluogo = capoluogo;
	}
	
	public String getCapoluogo() {
		return capoluogo;
	}

	public void setCapoluogo(String capoluogo) {
		this.capoluogo = capoluogo;
	}
	
	@Override
	public boolean equals(Object obj) {
		return (obj instanceof Regione) && ((Regione)obj).getId() == this.getId();
	}
	
	@Override
	public String toString() {
		return this.getNome();
	}
	
}
