package epidemic.model;

/**
 * Rappresenta i dati di contagio delle persone ricoverate e
 * di quelle in cura presso il medico di base per una specifica malattia contagiosa
 * 
 * @author Cristiano Di Bari
 * @author Matteo Cavaliere
 * @author Enrico Lonardi
 *
 */
public class Contagio {
	private int id;
	private MalattiaContagiosa malattia;
	private int personeRicoverate;
	private int personeInCura;
	private SegnalazioneContagi segnalazione;
	
	/**
	 * Crea un descittore di contagio 
	 * 
	 * @param malattia tipo di malattia contagiosa
	 * @param personeRicoverate numero di persone ricoverate
	 * @param personeInCura numero di persone in cura presso il medico
	 */
	public Contagio(MalattiaContagiosa malattia, int personeRicoverate, int personeInCura) {
		if(personeRicoverate < 0 || personeInCura < 0)
			throw new IllegalArgumentException();
		this.malattia = malattia;
		this.personeRicoverate = personeRicoverate;
		this.personeInCura = personeInCura;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public SegnalazioneContagi getSegnalazione() {
		return segnalazione;
	}
	
	public void setSegnalazione(SegnalazioneContagi segnalazione) {
		this.segnalazione = segnalazione;
	}
	
	public MalattiaContagiosa getMalattia() {
		return malattia;
	}
	
	public void setMalattia(MalattiaContagiosa malattia) {
		this.malattia = malattia;
	}
	
	public int getPersoneRicoverate() {
		return personeRicoverate;
	}
	
	public void setPersoneRicoverate(int personeRicoverate) {
		if(personeRicoverate >= 0)
			this.personeRicoverate = personeRicoverate;
	}
	
	public int getPersoneInCura() {
		return personeInCura;
	}
	
	public void setPersoneInCura(int personeInCura) {
		if(personeInCura >= 0)
			this.personeInCura = personeInCura;
	}
	
}
