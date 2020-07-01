package epidemic.model;

/**
 * Rappresenta un utente a Contratto, questo ruolo può inserire settimanalmente
 * i dati relativi ai casi di contagio avvenuti nei comuni dei quali è reponsabile.
 * 
 * @author Cristiano Di Bari
 * @author Matteo Cavaliere
 * @author Enrico Lonardi
 *
 */
public class Contratto extends Utente {

	/**
	 * Crea un utente a Contratto
	 * 
	 * @param nome
	 * @param cognome
	 * @param username
	 * @param password
	 */
	public Contratto(String nome, String cognome, String username, String password) {
		super(nome, cognome, username, password);
		this.setRuolo(Ruolo.CONTRATTO);
	}
	
}
