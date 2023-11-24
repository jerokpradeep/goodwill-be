package in.codifi.auth.controller;

import javax.inject.Inject;
import javax.ws.rs.Path;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MultivaluedMap;

import org.jboss.resteasy.reactive.RestResponse;

import in.codifi.auth.controller.spec.AuthLoginControllerSpec;
import in.codifi.auth.model.request.AuthReq;
import in.codifi.auth.model.response.GenericResponse;
import in.codifi.auth.servcie.spec.AuthLoginServiceSpec;
import in.codifi.auth.utility.AppConstants;
import in.codifi.auth.utility.AppUtils;
import in.codifi.auth.utility.PrepareResponse;
import in.codifi.auth.utility.StringUtil;
import in.codifi.cache.model.ClinetInfoModel;
import io.quarkus.logging.Log;

@Path("/auth")
public class AuthLoginController implements AuthLoginControllerSpec {

	@Context
	ContainerRequestContext request;

	@Inject
	AuthLoginServiceSpec authService;
	@Inject
	PrepareResponse prepareResponse;
	@Inject
	AppUtils appUtil;

	/**
	 * method to quick auth login
	 * 
	 * @author SowmiyaThangaraj
	 */
	@Override
	public RestResponse<GenericResponse> quickAuthLogin(AuthReq authmodel) {
		if (authmodel == null) {
			return prepareResponse.prepareFailedResponse(AppConstants.INVALID_PARAMETER);
		}
		return authService.quickAuthLogin(authmodel);
	}

	/**
	 * method to send otp
	 * 
	 * @author SowmiyaThangaraj
	 */
	@Override
	public RestResponse<GenericResponse> sendOtpFor2FA(AuthReq authmodel) {
		return authService.sendOtpFor2FA(authmodel);
	}

	/**
	 * method to validate 2fa otp
	 * 
	 * @author SowmiyaThangaraj
	 */
	@Override
	public RestResponse<GenericResponse> validate2FAOTP(AuthReq authmodel) {
		return authService.validate2FAOTP(authmodel);
	}

	/**
	 * method to forgot password
	 * 
	 * @author SowmiyaThangaraj
	 */
	@Override
	public RestResponse<GenericResponse> forgotPwd(AuthReq authmodel) {
		return authService.forgotPwd(authmodel);
	}

	/**
	 * method to unblock users
	 * 
	 * @author SowmiyaThangaraj
	 */
	@Override
	public RestResponse<GenericResponse> unblockUser(AuthReq authmodel) {
		return authService.unblockUser(authmodel);
	}

	/**
	 * method to change password
	 * 
	 * @author SowmiyaThangaraj
	 * @return
	 */
	public RestResponse<GenericResponse> changePwd(AuthReq authmodel) {
		return authService.changePwd(authmodel);
	}

	/**
	 * method to log out
	 * 
	 * @author SowmiyaThangaraj
	 * @return
	 */
	public RestResponse<GenericResponse> logout() {
		ClinetInfoModel info = appUtil.getClientInfo();
		if (info == null || StringUtil.isNullOrEmpty(info.getUserId())) {
			Log.error("Client info is null");
			return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
		}
		return authService.logout(info);
	}

	/**
	 * Method to logout user from keycloak by source
	 * 
	 * @author SowmiyaThangaraj
	 * @param authReq
	 * @return
	 */
	@Override
	public RestResponse<GenericResponse> kcLogout(AuthReq authReq) {
		if (authReq == null)
			return prepareResponse.prepareFailedResponse(AppConstants.INVALID_PARAMETER);
		return authService.kcLogout(authReq);
	}

	/**
	 * method to get users logged in details
	 * 
	 * @author SowmiyaThangaraj
	 */
	@Override
	public RestResponse<GenericResponse> getUserLoggedInDetails() {
		return authService.getUserLoggedInDetails();
	}

	/**
	 * 
	 * Method to validate password for bio metric login
	 * 
	 * @author Dinesh Kumar
	 *
	 * @param authReq
	 * @return
	 */
	@Override
	public RestResponse<GenericResponse> validatePasswordForBio(AuthReq authReq) {
		if (authReq == null)
			return prepareResponse.prepareFailedResponse(AppConstants.INVALID_PARAMETER);
		MultivaluedMap<String, String> headers = request.getHeaders();
		String deviceIp = headers.getFirst(AppConstants.X_FORWARDED_FOR);
		return authService.validatePasswordForBio(authReq, deviceIp);
	}
}
