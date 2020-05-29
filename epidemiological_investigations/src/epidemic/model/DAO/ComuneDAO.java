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

import epidemic.model.Comune;
import epidemic.model.Provincia;
import epidemic.model.Territorio;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


public class ComuneDAO implements DAO<Comune>{
	
	private static ComuneDAO istance;
	private Properties queries;
	
	private ComuneDAO() throws IOException {
		InputStream queryFile = new FileInputStream("queries/comuniQueries.properties");
		queries = new Properties();
		queries.load(queryFile);
	}
	
	public static ComuneDAO getIstance() throws IOException {
		if(istance == null)
			istance = new ComuneDAO();
		return istance;
	}
	
	public Comune getComuneDaNome(String nomeComune) {
		Comune comune = null;
		Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet result = null;
        
        try {
        	connection = MySqlDAOFactory.createConnection();
            preparedStatement = connection.prepareStatement(queries.getProperty("nome_query"));
            preparedStatement.setString(1, nomeComune);
            preparedStatement.execute();
            result = preparedStatement.getResultSet();
 
            if (result != null && result.next())
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
	
	public ObservableList<String> getNomeComuniPerResponsabile(int idUtenteContratto) {
		ObservableList<String> nomiComuni = FXCollections.observableArrayList();
		Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet result = null;
        
        try {
        	connection = MySqlDAOFactory.createConnection();
            preparedStatement = connection.prepareStatement(queries.getProperty("comuni_di_responsabilita_query"));
            preparedStatement.setInt(1, idUtenteContratto);
            preparedStatement.execute();
            result = preparedStatement.getResultSet();
 
            while (result.next())
            	nomiComuni.add(result.getString("nome"));
           
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
 
        return nomiComuni;
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
		int success = -1;
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
	public boolean update(Comune comune) {
		boolean success = false;
		Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
        	connection = MySqlDAOFactory.createConnection();
            preparedStatement = connection.prepareStatement(queries.getProperty("update_query"));
            setPreparedStatementFromComune(preparedStatement, comune);
            preparedStatement.setInt(9, comune.getId());
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
	public boolean delete(Comune comune) {
		boolean success = false;
		Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
        	connection = MySqlDAOFactory.createConnection();
            preparedStatement = connection.prepareStatement(queries.getProperty("delete_query"));
            preparedStatement.setInt(1, comune.getId());
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
	
	private void setPreparedStatementFromComune(PreparedStatement preparedStatement, Comune comune) throws SQLException {
		preparedStatement.setString(1, comune.getNome());
        preparedStatement.setDouble(2, comune.getSuperficie());
        preparedStatement.setString(3, comune.getIstat());
		preparedStatement.setDate(4, comune.getDataIstituzione());
		preparedStatement.setInt(5, comune.getTerritorio().ordinal());
		preparedStatement.setBoolean(6, comune.getSulMare());
		preparedStatement.setInt(7, comune.getProvinciaAppartenenza().getId());
		preparedStatement.setInt(8, comune.getResponsabile().getId());
	}
	
	private Comune getComuneFromRS(ResultSet result) throws SQLException {
		MySqlDAOFactory database = new MySqlDAOFactory();
		Provincia provincia;
		
		try {
			provincia = database.getProvinciaDAO().get(result.getInt("id_provincia"));
		} catch(IOException e) {
			e.printStackTrace();
			return null;
		}
		

		Comune comune = new Comune(result.getString("nome"), result.getDouble("superficie"), result.getString("istat"),
				result.getDate("data_istituzione"), Territorio.values()[result.getInt("territorio")], result.getBoolean("mare"), provincia);
		comune.setId(result.getInt("id"));
		
		return comune;
	}


}



