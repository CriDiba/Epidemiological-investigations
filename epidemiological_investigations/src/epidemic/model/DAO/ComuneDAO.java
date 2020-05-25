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

import epidemic.model.Comune;
import epidemic.model.Provincia;
import epidemic.model.Territorio;

public class ComuneDAO implements DAO<Comune>{
	
	private static ComuneDAO istance;
	private Properties queries;
	
	private ComuneDAO() throws IOException {
		InputStream queryFile = null;
		queries = new Properties();
		queryFile = getClass().getResourceAsStream("/queries/comuniQueries.properties");
		queries.load(queryFile);
	}
	
	public static ComuneDAO getIstance() throws IOException {
		if(istance == null)
			istance = new ComuneDAO();
		return istance;
	}
	
	@Override
	public List<Comune> getAll() {
		List<Comune> municipalities = new ArrayList<>();
		
		Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet result = null;
        
        try {
        	connection = MySqlDAOFactory.createConnection();
            preparedStatement = connection.prepareStatement(queries.getProperty("read_all_query"));
            preparedStatement.execute();
            result = preparedStatement.getResultSet();
           
            while(result.next())
            	municipalities.add(getComuneFromRS(result));
           
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
        
        return municipalities;
	}

	@Override
	public Comune get(int id) {
		Comune comune = null;
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
            	comune = getComuneFromRS(result);
            
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
 
        return comune;
	}

	@Override
	public int create(Comune comune) {
		Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet result = null;
        
        try {
            connection = MySqlDAOFactory.createConnection();
            preparedStatement = connection.prepareStatement(queries.getProperty("create_query"), Statement.RETURN_GENERATED_KEYS);
            setPreparedStatementFromComune(preparedStatement, comune);
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
	public boolean update(Comune comune) {
		Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
        	connection = MySqlDAOFactory.createConnection();
            preparedStatement = connection.prepareStatement(queries.getProperty("update_query"));
            setPreparedStatementFromComune(preparedStatement, comune);
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
	public boolean delete(Comune comune) {
		Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
        	connection = MySqlDAOFactory.createConnection();
            preparedStatement = connection.prepareStatement(queries.getProperty("delete_query"));
            preparedStatement.setInt(1, comune.getId());
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
	
	private void setPreparedStatementFromComune(PreparedStatement preparedStatement, Comune comune) throws SQLException {
		preparedStatement.setString(1, comune.getNome());
        preparedStatement.setDouble(2, comune.getSuperficie());
        preparedStatement.setString(3, comune.getIstat());
		preparedStatement.setDate(4, comune.getDataIstituzione());
		preparedStatement.setInt(5, comune.getTerritorio().ordinal());
		preparedStatement.setBoolean(6, comune.getSulMare());
		preparedStatement.setString(7, comune.getProvinciaAppartenenza().getNome());
		
	}
	
	private Comune getComuneFromRS(ResultSet result) {
		new Comune(result.getString("nome"), result.getDouble("superficie"), result.getString("istat"),
				result.getDate("dataIstituzione"), Territorio.values()[result.getInt("territorio")], result.getBoolean("sulMare")/*, result.getint()??*/);
	}


}



