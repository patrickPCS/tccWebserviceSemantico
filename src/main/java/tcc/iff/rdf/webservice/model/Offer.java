package tcc.iff.rdf.webservice.model;

import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Offer {
	
	
	private String offerID; // id da oferta
	private String offerPage; //pagina da oferta
	private String includes; //produto que essa oferta inclui
	private Date validFrom; //valido a partir de
	private Date validThrough; //valido até
	private String hasCurrency; //moeda (USD, BRL)
	private float price;	// ex.: 19.90
	private String companyID;
	private String description;
	
	
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
	
	public String getOfferPage() {
		return offerPage;
	}
	public void setOfferPage(String offerPage) {
		this.offerPage = offerPage;
	}
	public String getIncludes() {
		return includes;
	}
	public void setIncludes(String includes) {
		this.includes = includes;
	}
	
	public Date getValidFrom() {
		return validFrom;
	}
	public void setValidFrom(Date validFrom) {
		this.validFrom = validFrom;
	}
	public Date getValidThrough() {
		return validThrough;
	}
	public void setValidThrough(Date validThrough) {
		this.validThrough = validThrough;
	}
	public void setPrice(float price) {
		this.price = price;
	}
	public float getPrice() {
		return price;
	}
	public String getHasCurrency() {
		return hasCurrency;
	}
	public void setHasCurrency(String hasCurrency) {
		this.hasCurrency = hasCurrency;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
}
