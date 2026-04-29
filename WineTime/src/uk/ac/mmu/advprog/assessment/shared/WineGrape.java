package uk.ac.mmu.advprog.assessment.shared;

public class WineGrape {
	private int wine_id;
	private int grape_id;

	public WineGrape(int wine_id, int grape_id) {
		super();
		this.wine_id = wine_id;
		this.grape_id = grape_id;
	}

	public int getWine_id() {
		return wine_id;
	}

	public void setWine_id(int wine_id) {
		this.wine_id = wine_id;
	}

	public int getGrape_id() {
		return grape_id;
	}

	public void setGrape_id(int grape_id) {
		this.grape_id = grape_id;
	}


}
