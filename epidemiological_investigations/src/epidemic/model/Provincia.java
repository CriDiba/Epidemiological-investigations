package epidemic.model;

import java.util.List;

/**
 * Rappresenta una Provincia italiana, la provincia ha uno specifico capoluogo 
 * ed appartiene ad una determinata regione
 * 
 * @author Cristiano Di Bari
 * @author Matteo Cavaliere
 * @author Enrico Lonardi
 *
 */
public class Provincia extends Localita {
	private String capoluogo;
	private final Regione regioneAppartenenza;
	private List<SegnalazioneDecessi> segnalazioniDecessi;

	/**
	 * Crea una Provincia
	 * 
	 * @param nome nome della privincia
	 * @param superficie superficie in km^2
	 * @param capoluogo nome del capoluogo di provincia
	 * @param regioneAppartenenza regione di appartenenza
	 */
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
	
	public void setCapoluogo(String capoluogo) {
		this.capoluogo = capoluogo;
	}
	
	public Regione getRegioneAppartenenza() {
		return regioneAppartenenza;
	}	
	
	@Override
	public boolean equals(Object obj) {
		return (obj instanceof Provincia) && ((Provincia)obj).getId() == this.getId();
	}
	
	@Override
	public String toString() {
		return getNome();
	}
	
}
