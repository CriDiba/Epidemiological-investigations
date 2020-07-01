package epidemic.model;

import java.sql.Date;
import java.util.List;

public class SegnalazioneDecessi {
	private int id;
	private List<Decesso> decessi;
	private Date data;
	private Provincia provinciaRiferimento;
	
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
