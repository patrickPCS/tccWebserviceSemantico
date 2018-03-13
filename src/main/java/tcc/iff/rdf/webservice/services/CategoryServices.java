package tcc.iff.rdf.webservice.services;

import java.io.ByteArrayOutputStream;

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
import tcc.iff.rdf.webservice.model.Category;

public class CategoryServices {
	
	String sparqlEndpoint = "http://localhost:10035/catalogs/CatalogoGR/repositories/RepositorioGR/sparql";
	Authentication auth = new Authentication();	
	
	//GET ALL
	public String getAllCategories() {
		auth.getAuthentication();

		String querySelect = "PREFIX ex: <http://example.com/>\r\n" + 
				"PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#>\r\n" + 
				"\r\n" + 
				"SELECT ?category \r\n" + 
				"WHERE \r\n" + 
				"{\r\n" + 
				"   ?category       rdf:type     ex:Category\r\n" + 
				"}";
		
		Query query = QueryFactory.create(querySelect);
		QueryExecution qexec = QueryExecutionFactory.sparqlService(sparqlEndpoint, query);

		ResultSet results = qexec.execSelect();
		return ResultSetFormatter.asText(results);
	}
	
	//GET
	public String getCategory(String catName) {
		auth.getAuthentication();

		String q = "PREFIX exc: <http://example.com/Category/>\r\n" + 
				"PREFIX ex: <http://example.com/>\r\n" +
				"PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#>\r\n" + 
				"\r\n" + 
				"DESCRIBE exc:"+catName+" \r\n" + 
				"WHERE \r\n" + 
				"{\r\n" + 
				"   exc:"+catName+"       rdf:type     ex:Category\r\n" + 
				"}";


		Query query = QueryFactory.create(q);
		QueryExecution qexec = QueryExecutionFactory.sparqlService(sparqlEndpoint, query);

		Model results = qexec.execDescribe();	
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		results.write(outputStream, "JSON-LD");
		String output = new String(outputStream.toByteArray());

		return output;
	}

	//POST
	public void addCategory(Category newCategory) {
		auth.getAuthentication();

		String name = newCategory.getName();
		String id = newCategory.getId();

		String queryUpdate = 
						"PREFIX ex: <http://example.com/>\r\n" + 
						"PREFIX exc: <http://example.com/Category/>\r\n" + 
						"PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#>\r\n" + 
						"\r\n" + 
						"INSERT DATA\r\n" + 
						"{ \r\n" + 
						"  exc:"+id+"       rdf:type     ex:Category;\r\n" + 
						"  	                        ex:name    '"+name+"' .\r\n" + 
						"}";

		UpdateRequest request = UpdateFactory.create(queryUpdate);
		UpdateProcessor up = UpdateExecutionFactory.createRemote(request, sparqlEndpoint);
		up.execute();
	}

	//PUT
	public void updateCategory(String oldCatID, Category newCat) {
	auth.getAuthentication();
	
	String name = newCat.getName();
	String id = newCat.getId();

	String queryUpdate = "PREFIX exc: <http://example.com/Category/>\r\n" + 
			"PREFIX ex: <http://example.com/>\r\n" + 
			"PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#>\r\n" + 
			"\r\n" + 
			"DELETE \r\n" + 
			"	{ exc:"+id+" ?p ?s }\r\n" + 
			"WHERE\r\n" + 
			"{ \r\n" + 
			"  exc:"+id+" ?p ?s; \r\n" + 
			"                rdf:type		ex:Category	.\r\n" + 
			"                \r\n" + 
			"};\r\n" + 
			"\r\n" + 
			"INSERT DATA\r\n" + 
			"{ \r\n" + 
			"  exc:"+id+"        rdf:type     ex:Category;\r\n" + 
			"  	                 ex:name    '"+name+"' .\r\n" + 
			"}\r\n" + 
			"\r\n" + 
			"";
	
	UpdateRequest request = UpdateFactory.create(queryUpdate);
	UpdateProcessor up = UpdateExecutionFactory.createRemote(request, sparqlEndpoint);
	up.execute();
	}

	//DELETE
	public void deleteCategory(String catID) {
		auth.getAuthentication();

		String updateQuery = 
						"PREFIX exc: <http://example.com/Category/>\r\n" + 
						"PREFIX ex: <http://example.com/>\r\n" + 
						"PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#>\r\n" + 
						"\r\n" + 
						"DELETE \r\n" + 
						"	{ exc:"+catID+" ?p ?s }\r\n" + 
						"WHERE\r\n" + 
						"{ \r\n" + 
						"  exc:"+catID+" ?p ?s; \r\n" + 
						"                rdf:type		ex:Category	.\r\n" + 
						"                \r\n" + 
						"}\r\n" + 
						"\r\n" + 
						"";

		UpdateRequest request = UpdateFactory.create(updateQuery);
		UpdateProcessor up = UpdateExecutionFactory.createRemote(request, sparqlEndpoint);
		up.execute();
	}

	//DELETE ALL
	public void deleteAllCategories() {
		auth.getAuthentication();
		
		String updateQuery = "PREFIX ex: <http://example.com/>\r\n" + 
				"PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#>\r\n" + 
				"\r\n" + 
				"DELETE \r\n" + 
				"	{ ?category ?p ?s }\r\n" + 
				"WHERE\r\n" + 
				"{ \r\n" + 
				"  ?category ?p ?s; \r\n" + 
				"                rdf:type		ex:Category	.\r\n" + 
				"                \r\n" + 
				"}";
		
		UpdateRequest request = UpdateFactory.create(updateQuery);
		UpdateProcessor up = UpdateExecutionFactory.createRemote(request, sparqlEndpoint);
		up.execute();
	}
}
