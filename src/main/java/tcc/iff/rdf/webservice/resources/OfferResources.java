package tcc.iff.rdf.webservice.resources;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import tcc.iff.rdf.webservice.services.OfferServices;

@Path("/offers")
public class OfferResources {
	
	OfferServices offerServ = new OfferServices();
	@GET
    @Produces(MediaType.TEXT_PLAIN)
    public String listarOfertas() {		
		return offerServ.getAllOffers();	
    }
	
	@DELETE
	public Response deletarOfertas() {
		offerServ.deleteAllOffers();
		return Response.status(Response.Status.NO_CONTENT).build();
	}
	
	//offertas para um determinado produto
	
}
