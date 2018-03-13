package tcc.iff.rdf.webservice.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Company {
	private String companyID;
	private String companyURL;
	private String legalName;
	private String email;
	private String catalogURI;
	
	public String getCompanyID() {
		return companyID;
	}
	public void setCompanyID(String companyID) {
		this.companyID = companyID;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getCompanyURL() {
		return companyURL;
	}
	public void setCompanyURL(String companyURL) {
		this.companyURL = companyURL;
	}
	public String getLegalName() {
		return legalName;
	}
	public void setLegalName(String legalName) {
		this.legalName = legalName;
	}
	public String getCatalogURI() {
		return catalogURI;
	}
	public void setCatalogURI(String catalogURI) {
		this.catalogURI = catalogURI;
	}
	
	
	
	
}
