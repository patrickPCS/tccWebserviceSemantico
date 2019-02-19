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
import tcc.iff.rdf.webservice.model.Product;

public class ProductServices {
	
	String sparqlEndpoint = "http://localhost:10035/catalogs/CatalogoGR/repositories/RepositorioGR/sparql";
	Authentication auth = new Authentication();	
	Methods methods = new Methods();
	
	//GET
	public String getAllProducts() {
		auth.getAuthentication();
		
		String querySelect = methods.getAllProductSparqlSelect();
		
		Query query = QueryFactory.create(querySelect);
		QueryExecution qexec = QueryExecutionFactory.sparqlService(sparqlEndpoint, query);

		ResultSet results = qexec.execSelect();
		JsonArrayBuilder jsonArrayAdd = Json.createArrayBuilder();
		String product = "product";
		while(results.hasNext()) {
		jsonArrayAdd.add(results.nextSolution().getResource(product).getURI());
		}
		JsonArray ja = jsonArrayAdd.build();
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		JsonWriter writer = Json.createWriter(outputStream);
		writer.writeArray(ja);
		String output = new String(outputStream.toByteArray());

		return output;
	}

	//DELETE
	public void deleteCompany(String productID) {
		auth.getAuthentication();
		
		String querySelect = methods.getProductSparqlSelect(productID);
		
		Query querySc = QueryFactory.create(querySelect);
		QueryExecution queryExec = QueryExecutionFactory.sparqlService(sparqlEndpoint, querySc);
		ResultSet result = queryExec.execSelect();
		
		QuerySolution soln = result.nextSolution();
		String productType = soln.getResource("productType").toString();
		
		String queryUpdate = methods.deleteProductSparql(productID, productType);
		UpdateRequest request = UpdateFactory.create(queryUpdate);
		UpdateProcessor up = UpdateExecutionFactory.createRemote(request, sparqlEndpoint);
		up.execute();
	}

	//GET
	public Response getProduct(String productID, String accept) {
		auth.getAuthentication();
		
		String queryDescribe = methods.getProductDescribe(productID);
		
		Query query = QueryFactory.create(queryDescribe);
		QueryExecution qx = QueryExecutionFactory.sparqlService(sparqlEndpoint, query);
		
		Model productDescribe = qx.execDescribe();
		if(productDescribe.isEmpty()) {
			return Response.status(422)
					.entity("Please, choose a valid ProductType ID.")
					.build();
		}
		if(accept.equals("application/json") || methods.isValidFormat(accept)==false) {
			String querySelect = methods.getProductSparqlSelect(productID);
			
			Query querySc = QueryFactory.create(querySelect);
			QueryExecution queryExec = QueryExecutionFactory.sparqlService(sparqlEndpoint, querySc);
			ResultSet result = queryExec.execSelect();
			
			QuerySolution soln = result.nextSolution();
			//String productName = soln.getLiteral("productName").toString();
			String productLabel = soln.getLiteral("productLabel").toString();
			String productURL = soln.getResource("productURL").toString();
			String productDescription = soln.getLiteral("productDescription").toString();
			String productComment = soln.getLiteral("productComment").toString();
			String productLanguage = soln.getLiteral("productLanguage").toString();
			String productType = soln.getResource("productType").toString();
			
			JsonObject jobj = Json.createObjectBuilder()
					.add("ProductID", productID)
					//.add("ProductName", productName)
					.add("ProductLabel", productLabel)
					.add("ProductURL", productURL)
					.add("ProductDescription", productDescription)
					.add("ProductComment", productComment)
					.add("ProductLanguage", productLanguage)
					.add("Tipo de Produto", productType)
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
			productDescribe.write(outputStream, format);
			String output = new String(outputStream.toByteArray());

			return Response.status(Response.Status.OK)
					.entity(output)
					.build();
		}
	}
	
	//POST
	public Response addProduct(List<Product> productList) {
		auth.getAuthentication();
		int TAM;
		TAM = productList.size();
		JsonArrayBuilder jArrayAddproduct = Json.createArrayBuilder();
		String expr = "http://localhost:8080/webservice/webapi/products/";
		for(int i=0; i<TAM; i++) {
			String productID = productList.get(i).getProductID();
			String productName = productList.get(i).getProductName();
			String productLabel = productList.get(i).getProductName();
			String productURL = productList.get(i).getProductURL();
			String productDescription = productList.get(i).getProductDescription();
			String productComment = productList.get(i).getProductDescription();
			String productLanguage = productList.get(i).getProductLanguage();
			String productType = productList.get(i).getProductType();
			
			String queryDescribe  = methods.getProductDescribe(productID);
			Query query = QueryFactory.create(queryDescribe);
			QueryExecution qexec = QueryExecutionFactory.sparqlService(sparqlEndpoint, query);

			Model results = qexec.execDescribe();
			if(results.isEmpty()) {
				String queryInsert = methods.insertProductSpaqrl(productID, productType, productName, productLabel, productDescription, productComment, productLanguage, productURL);
				
				UpdateRequest request = UpdateFactory.create(queryInsert);
				UpdateProcessor up = UpdateExecutionFactory.createRemote(request, sparqlEndpoint);
				up.execute();
			
				jArrayAddproduct.add(expr+productID);
			}else {
				String message = "CONFLICT: already exists a Product with this same ID: "+expr+productID;
				ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
				String output = new String(outputStream.toByteArray());
				output = message;
				return Response.status(Response.Status.CONFLICT)
						.entity(output)
						.build();
			}
		}
		JsonArray ja = jArrayAddproduct.build();
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		JsonWriter writer = Json.createWriter(outputStream);
		writer.writeArray(ja);
		String output = new String(outputStream.toByteArray());
		return Response.status(Response.Status.CREATED)
				   .entity(output)
				   .build();
	}

	//PUT
	public Response updateProduct(String oldProductID, Product newProduct) {
		auth.getAuthentication();
		String expr = "http://localhost:8080/webservice/webapi/products/";
		
		String productID = newProduct.getProductID();
		//String productName = newProduct.getProductName();
		String productLabel = newProduct.getProductName();
		String productURL = newProduct.getProductURL();
		String productDescription = newProduct.getProductDescription();
		String productComment = newProduct.getProductDescription();
		String productLanguage = newProduct.getProductLanguage();
		String productType = newProduct.getProductType();
		
		String queryUpdate = methods.updateProductSparql(oldProductID, productID, productType, productLabel, productDescription, productComment, productLanguage, productURL);
		UpdateRequest request = UpdateFactory.create(queryUpdate);
		UpdateProcessor up = UpdateExecutionFactory.createRemote(request, sparqlEndpoint);
		up.execute();
		
		
		JsonArrayBuilder jsonArrayAdd = Json.createArrayBuilder();
		jsonArrayAdd.add(expr+productID);
		JsonArray ja = jsonArrayAdd.build();
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		JsonWriter writer = Json.createWriter(outputStream);
		writer.writeArray(ja);
		String output = new String(outputStream.toByteArray());
		return Response.status(Response.Status.CREATED)
				.entity(output)
				.build();
	}

}
