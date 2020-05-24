package epidemic.model;

public class Regione extends Localita{
	private Comune capoluogo;
	
	public Regione(String nome, float superficie, Comune capoluogo) {
		super(nome, superficie);
		this.capoluogo = capoluogo;
	}
	
	public Comune getCapoluogo() {
		return capoluogo;
	}

}
