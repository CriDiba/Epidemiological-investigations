package epidemic.model;

public class Contratto extends Utente {

	public Contratto(String nome, String cognome, String username, String password) {
		super(nome, cognome, username, password);
		this.setRuolo(Ruolo.CONTRATTO);
	}
	
	public Contratto(int id, String nome, String cognome, String username, String password) {
		super(id, nome, cognome, username, password);
		this.setRuolo(Ruolo.CONTRATTO);
	}


}
