package tcc.iff.rdf.webservice.services;

import java.io.ByteArrayOutputStream;
import java.util.List;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonWriter;

import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.ResultSet;
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
				"PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#>\r\n" + 
		
				"\r\n" + 
				"SELECT ?Products WHERE {?Products	rdf:type	gr:SomeItems}";
		
		Query query = QueryFactory.create(querySelect);
		QueryExecution qexec = QueryExecutionFactory.sparqlService(sparqlEndpoint, query);

		ResultSet results = qexec.execSelect();
		JsonArrayBuilder jsonArrayAdd = Json.createArrayBuilder();
		String p = "Products";
		while(results.hasNext()) {
		jsonArrayAdd.add(results.nextSolution().getResource(p).getURI());
		}
		JsonArray ja = jsonArrayAdd.build();
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		JsonWriter writer = Json.createWriter(outputStream);
		writer.writeArray(ja);
		String output = new String(outputStream.toByteArray());

		return output;
	}

	//@GET
	public String getProduct(String productID) {
		auth.getAuthentication();

		String q = 
				"PREFIX exp: <http://localhost:8080/webservice/webapi/products/>\r\n" + 
				"\r\n" + 
				"DESCRIBE exp:"+productID+"";

		Query query = QueryFactory.create(q);
		QueryExecution qexec = QueryExecutionFactory.sparqlService(sparqlEndpoint, query);

		Model results = qexec.execDescribe();	
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		results.write(outputStream, "JSON-LD");
		String output = new String(outputStream.toByteArray());

		return output;
	}

	//@POST
	public void addProduct(List<Product> newProduct) {
		auth.getAuthentication();

		int TAM;
		TAM = newProduct.size();
		for(int i=0; i<TAM; i++) {

			String productURI = newProduct.get(i).getProductURI();
			String productID = newProduct.get(i).getproductID();
			String name = newProduct.get(i).getName();
			String description = newProduct.get(i).getDescription();

			String queryUpdate = 
							"PREFIX gr: <http://purl.org/goodrelations/v1#>\r\n" + 
							"PREFIX exp: <http://localhost:8080/webservice/webapi/products/>\r\n" + 
							"PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#>\r\n" +
							"PREFIX foaf: <http://xmlns.com/foaf/0.1/> \r\n" + 
							"\r\n" + 
							"INSERT DATA\r\n" + 
							"{ \r\n" + 
							"  exp:"+productID+"	rdf:type	gr:SomeItems;\r\n" + 
							"                	 gr:name	   		'"+name+"';\r\n" + 
							"               	 gr:description		'"+description+"';\r\n" + 
							"               	 foaf:page			<"+productURI+"> .\r\n" + 
							"}";

			UpdateRequest request = UpdateFactory.create(queryUpdate);
			UpdateProcessor up = UpdateExecutionFactory.createRemote(request, sparqlEndpoint);
			up.execute();
		}
	}

	//@PUT
	public void updateProduct(String oldProductID, Product newProduct) {

		auth.getAuthentication();

		String productURI = newProduct.getProductURI();
		String productID = newProduct.getproductID();
		String name = newProduct.getName();
		String description = newProduct.getDescription();

		String queryUpdate = 
				"PREFIX gr: <http://purl.org/goodrelations/v1#>\r\n" + 
				"PREFIX exp: <http://localhost:8080/webservice/webapi/products/>\r\n" + 
				"PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#>\r\n" +
				"PREFIX foaf: <http://xmlns.com/foaf/0.1/> \r\n" + 
				"\r\n" + 
				"DELETE \r\n" + 
				"	{ exp:"+oldProductID+" ?p ?s }\r\n" + 
				"WHERE\r\n" + 
				"{ \r\n" + 
				"  exp:"+oldProductID+"   ?p ?s; \r\n" + 
				"                rdf:type	gr:SomeItems	.\r\n" + 
				"                \r\n" + 
				"};\r\n" + 
				"\r\n" + 
				"INSERT DATA\r\n" + 
				"{ \r\n" + 
				"  exp:"+productID+"	rdf:type	gr:SomeItems;\r\n" + 
				"                	 gr:name	   		'"+name+"';\r\n" + 
				"               	 gr:description		'"+description+"';\r\n" + 
				"               	 foaf:page			<"+productURI+"> .\r\n" + 
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
						"PREFIX exp: <http://localhost:8080/webservice/webapi/products/>\r\n" + 
						"PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#>\r\n" +
						"PREFIX foaf: <http://xmlns.com/foaf/0.1/> \r\n" + 
						"\r\n" + 
						"DELETE \r\n" + 
						"	{ exp:"+productID+" ?p ?s }\r\n" + 
						"WHERE\r\n" + 
						"{ \r\n" + 
						"  exp:"+productID+"   ?p ?s; \r\n" + 
						"                rdf:type	gr:SomeItems	.\r\n" + 
						"                \r\n" + 
						"}";

		UpdateRequest request = UpdateFactory.create(updateQuery);
		UpdateProcessor up = UpdateExecutionFactory.createRemote(request, sparqlEndpoint);
		up.execute();

	}
	
	//DELETE ALL 
	public void deleteAllProducts() {
		auth.getAuthentication();
		
		String updateQuery = 
				"PREFIX gr: <http://purl.org/goodrelations/v1#>\r\n" + 
				"PREFIX exp: <http://localhost:8080/webservice/webapi/products/>\r\n" + 
				"PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#>\r\n" +
				"PREFIX foaf: <http://xmlns.com/foaf/0.1/> \r\n" + 
				"\r\n" + 
				"DELETE \r\n" + 
				"	{ ?Product ?p ?s }\r\n" + 
				"WHERE\r\n" + 
				"{ \r\n" + 
				"  ?Product   ?p ?s; \r\n" + 
				"             rdf:type	gr:SomeItems .\r\n" + 
				"}";
		
		UpdateRequest request = UpdateFactory.create(updateQuery);
		UpdateProcessor up = UpdateExecutionFactory.createRemote(request, sparqlEndpoint);
		up.execute();
	}

	public String getOffersToProducts(String productID) {
		auth.getAuthentication();
		
		String querySelect = "PREFIX gr: <http://purl.org/goodrelations/v1#>\r\n" + 
				"PREFIX exp: <http://localhost:8080/webservice/webapi/products/>\r\n" + 
				"PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#>\r\n" +
				"\r\n" + 
				"SELECT ?Companies	?Prices \r\n" + 
				"WHERE { ?Companies 			gr:includes		exp:"+productID+";\r\n" + 
				"      						gr:hasPriceSpecification\r\n" + 
				"				        [rdf:type gr:UnitPriceSpecification ;\r\n" + 
				"				           gr:hasCurrencyValue		?Prices ]}\r\n" + 
				"ORDER BY(?Prices)";
		
		Query query = QueryFactory.create(querySelect);
		QueryExecution qexec = QueryExecutionFactory.sparqlService(sparqlEndpoint, query);

		ResultSet results = qexec.execSelect();
		JsonArrayBuilder jsonArrayAdd = Json.createArrayBuilder();
		String c = "Companies";
		String p = "Prices";
		while(results.hasNext()) {
		jsonArrayAdd.add(results.nextSolution().getResource(c).getURI())
					.add(results.nextSolution().getLiteral(p).toString());
		}
		JsonArray ja = jsonArrayAdd.build();
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		JsonWriter writer = Json.createWriter(outputStream);
		writer.writeArray(ja);
		String output = new String(outputStream.toByteArray());

		return output;
	}
}
