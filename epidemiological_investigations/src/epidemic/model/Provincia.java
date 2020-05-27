package epidemic.model;

import java.util.List;

public class Provincia extends Localita {
	private String capoluogo;
	private final Regione regioneAppartenenza;
	private List<SegnalazioneContagi> segnalazioniDecessi;


	public Provincia(String nome, double superficie, String capoluogo, Regione regioneAppartenenza) {
		super(nome, superficie);
		if(!isValid(capoluogo))
			throw new IllegalArgumentException();
		this.capoluogo = capoluogo;
		this.regioneAppartenenza = regioneAppartenenza;
	}
	
	public List<SegnalazioneContagi> getSegnalazioniDecessi() {
		return segnalazioniDecessi;
	}

	public void setSegnalazioniDecessi(List<SegnalazioneContagi> segnalazioniDecessi) {
		this.segnalazioniDecessi = segnalazioniDecessi;
	}

	public String getCapoluogo() {
		return capoluogo;
	}
	
	public Regione getRegioneAppartenenza() {
		return regioneAppartenenza;
	}	
	
}
