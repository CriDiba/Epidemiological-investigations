package epidemic.model.DAO;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;


/**
 * Fornisce i metodi di base per l'interazione con il database.
 * 
 * @author Cristiano Di Bari
 * @author Matteo Cavaliere
 * @author Enrico Lonardi
 *
 * @param <T> tipo di oggetto che estende l'interfaccia
 */
public interface DAO<T> {
	
	/**
	 * Ritorna tutti di oggetti di tipo <T> presenti nel database
	 * 
	 * @return lista di oggetti di tipo <T>
	 */
	public List<T> getAll();
	
	/**
	 * Ritorna l'oggetto di tipo <T> con uno specifico id
	 * 
	 * @param id l'id dell'oggetto da cercare
	 * @return oggetto di tipo <T>
	 */
	public T get(int id);
	
	/**
	 * Aggiunge l'ogetto di tipo <T> al database
	 * 
	 * @param t l'oggetto da aggiungere al database
	 * @return l'id dell'oggetto creato
	 */
	public int create(T t);
	
	/**
	 * Aggiorna un oggetto di tipo <T> già presente nel database
	 * 
	 * @param t l'oggetto da aggiornare
	 * @return true se l'aggiornamento ha successo
	 */
	public boolean update(T t);
	
	/**
	 * Elimina un oggetto di tipo <T> dal database
	 * 
	 * @param t l'oggetto da eliminare
	 * @return true se l'eliminazione ha successo
	 */
	public boolean delete(T t);
	
	/**
	 * Crea un oggetto di tipo <T> a partire da un ResultSet
	 * 
	 * @param result il result set restituito dal database
	 * @return oggetto di tipo <T>
	 * @throws SQLException 
	 */
	public T getItemFromRS(ResultSet result) throws SQLException;
	
	/**
	 * Inizializza la stringa di query preparedStatement con i parametri
	 * dell'oggetto di tipo <T>
	 * 
	 * @param preparedStatement stringa di query
	 * @param t l'oggetto da cui prendere i parametri
	 * @throws SQLException 
	 */
	public void setPreparedStatementFromItem(PreparedStatement preparedStatement, T t) throws SQLException;
}
