package epidemic.model;

public class Localita {
	private String nome;
	private float superficie;
	
	public Localita(String nome, float superficie) {
		boolean hasDigit = nome.matches(".*\\d+.*");
		
		if(nome.isEmpty() || Character.isLowerCase(nome.charAt(0))
				|| hasDigit || superficie < 0)
			throw new IllegalArgumentException();

		this.nome = nome;
		this.superficie = superficie;
	}
	
	public void setNome(String nome) {
		this.nome = nome;
	}
	
	public String getNome() {
		return nome;
	}
	
	public void setSuperficie(float superficie) {
		this.superficie = superficie;
	}
	
	public float getSuperficie() {
		return superficie;
	}
}
