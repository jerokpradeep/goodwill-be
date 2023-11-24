package in.codifi.auth.controller;

import javax.inject.Inject;
import javax.ws.rs.Path;

import org.jboss.resteasy.reactive.RestResponse;

import in.codifi.auth.controller.spec.AuthControllerSpec;
import in.codifi.auth.entity.primary.DeviceInfoEntity;
import in.codifi.auth.model.request.AuthReq;
import in.codifi.auth.model.request.ForgetPassReq;
import in.codifi.auth.model.request.UnblockReq;
import in.codifi.auth.model.response.GenericResponse;
import in.codifi.auth.servcie.spec.AuthServiceSpec;
import in.codifi.auth.utility.AppConstants;
import in.codifi.auth.utility.PrepareResponse;
import io.quarkus.logging.Log;

/**
 * Class for Authentication
 * 
 * @author Dinesh
 *
 */
@Path("/access")
public class AuthController implements AuthControllerSpec {

	@Inject
	AuthServiceSpec authServiceSpec;

	@Inject
	PrepareResponse prepareResponse;

	/**
	 * 
	 * Method to check user exist or not
	 * 
	 * @author Dinesh Kumar
	 *
	 * @param authmodel
	 * @return
	 */
	@Override
	public RestResponse<GenericResponse> verifyClient(AuthReq authmodel) {
		if (authmodel == null)
			return prepareResponse.prepareFailedResponse(AppConstants.INVALID_PARAMETER);
		return authServiceSpec.verifyClient(authmodel);
	}

	/**
	 * 
	 * Method to validate password
	 * 
	 * @author Dinesh Kumar
	 *
	 * @param authmodel
	 * @return
	 */
	@Override
	public RestResponse<GenericResponse> validatePassword(AuthReq authmodel) {

		System.out.println("validatePassword");
		Log.error(authmodel);
		if (authmodel == null)
			return prepareResponse.prepareFailedResponse(AppConstants.INVALID_PARAMETER);
		return authServiceSpec.validatePassword(authmodel);
	}

	/**
	 * 
	 * Method to validate OTP for 2FA
	 * 
	 * @author Dinesh Kumar
	 *
	 * @param authmodel
	 * @return
	 */
	@Override
	public RestResponse<GenericResponse> validate2FAOTP(AuthReq authmodel) {
		System.out.println("validate2FAOTP");
		Log.error(authmodel);
		if (authmodel == null)
			return prepareResponse.prepareFailedResponse(AppConstants.INVALID_PARAMETER);
		return authServiceSpec.validate2FAOTP(authmodel);
	}

	/**
	 * 
	 * Method to send OTP for 2FA verification
	 * 
	 * @author Dinesh Kumar
	 *
	 * @param authmodel
	 * @return
	 */
	@Override
	public RestResponse<GenericResponse> sendOtpFor2FA(AuthReq authmodel) {
		System.out.println("sendOtpFor2FA");
		Log.error(authmodel);
		if (authmodel == null)
			return prepareResponse.prepareFailedResponse(AppConstants.INVALID_PARAMETER);
		return authServiceSpec.sendOtpFor2FA(authmodel);
	}

	/**
	 * 
	 * Method to reset password
	 * 
	 * @author Dinesh Kumar
	 *
	 * @param authmodel
	 * @return
	 */
	@Override
	public RestResponse<GenericResponse> passwordReset(AuthReq authmodel) {
		System.out.println("passwordReset");
		Log.error(authmodel);
		if (authmodel == null)
			return prepareResponse.prepareFailedResponse(AppConstants.INVALID_PARAMETER);
		return authServiceSpec.passwordReset(authmodel);
	}

	/**
	 * 
	 * Method to unblock user
	 * 
	 * @author Dinesh Kumar
	 *
	 * @param unblockReq
	 * @return
	 */
	@Override
	public RestResponse<GenericResponse> unblock(UnblockReq unblockReq) {
		if (unblockReq == null)
			return prepareResponse.prepareFailedResponse(AppConstants.INVALID_PARAMETER);
		return authServiceSpec.unblock(unblockReq);
	}

	/**
	 * 
	 * Method to validate OTP to unblock
	 * 
	 * @author Dinesh Kumar
	 *
	 * @param unblockReq
	 * @return
	 */
	@Override
	public RestResponse<GenericResponse> validateOtpToUnblock(UnblockReq unblockReq) {
		if (unblockReq == null)
			return prepareResponse.prepareFailedResponse(AppConstants.INVALID_PARAMETER);
		return authServiceSpec.validateOtpToUnblock(unblockReq);
	}

	/**
	 * 
	 * Method to verify details to reset password
	 * 
	 * @author Dinesh Kumar
	 *
	 * @param authmodel
	 * @return
	 */
	@Override
	public RestResponse<GenericResponse> forgetPasword(ForgetPassReq forgetPassReq) {
		if (forgetPassReq == null)
			return prepareResponse.prepareFailedResponse(AppConstants.INVALID_PARAMETER);
		return authServiceSpec.forgetPasword(forgetPassReq);
	}

	/**
	 * 
	 * Method to validate OTP for forget password
	 * 
	 * @author Dinesh Kumar
	 *
	 * @param authmodel
	 * @return
	 */
	@Override
	public RestResponse<GenericResponse> validateForgetPwdOTP(ForgetPassReq forgetPassReq) {
		if (forgetPassReq == null)
			return prepareResponse.prepareFailedResponse(AppConstants.INVALID_PARAMETER);
		return authServiceSpec.validateForgetPwdOTP(forgetPassReq);
	}

	/**
	 * 
	 * Method to reset password
	 * 
	 * @author Dinesh Kumar
	 *
	 * @param authmodel
	 * @return
	 */
	@Override
	public RestResponse<GenericResponse> changePassword(AuthReq authmodel) {
		if (authmodel == null)
			return prepareResponse.prepareFailedResponse(AppConstants.INVALID_PARAMETER);
		return authServiceSpec.changePassword(authmodel);
	}

	/**
	 * Method to generate scanner for TOTP
	 * 
	 * @author DINESH KUMAR
	 *
	 * @param authReq
	 * @return
	 */
	@Override
	public RestResponse<GenericResponse> generateScanner(AuthReq authReq) {
		if (authReq == null)
			return prepareResponse.prepareFailedResponse(AppConstants.INVALID_PARAMETER);
		return authServiceSpec.generateScanner(authReq);
	}

	/**
	 * 
	 * Method to get scanner for TOTP
	 * 
	 * @author DINESH KUMAR
	 *
	 * @param authReq
	 * @return
	 */
	@Override
	public RestResponse<GenericResponse> getScanner(AuthReq authReq) {
		if (authReq == null)
			return prepareResponse.prepareFailedResponse(AppConstants.INVALID_PARAMETER);
		return authServiceSpec.getScanner(authReq);
	}

	/**
	 * 
	 * Method to enable TOTP
	 * 
	 * @author SOWMIYA
	 *
	 * @param authReq
	 * @return
	 */
	@Override
	public RestResponse<GenericResponse> enableTotp(AuthReq authReq) {
		if (authReq == null)
			return prepareResponse.prepareFailedResponse(AppConstants.INVALID_PARAMETER);
		return authServiceSpec.enableTotp(authReq);
	}

	/**
	 * 
	 * Method to verify TOTP
	 * 
	 * @author SOWMIYA
	 *
	 * @param authReq
	 * @return
	 */
	@Override
	public RestResponse<GenericResponse> verifyTotp(AuthReq authReq) {
		if (authReq == null)
			return prepareResponse.prepareFailedResponse(AppConstants.INVALID_PARAMETER);
		return authServiceSpec.verifyTotp(authReq);
	}

	/**
	 * method to save device information into database
	 * 
	 * @author sowmiya
	 */
	@Override
	public RestResponse<GenericResponse> saveDeviceInfo(DeviceInfoEntity entity) {
		return authServiceSpec.saveDeviceInfo(entity);
	}

	/**
	 * method to send email
	 * 
	 * @author SowmiyaThangaraj
	 * @return
	 */
	public RestResponse<GenericResponse> sendEmail() {
		return authServiceSpec.sendEmail();
	}
}
