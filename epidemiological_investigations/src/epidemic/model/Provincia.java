package epidemic.model;

import java.util.List;

public class Provincia extends Localita {
	private final Comune capoluogo;
	private final Regione regioneAppartenenza;
	private List<Segnalazione> segnalazioniDecessi;


	public Provincia(String nome, float superficie, Comune capoluogo, Regione regioneAppartenenza) {
		super(nome, superficie);
		this.capoluogo = capoluogo;
		this.regioneAppartenenza = regioneAppartenenza;
	}
	
	public List<Segnalazione> getSegnalazioniDecessi() {
		return segnalazioniDecessi;
	}

	public void setSegnalazioniDecessi(List<Segnalazione> segnalazioniDecessi) {
		this.segnalazioniDecessi = segnalazioniDecessi;
	}

	public Comune getCapoluogo() {
		return capoluogo;
	}
	
	public Regione getRegioneAppartenenza() {
		return regioneAppartenenza;
	}	
	
}
