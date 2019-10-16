package tcc.iff.rdf.webservice.model;

public class ProductType {
	
	private String id;
	private String label;
	private String homepage;
	private String description;
	private String subClassOf;
	
	public String getSubClassOf() {
		return subClassOf;
	}
	public void setSubClassOf(String subClassOf) {
		this.subClassOf = subClassOf;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public String getHomepage() {
		return homepage;
	}
	public void setHomepage(String homepage) {
		this.homepage = homepage;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
}
