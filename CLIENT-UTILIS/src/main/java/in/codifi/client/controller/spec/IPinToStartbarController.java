package in.codifi.client.controller.spec;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jboss.resteasy.reactive.RestResponse;

import in.codifi.client.model.request.PinToStartbarModel;
import in.codifi.client.model.response.GenericResponse;

public interface IPinToStartbarController {


	/**
	 * Method to get pin to start bar details
	 * 
	 * @author GOWTHAMAN M
	 * @return
	 */
	@Path("/get")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	RestResponse<GenericResponse> getPinToStartBar();

	/**
	 * Method to get pin to start bar details
	 * 
	 * @author GOWTHAMAN M
	 * @return
	 */
	@Path("/get/mob")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	RestResponse<GenericResponse> getPinToStartBarForMob();

	/**
	 * Method to add pin to start bar details
	 * 
	 * @author GOWTHAMAN M
	 * @return
	 */
	@Path("/add")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	RestResponse<GenericResponse> addPinToStartBar(PinToStartbarModel reqModel);

	/**
	 * Method to reload cache
	 * 
	 * @author Dinesh Kumar
	 * @return
	 */
	@Path("/reload")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	RestResponse<GenericResponse> reloadCache();

	/**
	 * Method to add pin to start bar details forr mobile
	 * 
	 * @author Dinesh Kumar
	 * @return
	 */
	@Path("/add/mob")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	RestResponse<GenericResponse> addPinToStartBarForMob(PinToStartbarModel model);

}
