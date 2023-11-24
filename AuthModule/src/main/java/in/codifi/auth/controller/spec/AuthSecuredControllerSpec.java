package in.codifi.auth.controller.spec;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jboss.resteasy.reactive.RestResponse;

import in.codifi.auth.model.request.AuthReq;
import in.codifi.auth.model.response.GenericResponse;

public interface AuthSecuredControllerSpec {


	/**
	 * Method to logout
	 * @author Dinesh Kumar
	 * @return
	 */
	@Path("/logout")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	RestResponse<GenericResponse> logout(AuthReq authReq);

	/**
	 * Method to re login
	 * @author Dinesh Kumar
	 * @return
	 */
	@Path("/relogin")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	RestResponse<GenericResponse> reLogin(AuthReq authReq);
}
