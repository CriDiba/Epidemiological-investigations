package epidemic.model;

import java.util.List;

public class Regione extends Localita{
	private Comune capoluogo;
	private List<Provincia> province;
	
	public Regione(String nome, float superficie, Comune capoluogo, List<Provincia> province) {
		super(nome, superficie);
		this.capoluogo = capoluogo;
		this.province = province;
	}
	
	public Comune getCapoluogo() {
		return capoluogo;
	}
	
	public List<Provincia> getProvince(){
		return province;
	}

}
