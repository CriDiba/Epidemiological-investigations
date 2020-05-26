package epidemic.model;

import java.sql.Date;
import java.util.List;

public class SegnalazioneContagi {
	private int id;
	private List<Contagio> contagi;
	private Date data;
	private Comune comuneRiferimento;
	
	public SegnalazioneContagi(List<Contagio> contagi, Date data, Comune comuneRiferimento) {
		this.contagi = contagi;
		this.data = data;
		this.comuneRiferimento = comuneRiferimento;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public List<Contagio> getContagi() {
		return contagi;
	}
	public void setContagi(List<Contagio> contagi) {
		this.contagi = contagi;
	}
	public Date getData() {
		return data;
	}
	public void setData(Date data) {
		this.data = data;
	}
	public Comune getComuneRiferimento() {
		return comuneRiferimento;
	}
	public void setComuneRiferimento(Comune comuneRiferimento) {
		this.comuneRiferimento = comuneRiferimento;
	}
	
}
