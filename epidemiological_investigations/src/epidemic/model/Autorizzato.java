package epidemic.model;

public class Autorizzato extends Utente {

	public Autorizzato(String nome, String cognome, String username, String password) {
		super(nome, cognome, username, password);
		this.setRuolo(Ruolo.AUTORIZZATO);
	}
	
	public Autorizzato(int id, String nome, String cognome, String username, String password) {
		super(id, nome, cognome, username, password);
		this.setRuolo(Ruolo.AUTORIZZATO);
	}


}
