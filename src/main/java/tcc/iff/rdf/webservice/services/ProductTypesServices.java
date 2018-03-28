package tcc.iff.rdf.webservice.services;

import java.io.ByteArrayOutputStream;
import java.util.List;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonWriter;
import javax.ws.rs.core.Response;

import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
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
	public Response getProductType(String productTypeID) {
		auth.getAuthentication();
		
		String queryDescribe = 
				"PREFIX exp: <http://localhost:8080/webservice/webapi/producttypes/> \r\n" + 
				"PREFIX pto: <http://www.productontology.org/id/>\r\n" + 
				"\r\n" + 
				"DESCRIBE exp:"+productTypeID+" pto:"+productTypeID+"\r\n" + 
				"WHERE\r\n" + 
				"{\r\n" + 
				"OPTIONAL { exp:"+productTypeID+" ?p ?o . }\r\n"+
				"OPTIONAL { pto:"+productTypeID+" ?p ?o . }\r\n"+
				"}";

		Query qr = QueryFactory.create(queryDescribe);
		QueryExecution qx = QueryExecutionFactory.sparqlService(sparqlEndpoint, qr);

		Model rst = qx.execDescribe();
		if (rst.isEmpty()) {
			return Response.status(422)
					.entity("Please, choose a valid ProductType ID.")
					.build();
		}
		
		String q = 
				"PREFIX exp: <http://localhost:8080/webservice/webapi/producttypes/>\r\n" + 
				"PREFIX gr: <http://purl.org/goodrelations/v1#>\r\n" + 
				"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\r\n" + 
				"PREFIX lang: <http://www.w3.org/XML/1998/>\r\n" + 
				"PREFIX foaf: <http://xmlns.com/foaf/0.1/>\r\n" + 
				"PREFIX pto: <http://www.productontology.org/id/>\r\n" + 
				"\r\n" + 
				"SELECT ?label ?homepage ?language ?description ?subClassOf\r\n" + 
				"\r\n" + 
				"WHERE{ \r\n" + 
				"  \r\n" + 
				"  OPTIONAL{\r\n" + 
				"  	exp:"+productTypeID+" 	  rdfs:label	?label;\r\n" + 
				"           		  foaf:homepage		?homepage;\r\n" + 
				"          	   	  lang:namespacelang	  ?language;\r\n" + 
				"          	      gr:description	?description .\r\n" + 
				"    \r\n" + 
				" 			OPTIONAL {  exp:"+productTypeID+" rdfs:subClassOf	?subClassOf . FILTER regex(str(?subClassOf), 'http://www.productontology.org/id/')}\r\n" + 
				" 			OPTIONAL {  exp:"+productTypeID+" rdfs:subClassOf	?subClassOf . FILTER regex(str(?subClassOf), '/producttypes/')}\r\n" + 
				" 			OPTIONAL {  exp:"+productTypeID+" rdfs:subClassOf	?subClassOf . FILTER regex(str(?subClassOf), '/goodrelations/')}\r\n" + 
				"  \r\n" + 
				" }\r\n" + 
				" \r\n" + 
				"   OPTIONAL {\r\n" + 
				"    pto:"+productTypeID+"	  rdfs:label	?label;\r\n" + 
				"           		  foaf:homepage		?homepage;\r\n" + 
				"          	   	  lang:namespacelang	  ?language;\r\n" + 
				"          	      rdfs:label		?description .\r\n" + 
				"          \r\n" + 
				"  		OPTIONAL {  pto:"+productTypeID+" rdfs:subClassOf	?subClassOf . FILTER regex(str(?subClassOf), '/goodrelations/')}\r\n" + 
				"}\r\n" + 
				"   \r\n" + 
				"}";
		
		
		Query query = QueryFactory.create(q);
		QueryExecution qexec = QueryExecutionFactory.sparqlService(sparqlEndpoint, query);
		ResultSet results = qexec.execSelect();
		
		  QuerySolution soln = results.nextSolution() ;
	      String label = soln.getLiteral("label").toString() ;  
	      String homepage = soln.getResource("homepage").toString() ;
	      String language = soln.getLiteral("language").toString() ;
	      String description = soln.getLiteral("description").toString() ;
	      String subClassOf = soln.getResource("subClassOf").toString() ;
	    
			JsonObject jobj = Json.createObjectBuilder()
					.add("id", productTypeID)
					.add("label",label)
					.add("homepage",homepage)
					.add("language",language)
					.add("description",description)
					.add("subClassOf", subClassOf)
					.build();
		
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			JsonWriter writer = Json.createWriter(outputStream);
			writer.writeObject(jobj);
			String output = new String(outputStream.toByteArray());
			writer.close();
		
			return Response.status(Response.Status.OK)
					.entity(output)
					.build();
	}

	//@POST
	public Response addProductType(List<ProductType> newProductType) {
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
			String subClassOf = newProductType.get(i).getSubClassOf();
			String superClassURI = "exp:"+subClassOf+"";

			if (subClassOf.isEmpty()) {
				return Response.status(422)
						.entity("The 'subClassOf' field is required! If you don't know which Class this"
								+ " belongs to, then fill it with 'ProductOrService' and submit again.")
						.build();
			}else
				if(subClassOf.contentEquals("ProductOrService")) {
					superClassURI = "gr:ProductOrService";		
				}else {

					String queryDescribe = 
							"PREFIX exp: <http://localhost:8080/webservice/webapi/producttypes/> \r\n" + 

							"\r\n" + 
							"DESCRIBE exp:"+subClassOf+"\r\n" + 
							"WHERE\r\n" + 
							"{\r\n" + 
							"exp:"+subClassOf+" ?p ?o \r\n"+
							"}";

					Query query = QueryFactory.create(queryDescribe);
					QueryExecution qexec = QueryExecutionFactory.sparqlService(sparqlEndpoint, query);

					Model results = qexec.execDescribe();
					if (results.isEmpty()) {
						superClassURI = "pto:"+subClassOf+"";	
						String queryDescribe2 = 
								"PREFIX pto: <http://www.productontology.org/id/>\r\n" + 
										"\r\n" + 
										"DESCRIBE pto:"+subClassOf+"\r\n" + 
										"WHERE\r\n" + 
										"{\r\n" + 
										"pto:"+subClassOf+" ?p ?o .\r\n" + 
										"}";

						Query query2 = QueryFactory.create(queryDescribe2);
						QueryExecution qexec2 = QueryExecutionFactory.sparqlService(sparqlEndpoint, query2);

						Model results2 = qexec2.execDescribe();
						if (results2.isEmpty()) {
								
							return Response.status(422)
									.entity("Please, fill the 'subClassOf' field with a valid Class ID! "
											+ "If you don't know which Class this belongs to, then fill it with 'ProductOrService' and submit again.")
									.build();
						}
					}

				}


			String qD = "PREFIX exp: <http://localhost:8080/webservice/webapi/producttypes/> \r\n" + 
					"\r\n" + 
					"DESCRIBE exp:"+id+"\r\n" + 
					"WHERE\r\n" + 
					"{\r\n" + 
					"exp:"+id+" ?p ?o .\r\n" + 
					"}";

			Query q = QueryFactory.create(qD);
			QueryExecution qex = QueryExecutionFactory.sparqlService(sparqlEndpoint, q);

			Model resultados = qex.execDescribe();
			if (resultados.isEmpty()) {

				String queryUpdate = 
						"PREFIX gr: <http://purl.org/goodrelations/v1#>\r\n" + 
								"PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#>\r\n" + 
								"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\r\n" + 
								"PREFIX pto: <http://www.productontology.org/id/>\r\n" + 
								"PREFIX lang: <http://www.w3.org/XML/1998/>\r\n" + 
								"PREFIX exp: <http://localhost:8080/webservice/webapi/producttypes/>\r\n" +
								"PREFIX foaf: <http://xmlns.com/foaf/0.1/> \r\n" + 
								"PREFIX owl: <http://www.w3.org/2002/07/owl#>\r\n" + 
								"\r\n" + 
								"\r\n" + 
								"INSERT DATA\r\n" + 
								"{ \r\n" + 
								"  exp:"+id+"	  	 rdf:type			owl:Class;\r\n" + 
								"			 rdfs:subClassOf		"+superClassURI+";\r\n" + 
								"			 rdfs:subClassOf		<http://schema.org/Product>;\r\n" + 
								"			 rdf:type			<http://www.productontology.org/>;\r\n" + 
								"			 rdfs:label			'"+label+"';\r\n" + 
								"			 foaf:homepage			<"+homepage+">;\r\n" + 
								"			 gr:description			'"+description+"';\r\n" + 
								"			 lang:namespacelang		'"+language+"' .\r\n" + 
								"			\r\n" + 
								"}		\r\n" + 
								"";

				UpdateRequest request = UpdateFactory.create(queryUpdate);
				UpdateProcessor up = UpdateExecutionFactory.createRemote(request, sparqlEndpoint);
				up.execute();

				jsonArrayAdd.add(exp+id);

			}else
			{
				String message = "CONFLICT: the ProductType "+id+" already exists: "+exp+id;
				ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
				String output = new String(outputStream.toByteArray());
				output = message;
				return Response.status(Response.Status.CONFLICT)
						.entity(output)
						.build();
			}
		}
		JsonArray ja = jsonArrayAdd.build();
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		JsonWriter writer = Json.createWriter(outputStream);
		writer.writeArray(ja);
		String output = new String(outputStream.toByteArray());
		return Response.status(Response.Status.CREATED).
				entity(output)
				.build();	
	}

	//@PUT
	public Response updateProductType(String oldProductID, ProductType newProductType) {

		auth.getAuthentication();

		String exp = "http://localhost:8080/webservice/webapi/producttypes/";
		String id = newProductType.getId();
		String label = newProductType.getLabel();
		String homepage = newProductType.getHomepage();
		String description = newProductType.getDescription();
		String language = newProductType.getLanguage();
		String subClassOf = newProductType.getSubClassOf();
		String superClassURI = "exp"+subClassOf;

		if (subClassOf.isEmpty()) {
			return Response.status(422)
					.entity("The 'subClassOf' field is required! If you don't know which Class this"
							+ " belongs to, then fill it with 'ProductOrService' and submit again.")
					.build();
		}else
			if(subClassOf.contentEquals("ProductOrService")) {
				superClassURI = "gr:ProductOrService";		
			}else {

				String queryDescribe = 
						"PREFIX exp: <http://localhost:8080/webservice/webapi/producttypes/> \r\n" + 

						"\r\n" + 
						"DESCRIBE exp:"+subClassOf+"\r\n" + 
						"WHERE\r\n" + 
						"{\r\n" + 
						"exp:"+subClassOf+" ?p ?o \r\n"+
						"}";

				Query query = QueryFactory.create(queryDescribe);
				QueryExecution qexec = QueryExecutionFactory.sparqlService(sparqlEndpoint, query);

				Model results = qexec.execDescribe();
				if (results.isEmpty()) {
					superClassURI = "pto:"+subClassOf+"";	
					String queryDescribe2 = 
							"PREFIX pto: <http://www.productontology.org/id/>\r\n" + 
									"\r\n" + 
									"DESCRIBE pto:"+subClassOf+"\r\n" + 
									"WHERE\r\n" + 
									"{\r\n" + 
									"pto:"+subClassOf+" ?p ?o .\r\n" + 
									"}";

					Query query2 = QueryFactory.create(queryDescribe2);
					QueryExecution qexec2 = QueryExecutionFactory.sparqlService(sparqlEndpoint, query2);

					Model results2 = qexec2.execDescribe();
					if (results2.isEmpty()) {
							
						return Response.status(422)
								.entity("Please, fill the 'subClassOf' field with a valid Class ID! "
										+ "If you don't know which Class this belongs to, then fill it with 'ProductOrService' and submit again.")
								.build();
					}
				}

			}

		

		String queryUpdate = 
				"PREFIX gr: <http://purl.org/goodrelations/v1#>\r\n" + 
						"PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#>\r\n" + 
						"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\r\n" + 
						"PREFIX lang: <http://www.w3.org/XML/1998/>\r\n" + 
						"PREFIX exp: <http://localhost:8080/webservice/webapi/producttypes/>\r\n" +
						"PREFIX pto: <http://www.productontology.org/id/>\r\n" + 
						"PREFIX foaf: <http://xmlns.com/foaf/0.1/> \r\n" + 
						"PREFIX owl: <http://www.w3.org/2002/07/owl#>\r\n" + 
						"\r\n" + 
						"DELETE \r\n" + 
						"	{ exp:"+oldProductID+" ?p ?s }\r\n" + 
						"WHERE\r\n" + 
						"{ \r\n" + 
						"  exp:"+oldProductID+"	  	 ?p ?s;" +
						"rdf:type			owl:Class;\r\n" + 
						"			 rdfs:subClassOf		<http://schema.org/Product> .\r\n" + 
						"};\r\n" + 
						"\r\n" + 
						"INSERT DATA\r\n" + 
						"{ \r\n" + 
						"  exp:"+id+"	  	 rdf:type			owl:Class;\r\n" + 
						"			 rdfs:subClassOf		"+superClassURI+";\r\n" + 
						"			 rdfs:subClassOf		<http://schema.org/Product>;\r\n" + 
						"			 rdf:type			<http://www.productontology.org/>;\r\n" + 
						"			 rdfs:label			'"+label+"';\r\n" + 
						"			 foaf:homepage			<"+homepage+">;\r\n" + 
						"			 gr:description			'"+description+"';\r\n" + 
						"			 lang:namespacelang		'"+language+"' .\r\n" + 
						"			\r\n" + 
						"}";

		UpdateRequest request = UpdateFactory.create(queryUpdate);
		UpdateProcessor up = UpdateExecutionFactory.createRemote(request, sparqlEndpoint);
		up.execute();

		JsonArrayBuilder jsonArrayAdd = Json.createArrayBuilder();
		jsonArrayAdd.add(exp+id);
		JsonArray ja = jsonArrayAdd.build();
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		JsonWriter writer = Json.createWriter(outputStream);
		writer.writeArray(ja);
		String output = new String(outputStream.toByteArray());
		return Response.status(Response.Status.CREATED)
				.entity(output)
				.build();	
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
