package in.codifi.sso.auth.controller.spec;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jboss.resteasy.reactive.RestResponse;

import in.codifi.sso.auth.entity.primary.VendorAppEntity;
import in.codifi.sso.auth.model.response.GenericResponse;

public interface VendorControllerSpec {

	/**
	 * Method to get vendor app details
	 * 
	 * @author Dinesh Kumar
	 * @return
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/")
	RestResponse<GenericResponse> getVendorAppDetails();

	/**
	 * Method to create new vendor app
	 * 
	 * @author Dinesh Kumar
	 * @param vendorAppReqModel
	 * @return
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/create")
	RestResponse<GenericResponse> createNewVendorApp(VendorAppEntity entity);

	/**
	 * Method to update vendor details
	 * 
	 * @author Dinesh Kumar
	 * @param entity
	 * @return
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/update")
	RestResponse<GenericResponse> updateVendorApp(VendorAppEntity entity);

	/**
	 * Method to delete vendor
	 * 
	 * @param appId
	 * @return
	 */
	@Path("/delete/{appId}")
	@DELETE
	@Produces(MediaType.APPLICATION_JSON)
	RestResponse<GenericResponse> deleteVendor(@PathParam("appId") long appId);

	/**
	 * method to reset api secret
	 * 
	 * @author SowmiyaThangaraj
	 * @param appId
	 * @return
	 */
	@Path("/reset/{appId}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	RestResponse<GenericResponse> restAPISecret(@PathParam("appId") long appId);

}
