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
import tcc.iff.rdf.webservice.model.ProductType;


public class ProductTypesServices {

	String sparqlEndpoint = "http://localhost:10035/catalogs/CatalogoGR/repositories/RepositorioGR/sparql";
	Authentication auth = new Authentication();	

	//@GET All
	public String getAllProductTypes() {
		auth.getAuthentication();

		String querySelect =  
				"PREFIX gr: <http://purl.org/goodrelations/v1#>\r\n" + 
				"PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#>\r\n" + 
				"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\r\n" + 
				"PREFIX owl: <http://www.w3.org/2002/07/owl#>\r\n" + 
				"\r\n" + 
				"SELECT ?ProductTypes \r\n" + 
				"WHERE {?ProductTypes   	 rdf:type			owl:Class;\r\n" + 
				"			 rdfs:subClassOf		gr:ProductOrService;\r\n" + 
				"			 rdfs:subClassOf		<http://schema.org/Product> .}";
		
		Query query = QueryFactory.create(querySelect);
		QueryExecution qexec = QueryExecutionFactory.sparqlService(sparqlEndpoint, query);

		ResultSet results = qexec.execSelect();
		JsonArrayBuilder jsonArrayAdd = Json.createArrayBuilder();
		String p = "ProductTypes";
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
	public String getProductType(String productTypeID) {
		auth.getAuthentication();

		String q = 
				"PREFIX gr: <http://purl.org/goodrelations/v1#>\r\n" + 
				"PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#>\r\n" + 
				"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\r\n" + 
				"PREFIX exp: <http://localhost:8080/webservice/webapi/producttypes/>\r\n" + 
				"PREFIX pto: <http://www.productontology.org/id/>\r\n" + 
				"PREFIX owl: <http://www.w3.org/2002/07/owl#>\r\n" + 
				"\r\n" + 
				"DESCRIBE exp:"+productTypeID+" pto:"+productTypeID+"\r\n" + 
				"WHERE {\r\n" + 
				"			 pto:"+productTypeID+" rdf:type			owl:Class;\r\n" + 
				"			 rdfs:subClassOf		gr:ProductOrService;\r\n" + 
				"			 rdfs:subClassOf		<http://schema.org/Product> .\r\n" + 
				"  \r\n" + 
				"  OPTIONAL { exp:"+productTypeID+"  rdf:type			owl:Class;\r\n" + 
				"			 rdfs:subClassOf		gr:ProductOrService;\r\n" + 
				"			 rdfs:subClassOf		<http://schema.org/Product> . }\r\n" + 
				"}";

		Query query = QueryFactory.create(q);
		QueryExecution qexec = QueryExecutionFactory.sparqlService(sparqlEndpoint, query);

		Model results = qexec.execDescribe();	
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		results.write(outputStream, "JSON-LD");
		String output = new String(outputStream.toByteArray());

		return output;
	}

	//@POST
	public String addProductType(List<ProductType> newProductType) {
		auth.getAuthentication();

		int TAM;
		TAM = newProductType.size();
		JsonArrayBuilder jsonArrayAdd = Json.createArrayBuilder();
		String exp = "http://localhost:8080/webservice/webapi/producttypes/";
		for(int i=0; i<TAM; i++) {
			
			String id = newProductType.get(i).getId();
			String label = newProductType.get(i).getLabel();
			String homepage = newProductType.get(i).getHomepage();
			String description = newProductType.get(i).getDescription();
			String language = newProductType.get(i).getLanguage();
			
			String queryUpdate = 
							"PREFIX gr: <http://purl.org/goodrelations/v1#>\r\n" + 
							"PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#>\r\n" + 
							"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\r\n" + 
							"PREFIX lang: <http://www.w3.org/XML/1998/>\r\n" + 
							"PREFIX exp: <http://localhost:8080/webservice/webapi/producttypes/>\r\n" +
							"PREFIX foaf: <http://xmlns.com/foaf/0.1/> \r\n" + 
							"PREFIX owl: <http://www.w3.org/2002/07/owl#>\r\n" + 
							"\r\n" + 
							"\r\n" + 
							"INSERT DATA\r\n" + 
							"{ \r\n" + 
							"  exp:"+id+"	  	 rdf:type			owl:Class;\r\n" + 
							"			 rdfs:subClassOf		gr:ProductOrService;\r\n" + 
							"			 rdfs:subClassOf		<http://schema.org/Product>;\r\n" + 
							"			 rdf:type			<http://www.productontology.org/>;\r\n" + 
							"			 rdfs:label			'"+label+"';\r\n" + 
							"			 foaf:homepage			'"+homepage+"';\r\n" + 
							"			 gr:description			'"+description+"';\r\n" + 
							"			 lang:namespacelang		'"+language+"' .\r\n" + 
							"			\r\n" + 
							"}		\r\n" + 
							"";

			UpdateRequest request = UpdateFactory.create(queryUpdate);
			UpdateProcessor up = UpdateExecutionFactory.createRemote(request, sparqlEndpoint);
			up.execute();
			
			jsonArrayAdd.add(exp+id);
			
		}
		JsonArray ja = jsonArrayAdd.build();
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		JsonWriter writer = Json.createWriter(outputStream);
		writer.writeArray(ja);
		String output = new String(outputStream.toByteArray());
		return output;
	}

	//@PUT
	public void updateProductType(String oldProductID, ProductType newProductType) {

		auth.getAuthentication();
		
		String id = newProductType.getId();
		String label = newProductType.getLabel();
		String homepage = newProductType.getHomepage();
		String description = newProductType.getDescription();
		String language = newProductType.getLanguage();

		String queryUpdate = 
				"PREFIX gr: <http://purl.org/goodrelations/v1#>\r\n" + 
				"PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#>\r\n" + 
				"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\r\n" + 
				"PREFIX lang: <http://www.w3.org/XML/1998/>\r\n" + 
				"PREFIX exp: <http://localhost:8080/webservice/webapi/producttypes/>\r\n" +
				"PREFIX foaf: <http://xmlns.com/foaf/0.1/> \r\n" + 
				"PREFIX owl: <http://www.w3.org/2002/07/owl#>\r\n" + 
				"\r\n" + 
				"DELETE \r\n" + 
				"	{ exp:"+oldProductID+" ?p ?s }\r\n" + 
				"WHERE\r\n" + 
				"{ \r\n" + 
				"  exp:"+oldProductID+"	  	 ?p ?s;" +
			    "rdf:type			owl:Class;\r\n" + 
				"			 rdfs:subClassOf		gr:ProductOrService;\r\n" + 
				"			 rdfs:subClassOf		<http://schema.org/Product> .\r\n" + 
				"};\r\n" + 
				"\r\n" + 
				"INSERT DATA\r\n" + 
				"{ \r\n" + 
				"  exp:"+id+"	  	 rdf:type			owl:Class;\r\n" + 
				"			 rdfs:subClassOf		gr:ProductOrService;\r\n" + 
				"			 rdfs:subClassOf		<http://schema.org/Product>;\r\n" + 
				"			 rdf:type			<http://www.productontology.org/>;\r\n" + 
				"			 rdfs:label			'"+label+"';\r\n" + 
				"			 foaf:homepage			'"+homepage+"';\r\n" + 
				"			 gr:description			'"+description+"';\r\n" + 
				"			 lang:namespacelang		'"+language+"' .\r\n" + 
				"			\r\n" + 
				"}";

		UpdateRequest request = UpdateFactory.create(queryUpdate);
		UpdateProcessor up = UpdateExecutionFactory.createRemote(request, sparqlEndpoint);
		up.execute();
	}

	//DELETE
	public void deleteProductType(String productID) {
		auth.getAuthentication();

		String updateQuery = 
						"PREFIX gr: <http://purl.org/goodrelations/v1#>\r\n" + 
						"PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#>\r\n" + 
						"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\r\n" + 
						"PREFIX lang: <http://www.w3.org/XML/1998/>\r\n" + 
						"PREFIX exp: <http://localhost:8080/webservice/webapi/producttypes/>\r\n" +
						"PREFIX foaf: <http://xmlns.com/foaf/0.1/> \r\n" + 
						"PREFIX owl: <http://www.w3.org/2002/07/owl#>\r\n" + 
						"\r\n" + 
						"DELETE \r\n" + 
						"	{ exp:"+productID+" ?p ?s }\r\n" + 
						"WHERE\r\n" + 
						"{ \r\n" + 
						"  exp:"+productID+"	  	?p ?s;" +
						" rdf:type			owl:Class;\r\n" + 
						"			 rdfs:subClassOf		gr:ProductOrService;\r\n" + 
						"			 rdfs:subClassOf		<http://schema.org/Product> .\r\n" + 
						"}";

		UpdateRequest request = UpdateFactory.create(updateQuery);
		UpdateProcessor up = UpdateExecutionFactory.createRemote(request, sparqlEndpoint);
		up.execute();

	}
	
	//DELETE ALL 
	public void deleteAllProductsTypes() {
		auth.getAuthentication();
		
		String updateQuery = 
				"PREFIX gr: <http://purl.org/goodrelations/v1#>\r\n" + 
						"PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#>\r\n" + 
						"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\r\n" + 
						"PREFIX lang: <http://www.w3.org/XML/1998/>\r\n" + 
						"PREFIX exp: <http://localhost:8080/webservice/webapi/producttypes/>\r\n" +
						"PREFIX foaf: <http://xmlns.com/foaf/0.1/> \r\n" + 
						"PREFIX owl: <http://www.w3.org/2002/07/owl#>\r\n" + 
						"\r\n" + 
						"DELETE \r\n" + 
						"	{ ?productType ?p ?s }\r\n" + 
						"WHERE\r\n" + 
						"{ \r\n" + 
						"  ?productType	  	 ?p			?s;" + 
					    "			 rdf:type			owl:Class;\r\n" + 
						"			 rdfs:subClassOf		gr:ProductOrService;\r\n" + 
						"			 rdfs:subClassOf		<http://schema.org/Product> .\r\n" + 
						"}";
		
		UpdateRequest request = UpdateFactory.create(updateQuery);
		UpdateProcessor up = UpdateExecutionFactory.createRemote(request, sparqlEndpoint);
		up.execute();
	}

	public String getOffersToProducts(String productID) {
		auth.getAuthentication();
		
		String querySelect = "PREFIX gr: <http://purl.org/goodrelations/v1#>\r\n" + 
				"PREFIX exp: <http://localhost:8080/webservice/webapi/producttypes/>\r\n" +
				"PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#>\r\n" +
				"PREFIX owl: <http://www.w3.org/2002/07/owl#>\r\n" + 
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
