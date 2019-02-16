package tcc.iff.rdf.webservice.resources;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import tcc.iff.rdf.webservice.model.Company;
import tcc.iff.rdf.webservice.services.CompanyServices;

@Path("/companies")
public class CompanyResources {
	
	CompanyServices cp = new CompanyServices();
	@GET
    @Produces(MediaType.APPLICATION_JSON)
    public String listarEmpresas() {		
		return cp.getAllCompanies();		
    }
	
	@DELETE
	public Response deletarEmpresas() {
		cp.deleteAllCompanies();
		return Response.status(Response.Status.NO_CONTENT).build();
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
		public Response adicionarEmpresa(List<Company> companyList) {
		return cp.addCompany(companyList);
	}
	
	@GET
	@Path("/{CompanyID}")
	@Produces({	"application/json", 
    	"application/ld+json",
    	"application/n-triples",
    	"application/rdf+xml",
    	"application/turtle",
    	"application/rdf+json"
    	})	
		public Response lerEmpresa(@PathParam("CompanyID") String companyID, @HeaderParam("Accept") String accept) {
		return cp.getCompany(companyID, accept);
	}
	@DELETE
	@Path("/{CompanyID}")
	public Response deletarEmpresa(@PathParam("CompanyID") String companyID) {
		cp.deleteCompany(companyID);
		return Response.status(Response.Status.NO_CONTENT).build();
	}
	
	@PUT
	@Path("/{CompanyID}")
	@Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
	public Response alterarEmpresa(@PathParam("CompanyID") String companyID, Company newCompany) {
		return cp.updateCompany(companyID, newCompany);
	}
	
	@Path("/{CompanyID}/offers")
	public CompanyOfferResources getOfferResources() {
		return new CompanyOfferResources();
	}
}
