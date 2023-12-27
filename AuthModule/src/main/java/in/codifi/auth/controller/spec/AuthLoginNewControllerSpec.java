package in.codifi.auth.controller.spec;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jboss.resteasy.reactive.RestResponse;

import in.codifi.auth.model.request.AuthReq;
import in.codifi.auth.model.response.GenericResponse;

public interface AuthLoginNewControllerSpec {

	/**
	 * method to forgot password otp
	 * 
	 * @author SowmiyaThangaraj
	 */
	@Path("/otp/send")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	RestResponse<GenericResponse> forgotPwdotp(AuthReq authmodel);

	/**
	 * method to quick auth login
	 * 
	 * @author SowmiyaThangaraj
	 */
	@Path("/otp/validate")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	RestResponse<GenericResponse> quickAuthLogin(AuthReq authmodel);

}
