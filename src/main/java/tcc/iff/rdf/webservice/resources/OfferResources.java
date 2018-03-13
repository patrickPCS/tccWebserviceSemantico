package tcc.iff.rdf.webservice.resources;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import tcc.iff.rdf.webservice.RDFMediaType;
import tcc.iff.rdf.webservice.model.Offer;
import tcc.iff.rdf.webservice.services.OfferServices;

@Path("/offers")
public class OfferResources {
	
	OfferServices offerServ = new OfferServices();
	@GET
    @Produces(MediaType.TEXT_PLAIN)
    public String listarProdutos() {		
		return offerServ.getAllOffers();	
    }
	
	@DELETE
	public Response deletarProdutos() {
		offerServ.deleteAllOffers();
		return Response.status(Response.Status.NO_CONTENT).build();
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
		public Response adicionarProduto(List<Offer> OfferList) {
		offerServ.addOffering(OfferList);
	   return Response.status(Response.Status.CREATED).build();
	}
	
	@GET
	@Path("/{OfferID}")
    @Produces(RDFMediaType.APPLICATION_JSON_LD)
	public String lerProduto(@PathParam("OfferID") String offerID) {
		return offerServ.getOffer(offerID);
	}
	
	@DELETE
	@Path("/{OfferID}")
	public Response deletarProduto(@PathParam("OfferID") String offerID) {
		offerServ.deleteOffer(offerID);
		return Response.status(Response.Status.NO_CONTENT).build();
	}
	
	@PUT
	@Path("/{OfferID}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response alterarProduto(@PathParam("OfferID") String offerID, Offer updatedOffer) {
		offerServ.updateOffering(offerID, updatedOffer);
		return Response.status(Response.Status.CREATED).build();
	}
	
}
