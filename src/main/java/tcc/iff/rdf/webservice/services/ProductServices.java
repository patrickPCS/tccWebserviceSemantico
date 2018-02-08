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
import tcc.iff.rdf.webservice.model.Product;


public class ProductServices {
	
	String sparqlEndpoint = "http://localhost:10035/catalogs/CatalogoGR/repositories/RepositorioGR/sparql";
	Authentication auth = new Authentication();	
	
//@GET All
public String getAllProducts() {
		auth.getAuthentication();
		
		/*String describeString = "PREFIX exc: <http://example.com/categories/>\r\n" + 
				"PREFIX excp: <http://example.com/categories/products/>\r\n" + 
				"\r\n" + 
				"DESCRIBE excp:Smartphone ?p ?o\r\n" + 
				"FROM exc:Categoria1\r\n" + 
				"WHERE\r\n" + 
				"  { excp:Smartphone ?p ?o }\r\n" + 
				""; 
		*/
		
		String constructQuery = "PREFIX exc: <http://example.com/categories/>\r\n" + 
				"PREFIX excp: <http://example.com/categories/products/>\r\n" + 
				"\r\n" + 
				"CONSTRUCT { ?s ?p ?o }\r\n" + 
				"\r\n" + 
				"WHERE\r\n" + 
				"{\r\n" + 
				"GRAPH exc:Categoria1 { ?s ?p ?o } .\r\n" + 
				"} ";
		
			
		Query query = QueryFactory.create(constructQuery);
		QueryExecution qexec = QueryExecutionFactory.sparqlService(sparqlEndpoint, query);
		
		Model results = qexec.execConstruct();
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		results.write(outputStream, "JSON-LD");
		String output = new String(outputStream.toByteArray());
		
		return output;
		
		
		}

//@GET
public String getProduct(String prodName) {
	auth.getAuthentication();

	String q = "PREFIX ex: <http://example.com/>\r\n" + 
			"\r\n" + 
			"SELECT ?s ?p ?o\r\n" + 
			"FROM ex:Produtos\r\n" + 
			"WHERE\r\n" + 
			"  { ex:"+prodName+" ?p ?o }";
	
			
	Query query = QueryFactory.create(q);
	QueryExecution qexec = QueryExecutionFactory.sparqlService(sparqlEndpoint, query);
	
	ResultSet results = qexec.execSelect();	
	
	ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

	ResultSetFormatter.outputAsJSON(outputStream, results);

	String json = new String(outputStream.toByteArray());

	return json;
	}


//@POST
public void addProduct(String category, Product newProduct) {
		auth.getAuthentication();
		
		String productURI = newProduct.getProductURI();
		String type = newProduct.getType();
		String name = newProduct.getName();
		String description = newProduct.getDescription();
		
		String queryUpdate = "\r\n" + 
				"PREFIX gr: <http://purl.org/goodrelations/v1#>\r\n" + 
				"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\r\n" + 
				"PREFIX exc: <http://example.com/categories/>\r\n" + 
				"PREFIX pto: <http://www.productontology.org/id/> \r\n" + 
				"\r\n" + 
				"\r\n" + 
				"INSERT DATA\r\n" + 
				"{\r\n" + 
				"  GRAPH exc:"+category+" \r\n" + 
				"  {\r\n" + 
				"<"+productURI+">           rdf:type          pto:"+type+",\r\n" + 
				"                                                     gr:SomeItems;\r\n" + 
				"                                gr:name         '"+name+"'@pt;\r\n" + 
				"                                gr:description          '"+description+"'@pt .\r\n" + 
				"                         \r\n" + 
				"\r\n" + 
				" }\r\n" + 
				"}\r\n" + 
				";\r\n" + 
				"";
		
		
		/*
		String replace1 = newProduct.replace("BEGIN", "INSERT DATA\r\n" + 
				"{\r\n" + 
				"  GRAPH <http://example.com/categories/"+category+"> \r\n" + 
				"  {\r\n" + 
				"");
		
		String queryUpdate = replace1.replace("END", "}};");
		*/
		UpdateRequest request = UpdateFactory.create(queryUpdate);
		UpdateProcessor up = UpdateExecutionFactory.createRemote(request, sparqlEndpoint);
		up.execute();
		
	}

//@PUT
public void updateProduct(String productID, String newProduct) {
	
	auth.getAuthentication();
	
	String queryUpdate = "PREFIX ex: <http://example.com/>\r\n" + 
			"PREFIX gr: <http://purl.org/goodrelations/v1#>\r\n"+
			"DELETE { ex:"+productID+" ?p ?o } WHERE { ex:"+productID+" ?p ?o };\r\n" + 
			"		\r\n" + 
			"INSERT DATA \r\n" + 
			"	{ GRAPH ex:Produtos { "+newProduct+" } }";
	
	UpdateRequest request = UpdateFactory.create(queryUpdate);
	UpdateProcessor up = UpdateExecutionFactory.createRemote(request, sparqlEndpoint);
	up.execute();
}

//DELETE
public void deleteCategory(String prodName) {
	auth.getAuthentication();
	
	String updateQuery = 
			"PREFIX ex: <http://example.com/>\r\n" + 
			"\r\n" + 
			"DELETE { ex:"+prodName+" ?p ?o } WHERE { ex:"+prodName+" ?p ?o }";
					
	UpdateRequest request = UpdateFactory.create(updateQuery);
	UpdateProcessor up = UpdateExecutionFactory.createRemote(request, sparqlEndpoint);
	up.execute();
	
}


}
