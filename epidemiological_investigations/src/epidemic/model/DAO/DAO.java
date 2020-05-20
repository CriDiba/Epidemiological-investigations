package epidemic.model.DAO;

import java.util.List;

public interface DAO<T> {
	
	/**
	 * 
	 * @return
	 */
	public List<T> getAll();
	
	public T get(int id);
	
	public int create(T t);
	
	public boolean update(T t);
	
	public boolean delete(T t);


}
