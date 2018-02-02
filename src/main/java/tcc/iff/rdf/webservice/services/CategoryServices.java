package tcc.iff.rdf.webservice.services;

import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.update.UpdateExecutionFactory;
import org.apache.jena.update.UpdateFactory;
import org.apache.jena.update.UpdateProcessor;
import org.apache.jena.update.UpdateRequest;

import tcc.iff.rdf.webservice.connection.Authentication;
import tcc.iff.rdf.webservice.data.Category;

public class CategoryServices {
	
	String sparqlEndpoint = "http://localhost:10035/catalogs/CatalogoGR/repositories/RepositorioGR/sparql";
	Authentication auth = new Authentication();

	
	public void addCategory(Category newCat) {
			int id = newCat.getId();
			String desc = newCat.getDescricao();
			
		String updateQuery = 
				"PREFIX gr: <http://purl.org/goodrelations/v1#>\r\n" + 
				"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\r\n" + 
				"\r\n" + 
				"INSERT DATA\r\n" + 
				"{\r" + 
				"\n  gr:category 	rdf:type   "+newCat+" \r\n" + 
				"}";
		
		
		auth.getAuthentication();
		
		
		UpdateRequest request = UpdateFactory.create(updateQuery);
		UpdateProcessor up = UpdateExecutionFactory.createRemote(request, sparqlEndpoint);
		up.execute();
		
	}
	
	public void getAllCategories() {
		
		String q = "SELECT ?subject ?predicate ?object\r\n" + 
				"WHERE {\r\n" + 
				"  ?subject ?predicate ?object\r\n" + 
				"}\r\n"+
				"LIMIT 30";
				
		Query query = QueryFactory.create(q);
		QueryExecution qexec = QueryExecutionFactory.sparqlService(sparqlEndpoint, query);
		qexec.execSelect();
		ResultSet results = qexec.execSelect();	
		
		while (results.hasNext()) {
			QuerySolution qs = results.next();
			System.out.println(qs + "\n");
			}
		}

	public void updateCategory(Category cat) {
		// TODO Auto-generated method stub
		
	}

	public void deleteCategory(int catID) {
		// TODO Auto-generated method stub
		
	}
		
		

}
