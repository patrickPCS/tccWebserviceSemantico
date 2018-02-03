package tcc.iff.rdf.webservice.services;

import java.util.ArrayList;
import java.util.List;

import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;

import tcc.iff.rdf.webservice.connection.Authentication;
import tcc.iff.rdf.webservice.data.Product;

public class ProductServices {
	
	String sparqlEndpoint = "http://localhost:10035/catalogs/CatalogoGR/repositories/RepositorioGR/sparql";
	Authentication auth = new Authentication();
	
	public List<Product> getAllProducts() {
		auth.getAuthentication();

		String q = "PREFIX ex: <http://example.com/>\r\n" + 
				"\r\n" + 
				"SELECT ?s ?p ?o\r\n" + 
				"FROM ex:Produtos\r\n" + 
				"WHERE\r\n" + 
				"  { ?s ?p ?o }";
		
				
		Query query = QueryFactory.create(q);
		QueryExecution qexec = QueryExecutionFactory.sparqlService(sparqlEndpoint, query);
		
		ResultSet results = qexec.execSelect();	
		
		
		List<Product> lista = new ArrayList<>();
		while (results.hasNext()) {
			lista.add((Product) results);
			}
		
		return lista;

		}

}
