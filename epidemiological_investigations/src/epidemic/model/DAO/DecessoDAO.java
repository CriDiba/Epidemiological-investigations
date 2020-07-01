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

import epidemic.model.CausaDecesso;
import epidemic.model.Decesso;

/**
 * Implementazione dei metodi di comunicazione con il database
 * per oggetti di tipo Decesso
 * 
 * @author Cristiano Di Bari
 * @author Matteo Cavaliere
 * @author Enrico Lonardi
 *
 */
public class DecessoDAO implements DAO<Decesso> {

	private static DecessoDAO istance;
	private Properties queries;
	
	/**
	 * Crea un oggetto DecessoDAO importando le query specificate nel
	 * file decessoQueries.properties
	 * 
	 * @throws IOException
	 */
	private DecessoDAO() throws IOException {
		InputStream queryFile = new FileInputStream("queries/decessoQueries.properties");
		queries = new Properties();
		queries.load(queryFile);
	}
	
	/**
	 * Utilizza il pattern Singleton per assicurarsi che venga creato
	 * un solo DecessoDAO per interagire con il database
	 * 
	 * @return l'oggetto DecessoDAO
	 * @throws IOException
	 */
	public static DecessoDAO getIstance() throws IOException {
		if(istance == null)
			istance = new DecessoDAO();
		return istance;
	}
	
	/**
	 * Restituisce una lista di decessi nel database che appartengono ad
	 * una specifica segnalazione
	 * 
	 * @param id_segnalazione segnalazione da cercare
	 * @return lista di decessi di una segnalazione
	 */
	public List<Decesso> getAllForSegnalazione(int id_segnalazione) {
		List<Decesso> decessi = new ArrayList<>();
		Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet result = null;
        
        try {
        	connection = MySqlDAOFactory.createConnection();
            preparedStatement = connection.prepareStatement(queries.getProperty("read_per_segnalazione_query"));
            preparedStatement.setInt(1, id_segnalazione);
            preparedStatement.execute();
            result = preparedStatement.getResultSet();
            
            while(result.next())
            	decessi.add(getItemFromRS(result));
            
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
 
        return decessi;
	}

	
	@Override
	public List<Decesso> getAll() {
		List<Decesso> decessi = new ArrayList<>();
		Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet result = null;
        
        try {
        	connection = MySqlDAOFactory.createConnection();
            preparedStatement = connection.prepareStatement(queries.getProperty("read_all_query"));
            preparedStatement.execute();
            result = preparedStatement.getResultSet();
           
            while(result.next())
            	decessi.add(getItemFromRS(result));
           
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
        
        return decessi;
	}

	
	@Override
	public Decesso get(int id) {
		Decesso decesso = null;
		Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet result = null;
        
        try {
        	connection = MySqlDAOFactory.createConnection();
            preparedStatement = connection.prepareStatement(queries.getProperty("read_query"));
            preparedStatement.setInt(1, id);
            preparedStatement.execute();
            result = preparedStatement.getResultSet();
            
            if(result != null && result.next())
            	decesso = getItemFromRS(result);
            
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
 
        return decesso;
	}
	

	@Override
	public int create(Decesso decesso) {
		int success = -1;
		Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet result = null;
        
        try {
            connection = MySqlDAOFactory.createConnection();
            preparedStatement = connection.prepareStatement(queries.getProperty("create_query"), Statement.RETURN_GENERATED_KEYS);
            setPreparedStatementFromItem(preparedStatement, decesso);
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
	public boolean update(Decesso decesso) {
		boolean success = false;
		Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
        	connection = MySqlDAOFactory.createConnection();
            preparedStatement = connection.prepareStatement(queries.getProperty("update_query"));
            setPreparedStatementFromItem(preparedStatement, decesso);
            preparedStatement.setInt(4, decesso.getId());
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
	public boolean delete(Decesso decesso) {
		boolean success = false;
		Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
        	connection = MySqlDAOFactory.createConnection();
            preparedStatement = connection.prepareStatement(queries.getProperty("delete_query"));
            preparedStatement.setInt(1, decesso.getId());
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
	public Decesso getItemFromRS(ResultSet result) throws SQLException {
		Decesso decesso = new Decesso(CausaDecesso.values()[result.getInt("causa")], result.getInt("numero"));
		decesso.setId(result.getInt("id"));
		return decesso;
	}

	@Override
	public void setPreparedStatementFromItem(PreparedStatement preparedStatement, Decesso decesso) throws SQLException {
		preparedStatement.setInt(1, decesso.getCausa().ordinal());
		preparedStatement.setInt(2, decesso.getNumero());
		preparedStatement.setInt(3, decesso.getSegnalazione().getId());
	}

}
