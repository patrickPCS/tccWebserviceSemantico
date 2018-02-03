package tcc.iff.rdf.webservice.resources;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.http.impl.auth.HttpAuthenticator;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.ResultSetFormatter;
import org.apache.jena.rdf.model.Model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import tcc.iff.rdf.webservice.RDFMediaType;
import tcc.iff.rdf.webservice.data.Category;
//import tcc.iff.rdf.webservice.connection.Authentication;
import tcc.iff.rdf.webservice.services.CategoryServices;

@Path("/categories")
public class CategoryResources {
	
	CategoryServices catServices = new CategoryServices();
	
	
	@GET
    @Produces(RDFMediaType.APPLICATION_JSON_LD)
    public Object  listarCategorias() {		
		return catServices.getAllCategories().toString()
				;
    }
	
	
	@GET
	@Produces(RDFMediaType.APPLICATION_JSON_LD)
	@Path("{CatName}")
	public Object getCategory(@PathParam("CatName") String catName) {
		
		return catServices.getCat(catName);
	}
	
	
		
	@POST
	@Consumes(RDFMediaType.APPLICATION_JSON_LD)
	public Response adicionarCategoria(Category newCat) {
	//public Response adicionarCategoria(@PathParam("newCat") String newCat, @Context UriInfo uriInfo) {
		//auth.getAuthentication();
		 catServices.addCategory(newCat);
		//URI uri = uriInfo.getAbsolutePathBuilder().build();
		/*return Response.created(uri)
				.entity(newCat)
				.build();
		*/
	   return Response.status(Response.Status.CREATED).build();
	}
	
	@PUT
	@Path("{oldName}/{newName}")
	public Response alterarCategoria(@PathParam("oldName") String oldName, @PathParam("newName") String newName) {
		catServices.updateCategory(oldName,newName);
		   return Response.status(Response.Status.OK).build();
	}

	@DELETE
	@Path("{CatName}")
	public Response removerCategoria(@PathParam("CatName") String catName) {
		catServices.deleteCategory(catName);
		
		return Response.status(Response.Status.NO_CONTENT).build();

	}
	
	
}
