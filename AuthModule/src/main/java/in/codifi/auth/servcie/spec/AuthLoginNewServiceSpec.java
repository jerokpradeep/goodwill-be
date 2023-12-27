package in.codifi.auth.servcie.spec;

import org.jboss.resteasy.reactive.RestResponse;

import in.codifi.auth.model.request.AuthReq;
import in.codifi.auth.model.response.GenericResponse;

public interface AuthLoginNewServiceSpec {

	/**
	 * method to forgot password otp
	 * 
	 * @author SowmiyaThangaraj
	 * 
	 * @param authmodel
	 * @return
	 */
	RestResponse<GenericResponse> forgotPwdotp(AuthReq authmodel);

	/**
	 * method to quick auth login
	 * 
	 * @author SowmiyaThangaraj
	 * 
	 * @param authmodel
	 * @return
	 */
	RestResponse<GenericResponse> quickAuthLogin(AuthReq authmodel);

}
