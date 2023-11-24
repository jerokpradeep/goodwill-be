package in.codifi.api.controller.spec;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.jboss.resteasy.reactive.RestResponse;

import in.codifi.api.model.MWReqModel;
import in.codifi.api.model.MwRequestModel;
import in.codifi.api.model.ResponseModel;

public interface AdvanceMWControllerSpec {

	/**
	 * method to get advance market watch
	 * 
	 * @author sowmiya
	 * @param reqModel
	 * @return
	 */
	@Path("/mw")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public RestResponse<ResponseModel> advanceMW(MWReqModel reqModel);

	/**
	 * method to get advance market watch scrips
	 * 
	 * @author sowmiya
	 * @param reqModel
	 * @return
	 */
	@Path("/mw/scrips")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public RestResponse<ResponseModel> advanceMWScrips(MWReqModel reqModel);

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

}
