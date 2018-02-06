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

@Path("/")
public class ProductResources {
	
	ProductServices prodServ = new ProductServices();
	@GET
    @Produces(RDFMediaType.APPLICATION_RDFXML)
    public String listarProdutos() {		
		return prodServ.getAllProducts();		
    }
	
	@DELETE
	public String deletarProdutos() {
		return "Método deletarProdutos ok";
	}
	
	@POST
	@Consumes(RDFMediaType.APPLICATION_SPARQL_UPDATE)
	public Response adicionarProduto(String newProduct) {
	   prodServ.addProduct(newProduct);
	   return Response.status(Response.Status.CREATED).build();
	}
	
	@GET
	@Path("/{ProductID}")
	@Produces(RDFMediaType.APPLICATION_SPARQL_UPDATE)
	public String lerProduto(@PathParam("ProductID") String prodName) {
		return prodServ.getProduct(prodName);
	}
	
	@DELETE
	@Path("{ProductID}")
	public Response deletarProduto(@PathParam("ProductID") String prodName) {
		prodServ.deleteCategory(prodName);
		return Response.status(Response.Status.NO_CONTENT).build();
	}
	
	@PUT
	@Path("{ProductID}")
	@Consumes(RDFMediaType.APPLICATION_SPARQL_UPDATE)
	public Response alterarProduto(@PathParam("ProductID") String prodID, String newProduct) {
		prodServ.updateProduct(prodID, newProduct);
		return Response.status(Response.Status.CREATED).build();
	}

	
/********************************************************************************/	
	
	@Path("/{ProductID}/offers")
	public OfferResources get1OfferResources() {
		return new OfferResources();
	}
	
	@Path("/offers")
	public OfferResources get2OfferResources() {
		return new OfferResources();
	}
	
	

}
