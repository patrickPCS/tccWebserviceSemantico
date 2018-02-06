package tcc.iff.rdf.webservice.resources;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;

@Path("/companies")
public class CompanyResources {
	
	@GET
	public String getAllCompanies() {
		return "Método getAllCompanies ok";
	}
	
	@DELETE
	public String deleteAllCompanies() {
		return "Método deleteAllCompanies ok";
	}
	
	@POST
	public String addCompany() {
		return "Método addCompany ok";
	}
	
	@GET 
	@Path("/{CompanyID}")
	public String getCompany() {
		return "Método getCompany ok";
	}
	
	@DELETE 
	@Path("/{CompanyID}")
	public String deleteCompany() {
		return "Método deleteCompany ok";
	}
	
	@PUT
	@Path("/{CompanyID}")
	public String updateCompany() {
		return "Método updateCompany ok";
	}
	
	
/********************************************************************************/	
	
	
	@Path("/{companyID}/categories")
	public CategoryResources get1CategoryResource() {
		return new CategoryResources();
	}
	
	@Path("/categories")
	public CategoryResources get2CategoryResource() {
		return new CategoryResources();
	}
	

}
