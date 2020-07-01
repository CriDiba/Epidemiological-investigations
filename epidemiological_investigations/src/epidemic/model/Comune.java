package epidemic.model;

import java.sql.Date;
import java.util.List;

public class Comune extends Localita {
	private final String istat;
	private final Date dataIstituzione;
	private final Territorio territorio;
	private final boolean sulMare;
	private final Provincia provinciaAppartenenza;
	private Contratto responsabile;
	private List<SegnalazioneContagi> segnalazioniContagi;
	
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

	public Date getDataIstituzione() {
		return dataIstituzione;
	}

	public Territorio getTerritorio() {
		return territorio;
	}

	public boolean getSulMare() {
		return sulMare;
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
