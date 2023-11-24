package in.codifi.auth.servcie;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.jboss.resteasy.reactive.ClientWebApplicationException;
import org.jboss.resteasy.reactive.RestResponse;
import org.json.simple.JSONObject;

import com.esotericsoftware.minlog.Log;
import com.fasterxml.jackson.databind.ObjectMapper;

import in.codifi.auth.config.ApplicationProperties;
import in.codifi.auth.config.HazelcastConfig;
import in.codifi.auth.config.RestPropertiesConfig;
import in.codifi.auth.entity.primary.DeviceMappingEntity;
import in.codifi.auth.entity.primary.VendorEntity;
import in.codifi.auth.entity.primary.VendorSubcriptionEntity;
import in.codifi.auth.model.request.AuthReq;
import in.codifi.auth.model.response.AuthRespModel;
import in.codifi.auth.model.response.GenericResponse;
import in.codifi.auth.model.response.UsersLoggedInModel;
import in.codifi.auth.model.response.UsersLoggedInRespModel;
import in.codifi.auth.repository.AccessLogManager;
import in.codifi.auth.repository.DeviceMappingRepository;
import in.codifi.auth.repository.VendorRepository;
import in.codifi.auth.repository.VendorSubcriptionRepository;
import in.codifi.auth.servcie.spec.AuthLoginServiceSpec;
import in.codifi.auth.utility.AppConstants;
import in.codifi.auth.utility.AppUtils;
import in.codifi.auth.utility.CommonUtils;
import in.codifi.auth.utility.KcConstants;
import in.codifi.auth.utility.PrepareResponse;
import in.codifi.auth.utility.StringUtil;
import in.codifi.cache.model.ClinetInfoModel;
import in.codifi.ws.model.client.LogoutRestReqModel;
import in.codifi.ws.model.client.UserDetailsRestReqModel;
import in.codifi.ws.model.kb.login.ForgotPwdRestReqModel;
import in.codifi.ws.model.kb.login.ForgotPwdRestRespModel;
import in.codifi.ws.model.kb.login.QuickAuthLoginReqModel;
import in.codifi.ws.model.kb.login.QuickAuthReqModel;
import in.codifi.ws.model.kb.login.QuickAuthRespModel;
import in.codifi.ws.model.kc.ChangePwdRestReqModel;
import in.codifi.ws.model.kc.GetIntroSpectResponse;
import in.codifi.ws.model.kc.GetTokenResponse;
import in.codifi.ws.model.kc.GetUserInfoResp;
import in.codifi.ws.model.kc.UnblockUsersRestReqModel;
import in.codifi.ws.service.KambalaRestServices;
import in.codifi.ws.service.KcAdminRest;
import in.codifi.ws.service.KcTokenRest;

@ApplicationScoped
public class AuthLoginService implements AuthLoginServiceSpec {
	@Inject
	PrepareResponse prepareResponse;
	@Inject
	AppUtils appUtils;
	@Inject
	KcAdminRest kcAdminRest;
	@Inject
	KcTokenRest kcTokenRest;
	@Inject
	ApplicationProperties props;
	@Inject
	KambalaRestServices kambalaRestServices;
	@Inject
	AppUtils appUtil;
	@Inject
	VendorRepository vendorRepository;
	@Inject
	VendorSubcriptionRepository subcriptionRepository;
	@Inject
	AccessLogManager accessLogManager;
	@Inject
	CommonUtils commonUtils;
	@Inject
	DeviceMappingRepository deviceMappingRepository;
	@Inject
	RestPropertiesConfig restPropertiesConfig;

	/**
	 * method to quick auth login
	 * 
	 * @author SowmiyaThangaraj
	 */
	@Override
	public RestResponse<GenericResponse> quickAuthLogin(AuthReq authmodel) {
		QuickAuthRespModel quickAuthRespModel = new QuickAuthRespModel();
		try {
			List<GetUserInfoResp> userInfo = verifyClient(authmodel);
			if (userInfo != null && userInfo.size() > 0) {
				String request = prepareQuickAuthRequest(authmodel, userInfo);
				if (request != null) {
					quickAuthRespModel = kambalaRestServices.quickAuthBypassLogin(request, authmodel.getSource(),
							authmodel.getUserId());
					if (quickAuthRespModel.getStat().equalsIgnoreCase(AppConstants.REST_STATUS_OK)) {
						if (StringUtil.isNotNullOrEmpty(quickAuthRespModel.getSUserToken())) {
							updateUserCache(quickAuthRespModel, authmodel.getUserId());
							return prepareResponse.prepareSuccessMessage(AppConstants.SUCCESS_STATUS);
						} else if (StringUtil.isNotNullOrEmpty(quickAuthRespModel.getEmsg())) {
							System.out.println("Failed to get REST Session -" + quickAuthRespModel.getEmsg());
							Log.error("Failed to get REST Session -" + quickAuthRespModel.getEmsg());
						}

					} else if (quickAuthRespModel.getStat().equalsIgnoreCase(AppConstants.REST_STATUS_NOT_OK)) {
						return prepareResponse.prepareFailedResponseForRestService(quickAuthRespModel.getEmsg());
					}
				}
			} else {
				return prepareResponse.prepareFailedResponse(AppConstants.FAILED_LOGIN);
			}

		} catch (Exception e) {
			e.printStackTrace();
			Log.error("quickAuthLogin", e);
		}
		return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
	}

	/**
	 * method to update user cache
	 * 
	 * @author SowmiyaThangaraj
	 * @param quickAuthRespModel
	 * @param userId
	 */
	private void updateUserCache(QuickAuthRespModel quickAuthRespModel, String userId) {
		String hzUserDetailKey = userId + AppConstants.HAZEL_KEY_USER_DETAILS;
		String hzUserSessionKey = userId + AppConstants.HAZEL_KEY_REST_SESSION;

		HazelcastConfig.getInstance().getUserSessionDetails().remove(hzUserDetailKey);
		HazelcastConfig.getInstance().getRestUserSession().remove(hzUserSessionKey);
		HazelcastConfig.getInstance().getUserSessionDetails().put(hzUserDetailKey, quickAuthRespModel);
		HazelcastConfig.getInstance().getRestUserSession().put(hzUserSessionKey, quickAuthRespModel.getSUserToken());

	}

	/**
	 * method to verify client and get user information
	 * 
	 * @author SowmiyaThangaraj
	 * @param authmodel
	 */
	private List<GetUserInfoResp> verifyClient(AuthReq authmodel) {
		List<GetUserInfoResp> userInfo = new ArrayList<>();
		if (appUtils.isMobileNumber(authmodel.getUserId())) {
			userInfo = verifyClientByAttribute(AppConstants.ATTRIBUTE_MOBILE, authmodel.getUserId());
		} else {
			/** Validate client by userId (UCC) **/
			userInfo = verifyClientByUserId(authmodel);
		}
		return userInfo;

	}

	/**
	 * method to verify client by userId
	 * 
	 * @author SowmiyaThangaraj
	 * @param authmodel
	 */
	private List<GetUserInfoResp> verifyClientByUserId(AuthReq authmodel) {
		List<GetUserInfoResp> userInfo = new ArrayList<>();
		try {
			userInfo = kcAdminRest.getUserInfo(authmodel.getUserId());
			if (StringUtil.isListNotNullOrEmpty(userInfo)) {
				if (userInfo.get(0) != null && userInfo.get(0).getEnabled() != null) {
					if (!userInfo.get(0).getEnabled())
						System.out.println((AppConstants.USER_BLOCKED));
				}
				HazelcastConfig.getInstance().getKeycloakUserDetails().remove(authmodel.getUserId());
				HazelcastConfig.getInstance().getKeycloakUserDetails().put(authmodel.getUserId(), userInfo);
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.error(e.getMessage());
		}
		return userInfo;

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
	private List<GetUserInfoResp> verifyClientByAttribute(String key, String value) {
		List<GetUserInfoResp> userInfo = new ArrayList<>();
		try {
			userInfo = kcAdminRest.getUserInfoByAttribute(key, value);
			if (StringUtil.isListNotNullOrEmpty(userInfo)) {
				/** If attribute linked with multiple userId return message **/
				if (userInfo.size() > 1)
					System.out.println(("Given " + key + AppConstants.MULTIPLE_USER_LINKED));
				String userId = userInfo.get(0).getUsername().toUpperCase();
				HazelcastConfig.getInstance().getKeycloakUserDetails().remove(userId);
				HazelcastConfig.getInstance().getKeycloakUserDetails().put(userId, userInfo);
			}

		} catch (Exception e) {
			e.printStackTrace();
			Log.error(e.getMessage());
		}
		return userInfo;
	}

	/**
	 * method to prepare quick auth request
	 * 
	 * @author SowmiyaThangaraj
	 * @param authmodel
	 * @param userInfo
	 * @return
	 */
	private String prepareQuickAuthRequest(AuthReq authmodel, List<GetUserInfoResp> userInfo) {
		String request = null;
		ObjectMapper mapper = new ObjectMapper();
		try {
			QuickAuthLoginReqModel reqModel = new QuickAuthLoginReqModel();
			reqModel.setApkVersion("1.0.0");
			String panNo = userInfo.get(0).getAttributes().getPan().get(0);
			reqModel.setFactor2(panNo);
			reqModel.setImei("0.0.0");
			reqModel.setUId(userInfo.get(0).getUsername().toUpperCase());
			reqModel.setSource("API");
			String appKey = appUtils
					.encryptWithSHA256(userInfo.get(0).getUsername().toUpperCase() + "|" + props.getAppKey());
			reqModel.setAppKey(appKey);
			reqModel.setVendorCode(props.getVendorCode());
			String pwd = appUtils.encryptWithSHA256(authmodel.getPassword());
			reqModel.setPwd(pwd);
			String reqText = mapper.writeValueAsString(reqModel);
			request = AppConstants.JDATA + reqText;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return request;
	}

	/**
	 * method to send otp
	 * 
	 * @author SowmiyaThangaraj
	 * @param authmodel
	 * @return
	 */
	public RestResponse<GenericResponse> sendOtpFor2FA(AuthReq authmodel) {

		try {
			/** Validate Request **/
			if (StringUtil.isNullOrEmpty(authmodel.getUserId()) || StringUtil.isNullOrEmpty(authmodel.getSource()))
				return prepareResponse.prepareFailedResponse(AppConstants.INVALID_PARAMETER);

			String response = sendOTP(authmodel.getSource(), authmodel.getUserId());

			if (response.equalsIgnoreCase(AppConstants.SUCCESS_STATUS)) {
				return prepareResponse.prepareSuccessMessage(AppConstants.OTP_SENT);
			} else {
				return prepareResponse.prepareFailedResponse(response);
			}

		} catch (Exception e) {
			e.printStackTrace();
			Log.error(e.getMessage());
		}
		return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
	}

	/**
	 * method to send otp
	 * 
	 * @author SowmiyaThangaraj
	 * @param mobileNo
	 * @param source
	 * @param message
	 * @param userId
	 * @return
	 */
	private String sendOTP(String source, String userId) {
		try {
			String hzKey = userId + "_" + source;
			if (HazelcastConfig.getInstance().getResendOtp().containsKey(hzKey + AppConstants.HAZEL_KEY_OTP_RESEND))
				return AppConstants.RESEND_FAILED;

			if (HazelcastConfig.getInstance().getHoldResendOtp().containsKey(hzKey + AppConstants.HAZEL_KEY_OTP_HOLD))
				return AppConstants.OTP_LIMIT_EXCEED;

			String otp = "123456";
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
	 * Method to validate OTP for 2FA
	 * 
	 * @author Dinesh Kumar
	 *
	 * @param authReq
	 * @return
	 */
	@Override
	public RestResponse<GenericResponse> validate2FAOTP(AuthReq authReq) {
		String hazelKey = authReq.getUserId() + "_" + authReq.getSource();
		AuthRespModel respModel = new AuthRespModel();
		UsersLoggedInRespModel loggedModel = new UsersLoggedInRespModel();
		Timestamp timestamp = new Timestamp(new Date().getTime());
		String hour = new SimpleDateFormat("HH:mm:ss").format(timestamp);
		try {
			/** Validate Request **/
			if (StringUtil.isNullOrEmpty(authReq.getUserId()) || StringUtil.isNullOrEmpty(authReq.getOtp())
					|| StringUtil.isNullOrEmpty(authReq.getSource()))
				return prepareResponse.prepareFailedResponse(AppConstants.INVALID_PARAMETER);
			String validateResp = validateOTP(authReq.getUserId(), authReq.getSource(), authReq.getOtp());
			if (validateResp.equalsIgnoreCase(AppConstants.SUCCESS_STATUS)) {
				GetTokenResponse kcTokenResp = kcTokenRest.getUserToken(authReq, 0);
				if (kcTokenResp != null) {

					/** Return if failed to login on key clock **/
					if (StringUtil.isNotNullOrEmpty(kcTokenResp.getError()))
						return prepareResponse.prepareFailedResponse(kcTokenResp.getErrorDescription());

					if (StringUtil.isNotNullOrEmpty(kcTokenResp.getAccessToken())) {

						/** To get user roles by requesting user Introspect API **/
						GetIntroSpectResponse introSpectResponse = kcTokenRest.getIntroSpect(authReq,
								kcTokenResp.getAccessToken());
						if (introSpectResponse != null && introSpectResponse.getClientRoles() != null
								&& introSpectResponse.getActive() != null) {

							if (!introSpectResponse.getActive()) {
								return prepareResponse.prepareFailedResponse(
										AppConstants.FAILED_STATUS_VALIDATE + " - " + AppConstants.USER_BLOCKED);
							}
							String userName = introSpectResponse.getName();

							/** update fcmToken into device mapping **/
							updateDeviceMapping(authReq.getUserId(), authReq.getFcmToken(), authReq.getSource(),
									userName.toUpperCase().trim());

							/** Logout old session If exist **/
							logoutFromKeycloak(authReq);

							/** Add new session into Distributed Cache **/

							HazelcastConfig.getInstance().getKeycloakSession().put(hazelKey, kcTokenResp);
							HazelcastConfig.getInstance().getKeycloakUserInfo().put(hazelKey, introSpectResponse);

							respModel.setAccessToken(kcTokenResp.getAccessToken());

							List<String> resourceAccessRole = introSpectResponse.getClientRoles();

							if (resourceAccessRole.contains(KcConstants.ACTIVE_USER)) {
								respModel.setKcRole(KcConstants.ACTIVE_USER);
							} else if (resourceAccessRole.contains(KcConstants.GUEST_USER)) {
								respModel.setKcRole(KcConstants.GUEST_USER);
							}
							loggedModel.setSource(authReq.getSource());
							loggedModel.setUserId(authReq.getUserId());
							loggedModel.setTime(hour);
							String hazelcastKey = "";

							String key = authReq.getUserId() + "_" + authReq.getSource().toUpperCase();

							String response = HazelcastConfig.getInstance().getLogResponseModel().get(key);
							if (response == null) {
								HazelcastConfig.getInstance().getLogResponseModel().put(key, key);
								if (authReq.getSource().equalsIgnoreCase("WEB")) {
									HazelcastConfig.getInstance().getWebLoggedInUsers().put(authReq.getUserId(),
											loggedModel);
									accessLogManager.insertUserLogginedInDetails(authReq.getUserId(),
											authReq.getSource(), hazelcastKey);
								}
								if (authReq.getSource().equalsIgnoreCase("MOB")) {
									HazelcastConfig.getInstance().getMobLoggedInUsers().put(authReq.getUserId(),
											loggedModel);
									accessLogManager.insertUserLogginedInDetails(authReq.getUserId(),
											authReq.getSource(), hazelcastKey);
								}
								if (authReq.getSource().equalsIgnoreCase("API")) {
									HazelcastConfig.getInstance().getApiLoggedInUsers().put(authReq.getUserId(),
											loggedModel);
									accessLogManager.insertUserLogginedInDetails(authReq.getUserId(),
											authReq.getSource(), hazelcastKey);
								}
							}

							if (authReq.getVendor() != null) {
								/** For SSO Login **/

								if (HazelcastConfig.getInstance().getKeycloakMedianSession().get(hazelKey) != null
										&& HazelcastConfig.getInstance().getKeycloakMedianUserInfo()
												.get(hazelKey) != null) {

									GetTokenResponse intermediateToken = HazelcastConfig.getInstance()
											.getKeycloakMedianSession().get(hazelKey);
									GetIntroSpectResponse intermediateUserInfo = HazelcastConfig.getInstance()
											.getKeycloakMedianUserInfo().get(hazelKey);

									if (HazelcastConfig.getInstance().getKeycloakSession().get(hazelKey) != null) {
										/** Logout new web/mob session. This session no need **/
										kcTokenRest.logout(intermediateUserInfo.getUserId(),
												intermediateToken.getAccessToken(),
												intermediateToken.getRefreshToken());
										/** Clear intermediate cache **/
										HazelcastConfig.getInstance().getKeycloakMedianSession().remove(hazelKey);
										HazelcastConfig.getInstance().getKeycloakMedianUserInfo().remove(hazelKey);
										HazelcastConfig.getInstance().getUserSessionOtp()
												.remove(hazelKey + AppConstants.HAZEL_KEY_OTP_SESSION);

										GetTokenResponse token = HazelcastConfig.getInstance().getKeycloakSession()
												.get(hazelKey);
										respModel.setAccessToken(token.getAccessToken());
//											respModel.setRefreshToken(token.getRefreshToken());
										respModel.setKcRole(KcConstants.ACTIVE_USER);
									} else {

										HazelcastConfig.getInstance().getKeycloakSession().put(hazelKey,
												intermediateToken);
										HazelcastConfig.getInstance().getKeycloakUserInfo().put(hazelKey,
												intermediateUserInfo);

										respModel.setAccessToken(intermediateToken.getAccessToken());
//											respModel.setRefreshToken(intermediateToken.getRefreshToken());
										respModel.setKcRole(KcConstants.ACTIVE_USER);

										/** Clear intermediate cache **/
										HazelcastConfig.getInstance().getKeycloakMedianSession().remove(hazelKey);
										HazelcastConfig.getInstance().getKeycloakMedianUserInfo().remove(hazelKey);
										HazelcastConfig.getInstance().getUserSessionOtp()
												.remove(hazelKey + AppConstants.HAZEL_KEY_OTP_SESSION);

									}

								}
								/*
								 * Check the vendor is valid or not
								 */
								String vendorKey = authReq.getVendor().toUpperCase();
								List<VendorEntity> vendorDetails = vendorRepository.findAllByApiKey(vendorKey);

								if (vendorDetails != null && vendorDetails.size() > 0) {

									/*
									 * Generate the auth code with Alpha numeric
									 */
									String authCode = CommonUtils.randomAlphaNumeric(20).toUpperCase();
									String temAuthCode = vendorKey + "_" + authCode;
									String shaKey = commonUtils.generateSHAKey(authReq.getUserId(), authCode,
											vendorDetails.get(0).getApiSecret());
									HazelcastConfig.getInstance().getVendorAuthCode().put(shaKey,
											authReq.getUserId() + "_" + vendorDetails.get(0).getAppName());
									HazelcastConfig.getInstance().getVendorAuthCode().put(temAuthCode.toUpperCase(),
											authReq.getUserId());

									List<VendorSubcriptionEntity> vendorSubcriptionEntities = subcriptionRepository
											.findAllByUserIdAndAppIdAndActiveStatus(authReq.getUserId(),
													vendorDetails.get(0).getId(), 1);
									if (vendorSubcriptionEntities != null && vendorSubcriptionEntities.size() > 0) {
										VendorSubcriptionEntity tempVendorSubcriptionEntity = vendorSubcriptionEntities
												.get(0);
										if (tempVendorSubcriptionEntity.getAuthorizationStatus() == 1) {
											respModel.setAuthorized(true);
											/*
											 * Build the response
											 */
											respModel.setRedirectUrl(vendorDetails.get(0).getRedirectUrl()
													+ "?authCode=" + authCode + "&userId=" + authReq.getUserId());
										} else {
											respModel.setAuthorized(false);
										}
									} else {
										respModel.setAuthorized(false);
									}
								}
								createSsoSession(authReq);
							}

							return prepareResponse.prepareSuccessResponseObject(respModel);

						}
					}
				} else {
					return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS_VALIDATE);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			Log.error(e.getMessage());
		}
		return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
	}

	/**
	 * method to update device mapping
	 * 
	 * @author SowmiyaThangaraj
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
	 * Method to check and create SSO and API Session
	 * 
	 * @author Dinesh kumar
	 * 
	 * @param authmodel
	 */
	private void createSsoSession(AuthReq authmodel) {
		String hazelKeySso = authmodel.getUserId() + "_" + AppConstants.SOURCE_SSO;
		String hazelKeyApi = authmodel.getUserId() + "_" + AppConstants.SOURCE_API;
		ExecutorService pool = Executors.newSingleThreadExecutor();
		pool.execute(new Runnable() {
			@SuppressWarnings("null")
			@Override
			public void run() {
				try {
					boolean updateSso = true;
					boolean updateApi = true;
					/** check SSO session, if already exist validate else create it **/
					if (HazelcastConfig.getInstance().getSsoKeycloakSession().get(hazelKeySso) != null) {
						GetTokenResponse kcTokenResp = HazelcastConfig.getInstance().getSsoKeycloakSession()
								.get(hazelKeySso);
						if (StringUtil.isNotNullOrEmpty(kcTokenResp.getAccessToken())) {
							GetIntroSpectResponse introSpectResponse = kcTokenRest.getIntroSpect(authmodel,
									kcTokenResp.getAccessToken());
//							if (introSpectResponse != null
//									&& StringUtil.isNotNullOrEmpty(introSpectResponse.getUsername())) {
//								updateSso = false;
//							}

							if (introSpectResponse != null && introSpectResponse.getActive()) {
								updateSso = false;
							}
						}
					}
					/** To create new session for SSO **/
					if (updateSso) {
						GetTokenResponse ssoKcTokenResp = kcTokenRest.getToken(authmodel);
						if (ssoKcTokenResp != null && StringUtil.isNotNullOrEmpty(ssoKcTokenResp.getAccessToken())) {
							GetIntroSpectResponse introSpectResponse = kcTokenRest.getIntroSpect(authmodel,
									ssoKcTokenResp.getAccessToken());
							if (introSpectResponse != null) {
								/** Put the user info into intermediate cache **/
								HazelcastConfig.getInstance().getSsokeycloakUserInfo().remove(hazelKeySso);
								HazelcastConfig.getInstance().getSsokeycloakUserInfo().put(hazelKeySso,
										introSpectResponse);
							}

							HazelcastConfig.getInstance().getSsoKeycloakSession().put(hazelKeySso, ssoKcTokenResp);
						}
					}
					UsersLoggedInRespModel loggedModel = new UsersLoggedInRespModel();
					Timestamp timestamp = new Timestamp(new Date().getTime());
					String hour = new SimpleDateFormat("HH:mm:ss").format(timestamp);
					loggedModel.setSource(hazelKeySso);
					loggedModel.setUserId(authmodel.getUserId());
					loggedModel.setTime(hour);
					loggedModel.setVendor(authmodel.getVendor());
					HazelcastConfig.getInstance().getSsoLoggedInUsers().put(hazelKeySso, loggedModel);
					accessLogManager.insertUserLogginedInDetails(authmodel.getUserId(), authmodel.getSource(),
							hazelKeySso);

					/** check API session, if already exist validate else create it **/
					if (HazelcastConfig.getInstance().getApiKeycloakSession().get(hazelKeyApi) != null) {
						GetTokenResponse kcTokenResp = HazelcastConfig.getInstance().getApiKeycloakSession()
								.get(hazelKeyApi);
						if (StringUtil.isNotNullOrEmpty(kcTokenResp.getAccessToken())) {
							GetIntroSpectResponse introSpectResponseApi = kcTokenRest.getIntroSpect(authmodel,
									kcTokenResp.getAccessToken());
							if (introSpectResponseApi != null
									|| StringUtil.isNotNullOrEmpty(introSpectResponseApi.getUsername())) {
								updateApi = false;
							}
						}
					}
					/** To create new session for API **/
					if (updateApi) {
						GetTokenResponse apiKcTokenResp = kcTokenRest.getToken(authmodel);
						if (apiKcTokenResp != null && StringUtil.isNotNullOrEmpty(apiKcTokenResp.getAccessToken())) {
							HazelcastConfig.getInstance().getApiKeycloakSession().put(hazelKeyApi, apiKcTokenResp);
							GetIntroSpectResponse introSpectResponse = kcTokenRest.getIntroSpect(authmodel,
									apiKcTokenResp.getAccessToken());
							if (introSpectResponse != null) {
								/** Put the user info into intermediate cache **/
								HazelcastConfig.getInstance().getApikeycloakUserInfo().remove(hazelKeyApi);
								HazelcastConfig.getInstance().getApikeycloakUserInfo().put(hazelKeyApi,
										introSpectResponse);
							}
						}
					}

				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					pool.shutdown();
				}
			}
		});
	}

	/**
	 * Method to logout user from keycloak
	 * 
	 * @param userId
	 * @param source
	 */
	private RestResponse<GenericResponse> logoutFromKeycloak(AuthReq authReq) {

		try {
			String hazelKey = authReq.getUserId() + "_" + authReq.getSource();
			if (HazelcastConfig.getInstance().getKeycloakSession().get(hazelKey) != null
					&& HazelcastConfig.getInstance().getKeycloakUserInfo().get(hazelKey) != null) {
				GetTokenResponse token = HazelcastConfig.getInstance().getKeycloakSession().get(hazelKey);
				GetIntroSpectResponse userInfo = HazelcastConfig.getInstance().getKeycloakUserInfo().get(hazelKey);
				kcTokenRest.logout(userInfo.getUserId(), token.getAccessToken(), token.getRefreshToken());
				HazelcastConfig.getInstance().getKeycloakSession().remove(hazelKey);
				HazelcastConfig.getInstance().getKeycloakUserInfo().remove(hazelKey);
				return prepareResponse.prepareSuccessMessage(AppConstants.SUCCESS_STATUS);
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.error(e.getMessage());
		}
		return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);

	}

	/**
	 * method to validate otp
	 * 
	 * @author SowmiyaThangaraj
	 * @param userId
	 * @param source
	 * @param otp
	 * @return
	 */
	private String validateOTP(String mobileNo, String source, String otp) {
		try {
			/** Validate Request **/
			if (StringUtil.isNullOrEmpty(mobileNo) || StringUtil.isNullOrEmpty(otp) || StringUtil.isNullOrEmpty(source))
				return AppConstants.INVALID_PARAMETER;
			System.out.println("Invalid parameter Passed 602");
			String hzKey = mobileNo + "_" + source;
			System.out.println("Hz key --> " + hzKey);

			/** Check hold time to validate **/
			if (HazelcastConfig.getInstance().getHoldResendOtp().containsKey(hzKey + AppConstants.HAZEL_KEY_OTP_HOLD))
				return AppConstants.OTP_LIMIT_EXCEED;
			System.out.println("OTP_LIMIT_EXCEED Passed 609");
			/** Check the validity **/
			if (!HazelcastConfig.getInstance().getOtp().containsKey(hzKey + AppConstants.HAZEL_KEY_OTP))
				return AppConstants.OTP_EXCEED;
			System.out.println("OTP_EXCEED Passed 612");
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
	 * method to forgot password
	 * 
	 * @author SowmiyaThangaraj
	 */
	@Override
	public RestResponse<GenericResponse> forgotPwd(AuthReq authmodel) {
		try {
			if (!validateReqParams(authmodel))
				return prepareResponse.prepareFailedResponse(AppConstants.INVALID_PARAMETER);
			String request = prepareForgotPwdRequest(authmodel);
			if (request != null) {
				return kambalaRestServices.forgotPwd(request, authmodel.getUserId());
			} else {
				return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.error("forgotPwd", e);

		}
		return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
	}

	/**
	 * method to prepare forgot password request
	 * 
	 * @author SowmiyaThangaraj
	 * @param authmodel
	 * @return
	 */
	private String prepareForgotPwdRequest(AuthReq authmodel) {
		String request = "";
		ObjectMapper mapper = new ObjectMapper();
		try {
			ForgotPwdRestReqModel reqModel = new ForgotPwdRestReqModel();
			reqModel.setUId(authmodel.getUserId());
			reqModel.setPan(authmodel.getPan());
			reqModel.setDob(authmodel.getDateOfBirth());
			String reqText = mapper.writeValueAsString(reqModel);
			request = AppConstants.JDATA + reqText;
		} catch (Exception e) {
			e.printStackTrace();

		}
		return request;
	}

	/**
	 * method to forgot password otp
	 * 
	 * @author SowmiyaThangaraj
	 * @return
	 */
	public RestResponse<GenericResponse> forgotPwdOTP(AuthReq reqModel) {
		try {
			if (StringUtil.isNullOrEmpty(reqModel.getUserId()) && StringUtil.isNullOrEmpty(reqModel.getPassword()))
				return prepareResponse.prepareFailedResponse(AppConstants.INVALID_PARAMETER);
			String request = prepareRequestForgotPwdOtp(reqModel);
			if (StringUtil.isNullOrEmpty(request))
				return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
			ForgotPwdRestRespModel respModel = kambalaRestServices.forgotPwdOtp(request, reqModel.getUserId());
			if (respModel != null && respModel.getStat().equalsIgnoreCase(AppConstants.REST_STATUS_OK)) {
				return prepareResponse.prepareSuccessMessage(AppConstants.OTP_SENT);
			} else {
				return prepareResponse.prepareFailedResponse(respModel.getEmsg());
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
	}

	/**
	 * method to prepare request
	 * 
	 * @author SowmiyaThangaraj
	 * @param reqModel
	 * @return
	 */
	private String prepareRequestForgotPwdOtp(AuthReq reqModel) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * method to unblock users
	 * 
	 * @author SowmiyaThangaraj
	 */
	@Override
	public RestResponse<GenericResponse> unblockUser(AuthReq authmodel) {
		try {
			if (!validateReqParams(authmodel))
				return prepareResponse.prepareFailedResponse(AppConstants.INVALID_PARAMETER);
			String request = prepareRequest(authmodel);
			return kambalaRestServices.unblockUsers(request, authmodel.getUserId());

		} catch (Exception e) {
			e.printStackTrace();
			Log.error("unblockUser", e);

		}

		return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
	}

	/**
	 * method to prepare request
	 * 
	 * @author SowmiyaThangaraj
	 * @param authmodel
	 * @param userSession
	 * @param userId
	 * @return
	 */
	private String prepareRequest(AuthReq authmodel) {
		ObjectMapper mapper = new ObjectMapper();
		String request = "";
		try {
			UnblockUsersRestReqModel restReqModel = new UnblockUsersRestReqModel();
			restReqModel.setUid(authmodel.getUserId());
			restReqModel.setPan(authmodel.getPan());
			restReqModel.setDob(authmodel.getDateOfBirth());
			String json = mapper.writeValueAsString(restReqModel);
			request = AppConstants.JDATA + json;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return request;
	}

	/**
	 * method to validate request params
	 * 
	 * @author SowmiyaThangaraj
	 * @param authmodel
	 * @return
	 */
	private boolean validateReqParams(AuthReq authmodel) {
		if (StringUtil.isNotNullOrEmpty(authmodel.getUserId())
				&& StringUtil.isNotNullOrEmpty(authmodel.getDateOfBirth())
				&& StringUtil.isNotNullOrEmpty(authmodel.getPan())) {
			return true;
		}
		return false;
	}

	/**
	 * method to change password
	 * 
	 * @author SowmiyaThangaraj
	 */
	@Override
	public RestResponse<GenericResponse> changePwd(AuthReq authmodel) {
		try {
			if (!validateChangePwdReq(authmodel))
				return prepareResponse.prepareFailedResponse(AppConstants.INVALID_PARAMETER);
			String request = prepareChangePwdReq(authmodel);
			if (request != null) {
				return kambalaRestServices.changePwd(request, authmodel.getUserId());
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.error("", e);
		}
		return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
	}

	/**
	 * method to prepare request
	 * 
	 * @author SowmiyaThangaraj
	 * @param authmodel
	 * @return
	 */
	private String prepareChangePwdReq(AuthReq authmodel) {
		String request = "";
		try {
			ChangePwdRestReqModel reqModel = new ChangePwdRestReqModel();
			reqModel.setUid(authmodel.getUserId());
			String op = authmodel.getOldPwd();
			String oldpwd = appUtils.encryptWithSHA256(op);
			reqModel.setOldpwd(oldpwd);
			reqModel.setPwd(authmodel.getNewPwd());
			ObjectMapper mapper = new ObjectMapper();
			String json = mapper.writeValueAsString(reqModel);
			request = AppConstants.JDATA + json;

		} catch (Exception e) {
			e.printStackTrace();
			Log.error("prepareChangePwdReq", e);
		}
		return request;
	}

	/**
	 * methdo to validate change password request
	 * 
	 * @author SowmiyaThangaraj
	 * @param authmodel
	 * @return
	 */
	private boolean validateChangePwdReq(AuthReq authmodel) {

		if (StringUtil.isNotNullOrEmpty(authmodel.getUserId()) && StringUtil.isNotNullOrEmpty(authmodel.getUserId())
				&& StringUtil.isNotNullOrEmpty(authmodel.getUserId())) {
			return true;
		}
		return false;
	}

	/**
	 * method to logout
	 * 
	 * @author SowmiyaThangaraj
	 */
	@Override
	public RestResponse<GenericResponse> logout(ClinetInfoModel info) {
		try {
			/** Get user session from cache **/
			String userSession = AppUtils.getUserSession(info.getUserId());
			if (StringUtil.isNullOrEmpty(userSession))
				return prepareResponse.prepareUnauthorizedResponse();
			String request = prepareLogoutReq(info.getUserId(), userSession);
			if (request != null) {
				return kambalaRestServices.logout(request, info.getUserId());
			}

		} catch (Exception e) {
			e.printStackTrace();
			Log.error("logout", e);
		}
		return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
	}

	/**
	 * method to prepare logout request
	 * 
	 * @author SowmiyaThangaraj
	 * @param userId
	 * @param userSession
	 * @return
	 */
	private String prepareLogoutReq(String userId, String userSession) {
		String request = null;
		try {
			LogoutRestReqModel model = new LogoutRestReqModel();
			model.setUid(userId);
			ObjectMapper mapper = new ObjectMapper();
			String json = mapper.writeValueAsString(model);
			request = AppConstants.JDATA + json + AppConstants.JKEY + userSession;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return request;
	}

	/**
	 * Method to logout user from keycloak
	 * 
	 * @author Dinesh Kumar
	 */
	@Override
	public RestResponse<GenericResponse> kcLogout(AuthReq authReq) {
		try {
			/** Validate Request **/
			if (StringUtil.isNullOrEmpty(authReq.getUserId()) || StringUtil.isNullOrEmpty(authReq.getSource()))
				return prepareResponse.prepareFailedResponse(AppConstants.INVALID_PARAMETER);

			String hazelKey = authReq.getUserId() + "_" + authReq.getSource();

			if (HazelcastConfig.getInstance().getKeycloakSession().get(hazelKey) != null
					&& HazelcastConfig.getInstance().getKeycloakUserInfo().get(hazelKey) != null) {

				GetTokenResponse token = HazelcastConfig.getInstance().getKeycloakSession().get(hazelKey);
				GetIntroSpectResponse userInfo = HazelcastConfig.getInstance().getKeycloakUserInfo().get(hazelKey);
				kcTokenRest.logout(userInfo.getUserId(), token.getAccessToken(), token.getRefreshToken());
			}

			HazelcastConfig.getInstance().getKeycloakSession().remove(hazelKey);
			HazelcastConfig.getInstance().getKeycloakUserInfo().remove(hazelKey);
			return prepareResponse.prepareSuccessMessage(AppConstants.SUCCESS_STATUS);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS_VALIDATE);
	}

	/**
	 * method to get users logged in details
	 * 
	 * @author SowmiyaThangaraj
	 */
	@Override
	public RestResponse<GenericResponse> getUserLoggedInDetails() {
		UsersLoggedInModel model = new UsersLoggedInModel();
		model = accessLogManager.getCountBySource();

		List<String> distinctVendor = accessLogManager.findDistinctVendors();
		if (distinctVendor != null && !distinctVendor.isEmpty()) {
			List<JSONObject> ssoCountByVendor = accessLogManager.getCountByVendor(distinctVendor);
			if (ssoCountByVendor != null) {
				model.setSso(ssoCountByVendor);
				return prepareResponse.prepareSuccessResponseObject(model);
			}
		} else {
			return prepareResponse.prepareSuccessResponseObject(model);
		}

		return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
	}

	/**
	 * method to validate password for bio metric
	 * 
	 * @author SowmiyaThangaraj
	 */
	@Override
	public RestResponse<GenericResponse> validatePasswordForBio(AuthReq authmodel, String deviceIp) {
		/** Validate Request **/
		if (StringUtil.isNullOrEmpty(authmodel.getUserId()) || StringUtil.isNullOrEmpty(authmodel.getPassword())
				|| StringUtil.isNullOrEmpty(authmodel.getSource()) || !authmodel.getSource().equalsIgnoreCase("MOB"))
			return prepareResponse.prepareFailedResponse(AppConstants.INVALID_PARAMETER);
		String key = authmodel.getUserId() + "_" + authmodel.getSource();
		try {
			GetTokenResponse kcTokenResp = kcTokenRest.getToken(authmodel);

			if (kcTokenResp != null) {

				/** Return if failed to login on key clock **/
				if (StringUtil.isNotNullOrEmpty(kcTokenResp.getError()))
					return prepareResponse.prepareFailedResponse(kcTokenResp.getErrorDescription());

				if (StringUtil.isNotNullOrEmpty(kcTokenResp.getAccessToken())) {

					AuthRespModel authRespModel = new AuthRespModel();
					/** To get user roles by requesting user Introspect API **/
					GetIntroSpectResponse introSpectResponse = kcTokenRest.getIntroSpect(authmodel,
							kcTokenResp.getAccessToken());

					if (introSpectResponse != null && introSpectResponse.getClientRoles() != null
							&& introSpectResponse.getActive() != null) {

						if (!introSpectResponse.getActive())
							return prepareResponse.prepareFailedResponse(AppConstants.USER_BLOCKED);

						/** Put the user info into intermediate cache **/
						HazelcastConfig.getInstance().getKeycloakMedianUserInfo().remove(key);
						HazelcastConfig.getInstance().getKeycloakMedianUserInfo().put(key, introSpectResponse);

						List<String> resourceAccessRole = introSpectResponse.getClientRoles();

						if (resourceAccessRole.contains(KcConstants.BLOCKED_USER)) {
							HazelcastConfig.getInstance().getKeycloakMedianUserInfo().remove(key);
							return prepareResponse.prepareFailedResponse(AppConstants.USER_BLOCKED_ADMIN);
						} else if (resourceAccessRole.contains(KcConstants.ACTIVE_USER)) {
//							otpRespModel = getSessionFor2FA(authmodel);
							loadKcIntermediateCache(authmodel, kcTokenResp);
							/** update rest session **/
							validateRestSessionForBio(authmodel, deviceIp);
							createSsoSession(authmodel);
							return prepareRepForActiveUser(authmodel);
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
				if (retryCount < 4) {
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
	 * Method to load intermediate key cloak cache.
	 * 
	 * @author Dinesh Kumar
	 *
	 * @param authmodel
	 * @param response
	 */
	private void loadKcIntermediateCache(AuthReq authmodel, GetTokenResponse response) {

		String hazelKey = authmodel.getUserId() + "_" + authmodel.getSource();
		HazelcastConfig.getInstance().getKeycloakMedianSession().put(hazelKey, response);
		ExecutorService pool = Executors.newSingleThreadExecutor();
		pool.execute(new Runnable() {
			@Override
			public void run() {
				try {
//					HazelcastConfig.getInstance().getKeycloakMedianSessionKB().put(hazelKey, response);
					/** Check if user has session for the source **/
					if (HazelcastConfig.getInstance().getKeycloakSession().get(hazelKey) != null) {
						// TODO Sent Web Socket notification with Info
					}
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					pool.shutdown();
				}
			}
		});
	}

	/**
	 * 
	 * Method to validate rest user session for active user
	 * 
	 * @author Dinesh Kumar
	 *
	 * @param userId
	 * @param session
	 */
	private boolean validateRestSessionForBio(AuthReq authReq, String deviceIp) {

		String hzUserSessionKey = authReq.getUserId() + AppConstants.HAZEL_KEY_REST_SESSION;
		boolean sessionActive = false;
		try {
			/** Check if user has session for the source **/
			if (HazelcastConfig.getInstance().getRestUserSession() != null
					&& HazelcastConfig.getInstance().getRestUserSession().get(hzUserSessionKey) != null) {
				String session = HazelcastConfig.getInstance().getRestUserSession().get(hzUserSessionKey);
				String req = prepareClientDetails(authReq.getUserId(), session);
				if (StringUtil.isNotNullOrEmpty(req)) {
					String response = kambalaRestServices.getUserDetails(req, authReq.getSource());
					if (StringUtil.isNotNullOrEmpty(response)
							&& response.equalsIgnoreCase(AppConstants.SUCCESS_STATUS)) {
						sessionActive = true;
					}
				}
			} else {
				/** Prepare request body **/
				String request = prepareQuickAuthRequest(authReq, deviceIp);
				if (StringUtil.isNotNullOrEmpty(request)) {
					QuickAuthRespModel quickAuthRespModel = kambalaRestServices.quickAuthBypass(request,
							authReq.getSource(), authReq.getUserId());
					if (quickAuthRespModel != null) {
						if (StringUtil.isNotNullOrEmpty(quickAuthRespModel.getSUserToken())) {
							updateUserCache(quickAuthRespModel, authReq.getUserId());
							sessionActive = true;
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
		return sessionActive;
	}

	/**
	 * method to prepare client details
	 * 
	 * @author SowmiyaThangaraj
	 * @param userId
	 * @param userSession
	 * @return
	 */
	private String prepareClientDetails(String userId, String userSession) {
		ObjectMapper mapper = new ObjectMapper();
		String request = "";
		try {
			UserDetailsRestReqModel reqModel = new UserDetailsRestReqModel();
			reqModel.setUid(userId);
			String json = mapper.writeValueAsString(reqModel);
			request = AppConstants.JDATA + json + AppConstants.SYMBOL_AND + AppConstants.JKEY + userSession;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return request;

	}

	private RestResponse<GenericResponse> prepareRepForActiveUser(AuthReq authmodel) {
		try {
			UsersLoggedInRespModel loggedModel = new UsersLoggedInRespModel();
			Timestamp timestamp = new Timestamp(new Date().getTime());
			String hour = new SimpleDateFormat("HH:mm:ss").format(timestamp);
			String hazelKey = authmodel.getUserId() + "_" + authmodel.getSource();
			if (HazelcastConfig.getInstance().getKeycloakMedianSession().containsKey(hazelKey)
					&& HazelcastConfig.getInstance().getKeycloakMedianUserInfo().containsKey(hazelKey)) {
				AuthRespModel respModel = new AuthRespModel();

				if (StringUtil.isNullOrEmpty(authmodel.getVendor())) {

					GetTokenResponse intermediateToken = HazelcastConfig.getInstance().getKeycloakMedianSession()
							.get(hazelKey);
					GetIntroSpectResponse intermediateUserInfo = HazelcastConfig.getInstance()
							.getKeycloakMedianUserInfo().get(hazelKey);
					/** Logout old session and remove Distributed Cache **/
					if (HazelcastConfig.getInstance().getKeycloakSession().get(hazelKey) != null
							&& HazelcastConfig.getInstance().getKeycloakUserInfo().get(hazelKey) != null) {

						GetTokenResponse token = HazelcastConfig.getInstance().getKeycloakSession().get(hazelKey);
						GetIntroSpectResponse userInfo = HazelcastConfig.getInstance().getKeycloakUserInfo()
								.get(hazelKey);
						if (StringUtil.isNotNullOrEmpty(token.getAccessToken())
								&& StringUtil.isNotNullOrEmpty(token.getRefreshToken())) {
							kcTokenRest.logout(userInfo.getUserId(), token.getAccessToken(), token.getRefreshToken());
						}
						HazelcastConfig.getInstance().getKeycloakSession().remove(hazelKey);
						HazelcastConfig.getInstance().getKeycloakUserInfo().remove(hazelKey);
					}
					/** Add new session into Distributed Cache **/
					HazelcastConfig.getInstance().getKeycloakSession().put(hazelKey, intermediateToken);
					HazelcastConfig.getInstance().getKeycloakUserInfo().put(hazelKey, intermediateUserInfo);

					respModel.setAccessToken(intermediateToken.getAccessToken());
//					respModel.setRefreshToken(intermediateToken.getRefreshToken());
					respModel.setKcRole(KcConstants.ACTIVE_USER);

					/** Clear intermediate cache **/
					HazelcastConfig.getInstance().getKeycloakMedianSession().remove(hazelKey);
					HazelcastConfig.getInstance().getKeycloakMedianUserInfo().remove(hazelKey);
					HazelcastConfig.getInstance().getUserSessionOtp()
							.remove(hazelKey + AppConstants.HAZEL_KEY_OTP_SESSION);

					loggedModel.setSource(authmodel.getSource());
					loggedModel.setUserId(authmodel.getUserId());
					loggedModel.setTime(hour);
					List<UsersLoggedInRespModel> accessLogModelList = new ArrayList<>();
					accessLogModelList.add(loggedModel);
					if (authmodel.getSource().equalsIgnoreCase("WEB") && (HazelcastConfig.getInstance()
							.getWebLoggedInUsers().get(authmodel.getUserId()) == null)) {
						HazelcastConfig.getInstance().getWebLoggedInUsers().put(authmodel.getUserId(), loggedModel);
						accessLogManager.insertUserLogginedInDetailsIntoDB(accessLogModelList);

					}
					if (authmodel.getSource().equalsIgnoreCase("MOB") && (HazelcastConfig.getInstance()
							.getMobLoggedInUsers().get(authmodel.getUserId()) == null)) {
						HazelcastConfig.getInstance().getMobLoggedInUsers().put(authmodel.getUserId(), loggedModel);
						accessLogManager.insertUserLogginedInDetailsIntoDB(accessLogModelList);

					}

				} else {
					/** For SSO Login **/

					if (HazelcastConfig.getInstance().getKeycloakMedianSession().get(hazelKey) != null
							&& HazelcastConfig.getInstance().getKeycloakMedianUserInfo().get(hazelKey) != null) {

						GetTokenResponse intermediateToken = HazelcastConfig.getInstance().getKeycloakMedianSession()
								.get(hazelKey);
						GetIntroSpectResponse intermediateUserInfo = HazelcastConfig.getInstance()
								.getKeycloakMedianUserInfo().get(hazelKey);

						if (HazelcastConfig.getInstance().getKeycloakSession().get(hazelKey) != null) {
							/** Logout new web/mob session. This session no need **/
							if (StringUtil.isNotNullOrEmpty(intermediateToken.getAccessToken())
									&& StringUtil.isNotNullOrEmpty(intermediateToken.getRefreshToken())) {
								kcTokenRest.logout(intermediateUserInfo.getUserId(), intermediateToken.getAccessToken(),
										intermediateToken.getRefreshToken());
							}
							/** Clear intermediate cache **/
							HazelcastConfig.getInstance().getKeycloakMedianSession().remove(hazelKey);
							HazelcastConfig.getInstance().getKeycloakMedianUserInfo().remove(hazelKey);
							HazelcastConfig.getInstance().getUserSessionOtp()
									.remove(hazelKey + AppConstants.HAZEL_KEY_OTP_SESSION);

							GetTokenResponse token = HazelcastConfig.getInstance().getKeycloakSession().get(hazelKey);
							respModel.setAccessToken(token.getAccessToken());
//							respModel.setRefreshToken(token.getRefreshToken());
							respModel.setKcRole(KcConstants.ACTIVE_USER);
						} else {

							HazelcastConfig.getInstance().getKeycloakSession().put(hazelKey, intermediateToken);
							HazelcastConfig.getInstance().getKeycloakUserInfo().put(hazelKey, intermediateUserInfo);

							respModel.setAccessToken(intermediateToken.getAccessToken());
//							respModel.setRefreshToken(intermediateToken.getRefreshToken());
							respModel.setKcRole(KcConstants.ACTIVE_USER);

							/** Clear intermediate cache **/
							HazelcastConfig.getInstance().getKeycloakMedianSession().remove(hazelKey);
							HazelcastConfig.getInstance().getKeycloakMedianUserInfo().remove(hazelKey);
							HazelcastConfig.getInstance().getUserSessionOtp()
									.remove(hazelKey + AppConstants.HAZEL_KEY_OTP_SESSION);

						}

					}

					/*
					 * Check the vendor is valid or not
					 */
					String vendorKey = authmodel.getVendor().toUpperCase();
					List<VendorEntity> vendorDetails = vendorRepository.findAllByApiKey(vendorKey);

					if (vendorDetails != null && vendorDetails.size() > 0) {

						/*
						 * Generate the auth code with Alpha numeric
						 */
						String authCode = CommonUtils.randomAlphaNumeric(20).toUpperCase();
						String temAuthCode = vendorKey + "_" + authCode;
						String shaKey = commonUtils.generateSHAKey(authmodel.getUserId(), authCode,
								vendorDetails.get(0).getApiSecret());
						HazelcastConfig.getInstance().getVendorAuthCode().put(shaKey,
								authmodel.getUserId() + "_" + vendorDetails.get(0).getAppName());
						HazelcastConfig.getInstance().getVendorAuthCode().put(temAuthCode.toUpperCase(),
								authmodel.getUserId());

						List<VendorSubcriptionEntity> vendorSubcriptionEntities = subcriptionRepository
								.findAllByUserIdAndAppIdAndActiveStatus(authmodel.getUserId(),
										vendorDetails.get(0).getId(), 1);
						if (vendorSubcriptionEntities != null && vendorSubcriptionEntities.size() > 0) {
							VendorSubcriptionEntity tempVendorSubcriptionEntity = vendorSubcriptionEntities.get(0);
							if (tempVendorSubcriptionEntity.getAuthorizationStatus() == 1) {
								respModel.setAuthorized(true);
								/*
								 * Build the response
								 */
								respModel.setRedirectUrl(vendorDetails.get(0).getRedirectUrl() + "?authCode=" + authCode
										+ "&userId=" + authmodel.getUserId());
							} else {
								respModel.setAuthorized(false);
							}
						} else {
							respModel.setAuthorized(false);
						}
					}
				}

				return prepareResponse.prepareSuccessResponseObject(respModel);
			}
			Log.error("KeyCloak info does not exist");
		} catch (Exception e) {
			e.printStackTrace();
		}

		return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
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
//		respModel.setRefreshToken(response.getRefreshToken());
		respModel.setKcRole(KcConstants.GUEST_USER);
		String hzKey = authmodel.getUserId() + "_" + authmodel.getSource();
		HazelcastConfig.getInstance().getKeycloakSession().remove(hzKey);
		HazelcastConfig.getInstance().getKeycloakSession().put(hzKey, response);
		return respModel;
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

}
