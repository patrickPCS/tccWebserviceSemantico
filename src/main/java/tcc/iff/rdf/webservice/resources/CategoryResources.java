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
import tcc.iff.rdf.webservice.model.Category;
import tcc.iff.rdf.webservice.services.CategoryServices;

@Path("/categories")
public class CategoryResources {
	
		CategoryServices catServ = new CategoryServices();
		@GET
	    @Produces(MediaType.TEXT_PLAIN)
	    public String listarCategorias() {		
			return catServ.getAllCategories();		
	    }
		
		@DELETE
		public Response deletarCategorias() {
			catServ.deleteAllCategories();
			return Response.status(Response.Status.NO_CONTENT).build();
		}
		
		@POST
		@Consumes(MediaType.APPLICATION_JSON)
			public Response adicionarCategoria(List<Category> CategoryList) {
			catServ.addCategory(CategoryList);
		   return Response.status(Response.Status.CREATED).build();
		}
		
		@GET
		@Path("/{CategoryID}")
	    @Produces(RDFMediaType.APPLICATION_JSON_LD)
		public String lerProduto(@PathParam("CategoryID") String catID) {
			return catServ.getCategory(catID);
		}
		
		@DELETE
		@Path("/{CategoryID}")
		public Response deletarProduto(@PathParam("CategoryID") String catID) {
			catServ.deleteCategory(catID);
			return Response.status(Response.Status.NO_CONTENT).build();
		}
		
		@PUT
		@Path("/{CategoryID}")
		@Consumes(MediaType.APPLICATION_JSON)
		public Response alterarCategoria(@PathParam("CategoryID") String catID, Category newCategory) {
			catServ.updateCategory(catID, newCategory);
			return Response.status(Response.Status.CREATED).build();
		}
		
	


}
