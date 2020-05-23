package epidemic.model;

public class Provincia extends Localita {
	private final Comune capoluogo;
	
	public Provincia(String nome, float superficie, Comune capoluogo) {
		super(nome, superficie);
		this.capoluogo = capoluogo;
	}

	public Comune getCapoluogo() {
		return capoluogo;
	}	
	
}
