package epidemic.model;

/**
 * Rappresenta un utente Autorizzato dell'ente, questo ruolo può assegnare al
 * personale a contratto i comuni di riferimento, inserire le segnalazioni annuali
 * dei decessi e visionare i dati presenti nel sistema.
 * 
 * @author Cristiano Di Bari
 * @author Matteo Cavaliere
 * @author Enrico Lonardi
 *
 */
public class Autorizzato extends Utente {

	/**
	 * Crea un utente Autorizzato
	 * 
	 * @param nome
	 * @param cognome
	 * @param username
	 * @param password
	 */
	public Autorizzato(String nome, String cognome, String username, String password) {
		super(nome, cognome, username, password);
		this.setRuolo(Ruolo.AUTORIZZATO);
	}
	
}
