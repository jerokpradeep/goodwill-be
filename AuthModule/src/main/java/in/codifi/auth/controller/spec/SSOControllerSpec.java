package in.codifi.auth.controller.spec;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jboss.resteasy.reactive.RestResponse;

import in.codifi.auth.model.request.VendorReqModel;
import in.codifi.auth.model.response.GenericResponse;

public interface SSOControllerSpec {

	/**
	 * 
	 * Method to authorize Vendor
	 * 
	 * @author Dinesh Kumar
	 *
	 * @param vendorReqModel
	 * @return
	 */
	@Path("/vendor/authorize")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	RestResponse<GenericResponse> ssoAuthorizeVendor(VendorReqModel vendorReqModel);

	/**
	 * Method to check Vendor Authorization
	 * 
	 * @author dinesh
	 * @param vendorReqModel
	 * @return
	 */
	@Path("/vendor/authorize/check")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	RestResponse<GenericResponse> checkVendorAuthorization(VendorReqModel vendorReqModel);

	@Path("/vendor/getUserDetails")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	Object getUserDetails(VendorReqModel vendorReqModel);

	@Path("/vendor/auth/getUserDetails")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	Object getUserDetailsByAuth(VendorReqModel vendorReqModel);

	/**
	 * Method to get vendor APP details
	 * 
	 * @author Dinesh Kumar
	 * @param vendorReqModel
	 * @return
	 */
	@Path("/vendor/deatils")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	RestResponse<GenericResponse> getVendorAppDetails(VendorReqModel vendorReqModel);

}
