
package epidemic.model;

public abstract class Localita {
	private int id;
	private String nome;
	private double superficie;
	
	public Localita(String nome, double superficie) {
		if(!isValid(nome) || superficie < 0)
			throw new IllegalArgumentException();

		this.nome = nome;
		this.superficie = superficie;
	}
	
	public boolean isValid(String nome) {
		boolean hasDigit = nome.matches(".*\\d+.*");
		return !(nome.isBlank() || Character.isLowerCase(nome.charAt(0)) || nome == null || hasDigit);
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}
	
	public String getNome() {
		return nome;
	}
	
	public void setSuperficie(float superficie) {
		if(superficie >= 0)
			this.superficie = superficie;
	}
	
	public double getSuperficie() {
		return superficie;
	}
}