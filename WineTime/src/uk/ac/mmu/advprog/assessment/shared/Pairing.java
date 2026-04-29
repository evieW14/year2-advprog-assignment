package uk.ac.mmu.advprog.assessment.shared;

public class Pairing {
	private int id;
	private String food;

	public Pairing(int id, String food) {
		super();
		this.id = id;
		this.food = food;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getFood() {
		return food;
	}

	public void setFood(String food) {
		this.food = food;
	}


}
