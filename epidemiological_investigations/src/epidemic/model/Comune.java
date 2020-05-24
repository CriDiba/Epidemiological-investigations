package epidemic.model;

import java.sql.Date;

public class Comune extends Localita {
	private final String istat;
	private final Date dataIstituzione;
	private final Territorio territorio;
	private final boolean sulMare;
	private final Provincia provincia;
	
	public Comune(String nome, float superficie, String istat, Date dataIstituzione, Territorio territorio, boolean sulMare, Provincia provincia) {
		super(nome, superficie);
		
		if(!istat.chars().allMatch(c -> Character.isDigit(c)))
			throw new IllegalArgumentException();
		
		this.istat = istat;
		this.dataIstituzione = dataIstituzione;
		this.territorio = territorio;
		this.sulMare = sulMare;
		this.provincia = provincia;

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

	public Provincia getProvincia() {
		return provincia;
	}	
	
}
