package epidemic.model;

public class Analista extends Utente {

	public Analista(String nome, String cognome, String username, String password) {
		super(nome, cognome, username, password);
		this.setRuolo(Ruolo.ANALISTA);
	}
	
	public Analista(int id, String nome, String cognome, String username, String password) {
		super(id, nome, cognome, username, password);
		this.setRuolo(Ruolo.ANALISTA);
	}

}
