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
import epidemic.model.Regione;

/**
 * Implementazione dei metodi di comunicazione con il database
 * per oggetti di tipo Regione
 * 
 * @author Cristiano Di Bari
 * @author Matteo Cavaliere
 * @author Enrico Lonardi
 *
 */
public class RegioneDAO implements DAO<Regione> {

	private static RegioneDAO istance;
	private Properties queries;
	
	/**
	 * Crea un oggetto RegioneDAO importando le query specificate nel
	 * file regioniQueries.properties
	 * 
	 * @throws IOException
	 */
	private RegioneDAO() throws IOException {
		InputStream queryFile = new FileInputStream("queries/regioniQueries.properties");
		queries = new Properties();
		queries.load(queryFile);
	}
	
	/**
	 * Utilizza il pattern Singleton per assicurarsi che venga creato
	 * un solo RegioneDAO per interagire con il database
	 * 
	 * @return l'oggetto RegioneDAO
	 * @throws IOException
	 */
	public static RegioneDAO getIstance() throws IOException {
		if(istance == null)
			istance = new RegioneDAO();
		return istance;
	}
	
	@Override
	public List<Regione> getAll() {
		List<Regione> regions = new ArrayList<>();
		
		Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet result = null;
        
        try {
        	connection = MySqlDAOFactory.createConnection();
            preparedStatement = connection.prepareStatement(queries.getProperty("read_all_query"));
            preparedStatement.execute();
            result = preparedStatement.getResultSet();
           
            while(result.next())
	           	regions.add(getItemFromRS(result));
           
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
        
        return regions;
	}

	@Override
	public Regione get(int id) {
		Regione regione = null;
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
            	regione = getItemFromRS(result);
            
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
 
        return regione;
	}

	@Override
	public int create(Regione regione) {
		int success = -1;
		Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet result = null;
        
        try {
            connection = MySqlDAOFactory.createConnection();
            preparedStatement = connection.prepareStatement(queries.getProperty("create_query"), Statement.RETURN_GENERATED_KEYS);
            setPreparedStatementFromItem(preparedStatement, regione);           
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
	public boolean update(Regione regione) {
		boolean success = false;
		Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
        	connection = MySqlDAOFactory.createConnection();
            preparedStatement = connection.prepareStatement(queries.getProperty("update_query"));
            setPreparedStatementFromItem(preparedStatement, regione);
            preparedStatement.setInt(4, regione.getId());
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
	public boolean delete(Regione regione) {
		boolean success = false;
		Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
        	connection = MySqlDAOFactory.createConnection();
            preparedStatement = connection.prepareStatement(queries.getProperty("delete_query"));
            preparedStatement.setInt(1, regione.getId());
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
	public void setPreparedStatementFromItem(PreparedStatement preparedStatement, Regione regione) throws SQLException {
		 preparedStatement.setString(1, regione.getNome());
         preparedStatement.setDouble(2, regione.getSuperficie());
         preparedStatement.setString(3, regione.getCapoluogo());
	}
	
	@Override
	public Regione getItemFromRS(ResultSet result) throws SQLException {
		Regione regione = new Regione(result.getString("nome"), result.getDouble("superficie"), result.getString("capoluogo"));
		regione.setId(result.getInt("id"));
		return regione;
	}

}
