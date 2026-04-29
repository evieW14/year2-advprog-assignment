package uk.ac.mmu.advprog.assessment.shared;

public class Wine {
	private int id;
	private String name;
	private String type;
	private String blend_type;
	private double abv;
	private String acidity;
	private String body;
	private int winery_id;
	
	public Wine(int id, String name, String type, String blend_type, double abv, String acidity, String body,
			int winery_id) {
		super();
		this.id = id;
		this.name = name;
		this.type = type;
		this.blend_type = blend_type;
		this.abv = abv;
		this.acidity = acidity;
		this.body = body;
		this.winery_id = winery_id;
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
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getBlend_type() {
		return blend_type;
	}
	public void setBlend_type(String blend_type) {
		this.blend_type = blend_type;
	}
	public double getAbv() {
		return abv;
	}
	public void setAbv(double abv) {
		this.abv = abv;
	}
	public String getAcidity() {
		return acidity;
	}
	public void setAcidity(String acidity) {
		this.acidity = acidity;
	}
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	public int getWinery_id() {
		return winery_id;
	}
	public void setWinery_id(int winery_id) {
		this.winery_id = winery_id;
	}
}
