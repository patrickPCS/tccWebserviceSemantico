package tcc.iff.rdf.webservice.services;

import java.io.ByteArrayOutputStream;


import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
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
	
//@GET All
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
		
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

		ResultSetFormatter.outputAsJSON(outputStream, results);
		
		String json = new String(outputStream.toByteArray());

		return json;
		}

//@GET
public String getProduct(String prodName) {
	auth.getAuthentication();

	String q = "PREFIX ex: <http://example.com/>\r\n" + 
			"\r\n" + 
			"SELECT ?s ?p ?o\r\n" + 
			"FROM ex:Produtos\r\n" + 
			"WHERE\r\n" + 
			"  { ex:"+prodName+" ?p ?o }";
	
			
	Query query = QueryFactory.create(q);
	QueryExecution qexec = QueryExecutionFactory.sparqlService(sparqlEndpoint, query);
	
	ResultSet results = qexec.execSelect();	
	
	ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

	ResultSetFormatter.outputAsJSON(outputStream, results);

	String json = new String(outputStream.toByteArray());

	return json;
	}


//@POST
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

//@PUT
public void updateProduct(String productID, String newProduct) {
	
	auth.getAuthentication();
	
	String queryUpdate = "PREFIX ex: <http://example.com/>\r\n" + 
			"PREFIX gr: <http://purl.org/goodrelations/v1#>\r\n"+
			"DELETE { ex:"+productID+" ?p ?o } WHERE { ex:"+productID+" ?p ?o };\r\n" + 
			"		\r\n" + 
			"INSERT DATA \r\n" + 
			"	{ GRAPH ex:Produtos { "+newProduct+" } }";
	
	UpdateRequest request = UpdateFactory.create(queryUpdate);
	UpdateProcessor up = UpdateExecutionFactory.createRemote(request, sparqlEndpoint);
	up.execute();
}

//DELETE
public void deleteCategory(String prodName) {
	auth.getAuthentication();
	
	String updateQuery = 
			"PREFIX ex: <http://example.com/>\r\n" + 
			"\r\n" + 
			"DELETE { ex:"+prodName+" ?p ?o } WHERE { ex:"+prodName+" ?p ?o }";
					
	UpdateRequest request = UpdateFactory.create(updateQuery);
	UpdateProcessor up = UpdateExecutionFactory.createRemote(request, sparqlEndpoint);
	up.execute();
	
}


}
