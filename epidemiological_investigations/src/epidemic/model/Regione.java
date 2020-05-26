package epidemic.model;

public class Regione extends Localita{
	private String capoluogo;
	
	public Regione(String nome, double superficie, String capoluogo) {
		super(nome, superficie);
		if(!isValid(capoluogo))
			throw new IllegalArgumentException();
		this.capoluogo = capoluogo;
	}
	
	public Regione(int id, String nome, double superficie, String capoluogo) {
		this(nome, superficie, capoluogo);
		setId(id);
	}
	
	public String getCapoluogo() {
		return capoluogo;
	}

}
