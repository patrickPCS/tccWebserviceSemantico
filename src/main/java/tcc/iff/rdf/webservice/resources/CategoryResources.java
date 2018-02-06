package tcc.iff.rdf.webservice.resources;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;

@Path("/")
public class CategoryResources {

	@GET
	public String getAllCategories() {
		return "Método getAllCategories ok";
	}
	
	@DELETE
	public String deleteAllCategories() {
		return "Método deleteAllCategories ok";
	}
	
	@POST
	public String addCategory() {
		return "Método addCategory ok";
	}
	
	@GET 
	@Path("/{CategoryID}")
	public String getCategory() {
		return "Método getCategory ok";
	}
	
	@DELETE 
	@Path("/{CategoryID}")
	public String deleteCategory() {
		return "Método deleteCategory ok";
	}
	
	@PUT
	@Path("/{CategoryID}")
	public String updateCategory() {
		return "Método updateCategory ok";
	}
	
	
/********************************************************************************/	
	
	
	@Path("/{CategoryID}/products")
	public ProductResources get1ProductResources() {
		return new ProductResources();
	}
	
	@Path("/products")
	public ProductResources get2ProductResources() {
		return new ProductResources();
	}
	
}
