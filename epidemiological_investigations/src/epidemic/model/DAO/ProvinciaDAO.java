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

import epidemic.model.Provincia;
import epidemic.model.Regione;

/**
 * Implementazione dei metodi di comunicazione con il database
 * per oggetti di tipo Provincia
 * 
 * @author Cristiano Di Bari
 * @author Matteo Cavaliere
 * @author Enrico Lonardi
 *
 */
public class ProvinciaDAO implements DAO<Provincia>{
	
	private static ProvinciaDAO istance;
	private Properties queries;
	
	/**
	 * Crea un oggetto ProvinciaDAO importando le query specificate nel
	 * file provinceQueries.properties
	 * 
	 * @throws IOException
	 */
	private ProvinciaDAO() throws IOException {
		InputStream queryFile = new FileInputStream("queries/provinceQueries.properties");
		queries = new Properties();
		queries.load(queryFile);
	}
	
	/**
	 * Utilizza il pattern Singleton per assicurarsi che venga creato
	 * un solo ProvinciaDAO per interagire con il database
	 * 
	 * @return l'oggetto ProvinciaDAO
	 * @throws IOException
	 */
	public static ProvinciaDAO getIstance() throws IOException {
		if(istance == null)
			istance = new ProvinciaDAO();
		return istance;
	}
	
	@Override
	public List<Provincia> getAll() {
		List<Provincia> provinces = new ArrayList<>();
		
		Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet result = null;
        
        try {
        	connection = MySqlDAOFactory.createConnection();
            preparedStatement = connection.prepareStatement(queries.getProperty("read_all_query"));
            preparedStatement.execute();
            result = preparedStatement.getResultSet();
           
            while(result.next())
	           	provinces.add(getItemFromRS(result));
           
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
        
        return provinces;
	}

	@Override
	public Provincia get(int id) {
		Provincia provincia = null;
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
            	provincia = getItemFromRS(result);
            
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
 
        return provincia;
	}

	@Override
	public int create(Provincia provincia) {
		int success = -1;
		Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet result = null;
        
        try {
            connection = MySqlDAOFactory.createConnection();
            preparedStatement = connection.prepareStatement(queries.getProperty("create_query"), Statement.RETURN_GENERATED_KEYS);
            setPreparedStatementFromItem(preparedStatement, provincia);
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
	public boolean update(Provincia provincia) {
		boolean success = false;
		Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
        	connection = MySqlDAOFactory.createConnection();
            preparedStatement = connection.prepareStatement(queries.getProperty("update_query"));
            setPreparedStatementFromItem(preparedStatement, provincia);
            preparedStatement.setInt(5, provincia.getId());
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
	public boolean delete(Provincia provincia) {
		boolean success = false;
		Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
        	connection = MySqlDAOFactory.createConnection();
            preparedStatement = connection.prepareStatement(queries.getProperty("delete_query"));
            preparedStatement.setInt(1, provincia.getId());
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
	public void setPreparedStatementFromItem(PreparedStatement preparedStatement, Provincia provincia) throws SQLException {
		preparedStatement.setString(1, provincia.getNome());
        preparedStatement.setDouble(2, provincia.getSuperficie());
        preparedStatement.setString(3, provincia.getCapoluogo());
        preparedStatement.setInt(4, provincia.getRegioneAppartenenza().getId());
	}
	
	@Override
	public Provincia getItemFromRS(ResultSet result) throws SQLException {
		MySqlDAOFactory database = new MySqlDAOFactory();
		Regione regione = null;
		try {
			regione = database.getRegioneDAO().get(result.getInt("id_regione"));
		} catch(IOException e) {
			e.printStackTrace();
			return null;
		}
		
		Provincia provincia =  new Provincia(result.getString("nome"), result.getDouble("superficie"), result.getString("capoluogo"), regione);
		provincia.setId(result.getInt("id"));
		return provincia;
	}
}
