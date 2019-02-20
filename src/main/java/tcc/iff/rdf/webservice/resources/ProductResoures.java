package tcc.iff.rdf.webservice.resources;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import tcc.iff.rdf.webservice.model.Product;
import tcc.iff.rdf.webservice.services.ProductServices;

@Path("/products")
public class ProductResoures {
	
	ProductServices product = new ProductServices();
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String listarProdutos() {
		return product.getAllProducts(); 
	}
	
	@GET
	@Path("/{productID}")
	@Produces({	"application/json", 
    	"application/ld+json",
    	"application/n-triples",
    	"application/rdf+xml",
    	"application/turtle",
    	"application/rdf+json"
    	})
	public Response listarProduto(@PathParam("productID") String productID,  @HeaderParam("Accept") String accept) {
		return product.getProduct(productID, accept);
	}
	
	@GET 
	@Path("/{ProductID}/offers")
	@Produces({"application/json", 
	    	"application/ld+json",
	    	"application/n-triples",
	    	"application/rdf+xml",
	    	"application/turtle",
	    	"application/rdf+json"
	    	})
    public Response listarOfertasParaProdutos(@PathParam("ProductID") String productID, @HeaderParam("Accept") String accept) {		
		return product.getOffersToProducts(productID, accept);		
    }
	@DELETE
	@Path("/{productID}")
	public Response deletarEmpresa(@PathParam("productID") String productID) {
		product.deleteCompany(productID);
		return Response.status(Response.Status.NO_CONTENT).build();
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
	public Response AdicionarProduto(List<Product> productList) {
		return product.addProduct(productList);
	}
	
	@PUT
	@Path("/{productID}")
	@Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
	public Response alterarProduto(@PathParam("productID") String productID,  Product newProduct) {
		return product.updateProduct(productID, newProduct);
		
	}
	
}
