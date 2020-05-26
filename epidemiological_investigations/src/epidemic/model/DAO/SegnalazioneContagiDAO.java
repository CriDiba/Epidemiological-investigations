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
import epidemic.model.Contagio;
import epidemic.model.MalattiaContagiosa;
import epidemic.model.SegnalazioneContagi;

public class SegnalazioneContagiDAO implements DAO<SegnalazioneContagi> {
	private static SegnalazioneContagiDAO istance;
	private Properties queries;
	
	private SegnalazioneContagiDAO() throws IOException {
		InputStream queryFile = null;
		queries = new Properties();
		queryFile = getClass().getResourceAsStream("/queries/segnalazioneContagiQueries.properties");
		queries.load(queryFile);
	}
	
	public static SegnalazioneContagiDAO getIstance() throws IOException {
		if(istance == null)
			istance = new SegnalazioneContagiDAO();
		return istance;
	}
	
	@Override
	public List<SegnalazioneContagi> getAll() {
		List<SegnalazioneContagi> segnContagi = new ArrayList<>();
		Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet result = null;
        
        try {
        	connection = MySqlDAOFactory.createConnection();
            preparedStatement = connection.prepareStatement(queries.getProperty("read_all_query"));
            preparedStatement.execute();
            result = preparedStatement.getResultSet();
           
            while(result.next())
            	segnContagi.add(getSegnalazioneFromRS(result, connection));
           
        } catch (SQLException | IOException e) {
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
        
        return segnContagi;
	}


	@Override
	public SegnalazioneContagi get(int id) {
		SegnalazioneContagi segnContagi = null;
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
            	segnContagi = getSegnalazioneFromRS(result, connection);
            
        } catch (SQLException | IOException e) {
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
 
        return segnContagi;
	}

	@Override
	public int create(SegnalazioneContagi segnContagi) {
		int success = -1;
		Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet result = null;
        
        try {
            connection = MySqlDAOFactory.createConnection();
            preparedStatement = connection.prepareStatement(queries.getProperty("create_query"), Statement.RETURN_GENERATED_KEYS);
            setPreparedStatementFromSegnalazione(preparedStatement, segnContagi);
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
            try {
                connection.close();
            } catch (Exception cse) {
                cse.printStackTrace();
            }
        }
 
        return success;
	}

	@Override
	public boolean update(SegnalazioneContagi segnContagi) {
		boolean success = false;
		Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
        	connection = MySqlDAOFactory.createConnection();
            preparedStatement = connection.prepareStatement(queries.getProperty("update_query"));
            setPreparedStatementFromSegnalazione(preparedStatement, segnContagi);
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
            try {
                connection.close();
            } catch (Exception cse) {
                cse.printStackTrace();
            }
        }
        return success;
	}

	@Override
	public boolean delete(SegnalazioneContagi segnContagi) {
		boolean success = false;
		Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
        	connection = MySqlDAOFactory.createConnection();
            preparedStatement = connection.prepareStatement(queries.getProperty("delete_query"));
            preparedStatement.setInt(1, segnContagi.getId());
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
            try {
            	connection.close();
            } catch (Exception cse) {
                cse.printStackTrace();
            }
        }
		return success;
	}
	
	private void setPreparedStatementFromSegnalazione(PreparedStatement preparedStatement, SegnalazioneContagi segnContagi) {
		// TODO Auto-generated method stub
		
	}
	
	private SegnalazioneContagi getSegnalazioneFromRS(ResultSet result, Connection connection) throws IOException, SQLException {
		PreparedStatement getContagi = connection.prepareStatement(queries.getProperty("get_contagi_query"));
		getContagi.setInt(1, result.getInt("id"));
		getContagi.execute();
		ResultSet contagi = getContagi.getResultSet();
		
		List<Contagio> listaContagi = new ArrayList<>();
		while(contagi.next())
			listaContagi.add(new Contagio(MalattiaContagiosa.values()[contagi.getInt("malattia")],
					contagi.getInt("persone_ricoverate"),
					contagi.getInt("persone_in_cura")));
		
		MySqlDAOFactory database = new MySqlDAOFactory();
		Comune comune = database.getComuneDAO().get(result.getInt("id_comune"));
		
		return new SegnalazioneContagi(listaContagi, result.getDate("data"), comune);
	}

}
