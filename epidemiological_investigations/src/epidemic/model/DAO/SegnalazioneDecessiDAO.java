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

/**
 * Implementazione dei metodi di comunicazione con il database
 * per oggetti di tipo SegnalazioneDecessi
 * 
 * @author Cristiano Di Bari
 * @author Matteo Cavaliere
 * @author Enrico Lonardi
 *
 */
public class SegnalazioneDecessiDAO implements DAO<SegnalazioneDecessi> {
	
	private static SegnalazioneDecessiDAO istance;
	private Properties queries;
	
	/**
	 * Crea un oggetto SegnalazioneDecessiDAO importando le query specificate nel
	 * file segnalazioneDecessiQueries.properties
	 * 
	 * @throws IOException
	 */
	private SegnalazioneDecessiDAO() throws IOException {
		InputStream queryFile = new FileInputStream("queries/segnalazioneDecessiQueries.properties");
		queries = new Properties();
		queries.load(queryFile);
	}
	
	/**
	 * Utilizza il pattern Singleton per assicurarsi che venga creato
	 * un solo SegnalazioneDecessiDAO per interagire con il database 
	 * 
	 * @return l'oggetto SegnalazioneDecessiDAO
	 * @throws IOException
	 */
	public static SegnalazioneDecessiDAO getIstance() throws IOException {
		if(istance == null)
			istance = new SegnalazioneDecessiDAO();
		return istance;
	}
	
	/**
	 * Ritorna l'ultima (più recente) segnalazione di decessi
	 * presente nel database per una determinata provincia
	 * 
	 * @param provincia provincia da ricercare
	 * @return ultima segnalazione effettuata per una provincia
	 */
	public SegnalazioneDecessi getLastForProvincia(Provincia provincia) {
		SegnalazioneDecessi segnDecessi = null;
		Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet result = null;
        
        try {
        	connection = MySqlDAOFactory.createConnection();
            preparedStatement = connection.prepareStatement(queries.getProperty("read_per_provincia_query"));
            preparedStatement.setInt(1, provincia.getId());
            preparedStatement.execute();
            result = preparedStatement.getResultSet();
            
            while(result.next())
            	if(result.isLast())
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
	
	
	/**
	 * Ritorna la lista delle segnalazioni effettuate per una provincia
	 * 
	 * @param provincia provincia da ricercare
	 * @return lista di segnalazioni effettuate per quella provincia
	 */
	public List<SegnalazioneDecessi> getForProvincia(Provincia provincia) {
		List<SegnalazioneDecessi> segnDecessi = new ArrayList<>();
		Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet result = null;
        
        try {
        	connection = MySqlDAOFactory.createConnection();
            preparedStatement = connection.prepareStatement(queries.getProperty("read_per_provincia_query"));
            preparedStatement.setInt(1, provincia.getId());
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
            setPreparedStatementFromItem(preparedStatement, segnDecessi);
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
            setPreparedStatementFromItem(preparedStatement, segnDecessi);
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
	
	@Override
	public void setPreparedStatementFromItem(PreparedStatement preparedStatement, SegnalazioneDecessi segnDecessi) throws SQLException {
		preparedStatement.setDate(1, segnDecessi.getData());
		preparedStatement.setInt(2, segnDecessi.getProvinciaRiferimento().getId());
	}	
	
	@Override
	public SegnalazioneDecessi getItemFromRS(ResultSet result) throws SQLException {
		MySqlDAOFactory database = new MySqlDAOFactory();
		List<Decesso> listaDecessi = new ArrayList<>();
		
		Provincia provincia = null;
		try {
			listaDecessi = database.getDecessoDAO().getAllForSegnalazione(result.getInt("id"));			
			provincia = database.getProvinciaDAO().get(result.getInt("id_provincia"));
		} catch(IOException e) {
			e.printStackTrace();
			return null;
		}
		
		return new SegnalazioneDecessi(listaDecessi, result.getDate("data"), provincia);
	}
	
	/**
	 * Crea nel database i decessi relativi ad una determinata segnalazione di decessi
	 * 
	 * @param segnDecessi la segnalazione di decessi
	 * @param success id della segnalazione di decessi
	 */
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
