package tcc.iff.rdf.webservice.resources;

import java.net.URI;

//import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
//import javax.ws.rs.core.MediaType;
//import org.apache.jena.atlas.web.MediaType ;
//import org.apache.jena.ext.com.google.common.net.MediaType;

import tcc.iff.rdf.webservice.RDFMediaType;
import tcc.iff.rdf.webservice.connection.Authentication;
import tcc.iff.rdf.webservice.services.CategoryServices;

@Path("/categories")
public class CategoryResources {
	
	Authentication auth = new Authentication();
	CategoryServices catServices = new CategoryServices();
	
	/*
	@GET
    @Produces(MediaType.TEXT_PLAIN)
    public String getCategories() {
		auth.getAuthentication();
        return "Got categories!";
    }
	
		*/
	@POST
	@Path("/{newCat}")
	@Produces(RDFMediaType.APPLICATION_SPARQL_UPDATE)
	public Response adicionarCategoria(@PathParam("newCat") String newCat, @Context UriInfo uriInfo) {
		auth.getAuthentication();
		catServices.addCategory(newCat);
		URI uri = uriInfo.getAbsolutePathBuilder().build();
		return Response.created(uri)
				.entity(newCat)
				.build();
		
	}

	
	
	
}
