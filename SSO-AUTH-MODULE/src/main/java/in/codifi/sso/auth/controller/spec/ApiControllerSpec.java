package in.codifi.sso.auth.controller.spec;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jboss.resteasy.reactive.RestResponse;

import in.codifi.sso.auth.model.request.ApiKeyReqModel;
import in.codifi.sso.auth.model.response.GenericResponse;

public interface ApiControllerSpec {

	/**
	 * Method to get API Key
	 * 
	 * @author Dinesh Kumar
	 * @return
	 */
	@Path("/key")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	RestResponse<GenericResponse> getApiKey();

	/**
	 * Method to generate API Key
	 * 
	 * @author Dinesh Kumar
	 * @return
	 */
	@Path("/key/generate")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	RestResponse<GenericResponse> generateApiKey(ApiKeyReqModel req);
	
	/**
	 * Method to generate API Key
	 * 
	 * @author Dinesh Kumar
	 * @return
	 */
	@Path("/key/regenerate")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	RestResponse<GenericResponse> reGenerateApiKey(ApiKeyReqModel req);
}
