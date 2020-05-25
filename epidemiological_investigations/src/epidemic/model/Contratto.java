package epidemic.model;

import java.util.List;
import java.util.ArrayList;

public class Contratto extends Utente {
	private List<Comune> comuniAssegnati = new ArrayList<>();
	
	public Contratto(String nome, String cognome, String username, String password) {
		super(nome, cognome, username, password);
		this.setRuolo(Ruolo.CONTRATTO);
	}
	
	public Contratto(int id, String nome, String cognome, String username, String password) {
		super(id, nome, cognome, username, password);
		this.setRuolo(Ruolo.CONTRATTO);
	}
	
	public void setComuniAssegnati(Comune... comuniAssegnati) {
		for(Comune comune: comuniAssegnati) {
			this.comuniAssegnati.add(comune);
		}
	}
	
	public void setComuniAssegnati(ArrayList<Comune> comuniAssegnati) {
		this.comuniAssegnati = comuniAssegnati;
	}


}
