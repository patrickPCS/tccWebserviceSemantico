package tcc.iff.rdf.webservice.resources;


import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;


import tcc.iff.rdf.webservice.RDFMediaType;
import tcc.iff.rdf.webservice.services.ProductServices;

@Path("/products")
public class ProductResources {
	
ProductServices prodServ = new ProductServices();

	@PUT
	@Path("{prodName}")
	@Consumes(RDFMediaType.APPLICATION_SPARQL_UPDATE)
	public Response alterarProduto(@PathParam("prodName") String prodID, String newProduct) {
		prodServ.updateProduct(prodID, newProduct);
		return Response.status(Response.Status.CREATED).build();
	}
	
	@POST
	@Consumes(RDFMediaType.APPLICATION_SPARQL_UPDATE)
	public Response adicionarProduto(String newProduct) {
	   prodServ.addProduct(newProduct);
	   return Response.status(Response.Status.CREATED).build();
	}
	
	@DELETE
	@Path("{prodName}")
	public Response deletarProduto(@PathParam("prodName") String prodName) {
		prodServ.deleteCategory(prodName);
		return Response.status(Response.Status.NO_CONTENT).build();
	}
	
	@GET
	@Path("/{prodName}")
	@Produces(RDFMediaType.APPLICATION_JSON_LD)
	public String lerProduto(@PathParam("prodName") String prodName) {
		return prodServ.getProduct(prodName);
	}


	@GET
    @Produces(RDFMediaType.APPLICATION_JSON_LD)
    public String listarProdutos() {		
		return prodServ.getAllProducts();
				
    }

}
