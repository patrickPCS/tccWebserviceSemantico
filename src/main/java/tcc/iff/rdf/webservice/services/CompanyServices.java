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
import tcc.iff.rdf.webservice.model.Company;

public class CompanyServices {
	
	String sparqlEndpoint = "http://localhost:10035/catalogs/CatalogoGR/repositories/RepositorioGR/sparql";
	Authentication auth = new Authentication();	
	Methods methods = new Methods();
	
	//GET ALL
	public String getAllCompanies() {
		auth.getAuthentication();

		String querySelect = methods.getAllCompaniesSparqlSelect();
		
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
	public Response getCompany(String companyID, String accept) {
		auth.getAuthentication();

		String queryDescribe = methods.getCompanySparqlDescribe(companyID);
		
		Query query = QueryFactory.create(queryDescribe);
		QueryExecution qx = QueryExecutionFactory.sparqlService(sparqlEndpoint, query);
		
		Model rst = qx.execDescribe();
		if (rst.isEmpty()) {
			return Response.status(422)
					.entity("Please, choose a valid ProductType ID.")
					.build();
		}
		
		if(accept.equals("application/json") || methods.isValidFormat(accept)==false) {
			
			String querySelect = methods.getCompanySparqlSelect(companyID);

			Query queryS = QueryFactory.create(querySelect);
			QueryExecution qexec = QueryExecutionFactory.sparqlService(sparqlEndpoint, queryS);
			ResultSet results = qexec.execSelect();
			
			QuerySolution soln = results.nextSolution();
			String companyURL = soln.getResource("companyURL").toString() ;  
			String legalName = soln.getLiteral("legalName").toString() ;
			String email = soln.getResource("email").toString() ;
			String name = soln.getResource("name").toString() ;

			JsonObject jobj = Json.createObjectBuilder()
					.add("id", companyID)
					.add("companyURL",companyURL)
					.add("legalName",legalName)
					.add("email",email)
					.add("name",name)
					.add("label",name)
					.build();

			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			JsonWriter writer = Json.createWriter(outputStream);
			writer.writeObject(jobj);
			String output = new String(outputStream.toByteArray());
			writer.close();

			return Response.status(Response.Status.OK)
					.entity(output)
					.build();
			
		}else {
		
		String format = methods.convertFromAcceptToFormat(accept);
		
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		rst.write(outputStream, format);
		String output = new String(outputStream.toByteArray());

		return Response.status(Response.Status.OK)
				.entity(output)
				.build();
		}
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
			String name = companyList.get(i).getName();
			
			String queryDescribe = methods.getCompanySparqlDescribe(companyID);
			
			Query query = QueryFactory.create(queryDescribe);
			QueryExecution qexec = QueryExecutionFactory.sparqlService(sparqlEndpoint, query);

			Model results = qexec.execDescribe();
			if (results.isEmpty()) {
	
			String queryUpdate = methods.insertCompanySparql(companyID, companyURL, email, name, legalName);
	
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
	String name = newCompany.getName();

	String queryUpdate = methods.updateCompanySparql(oldCompanyID, companyID, companyURL, email, name, legalName);
	
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

		String updateQuery = methods.deleteCompanySparql(companyID);

		UpdateRequest request = UpdateFactory.create(updateQuery);
		UpdateProcessor up = UpdateExecutionFactory.createRemote(request, sparqlEndpoint);
		up.execute();
	}

	//DELETE ALL
	public void deleteAllCompanies() {
		auth.getAuthentication();
		
		String updateQuery = methods.deleteAllCompaniesSparql();
		
		UpdateRequest request = UpdateFactory.create(updateQuery);
		UpdateProcessor up = UpdateExecutionFactory.createRemote(request, sparqlEndpoint);
		up.execute();
	}
}
