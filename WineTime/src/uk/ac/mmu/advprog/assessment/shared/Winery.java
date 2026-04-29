package uk.ac.mmu.advprog.assessment.shared;

public class Winery {
	private int id;
	private String name;
	private int region_id;
	private String website;

	public Winery(int id, String name, int region_id, String website) {
		super();
		this.id = id;
		this.name = name;
		this.region_id = region_id;
		this.website = website;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getRegion_id() {
		return region_id;
	}

	public void setRegion_id(int region_id) {
		this.region_id = region_id;
	}

	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

}
