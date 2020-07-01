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

import epidemic.model.Decesso;
import epidemic.model.Provincia;
import epidemic.model.SegnalazioneDecessi;

public class SegnalazioneDecessiDAO implements DAO<SegnalazioneDecessi> {
	
	private static SegnalazioneDecessiDAO istance;
	private Properties queries;
	
	private SegnalazioneDecessiDAO() throws IOException {
		InputStream queryFile = new FileInputStream("queries/segnalazioneDecessiQueries.properties");
		queries = new Properties();
		queries.load(queryFile);
	}
	
	public static SegnalazioneDecessiDAO getIstance() throws IOException {
		if(istance == null)
			istance = new SegnalazioneDecessiDAO();
		return istance;
	}
	
	
	@Override
	public List<SegnalazioneDecessi> getAll() {
		List<SegnalazioneDecessi> segnDecessi = new ArrayList<>();
		Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet result = null;
        
        try {
        	connection = MySqlDAOFactory.createConnection();
            preparedStatement = connection.prepareStatement(queries.getProperty("read_all_query"));
            preparedStatement.execute();
            result = preparedStatement.getResultSet();
           
            while(result.next())
            	segnDecessi.add(getItemFromRS(result));
           
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
        
        return segnDecessi;
	}


	@Override
	public SegnalazioneDecessi get(int id) {
		SegnalazioneDecessi segnDecessi = null;
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
            	segnDecessi = getItemFromRS(result);
            
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
 
        return segnDecessi;
	}
	


	@Override
	public int create(SegnalazioneDecessi segnDecessi) {
		int success = -1;
		Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet result = null;
        
        try {
            connection = MySqlDAOFactory.createConnection();
            preparedStatement = connection.prepareStatement(queries.getProperty("create_query"), Statement.RETURN_GENERATED_KEYS);
            setPreparedStatementFromSegnalazione(preparedStatement, segnDecessi);
            preparedStatement.execute();
            result = preparedStatement.getGeneratedKeys();
            
            if (result.next() && result != null) {
                success = result.getInt(1);
                createDecessiPerSegnalazione(segnDecessi, success);
            }
            
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
	public boolean update(SegnalazioneDecessi segnDecessi) {
		boolean success = false;
		Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
        	connection = MySqlDAOFactory.createConnection();
            preparedStatement = connection.prepareStatement(queries.getProperty("update_query"));
            setPreparedStatementFromSegnalazione(preparedStatement, segnDecessi);
            preparedStatement.setInt(3, segnDecessi.getId());
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
	public boolean delete(SegnalazioneDecessi segnDecessi) {
		boolean success = false;
		Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
        	connection = MySqlDAOFactory.createConnection();
            preparedStatement = connection.prepareStatement(queries.getProperty("delete_query"));
            preparedStatement.setInt(1, segnDecessi.getId());
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
	
	
	private void setPreparedStatementFromSegnalazione(PreparedStatement preparedStatement, SegnalazioneDecessi segnDecessi) throws SQLException {
		preparedStatement.setDate(1, segnDecessi.getData());
		preparedStatement.setInt(2, segnDecessi.getProvinciaRiferimento().getId());
	}	
	
	
	public SegnalazioneDecessi getItemFromRS(ResultSet result) throws SQLException {
		MySqlDAOFactory database = new MySqlDAOFactory();
		List<Decesso> listaDecessi = new ArrayList<>();
		
		Provincia provincia = null;
		try {
			listaDecessi = database.getDecessoDAO().getAllForSegnalazione(result.getInt("id"));			
			provincia = database.getProvinciaDAO().get(result.getInt("id_comune"));
		} catch(IOException e) {
			e.printStackTrace();
			return null;
		}
		
		return new SegnalazioneDecessi(listaDecessi, result.getDate("data"), provincia);
	}
	
	
	private void createDecessiPerSegnalazione(SegnalazioneDecessi segnDecessi, int success) {
		MySqlDAOFactory database = new MySqlDAOFactory();
		DecessoDAO decessoDAO;
		
		try {
			decessoDAO = database.getDecessoDAO();
		} catch(IOException e) {
			e.printStackTrace();
			return;
		}
		
		segnDecessi.setId(success);
        
        for(Decesso decesso: segnDecessi.getDecessi()) {
        	decesso.setSegnalazione(segnDecessi);
        	decessoDAO.create(decesso);
        }
		
	}
	

}
