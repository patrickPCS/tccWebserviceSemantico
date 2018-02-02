package tcc.iff.rdf.webservice.resources;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import tcc.iff.rdf.webservice.RDFMediaType;
import tcc.iff.rdf.webservice.data.Category;
//import tcc.iff.rdf.webservice.connection.Authentication;
import tcc.iff.rdf.webservice.services.CategoryServices;

@Path("/categories")
public class CategoryResources {
	
	CategoryServices catServices = new CategoryServices();
	
	
	@GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Category> listarCategorias() {
		catServices.getAllCategories();
		List<Category> categories = new ArrayList<>();
		return categories;
    }
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("{CategoryID}")
	public Category getCategory() {
		Category newCat = new Category();
		return newCat;
	}
	
		
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response adicionarCategoria(Category newCat) {
	//public Response adicionarCategoria(@PathParam("newCat") String newCat, @Context UriInfo uriInfo) {
		//auth.getAuthentication();
		 catServices.addCategory(newCat);
		//URI uri = uriInfo.getAbsolutePathBuilder().build();
		/*return Response.created(uri)
				.entity(newCat)
				.build();
		*/
	   return Response.status(Response.Status.CREATED).build();
	}
	
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	public Response alterarCategoria(Category cat) {
		catServices.updateCategory(cat);
		   return Response.status(Response.Status.OK).build();
	}

	@DELETE
	@Path("{CategoryID}")
	public Response removerCategoria(@PathParam("CategoriaID") int catID) {
		catServices.deleteCategory(catID);
		   return Response.status(Response.Status.NO_CONTENT).build();

	}
	
	
}
