package epidemic.model;

/**
 * Rappresenta un utente Admin, questo ruolo ha la possibilità di 
 * creare, modificare ed eliminare gli account utente del sistema 
 * ed assegnare loro un ruolo
 * 
 * @author Cristiano Di Bari
 * @author Matteo Cavaliere
 * @author Enrico Lonardi
 *
 */
public class Admin extends Utente {

	/**
	 * Crea l'utente Admin
	 * 
	 * @param nome
	 * @param cognome
	 * @param username
	 * @param password
	 */
	public Admin(String nome, String cognome, String username, String password) {
		super(nome, cognome, username, password);
		this.setRuolo(Ruolo.ADMIN);
	}

}
