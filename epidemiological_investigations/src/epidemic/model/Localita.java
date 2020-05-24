package epidemic.model;

public abstract class Localita {
	private String nome;
	private float superficie;
	private int id;
	
	public Localita(String nome, float superficie) {
		boolean hasDigit = nome.matches(".*\\d+.*");
		
		if(nome.isBlank() || Character.isLowerCase(nome.charAt(0))
				|| nome == null || hasDigit || superficie < 0)
			throw new IllegalArgumentException();

		this.nome = nome;
		this.superficie = superficie;
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
		this.superficie = superficie;
	}
	
	public float getSuperficie() {
		return superficie;
	}
}
