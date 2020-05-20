package epidemic.model.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import epidemic.model.*;

public class UtenteDAO implements DAO<Utente>{
	
	private static final String READ_ALL_QUERY = "SELECT * FROM utenti";
	
	private static final String READ_QUERY = "SELECT * FROM utenti WHERE id = ?";
	
	private static final String CREATE_QUERY = "INSERT INTO utenti (nome, cognome, username, password, ruolo) VALUES (?,?,?,?,?)";

    private static final String UPDATE_QUERY = "UPDATE utenti SET nome=?, cognome=?, username=?, password=?, ruolo=? WHERE id = ?";

    private static final String DELETE_QUERY = "DELETE FROM utenti WHERE id = ?";
    
    private static final String USERNAME = "SELECT * FROM utenti WHERE username = ?";
    
    
    public Utente getUsername(String username) {
		Utente utente = null;
		Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet result = null;
        
        try {
        	connection = MySqlDAOFactory.createConnection();
            preparedStatement = connection.prepareStatement(USERNAME);
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
            preparedStatement = connection.prepareStatement(READ_ALL_QUERY);
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
            preparedStatement = connection.prepareStatement(READ_QUERY);
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
		Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet result = null;
        
        try {
            connection = MySqlDAOFactory.createConnection();
            preparedStatement = connection.prepareStatement(CREATE_QUERY, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, utente.getNome());
            preparedStatement.setString(2, utente.getCognome());
            preparedStatement.setString(3, utente.getUsername());
            preparedStatement.setString(4, utente.getPassword());
            preparedStatement.setInt(5, utente.getRuolo().ordinal());
            preparedStatement.execute();
            result = preparedStatement.getGeneratedKeys();
 
            if (result.next() && result != null) {
                return result.getInt(1);
            } else {
                return -1;
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
 
        return -1;
	}

	@Override
	public boolean update(Utente utente) {
		Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
        	connection = MySqlDAOFactory.createConnection();
            preparedStatement = connection.prepareStatement(UPDATE_QUERY);
            preparedStatement.setString(1, utente.getNome());
            preparedStatement.setString(2, utente.getCognome());
            preparedStatement.setString(3, utente.getUsername());
            preparedStatement.setString(4, utente.getPassword());
            preparedStatement.setInt(5, utente.getRuolo().ordinal());
            preparedStatement.setInt(6, utente.getId());
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
	public boolean delete(Utente utente) {
		Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
        	connection = MySqlDAOFactory.createConnection();
            preparedStatement = connection.prepareStatement(DELETE_QUERY);
            preparedStatement.setInt(1, utente.getId());
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

}
