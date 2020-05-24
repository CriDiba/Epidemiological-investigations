package epidemic.model.DAO;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import epidemic.model.Comune;
import epidemic.model.Localita;
import epidemic.model.Provincia;
import epidemic.model.Regione;
import epidemic.model.Utente;

public class LocalitaDAO implements DAO<Localita>{
	
	private final String tipoLocalita;
	private final Properties queries;
	
	public LocalitaDAO(String tipoLocalita) throws IOException {
		InputStream queryFile = null;
		queries = new Properties();
		
		if(tipoLocalita.equalsIgnoreCase("regione"))
			queryFile = getClass().getResourceAsStream("regioniQueries.properties");
		else if(tipoLocalita.equalsIgnoreCase("comune"))
			queryFile = getClass().getResourceAsStream("comuniQueries.properties");
		else if(tipoLocalita.equalsIgnoreCase("provincia"))
			queryFile = getClass().getResourceAsStream("provinceQueries.properties");
			
		queries.load(queryFile);
		
		this.tipoLocalita = tipoLocalita;			
	}
	
	public List<Localita> getAll() {
		List<Localita> localities = new ArrayList<>();
		
		Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet result = null;
        
        try {
        	connection = MySqlDAOFactory.createConnection();
            preparedStatement = connection.prepareStatement(queries.getProperty("read_all_query"));
            preparedStatement.execute();
            result = preparedStatement.getResultSet();
            
            if(tipoLocalita.equalsIgnoreCase("regione"))
	            while(result.next())
	            	localities.add(new Regione(/*da riempire in base al database*/));
            else if(tipoLocalita.equalsIgnoreCase("comune"))
            	while(result.next())
            		localities.add(new Comune(/**/));
            else
            	while(result.next())
            		localities.add(new Provincia(/**/));
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
        
        return localities;
	}

	public Localita get(int id) {
		Localita localita = null;
		Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet result = null;
        
        try {
        	connection = MySqlDAOFactory.createConnection();
            preparedStatement = connection.prepareStatement(queries.getProperty("read_query"));
            preparedStatement.setInt(1, id);
            preparedStatement.execute();
            result = preparedStatement.getResultSet();
            
            if(result != null && result.next()) {
            	if(tipoLocalita.equalsIgnoreCase("regione"))
            		localita = new Regione(/**/);
            	else if(tipoLocalita.equalsIgnoreCase("comune"))
            		localita = new Comune(/**/);
            	else
            		localita = new Provincia(/**/);
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
 
        return localita;
    	
	}

	public int create(Localita t) {
		// TODO Auto-generated method stub
		return 0;
	}

	public boolean update(Localita t) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean delete(Localita t) {
		// TODO Auto-generated method stub
		return false;
	}

}
