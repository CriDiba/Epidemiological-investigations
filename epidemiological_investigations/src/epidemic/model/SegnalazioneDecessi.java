package epidemic.model;

import java.sql.Date;
import java.util.List;

/**
 * Rappresenta una segnalazione annuale dei deceduti in una specifica provincia
 * 
 * @author Cristiano Di Bari
 * @author Matteo Cavaliere
 * @author Enrico Lonardi
 *
 */
public class SegnalazioneDecessi {
	private int id;
	private List<Decesso> decessi;
	private Date data;
	private Provincia provinciaRiferimento;
	
	/**
	 * Crea una segnalazione di decessi
	 * 
	 * @param decessi lista di diversi tipi di decessi
	 * @param data data di riferimento
	 * @param provinciaRiferimento provincia di riferimento
	 */
	public SegnalazioneDecessi(List<Decesso> decessi, Date data, Provincia provinciaRiferimento) {
		this.data = data;
		this.provinciaRiferimento = provinciaRiferimento;
		this.decessi = decessi;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public List<Decesso> getDecessi() {
		return decessi;
	}
	
	public void setDecessi(List<Decesso> decessi) {
		this.decessi = decessi;
	}
	
	public Date getData() {
		return data;
	}
	
	public void setData(Date data) {
		this.data = data;
	}
	
	public Provincia getProvinciaRiferimento() {
		return provinciaRiferimento;
	}
	public void seProvinciaRiferimento(Provincia provinciaRiferimento) {
		this.provinciaRiferimento = provinciaRiferimento;
	}

}
