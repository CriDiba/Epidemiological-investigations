package epidemic.model;

/**
 * Rappresenta un Utente dell'applicazione, ogni utente ha un determinato ruolo
 * che ne specifica i permessi e le modalità d'uso del software
 * 
 * @author Cristiano Di Bari
 * @author Matteo Cavaliere
 * @author Enrico Lonardi
 *
 */
public abstract class Utente {
	
	private int id;
    private String nome;
    private String cognome;
    private String username;
    private String password;
    private Ruolo ruolo;
    
    /**
     * Crea un utente
     * 
     * @param nome nome dell'utente
     * @param cognome cognome dell'utente
     * @param username username associato all'utente per effettuare l'accesso
     * @param password password associata all'utente per effettuare l'accesso
     */
    public Utente(String nome, String cognome, String username, String password) {
        this.nome = nome;
        this.cognome = cognome;
        this.username = username;
        this.password = password;
    }
    

    public int getId() {
        return id;
    }
    
    public void setId(int id) {
    	this.id = id;
    }

    public String getNome() {
        return nome;
    }
    
    public void setNome(String nome) {
        this.nome =  nome;
    }

    public String getCognome() {
        return cognome;
    }
    
	public void setCognome(String cognome) {
		this.cognome = cognome;
	}
    
    public String getUsername() {
        return username;
    }
    
	public void setUsername(String username) {
		this.username = username;
	}
    
    public String getPassword() {
        return password;
    }
    
	public void setPassword(String password) {
		this.password = password;
	}
           
    public Ruolo getRuolo() {
    	return this.ruolo;
    }
    
    public void setRuolo(Ruolo ruolo) {
    	this.ruolo = ruolo;
    }
    
    @Override
    public String toString() {
    	return this.nome + " " + this.cognome;
    }
 
}
