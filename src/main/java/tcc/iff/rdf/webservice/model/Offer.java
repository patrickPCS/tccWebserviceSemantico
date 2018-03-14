package tcc.iff.rdf.webservice.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Offer {
	
	private String offerID;
	private String offerURI; //pagina da oferta
	private String includes; //produto que essa oferta inclui
	private String validFrom; //valido a partir de
	private String validThrough; //valido até
	private String hasCurrency; //moeda (USD, BRL)
	private String price;	// ex.: 19.90
	private String companyID; 
	
	public String getCompanyID() {
		return companyID;
	}
	public void setCompanyID(String companyID) {
		this.companyID = companyID;
	}
	public String getOfferID() {
		return offerID;
	}
	public void setOfferID(String offerID) {
		this.offerID = offerID;
	}
	
	public String getOfferURI() {
		return offerURI;
	}
	public void setOfferURI(String offerURI) {
		this.offerURI = offerURI;
	}
	public String getIncludes() {
		return includes;
	}
	public void setIncludes(String includes) {
		this.includes = includes;
	}
	public String getValidFrom() {
		return validFrom;
	}
	public void setValidFrom(String validFrom) {
		this.validFrom = validFrom;
	}
	public String getValidThrough() {
		return validThrough;
	}
	public void setValidThrough(String validThrough) {
		this.validThrough = validThrough;
	}
	public String getHasCurrency() {
		return hasCurrency;
	}
	public void setHasCurrency(String hasCurrency) {
		this.hasCurrency = hasCurrency;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	
	
}
