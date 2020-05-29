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

import epidemic.model.*;

public class UtenteDAO implements DAO<Utente>{
	private static UtenteDAO istance;
	private Properties queries;
	
	private UtenteDAO() throws IOException {
		InputStream queryFile = new FileInputStream("queries/utentiQueries.properties");
		queries = new Properties();
		queries.load(queryFile);
	}
	
	public static UtenteDAO getIstance() throws IOException {
		if(istance == null)
			istance = new UtenteDAO();
		return istance;
	}
    
    public Utente getUsername(String username) {
		Utente utente = null;
		Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet result = null;
        
        try {
        	connection = MySqlDAOFactory.createConnection();
            preparedStatement = connection.prepareStatement(queries.getProperty("username_query"));
            preparedStatement.setString(1, username);
            preparedStatement.execute();
            result = preparedStatement.getResultSet();
 
            if (result.next() && result != null) {
            	Ruolo ruolo = Ruolo.values()[result.getInt("ruolo")];
            	switch(ruolo) {
	            	case ADMIN: 
	            		utente = new Admin(result.getInt(1), result.getString(2), result.getString(3), result.getString(4), result.getString(5));
	            		break;
	            	case ANALISTA:
	            		utente = new Analista(result.getInt(1), result.getString(2), result.getString(3), result.getString(4), result.getString(5));
	            		break;
	            	case AUTORIZZATO: 
	            		utente = new Autorizzato(result.getInt(1), result.getString(2), result.getString(3), result.getString(4), result.getString(5));
	            		break;
	            	case CONTRATTO:
	            		utente = new Contratto(result.getInt(1), result.getString(2), result.getString(3), result.getString(4), result.getString(5));
	            		break;
	        	}
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
            try {
                connection.close();
            } catch (Exception cse) {
                cse.printStackTrace();
            }
        }
 
        return utente;
    	

    }

	@Override
	public List<Utente> getAll() {
		List<Utente> users = new ArrayList<Utente>();

		Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet result = null;
        
        try {
        	connection = MySqlDAOFactory.createConnection();
            preparedStatement = connection.prepareStatement(queries.getProperty("read_all_query"));
            preparedStatement.execute();
            result = preparedStatement.getResultSet();
 
            while (result.next()) {
            	Ruolo ruolo = Ruolo.values()[result.getInt("ruolo")];
            	Utente utente = null;
            	switch(ruolo) {
	            	case ADMIN: 
	            		utente = new Admin(result.getInt(1), result.getString(2), result.getString(3), result.getString(4), result.getString(5));
	            		break;
	            	case ANALISTA:
	            		utente = new Analista(result.getInt(1), result.getString(2), result.getString(3), result.getString(4), result.getString(5));
	            		break;
	            	case AUTORIZZATO: 
	            		utente = new Autorizzato(result.getInt(1), result.getString(2), result.getString(3), result.getString(4), result.getString(5));
	            		break;
	            	case CONTRATTO:
	            		utente = new Contratto(result.getInt(1), result.getString(2), result.getString(3), result.getString(4), result.getString(5));
	            		break;
            	}
                        	
            	users.add(utente);
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
            try {
                connection.close();
            } catch (Exception cse) {
                cse.printStackTrace();
            }
        }
        
        return users;
	}

	@Override
	public Utente get(int id) {
		Utente utente = null;
		Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet result = null;
        
        try {
        	connection = MySqlDAOFactory.createConnection();
            preparedStatement = connection.prepareStatement(queries.getProperty("read_query"));
            preparedStatement.setInt(1, id);
            preparedStatement.execute();
            result = preparedStatement.getResultSet();
 
            if (result.next() && result != null) {
            	Ruolo ruolo = Ruolo.values()[result.getInt("ruolo")];
            	switch(ruolo) {
	            	case ADMIN: 
	            		utente = new Admin(result.getInt(1), result.getString(2), result.getString(3), result.getString(4), result.getString(5));
	            		break;
	            	case ANALISTA:
	            		utente = new Analista(result.getInt(1), result.getString(2), result.getString(3), result.getString(4), result.getString(5));
	            		break;
	            	case AUTORIZZATO: 
	            		utente = new Autorizzato(result.getInt(1), result.getString(2), result.getString(3), result.getString(4), result.getString(5));
	            		break;
	            	case CONTRATTO:
	            		utente = new Contratto(result.getInt(1), result.getString(2), result.getString(3), result.getString(4), result.getString(5));
	            		break;
	        	}
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
            try {
                connection.close();
            } catch (Exception cse) {
                cse.printStackTrace();
            }
        }
 
        return utente;
	}

	@Override
	public int create(Utente utente) {
		int success = -1;
		Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet result = null;
        
        try {
            connection = MySqlDAOFactory.createConnection();
            preparedStatement = connection.prepareStatement(queries.getProperty("create_query"), Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, utente.getNome());
            preparedStatement.setString(2, utente.getCognome());
            preparedStatement.setString(3, utente.getUsername());
            preparedStatement.setString(4, utente.getPassword());
            preparedStatement.setInt(5, utente.getRuolo().ordinal());
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
	public boolean update(Utente utente) {
		boolean success = false;
		Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
        	connection = MySqlDAOFactory.createConnection();
            preparedStatement = connection.prepareStatement(queries.getProperty("update_query"));
            preparedStatement.setString(1, utente.getNome());
            preparedStatement.setString(2, utente.getCognome());
            preparedStatement.setString(3, utente.getUsername());
            preparedStatement.setString(4, utente.getPassword());
            preparedStatement.setInt(5, utente.getRuolo().ordinal());
            preparedStatement.setInt(6, utente.getId());
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
	public boolean delete(Utente utente) {
		boolean success = false;
		Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
        	connection = MySqlDAOFactory.createConnection();
            preparedStatement = connection.prepareStatement(queries.getProperty("delete_query"));
            preparedStatement.setInt(1, utente.getId());
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

}
