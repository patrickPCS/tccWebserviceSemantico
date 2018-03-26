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
import tcc.iff.rdf.webservice.model.ProductType;
import tcc.iff.rdf.webservice.services.ProductTypesServices;

@Path("/producttypes")
public class ProductTypeResources {
		
	ProductTypesServices prodServ = new ProductTypesServices();
	@GET
    @Produces(MediaType.APPLICATION_JSON)
    public String listarProdutos() {		
		return prodServ.getAllProductTypes();		
    }
	
	@GET
	@Path("/{ProductID}/offers")
    @Produces(MediaType.APPLICATION_JSON)
    public String listarOfertasParaProdutos(@PathParam("ProductID") String productID) {		
		return prodServ.getOffersToProducts(productID);		
    }
	
	@DELETE
	public Response deletarProdutos() {
		prodServ.deleteAllProductsTypes();
		return Response.status(Response.Status.NO_CONTENT).build();
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
		public Response adicionarProduto(List<ProductType> ProductList) {
		return prodServ.addProductType(ProductList);
	}
	
	@GET
	@Path("/{ProductID}")
    @Produces(RDFMediaType.APPLICATION_JSON_LD)
	public String lerProduto(@PathParam("ProductID") String prodName) {
		return prodServ.getProductType(prodName);
	}
	
	@DELETE
	@Path("/{ProductID}")
	public Response deletarProduto(@PathParam("ProductID") String prodName) {
		prodServ.deleteProductType(prodName);
		return Response.status(Response.Status.NO_CONTENT).build();
	}
	
	@PUT
	@Path("/{ProductID}")
	@Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
	public Response alterarProduto(@PathParam("ProductID") String prodID, ProductType newProduct) {
		return prodServ.updateProductType(prodID, newProduct);
	}
	
}
