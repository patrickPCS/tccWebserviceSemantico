package tcc.iff.rdf.webservice.resources;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;

@Path("/")
public class OfferResources {
	
	@GET
	public String getAllOffers() {
		return "Método getAllOffers ok";
	}
	
	@DELETE
	public String deleteAllOffers() {
		return "Método deleteAllOffers ok";
	}
	
	@POST
	public String addOffer() {
		return "Método addOffer ok";
	}
	
	@GET 
	@Path("/{OfferID}")
	public String getOffer() {
		return "Método getOffer ok";
	}
	
	@DELETE 
	@Path("/{OfferID}")
	public String deleteOffer() {
		return "Método deleteOffer ok";
	}
	
	@PUT
	@Path("/{OfferID}")
	public String updateOffer() {
		return "Método updateOffer ok";
	}
}
