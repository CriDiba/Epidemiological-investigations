package epidemic.model;

public class Admin extends Utente {

	public Admin(String nome, String cognome, String username, String password) {
		super(nome, cognome, username, password);
		this.setRuolo(Ruolo.ADMIN);
	}

	public Admin(int id, String nome, String cognome, String username, String password) {
		super(id, nome, cognome, username, password);
		this.setRuolo(Ruolo.ADMIN);
	}

}
