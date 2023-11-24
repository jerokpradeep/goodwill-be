package in.codifi.api.controller.spec;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.jboss.resteasy.reactive.RestResponse;

import in.codifi.api.entity.primary.PredefinedMwEntity;
import in.codifi.api.model.MwRequestModel;
import in.codifi.api.model.PreDefMwReqModel;
import in.codifi.api.model.ResponseModel;

public interface IPredefinedMwContoller {

	/**
	 * 
	 * Method to get all predefined market watch
	 * 
	 * @author Dinesh Kumar
	 *
	 * @param pDto
	 * @return
	 */
	@Path("/get")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@APIResponse(description = "Get Scrip details for given user id .")
	RestResponse<ResponseModel> getAllPreDedinedMwScrips(MwRequestModel pDto);

	/**
	 * Method to add the script
	 * 
	 * @author SOWMIYA
	 */
	@Path("/addscrips")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	RestResponse<ResponseModel> addscrip(PredefinedMwEntity predefinedMwEntity);

	/**
	 * Method to delete the script
	 * 
	 * @author SOWMIYA
	 */
	@Path("/deletescrips")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	RestResponse<ResponseModel> deletescrip(PredefinedMwEntity predefinedEntity);

	/**
	 * Method to0 sort mw scrips
	 * 
	 * @author SOWMIYA
	 */
	@Path("/sortscrips")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	RestResponse<ResponseModel> sortMwScrips(PredefinedMwEntity predefinedEntity);

	@Path("/getpdmw")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	RestResponse<ResponseModel> getPDMwScrips(PreDefMwReqModel pDto);

	/**
	 * Method to get pre defined market watch name
	 * 
	 * @author DINESH KUMAR
	 *
	 * @return
	 */
	@Path("/get/mwname")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	RestResponse<ResponseModel> getMwNameList();

	/**
	 * 
	 * Method to get all predefined market watch based on source and preference
	 * 
	 * @author Dinesh Kumar
	 *
	 * @param pDto
	 * @return
	 */
	@Path("/get/pdmw")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	RestResponse<ResponseModel> getPrefedinedMwBySource(@QueryParam("source") String source);

}
