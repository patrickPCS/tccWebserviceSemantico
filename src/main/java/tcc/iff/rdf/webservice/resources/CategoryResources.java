package tcc.iff.rdf.webservice.resources;

import java.net.URI;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import tcc.iff.rdf.webservice.connection.Authentication;
import tcc.iff.rdf.webservice.services.CategoryServices;

@Path("/categories")
public class CategoryResources {
	
	Authentication auth = new Authentication();
	CategoryServices catServices = new CategoryServices();
	
	@GET
	public String getCategories() {
		return "Teste Ok";
	}
	
	@POST
	@Path("/{newCat}")
	public Response adicionarCategoria(@PathParam("newCat") String newCat, @Context UriInfo uriInfo) {
		auth.getAuthentication();
		catServices.addCategory(newCat);
		URI uri = uriInfo.getAbsolutePathBuilder().build();
		return Response.created(uri)
				.entity(newCat)
				.build();
		
	}
	
	
	
	
}
