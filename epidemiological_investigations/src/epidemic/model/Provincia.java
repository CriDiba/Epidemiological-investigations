package epidemic.model;

import java.util.List;

public class Provincia extends Localita {
	private String capoluogo;
	private final Regione regioneAppartenenza;
	private List<SegnalazioneDecessi> segnalazioniDecessi;


	public Provincia(String nome, double superficie, String capoluogo, Regione regioneAppartenenza) {
		super(nome, superficie);
		if(!isValid(capoluogo))
			throw new IllegalArgumentException();
		this.capoluogo = capoluogo;
		this.regioneAppartenenza = regioneAppartenenza;
	}
	
	public List<SegnalazioneDecessi> getSegnalazioniDecessi() {
		return segnalazioniDecessi;
	}

	public void setSegnalazioniDecessi(List<SegnalazioneDecessi> segnalazioniDecessi) {
		this.segnalazioniDecessi = segnalazioniDecessi;
	}

	public String getCapoluogo() {
		return capoluogo;
	}
	
	public Regione getRegioneAppartenenza() {
		return regioneAppartenenza;
	}	
	
}
