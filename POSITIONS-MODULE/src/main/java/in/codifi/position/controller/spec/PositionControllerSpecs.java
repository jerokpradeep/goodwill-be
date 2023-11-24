package in.codifi.position.controller.spec;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.jboss.resteasy.reactive.RestResponse;

import in.codifi.position.model.request.PositionConversionReq;
import in.codifi.position.model.response.GenericResponse;

public interface PositionControllerSpecs {

	/**
	 * Method to get the position
	 * 
	 * @author Nesan
	 *
	 */
	@Path("/")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@APIResponse(description = "Get position")
	RestResponse<GenericResponse> getposition();

	/**
	 * Method to get the position conversion
	 * 
	 * @author Nesan
	 *
	 */
	@Path("/conversion")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@APIResponse(description = "Position conversion")
	RestResponse<GenericResponse> positionConversion(PositionConversionReq positionConversionReq);

}
