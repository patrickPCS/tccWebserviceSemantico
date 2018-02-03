package tcc.iff.rdf.webservice.services;

import java.util.ArrayList;
import java.util.List;

import org.apache.jena.atlas.json.JSON;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.ResultSetFormatter;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.update.UpdateExecutionFactory;
import org.apache.jena.update.UpdateFactory;
import org.apache.jena.update.UpdateProcessor;
import org.apache.jena.update.UpdateRequest;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import tcc.iff.rdf.webservice.connection.Authentication;
import tcc.iff.rdf.webservice.data.Category;
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonSerialize
public class CategoryServices {
	
	String sparqlEndpoint = "http://localhost:10035/catalogs/CatalogoGR/repositories/RepositorioGR/sparql";
	Authentication auth = new Authentication();

	
	public void addCategory(Category newCat) {
		
		auth.getAuthentication();

		String name = newCat.getName();
			
		String updateQuery = 
				"PREFIX gr: <http://purl.org/goodrelations/v1#>\r\n" + 
				"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\r\n" + 
				"PREFIX ex: <http://example.com/>\r\n" + 
				"\r\n" + 
				"INSERT DATA\r\n" + 
				"{\r\n" + 
				"ex:Categoria	rdf:type	gr:category;\r\n" + 
				"		gr:name		"+name+"	.\r\n" + 
				"		\r\n" + 
				"}";		
		
		UpdateRequest request = UpdateFactory.create(updateQuery);
		UpdateProcessor up = UpdateExecutionFactory.createRemote(request, sparqlEndpoint);
		up.execute();
		
	}
	
	public Object getCat(String catName) {
		auth.getAuthentication();

		String q = "SELECT ?s ?p ?o WHERE { ?s ?p '"+catName+"' }";
				
		Query query = QueryFactory.create(q);
		QueryExecution qexec = QueryExecutionFactory.sparqlService(sparqlEndpoint, query);
		ResultSet result = qexec.execSelect();
		ResultSetFormatter.outputAsJSON(result);
		return result;
	}
	
	public List<QuerySolution> getAllCategories() {
		auth.getAuthentication();

		String q = "SELECT ?subject ?predicate ?object\r\n" + 
				"WHERE {\r\n" + 
				"  ?subject ?predicate ?object\r\n" + 
				"}\r\n"+
				"LIMIT 30";
				
		Query query = QueryFactory.create(q);
		QueryExecution qexec = QueryExecutionFactory.sparqlService(sparqlEndpoint, query);
		//qexec.execSelect();
		ResultSet results = qexec.execSelect();	
		ResultSetFormatter.outputAsJSON(results);
		
		List<QuerySolution> lista = new ArrayList<>();
		while (results.hasNext()) {
			QuerySolution qs = results.next();
			lista.add(qs);
			}
		
		return lista;

		}

	public void updateCategory(String oldName, String newName) {
		auth.getAuthentication();
			
		String updateQuery = 
				"PREFIX ex: <http://example.com/>\r\n" + 
				"PREFIX gr: <http://purl.org/goodrelations/v1#>\r\n" + 
				"\r\n" + 
				"DELETE DATA\r\n" + 
				"{ ex:Categoria  gr:name  '"+oldName+"' } ;\r\n" + 
				"\r\n" + 
				"PREFIX dc: <http://purl.org/dc/elements/1.1/>\r\n" + 
				"INSERT DATA\r\n" + 
				"{ ex:Categoria  gr:name  '"+newName+"' }";		
		
		UpdateRequest request = UpdateFactory.create(updateQuery);
		UpdateProcessor up = UpdateExecutionFactory.createRemote(request, sparqlEndpoint);
		up.execute();
		
	}

	public void deleteCategory(String catName) {
		auth.getAuthentication();
		
		String updateQuery = 
				"PREFIX ex: <http://example.com/>\r\n" + 
				"PREFIX gr: <http://purl.org/goodrelations/v1#>\r\n" + 
				"\r\n" + 
				"DELETE DATA\r\n" + 
				"{ ex:Categoria  gr:name  '"+catName+"' } ";
						
		
		UpdateRequest request = UpdateFactory.create(updateQuery);
		UpdateProcessor up = UpdateExecutionFactory.createRemote(request, sparqlEndpoint);
		up.execute();
		
	}
		
		

}
