package tcc.iff.rdf.webservice.services;

import java.io.ByteArrayOutputStream;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.ResultSetFormatter;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.update.UpdateExecutionFactory;
import org.apache.jena.update.UpdateFactory;
import org.apache.jena.update.UpdateProcessor;
import org.apache.jena.update.UpdateRequest;

import tcc.iff.rdf.webservice.connection.Authentication;
import tcc.iff.rdf.webservice.model.Product;


public class ProductServices {

	String sparqlEndpoint = "http://localhost:10035/catalogs/CatalogoGR/repositories/RepositorioGR/sparql";
	Authentication auth = new Authentication();	

	//@GET All
	public String getAllProducts() {
		auth.getAuthentication();

		String querySelect = "PREFIX gr: <http://purl.org/goodrelations/v1#>\r\n" + 
				"\r\n" + 
				"SELECT ?ProductURI WHERE {?ProductURI	rdfs:subClassOf	gr:ProductOrService}";

		Query query = QueryFactory.create(querySelect);
		QueryExecution qexec = QueryExecutionFactory.sparqlService(sparqlEndpoint, query);

		Model results = qexec.execConstruct();
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		results.write(outputStream, "JSON-LD");
		String output = new String(outputStream.toByteArray());

		return output;


	}

	//@GET
	public String getProduct(String prodName) {
		auth.getAuthentication();

		String q = "PREFIX pto: <http://www.productontology.org/id/>\r\n" + 
				"\r\n" + 
				"DESCRIBE ?productURI\r\n" + 
				"WHERE { ?productURI owl:productID pto:"+prodName+" }";


		Query query = QueryFactory.create(q);
		QueryExecution qexec = QueryExecutionFactory.sparqlService(sparqlEndpoint, query);

		ResultSet results = qexec.execSelect();	

		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

		ResultSetFormatter.outputAsJSON(outputStream, results);

		String json = new String(outputStream.toByteArray());

		return json;
	}


	//@POST
	public void addProduct(Product newProduct) {
		auth.getAuthentication();

		String productURI = newProduct.getProductURI();
		String productID = newProduct.getproductID();
		String name = newProduct.getName();
		String description = newProduct.getDescription();

		String queryUpdate = "PREFIX gr: <http://purl.org/goodrelations/v1#>\r\n" + 
				"PREFIX ex: <http://example.com/>\r\n" + 
				"PREFIX pto: <http://www.productontology.org/id/>\r\n" + 
				"\r\n" + 
				"INSERT DATA\r\n" + 
				"{ \r\n" + 
				"  "+productURI+"    rdfs:subClassOf		gr:ProductOrService;\r\n" + 
				"                owl:sameAs			pto:"+productID+";\r\n" + 
				"                gr:name			"+name+";\r\n" + 
				"                gr:description		"+description+"	.\r\n" + 
				"                \r\n" + 
				"}";

		UpdateRequest request = UpdateFactory.create(queryUpdate);
		UpdateProcessor up = UpdateExecutionFactory.createRemote(request, sparqlEndpoint);
		up.execute();

	}

	//@PUT
	public void updateProduct(String oldProductID, Product newProduct) {

		auth.getAuthentication();

		String productURI = newProduct.getProductURI();
		String productID = newProduct.getproductID();
		String name = newProduct.getName();
		String description = newProduct.getDescription();

		String queryUpdate = "PREFIX gr: <http://purl.org/goodrelations/v1#>\r\n" + 
				"PREFIX ex: <http://example.com/>\r\n" + 
				"PREFIX pto: <http://www.productontology.org/id/>\r\n" + 
				"\r\n" + 
				"DELETE \r\n" + 
				"	{ ?productURL ?p ?s }\r\n" + 
				"WHERE\r\n" + 
				"{ \r\n" + 
				"  ?productURL   ?p ?s; \r\n" + 
				"                owl:sameAs		pto:"+oldProductID+"	.\r\n" + 
				"                \r\n" + 
				"};\r\n" + 
				"\r\n" + 
				"INSERT DATA\r\n" + 
				"{ \r\n" + 
				"  "+productURI+"    rdfs:subClassOf		gr:ProductOrService;\r\n" + 
				"                owl:sameAs			pto:"+productID+";\r\n" + 
				"                gr:name			"+name+";\r\n" + 
				"                gr:description		"+description+"	.\r\n" + 
				"                \r\n" + 
				"}";

		UpdateRequest request = UpdateFactory.create(queryUpdate);
		UpdateProcessor up = UpdateExecutionFactory.createRemote(request, sparqlEndpoint);
		up.execute();
	}

	//DELETE
	public void deleteProduct(String productID) {
		auth.getAuthentication();

		String updateQuery = 
				"PREFIX gr: <http://purl.org/goodrelations/v1#>\r\n" + 
						"PREFIX ex: <http://example.com/>\r\n" + 
						"PREFIX pto: <http://www.productontology.org/id/>\r\n" + 
						"\r\n" + 
						"DELETE \r\n" + 
						"	{ ?productURL ?p ?s }\r\n" + 
						"WHERE\r\n" + 
						"{ \r\n" + 
						"  ?productURL   ?p ?s; \r\n" + 
						"                owl:sameAs		pto:"+productID+"	.\r\n" + 
						"                \r\n" + 
						"};";

		UpdateRequest request = UpdateFactory.create(updateQuery);
		UpdateProcessor up = UpdateExecutionFactory.createRemote(request, sparqlEndpoint);
		up.execute();

	}
	
	//DELETE ALL 
	public void deleteAllProducts() {
		auth.getAuthentication();
		
		String updateQuery = "PREFIX gr: <http://purl.org/goodrelations/v1#>\r\n" + 
				"\r\n" + 
				"DELETE \r\n" + 
				"	{ ?productURL ?p ?s }\r\n" + 
				"WHERE\r\n" + 
				"{ \r\n" + 
				"  ?productURL   ?p ?s; \r\n" + 
				"                rdfs:subClassOf		gr:ProductOrService	.\r\n" + 
				"                \r\n" + 
				"}\r\n" + 
				"\r\n" + 
				"";
		
		UpdateRequest request = UpdateFactory.create(updateQuery);
		UpdateProcessor up = UpdateExecutionFactory.createRemote(request, sparqlEndpoint);
		up.execute();
	}
}
