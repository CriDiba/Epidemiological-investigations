package epidemic.model;

/**
 * Rappresenta i dati delle persone decedute per una specifica causa di morte
 * 
 * @author Cristiano Di Bari
 * @author Matteo Cavaliere
 * @author Enrico Lonardi
 *
 */
public class Decesso {
	
	private int id;
	private CausaDecesso causa;
	private int numero;
	private SegnalazioneDecessi segnalazione;
	
	/**
	 * Crea un descrittore di decessi
	 * 
	 * @param causa causa del decesso
	 * @param numero numero persone decedute
	 */
	public Decesso(CausaDecesso causa, int numero) {
		if(numero < 0)
			throw new IllegalArgumentException();
		
		this.numero = numero;
		this.causa = causa;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public SegnalazioneDecessi getSegnalazione() {
		return segnalazione;
	}
	
	public void setSegnalazione(SegnalazioneDecessi segnalazione) {
		this.segnalazione = segnalazione;
	}
	
	public CausaDecesso getCausa() {
		return causa;
	}
	
	public void setCausa(CausaDecesso causa) {
		this.causa = causa;
	}
	
	public int getNumero() {
		return numero;
	}
	
	public void setNumero(int numero) {
		if(numero >= 0)
			this.numero = numero;
	}
	
}
