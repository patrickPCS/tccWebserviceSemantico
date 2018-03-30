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
	Methods methods = new Methods();

	String sparqlEndpoint = "http://localhost:10035/catalogs/CatalogoGR/repositories/RepositorioGR/sparql";
	Authentication auth = new Authentication();	

	//@GET All
	public String getAllOffers() {
		auth.getAuthentication();

		String querySelect = methods.getAllOffersSparqlSelect();
		
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
		
		String updateQuery = methods.deleteAllOffers();
		
		UpdateRequest request = UpdateFactory.create(updateQuery);
		UpdateProcessor up = UpdateExecutionFactory.createRemote(request, sparqlEndpoint);
		up.execute();
	}
}
