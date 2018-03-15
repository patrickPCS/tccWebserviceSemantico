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

public class CompanyOfferServices {

	String sparqlEndpoint = "http://localhost:10035/catalogs/CatalogoGR/repositories/RepositorioGR/sparql";
	Authentication auth = new Authentication();	

	//@GET All
	public String getAllOffers(String companyID) {
		auth.getAuthentication();

		String querySelect = "PREFIX gr: <http://purl.org/goodrelations/v1#>\r\n" + 
				"PREFIX exco:<http://localhost:8080/webservice/webapi/companies/> \r\n" + 
				"\r\n" + 
				"SELECT ?Offers\r\n" + 
				"WHERE\r\n" + 
				"{\r\n" + 
				"exco:"+companyID+"	gr:offers     ?Offers    .\r\n" + 
				"}\r\n" + 
				"";
		
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

		String q = 
		"DESCRIBE <http://localhost:8080/webservice/webapi/companies/"+companyID+"/offers/"+offerID+">\r\n";

		Query query = QueryFactory.create(q);
		QueryExecution qexec = QueryExecutionFactory.sparqlService(sparqlEndpoint, query);

		Model results = qexec.execDescribe();	
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		results.write(outputStream, "JSON-LD");
		String output = new String(outputStream.toByteArray());

		return output;
	}

	//@POST
	public void addOffering(String companyID, List<Offer> OfferList) {
		auth.getAuthentication();

		int TAM;
		TAM = OfferList.size();
		for(int i=0; i<TAM; i++) {
			
			String offerURI = OfferList.get(i).getOfferURI();
			String productID = OfferList.get(i).getIncludes();
			String validFrom = OfferList.get(i).getValidFrom();
			String validThrough = OfferList.get(i).getValidThrough();
			String hasCurrency = OfferList.get(i).getHasCurrency();
			String price = OfferList.get(i).getPrice();
			String queryUpdate = 
					"PREFIX gr: <http://purl.org/goodrelations/v1#>\r\n" + 
					"PREFIX exo: <http://localhost:8080/webservice/webapi/companies/"+companyID+"/offers/>\r\n" + 
					"PREFIX exco: <http://localhost:8080/webservice/webapi/companies/>\r\n" + 
					"PREFIX exp: <http://localhost:8080/webservice/webapi/products/>\r\n" + 
					"PREFIX foaf: <http://xmlns.com/foaf/0.1/> \r\n" + 
					"PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>  \r\n" + 
					"PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#>\r\n" + 
					"\r\n" + 
					"INSERT DATA\r\n" + 
					"{ \r\n" + 
					"  exo:"+productID+"	rdf:type		gr:Offering;\r\n" + 
					"                        gr:includes      exp:"+productID+";\r\n" + 
					"	gr:BusinessEntity     exco:"+companyID+";\r\n" + 
					"	foaf:page     <"+offerURI+">;\r\n" + 
					"	gr:hasBusinessFunction gr:Sell ;\r\n" + 
					"	gr:validFrom '"+validFrom+"'^^xsd:dateTime ;\r\n" + 
					"	gr:validThrough '"+validThrough+"'^^xsd:dateTime ;\r\n" + 
					"	gr:hasPriceSpecification\r\n" + 
					"         [ a gr:UnitPriceSpecification ;\r\n" + 
					"           gr:hasCurrency '"+hasCurrency+"'^^xsd:string ;\r\n" + 
					"           gr:hasCurrencyValue '"+price+"' ;\r\n" + 
					"           gr:validThrough '"+validThrough+"'^^xsd:dateTime ] .   \r\n" +
					"  exco:"+companyID+"	gr:offers		exo:"+productID+" .\r\n" + 
					"}";

			UpdateRequest request = UpdateFactory.create(queryUpdate);
			UpdateProcessor up = UpdateExecutionFactory.createRemote(request, sparqlEndpoint);
			up.execute();
		}
	}

	//@PUT
	public void updateOffering(String companyID, String oldOfferID, Offer updatedOffer) {

		auth.getAuthentication();

		String offerURI = updatedOffer.getOfferURI();
		String productID = updatedOffer.getIncludes();
		String validFrom = updatedOffer.getValidFrom();
		String validThrough = updatedOffer.getValidThrough();
		String hasCurrency = updatedOffer.getHasCurrency();
		String price = updatedOffer.getPrice();

		String queryUpdate = 
				"PREFIX gr: <http://purl.org/goodrelations/v1#>\r\n" + 
				"PREFIX exo: <http://localhost:8080/webservice/webapi/companies/"+companyID+"/offers/>\r\n" + 
				"PREFIX exp: <http://localhost:8080/webservice/webapi/products/>\r\n" + 
				"PREFIX foaf: <http://xmlns.com/foaf/0.1/> \r\n" + 
				"PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>  \r\n" + 
				"PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#>\r\n" + 
				"PREFIX exco: <http://localhost:8080/webservice/webapi/companies/>\r\n" + 
				"\r\n" + 
				"DELETE \r\n" + 
				"	{ exo:"+oldOfferID+" ?p ?s }\r\n" + 
				"WHERE\r\n" + 
				"{ \r\n" + 
				"  exo:"+oldOfferID+" ?p ?s;\r\n" + 
				" 		rdf:type gr:Offering .\r\n" + 
				"};\r\n" + 
				"\r\n" + 
				"INSERT DATA\r\n" + 
				"{ \r\n" + 
				"  exo:"+productID+"	rdf:type		gr:Offering;\r\n" + 
				"                        gr:includes      exp:"+productID+";\r\n" + 
				"	gr:BusinessEntity     exco:"+companyID+";\r\n" + 
				"	foaf:page     <"+offerURI+">;\r\n" + 
				"	gr:hasBusinessFunction gr:Sell ;\r\n" + 
				"	gr:validFrom '"+validFrom+"'^^xsd:dateTime ;\r\n" + 
				"	gr:validThrough '"+validThrough+"'^^xsd:dateTime ;\r\n" + 
				"	gr:hasPriceSpecification\r\n" + 
				"         [ rdf:type gr:UnitPriceSpecification ;\r\n" + 
				"           gr:hasCurrency '"+hasCurrency+"'^^xsd:string ;\r\n" + 
				"           gr:hasCurrencyValue '"+price+"'^^xsd:decimal ;\r\n" + 
				"           gr:validThrough '"+validThrough+"'^^xsd:dateTime ] .   \r\n" +
				"  exco:"+companyID+"	gr:offers		exo:"+productID+" .\r\n" + 
				"}";

		UpdateRequest request = UpdateFactory.create(queryUpdate);
		UpdateProcessor up = UpdateExecutionFactory.createRemote(request, sparqlEndpoint);
		up.execute();
	}

	//DELETE
	public void deleteOffer(String companyID, String offerID) {
		auth.getAuthentication();

		String updateQuery = 
				"PREFIX exo: <http://localhost:8080/webservice/webapi/companies/"+companyID+"/offers/>\r\n" + 
				"PREFIX gr: <http://purl.org/goodrelations/v1#>\r\n" + 
				"PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#>\r\n" + 
				"PREFIX exco:<http://localhost:8080/webservice/webapi/companies/> \r\n" + 
				"\r\n" + 
				"DELETE \r\n" + 
				"	{ exo:"+offerID+" ?p ?s }\r\n" + 
				"WHERE\r\n" + 
				"{ \r\n" + 
				"  exo:"+offerID+" ?p ?s;\r\n" +
				" 		rdf:type gr:Offering .\r\n" + 
				"};\r\n" + 
				"\r\n" + 
				"DELETE {exco:"+companyID+"	gr:offers     exo:"+offerID+" }\r\n" + 
				"WHERE\r\n" + 
				"{\r\n" + 
				"exco:"+companyID+"	gr:offers     exo:"+offerID+"    .\r\n" +
				"}";

		UpdateRequest request = UpdateFactory.create(updateQuery);
		UpdateProcessor up = UpdateExecutionFactory.createRemote(request, sparqlEndpoint);
		up.execute();

	}
	
	//DELETE ALL 
	public void deleteAllOffers(String companyID) {
		auth.getAuthentication();
		
		String updateQuery = 
				"PREFIX gr: <http://purl.org/goodrelations/v1#>\r\n" + 
				"PREFIX exco:<http://localhost:8080/webservice/webapi/companies/> \r\n" + 
				"PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#>\r\n" + 
				"\r\n" + 
				"DELETE {exco:"+companyID+"	gr:offers     ?Offers }\r\n" + 
				"WHERE\r\n" + 
				"{\r\n" + 
				"exco:"+companyID+"	gr:offers     ?Offers    .\r\n" +
				"};\r\n" + 
				
				"\r\n" + 
				"DELETE {?Offer	?p     ?o }\r\n" + 
				"WHERE\r\n" + 
				"{\r\n" + 
				"?Offer	?p     ?o;"
				+ "		gr:BusinessEntity	exco:"+companyID+" .\r\n" +
				"}\r\n" + 
				"";
		
		
		
		UpdateRequest request = UpdateFactory.create(updateQuery);
		UpdateProcessor up = UpdateExecutionFactory.createRemote(request, sparqlEndpoint);
		up.execute();
	}
}
