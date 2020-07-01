package epidemic.model.DAO;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import epidemic.model.*;

/**
 * Implementazione dei metodi di comunicazione con il database
 * per oggetti di tipo Utente
 * 
 * @author Cristiano Di Bari
 * @author Matteo Cavaliere
 * @author Enrico Lonardi
 *
 */
public class UtenteDAO implements DAO<Utente>{
	private static UtenteDAO istance;
	private Properties queries;
	
	/**
	 * Crea un oggetto UtenteDAO importando le query specificate nel
	 * file utentiQueries.properties
	 * 
	 * @throws IOException
	 */
	private UtenteDAO() throws IOException {
		InputStream queryFile = new FileInputStream("queries/utentiQueries.properties");
		queries = new Properties();
		queries.load(queryFile);
	}
	
	/**
	 * Utilizza il pattern Singleton per assicurarsi che venga creato
	 * un solo UtenteDAO per interagire con il database
	 * 
	 * @return l'oggetto UtenteDAO
	 * @throws IOException
	 */
	public static UtenteDAO getIstance() throws IOException {
		if(istance == null)
			istance = new UtenteDAO();
		return istance;
	}
    
	
	/**
	 * Restituisce un oggetto utente cercandolo nel 
	 * database in base al suo username
	 * 
	 * @param username username da cercare
	 * @return l'oggetto utente trovato
	 */
    public Utente getUsername(String username) {
		Utente utente = null;
		Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet result = null;
        
        try {
        	connection = MySqlDAOFactory.createConnection();
            preparedStatement = connection.prepareStatement(queries.getProperty("username_query"));
            preparedStatement.setString(1, username);
            preparedStatement.execute();
            result = preparedStatement.getResultSet();
            
            if (result.next() && result != null) 
            	utente = getItemFromRS(result);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                result.close();
            } catch (Exception rse) {
                rse.printStackTrace();
            }
            try {
                preparedStatement.close();
            } catch (Exception sse) {
                sse.printStackTrace();
            }
        }
 
        return utente;
    }
    

	@Override
	public List<Utente> getAll() {
		List<Utente> users = new ArrayList<Utente>();

		Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet result = null;
        
        try {
        	connection = MySqlDAOFactory.createConnection();
            preparedStatement = connection.prepareStatement(queries.getProperty("read_all_query"));
            preparedStatement.execute();
            result = preparedStatement.getResultSet();
 
            while (result.next())
            	users.add(getItemFromRS(result));
            
            
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                result.close();
            } catch (Exception rse) {
                rse.printStackTrace();
            }
            try {
                preparedStatement.close();
            } catch (Exception sse) {
                sse.printStackTrace();
            }
        }
        
        return users;
	}

	@Override
	public Utente get(int id) {
		Utente utente = null;
		Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet result = null;
        
        try {
        	connection = MySqlDAOFactory.createConnection();
            preparedStatement = connection.prepareStatement(queries.getProperty("read_query"));
            preparedStatement.setInt(1, id);
            preparedStatement.execute();
            result = preparedStatement.getResultSet();
 
            if (result.next() && result != null) 
            	utente = getItemFromRS(result);
            
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                result.close();
            } catch (Exception rse) {
                rse.printStackTrace();
            }
            try {
                preparedStatement.close();
            } catch (Exception sse) {
                sse.printStackTrace();
            }
        }
 
        return utente;
	}

	@Override
	public int create(Utente utente) {
		int success = -1;
		Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet result = null;
        
        try {
            connection = MySqlDAOFactory.createConnection();
            preparedStatement = connection.prepareStatement(queries.getProperty("create_query"), Statement.RETURN_GENERATED_KEYS);
            setPreparedStatementFromItem(preparedStatement, utente);
            preparedStatement.execute();
            result = preparedStatement.getGeneratedKeys();
 
            if (result.next() && result != null) 
                success = result.getInt(1);
            
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                result.close();
            } catch (Exception rse) {
                rse.printStackTrace();
            }
            try {
                preparedStatement.close();
            } catch (Exception sse) {
                sse.printStackTrace();
            }
        }
 
        return success;
	}

	@Override
	public boolean update(Utente utente) {
		boolean success = false;
		Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
        	connection = MySqlDAOFactory.createConnection();
            preparedStatement = connection.prepareStatement(queries.getProperty("update_query"));
            setPreparedStatementFromItem(preparedStatement, utente);
            preparedStatement.setInt(6, utente.getId());
            preparedStatement.execute();
            success = true;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                preparedStatement.close();
            } catch (Exception sse) {
                sse.printStackTrace();
            }
        }
        return success;
	}

	@Override
	public boolean delete(Utente utente) {
		boolean success = false;
		Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
        	connection = MySqlDAOFactory.createConnection();
            preparedStatement = connection.prepareStatement(queries.getProperty("delete_query"));
            preparedStatement.setInt(1, utente.getId());
            preparedStatement.execute();
            success = true;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                preparedStatement.close();
            } catch (Exception sse) {
                sse.printStackTrace();
            }
        }
        return success;
	}

	@Override
	public Utente getItemFromRS(ResultSet result) throws SQLException {
		Utente utente;
        Ruolo ruolo = Ruolo.values()[result.getInt("ruolo")];
        switch(ruolo) {
           	case ADMIN: 
           		utente = new Admin(result.getString(2), result.getString(3), result.getString(4), result.getString(5));
           		break;
           		
           	case ANALISTA:
           		utente = new Analista(result.getString(2), result.getString(3), result.getString(4), result.getString(5));
           		break;
           		
           	case AUTORIZZATO: 
           		utente = new Autorizzato(result.getString(2), result.getString(3), result.getString(4), result.getString(5));
           		break;
           		
           	case CONTRATTO:
           		utente = new Contratto(result.getString(2), result.getString(3), result.getString(4), result.getString(5));
           		break;
           		
           	default:
           		return null;
        }
        
        utente.setId(result.getInt(1));
        return utente;
	}

	@Override
	public void setPreparedStatementFromItem(PreparedStatement preparedStatement, Utente utente) throws SQLException {
        preparedStatement.setString(1, utente.getNome());
        preparedStatement.setString(2, utente.getCognome());
        preparedStatement.setString(3, utente.getUsername());
        preparedStatement.setString(4, utente.getPassword());
        preparedStatement.setInt(5, utente.getRuolo().ordinal());
	}

}
