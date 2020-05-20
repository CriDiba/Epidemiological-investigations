package epidemic.model.DAO;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;

public class MySqlDAOFactory {
	
    /**
     * Metodo per creare una connessione sul DB MySQL
     * 
     * @return l'oggetto Connection.
     */
    public static Connection createConnection() {
        Connection connection = null;
                
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
        
        return connection;
    }
    
    

	public UtenteDAO getUtenteDAO() {
		return new UtenteDAO();
	}
	
	//Posso restituire VARI DAO 

}
