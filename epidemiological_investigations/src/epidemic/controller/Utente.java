package epidemic.controller;

public class Utente {
	
    private String nome;
    private String cognome;
    private String username;
    private String password;
    private Ruolo ruolo;
    
    public Utente(String nome, String cognome, String username, String password, Ruolo ruolo) {
        this.nome = nome;
        this.cognome = cognome;
        this.username = username;
        this.password = password;
        this.ruolo = ruolo;
    }

    public String getNome() {
        return nome;
    }

    public String getCognome() {
        return cognome;
    }
    
    public String getUsername() {
        return username;
    }
    
    public String getPassword() {
        return password;
    }
    
    public Ruolo getRuolo() {
    	return this.ruolo;
    }
    
    public void setRuolo(Ruolo ruolo) {
    	this.ruolo = ruolo;
    }
    
    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public void getPassword(String password) {
        this.password = password;
    }
        
}
