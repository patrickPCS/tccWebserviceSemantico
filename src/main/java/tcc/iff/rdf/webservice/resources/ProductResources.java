package tcc.iff.rdf.webservice.resources;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import tcc.iff.rdf.webservice.RDFMediaType;
import tcc.iff.rdf.webservice.data.Product;
import tcc.iff.rdf.webservice.services.ProductServices;

@Path("/produtos")
public class ProductResources {
	
ProductServices prodServ = new ProductServices();
	
	@GET
    @Produces(RDFMediaType.APPLICATION_JSON_LD)
    public List<Product>  listarCategorias() {		
		return prodServ.getAllProducts();
				
    }

}
