package in.codifi.auth.servcie.spec;

import org.jboss.resteasy.reactive.RestResponse;

import in.codifi.auth.model.request.AuthReq;
import in.codifi.auth.model.response.GenericResponse;
import in.codifi.cache.model.ClinetInfoModel;

public interface AuthLoginServiceSpec {

//	/**
//	 * method to quick auth login
//	 * 
//	 * @author SowmiyaThangaraj
//	 * @param authmodel
//	 * @return
//	 */
//	RestResponse<GenericResponse> quickAuthLogin(AuthReq authmodel);

	/**
	 * method to send otp for 2fa
	 * 
	 * @author SowmiyaThangaraj
	 * @param authmodel
	 * @return
	 */
	RestResponse<GenericResponse> sendOtpFor2FA(AuthReq authmodel);

	/**
	 * 
	 * Method to validate OTP for 2FA
	 * 
	 * @author Dinesh Kumar
	 *
	 * @param authReq
	 * @return
	 */
	RestResponse<GenericResponse> validate2FAOTP(AuthReq authReq);

	/**
	 * method to forgot password
	 * 
	 * @author SowmiyaThangaraj
	 * @param authmodel
	 * @return
	 */
	RestResponse<GenericResponse> forgotPwd(AuthReq authmodel);

	/**
	 * method to unblock users
	 * 
	 * @author SowmiyaThangaraj
	 */
	RestResponse<GenericResponse> unblockUser(AuthReq authmodel);

	/**
	 * method to change password
	 * 
	 * @author SowmiyaThangaraj
	 * @param authmodel
	 * @return
	 */
	RestResponse<GenericResponse> changePwd(AuthReq authmodel);

	/**
	 * method to logout
	 * 
	 * @author SowmiyaThangaraj
	 * @param info
	 * @return
	 */
	RestResponse<GenericResponse> logout(ClinetInfoModel info);

	/**
	 * method to logout from kc based on source
	 * 
	 * @author SowmiyaThangaraj
	 * @param authReq
	 * @return
	 */
	RestResponse<GenericResponse> kcLogout(AuthReq authReq);

	/**
	 * method to get users logged in details
	 * 
	 * @author SowmiyaThangaraj
	 */
	RestResponse<GenericResponse> getUserLoggedInDetails();

	/**
	 * method to validate password for biometric
	 * 
	 * @author SowmiyaThangaraj
	 * @param authReq
	 * @param deviceIp
	 * @return
	 */
	RestResponse<GenericResponse> validatePasswordForBio(AuthReq authReq, String deviceIp);

//	/**
//	 * method to forgot password otp
//	 * 
//	 * @author SowmiyaThangaraj
//	 * @param authmodel
//	 * @return
//	 */
//	RestResponse<GenericResponse> forgotPwdotp(AuthReq authmodel);

}
