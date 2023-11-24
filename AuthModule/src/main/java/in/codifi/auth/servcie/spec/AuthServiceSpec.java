package in.codifi.auth.servcie.spec;

import org.jboss.resteasy.reactive.RestResponse;

import in.codifi.auth.entity.primary.DeviceInfoEntity;
import in.codifi.auth.model.request.AuthReq;
import in.codifi.auth.model.request.ForgetPassReq;
import in.codifi.auth.model.request.UnblockReq;
import in.codifi.auth.model.response.GenericResponse;

public interface AuthServiceSpec {

	/**
	 * 
	 * Method to check user exist or not
	 * 
	 * @author Dinesh Kumar
	 *
	 * @param authmodel
	 * @return
	 */
	RestResponse<GenericResponse> verifyClient(AuthReq authmodel);

	/**
	 * 
	 * Method to validate password
	 * 
	 * @author Dinesh Kumar
	 *
	 * @param authmodel
	 * @return
	 */
	RestResponse<GenericResponse> validatePassword(AuthReq authmodel);

	/**
	 * 
	 * Method to validate OTP
	 * 
	 * @author Dinesh Kumar
	 *
	 * @param authmodel
	 * @return
	 */
	RestResponse<GenericResponse> validate2FAOTP(AuthReq authmodel);

	/**
	 * 
	 * Method to send OTP
	 * 
	 * @author Dinesh Kumar
	 *
	 * @param authmodel
	 * @return
	 */
	RestResponse<GenericResponse> sendOtpFor2FA(AuthReq authmodel);

	/**
	 * 
	 * Method to reset password
	 * 
	 * @author Dinesh Kumar
	 *
	 * @param authmodel
	 * @return
	 */
	RestResponse<GenericResponse> passwordReset(AuthReq authmodel);

	/**
	 * 
	 * Method to verify details to reset password
	 * 
	 * @author Dinesh Kumar
	 *
	 * @param forgetPassReq
	 * @return
	 */
	RestResponse<GenericResponse> forgetPasword(ForgetPassReq forgetPassReq);

	/**
	 * 
	 * Method to validate otp for forget password
	 * 
	 * @author Dinesh Kumar
	 *
	 * @param authmodel
	 * @return
	 */
	RestResponse<GenericResponse> validateForgetPwdOTP(ForgetPassReq forgetPassReq);

	/**
	 * 
	 * Method to unblock user
	 * 
	 * @author Dinesh Kumar
	 *
	 * @param unblockReq
	 * @return
	 */
	RestResponse<GenericResponse> unblock(UnblockReq unblockReq);

	/**
	 * 
	 * Method to validate otp for forget password
	 * 
	 * @author Dinesh Kumar
	 *
	 * @param unblockReq
	 * @return
	 */
	RestResponse<GenericResponse> validateOtpToUnblock(UnblockReq unblockReq);

	/**
	 * Method to change password
	 * 
	 * @author DINESH KUMAR
	 *
	 * @param authmodel
	 * @return
	 */
	RestResponse<GenericResponse> changePassword(AuthReq authmodel);

	/**
	 * Method to generate scanner for TOTP
	 * 
	 * @author DINESH KUMAR
	 *
	 * @param authmodel
	 * @return
	 */
	RestResponse<GenericResponse> generateScanner(AuthReq authmodel);

	/**
	 * Method to get scanner
	 * 
	 * @author Dinesh Kumar
	 *
	 * @param authReq
	 * @return
	 */
	RestResponse<GenericResponse> getScanner(AuthReq authReq);

	/**
	 * Method to enable totp
	 * 
	 * @author SOWMIYA
	 *
	 * @param authReq , totp
	 * @return
	 */
	RestResponse<GenericResponse> enableTotp(AuthReq authReq);

	/**
	 * 
	 * Method to verify TOTP
	 * 
	 * @author SOWMIYA
	 *
	 * @param authReq
	 * @return
	 */
	RestResponse<GenericResponse> verifyTotp(AuthReq authReq);

	/**
	 * method to save device information into database
	 * 
	 * @author sowmiya
	 * @param entity
	 * @return
	 */
	RestResponse<GenericResponse> saveDeviceInfo(DeviceInfoEntity entity);

	/**
	 * Method to logout
	 * 
	 * @author Dinesh Kumar
	 * @return
	 */
	RestResponse<GenericResponse> logout(AuthReq authReq);

	/**
	 * Method to re login
	 * 
	 * @author Dinesh Kumar
	 * @return
	 */
	RestResponse<GenericResponse> reLogin(AuthReq authReq, String deviceIp);

	/**
	 * method to send a email
	 * 
	 * @author SowmiyaThangaraj
	 * @return
	 */
	RestResponse<GenericResponse> sendEmail();

}
