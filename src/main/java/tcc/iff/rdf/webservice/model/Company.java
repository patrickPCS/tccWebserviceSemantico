package tcc.iff.rdf.webservice.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Company {
	private String site;
	private String legalName;
	private String offer;
	
	public String getSite() {
		return site;
	}
	public void setSite(String site) {
		this.site = site;
	}
	public String getLegalName() {
		return legalName;
	}
	public void setLegalName(String legalName) {
		this.legalName = legalName;
	}
	public String getOffer() {
		return offer;
	}
	public void setOffer(String offer) {
		this.offer = offer;
	}
	
	
}
