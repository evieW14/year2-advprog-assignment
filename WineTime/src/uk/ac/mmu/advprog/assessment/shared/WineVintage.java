package uk.ac.mmu.advprog.assessment.shared;

public class WineVintage {
	private int wine_id;
	private int year;
	
	public WineVintage(int wine_id, int year) {
		super();
		this.wine_id = wine_id;
		this.year = year;
	}

	public int getWine_id() {
		return wine_id;
	}

	public void setWine_id(int wine_id) {
		this.wine_id = wine_id;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}
}
