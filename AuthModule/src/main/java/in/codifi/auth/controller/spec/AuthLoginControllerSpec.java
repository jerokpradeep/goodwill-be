package in.codifi.auth.controller.spec;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jboss.resteasy.reactive.RestResponse;

import in.codifi.auth.model.request.AuthReq;
import in.codifi.auth.model.response.GenericResponse;
import in.codifi.cache.model.ClinetInfoModel;

public interface AuthLoginControllerSpec {

	/**
	 * 
	 * Method to check user exist or not
	 * 
	 * @author sowmiya
	 *
	 * @param authmodel
	 * @return
	 */
	@Path("/login")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	RestResponse<GenericResponse> quickAuthLogin(AuthReq authmodel);

	/**
	 * 
	 * Method to send OTP for 2FA verification
	 * 
	 * @author sowmiyathangaraj
	 *
	 * @param authmodel
	 * @return
	 */
	@Path("/otp/send")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	RestResponse<GenericResponse> sendOtpFor2FA(AuthReq authmodel);

	/**
	 * 
	 * Method to validate OTP for 2FA
	 * 
	 * @author sowmiyathangaraj
	 *
	 * @param authmodel
	 * @return
	 */
	@Path("/otp/validate")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	RestResponse<GenericResponse> validate2FAOTP(AuthReq authmodel);

	/**
	 * method to forgot password
	 * 
	 * @author SowmiyaThangaraj
	 */
	@Path("/forgot/pwd")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	RestResponse<GenericResponse> forgotPwd(AuthReq authmodel);

	/**
	 * method to unblock users
	 * 
	 * @author SowmiyaThangaraj
	 */
	@Path("/unblock")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	RestResponse<GenericResponse> unblockUser(AuthReq authmodel);

	/**
	 * method to change password
	 * 
	 * @author SowmiyaThangaraj
	 * @param authmodel
	 * @return
	 */
	@Path("/change/pwd")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	RestResponse<GenericResponse> changePwd(AuthReq authmodel);

	/**
	 * method to logout
	 * 
	 * @author SowmiyaThangaraj
	 * @param authmodel
	 * @return
	 */
	@Path("/logout")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	RestResponse<GenericResponse> logout();

	/**
	 * Method to logout user from keycloak by source
	 * 
	 * @author SowmiyaThangaraj
	 * @param authReq
	 * @return
	 */
	@Path("/client/logout")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	RestResponse<GenericResponse> kcLogout(AuthReq authReq);

	/**
	 * Method to get user logged in details
	 * 
	 * @author SOWMIYA
	 * @return
	 */
	@Path("/login/details")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	RestResponse<GenericResponse> getUserLoggedInDetails();

	/**
	 * 
	 * Method to validate password for bio metric login
	 * 
	 * @author Dinesh Kumar
	 *
	 * @param authReq
	 * @return
	 */
	@Path("/bio/pwd/validate/test")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	RestResponse<GenericResponse> validatePasswordForBio(AuthReq authReq);

}
