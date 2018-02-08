package tcc.iff.rdf.webservice.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Product {
	
	private String productURI;
	private String type;
	private String name;
	private String description;
	
	public String getProductURI() {
		return productURI;
	}
	public void setProductURI(String productURI) {
		this.productURI = productURI;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	

}
