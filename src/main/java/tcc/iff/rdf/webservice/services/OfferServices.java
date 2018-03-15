package tcc.iff.rdf.webservice.services;

import java.io.ByteArrayOutputStream;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonWriter;

import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.ResultSet;
import org.apache.jena.update.UpdateExecutionFactory;
import org.apache.jena.update.UpdateFactory;
import org.apache.jena.update.UpdateProcessor;
import org.apache.jena.update.UpdateRequest;
import tcc.iff.rdf.webservice.connection.Authentication;

public class OfferServices {

	String sparqlEndpoint = "http://localhost:10035/catalogs/CatalogoGR/repositories/RepositorioGR/sparql";
	Authentication auth = new Authentication();	

	//@GET All
	public String getAllOffers() {
		auth.getAuthentication();

		String querySelect = "PREFIX gr: <http://purl.org/goodrelations/v1#>\r\n" + 
				"PREFIX exco:<http://localhost:8080/webservice/webapi/companies/> \r\n" + 
				"\r\n" + 
				"SELECT ?Offers\r\n" + 
				"WHERE\r\n" + 
				"{\r\n" + 
				"?company	gr:offers     ?Offers    .\r\n" + 
				"}\r\n" + 
				"";
		
		Query query = QueryFactory.create(querySelect);
		QueryExecution qexec = QueryExecutionFactory.sparqlService(sparqlEndpoint, query);

		ResultSet results = qexec.execSelect();
		JsonArrayBuilder jsonArrayAdd = Json.createArrayBuilder();
		String o = "Offers";
		while(results.hasNext()) {
		jsonArrayAdd.add(results.nextSolution().getResource(o).getURI());
		}
		JsonArray ja = jsonArrayAdd.build();
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		JsonWriter writer = Json.createWriter(outputStream);
		writer.writeArray(ja);
		String output = new String(outputStream.toByteArray());

		return output;
	}
	
	//DELETE ALL 
	public void deleteAllOffers() {
		auth.getAuthentication();
		
		String updateQuery = 
				"PREFIX gr: <http://purl.org/goodrelations/v1#>\r\n" + 
				"PREFIX exco:<http://localhost:8080/webservice/webapi/companies/> \r\n" + 
				"PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#>\r\n" + 
				"\r\n" + 
				"DELETE {?company	gr:offers     ?Offers }\r\n" + 
				"WHERE\r\n" + 
				"{\r\n" + 
				"?company	gr:offers     ?Offers    .\r\n" +
				"};\r\n" + 
				
				"\r\n" + 
				"DELETE {?Offer	?p     ?o }\r\n" + 
				"WHERE\r\n" + 
				"{\r\n" + 
				"?Offer	?p     ?o;"
				+ "		gr:BusinessEntity	?company .\r\n" +
				"}\r\n" + 
				"";
		
		UpdateRequest request = UpdateFactory.create(updateQuery);
		UpdateProcessor up = UpdateExecutionFactory.createRemote(request, sparqlEndpoint);
		up.execute();
	}
}
