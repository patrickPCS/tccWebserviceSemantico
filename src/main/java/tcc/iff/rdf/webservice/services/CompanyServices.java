package tcc.iff.rdf.webservice.services;

import java.io.ByteArrayOutputStream;
import java.util.List;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonWriter;
import javax.ws.rs.core.Response;

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
import tcc.iff.rdf.webservice.model.Company;

public class CompanyServices {
	
	String sparqlEndpoint = "http://localhost:10035/catalogs/CatalogoGR/repositories/RepositorioGR/sparql";
	Authentication auth = new Authentication();	
	
	//GET ALL
	public String getAllCompanies() {
		auth.getAuthentication();

		String querySelect = "PREFIX gr: <http://purl.org/goodrelations/v1#>\r\n" + 
				"PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#>\r\n" + 
				"\r\n" + 
				"SELECT ?company\r\n" + 
				"WHERE\r\n" + 
				"{\r\n" + 
				"?company rdf:type gr:BusinessEntity .\r\n" + 
				"}\r\n" + 
				"";
		
		Query query = QueryFactory.create(querySelect);
		QueryExecution qexec = QueryExecutionFactory.sparqlService(sparqlEndpoint, query);

		ResultSet results = qexec.execSelect();
		//return ResultSetFormatter.asText(results);
		
		JsonArrayBuilder jsonArrayAdd = Json.createArrayBuilder();
		String c = "company";
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
	
	//GET
	public String getCompany(String companyID) {
		auth.getAuthentication();

		String q = 
				"PREFIX gr: <http://purl.org/goodrelations/v1#>\r\n" + 
				"PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#>\r\n" + 
				"PREFIX exco: <http://localhost:8080/webservice/webapi/companies/>\r\n" + 
				"\r\n" + 
				"DESCRIBE exco:"+companyID+"\r\n" + 
				"WHERE\r\n" + 
				"{\r\n" + 
				"exco:"+companyID+" rdf:type gr:BusinessEntity  .\r\n" + 
				"}\r\n" + 
				"";


		Query query = QueryFactory.create(q);
		QueryExecution qexec = QueryExecutionFactory.sparqlService(sparqlEndpoint, query);

		Model results = qexec.execDescribe();	
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		results.write(outputStream, "RDF/JSON");
		String output = new String(outputStream.toByteArray());

		return output;
	}

	//POST
	public Response addCompany(List<Company> companyList) {
		auth.getAuthentication();
		int TAM;
		TAM = companyList.size();
		JsonArrayBuilder jsonArrayAdd = Json.createArrayBuilder();
		String exco = "http://localhost:8080/webservice/webapi/companies/";
		for(int i=0; i<TAM; i++) {
			String companyID = companyList.get(i).getCompanyID();
			String companyURL = companyList.get(i).getCompanyURL();
			String legalName = companyList.get(i).getLegalName();
			String email = companyList.get(i).getEmail();
			String catalogURI = companyList.get(i).getCatalogURI();
			
			String queryDescribe = 
					"PREFIX exco: <http://localhost:8080/webservice/webapi/companies/>\r\n" +  
					"\r\n" + 
					"DESCRIBE exco:"+companyID+"\r\n" + 
					"WHERE\r\n" + 
					"{\r\n" + 
					"exco:"+companyID+" ?p ?o .\r\n" + 
					"}";
			
			Query query = QueryFactory.create(queryDescribe);
			QueryExecution qexec = QueryExecutionFactory.sparqlService(sparqlEndpoint, query);

			Model results = qexec.execDescribe();
			if (results.isEmpty()) {
	
			String queryUpdate = 
							"PREFIX gr: <http://purl.org/goodrelations/v1#>\r\n" + 
							"PREFIX ex: <http://example.com/>\r\n" + 
							"PREFIX vcard: <http://www.w3.org/2006/vcard/ns#>\r\n" + 
							"PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#>\r\n" + 
							"PREFIX exco: <http://localhost:8080/webservice/webapi/companies/>\r\n" + 
							"\r\n" + 
							"\r\n" + 
							"INSERT DATA\r\n" + 
							"{ \r\n" + 
							"  exco:"+companyID+" 	rdf:type		gr:BusinessEntity;\r\n" + 
							"                vcard:hasURL	<"+companyURL+">;\r\n" + 
							"                vcard:hasEmail	<"+email+">;\r\n" + 
							"                ex:catalogURI	<"+catalogURI+">;\r\n" + 
							"                gr:legalName	'"+legalName+"'   .           \r\n" + 
							"}";
	
			UpdateRequest request = UpdateFactory.create(queryUpdate);
			UpdateProcessor up = UpdateExecutionFactory.createRemote(request, sparqlEndpoint);
			up.execute();
			
			jsonArrayAdd.add(exco+companyID);
			}else {
				String message = "CONFLICT: already exists a Company with this same ID: "+exco+companyID;
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
			return Response.status(Response.Status.CREATED)
					   .entity(output)
					   .build();	
			}

	//PUT
	public Response updateCompany(String oldCompanyID, Company newCompany) {
	auth.getAuthentication();
	
	String exco = "http://localhost:8080/webservice/webapi/companies/";
	
	String companyID = newCompany.getCompanyID();
	String companyURL = newCompany.getCompanyURL();
	String legalName = newCompany.getLegalName();
	String email = newCompany.getEmail();
	String catalogURI = newCompany.getCatalogURI();

	String queryUpdate = 
				"PREFIX gr: <http://purl.org/goodrelations/v1#>\r\n" + 
				"PREFIX ex: <http://example.com/>\r\n" + 
				"PREFIX vcard: <http://www.w3.org/2006/vcard/ns#>\r\n" + 
				"PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#>\r\n" + 
				"PREFIX exco: <http://localhost:8080/webservice/webapi/companies/>\r\n" + 
				"\r\n" + 
				"DELETE\r\n" + 
				"{ exco:"+oldCompanyID+" ?p ?s }\r\n" + 
				"WHERE\r\n" + 
				"{\r\n" + 
				"exco:"+oldCompanyID+" ?p ?s;\r\n" + 
				"rdf:type  gr:BusinessEntity .\r\n" + 
				"};"+
				"\r\n" + 
				"INSERT DATA\r\n" + 
				"{ \r\n" + 
				"  exco:"+companyID+" 	rdf:type		gr:BusinessEntity;\r\n" + 
				"                vcard:hasURL	<"+companyURL+">;\r\n" + 
				"                vcard:hasEmail	<"+email+">;\r\n" + 
				"                ex:catalogURI	<"+catalogURI+">;\r\n" + 
				"                gr:legalName	'"+legalName+"' .\r\n" + 
				"}";
	
	UpdateRequest request = UpdateFactory.create(queryUpdate);
	UpdateProcessor up = UpdateExecutionFactory.createRemote(request, sparqlEndpoint);
	up.execute();
	
	JsonArrayBuilder jsonArrayAdd = Json.createArrayBuilder();
	jsonArrayAdd.add(exco+companyID);
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
	public void deleteCompany(String companyID) {
		auth.getAuthentication();

		String updateQuery = 
						"PREFIX exco: <http://localhost:8080/webservice/webapi/companies/>\r\n" + 
						"PREFIX gr: <http://purl.org/goodrelations/v1#>\r\n" + 
						"PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#>\r\n" + 
						"\r\n" + 
						"DELETE \r\n" + 
						"	{ exco:"+companyID+" ?p ?s }\r\n" + 
						"WHERE\r\n" + 
						"{ \r\n" + 
						"  exco:"+companyID+" ?p ?s; .\r\n" + 
						"}\r\n" + 
						"";

		UpdateRequest request = UpdateFactory.create(updateQuery);
		UpdateProcessor up = UpdateExecutionFactory.createRemote(request, sparqlEndpoint);
		up.execute();
	}

	//DELETE ALL
	public void deleteAllCompanies() {
		auth.getAuthentication();
		
		String updateQuery = "PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#>\r\n" + 
				"PREFIX gr: <http://purl.org/goodrelations/v1#>\r\n" + 
				"\r\n" + 
				"DELETE\r\n" + 
				"{ ?companyURL ?p ?s }\r\n" + 
				"WHERE\r\n" + 
				"{\r\n" + 
				"?companyURL ?p ?s;\r\n" + 
				"rdf:type  gr:BusinessEntity .\r\n" + 
				"}";
		
		UpdateRequest request = UpdateFactory.create(updateQuery);
		UpdateProcessor up = UpdateExecutionFactory.createRemote(request, sparqlEndpoint);
		up.execute();
	}
}
