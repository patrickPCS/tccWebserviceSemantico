package tcc.iff.rdf.webservice.services;

import org.apache.jena.update.UpdateExecutionFactory;
import org.apache.jena.update.UpdateFactory;
import org.apache.jena.update.UpdateProcessor;
import org.apache.jena.update.UpdateRequest;

import tcc.iff.rdf.webservice.connection.Authentication;

public class CategoryServices {
	
	String sparqlEndpoint = "http://localhost:10035/catalogs/CatalogoGR/repositories/RepositorioGR/sparql";
	
	Authentication auth = new Authentication();
	
	public void addCategory(String newCat) {
		String updateQuery = 
				"PREFIX gr: <http://purl.org/goodrelations/v1#>\r\n" + 
				"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\r\n" + 
				"\r\n" + 
				"INSERT DATA\r\n" + 
				"{\r" + 
				"\n  gr:category 	rdf:type   "+newCat+" \r\n" + 
				"}";
		
		UpdateRequest request = UpdateFactory.create(updateQuery);
		UpdateProcessor up = UpdateExecutionFactory.createRemote(request, sparqlEndpoint);
		up.execute();
		
	}

}
