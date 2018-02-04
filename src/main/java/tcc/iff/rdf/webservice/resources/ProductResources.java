package tcc.iff.rdf.webservice.resources;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.jena.query.QuerySolution;

import tcc.iff.rdf.webservice.RDFMediaType;
import tcc.iff.rdf.webservice.services.ProductServices;

@Path("/products")
public class ProductResources {
	
ProductServices prodServ = new ProductServices();
	
	@POST
	@Consumes(RDFMediaType.APPLICATION_SPARQL_UPDATE)
	public Response adicionarCategoria(String newProduct) {
	//public Response adicionarCategoria(@PathParam("newCat") String newCat, @Context UriInfo uriInfo) {
		//auth.getAuthentication();
		prodServ.addProduct(newProduct);
		//URI uri = uriInfo.getAbsolutePathBuilder().build();
		/*return Response.created(uri)
				.entity(newCat)
				.build();
		*/
	   return Response.status(Response.Status.CREATED).build();
	}


	@GET
    @Produces(RDFMediaType.APPLICATION_JSON_LD)
    public String listarProdutos() {		
		return prodServ.getAllProducts();
				
    }

}
