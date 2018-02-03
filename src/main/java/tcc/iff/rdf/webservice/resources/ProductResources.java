package tcc.iff.rdf.webservice.resources;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.jena.query.QuerySolution;

import tcc.iff.rdf.webservice.RDFMediaType;
import tcc.iff.rdf.webservice.data.Product;
import tcc.iff.rdf.webservice.services.ProductServices;

@Path("/products")
public class ProductResources {
	
ProductServices prodServ = new ProductServices();
	
	@GET
    @Produces(RDFMediaType.APPLICATION_RDFXML)
    public Object listarCategorias() {		
		return prodServ.getAllProducts().toString();
				
    }

}
