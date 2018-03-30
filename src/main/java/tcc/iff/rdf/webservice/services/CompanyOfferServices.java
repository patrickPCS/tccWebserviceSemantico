package tcc.iff.rdf.webservice.services;

import java.io.ByteArrayOutputStream;
import java.util.List;

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
import tcc.iff.rdf.webservice.model.Offer;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonWriter;

import javax.ws.rs.core.Response;

public class CompanyOfferServices {

	String sparqlEndpoint = "http://localhost:10035/catalogs/CatalogoGR/repositories/RepositorioGR/sparql";
	Authentication auth = new Authentication();	
	Methods methods = new Methods();

	//@GET All
	public String getAllOffers(String companyID) {
		auth.getAuthentication();

		String querySelect = methods.getAllOffersSparqlSelect(companyID);

		Query query = QueryFactory.create(querySelect);
		QueryExecution qexec = QueryExecutionFactory.sparqlService(sparqlEndpoint, query);

		ResultSet results = qexec.execSelect();

		JsonArrayBuilder jsonArrayAdd = Json.createArrayBuilder();
		String c = "Offers";
		while(results.hasNext()) {
			jsonArrayAdd.add(results.nextSolution().getResource(c).getURI());
		}
		JsonArray ja = jsonArrayAdd.build();
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		JsonWriter writer = Json.createWriter(outputStream);
		writer.writeArray(ja);
		String output = new String(outputStream.toByteArray());

		return output;
	}

	//@GET
	public String getOffer(String companyID, String offerID) {
		auth.getAuthentication();

		String q = methods.getOfferSparqlDescribe(companyID, offerID);

		Query query = QueryFactory.create(q);
		QueryExecution qexec = QueryExecutionFactory.sparqlService(sparqlEndpoint, query);

		Model results = qexec.execDescribe();	
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		results.write(outputStream, "JSON-LD");
		String output = new String(outputStream.toByteArray());

		return output;
	}

	//@POST
	public Response addOffering(String companyID, List<Offer> OfferList) {
		auth.getAuthentication();

		int TAM;
		TAM = OfferList.size();
		JsonArrayBuilder jsonArrayAdd = Json.createArrayBuilder();
		String exo = "http://localhost:8080/webservice/webapi/companies/"+companyID+"/offers/";
		for(int i=0; i<TAM; i++) {

			String offerURI = OfferList.get(i).getOfferURI();
			String productID = OfferList.get(i).getIncludes();
			String validFrom = OfferList.get(i).getValidFrom();
			String validThrough = OfferList.get(i).getValidThrough();
			String hasCurrency = OfferList.get(i).getHasCurrency();
			String price = OfferList.get(i).getPrice();

			String queryDescribe = methods.getOfferSparqlDescribe(companyID, productID);

			Query query = QueryFactory.create(queryDescribe);
			QueryExecution qexec = QueryExecutionFactory.sparqlService(sparqlEndpoint, query);

			Model results = qexec.execDescribe();

			if (results.isEmpty()) {

				String queryUpdate = methods.insertOfferSparql(companyID, productID, offerURI, validFrom, validThrough, hasCurrency, price);

				UpdateRequest request = UpdateFactory.create(queryUpdate);
				UpdateProcessor up = UpdateExecutionFactory.createRemote(request, sparqlEndpoint);
				up.execute();

				jsonArrayAdd.add(exo+productID);
			}
			else
			{
				String message = "CONFLICT: the "+productID+" Offering already exists: "+exo+productID;
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
	public Response updateOffering(String companyID, String oldOfferID, Offer updatedOffer) {

		auth.getAuthentication();
		
		String exo = "http://localhost:8080/webservice/webapi/companies/"+companyID+"/offers/";
		
		String offerURI = updatedOffer.getOfferURI();
		String productID = updatedOffer.getIncludes();
		String validFrom = updatedOffer.getValidFrom();
		String validThrough = updatedOffer.getValidThrough();
		String hasCurrency = updatedOffer.getHasCurrency();
		String price = updatedOffer.getPrice();

		String queryUpdate = methods.updateOfferSparql(oldOfferID, companyID, productID, offerURI, validFrom, validThrough, hasCurrency, price);

		UpdateRequest request = UpdateFactory.create(queryUpdate);
		UpdateProcessor up = UpdateExecutionFactory.createRemote(request, sparqlEndpoint);
		up.execute();
		
		JsonArrayBuilder jsonArrayAdd = Json.createArrayBuilder();
		jsonArrayAdd.add(exo+productID);
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
	public void deleteOffer(String companyID, String offerID) {
		auth.getAuthentication();

		String updateQuery = methods.deleteOfferSparql(companyID, offerID);

		UpdateRequest request = UpdateFactory.create(updateQuery);
		UpdateProcessor up = UpdateExecutionFactory.createRemote(request, sparqlEndpoint);
		up.execute();

	}

	//DELETE ALL 
	public void deleteAllOffers(String companyID) {
		auth.getAuthentication();

		String updateQuery = methods.deleteAllOffersSparql(companyID);

		UpdateRequest request = UpdateFactory.create(updateQuery);
		UpdateProcessor up = UpdateExecutionFactory.createRemote(request, sparqlEndpoint);
		up.execute();
	}
}
