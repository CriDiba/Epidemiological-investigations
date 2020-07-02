package epidemic.model;

/**
 * Rappresenta un utente Analista, questo ruolo può analizzare i dati
 * presenti nel sistema ed ha la possibilità di generare grafici o esportare
 * dati in formati come XLS, CSV, XML...
 * 
 * @author Cristiano Di Bari
 * @author Matteo Cavaliere
 * @author Enrico Lonardi
 *
 */
public class Analista extends Utente {

	/**
	 * Crea un utente Analista
	 * 
	 * @param nome
	 * @param cognome
	 * @param username
	 * @param password
	 */
	public Analista(String nome, String cognome, String username, String password) {
		super(nome, cognome, username, password);
		this.setRuolo(Ruolo.ANALISTA);
	}
	
}
