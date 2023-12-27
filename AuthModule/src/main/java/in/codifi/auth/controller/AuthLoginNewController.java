package in.codifi.auth.controller;

import javax.inject.Inject;
import javax.ws.rs.Path;

import org.jboss.resteasy.reactive.RestResponse;

import in.codifi.auth.controller.spec.AuthLoginNewControllerSpec;
import in.codifi.auth.model.request.AuthReq;
import in.codifi.auth.model.response.GenericResponse;
import in.codifi.auth.servcie.spec.AuthLoginNewServiceSpec;
import in.codifi.auth.utility.AppConstants;
import in.codifi.auth.utility.PrepareResponse;

@Path("/newAuth")
public class AuthLoginNewController implements AuthLoginNewControllerSpec {

	@Inject
	AuthLoginNewServiceSpec service;
	@Inject
	PrepareResponse prepareResponse;

	/**
	 * method to forgot password otp
	 * 
	 * @author SowmiyaThangaraj
	 */
	@Override
	public RestResponse<GenericResponse> forgotPwdotp(AuthReq authmodel) {
		return service.forgotPwdotp(authmodel);
	}

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
		return service.quickAuthLogin(authmodel);
	}
}
