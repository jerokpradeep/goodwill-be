package in.codifi.admin.controller.spec;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jboss.resteasy.reactive.RestResponse;

import in.codifi.admin.model.request.SendNoficationReqModel;
import in.codifi.admin.model.request.UserReqModel;
import in.codifi.admin.model.response.GenericResponse;
import in.codifi.admin.model.response.KcUserDetailsRequest;
import in.codifi.admin.req.model.MobVersionReqModel;
import in.codifi.admin.req.model.VendorAppReqModel;
import in.codifi.admin.ws.model.kc.CreateUserRequestModel;

public interface CommonControllerSpec {

	/**
	 * Method to send recommendation message
	 * 
	 * @author Gowthaman
	 * @param reqModel
	 * @return
	 */
	@Path("/msg/send")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public RestResponse<GenericResponse> sendRecommendationMessage(SendNoficationReqModel reqModel);

	/**
	 * method to get mobile version
	 * 
	 * @author LOKESH
	 * @return
	 */
	@Path("/version/get")
	@GET
	@Consumes(MediaType.APPLICATION_JSON)
	public RestResponse<GenericResponse> getMobileVersion();

	/**
	 * method to add mobile version
	 * 
	 * @author LOKESH
	 */
	@Path("/version/add")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public RestResponse<GenericResponse> addVersion(MobVersionReqModel model);

	/**
	 * method to update mobile version
	 * 
	 * @author LOKESH
	 */
	@Path("/version/update")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public RestResponse<GenericResponse> updateVersion(MobVersionReqModel model);

	/**
	 * method to delete mobile version
	 * 
	 * @author LOKESH
	 */
	@Path("/version/delete")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public RestResponse<GenericResponse> deleteVersion(MobVersionReqModel model);

	/**
	 * Method to get api from Vendor
	 * 
	 * @author LOKESH
	 * @return
	 */
	@Path("/getVendors")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public RestResponse<GenericResponse> getVendor(VendorAppReqModel VendorAppModel);

	/**
	 * Method to Update authorization_status
	 * 
	 * @author LOKESH
	 * @return
	 */
	@Path("/updateAuthorize")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public RestResponse<GenericResponse> updateVendor(VendorAppReqModel VendorAppModel);
	
	/**
	 * Method to get kc user details
	 * 
	 * @author Gowthaman
	 * @param req
	 * @return
	 */
	@Path("/getKcUserDetails")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public RestResponse<GenericResponse> getKcUserDetails(KcUserDetailsRequest req);
	
	/**
	 * Method to get kc All user details
	 * 
	 * @author Gowthaman
	 * @param req
	 * @return
	 */
	@Path("/getKcAllUserDetails")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public RestResponse<GenericResponse> getKcAllUserDetails(KcUserDetailsRequest req);
	
	/**
	 * Method to get kc User details in csv
	 * 
	 * @author Gowthaman
	 * @return
	 */
	@Path("/getUserDetailsCsv")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public RestResponse<GenericResponse> getUserDetailsCsv();
	
	/**
	 * Method to update Kc User Details
	 * 
	 * @author Gowthaman
	 * @param req
	 * @return
	 */
	@Path("/updateKcUserDetails")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public RestResponse<GenericResponse> updateKcUserDetails(CreateUserRequestModel user);
	
	/**
	 * Method to add New User in keyclock
	 * 
	 * @author Gowthaman
	 * @param req
	 * @return
	 */
	@Path("/addNewUser")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public RestResponse<GenericResponse> addNewUser(CreateUserRequestModel user);
	
	
	@Path("/notification/list")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	RestResponse<GenericResponse> getUserNotificationList(UserReqModel reqModel);

}
