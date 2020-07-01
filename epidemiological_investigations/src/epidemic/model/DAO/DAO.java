package epidemic.model.DAO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public interface DAO<T> {
	
	public List<T> getAll();
	
	public T get(int id);
	
	public int create(T t);
	
	public boolean update(T t);
	
	public boolean delete(T t);
	
	public T getItemFromRS(ResultSet result) throws SQLException;
}
