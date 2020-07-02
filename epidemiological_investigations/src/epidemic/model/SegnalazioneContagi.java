package epidemic.model;

import java.sql.Date;
import java.util.List;

/**
 * Rappresenta una segnalazione settimanale dei casi di contagio
 * riferiti ad uno specifico comune
 * 
 * @author Cristiano Di Bari
 * @author Matteo Cavaliere
 * @author Enrico Lonardi
 *
 */
public class SegnalazioneContagi {
	private int id;
	private List<Contagio> contagi;
	private Date data;
	private Comune comuneRiferimento;
	
	/**
	 * Crea una segnalazione di contagi
	 * 
	 * @param contagi lista di diversi tipi di contagio
	 * @param data data di riferimento
	 * @param comuneRiferimento comune di riferimento
	 */
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
