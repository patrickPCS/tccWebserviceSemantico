package tcc.iff.rdf.webservice.services;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.ResultSetFormatter;
import org.apache.jena.update.UpdateExecutionFactory;
import org.apache.jena.update.UpdateFactory;
import org.apache.jena.update.UpdateProcessor;
import org.apache.jena.update.UpdateRequest;

import tcc.iff.rdf.webservice.connection.Authentication;


public class ProductServices {
	
	String sparqlEndpoint = "http://localhost:10035/catalogs/CatalogoGR/repositories/RepositorioGR/sparql";
	Authentication auth = new Authentication();
	
	public String getAllProducts() {
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
		
		// write to a ByteArrayOutputStream
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

		ResultSetFormatter.outputAsJSON(outputStream, results);

		// and turn that into a String
		String json = new String(outputStream.toByteArray());
		
		/*
		
		List<QuerySolution> lista = new ArrayList<>();
		while (results.hasNext()) {
			QuerySolution qs = results.next();
			lista.add(qs);
			}
		
		return lista;
*/
		return json;
		}
	
public void addProduct(String newProduct) {
		
		auth.getAuthentication();

			
		String updateQuery = 
				"PREFIX gr: <http://purl.org/goodrelations/v1#>\r\n" + 
				"PREFIX ex: <http://example.com/>\r\n" + 
				"\r\n" + 
				"INSERT DATA\r\n" + 
				"{\r\n" + 
				"  GRAPH ex:Produtos\r\n" + 
				"{ \n"+
				newProduct+ "\n"+
				" }\r\n" + 
				"}\r\n" + 
				";";		
		
		UpdateRequest request = UpdateFactory.create(updateQuery);
		UpdateProcessor up = UpdateExecutionFactory.createRemote(request, sparqlEndpoint);
		up.execute();
		
	}

}
