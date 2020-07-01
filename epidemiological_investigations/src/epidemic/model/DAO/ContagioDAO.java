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

import epidemic.model.Contagio;
import epidemic.model.MalattiaContagiosa;

/**
 * Implementazione dei metodi di comunicazione con il database
 * per oggetti di tipo Contagio
 * 
 * @author Cristiano Di Bari
 * @author Matteo Cavaliere
 * @author Enrico Lonardi
 *
 */
public class ContagioDAO implements DAO<Contagio>{
	private static ContagioDAO istance;
	private Properties queries;
	
	/**
	 * Crea un oggetto ContagioDAO importando le query specificate nel
	 * file contagioQueries.properties
	 * 
	 * @throws IOException
	 */
	private ContagioDAO() throws IOException {
		InputStream queryFile = new FileInputStream("queries/contagioQueries.properties");
		queries = new Properties();
		queries.load(queryFile);
	}
	
	/**
	 * Utilizza il pattern Singleton per assicurarsi che venga creato
	 * un solo ContagioDAO per interagire con il database
	 * 
	 * @return l'oggetto ContagioDAO
	 * @throws IOException
	 */
	public static ContagioDAO getIstance() throws IOException {
		if(istance == null)
			istance = new ContagioDAO();
		return istance;
	}
	
	/**
	 * Restituisce una lista di contagi nel database che appartengono ad
	 * una specifica segnalazione
	 * 
	 * @param id_segnalazione segnalazione da cercare
	 * @return lista di contagi di una segnalazione
	 */
	public List<Contagio> getAllForSegnalazione(int id_segnalazione) {
		List<Contagio> contagi = new ArrayList<>();
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
            	contagi.add(getItemFromRS(result));
            
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
 
        return contagi;
	}

	@Override
	public List<Contagio> getAll() {
		List<Contagio> contagi = new ArrayList<>();
		Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet result = null;
        
        try {
        	connection = MySqlDAOFactory.createConnection();
            preparedStatement = connection.prepareStatement(queries.getProperty("read_all_query"));
            preparedStatement.execute();
            result = preparedStatement.getResultSet();
           
            while(result.next())
            	contagi.add(getItemFromRS(result));
           
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
        
        return contagi;
	}

	@Override
	public Contagio get(int id) {
		Contagio contagio = null;
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
            	contagio = getItemFromRS(result);
            
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
 
        return contagio;
	}

	@Override
	public int create(Contagio contagio) {
		int success = -1;
		Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet result = null;
        
        try {
            connection = MySqlDAOFactory.createConnection();
            preparedStatement = connection.prepareStatement(queries.getProperty("create_query"), Statement.RETURN_GENERATED_KEYS);
            setPreparedStatementFromItem(preparedStatement, contagio);
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
	public boolean update(Contagio contagio) {
		boolean success = false;
		Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
        	connection = MySqlDAOFactory.createConnection();
            preparedStatement = connection.prepareStatement(queries.getProperty("update_query"));
            setPreparedStatementFromItem(preparedStatement, contagio);
            preparedStatement.setInt(5, contagio.getId());
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
	public boolean delete(Contagio contagio) {
		boolean success = false;
		Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
        	connection = MySqlDAOFactory.createConnection();
            preparedStatement = connection.prepareStatement(queries.getProperty("delete_query"));
            preparedStatement.setInt(1, contagio.getId());
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
	public void setPreparedStatementFromItem(PreparedStatement preparedStatement, Contagio contagio) throws SQLException {
		preparedStatement.setInt(1, contagio.getMalattia().ordinal());
		preparedStatement.setInt(2, contagio.getPersoneRicoverate());
		preparedStatement.setInt(3, contagio.getPersoneInCura());
		preparedStatement.setInt(4, contagio.getSegnalazione().getId());
	}
	
	@Override
	public Contagio getItemFromRS(ResultSet result) throws SQLException {
		Contagio contagio = new Contagio(MalattiaContagiosa.values()[result.getInt("malattia")],
							result.getInt("persone_ricoverate"), result.getInt("persone_in_cura"));
		
		contagio.setId(result.getInt("id"));
		return contagio;
	}

	
}
