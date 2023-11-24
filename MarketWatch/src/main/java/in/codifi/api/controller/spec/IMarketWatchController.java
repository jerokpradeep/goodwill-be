package in.codifi.api.controller.spec;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.jboss.resteasy.reactive.RestResponse;

import in.codifi.api.model.MwRequestModel;
import in.codifi.api.model.ResponseModel;

public interface IMarketWatchController {

	/**
	 * Method to get the scrip details for the given User Id *
	 * 
	 * @author Gowrisankar
	 * @param pDto
	 * @return
	 */
	@Path("/getAllMwScrips")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@APIResponse(description = "Get Scrip details for given user id .")
	public RestResponse<ResponseModel> getAllMwScrips(MwRequestModel pDto);

	/**
	 * Method to get the Scrip for given user id and market watch Id
	 * 
	 * @author Gowrisankar
	 * @param pDto
	 * @return
	 */
	@Path("/getMWScrips")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@APIResponse(description = "Get Scrip details for given user id and Market Watch.")
	public RestResponse<ResponseModel> getMWScrips(MwRequestModel pDto);

	/**
	 * Method to delete the scrips from the cache and market watch
	 * 
	 * @author Gowrisankar
	 * @param pDto
	 * @return
	 */
	@Path("/deletescrip")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@APIResponse(description = "To delete symbol in market Watch")
	public RestResponse<ResponseModel> deletescrip(MwRequestModel pDto);

	/**
	 * Method to add the scrip into cache and data base
	 * 
	 * @author Gowrisankar
	 * @param pDto
	 * @return
	 */
	@Path("/addscrip")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@APIResponse(description = "To add symbol in market Watch")
	public RestResponse<ResponseModel> addscrip(MwRequestModel pDto);

	@Path("/sortMwScrips")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@APIResponse(description = "To add symbol in market Watch")
	public RestResponse<ResponseModel> sortMwScrips(MwRequestModel pDto);

	/**
	 * Method to create the new marketWatch
	 * 
	 * @author Dinesh Kumar
	 * @param requestContext
	 * @return
	 */
	@Path("/createMW")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@APIResponse(description = "To create market Watch")
	public RestResponse<ResponseModel> createMW(MwRequestModel pDto);

	/**
	 * Method to change market watch name
	 * 
	 * @param pDto
	 * @return
	 */
	@Path("/renameMW")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@APIResponse(description = "To rename market Watch")
	RestResponse<ResponseModel> renameMarketWatch(MwRequestModel pDto);

	/**
	 * 
	 * Method to Delete expired scrips in MW List
	 * 
	 * @author Dinesh Kumar
	 *
	 * @return
	 */
	@Path("/delete/expired/scrip")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	RestResponse<ResponseModel> deleteExpiredContract();

}
