package epidemic.model;

public class Provincia extends Localita {
	private final Comune capoluogo;
	private final Regione regioneAppartenenza;
	
	public Provincia(String nome, float superficie, Comune capoluogo, Regione regioneAppartenenza) {
		super(nome, superficie);
		this.capoluogo = capoluogo;
		this.regioneAppartenenza = regioneAppartenenza;
	}

	public Comune getCapoluogo() {
		return capoluogo;
	}
	
	public Regione getRegioneAppartenenza() {
		return regioneAppartenenza;
	}	
	
}
