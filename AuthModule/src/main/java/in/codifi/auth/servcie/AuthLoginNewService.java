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

import org.jboss.resteasy.reactive.RestResponse;

import com.esotericsoftware.minlog.Log;
import com.fasterxml.jackson.databind.ObjectMapper;

import in.codifi.auth.config.ApplicationProperties;
import in.codifi.auth.config.HazelcastConfig;
import in.codifi.auth.entity.primary.DeviceMappingEntity;
import in.codifi.auth.entity.primary.VendorEntity;
import in.codifi.auth.entity.primary.VendorSubcriptionEntity;
import in.codifi.auth.model.request.AuthReq;
import in.codifi.auth.model.response.AuthRespModel;
import in.codifi.auth.model.response.GenericResponse;
import in.codifi.auth.model.response.UsersLoggedInRespModel;
import in.codifi.auth.repository.AccessLogManager;
import in.codifi.auth.repository.DeviceMappingRepository;
import in.codifi.auth.repository.VendorRepository;
import in.codifi.auth.repository.VendorSubcriptionRepository;
import in.codifi.auth.servcie.spec.AuthLoginNewServiceSpec;
import in.codifi.auth.utility.AppConstants;
import in.codifi.auth.utility.AppUtils;
import in.codifi.auth.utility.CommonUtils;
import in.codifi.auth.utility.KcConstants;
import in.codifi.auth.utility.PrepareResponse;
import in.codifi.auth.utility.StringUtil;
import in.codifi.ws.model.kb.login.ForgotOTPRestReqModel;
import in.codifi.ws.model.kb.login.ForgotOTPRestRespModel;
import in.codifi.ws.model.kb.login.QuickAuthLoginReqModel;
import in.codifi.ws.model.kb.login.QuickAuthRespModel;
import in.codifi.ws.model.kc.GetIntroSpectResponse;
import in.codifi.ws.model.kc.GetTokenResponse;
import in.codifi.ws.model.kc.GetUserInfoResp;
import in.codifi.ws.service.KambalaRestServices;
import in.codifi.ws.service.KcAdminRest;
import in.codifi.ws.service.KcTokenRest;

@ApplicationScoped
public class AuthLoginNewService implements AuthLoginNewServiceSpec {

	@Inject
	PrepareResponse prepareResponse;
	@Inject
	KambalaRestServices kambalaRestServices;
	@Inject
	AppUtils appUtils;
	@Inject
	KcAdminRest kcAdminRest;
	@Inject
	ApplicationProperties props;
	@Inject
	KcTokenRest kcTokenRest;
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

	/**
	 * method to forgot password otp
	 * 
	 * @author SowmiyaThangaraj
	 */
	@Override
	public RestResponse<GenericResponse> forgotPwdotp(AuthReq authmodel) {
		ForgotOTPRestRespModel forgotOTPRespModel = new ForgotOTPRestRespModel();
		try {
			if (!validateOtpParams(authmodel))
				return prepareResponse.prepareFailedResponse(AppConstants.INVALID_PARAMETER);
			HazelcastConfig.getInstance().getPassword().put(authmodel.getUserId(), authmodel.getPassword(), 300,
					TimeUnit.SECONDS);
			String request = prepareForgotOtpRequest(authmodel);
			if (StringUtil.isNotNullOrEmpty(request)) {
				forgotOTPRespModel = kambalaRestServices.forgotPwdOtp(request, authmodel.getUserId());
				if (forgotOTPRespModel.getReqStatus() != null) {
					return prepareResponse.prepareSuccessMessage(forgotOTPRespModel.getReqStatus());
				} else if (forgotOTPRespModel.getEmsg() != null) {
					return prepareResponse.prepareFailedResponse(forgotOTPRespModel.getEmsg());
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			Log.error("forgotPwdotp", e);
		}

		return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
	}

	/**
	 * method to prepare forgot otp request
	 * 
	 * @author SowmiyaThangaraj
	 * @param authmodel
	 * @return
	 */
	private String prepareForgotOtpRequest(AuthReq authmodel) {
		String request = "";
		ForgotOTPRestReqModel reqModel = new ForgotOTPRestReqModel();
		ObjectMapper mapper = new ObjectMapper();
		try {
			String pwd1 = appUtils.encryptWithSHA256(authmodel.getPassword());
			String pwd2 = appUtils.encryptWithSHA256(pwd1);
			String pwd3 = appUtils.encryptWithSHA256(pwd2);
			reqModel.setUserId(authmodel.getUserId());
			reqModel.setPassword(pwd3);
			String model = mapper.writeValueAsString(reqModel);
			request = AppConstants.JDATA + model;
			Log.info("forgotOTPRequest  :", request);
		} catch (Exception e) {
			e.printStackTrace();

		}
		return request;
	}

	/**
	 * method to validate otp params
	 * 
	 * @author SowmiyaThangaraj
	 * 
	 * @param authmodel
	 * @return
	 */
	private boolean validateOtpParams(AuthReq authmodel) {
		if (StringUtil.isNotNullOrEmpty(authmodel.getUserId())
				&& StringUtil.isNotNullOrEmpty(authmodel.getPassword())) {
			return true;
		}
		return false;
	}

	/**
	 * method to quick auth login
	 * 
	 * @author SowmiyaThangaraj
	 */
	@Override
	public RestResponse<GenericResponse> quickAuthLogin(AuthReq authmodel) {
		String hazelKey = authmodel.getUserId() + "_" + authmodel.getSource();
		QuickAuthRespModel quickAuthRespModel = new QuickAuthRespModel();
		UsersLoggedInRespModel loggedModel = new UsersLoggedInRespModel();
		Timestamp timestamp = new Timestamp(new Date().getTime());
		String hour = new SimpleDateFormat("HH:mm:ss").format(timestamp);
		AuthRespModel respModel = new AuthRespModel();
		respModel.setIsUpdate(0);
		try {
			List<GetUserInfoResp> userInfo = verifyClient(authmodel);
			if (userInfo != null && userInfo.size() > 0) {
				String request = prepareQuickAuthRequest(authmodel, userInfo);
				if (request != null) {
//					quickAuthLoginForWeb(authmodel, userInfo);
					quickAuthRespModel = kambalaRestServices.quickAuthBypassLogin(request, authmodel.getSource(),
							authmodel.getUserId());
					if (quickAuthRespModel.getStat().equalsIgnoreCase(AppConstants.REST_STATUS_OK)) {
						if (StringUtil.isNotNullOrEmpty(quickAuthRespModel.getSUserToken())) {
							updateUserCache(quickAuthRespModel, authmodel.getUserId());
							GetTokenResponse kcTokenResp = kcTokenRest.getUserToken(authmodel, 0);
//							quickAuthLoginForWeb(authmodel, userInfo);
//							quickAuthLoginForMob(authmodel, userInfo);
							if (kcTokenResp != null) {

								/** Return if failed to login on key clock **/
								if (StringUtil.isNotNullOrEmpty(kcTokenResp.getError()))
									return prepareResponse.prepareFailedResponse(kcTokenResp.getErrorDescription());

								if (StringUtil.isNotNullOrEmpty(kcTokenResp.getAccessToken())) {

									/** To get user roles by requesting user Introspect API **/
									GetIntroSpectResponse introSpectResponse = kcTokenRest.getIntroSpect(authmodel,
											kcTokenResp.getAccessToken());
									if (introSpectResponse != null && introSpectResponse.getClientRoles() != null
											&& introSpectResponse.getActive() != null) {

										if (!introSpectResponse.getActive()) {
											return prepareResponse
													.prepareFailedResponse(AppConstants.FAILED_STATUS_VALIDATE + " - "
															+ AppConstants.USER_BLOCKED);
										}
										String userName = introSpectResponse.getName();

										/** update fcmToken into device mapping **/
										updateDeviceMapping(authmodel.getUserId(), authmodel.getFcmToken(),
												authmodel.getSource(), userName.toUpperCase().trim());

										/** Logout old session If exist **/
										logoutFromKeycloak(authmodel);

										/** Add new session into Distributed Cache **/

										HazelcastConfig.getInstance().getKeycloakSession().put(hazelKey, kcTokenResp);
										HazelcastConfig.getInstance().getKeycloakUserInfo().put(hazelKey,
												introSpectResponse);

										respModel.setAccessToken(kcTokenResp.getAccessToken());

										List<String> resourceAccessRole = introSpectResponse.getClientRoles();

										if (resourceAccessRole.contains(KcConstants.ACTIVE_USER)) {
											respModel.setKcRole(KcConstants.ACTIVE_USER);
										} else if (resourceAccessRole.contains(KcConstants.GUEST_USER)) {
											respModel.setKcRole(KcConstants.GUEST_USER);
										}
										loggedModel.setSource(authmodel.getSource());
										loggedModel.setUserId(authmodel.getUserId());
										loggedModel.setTime(hour);
										String hazelcastKey = "";

										String key = authmodel.getUserId() + "_" + authmodel.getSource().toUpperCase();

										String response = HazelcastConfig.getInstance().getLogResponseModel().get(key);
										if (response == null) {
											HazelcastConfig.getInstance().getLogResponseModel().put(key, key);
											if (authmodel.getSource().equalsIgnoreCase("WEB")) {
												HazelcastConfig.getInstance().getWebLoggedInUsers()
														.put(authmodel.getUserId(), loggedModel);
												accessLogManager.insertUserLogginedInDetails(authmodel.getUserId(),
														authmodel.getSource(), hazelcastKey);
											}
											if (authmodel.getSource().equalsIgnoreCase("MOB")) {
												HazelcastConfig.getInstance().getMobLoggedInUsers()
														.put(authmodel.getUserId(), loggedModel);
												accessLogManager.insertUserLogginedInDetails(authmodel.getUserId(),
														authmodel.getSource(), hazelcastKey);
											}
											if (authmodel.getSource().equalsIgnoreCase("API")) {
												HazelcastConfig.getInstance().getApiLoggedInUsers()
														.put(authmodel.getUserId(), loggedModel);
												accessLogManager.insertUserLogginedInDetails(authmodel.getUserId(),
														authmodel.getSource(), hazelcastKey);
											}
										}

										if (authmodel.getVendor() != null) {
											/** For SSO Login **/

											if (HazelcastConfig.getInstance().getKeycloakMedianSession()
													.get(hazelKey) != null
													&& HazelcastConfig.getInstance().getKeycloakMedianUserInfo()
															.get(hazelKey) != null) {

												GetTokenResponse intermediateToken = HazelcastConfig.getInstance()
														.getKeycloakMedianSession().get(hazelKey);
												GetIntroSpectResponse intermediateUserInfo = HazelcastConfig
														.getInstance().getKeycloakMedianUserInfo().get(hazelKey);

												if (HazelcastConfig.getInstance().getKeycloakSession()
														.get(hazelKey) != null) {
													/** Logout new web/mob session. This session no need **/
													kcTokenRest.logout(intermediateUserInfo.getUserId(),
															intermediateToken.getAccessToken(),
															intermediateToken.getRefreshToken());
													/** Clear intermediate cache **/
													HazelcastConfig.getInstance().getKeycloakMedianSession()
															.remove(hazelKey);
													HazelcastConfig.getInstance().getKeycloakMedianUserInfo()
															.remove(hazelKey);
													HazelcastConfig.getInstance().getUserSessionOtp()
															.remove(hazelKey + AppConstants.HAZEL_KEY_OTP_SESSION);

													GetTokenResponse token = HazelcastConfig.getInstance()
															.getKeycloakSession().get(hazelKey);
													respModel.setAccessToken(token.getAccessToken());
//														respModel.setRefreshToken(token.getRefreshToken());
													respModel.setKcRole(KcConstants.ACTIVE_USER);
												} else {

													HazelcastConfig.getInstance().getKeycloakSession().put(hazelKey,
															intermediateToken);
													HazelcastConfig.getInstance().getKeycloakUserInfo().put(hazelKey,
															intermediateUserInfo);

													respModel.setAccessToken(intermediateToken.getAccessToken());
//														respModel.setRefreshToken(intermediateToken.getRefreshToken());
													respModel.setKcRole(KcConstants.ACTIVE_USER);

													/** Clear intermediate cache **/
													HazelcastConfig.getInstance().getKeycloakMedianSession()
															.remove(hazelKey);
													HazelcastConfig.getInstance().getKeycloakMedianUserInfo()
															.remove(hazelKey);
													HazelcastConfig.getInstance().getUserSessionOtp()
															.remove(hazelKey + AppConstants.HAZEL_KEY_OTP_SESSION);

												}

											}
											/*
											 * Check the vendor is valid or not
											 */
											String vendorKey = authmodel.getVendor().toUpperCase();
											List<VendorEntity> vendorDetails = vendorRepository
													.findAllByApiKey(vendorKey);

											if (vendorDetails != null && vendorDetails.size() > 0) {

												/*
												 * Generate the auth code with Alpha numeric
												 */
												String authCode = CommonUtils.randomAlphaNumeric(20).toUpperCase();
												String temAuthCode = vendorKey + "_" + authCode;
												String shaKey = commonUtils.generateSHAKey(authmodel.getUserId(),
														authCode, vendorDetails.get(0).getApiSecret());
												HazelcastConfig.getInstance().getVendorAuthCode().put(shaKey,
														authmodel.getUserId() + "_"
																+ vendorDetails.get(0).getAppName());
												HazelcastConfig.getInstance().getVendorAuthCode()
														.put(temAuthCode.toUpperCase(), authmodel.getUserId());

												List<VendorSubcriptionEntity> vendorSubcriptionEntities = subcriptionRepository
														.findAllByUserIdAndAppIdAndActiveStatus(authmodel.getUserId(),
																vendorDetails.get(0).getId(), 1);
												if (vendorSubcriptionEntities != null
														&& vendorSubcriptionEntities.size() > 0) {
													VendorSubcriptionEntity tempVendorSubcriptionEntity = vendorSubcriptionEntities
															.get(0);
													if (tempVendorSubcriptionEntity.getAuthorizationStatus() == 1) {
														respModel.setAuthorized(true);
														/*
														 * Build the response
														 */
														respModel.setRedirectUrl(vendorDetails.get(0).getRedirectUrl()
																+ "?authCode=" + authCode + "&userId="
																+ authmodel.getUserId());
													} else {
														respModel.setAuthorized(false);
													}
												} else {
													respModel.setAuthorized(false);
												}
											}
											createSsoSession(authmodel);
										}

										return prepareResponse.prepareSuccessResponseObject(respModel);

									}
								}
							} else {
								return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS_VALIDATE);
							}

						} else if (quickAuthRespModel.getStat().equalsIgnoreCase(AppConstants.REST_STATUS_NOT_OK)) {
							if (StringUtil.isNotNullOrEmpty(quickAuthRespModel.getEmsg())
									&& quickAuthRespModel.getEmsg().contains("Password Expired")) {
								respModel.setIsUpdate(1);
							}
							return prepareResponse.prepareFailedResponseObjectQuickAuth(respModel,
									quickAuthRespModel.getEmsg());
						}

					} else if (StringUtil.isNotNullOrEmpty(quickAuthRespModel.getEmsg())) {
						System.out.println("Failed to get REST Session -" + quickAuthRespModel.getEmsg());
						Log.error("Failed to get REST Session -" + quickAuthRespModel.getEmsg());
						if (StringUtil.isNotNullOrEmpty(quickAuthRespModel.getEmsg())
								&& quickAuthRespModel.getEmsg().contains("Password Expired")) {
							respModel.setIsUpdate(1);
						}
						return prepareResponse.prepareFailedResponseObjectQuickAuth(respModel,
								quickAuthRespModel.getEmsg());
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
			reqModel.setFactor2(authmodel.getOtp());
			reqModel.setImei("0.0.0");
			reqModel.setUId(userInfo.get(0).getUsername().toUpperCase());
			reqModel.setSource("API");
			String appKey = appUtils
					.encryptWithSHA256(userInfo.get(0).getUsername().toUpperCase() + "|" + props.getAppKey());
			reqModel.setAppKey(appKey);
			reqModel.setVendorCode(props.getVendorCode());
			String password = HazelcastConfig.getInstance().getPassword().get(authmodel.getUserId());
			String pwd = appUtils.encryptWithSHA256(password);
			reqModel.setPwd(pwd);
			String reqText = mapper.writeValueAsString(reqModel);
			request = AppConstants.JDATA + reqText;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return request;
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
	 * method to quick auth login for web
	 * 
	 * @author SowmiyaThangaraj
	 * 
	 * @return
	 */
	public RestResponse<GenericResponse> quickAuthLoginForWeb(AuthReq authmodel, List<GetUserInfoResp> userInfo) {
		QuickAuthRespModel quickAuthRespModel = new QuickAuthRespModel();
		try {
			String request = null;
			ObjectMapper mapper = new ObjectMapper();
			try {
				QuickAuthLoginReqModel reqModel = new QuickAuthLoginReqModel();
				reqModel.setApkVersion("1.0.0");
				reqModel.setFactor2(authmodel.getOtp());
				reqModel.setImei("0.0.0");
				reqModel.setUId(userInfo.get(0).getUsername().toUpperCase());
				reqModel.setSource("WEB");
				String appKey = appUtils
						.encryptWithSHA256(userInfo.get(0).getUsername().toUpperCase() + "|" + props.getWebAppKey());
				reqModel.setAppKey(appKey);
				reqModel.setVendorCode(props.getWebVendorCode());
				String password = HazelcastConfig.getInstance().getPassword().get(authmodel.getUserId());
				String pwd = appUtils.encryptWithSHA256(password);
				reqModel.setPwd(pwd);
				String reqText = mapper.writeValueAsString(reqModel);
				request = AppConstants.JDATA + reqText;
				quickAuthRespModel = kambalaRestServices.quickAuthBypassLoginforWeb(request, authmodel.getSource(),
						authmodel.getUserId());

			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
	}

	/**
	 * method to quick auth login for mob
	 * 
	 * @author SowmiyaThangaraj
	 * @param userInfo
	 * @param authmodel
	 * @return
	 */
	public RestResponse<GenericResponse> quickAuthLoginForMob(AuthReq authmodel, List<GetUserInfoResp> userInfo) {
		QuickAuthRespModel quickAuthRespModel = new QuickAuthRespModel();
		try {
			String request = null;
			ObjectMapper mapper = new ObjectMapper();
			try {
				QuickAuthLoginReqModel reqModel = new QuickAuthLoginReqModel();
				reqModel.setApkVersion("1.0.0");
				reqModel.setFactor2(authmodel.getOtp());
				reqModel.setImei("0.0.0");
				reqModel.setUId(userInfo.get(0).getUsername().toUpperCase());
				reqModel.setSource("MOB");
				String appKey = appUtils
						.encryptWithSHA256(userInfo.get(0).getUsername().toUpperCase() + "|" + props.getMobAppKey());
				reqModel.setAppKey(appKey);
				reqModel.setVendorCode(props.getMobVendorCode());
				String password = HazelcastConfig.getInstance().getPassword().get(authmodel.getUserId());
				String pwd = appUtils.encryptWithSHA256(password);
				reqModel.setPwd(pwd);
				String reqText = mapper.writeValueAsString(reqModel);
				request = AppConstants.JDATA + reqText;
				quickAuthRespModel = kambalaRestServices.quickAuthBypassLoginForMob(request, authmodel.getSource(),
						authmodel.getUserId());

			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
	}

}
