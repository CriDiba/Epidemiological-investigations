package epidemic.model;

public abstract class Utente {
	
	private int id;
    private String nome;
    private String cognome;
    private String username;
    private String password;
    private Ruolo ruolo;
    
    public Utente(String nome, String cognome, String username, String password) {
        this.nome = nome;
        this.cognome = cognome;
        this.username = username;
        this.password = password;
    }
    
    public Utente(int id, String nome, String cognome, String username, String password) {
    	this.id = id;
        this.nome = nome;
        this.cognome = cognome;
        this.username = username;
        this.password = password;
    }

    public int getId() {
        return id;
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
    
    public void setId(int id) {
    	this.id = id;
    }
    
    public void setRuolo(Ruolo ruolo) {
    	this.ruolo = ruolo;
    }
        
}