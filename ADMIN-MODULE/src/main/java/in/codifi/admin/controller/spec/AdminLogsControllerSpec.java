package in.codifi.admin.controller.spec;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jboss.resteasy.reactive.RestResponse;

import in.codifi.admin.model.response.AccesslogResponseModel;
import in.codifi.admin.model.response.GenericResponse;

public interface AdminLogsControllerSpec {

	/**
	 * Method to get the total logged in details for past days
	 * 
	 * @author LOKESH
	 * @param requestContext
	 * @return
	 */
	@Path("/userLogDetails")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public RestResponse<GenericResponse> userLogDetails();

	/**
	 * method to get the user based records from the data base (TOP 10 USER)
	 * 
	 * @author LOKESH
	 * @param accesslogDTO
	 * @return
	 */
	@Path("/getUserbasedRecords")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public RestResponse<GenericResponse> getUserbasedRecords(AccesslogResponseModel accessModel);

	/**
	 * method to get the user based records from the data base (TOP 10 USER)
	 * 
	 * @author LOKESH
	 * @param accesslogDTO
	 * @return
	 */
	@Path("/getUrlBasedRecords")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public RestResponse<GenericResponse> getUrlBasedRecords();

	/**
	 * Method to get last 12 hour login count
	 * 
	 * @author LOKESH
	 * @return
	 */
	@Path("/getLast12hourLoginCount")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public RestResponse<GenericResponse> getLast12hourLoginCount();

	/**
	 * Method to get distinct url for drop down
	 * 
	 * @author LOKESH
	 * @return
	 */
	@Path("/getDistinctUrl")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public RestResponse<GenericResponse> getDistinctUrl();

	/**
	 * Method to get user logged in details
	 * 
	 * @author SOWMIYA
	 * @return
	 */
	@Path("/login/details")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	RestResponse<GenericResponse> getUserLoggedInDetails();

}
