package tcc.iff.rdf.webservice.services;

import java.io.ByteArrayOutputStream;
import java.util.List;

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
import tcc.iff.rdf.webservice.model.Offer;

public class OfferServices {

	String sparqlEndpoint = "http://localhost:10035/catalogs/CatalogoGR/repositories/RepositorioGR/sparql";
	Authentication auth = new Authentication();	

	//@GET All
	public String getAllOffers() {
		auth.getAuthentication();

		String querySelect = "PREFIX gr: <http://purl.org/goodrelations/v1#>\r\n" + 
				"PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#>\r\n" + 
				"\r\n" + 
				"SELECT ?offer\r\n" + 
				"WHERE\r\n" + 
				"{\r\n" + 
				"?offer     rdf:type     gr:Offering    .\r\n" + 
				"}\r\n" + 
				"";
		
		Query query = QueryFactory.create(querySelect);
		QueryExecution qexec = QueryExecutionFactory.sparqlService(sparqlEndpoint, query);

		ResultSet results = qexec.execSelect();
		return ResultSetFormatter.asText(results);

	}

	//@GET
	public String getOffer(String offerID) {
		auth.getAuthentication();

		String q = "PREFIX gr: <http://purl.org/goodrelations/v1#>\r\n" + 
				"PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#>\r\n" + 
				"PREFIX exo: <http://example.com/Offering/>\r\n" + 
				"\r\n" + 
				"DESCRIBE ?offerURI\r\n" + 
				"WHERE\r\n" + 
				"{\r\n" + 
				" ?offerURI      rdf:type     gr:Offering;\r\n" + 
				"                      exo:offerID  '"+offerID+"' .\r\n" + 
				"}\r\n" + 
				"";


		Query query = QueryFactory.create(q);
		QueryExecution qexec = QueryExecutionFactory.sparqlService(sparqlEndpoint, query);

		Model results = qexec.execDescribe();	
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		results.write(outputStream, "JSON-LD");
		String output = new String(outputStream.toByteArray());

		return output;
	}

	//@POST
	public void addOffering(List<Offer> OfferList) {
		auth.getAuthentication();

		int TAM;
		TAM = OfferList.size();
		for(int i=0; i<TAM; i++) {
			
			String offerID = OfferList.get(i).getOfferID();
			String offerURI = OfferList.get(i).getOfferURI();
			String productID = OfferList.get(i).getIncludes();
			String validFrom = OfferList.get(i).getValidFrom();
			String validThrough = OfferList.get(i).getValidThrough();
			String hasCurrency = OfferList.get(i).getHasCurrency();
			String price = OfferList.get(i).getPrice();

			String queryUpdate = 
					"PREFIX gr: <http://purl.org/goodrelations/v1#>\r\n" + 
					"PREFIX ex: <http://example.com/>\r\n" + 
					"PREFIX foaf: <http://xmlns.com/foaf/0.1/> \r\n" + 
					"PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>  \r\n" + 
					"PREFIX exo: <http://example.com/Offering/>\r\n" + 
					"PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#>\r\n" + 
					"\r\n" + 
					"INSERT DATA\r\n" + 
					"{ \r\n" + 
					"  <"+offerURI+">	rdf:type		gr:Offering;\r\n" + 
					"                        gr:includes      '"+productID+"';\r\n" + 
					"                        exo:offerID      '"+offerID+"';\r\n" + 
					"	foaf:page     <"+offerURI+">;\r\n" + 
					"	gr:hasBusinessFunction gr:Sell ;\r\n" + 
					"	gr:validFrom '"+validFrom+"'^^xsd:dateTime ;\r\n" + 
					"	gr:validThrough '"+validThrough+"'^^xsd:dateTime ;\r\n" + 
					"	gr:hasPriceSpecification\r\n" + 
					"         [ a gr:UnitPriceSpecification ;\r\n" + 
					"           gr:hasCurrency '"+hasCurrency+"'^^xsd:string ;\r\n" + 
					"           gr:hasCurrencyValue '"+price+"'^^xsd:float ;\r\n" + 
					"           gr:validThrough '"+validThrough+"'^^xsd:dateTime ] .   \r\n" + 
					"}";

			UpdateRequest request = UpdateFactory.create(queryUpdate);
			UpdateProcessor up = UpdateExecutionFactory.createRemote(request, sparqlEndpoint);
			up.execute();
		}
	}

	//@PUT
	public void updateOffering(String oldOfferID, Offer updatedOffer) {

		auth.getAuthentication();

		String offerID = updatedOffer.getOfferID();
		String offerURI = updatedOffer.getOfferURI();
		String productID = updatedOffer.getIncludes();
		String validFrom = updatedOffer.getValidFrom();
		String validThrough = updatedOffer.getValidThrough();
		String hasCurrency = updatedOffer.getHasCurrency();
		String price = updatedOffer.getPrice();

		String queryUpdate = 
				"PREFIX gr: <http://purl.org/goodrelations/v1#>\r\n" + 
				"PREFIX ex: <http://example.com/>\r\n" + 
				"PREFIX foaf: <http://xmlns.com/foaf/0.1/> \r\n" + 
				"PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>  \r\n" + 
				"PREFIX exo: <http://example.com/Offering/>\r\n" + 
				"PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#>\r\n" + 
				"\r\n" + 
				"DELETE \r\n" + 
				"	{ ?offerURI ?p ?s }\r\n" + 
				"WHERE\r\n" + 
				"{ \r\n" + 
				"  ?offerURI ?p ?s; \r\n" + 
				"                exo:offerID		'"+oldOfferID+"'	.\r\n" + 
				"};\r\n" + 
				"\r\n" + 
				"INSERT DATA\r\n" + 
				"{ \r\n" + 
				"  <"+offerURI+">	rdf:type		gr:Offering;\r\n" + 
				"                        gr:includes      '"+productID+"';\r\n" + 
				"                        exo:offerID      '"+offerID+"';\r\n" + 
				"	foaf:page     <"+offerURI+">;\r\n" + 
				"	gr:hasBusinessFunction gr:Sell ;\r\n" + 
				"	gr:validFrom '"+validFrom+"'^^xsd:dateTime ;\r\n" + 
				"	gr:validThrough '"+validThrough+"'^^xsd:dateTime ;\r\n" + 
				"	gr:hasPriceSpecification\r\n" + 
				"         [ a gr:UnitPriceSpecification ;\r\n" + 
				"           gr:hasCurrency '"+hasCurrency+"'^^xsd:string ;\r\n" + 
				"           gr:hasCurrencyValue '"+price+"'^^xsd:float ;\r\n" + 
				"           gr:validThrough '"+validThrough+"'^^xsd:dateTime ] .   \r\n" + 
				"}";

		UpdateRequest request = UpdateFactory.create(queryUpdate);
		UpdateProcessor up = UpdateExecutionFactory.createRemote(request, sparqlEndpoint);
		up.execute();
	}

	//DELETE
	public void deleteOffer(String offerID) {
		auth.getAuthentication();

		String updateQuery = 
				"PREFIX exo: <http://example.com/Offering/>\r\n" + 
				"PREFIX gr: <http://purl.org/goodrelations/v1#>\r\n" + 
				"PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#>\r\n" + 
				"\r\n" + 
				"DELETE \r\n" + 
				"	{ ?offerURI ?p ?s }\r\n" + 
				"WHERE\r\n" + 
				"{ \r\n" + 
				"  ?offerURI ?p ?s; \r\n" + 
				"                exo:offerID		'"+offerID+"'	.\r\n" + 
				"}\r\n" + 
				"";

		UpdateRequest request = UpdateFactory.create(updateQuery);
		UpdateProcessor up = UpdateExecutionFactory.createRemote(request, sparqlEndpoint);
		up.execute();

	}
	
	//DELETE ALL 
	public void deleteAllOffers() {
		auth.getAuthentication();
		
		String updateQuery = "PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#>\r\n" + 
				"PREFIX gr: <http://purl.org/goodrelations/v1#>\r\n" + 
				"\r\n" + 
				"DELETE\r\n" + 
				"{ ?offering ?p ?s }\r\n" + 
				"WHERE\r\n" + 
				"{\r\n" + 
				"?offering ?p ?s;\r\n" + 
				"rdf:type  gr:Offering .\r\n" + 
				"}";
		
		UpdateRequest request = UpdateFactory.create(updateQuery);
		UpdateProcessor up = UpdateExecutionFactory.createRemote(request, sparqlEndpoint);
		up.execute();
	}
}
