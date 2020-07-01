package epidemic.model.DAO;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;

/**
 * Rappresenta una factory usata per generare per oggetti di tipo DAO 
 * che interagiscono con un database MySql
 * 
 * @author Cristiano Di Bari
 * @author Matteo Cavaliere
 * @author Enrico Lonardi
 *
 */
public class MySqlDAOFactory {
	private static Connection connection = null;
	
    /**
     * Metodo per creare una connessione sul DB MySQL
     * 
     * @return l'oggetto Connection.
     * @throws SQLException 
     */
    public static Connection createConnection() throws SQLException {
    	
    	if(connection == null || connection.isClosed()) {
	        try {
	            Properties config = new Properties();
	        	config.load(new FileInputStream("config.properties"));
	        	
	            Class.forName(config.getProperty("jdbcDriver"));
	            connection = DriverManager.getConnection(config.getProperty("jdbcUrl"), config.getProperty("jdbcUsername"), config.getProperty("jdbcPassword"));
	        } catch (SQLException e) {
	            e.printStackTrace();
	        } catch (ClassNotFoundException | IOException e) {
	        	e.printStackTrace();
	        }
    	}
    	
        return connection;
    }
    
    
    /**
     * Crea un DAO per oggetti di tipo Utente
     * 
     * @return l'oggetto UtenteDAO
     * @throws IOException
     */
	public UtenteDAO getUtenteDAO() throws IOException {
		return UtenteDAO.getIstance();
	}
	
	/**
	 * Crea un DAO per oggetti di tipo Regione
	 * 
	 * @return l'oggetto RegioneDAO
	 * @throws IOException
	 */
	public RegioneDAO getRegioneDAO() throws IOException {
		return RegioneDAO.getIstance();
	}

	/**
	 * Crea un DAO per oggetti di tipo Provincia
	 * 
	 * @return l'oggetto ProvinciaDAO
	 * @throws IOException
	 */
	public ProvinciaDAO getProvinciaDAO() throws IOException {
		return ProvinciaDAO.getIstance();
	}
	
	/**
	 * Crea un DAO per oggetti di tipo Comune
	 * 
	 * @return l'oggetto ComuneDAO
	 * @throws IOException
	 */
	public ComuneDAO getComuneDAO() throws IOException {
		return ComuneDAO.getIstance();
	}

	/**
	 * Crea un DAO per oggetti di tipo SegnalazioneContagi
	 * 
	 * @return l'oggetto SegnalazioneContagiDAO
	 * @throws IOException
	 */
	public SegnalazioneContagiDAO getSegnalazioneContagiDAO() throws IOException {
		return SegnalazioneContagiDAO.getIstance();
	}

	/**
	 * Crea un DAO per oggetti di tipo Contagio
	 * 
	 * @return l'oggetto ContagioDAO
	 * @throws IOException
	 */
	public ContagioDAO getContagioDAO() throws IOException {
		return ContagioDAO.getIstance();
	}
	
	/**
	 * Crea un DAO per oggetti di tipo SegnalazioneDecessi
	 * 
	 * @return l'oggetto SegnalazioneDecessiDAO
	 * @throws IOException
	 */
	public SegnalazioneDecessiDAO getSegnalazioneDecessiDAO() throws IOException {
		return SegnalazioneDecessiDAO.getIstance();
	}

	/**
	 * Crea un DAO per oggetti di tipo Decesso
	 * 
	 * @return l'oggetto DecessoDAO
	 * @throws IOException
	 */
	public DecessoDAO getDecessoDAO() throws IOException {
		return DecessoDAO.getIstance();
	}

}
