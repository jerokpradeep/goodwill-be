package in.codifi.auth.servcie;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.core.Response;

import org.apache.commons.codec.binary.Base32;
import org.apache.commons.codec.binary.Hex;
import org.jboss.resteasy.reactive.ClientWebApplicationException;
import org.jboss.resteasy.reactive.RestResponse;

import com.fasterxml.jackson.databind.ObjectMapper;

import de.taimos.totp.TOTP;
import in.codifi.auth.config.HazelcastConfig;
import in.codifi.auth.config.RestPropertiesConfig;
import in.codifi.auth.entity.primary.DefaultOTPEntity;
import in.codifi.auth.entity.primary.DeviceInfoEntity;
import in.codifi.auth.entity.primary.DeviceMappingEntity;
import in.codifi.auth.entity.primary.TotpDetailsEntity;
import in.codifi.auth.entity.primary.TwoFAPreferenceEntity;
import in.codifi.auth.model.request.AuthReq;
import in.codifi.auth.model.request.ForgetPassReq;
import in.codifi.auth.model.request.UnblockReq;
import in.codifi.auth.model.response.AuthRespModel;
import in.codifi.auth.model.response.GenericResponse;
import in.codifi.auth.model.response.OtpRespModel;
import in.codifi.auth.model.response.TotpResponseModel;
import in.codifi.auth.model.response.ValidatePwdOtpResp;
import in.codifi.auth.model.response.VerifyClientResp;
import in.codifi.auth.repository.DefaultOTPRepository;
import in.codifi.auth.repository.DeviceInfoRepository;
import in.codifi.auth.repository.DeviceMappingRepository;
import in.codifi.auth.repository.TotpRepository;
import in.codifi.auth.repository.TwoFAPreferenceRepository;
import in.codifi.auth.servcie.spec.AuthServiceSpec;
import in.codifi.auth.utility.AppConstants;
import in.codifi.auth.utility.AppUtils;
import in.codifi.auth.utility.CommonUtils;
import in.codifi.auth.utility.KcConstants;
import in.codifi.auth.utility.PrepareResponse;
import in.codifi.auth.utility.StringUtil;
import in.codifi.ws.model.client.UserDetailsRestReqModel;
import in.codifi.ws.model.kb.login.LogoutReqModel;
import in.codifi.ws.model.kb.login.QuickAuthReqModel;
import in.codifi.ws.model.kb.login.QuickAuthRespModel;
import in.codifi.ws.model.kc.GetIntroSpectResponse;
import in.codifi.ws.model.kc.GetTokenResponse;
import in.codifi.ws.model.kc.GetUserInfoResp;
import in.codifi.ws.model.kc.RestPasswordErrorResp;
import in.codifi.ws.service.KambalaRestServices;
import in.codifi.ws.service.KcAdminRest;
import in.codifi.ws.service.KcTokenRest;
import io.quarkus.logging.Log;
import io.quarkus.mailer.Mail;
import io.quarkus.mailer.Mailer;

@ApplicationScoped
public class AuthService implements AuthServiceSpec {

	@Inject
	PrepareResponse prepareResponse;

	@Inject
	KcAdminRest kcAdminRest;

	@Inject
	KcTokenRest kcTokenRest;

	@Inject
	TwoFAPreferenceRepository twoFARepo;

	@Inject
	TotpRepository totpRepository;

	@Inject
	RestPropertiesConfig restPropertiesConfig;

	@Inject
	KambalaRestServices kambalaRestServices;

	@Inject
	AppUtils appUtils;

	@Inject
	DeviceMappingRepository deviceMappingRepository;

	@Inject
	DeviceInfoRepository deviceInfoRepo;

	@Inject
	DefaultOTPRepository defaultOTPRepository;
	@Inject
	Mailer mailer;

	@Override
	public RestResponse<GenericResponse> verifyClient(AuthReq authmodel) {
		try {

			if (StringUtil.isNullOrEmpty(authmodel.getUserId()))
				return prepareResponse.prepareFailedResponse(AppConstants.INVALID_PARAMETER);
			/** Find the type of userId **/
			if (appUtils.isMobileNumber(authmodel.getUserId())) {
				return verifyClientByAttribute(AppConstants.ATTRIBUTE_MOBILE, authmodel.getUserId());
//			} else if (appUtils.isEmail(authmodel.getUserId())) {
//				TODO - Add verification for Email based Authentication
//			} else if (appUtils.isPAN(authmodel.getUserId())) {
//				TODO - Add verification for PAN based Authentication
			} else {
				/** Validate client by userId (UCC) **/
				return verifyClientByUserId(authmodel.getUserId());
			}

		} catch (Exception e) {
			e.printStackTrace();
			Log.error(e.getMessage());
		}
		return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
	}

	/**
	 * 
	 * Method to verify client by userId
	 * 
	 * @author Dinesh Kumar
	 *
	 * @param userId
	 * @return
	 */
	private RestResponse<GenericResponse> verifyClientByUserId(String userId) {
		try {
			VerifyClientResp verifyClientResp = new VerifyClientResp();
			List<GetUserInfoResp> userInfo = kcAdminRest.getUserInfo(userId);
			if (StringUtil.isListNotNullOrEmpty(userInfo)) {
				if (userInfo.get(0) != null && userInfo.get(0).getEnabled() != null) {
					if (!userInfo.get(0).getEnabled())
						return prepareResponse.prepareFailedResponse(AppConstants.USER_BLOCKED);
				}
				HazelcastConfig.getInstance().getKeycloakUserDetails().remove(userId);
				HazelcastConfig.getInstance().getKeycloakUserDetails().put(userId, userInfo);
				verifyClientResp.setIsExist(AppConstants.YES);
				verifyClientResp.setUcc(userInfo.get(0).getUsername().toUpperCase());
				return prepareResponse.prepareSuccessResponseObject(verifyClientResp);
			} else {
				verifyClientResp.setIsExist(AppConstants.NO);
				return prepareResponse.prepareSuccessResponseObject(verifyClientResp);
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.error(e.getMessage());
		}
		return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
	}

	/**
	 * Method to verify client by attribute
	 * 
	 * @author DINESH KUMAR
	 *
	 * @param key
	 * @param value
	 * @return
	 */
	private RestResponse<GenericResponse> verifyClientByAttribute(String key, String value) {

		try {
			VerifyClientResp verifyClientResp = new VerifyClientResp();
			List<GetUserInfoResp> userInfo = kcAdminRest.getUserInfoByAttribute(key, value);
			if (StringUtil.isListNotNullOrEmpty(userInfo)) {
				/** If attribute linked with multiple userId return message **/
				if (userInfo.size() > 1)
					return prepareResponse.prepareFailedResponse("Given " + key + AppConstants.MULTIPLE_USER_LINKED);

				String userId = userInfo.get(0).getUsername().toUpperCase();
				HazelcastConfig.getInstance().getKeycloakUserDetails().remove(userId);
				HazelcastConfig.getInstance().getKeycloakUserDetails().put(userId, userInfo);
				verifyClientResp.setIsExist(AppConstants.YES);
				verifyClientResp.setUcc(userId);
				return prepareResponse.prepareSuccessResponseObject(verifyClientResp);
			} else {
				verifyClientResp.setIsExist(AppConstants.NO);
				return prepareResponse.prepareSuccessResponseObject(verifyClientResp);
			}

		} catch (Exception e) {
			e.printStackTrace();
			Log.error(e.getMessage());
		}
		return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
	}

	/**
	 * Method to validate password
	 * 
	 * @author DINESH KUMAR
	 *
	 * @param authmodel
	 * @return
	 */
	@Override
	public RestResponse<GenericResponse> validatePassword(AuthReq authmodel) {

		/** Validate Request **/
		if (StringUtil.isNullOrEmpty(authmodel.getUserId()) || StringUtil.isNullOrEmpty(authmodel.getPassword())
				|| StringUtil.isNullOrEmpty(authmodel.getSource()))
			return prepareResponse.prepareFailedResponse(AppConstants.INVALID_PARAMETER);

		if (!HazelcastConfig.getInstance().getKeycloakUserDetails().containsKey(authmodel.getUserId()))
			return prepareResponse.prepareFailedResponse(AppConstants.USER_NOT_VERIFIED);

		String key = authmodel.getUserId() + "_" + authmodel.getSource();
		try {
			GetTokenResponse kcTokenResp = kcTokenRest.getToken(authmodel);

			if (kcTokenResp != null) {

				/** Return if failed to login on key clock **/
				if (StringUtil.isNotNullOrEmpty(kcTokenResp.getError()))
					return prepareResponse.prepareFailedResponse(kcTokenResp.getErrorDescription());

				if (StringUtil.isNotNullOrEmpty(kcTokenResp.getAccessToken())) {

					AuthRespModel authRespModel = new AuthRespModel();
					OtpRespModel otpRespModel = new OtpRespModel();
					/** To get user roles by requesting user Introspect API **/
					GetIntroSpectResponse introSpectResponse = kcTokenRest.getIntroSpect(authmodel,
							kcTokenResp.getAccessToken());

//					if (introSpectResponse != null && introSpectResponse.getResourceAccess() != null
//							&& introSpectResponse.getResourceAccess().getClientRoles() != null
//							&& introSpectResponse.getActive() != null) {
					if (introSpectResponse != null && introSpectResponse.getClientRoles() != null
							&& introSpectResponse.getActive() != null) {

						if (!introSpectResponse.getActive())
							return prepareResponse.prepareFailedResponse(AppConstants.USER_BLOCKED);

						/** Put the user info into intermediate cache **/
						HazelcastConfig.getInstance().getKeycloakMedianUserInfo().remove(key);
						HazelcastConfig.getInstance().getKeycloakMedianUserInfo().put(key, introSpectResponse);

//						List<String> resourceAccessRole = introSpectResponse.getResourceAccess().getClientRoles()
//								.getRoles();
						List<String> resourceAccessRole = introSpectResponse.getClientRoles();

						if (resourceAccessRole.contains(KcConstants.ACTIVE_USER)) {
							otpRespModel = getSessionFor2FA(authmodel);
							loadKcIntermediateCache(authmodel, kcTokenResp);
							validateRestSession(authmodel);
							return prepareResponse.prepareSuccessResponseObject(otpRespModel);
						} else if (resourceAccessRole.contains(KcConstants.GUEST_USER)) {
							authRespModel = prepareRespForGuest(kcTokenResp, introSpectResponse, authmodel);
							return prepareResponse.prepareSuccessResponseObject(authRespModel);
						}

					}
				}
			}
		} catch (ClientWebApplicationException ex) {
			if (ex.getResponse().getStatus() == 401) {

				int retryCount = 1;
				if (HazelcastConfig.getInstance().getPasswordRetryCount()
						.containsKey(authmodel.getUserId() + AppConstants.HAZEL_KEY_PWD_RETRY_COUNT)) {
					retryCount = HazelcastConfig.getInstance().getPasswordRetryCount()
							.get(authmodel.getUserId() + AppConstants.HAZEL_KEY_PWD_RETRY_COUNT) + 1;
				}
				HazelcastConfig.getInstance().getPasswordRetryCount().put(
						authmodel.getUserId() + AppConstants.HAZEL_KEY_PWD_RETRY_COUNT, retryCount, 300,
						TimeUnit.SECONDS);
				if (retryCount < 5) {
					return prepareResponse.prepareFailedResponse(AppConstants.INVALID_CREDENTIALS);
				} else {
					blockAccount(authmodel.getUserId());
					HazelcastConfig.getInstance().getPasswordRetryCount()
							.remove(authmodel.getUserId() + AppConstants.HAZEL_KEY_PWD_RETRY_COUNT);
					return prepareResponse.prepareFailedResponse(AppConstants.USER_BLOCKED);
				}

			}
			if (ex.getResponse().getStatus() == 400)
				return prepareResponse.prepareFailedResponse(AppConstants.USER_BLOCKED);
		}
		return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS_VALIDATE);
	}

	/**
	 * 
	 * Method to generate intermediate session to authenticate OTP validation
	 * service
	 * 
	 * @author Dinesh Kumar
	 *
	 * @param authmodel
	 * @return
	 */
	private OtpRespModel getSessionFor2FA(AuthReq authmodel) {
		OtpRespModel respModel = new OtpRespModel();
		try {
			String sessionId = appUtils.randomAlphaNumeric(256);

			if (StringUtil.isNotNullOrEmpty(sessionId)) {
				String key = authmodel.getUserId() + "_" + authmodel.getSource() + AppConstants.HAZEL_KEY_OTP_SESSION;
				HazelcastConfig.getInstance().getUserSessionOtp().put(key, sessionId);

				if (HazelcastConfig.getInstance().getTwoFAUserPreference().containsKey(authmodel.getUserId())) {
					String type = HazelcastConfig.getInstance().getTwoFAUserPreference().get(authmodel.getUserId());
					if (type.equalsIgnoreCase(AppConstants.TOTP)) {
						respModel.setTotpAvailable(true);
					}
				}
				respModel.setToken(sessionId);
				respModel.setKcRole(KcConstants.ACTIVE_USER);
				/** To clear OTP cache for resend **/
				String hzKey = authmodel.getUserId() + "_" + authmodel.getSource();
				removeOTPCache(hzKey);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return respModel;
	}

	/**
	 * 
	 * Method to load intermediate key cloak cache.
	 * 
	 * @author Dinesh Kumar
	 *
	 * @param authmodel
	 * @param response
	 */
	private void loadKcIntermediateCache(AuthReq authmodel, GetTokenResponse response) {

		String hazelKey = authmodel.getUserId() + "_" + authmodel.getSource();
		ExecutorService pool = Executors.newSingleThreadExecutor();
		pool.execute(new Runnable() {
			@Override
			public void run() {
				try {
					HazelcastConfig.getInstance().getKeycloakMedianSession().put(hazelKey, response);
					/** Check if user has session for the source **/
					if (HazelcastConfig.getInstance().getKeycloakSession().get(hazelKey) != null) {
						// TODO Sent Web Socket notification with Info
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		pool.shutdown();
	}

	private void validateRestSession(AuthReq authReq) {

		String hzUserSessionKey = authReq.getUserId() + AppConstants.HAZEL_KEY_REST_SESSION;
		ExecutorService pool = Executors.newSingleThreadExecutor();
		pool.execute(new Runnable() {
			@Override
			public void run() {
				try {
					boolean sessionActive = false;
					/** Check if user has session for the source **/
					if (HazelcastConfig.getInstance().getRestUserSession().containsKey(hzUserSessionKey)) {
						String session = HazelcastConfig.getInstance().getRestUserSession().get(hzUserSessionKey);
						String req = prepareClientDetails(authReq.getUserId(), authReq.getSource(), session);
						if (StringUtil.isNotNullOrEmpty(req)) {
							String response = kambalaRestServices.getUserDetails(req, authReq.getSource(),
									authReq.getUserId());
							if (StringUtil.isNotNullOrEmpty(response)
									&& response.equalsIgnoreCase(AppConstants.SUCCESS_STATUS)) {
								sessionActive = true;
							}
						}
					}
					HazelcastConfig.getInstance().getIsRestUserSessionActive().put(hzUserSessionKey, sessionActive);

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		pool.shutdown();
	}

	/**
	 * Prepare client details request model
	 * 
	 * @author DINESH KUMAR
	 *
	 * @param userId
	 * @param source
	 * @param userSession
	 * @return
	 */
	private String prepareClientDetails(String userId, String source, String userSession) {
		ObjectMapper mapper = new ObjectMapper();
		String request = "";
		try {
			UserDetailsRestReqModel reqModel = new UserDetailsRestReqModel();

			String key = userId + "_" + source;
			GetIntroSpectResponse userInfo = new GetIntroSpectResponse();
			if (HazelcastConfig.getInstance().getKeycloakMedianUserInfo().containsKey(key)) {
				userInfo = HazelcastConfig.getInstance().getKeycloakMedianUserInfo().get(key);
			} else if (HazelcastConfig.getInstance().getKeycloakUserInfo().containsKey(key)) {
				userInfo = HazelcastConfig.getInstance().getKeycloakUserInfo().get(key);
			}
			if (userInfo != null && StringUtil.isNotNullOrEmpty(userInfo.getUsername())) {
				reqModel.setUid(userInfo.getUsername().toUpperCase());
				String json = mapper.writeValueAsString(reqModel);
				request = AppConstants.JDATA + json + AppConstants.SYMBOL_AND + AppConstants.JKEY + userSession;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return request;

	}

	/**
	 * 
	 * Method to prepare response for guest user
	 * 
	 * @author Dinesh Kumar
	 *
	 * @param response
	 * @param role
	 * @return
	 */
	private AuthRespModel prepareRespForGuest(GetTokenResponse response, GetIntroSpectResponse introSpectResponse,
			AuthReq authmodel) {
		AuthRespModel respModel = new AuthRespModel();
		respModel.setAccessToken(response.getAccessToken());
		respModel.setKcRole(KcConstants.GUEST_USER);
		String hzKey = authmodel.getUserId() + "_" + authmodel.getSource();
		HazelcastConfig.getInstance().getKeycloakSession().remove(hzKey);
		HazelcastConfig.getInstance().getKeycloakSession().put(hzKey, response);
		return respModel;
	}

	/**
	 * 
	 * Method to send OTP
	 * 
	 * @author Dinesh Kumar
	 *
	 * @param authmodel
	 * @return
	 */
	@Override
	public RestResponse<GenericResponse> sendOtpFor2FA(AuthReq authmodel) {

		try {
			/** Validate Request **/
			if (StringUtil.isNullOrEmpty(authmodel.getUserId()) || StringUtil.isNullOrEmpty(authmodel.getSource()))
				return prepareResponse.prepareFailedResponse(AppConstants.INVALID_PARAMETER);

			String hazelKey = authmodel.getUserId() + "_" + authmodel.getSource();
			if (HazelcastConfig.getInstance().getKeycloakMedianUserInfo().containsKey(hazelKey)) {
				GetIntroSpectResponse userInfo = HazelcastConfig.getInstance().getKeycloakMedianUserInfo()
						.get(hazelKey);
				if (userInfo != null && StringUtil.isNotNullOrEmpty(userInfo.getMobile())) {
					String response = sendOTP(userInfo.getMobile(), authmodel.getSource(), AppConstants.OTP_MSG,
							authmodel.getUserId());

					if (response.equalsIgnoreCase(AppConstants.SUCCESS_STATUS)) {
						return prepareResponse.prepareSuccessMessage(AppConstants.OTP_SENT);
					} else {
						return prepareResponse.prepareFailedResponse(response);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.error(e.getMessage());
		}
		return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
	}

	/**
	 * 
	 * Method to validate OTP for 2FA
	 * 
	 * @author Dinesh Kumar
	 *
	 * @param authReq
	 * @return
	 */
	@Override
	public RestResponse<GenericResponse> validate2FAOTP(AuthReq authReq) {
		try {
			/** Validate Request **/
			if (StringUtil.isNullOrEmpty(authReq.getUserId()) || StringUtil.isNullOrEmpty(authReq.getOtp())
					|| StringUtil.isNullOrEmpty(authReq.getSource()))
				return prepareResponse.prepareFailedResponse(AppConstants.INVALID_PARAMETER);
			// TODO to be un commended after initial demo
			String validateResp = validateOTP(authReq.getUserId(), authReq.getSource(), authReq.getOtp());

			if (validateResp.equalsIgnoreCase(AppConstants.SUCCESS_STATUS)) {

				/** get user info from cache. If does not exist get it from keycloak **/
				List<GetUserInfoResp> userInfo = new ArrayList<>();
				if (HazelcastConfig.getInstance().getKeycloakUserDetails().containsKey(authReq.getUserId())) {
					userInfo = HazelcastConfig.getInstance().getKeycloakUserDetails().get(authReq.getUserId());
				} else {
					userInfo = kcAdminRest.getUserInfo(authReq.getUserId());
				}
				String userName = "";
				userName = StringUtil.isNotNullOrEmpty(userInfo.get(0).getFirstName()) ? userInfo.get(0).getFirstName()
						: "";
				userName = userName + " "
						+ (StringUtil.isNotNullOrEmpty(userInfo.get(0).getLastName()) ? userInfo.get(0).getLastName()
								: "");
				/** update fcmToken into device mapping **/
				updateDeviceMapping(authReq.getUserId(), authReq.getFcmToken(), authReq.getSource(),
						userName.toUpperCase().trim());

				/** update rest session **/
				updateRestSession(authReq);
				return prepareRepForActiveUser(authReq);
			} else {
				return prepareResponse.prepareFailedResponse(validateResp);
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.error(e.getMessage());
		}
		return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
	}

	/**
	 * 
	 * Method to validate OTP
	 * 
	 * @author Dinesh Kumar
	 *
	 * @param authmodel
	 * @return
	 */
	private String validateOTP(String mobileNo, String source, String otp) {
		try {
			/** Validate Request **/
			if (StringUtil.isNullOrEmpty(mobileNo) || StringUtil.isNullOrEmpty(otp) || StringUtil.isNullOrEmpty(source))
				return AppConstants.INVALID_PARAMETER;

			String hzKey = mobileNo + "_" + source;

			/** Check hold time to validate **/
			if (HazelcastConfig.getInstance().getHoldResendOtp().containsKey(hzKey + AppConstants.HAZEL_KEY_OTP_HOLD))
				return AppConstants.OTP_LIMIT_EXCEED;

			/** Check the validity **/
			if (!HazelcastConfig.getInstance().getOtp().containsKey(hzKey + AppConstants.HAZEL_KEY_OTP))
				return AppConstants.OTP_EXCEED;

			/** Check the retry count **/
			if (HazelcastConfig.getInstance().getRetryOtpCount()
					.containsKey(hzKey + AppConstants.HAZEL_KEY_OTP_RETRY_COUNT)
					&& HazelcastConfig.getInstance().getRetryOtpCount()
							.get(hzKey + AppConstants.HAZEL_KEY_OTP_RETRY_COUNT) > 3) {
				HazelcastConfig.getInstance().getHoldResendOtp().put(hzKey + AppConstants.HAZEL_KEY_OTP_HOLD, true, 300,
						TimeUnit.SECONDS);
				HazelcastConfig.getInstance().getRetryOtpCount().remove(hzKey + AppConstants.HAZEL_KEY_OTP_RETRY_COUNT);
				return AppConstants.OTP_LIMIT_EXCEED;
			}

			String cacheOtp = HazelcastConfig.getInstance().getOtp().get(hzKey + AppConstants.HAZEL_KEY_OTP);

			/** validate OTP **/
			if (otp.equals(cacheOtp)) {

				/** Removing OTP cache if OTP validated **/
				removeOTPCache(hzKey);
				return AppConstants.SUCCESS_STATUS;
			} else {
				int retryCount = 1;
				if (HazelcastConfig.getInstance().getRetryOtpCount()
						.containsKey(hzKey + AppConstants.HAZEL_KEY_OTP_RETRY_COUNT)) {
					retryCount = HazelcastConfig.getInstance().getRetryOtpCount()
							.get(hzKey + AppConstants.HAZEL_KEY_OTP_RETRY_COUNT) + 1;
				}
				HazelcastConfig.getInstance().getRetryOtpCount().put(hzKey + AppConstants.HAZEL_KEY_OTP_RETRY_COUNT,
						retryCount);
				return AppConstants.OTP_INVALID;
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.error(e.getMessage());
		}
		return AppConstants.FAILED_STATUS;
	}

	/**
	 * 
	 * Method to remove OTP cache
	 * 
	 * @author Dinesh Kumar
	 *
	 * @param hzKey
	 */
	private void removeOTPCache(String hzKey) {

		HazelcastConfig.getInstance().getOtp().remove(hzKey + AppConstants.HAZEL_KEY_OTP);
		HazelcastConfig.getInstance().getHoldResendOtp().remove(hzKey + AppConstants.HAZEL_KEY_OTP_HOLD);
		HazelcastConfig.getInstance().getResendOtp().remove(hzKey + AppConstants.HAZEL_KEY_OTP_RESEND);
		HazelcastConfig.getInstance().getRetryOtpCount().remove(hzKey + AppConstants.HAZEL_KEY_OTP_RETRY_COUNT);
	}

	/**
	 * 
	 * Method to send OTP
	 * 
	 * @author Dinesh Kumar
	 *
	 * @param authmodel
	 * @return
	 */
	private String sendOTP(String mobileNo, String source, String message, String userId) {

		try {
			String hzKey = userId + "_" + source;
			if (HazelcastConfig.getInstance().getResendOtp().containsKey(hzKey + AppConstants.HAZEL_KEY_OTP_RESEND))
				return AppConstants.RESEND_FAILED;

			if (HazelcastConfig.getInstance().getHoldResendOtp().containsKey(hzKey + AppConstants.HAZEL_KEY_OTP_HOLD))
				return AppConstants.OTP_LIMIT_EXCEED;

			String otp = "";
			if (HazelcastConfig.getInstance().getOtpDefaultUser().get(AppConstants.DEFAULT_USERS) != null
					&& HazelcastConfig.getInstance().getOtpDefaultUser().get(AppConstants.DEFAULT_USERS).size() > 0) {
				List<String> userList = HazelcastConfig.getInstance().getOtpDefaultUser()
						.get(AppConstants.DEFAULT_USERS);
				if (userList.contains(userId)) {
					otp = "123456";
				} else {
					otp = appUtils.generateOTP();
				}
			} else {
				otp = appUtils.generateOTP();
			}
			long phoneNo = Long.parseLong(mobileNo);
//			boolean sendOtp = appUtils.sendOtpToMobile(otp, phoneNo, message);
			boolean sendOtp = true;
			if (sendOtp) {
				/** Set 5 mins validity for otp validation **/
				HazelcastConfig.getInstance().getOtp().put(hzKey + AppConstants.HAZEL_KEY_OTP, otp, 300,
						TimeUnit.SECONDS);
				/** Set 30 sec validity for resent **/
				HazelcastConfig.getInstance().getResendOtp().put(hzKey + AppConstants.HAZEL_KEY_OTP_RESEND, otp, 30,
						TimeUnit.SECONDS);
				return AppConstants.SUCCESS_STATUS;
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.error(e.getMessage());
		}
		return AppConstants.FAILED_STATUS;
	}

	/**
	 * 
	 * Method to prepare final response for active user after 2FA validation
	 * 
	 * @author Dinesh Kumar
	 *
	 * @param authmodel
	 * @return
	 */
	private RestResponse<GenericResponse> prepareRepForActiveUser(AuthReq authmodel) {
		try {
			String hazelKey = authmodel.getUserId() + "_" + authmodel.getSource();
			if (HazelcastConfig.getInstance().getKeycloakMedianSession().containsKey(hazelKey)
					&& HazelcastConfig.getInstance().getKeycloakMedianUserInfo().containsKey(hazelKey)) {

				GetTokenResponse intermediateToken = HazelcastConfig.getInstance().getKeycloakMedianSession()
						.get(hazelKey);
				GetIntroSpectResponse intermediateUserInfo = HazelcastConfig.getInstance().getKeycloakMedianUserInfo()
						.get(hazelKey);
				/** Logout old session and remove Distributed Cache **/
				if (HazelcastConfig.getInstance().getKeycloakSession().get(hazelKey) != null
						&& HazelcastConfig.getInstance().getKeycloakUserInfo().get(hazelKey) != null) {

					GetTokenResponse token = HazelcastConfig.getInstance().getKeycloakSession().get(hazelKey);
					GetIntroSpectResponse userInfo = HazelcastConfig.getInstance().getKeycloakUserInfo().get(hazelKey);
					kcTokenRest.logout(userInfo.getUserId(), token.getAccessToken(), token.getRefreshToken());
					HazelcastConfig.getInstance().getKeycloakSession().remove(hazelKey);
					HazelcastConfig.getInstance().getKeycloakUserInfo().remove(hazelKey);
				}
				/** Add new session into Distributed Cache **/
				HazelcastConfig.getInstance().getKeycloakSession().put(hazelKey, intermediateToken);
				HazelcastConfig.getInstance().getKeycloakUserInfo().put(hazelKey, intermediateUserInfo);

				AuthRespModel respModel = new AuthRespModel();
				respModel.setAccessToken(intermediateToken.getAccessToken());
				respModel.setKcRole(KcConstants.ACTIVE_USER);

				/** Clear intermediate cache **/
				HazelcastConfig.getInstance().getKeycloakMedianSession().remove(hazelKey);
				HazelcastConfig.getInstance().getKeycloakMedianUserInfo().remove(hazelKey);
				HazelcastConfig.getInstance().getUserSessionOtp().remove(hazelKey + AppConstants.HAZEL_KEY_OTP_SESSION);

				return prepareResponse.prepareSuccessResponseObject(respModel);
			}
			Log.error("KeyCloak info does not exist");
		} catch (Exception e) {
			e.printStackTrace();
			Log.info(e.getMessage());
		}

		return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
	}

	/**
	 * 
	 * Method to update rest user session after OTP validation
	 * 
	 * @author Dinesh Kumar
	 *
	 * @param userId
	 */
	private void updateRestSession(AuthReq authmodel) {

		String hzUserSessionKey = authmodel.getUserId() + AppConstants.HAZEL_KEY_REST_SESSION;
		ExecutorService pool = Executors.newSingleThreadExecutor();
		pool.execute(new Runnable() {
			@Override
			public void run() {
				try {
					/** Check if rest user session is active, if not get new session **/
					if (!HazelcastConfig.getInstance().getIsRestUserSessionActive().get(hzUserSessionKey)) {

						/** Prepare request body **/
						String request = prepareQuickAuthRequest(authmodel);
						if (StringUtil.isNotNullOrEmpty(request)) {
							QuickAuthRespModel quickAuthRespModel = kambalaRestServices.quickAuthBypass(request,
									authmodel.getSource(), authmodel.getUserId());
							if (quickAuthRespModel != null) {
								if (StringUtil.isNotNullOrEmpty(quickAuthRespModel.getSUserToken())) {
									updateUserCache(quickAuthRespModel, authmodel.getUserId());
								} else if (StringUtil.isNotNullOrEmpty(quickAuthRespModel.getEmsg())) {
									System.out.println("Failed to get REST Session -" + quickAuthRespModel.getEmsg());
									Log.error("Failed to get REST Session -" + quickAuthRespModel.getEmsg());
								}
							}
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		pool.shutdown();
	}

	/**
	 * Method to Prepare request body for kambala login API
	 * 
	 * @author Dinesh Kumar
	 *
	 * @param authmodel
	 * @return
	 */
	private String prepareQuickAuthRequest(AuthReq authmodel) {

		String request = "";
		try {
			String key = authmodel.getUserId() + "_" + authmodel.getSource();
			GetIntroSpectResponse userInfo = new GetIntroSpectResponse();
			if (HazelcastConfig.getInstance().getKeycloakMedianUserInfo().containsKey(key)) {
				userInfo = HazelcastConfig.getInstance().getKeycloakMedianUserInfo().get(key);
			} else if (HazelcastConfig.getInstance().getKeycloakUserInfo().containsKey(key)) {
				userInfo = HazelcastConfig.getInstance().getKeycloakUserInfo().get(key);
			}
			if (userInfo != null && StringUtil.isNotNullOrEmpty(userInfo.getUsername())) {
				QuickAuthReqModel model = new QuickAuthReqModel();
				String appKey = "";
				if (authmodel.getSource().equalsIgnoreCase(AppConstants.SOURCE_MOB)) {
					appKey = appUtils.encryptWithSHA256(userInfo.getUsername().toUpperCase() + AppConstants.SYMBOL_PIPE
							+ restPropertiesConfig.getMobileVendorKey());
					model.setVendorCode(restPropertiesConfig.getMobileVendorCode());
				} else if (authmodel.getSource().equalsIgnoreCase(AppConstants.SOURCE_WEB)) {
					appKey = appUtils.encryptWithSHA256(userInfo.getUsername().toUpperCase() + AppConstants.SYMBOL_PIPE
							+ restPropertiesConfig.getWebVendorKey());
					model.setVendorCode(restPropertiesConfig.getWebVendorCode());
				} else if (authmodel.getSource().equalsIgnoreCase(AppConstants.SOURCE_API)) {
					appKey = appUtils.encryptWithSHA256(userInfo.getUsername().toUpperCase() + AppConstants.SYMBOL_PIPE
							+ restPropertiesConfig.getApiVendorKey());
					model.setVendorCode(restPropertiesConfig.getApiVendorCode());
				}

				model.setuId(userInfo.getUsername().toUpperCase());
				model.setApkVersion(restPropertiesConfig.getKambalaApkVersion());
				model.setAppKey(appKey);
				model.setImei("0.0.0.0");
//			model.setSource(authmodel.getSource());
				model.setSource(restPropertiesConfig.getKambalaSource());// TODO need to change
				ObjectMapper mapper = new ObjectMapper();
				request = AppConstants.JDATA + mapper.writeValueAsString(model);
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.error(e.getMessage());
		}
		return request;
	}

	/**
	 * Method update latest user session details on cache
	 * 
	 * @author Dinesh Kumar
	 *
	 * @param model
	 * @param userID
	 */
	private void updateUserCache(QuickAuthRespModel model, String userID) {

		String hzUserDetailKey = userID + AppConstants.HAZEL_KEY_USER_DETAILS;
		String hzUserSessionKey = userID + AppConstants.HAZEL_KEY_REST_SESSION;

		HazelcastConfig.getInstance().getUserSessionDetails().remove(hzUserDetailKey);
		HazelcastConfig.getInstance().getRestUserSession().remove(hzUserSessionKey);
		HazelcastConfig.getInstance().getUserSessionDetails().put(hzUserDetailKey, model);
		HazelcastConfig.getInstance().getRestUserSession().put(hzUserSessionKey, model.getSUserToken());
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
		try {
			/** Validate Request **/
			if (StringUtil.isNullOrEmpty(authmodel.getUserId()) || StringUtil.isNullOrEmpty(authmodel.getPassword()))
				return prepareResponse.prepareFailedResponse(AppConstants.INVALID_PARAMETER);
			String response = kcAdminRest.resetPassword(authmodel);
			if (StringUtil.isNotNullOrEmpty(response) && response.equalsIgnoreCase(AppConstants.SUCCESS_STATUS))
				return prepareResponse.prepareSuccessMessage(AppConstants.PASSWORD_CHANGED_SUCCESS);

		} catch (ClientWebApplicationException e) {
			ObjectMapper mapper = new ObjectMapper();
			Response response = e.getResponse();
			if (response.getStatus() == 401) {
				HazelcastConfig.getInstance().getKeycloakAdminSession().clear();
			} else if (response.getStatus() == 400) {
//				System.out.println("Bad request: " + response.readEntity(RestPasswordErrorResp.class));
				RestPasswordErrorResp errorResp = response.readEntity(RestPasswordErrorResp.class);
				return prepareResponse.prepareFailedResponse(errorResp.getErrorDescription());
			}
			Log.info(e.getMessage());
		}

		return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
	}

	/**
	 * 
	 * Method to verify details to reset password
	 * 
	 * @author Dinesh Kumar
	 *
	 * @param forgetPassReq
	 * @return
	 */
	@Override
	public RestResponse<GenericResponse> forgetPasword(ForgetPassReq forgetPassReq) {

		try {

			if (StringUtil.isNullOrEmpty(forgetPassReq.getUserId()) || StringUtil.isNullOrEmpty(forgetPassReq.getPan()))
				return prepareResponse.prepareFailedResponse(AppConstants.INVALID_PARAMETER);

			List<GetUserInfoResp> userInfo = new ArrayList<>();
			/** get user info from keycloak **/
			userInfo = kcAdminRest.getUserInfo(forgetPassReq.getUserId());

			/** Return if user does not exist **/
			if (!StringUtil.isListNotNullOrEmpty(userInfo))
				return prepareResponse.prepareFailedResponse(AppConstants.INVALID_USER);

			/** Validate Pan, if success send otp **/
			if (userInfo.get(0).getAttributes() != null
					&& StringUtil.isListNotNullOrEmpty(userInfo.get(0).getAttributes().getPan())
					&& StringUtil.isListNotNullOrEmpty(userInfo.get(0).getAttributes().getMobile())) {
				String pan = userInfo.get(0).getAttributes().getPan().get(0);
				if (forgetPassReq.getPan().equalsIgnoreCase(pan)) {

					String response = sendOTP(userInfo.get(0).getAttributes().getMobile().get(0),
							forgetPassReq.getSource(), AppConstants.OTP_MSG, forgetPassReq.getUserId());
					if (response.equalsIgnoreCase(AppConstants.SUCCESS_STATUS)) {
						return prepareResponse.prepareSuccessMessage(AppConstants.OTP_SENT);
					} else {
						return prepareResponse.prepareFailedResponse(response);
					}
				} else {
					return prepareResponse.prepareFailedResponse(AppConstants.INVALID_PAN);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.info(e.getMessage());
		}
		return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
	}

	/**
	 * 
	 * Method to validate otp for forget password
	 * 
	 * @author Dinesh Kumar
	 *
	 * @param forgetPassReq
	 * @return
	 */
	@Override
	public RestResponse<GenericResponse> validateForgetPwdOTP(ForgetPassReq forgetPassReq) {
		try {
			/** Validate Request **/
			if (StringUtil.isNullOrEmpty(forgetPassReq.getUserId()) || StringUtil.isNullOrEmpty(forgetPassReq.getOtp())
					|| StringUtil.isNullOrEmpty(forgetPassReq.getSource()))
				return prepareResponse.prepareFailedResponse(AppConstants.INVALID_PARAMETER);

			/** validate OTP **/
			String validateResp = validateOTP(forgetPassReq.getUserId(), forgetPassReq.getSource(),
					forgetPassReq.getOtp());

			if (!validateResp.equalsIgnoreCase(AppConstants.SUCCESS_STATUS))
				return prepareResponse.prepareFailedResponse(validateResp);

			/** If success send token to reset or unblock **/
			ValidatePwdOtpResp resp = new ValidatePwdOtpResp();
			String session = getSessionForReset(forgetPassReq.getUserId(), forgetPassReq.getSource());
			if (StringUtil.isNullOrEmpty(session))
				return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
			resp.setToken(session);
			return prepareResponse.prepareSuccessResponseObject(resp);

		} catch (Exception e) {
			e.printStackTrace();
			Log.error(e.getMessage());
		}
		return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
	}

	/**
	 * 
	 * Method to get session for reset password and unblock user
	 * 
	 * @author Dinesh Kumar
	 *
	 * @param userId
	 * @param source
	 * @return
	 */
	private String getSessionForReset(String userId, String source) {
		String sessionId = "";
		try {
			sessionId = appUtils.randomAlphaNumeric(256);

			if (StringUtil.isNotNullOrEmpty(sessionId)) {
				String key = userId + "_" + source + AppConstants.HAZEL_KEY_OTP_SESSION;
				HazelcastConfig.getInstance().getUserSessionOtp().put(key, sessionId);
				return sessionId;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sessionId;
	}

	/**
	 * 
	 * Method to unblock account
	 * 
	 * @author Dinesh Kumar
	 *
	 * @param unblockReq
	 * @return
	 */
	@Override
	public RestResponse<GenericResponse> unblock(UnblockReq unblockReq) {
		try {

			if (StringUtil.isNullOrEmpty(unblockReq.getUserId()) || StringUtil.isNullOrEmpty(unblockReq.getPan()))
				return prepareResponse.prepareFailedResponse(AppConstants.INVALID_PARAMETER);

			List<GetUserInfoResp> userInfo = new ArrayList<>();
			/** get user info from keycloak **/
			userInfo = kcAdminRest.getUserInfo(unblockReq.getUserId());

			/** Return if user does not exist **/
			if (!StringUtil.isListNotNullOrEmpty(userInfo))
				return prepareResponse.prepareFailedResponse(AppConstants.INVALID_USER);

			/** Validate Pan, if success send otp **/
			if (userInfo.get(0).getAttributes() != null
					&& StringUtil.isListNotNullOrEmpty(userInfo.get(0).getAttributes().getPan())
					&& StringUtil.isListNotNullOrEmpty(userInfo.get(0).getAttributes().getMobile())) {

				String pan = userInfo.get(0).getAttributes().getPan().get(0);
				if (unblockReq.getPan().equalsIgnoreCase(pan)) {
					String response = sendOTP(userInfo.get(0).getAttributes().getMobile().get(0),
							unblockReq.getSource(), AppConstants.OTP_MSG, unblockReq.getUserId());
					if (response.equalsIgnoreCase(AppConstants.SUCCESS_STATUS)) {
						return prepareResponse.prepareSuccessMessage(AppConstants.OTP_SENT);
					} else {
						return prepareResponse.prepareFailedResponse(response);
					}
				} else {
					return prepareResponse.prepareFailedResponse(AppConstants.INVALID_PAN);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			Log.info(e.getMessage());
		}
		return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
	}

	/**
	 * 
	 * Method to validate otp for unblock user
	 * 
	 * @author Dinesh Kumar
	 *
	 * @param unblockReq
	 * @return
	 */
	@Override
	public RestResponse<GenericResponse> validateOtpToUnblock(UnblockReq unblockReq) {
		try {
			/** Validate Request **/
			if (StringUtil.isNullOrEmpty(unblockReq.getUserId()) || StringUtil.isNullOrEmpty(unblockReq.getOtp())
					|| StringUtil.isNullOrEmpty(unblockReq.getSource()))
				return prepareResponse.prepareFailedResponse(AppConstants.INVALID_PARAMETER);

			/** validate OTP **/
			String validateResp = validateOTP(unblockReq.getUserId(), unblockReq.getSource(), unblockReq.getOtp());

			if (!validateResp.equalsIgnoreCase(AppConstants.SUCCESS_STATUS))
				return prepareResponse.prepareFailedResponse(validateResp);

			String unblockResp = unblockAccount(unblockReq.getUserId());

			if (unblockResp.equalsIgnoreCase(AppConstants.USER_UNBLOCK_SUCCESS)) {
				// TODO need to change to user id
				HazelcastConfig.getInstance().getPasswordRetryCount().remove(unblockReq.getUserId());
				return prepareResponse.prepareSuccessMessage(unblockResp);
			}

			return prepareResponse.prepareFailedResponse(unblockResp);

		} catch (Exception e) {
			e.printStackTrace();
			Log.error(e.getMessage());
		}
		return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
	}

	/**
	 * 
	 * Method to unblock user
	 * 
	 * @author Dinesh Kumar
	 *
	 * @param userId
	 * @return
	 */

	private String unblockAccount(String userId) {
		try {
			String response = kcAdminRest.unblockAccount(userId);
			if (StringUtil.isNotNullOrEmpty(response) && response.equalsIgnoreCase(AppConstants.SUCCESS_STATUS))
				return AppConstants.USER_UNBLOCK_SUCCESS;

		} catch (Exception e) {
			e.printStackTrace();
			Log.info(e.getMessage());
		}
		return AppConstants.FAILED_STATUS;
	}

	/**
	 * method to block user
	 * 
	 * @author DINESH KUMAR
	 *
	 * @param userId
	 * @return
	 */
	private String blockAccount(String userId) {
		try {
			String response = kcAdminRest.blockAccount(userId);
			if (StringUtil.isNotNullOrEmpty(response) && response.equalsIgnoreCase(AppConstants.SUCCESS_STATUS))
				return AppConstants.USER_BLOCKED;

		} catch (Exception e) {
			e.printStackTrace();
			Log.info(e.getMessage());
		}
		return AppConstants.FAILED_STATUS;
	}

	/**
	 * Method to change password
	 * 
	 * @author DINESH KUMAR
	 *
	 * @param authmodel
	 * @return
	 */
	@Override
	public RestResponse<GenericResponse> changePassword(AuthReq authmodel) {

		if (StringUtil.isNullOrEmpty(authmodel.getUserId()) || StringUtil.isNullOrEmpty(authmodel.getPassword())
				|| StringUtil.isNullOrEmpty(authmodel.getNewPassword()))
			return prepareResponse.prepareFailedResponse(AppConstants.INVALID_PARAMETER);
		try {
			GetTokenResponse kcTokenResp = kcTokenRest.getToken(authmodel);
			if (kcTokenResp != null) {
				String response = kcAdminRest.changePassword(authmodel);
				if (StringUtil.isNotNullOrEmpty(response)) {
					if (response.equalsIgnoreCase(AppConstants.SUCCESS_STATUS)) {
						return prepareResponse.prepareSuccessMessage(AppConstants.PASSWORD_CHANGED_SUCCESS);
					} else {
						return prepareResponse.prepareFailedResponse(response);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.error(e.getMessage());
		}
		return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
	}

	/**
	 * Method to generate scanner for TOTP
	 * 
	 * @author DINESH KUMAR
	 *
	 * @param authmodel
	 * @return
	 */
	@Override
	public RestResponse<GenericResponse> generateScanner(AuthReq authmodel) {

		if (StringUtil.isNullOrEmpty(authmodel.getUserId()) || StringUtil.isNullOrEmpty(authmodel.getSource()))
			return prepareResponse.prepareFailedResponse(AppConstants.INVALID_PARAMETER);
		try {
			List<GetUserInfoResp> userInfo = new ArrayList<>();
			/** get user info from cache. If does not exist get it from keycloak **/
			if (HazelcastConfig.getInstance().getKeycloakUserDetails().containsKey(authmodel.getUserId())) {
				userInfo = HazelcastConfig.getInstance().getKeycloakUserDetails().get(authmodel.getUserId());
			}
			HazelcastConfig.getInstance().getResendOtp()
					.remove(authmodel.getUserId() + "_" + authmodel.getSource() + AppConstants.HAZEL_KEY_OTP_RESEND);

			if (userInfo.get(0).getAttributes() != null
					&& StringUtil.isListNotNullOrEmpty(userInfo.get(0).getAttributes().getMobile())) {

				String response = sendOTP(userInfo.get(0).getAttributes().getMobile().get(0), authmodel.getSource(),
						AppConstants.OTP_MSG, authmodel.getUserId());
				if (response.equalsIgnoreCase(AppConstants.SUCCESS_STATUS)) {
					return prepareResponse.prepareSuccessMessage(AppConstants.OTP_SENT);
				} else {
					return prepareResponse.prepareFailedResponse(response);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.info(e.getMessage());
		}

		return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
	}

	/**
	 * 
	 * Method to get scanner
	 * 
	 * @author Dinesh Kumar
	 *
	 * @param authReq
	 * @return
	 */
	@Override
	public RestResponse<GenericResponse> getScanner(AuthReq authReq) {
		/** Validate Request **/
		if (StringUtil.isNullOrEmpty(authReq.getUserId()) || StringUtil.isNullOrEmpty(authReq.getOtp())
				|| StringUtil.isNullOrEmpty(authReq.getSource()))
			return prepareResponse.prepareFailedResponse(AppConstants.INVALID_PARAMETER);
		try {
			/** validate OTP **/
			String validateResp = validateOTP(authReq.getUserId(), authReq.getSource(), authReq.getOtp());

			if (validateResp.equalsIgnoreCase(AppConstants.SUCCESS_STATUS)) {
				TotpDetailsEntity totpDetailsEntity = totpRepository.findByUserId(authReq.getUserId());
				/** Get TOTP details for user. If exist reset otherwise create new **/
				if (totpDetailsEntity != null && StringUtil.isNotNullOrEmpty(totpDetailsEntity.getUserId())) {
					totpRepository.deleteById(totpDetailsEntity.getId());
				}
				TotpDetailsEntity totpDetailsEntityNew = appUtils.createScanner(authReq.getUserId());
				if (totpDetailsEntityNew != null) {
					totpDetailsEntityNew = totpRepository.save(totpDetailsEntityNew);
					TotpResponseModel response = new TotpResponseModel();
					response.setScanImge(totpDetailsEntityNew.getImg());
					response.setSecKey(totpDetailsEntityNew.getSecretKey());
					response.setTotpEnabled(false);
					return prepareResponse.prepareSuccessResponseObject(response);
				}
			} else {
				return prepareResponse.prepareFailedResponse(validateResp);
			}

		} catch (Exception e) {
			e.printStackTrace();
			Log.info(e.getMessage());
		}
		return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
	}

	/**
	 * 
	 * Method to enable TOTP
	 * 
	 * @author SOWMIYA
	 *
	 * @param authReq,TOTP
	 * @return
	 */
	@Override
	public RestResponse<GenericResponse> enableTotp(AuthReq authReq) {
		try {
			if (StringUtil.isNullOrEmpty(authReq.getTotp()) || StringUtil.isNullOrEmpty(authReq.getUserId())
					|| StringUtil.isNullOrEmpty(authReq.getSource()))
				return prepareResponse.prepareFailedResponse(AppConstants.INVALID_PARAMETER);

			TotpDetailsEntity totpDetailsEntity = totpRepository.findByUserId(authReq.getUserId());
			if (totpDetailsEntity != null) {
				int activeStatus = totpDetailsEntity.getActiveStatus();
				String secretKey = totpDetailsEntity.getSecretKey();
				String userName = totpDetailsEntity.getUserId();
				/** IF already enabled **/
				if (activeStatus == 1)
					return prepareResponse.prepareFailedResponse(AppConstants.TOTP_ALREADY_ENABLED);
				if (authReq.getTotp().equalsIgnoreCase(getTOTPCode(secretKey))) {
					int updateTotp = totpRepository.enableTotp(authReq.getUserId(), userName);
					if (updateTotp > 0) {
						setUserPreference(authReq.getUserId(), "TOTP");
						return prepareResponse.prepareSuccessMessage(AppConstants.SUCCESS_STATUS);
					} else {
						return prepareResponse.prepareFailedResponse(AppConstants.INTERNAL_ERROR);
					}
				} else {
					return prepareResponse.prepareFailedResponse(AppConstants.INVALID_TOPT);
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
	}

	private String getTOTPCode(String secretKey) {
		Base32 base32 = new Base32();
		byte[] bytes = base32.decode(secretKey);
		String hexKey = Hex.encodeHexString(bytes);
		return TOTP.getOTP(hexKey);
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

		try {
			if (StringUtil.isNullOrEmpty(authReq.getTotp()) || StringUtil.isNullOrEmpty(authReq.getUserId())
					|| StringUtil.isNullOrEmpty(authReq.getSource()))
				return prepareResponse.prepareFailedResponse(AppConstants.INVALID_PARAMETER);

			TotpDetailsEntity totpDetailsEntity = totpRepository.findByUserId(authReq.getUserId());
			if (totpDetailsEntity != null) {
				int activeStatus = totpDetailsEntity.getActiveStatus();
				String secretKey = totpDetailsEntity.getSecretKey();
				if (activeStatus == 0)
					return prepareResponse.prepareFailedResponse(AppConstants.TOTP_NOT_ENABLED);
				if (authReq.getTotp().equalsIgnoreCase(getTOTPCode(secretKey))) {

					/** get user info from cache. If does not exist get it from keycloak **/
					List<GetUserInfoResp> userInfo = new ArrayList<>();
					if (HazelcastConfig.getInstance().getKeycloakUserDetails().containsKey(authReq.getUserId())) {
						userInfo = HazelcastConfig.getInstance().getKeycloakUserDetails().get(authReq.getUserId());
					} else {
						userInfo = kcAdminRest.getUserInfo(authReq.getUserId());
					}
					String userName = "";
					userName = StringUtil.isNotNullOrEmpty(userInfo.get(0).getFirstName())
							? userInfo.get(0).getFirstName()
							: "";
					userName = userName + " "
							+ (StringUtil.isNotNullOrEmpty(userInfo.get(0).getLastName())
									? userInfo.get(0).getLastName()
									: "");
					/** update fcmToken into device mapping **/
					updateDeviceMapping(authReq.getUserId(), authReq.getFcmToken(), authReq.getSource(),
							userName.toUpperCase().trim());

					/** update rest session **/
					updateRestSession(authReq);
					return prepareRepForActiveUser(authReq);
				} else {
					return prepareResponse.prepareFailedResponse(AppConstants.INVALID_TOPT);
				}

			} else {
				return prepareResponse.prepareFailedResponse(AppConstants.TOTP_NOT_ENABLED);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
	}

	/**
	 * 
	 * Method to set 2FA user preference
	 * 
	 * @author SOWMIYA
	 *
	 * @param authReq,TOTP
	 * @return
	 */
	private TwoFAPreferenceEntity setUserPreference(String userId, String TOTP) {
		TwoFAPreferenceEntity twoFAPreferenceEntity = new TwoFAPreferenceEntity();
		try {
			twoFAPreferenceEntity.setUserId(userId);
			twoFAPreferenceEntity.setType(TOTP);
			TwoFAPreferenceEntity entity = twoFARepo.findByUserId(userId);
			if (entity != null && StringUtil.isNotNullOrEmpty(entity.getType())) {
				entity.setType(TOTP);
				entity.setUpdatedBy(userId);
				entity.setActiveStatus(1);
				twoFARepo.save(entity);
				HazelcastConfig.getInstance().getTwoFAUserPreference().put(userId, TOTP);
			} else {
				twoFAPreferenceEntity = twoFARepo.save(twoFAPreferenceEntity);
				HazelcastConfig.getInstance().getTwoFAUserPreference().put(userId, TOTP);
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.error(e.getMessage());
		}
		return twoFAPreferenceEntity;

	}

	/**
	 * 
	 * Method to load 2FA preference into cache
	 * 
	 * @author Dinesh Kumar
	 *
	 * @return
	 */
	public RestResponse<GenericResponse> loadTwoFAUserPreference() {
		try {

			List<String> userIds = twoFARepo.getUserIds();
			if (StringUtil.isListNullOrEmpty(userIds))
				return prepareResponse.prepareFailedResponse(AppConstants.NO_RECORDS_FOUND);
			HazelcastConfig.getInstance().getTwoFAUserPreference().clear();
			for (String userId : userIds) {
				TwoFAPreferenceEntity entity = twoFARepo.findByUserIdAndActiveStatus(userId, 1);
				if (entity != null && StringUtil.isNotNullOrEmpty(entity.getType())) {
					HazelcastConfig.getInstance().getTwoFAUserPreference().put(userId, entity.getType());
				}
			}
			return prepareResponse.prepareFailedResponse(AppConstants.SUCCESS_STATUS);
		} catch (Exception e) {
			e.printStackTrace();
			Log.error(e.getMessage());
		}
		return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
	}

	/**
	 * 
	 * Method to update device id
	 * 
	 * @author Dinesh Kumar
	 *
	 * @param userId
	 * @param deviceId
	 * @param deviceType
	 * @param userName
	 */
	private void updateDeviceMapping(String userId, String deviceId, String deviceType, String userName) {
		DeviceMappingEntity mappingEntity = new DeviceMappingEntity();
		try {
			if (StringUtil.isNotNullOrEmpty(userId) && StringUtil.isNotNullOrEmpty(deviceId)
					&& StringUtil.isNotNullOrEmpty(deviceType) && StringUtil.isNotNullOrEmpty(userName)) {
				DeviceMappingEntity dbMappingEntity = deviceMappingRepository
						.findAllByDeviceIdAndDeviceTypeAndActiveStatus(deviceId, deviceType, 1);
				ExecutorService pool = Executors.newSingleThreadExecutor();
				pool.execute(new Runnable() {
					@Override
					public void run() {
						if (dbMappingEntity != null && StringUtil.isNotNullOrEmpty(dbMappingEntity.getUserId())) {
							if (!dbMappingEntity.getUserId().equalsIgnoreCase(userId)) {
								dbMappingEntity.setUserId(userId);
								dbMappingEntity.setUpdatedBy(userId);
								deviceMappingRepository.saveAndFlush(dbMappingEntity);
							}
						} else {
							mappingEntity.setUserName(userName);
							mappingEntity.setUserId(userId);
							mappingEntity.setDeviceId(deviceId);
							mappingEntity.setDeviceType(deviceType);
							mappingEntity.setCreatedBy(userId);
							deviceMappingRepository.saveAndFlush(mappingEntity);
						}
					}
				});
				pool.shutdown();
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.error(e.getMessage());
		}

	}

	/**
	 * method to save device information into database
	 * 
	 * @author sowmiya
	 */
	@Override
	public RestResponse<GenericResponse> saveDeviceInfo(DeviceInfoEntity entity) {
		DeviceInfoEntity deviceInfo = new DeviceInfoEntity();
		try {
			if (!validateDeviceInfoParams(entity))
				return prepareResponse.prepareFailedResponse(AppConstants.INVALID_PARAMETER);
			deviceInfo = deviceInfoRepo.findByUniqueIdAndTypeAndActiveStatus(entity.getUniqueId(), entity.getType(), 1);
			if (deviceInfo != null && StringUtil.isNotNullOrEmpty(deviceInfo.getUserId())) {
				if (!deviceInfo.getUserId().equalsIgnoreCase(entity.getUserId())) {
					deviceInfo.setUserId(entity.getUserId());
					deviceInfo.setCreatedBy(entity.getUserId());
					deviceInfoRepo.saveAndFlush(deviceInfo);
				}
			} else {
				deviceInfoRepo.saveAndFlush(entity);
			}

			return prepareResponse.prepareSuccessMessage(AppConstants.INSERTED);

		} catch (Exception e) {
			Log.error("auth - saveDeviceInfo  -", e);
		}
		return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
	}

	/**
	 * method to validate device information
	 * 
	 * @author sowmiya
	 * @param entity
	 * @return
	 */
	private boolean validateDeviceInfoParams(DeviceInfoEntity entity) {
		if (StringUtil.isNotNullOrEmpty(entity.getUniqueId()) && StringUtil.isNotNullOrEmpty(entity.getUserId())
				&& StringUtil.isNotNullOrEmpty(entity.getOs()) && StringUtil.isNotNullOrEmpty(entity.getMake())
				&& StringUtil.isNotNullOrEmpty(entity.getModel())
				&& StringUtil.isNotNullOrEmpty(entity.getAppVersion())) {
			return true;
		}
		return false;
	}

	/**
	 * Method to logout
	 * 
	 * @author Dinesh Kumar
	 */
	@Override
	public RestResponse<GenericResponse> logout(AuthReq authReq) {
		try {
			if (StringUtil.isNullOrEmpty(authReq.getSource()))
				return prepareResponse.prepareFailedResponse(AppConstants.INVALID_PARAMETER);
			/** Get user session from cache **/
			String userSession = AppUtils.getUserSession(authReq.getUserId());
			if (StringUtil.isNullOrEmpty(userSession))
				return prepareResponse.prepareUnauthorizedResponse();

			/** Prepare logout request **/
			String request = prepareLogoutRequest(authReq.getUserId(), userSession);
			if (StringUtil.isNullOrEmpty(request))
				return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);

			/** Logout from kambal API **/
			return kambalaRestServices.logout(request, authReq.getSource(), authReq.getUserId());
		} catch (Exception e) {
			Log.error(e.getMessage());
			e.printStackTrace();
		}

		return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
	}

	private String prepareLogoutRequest(String userId, String userSession) {

		String request = "";
		try {
			LogoutReqModel model = new LogoutReqModel();
			model.setUId(userId);
			ObjectMapper mapper = new ObjectMapper();
			String json = mapper.writeValueAsString(model);
			request = AppConstants.JDATA + AppConstants.SYMBOL_EQUAL + json + AppConstants.SYMBOL_AND
					+ AppConstants.JKEY + AppConstants.SYMBOL_EQUAL + userSession;
		} catch (Exception e) {
			e.printStackTrace();
			Log.error(e.getMessage());
		}
		return request;
	}

	/**
	 * Method to re login
	 * 
	 * @author Dinesh Kumar
	 * @return
	 */
	@Override
	public RestResponse<GenericResponse> reLogin(AuthReq authReq, String deviceIp) {
		try {

			if (StringUtil.isNullOrEmpty(authReq.getSource()))
				return prepareResponse.prepareFailedResponse(AppConstants.INVALID_PARAMETER);
			/** Prepare request body **/
			String request = prepareQuickAuthRequest(authReq, deviceIp);
			if (StringUtil.isNotNullOrEmpty(request)) {
				QuickAuthRespModel quickAuthRespModel = kambalaRestServices.quickAuthBypass(request,
						authReq.getSource(), authReq.getUserId());
				if (quickAuthRespModel != null) {
					if (StringUtil.isNotNullOrEmpty(quickAuthRespModel.getSUserToken())) {
						updateUserCache(quickAuthRespModel, authReq.getUserId());
					} else if (StringUtil.isNotNullOrEmpty(quickAuthRespModel.getEmsg())) {
						System.out.println("Failed to get REST Session -" + quickAuthRespModel.getEmsg());
						Log.error("Failed to get REST Session -" + quickAuthRespModel.getEmsg());
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
	}

	/**
	 * Method to Prepare request body for kambala login API
	 * 
	 * @author Dinesh Kumar
	 *
	 * @param authmodel
	 * @return
	 */
	private String prepareQuickAuthRequest(AuthReq authmodel, String deviceIp) {

		String request = "";
		try {
			String key = authmodel.getUserId() + "_" + authmodel.getSource();
			GetIntroSpectResponse userInfo = new GetIntroSpectResponse();
			if (HazelcastConfig.getInstance().getKeycloakMedianUserInfo().containsKey(key)) {
				userInfo = HazelcastConfig.getInstance().getKeycloakMedianUserInfo().get(key);
			} else if (HazelcastConfig.getInstance().getKeycloakUserInfo().containsKey(key)) {
				userInfo = HazelcastConfig.getInstance().getKeycloakUserInfo().get(key);
			}
			if (userInfo != null) {
				QuickAuthReqModel model = new QuickAuthReqModel();
				String appKey = "";
				if (authmodel.getSource().equalsIgnoreCase(AppConstants.SOURCE_MOB)) {
					appKey = CommonUtils.encryptWithSHA256(authmodel.getUserId() + AppConstants.SYMBOL_PIPE
							+ restPropertiesConfig.getMobileVendorKey());
					model.setVendorCode(restPropertiesConfig.getMobileVendorCode());
				} else if (authmodel.getSource().equalsIgnoreCase(AppConstants.SOURCE_WEB)) {
					appKey = CommonUtils.encryptWithSHA256(
							authmodel.getUserId() + AppConstants.SYMBOL_PIPE + restPropertiesConfig.getWebVendorKey());
					model.setVendorCode(restPropertiesConfig.getWebVendorCode());
				} else if (authmodel.getSource().equalsIgnoreCase(AppConstants.SOURCE_API)) {
					appKey = CommonUtils.encryptWithSHA256(
							authmodel.getUserId() + AppConstants.SYMBOL_PIPE + restPropertiesConfig.getApiVendorKey());
					model.setVendorCode(restPropertiesConfig.getApiVendorCode());
				}

				model.setuId(authmodel.getUserId());
				model.setApkVersion(restPropertiesConfig.getKambalaApkVersion());
				model.setAppKey(appKey);
				if (StringUtil.isNotNullOrEmpty(authmodel.getImei())) {
					model.setImei(authmodel.getImei());
				} else {
					model.setImei("0.0.0.0");
				}
				model.setIpAddress(deviceIp);
//				model.setSource(authmodel.getSource());
				/** Live **/
//				model.setSource(AppConstants.SOURCE_WEB);
				/** UAT **/
//				model.setSource(AppConstants.SOURCE_API);

				model.setSource(restPropertiesConfig.getKambalaSource());// TODO need to change
				ObjectMapper mapper = new ObjectMapper();
				request = AppConstants.JDATA + mapper.writeValueAsString(model);
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.error(e.getMessage());
		}
		return request;
	}

	/**
	 * method to load default otp into cache
	 * 
	 * @author SowmiyaThangaraj
	 * @return
	 */
	public RestResponse<GenericResponse> loadDefaultOTP() {
		try {
			List<String> userList = new ArrayList<>();
			List<DefaultOTPEntity> userIdList = defaultOTPRepository.findAllByActiveStatus(1);
			if (userIdList != null && !userIdList.isEmpty()) {
				HazelcastConfig.getInstance().getOtpDefaultUser().clear();
				for (DefaultOTPEntity entity : userIdList) {
					userList.add(entity.getUserId());
				}
				HazelcastConfig.getInstance().getOtpDefaultUser().put(AppConstants.DEFAULT_USERS, userList);
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.error("loadDefaultOTP -", e);
		}
		return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);

	}

	/**
	 * method to send a mail
	 * 
	 * @author SowmiyaThangaraj
	 */
	@Override
	public RestResponse<GenericResponse> sendEmail() {
		mailer.send(Mail.withText("sowmiyathangaraj10@gmail.com", "Subject", "Hello, this is the email body"));
		return prepareResponse.prepareSuccessMessage(AppConstants.STATUS_OK);
	}

}
