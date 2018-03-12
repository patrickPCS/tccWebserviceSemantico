package tcc.iff.rdf.webservice.resources;


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
import tcc.iff.rdf.webservice.model.Product;
import tcc.iff.rdf.webservice.services.ProductServices;

@Path("/products")
public class ProductResources {
	
	ProductServices prodServ = new ProductServices();
	@GET
    @Produces(RDFMediaType.APPLICATION_JSON_LD)
    public String listarProdutos() {		
		return prodServ.getAllProducts();		
    }
	
	@DELETE
	public Response deletarProdutos() {
		prodServ.deleteAllProducts();
		return Response.status(Response.Status.NO_CONTENT).build();
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	//public Response adicionarProduto(@PathParam("CategoryID") String category, Product newProduct) {
	public Response adicionarProduto(Product newProduct) {
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
	@Path("/{ProductID}")
	public Response deletarProduto(@PathParam("ProductID") String prodName) {
		prodServ.deleteProduct(prodName);
		return Response.status(Response.Status.NO_CONTENT).build();
	}
	
	@PUT
	@Path("/{ProductID}")
	@Consumes(RDFMediaType.APPLICATION_SPARQL_UPDATE)
	public Response alterarProduto(@PathParam("ProductID") String prodID, Product newProduct) {
		prodServ.updateProduct(prodID, newProduct);
		return Response.status(Response.Status.CREATED).build();
	}
	
}
