package epidemic.model;

import java.util.Date;
import java.util.List;

public class SegnalazioneContagi {
	private List<Contagio> contagi;
	private Date data;
	private Comune comuneRiferimento;
	
	public SegnalazioneContagi(List<Contagio> contagi, Date data, Comune comuneRiferimento) {
		this.contagi = contagi;
		this.data = data;
		this.comuneRiferimento = comuneRiferimento;
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
