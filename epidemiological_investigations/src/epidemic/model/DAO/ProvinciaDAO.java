package epidemic.model.DAO;

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

public class ProvinciaDAO implements DAO<Provincia>{
	
	private static ProvinciaDAO istance;
	private Properties queries;
	
	private ProvinciaDAO() throws IOException {
		InputStream queryFile = null;
		queries = new Properties();
		queryFile = getClass().getResourceAsStream("/queries/provinceQueries.properties");
		queries.load(queryFile);
	}
	
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
	           	provinces.add(getProvinciaFromRS(result, connection));
           
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
            try {
                connection.close();
            } catch (Exception cse) {
                cse.printStackTrace();
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
            	provincia = getProvinciaFromRS(result, connection);
            
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
            try {
                connection.close();
            } catch (Exception cse) {
                cse.printStackTrace();
            }
        }
 
        return provincia;
	}

	@Override
	public int create(Provincia provincia) {
		Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet result = null;
        
        try {
            connection = MySqlDAOFactory.createConnection();
            preparedStatement = connection.prepareStatement(queries.getProperty("create_query"), Statement.RETURN_GENERATED_KEYS);
            setPreparedStatementFromProvincia(preparedStatement, provincia);
            preparedStatement.execute();
            result = preparedStatement.getGeneratedKeys();
            
            if (result.next() && result != null)
                return result.getInt(1);
            
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
            try {
                connection.close();
            } catch (Exception cse) {
                cse.printStackTrace();
            }
        }
 
        return -1;
	}


	@Override
	public boolean update(Provincia provincia) {
		Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
        	connection = MySqlDAOFactory.createConnection();
            preparedStatement = connection.prepareStatement(queries.getProperty("update_query"));
            setPreparedStatementFromProvincia(preparedStatement, provincia);
            preparedStatement.execute();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                preparedStatement.close();
            } catch (Exception sse) {
                sse.printStackTrace();
            }
            try {
                connection.close();
            } catch (Exception cse) {
                cse.printStackTrace();
            }
        }
        return false;
	}

	@Override
	public boolean delete(Provincia provincia) {
		Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
        	connection = MySqlDAOFactory.createConnection();
            preparedStatement = connection.prepareStatement(queries.getProperty("delete_query"));
            preparedStatement.setInt(1, provincia.getId());
            preparedStatement.execute();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                preparedStatement.close();
            } catch (Exception sse) {
                sse.printStackTrace();
            }
            try {
            	connection.close();
            } catch (Exception cse) {
                cse.printStackTrace();
            }
        }
		return false;
	}

	private void setPreparedStatementFromProvincia(PreparedStatement preparedStatement, Provincia provincia) throws SQLException {
		preparedStatement.setString(1, provincia.getNome());
        preparedStatement.setDouble(2, provincia.getSuperficie());
        preparedStatement.setString(3, provincia.getCapoluogo());
        preparedStatement.setString(4, provincia.getRegioneAppartenenza().getNome());
	}
	
	private Provincia getProvinciaFromRS(ResultSet result, Connection connection) throws SQLException {
		PreparedStatement getRegione = connection.prepareStatement(queries.getProperty("get_regione_query"));
		getRegione.setInt(1, result.getInt("id_regione"));
		getRegione.execute();
		ResultSet regione = getRegione.getResultSet();
		
		Provincia provincia =  new Provincia(result.getString("nome"), result.getDouble("superficie"), 
    			result.getString("capoluogo"), new Regione(regione.getString("nome"), regione.getDouble("superficie"),
    					regione.getString("capoluogo")));
		provincia.setId(result.getInt("id"));
		return provincia;
	}
}
