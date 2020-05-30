package epidemic.model.DAO;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;


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
	        	config.load(new FileInputStream("config"));
	        	
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
    
    

	public UtenteDAO getUtenteDAO() throws IOException {
		return UtenteDAO.getIstance();
	}
	
	public RegioneDAO getRegioneDAO() throws IOException {
		return RegioneDAO.getIstance();
	}

	public ProvinciaDAO getProvinciaDAO() throws IOException {
		return ProvinciaDAO.getIstance();
	}
	
	public ComuneDAO getComuneDAO() throws IOException {
		return ComuneDAO.getIstance();
	}

	public SegnalazioneContagiDAO getSegnalazioneContagiDAO() throws IOException {
		return SegnalazioneContagiDAO.getIstance();
	}

	public ContagioDAO getContagioDAO() throws IOException {
		return ContagioDAO.getIstance();
	}

}
