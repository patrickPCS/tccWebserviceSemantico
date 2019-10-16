package tcc.iff.rdf.webservice.services;

import java.io.ByteArrayOutputStream;
import java.util.Iterator;
import java.util.List;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonValue;
import javax.json.JsonWriter;
import javax.ws.rs.core.Response;

import org.apache.jena.graph.Node;
import org.apache.jena.graph.NodeFactory;
import org.apache.jena.graph.Triple;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;
import org.apache.jena.shared.PrefixMapping;
import org.apache.jena.update.UpdateExecutionFactory;
import org.apache.jena.update.UpdateFactory;
import org.apache.jena.update.UpdateProcessor;
import org.apache.jena.update.UpdateRequest;
import org.apache.jena.vocabulary.RDF;

import tcc.iff.rdf.webservice.connection.Authentication;
import tcc.iff.rdf.webservice.model.ProductType;


public class ProductTypesServices {
	Methods methods = new Methods();
	String sparqlEndpoint = "http://localhost:10035/catalogs/CatalogoGR/repositories/RepositorioGR/sparql";
	Authentication auth = new Authentication();
	

	//@GET All
	public String getAllProductTypes() {
		auth.getAuthentication();

		String querySelect = methods.getAllProductTypesSparqlSelect();

		Query query = QueryFactory.create(querySelect);
		QueryExecution qexec = QueryExecutionFactory.sparqlService(sparqlEndpoint, query);

		ResultSet results = qexec.execSelect();
		JsonArrayBuilder jsonArrayAdd = Json.createArrayBuilder();
		String p = "ProductTypes";
		while(results.hasNext()) {
			jsonArrayAdd.add(results.nextSolution().getResource(p).getURI());
		}
		JsonArray ja = jsonArrayAdd.build();
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		JsonWriter writer = Json.createWriter(outputStream);
		writer.writeArray(ja);
		String output = new String(outputStream.toByteArray());

		return output;
	}

	//@GET
	public Response getProductType(String productTypeID, String accept) {
		auth.getAuthentication();
		String format;

		String queryDescribe = methods.getProducttypeSparqlDescribe(productTypeID);

		Query qr = QueryFactory.create(queryDescribe);
		QueryExecution qx = QueryExecutionFactory.sparqlService(sparqlEndpoint, qr);

		Model rst = qx.execDescribe();
		if (rst.isEmpty()) {
			return Response.status(422)
					.entity("Please, choose a valid ProductType ID.")
					.build();
		}
		
		if(accept.equals("application/json") || methods.isValidFormat(accept)==false)
		{
			String q = methods.getProductTypeSparqlSelect(productTypeID);

			Query query = QueryFactory.create(q);
			QueryExecution qexec = QueryExecutionFactory.sparqlService(sparqlEndpoint, query);
			ResultSet results = qexec.execSelect();

			QuerySolution soln = results.nextSolution() ;
			String label = soln.getLiteral("label").toString() ;
			//String name = soln.getLiteral("name").toString();
			String homepage = soln.getResource("homepage").toString() ;
			String description = soln.getLiteral("description").toString() ;
			String comment = soln.getLiteral("comment").toString();
			String subClassOf = soln.getResource("subClassOf").toString() ;

			JsonObject jobj = Json.createObjectBuilder()
					.add("id", productTypeID)
					.add("label",label)
					//.add("name", name)
					.add("homepage",homepage)
					.add("description",description)
					.add("comment",comment)
					.add("subClassOf", subClassOf)
					.build();

			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			JsonWriter writer = Json.createWriter(outputStream);
			writer.writeObject(jobj);
			String output = new String(outputStream.toByteArray());
			writer.close();

			return Response.status(Response.Status.OK)
					.entity(output)
					.build();
		}

		else{

			format = methods.convertFromAcceptToFormat(accept);

			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			rst.write(outputStream, format);
			String output = new String(outputStream.toByteArray());

			return Response.status(Response.Status.OK)
					.entity(output)
					.build();
		}
	}

	//@POST
	public Response addProductType(List<ProductType> newProductType) {
		auth.getAuthentication();

		int TAM;
		TAM = newProductType.size();
		JsonArrayBuilder jsonArrayAdd = Json.createArrayBuilder();
		String exp = "http://localhost:8080/webservice/webapi/producttypes/";
		for(int i=0; i<TAM; i++) {
			String id = newProductType.get(i).getId();
			String label = newProductType.get(i).getLabel();
			String name =  newProductType.get(i).getLabel();
			String homepage = newProductType.get(i).getHomepage();
			String description = newProductType.get(i).getDescription();
			String comment = newProductType.get(i).getDescription();
			String subClassOf = newProductType.get(i).getSubClassOf();
			String superClassURI = "exp:"+subClassOf+"";

			if (subClassOf.isEmpty()) {
				return Response.status(422)
						.entity("The 'subClassOf' field is required! If you don't know which Class this"
								+ " belongs to, then fill it with 'ProductOrService' and submit again.")
						.build();
			}else
				if(subClassOf.contentEquals("ProductOrService")) {
					superClassURI = "gr:ProductOrService";		
				}else {

					String queryDescribe = methods.getProducttypeSparqlDescribeExp(subClassOf);

					Query query = QueryFactory.create(queryDescribe);
					QueryExecution qexec = QueryExecutionFactory.sparqlService(sparqlEndpoint, query);

					Model results = qexec.execDescribe();
					if (results.isEmpty()) {
						superClassURI = "pto:"+subClassOf+"";	
						String queryDescribe2 = methods.getProducttypeSparqlDescribePto(subClassOf);

						Query query2 = QueryFactory.create(queryDescribe2);
						QueryExecution qexec2 = QueryExecutionFactory.sparqlService(sparqlEndpoint, query2);

						Model results2 = qexec2.execDescribe();
						if (results2.isEmpty()) {

							return Response.status(422)
									.entity("Please, fill the 'subClassOf' field with a valid Class ID! "
											+ "If you don't know which Class this belongs to, then fill it with 'ProductOrService' and submit again.")
									.build();
						}
					}

				}


			String qD = methods.getProducttypeSparqlDescribe(id);

			Query q = QueryFactory.create(qD);
			QueryExecution qex = QueryExecutionFactory.sparqlService(sparqlEndpoint, q);

			Model resultados = qex.execDescribe();
			if (resultados.isEmpty()) {

				String queryUpdate = methods.insertProducttypeSparql(id, superClassURI, label, name, homepage, description, comment);

				UpdateRequest request = UpdateFactory.create(queryUpdate);
				UpdateProcessor up = UpdateExecutionFactory.createRemote(request, sparqlEndpoint);
				up.execute();

				jsonArrayAdd.add(exp+id);

			}else
			{
				String message = "CONFLICT: the ProductType "+id+" already exists: "+exp+id;
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
		return Response.status(Response.Status.CREATED).
				entity(output)
				.build();	
	}

	//@PUT
	public Response updateProductType(String oldProductID, ProductType newProductType) {

		auth.getAuthentication();

		String exp = "http://localhost:8080/webservice/webapi/producttypes/";
		String id = newProductType.getId();
		String label = newProductType.getLabel();
		String name = newProductType.getLabel();
		String homepage = newProductType.getHomepage();
		String description = newProductType.getDescription();
		String comment = newProductType.getDescription();
		String subClassOf = newProductType.getSubClassOf();
		String superClassURI = "exp"+subClassOf;

		if (subClassOf.isEmpty()) {
			return Response.status(422)
					.entity("The 'subClassOf' field is required! If you don't know which Class this"
							+ " belongs to, then fill it with 'ProductOrService' and submit again.")
					.build();
		}else
			if(subClassOf.contentEquals("ProductOrService")) {
				superClassURI = "gr:ProductOrService";		
			}else {

				String queryDescribe = methods.getProducttypeSparqlDescribeExp(subClassOf);

				Query query = QueryFactory.create(queryDescribe);
				QueryExecution qexec = QueryExecutionFactory.sparqlService(sparqlEndpoint, query);

				Model results = qexec.execDescribe();
				if (results.isEmpty()) {
					superClassURI = "pto:"+subClassOf+"";	
					String queryDescribe2 = methods.getProducttypeSparqlDescribePto(subClassOf);

					Query query2 = QueryFactory.create(queryDescribe2);
					QueryExecution qexec2 = QueryExecutionFactory.sparqlService(sparqlEndpoint, query2);

					Model results2 = qexec2.execDescribe();
					if (results2.isEmpty()) {

						return Response.status(422)
								.entity("Please, fill the 'subClassOf' field with a valid Class ID! "
										+ "If you don't know which Class this belongs to, then fill it with 'ProductOrService' and submit again.")
								.build();
					}
				}

			}

		String queryUpdate = methods.insertProducttypeSparql(id, superClassURI, label, name, homepage, description,comment);

		UpdateRequest request = UpdateFactory.create(queryUpdate);
		UpdateProcessor up = UpdateExecutionFactory.createRemote(request, sparqlEndpoint);
		up.execute();

		JsonArrayBuilder jsonArrayAdd = Json.createArrayBuilder();
		jsonArrayAdd.add(exp+id);
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
	public void deleteProductType(String productID) {
		auth.getAuthentication();

		String updateQuery = methods.deleteProducttypeSparql(productID);

		UpdateRequest request = UpdateFactory.create(updateQuery);
		UpdateProcessor up = UpdateExecutionFactory.createRemote(request, sparqlEndpoint);
		up.execute();

	}

	//DELETE ALL 
	public void deleteAllProductsTypes() {
		auth.getAuthentication();

		String updateQuery = methods.deleteAllProducttypes();

		UpdateRequest request = UpdateFactory.create(updateQuery);
		UpdateProcessor up = UpdateExecutionFactory.createRemote(request, sparqlEndpoint);
		up.execute();
	}
}












