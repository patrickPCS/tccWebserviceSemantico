package tcc.iff.rdf.webservice.resources;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;

@Path("/users")
public class UserResources {
	
	@GET
	public String getAllUsers() {
		return "getAllUsers ok";
	}
	
	@POST
	public String addUser() {
		return "addUser ok";
	}
	
	@DELETE
	public String deleteAllUsers() {
		return "deleteAllUsers ok";
	}
	
	@PUT
	@Path("/{UserID}")
	public String updateUser() {
		return "updateUser ok";
	}
	
	@GET
	@Path("/{UserID}")
	public String getUser() {
		return "getUser ok";
	}
	
	@DELETE
	@Path("/{UserID}")
	public String deleteUser() {
		return "deleteUser ok";
	}
}
