package uk.ac.mmu.advprog.assessment.shared;

public class WinePairing {
	private int wine_id;
	private int pairing_id;

	public WinePairing(int wine_id, int pairing_id) {
		super();
		this.wine_id = wine_id;
		this.pairing_id = pairing_id;
	}

	public int getWine_id() {
		return wine_id;
	}

	public void setWine_id(int wine_id) {
		this.wine_id = wine_id;
	}

	public int getPairing_id() {
		return pairing_id;
	}

	public void setPairing_id(int pairing_id) {
		this.pairing_id = pairing_id;
	}
}
