package in.codifi.admin.controller.spec;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jboss.resteasy.reactive.RestResponse;

import in.codifi.admin.model.response.GenericResponse;

public interface UserControllerSpec {

	/**
	 * Method to get user logged in details
	 * 
	 * @author LOKESH
	 * @return
	 */
	@Path("/login/details")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	RestResponse<GenericResponse> getUserLoggedInDetails();
	
	
	/**
	 * Method to Truncate user logged in details
	 * 
	 * @author LOKESH
	 * @return
	 */
	@Path("/truncate/details")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	RestResponse<GenericResponse> truncateUserLoggedInDetails();
}
