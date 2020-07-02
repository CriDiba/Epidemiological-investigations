package epidemic.model;

import java.sql.Date;
import java.util.List;

/**
 * Rappresenta un Comune italiano caratterizzato da: un codice ISTAT univoco,
 * una data di istituzione, il tipo di territorio, la presenza del mare, 
 * una provincia di appartenenza
 * 
 * @author Cristiano Di Bari
 * @author Matteo Cavaliere
 * @author Enrico Lonardi
 *
 */
public class Comune extends Localita {
	private String istat;
	private Date dataIstituzione;
	private Territorio territorio;
	private boolean sulMare;
	private final Provincia provinciaAppartenenza;
	private Contratto responsabile;
	private List<SegnalazioneContagi> segnalazioniContagi;
	
	/**
	 * Crea un comune
	 * 
	 * @param nome nome del comune
	 * @param superficie superficie in km^2
	 * @param istat codice istat univoco
	 * @param dataIstituzione data di istituzione
	 * @param territorio morfologia del territorio
	 * @param sulMare presenza del mare
	 * @param provinciaAppartenenza provincia di appartenenza
	 */
	public Comune(String nome, double superficie, String istat, Date dataIstituzione, Territorio territorio, boolean sulMare, Provincia provinciaAppartenenza) {
		super(nome, superficie);
		
		if(!istat.chars().allMatch(c -> Character.isDigit(c)) || istat.length() != 6)
			throw new IllegalArgumentException();
		
		this.istat = istat;
		this.dataIstituzione = dataIstituzione;
		this.territorio = territorio;
		this.sulMare = sulMare;
		this.provinciaAppartenenza = provinciaAppartenenza;
	}
	
	
	public List<SegnalazioneContagi> getSegnalazioniContagi() {
		return segnalazioniContagi;
	}

	public void setSegnalazioniContagi(List<SegnalazioneContagi> segnalazioniContagi) {
		this.segnalazioniContagi = segnalazioniContagi;
	}

	public void setResponsabile(Contratto responsabile) {
		this.responsabile = responsabile;
	}
	
	public Contratto getResponsabile() {
		return responsabile;
	}

	public String getIstat() {
		return istat;
	}
	
	public void setIstat(String istat) {
		this.istat = istat;
	}
	
	public Date getDataIstituzione() {
		return dataIstituzione;
	}
	
	public void setDataIstituzione(Date dataIstituzione) {
		this.dataIstituzione = dataIstituzione;
	}

	public Territorio getTerritorio() {
		return territorio;
	}
	
	public void setTerritorio(Territorio territorio) {
		this.territorio = territorio;
	}

	public boolean getSulMare() {
		return sulMare;
	}
	
	public void setSulMare(boolean sulMare) {
		this.sulMare = sulMare;
	}

	public Provincia getProvinciaAppartenenza() {
		return provinciaAppartenenza;
	}
	
	@Override
	public boolean equals(Object obj) {
		return (obj instanceof Comune) && ((Comune)obj).getNome().equals(this.getNome());		
	}
	
	@Override
	public String toString() {
		return this.getNome();
	}
	
}
