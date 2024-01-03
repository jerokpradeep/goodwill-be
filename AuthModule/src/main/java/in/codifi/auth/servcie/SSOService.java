package in.codifi.auth.servcie;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.jboss.resteasy.reactive.RestResponse;

import in.codifi.auth.config.HazelcastConfig;
import in.codifi.auth.entity.primary.VendorEntity;
import in.codifi.auth.entity.primary.VendorSubcriptionEntity;
import in.codifi.auth.model.request.AuthReq;
import in.codifi.auth.model.request.VendorReqModel;
import in.codifi.auth.model.response.Auth1ResponseModel;
import in.codifi.auth.model.response.AuthRespModel;
import in.codifi.auth.model.response.CommonErrorResponse;
import in.codifi.auth.model.response.GenericResponse;
import in.codifi.auth.model.response.VendorAppRespModel;
import in.codifi.auth.model.response.VendorDetailsRespModel;
import in.codifi.auth.model.response.VendorRespModel;
import in.codifi.auth.repository.AccessLogManager;
import in.codifi.auth.repository.ApiKeyRepository;
import in.codifi.auth.repository.VendorRepository;
import in.codifi.auth.repository.VendorSubcriptionDAO;
import in.codifi.auth.repository.VendorSubcriptionRepository;
import in.codifi.auth.servcie.spec.SSOServiceSpec;
import in.codifi.auth.utility.APITokenModule;
import in.codifi.auth.utility.AppConstants;
import in.codifi.auth.utility.CommonUtils;
import in.codifi.auth.utility.EmailUtils;
import in.codifi.auth.utility.KcConstants;
import in.codifi.auth.utility.PrepareResponse;
import in.codifi.auth.utility.StringUtil;
import in.codifi.ws.model.kc.GetIntroSpectResponse;
import in.codifi.ws.model.kc.GetTokenResponse;
import in.codifi.ws.service.KcTokenRest;
import io.quarkus.logging.Log;

@ApplicationScoped
public class SSOService implements SSOServiceSpec {

	@Inject
	PrepareResponse prepareResponse;

//	@Inject
//	VendorRepository vendorRepository;

	@Inject
	VendorRepository vendorRepository;

	@Inject
	VendorSubcriptionRepository subcriptionRepository;

	@Inject
	VendorSubcriptionDAO subcriptionDao;

	@Inject
	CommonUtils commonUtils;

	@Inject
	KcTokenRest kcTokenRest;

	@Inject
	ApiKeyRepository apiKeyDao;

	@Inject
	EmailUtils emailUtils;

	@Inject
	AccessLogManager accessLogManager;

	/**
	 * Method to authorize Vendor
	 * 
	 * @author dinesh
	 * @param vendorReqModel
	 * @return
	 */
	@Override
	public RestResponse<GenericResponse> ssoAuthorizeVendor(VendorReqModel authReq) {

		try {

			if (StringUtil.isNullOrEmpty(authReq.getVendor()) || StringUtil.isNullOrEmpty(authReq.getUserId()))
				return prepareResponse.prepareFailedResponse(AppConstants.INVALID_PARAMETER);

			String apiKey = authReq.getVendor().toUpperCase();
			// Check the Vendor details with this API Key
			List<VendorEntity> vendorEntities = vendorRepository
					.findAllByApiKeyAndAuthorizationStatusAndActiveStatus(apiKey, 1, 1);
			if (vendorEntities == null || vendorEntities.size() <= 0)
				return prepareResponse.prepareFailedResponse(AppConstants.INVALID_PARAMETER);

			VendorEntity vendorDetails = vendorEntities.get(0);

			List<VendorSubcriptionEntity> vendorSubcriptionEntities = subcriptionRepository
					.findAllByUserIdAndAppIdAndActiveStatus(authReq.getUserId(), vendorDetails.getId(), 1);
//			if (vendorSubcriptionEntities == null || vendorSubcriptionEntities.size() <= 0)
//				return prepareResponse.prepareFailedResponse(AppConstants.INVALID_PARAMETER);

//			VendorSubcriptionEntity tempVendorSubcriptionEntity = vendorSubcriptionEntities.get(0);
			VendorRespModel vendorRespModel = new VendorRespModel();
			if (vendorSubcriptionEntities != null && vendorSubcriptionEntities.size() > 0
					&& vendorSubcriptionEntities.get(0).getAuthorizationStatus() == 1) {

				String authCode = CommonUtils.randomAlphaNumeric(20).toUpperCase();
				String temAuthCode = apiKey + "_" + authCode;
				String shaKey = commonUtils.generateSHAKey(authReq.getUserId(), authCode, vendorDetails.getApiSecret());
				HazelcastConfig.getInstance().getVendorAuthCode().put(shaKey,
						authReq.getUserId() + "_" + vendorDetails.getAppName());
				HazelcastConfig.getInstance().getVendorAuthCode().put(temAuthCode.toUpperCase(), authReq.getUserId());
				vendorRespModel.setRedirectUrl(
						vendorDetails.getRedirectUrl() + "?authCode=" + authCode + "&userId=" + authReq.getUserId());
				vendorRespModel.setAuthorized(true);
				return prepareResponse.prepareSuccessResponseObject(vendorRespModel);
			} else {

				/**
				 * Insert in to data base as the user authorized the vendor
				 */
				VendorSubcriptionEntity subcriptionEntity = new VendorSubcriptionEntity();
				subcriptionEntity.setAppId(vendorDetails.getId());
				subcriptionEntity.setUserId(authReq.getUserId());
				subcriptionEntity.setAuthorizationStatus(1);
				subcriptionEntity.setCreatedBy(authReq.getUserId());
//				VendorSubcriptionEntity subcriptionEntityNew = subcriptionRepository.saveAndFlush(subcriptionEntity);
				boolean authorizeVendor = subcriptionDao.authorizeUser(vendorDetails.getId(), authReq.getUserId());
				if (authorizeVendor) {
					String authCode = CommonUtils.randomAlphaNumeric(20).toUpperCase();
					String temAuthCode = apiKey + "_" + authCode;
					String shaKey = commonUtils.generateSHAKey(authReq.getUserId(), authCode,
							vendorDetails.getApiSecret());
					HazelcastConfig.getInstance().getVendorAuthCode().put(shaKey,
							authReq.getUserId() + "_" + vendorDetails.getAppName());
					HazelcastConfig.getInstance().getVendorAuthCode().put(temAuthCode.toUpperCase(),
							authReq.getUserId());
					vendorRespModel.setRedirectUrl(vendorDetails.getRedirectUrl() + "?authCode=" + authCode + "&userId="
							+ authReq.getUserId());
					vendorRespModel.setAuthorized(true);
					return prepareResponse.prepareSuccessResponseObject(vendorRespModel);
				} else {
					return prepareResponse.prepareFailedResponse(AppConstants.INTERNAL_ERROR);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			Log.error(e.getMessage());
		}
		return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
	}

	/**
	 * Method to check Vendor Authorization
	 * 
	 * @author dinesh
	 * @param vendorReqModel
	 * @return
	 */
	@Override
	public RestResponse<GenericResponse> checkVendorAuthorization(VendorReqModel authReq) {

		try {

			if (StringUtil.isNullOrEmpty(authReq.getVendor()) || StringUtil.isNullOrEmpty(authReq.getUserId()))
				return prepareResponse.prepareFailedResponse(AppConstants.INVALID_PARAMETER);

			String apiKey = authReq.getVendor().toUpperCase();
			// Check the Vendor details with this API Key
			List<VendorEntity> vendorEntities = vendorRepository
					.findAllByApiKeyAndAuthorizationStatusAndActiveStatus(apiKey, 1, 1);
			if (vendorEntities == null || vendorEntities.size() <= 0)
				return prepareResponse.prepareFailedResponse(AppConstants.INVALID_PARAMETER);

			VendorEntity vendorDetails = vendorEntities.get(0);

			List<VendorSubcriptionEntity> vendorSubcriptionEntities = subcriptionRepository
					.findAllByUserIdAndAppIdAndActiveStatus(authReq.getUserId(), vendorDetails.getId(), 1);
//			if (vendorSubcriptionEntities == null || vendorSubcriptionEntities.size() <= 0)
//				return prepareResponse.prepareFailedResponse(AppConstants.INVALID_PARAMETER);

			VendorRespModel vendorRespModel = new VendorRespModel();

			String authCode = CommonUtils.randomAlphaNumeric(20).toUpperCase();
			String temAuthCode = apiKey + "_" + authCode;
			String shaKey = commonUtils.generateSHAKey(authReq.getUserId(), authCode, vendorDetails.getApiSecret());
			HazelcastConfig.getInstance().getVendorAuthCode().put(shaKey,
					authReq.getUserId() + "_" + vendorDetails.getAppName());
			HazelcastConfig.getInstance().getVendorAuthCode().put(temAuthCode.toUpperCase(), authReq.getUserId());

//			VendorSubcriptionEntity tempVendorSubcriptionEntity = vendorSubcriptionEntities.get(0);
//			if (tempVendorSubcriptionEntity.getAuthorizationStatus() == 1) {

			if (vendorSubcriptionEntities != null && vendorSubcriptionEntities.size() > 0
					&& vendorSubcriptionEntities.get(0).getAuthorizationStatus() == 1) {
				vendorRespModel.setRedirectUrl(
						vendorDetails.getRedirectUrl() + "?authCode=" + authCode + "&userId=" + authReq.getUserId());
				vendorRespModel.setAuthorized(true);
			} else {
				vendorRespModel.setAuthorized(false);
			}
			return prepareResponse.prepareSuccessResponseObject(vendorRespModel);

		} catch (Exception e) {
			e.printStackTrace();
			Log.error(e.getMessage());
		}
		return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
	}

	@Override
	public Object getUserDetails(VendorReqModel authReq) {
		CommonErrorResponse errorResponse = new CommonErrorResponse();
		System.out.println("Reached User details");
		try {
			if (authReq == null) {
				errorResponse.setStat(AppConstants.STATUS_NOT_OK_API);
				errorResponse.setEmsg(AppConstants.INVALID_PARAMETER);
				return errorResponse;
			}

			if (StringUtil.isNullOrEmpty(authReq.getCheckSum())) {
				errorResponse.setStat(AppConstants.STATUS_NOT_OK_API);
				errorResponse.setEmsg(AppConstants.INVALID_PARAMETER);
				return errorResponse;
			}

			/**
			 * Get user by the given auth code
			 */
//			if (StringUtil.isNullOrEmpty(
//					HazelcastConfig.getInstance().getVendorAuthCodeKB().get(authReq.getCheckSum().toUpperCase()))) {
//				errorResponse.setStat(AppConstants.STATUS_NOT_OK_API);
//				errorResponse.setEmsg(AppConstants.INVALID_AUTH_CODE);
//				return errorResponse;
//			}
			System.out.println("CheckSum" + authReq.getCheckSum());
			if (StringUtil.isNotNullOrEmpty(
					HazelcastConfig.getInstance().getVendorAuthCode().get(authReq.getCheckSum().toUpperCase()))
					|| StringUtil.isNotNullOrEmpty(HazelcastConfig.getInstance().getVendorAuthCode()
							.get(authReq.getCheckSum().toUpperCase()))) {

				String vendoeDetails = HazelcastConfig.getInstance().getVendorAuthCode()
						.get(authReq.getCheckSum().toUpperCase());
				if (vendoeDetails == null || vendoeDetails.isBlank() || vendoeDetails.isEmpty()) {
					vendoeDetails = HazelcastConfig.getInstance().getVendorAuthCode()
							.get(authReq.getCheckSum().toUpperCase());
				}
				String[] details = vendoeDetails.split("_");
				String clientId = details[0];
				String vendorName = details[1];
				System.out.println("clientId in getUserDetails -" + clientId);
				System.out.println("vendorName in getUserDetails -" + vendorName);
				/**
				 * Check the per hour count and set into the cache
				 */
				if (HazelcastConfig.getInstance().getApiRequestCount().get(clientId) == null) {
					HazelcastConfig.getInstance().getApiRequestCount().put(clientId,
							Long.parseLong(AppConstants.API_REQUEST_COUNT));
				} else {
					long count = HazelcastConfig.getInstance().getApiRequestCount().get(clientId);
					HazelcastConfig.getInstance().getApiRequestCount().put(clientId, count);
				}
				String userSessionId = getStringUserSessionIdNew(clientId, vendorName);
				System.out.println("userSessionId in  User details" + userSessionId);
				if (StringUtil.isNotNullOrEmpty(userSessionId)) {
					Auth1ResponseModel authRespModel = new Auth1ResponseModel();
					HazelcastConfig.getInstance().getApiUser256Cache().put(clientId, userSessionId);
					APITokenModule.storeTokenCache(userSessionId, clientId);
					authRespModel.setUserSession(userSessionId);
					authRespModel.setClientId(clientId);
					authRespModel.setStat(AppConstants.STATUS_OK_API);
					System.out.println("Success resp in  User details");
					return authRespModel;
				} else {
					System.out.println("Failed to get access token");
					Log.error("Failed to get access token");
				}

			} else {
				System.out.println("Invalid auth code");
				errorResponse.setStat(AppConstants.STATUS_NOT_OK_API);
				errorResponse.setEmsg(AppConstants.INVALID_AUTH_CODE);
				return errorResponse;
			}

		} catch (Exception e) {
			System.out.println("Inside Exception");
			e.printStackTrace();
			Log.error(e.getMessage());
		}
		System.out.println("outside Exception");
		errorResponse.setStat(AppConstants.STATUS_NOT_OK_API);
		errorResponse.setEmsg(AppConstants.FAILED_STATUS);
		return errorResponse;
	}

	/**
	 * Method to generate the new access token
	 * 
	 * @author Gowrisankar
	 * @param pUserId
	 * @return
	 */
	private String getStringUserSessionIdNew(String pUserId, String appName) {
		String accessToken = "";
		try {
			AuthReq authmodel = new AuthReq();
			authmodel.setUserId(pUserId);
			authmodel.setVendor(appName);
//			authmodel.setSource("WEB");
			AuthRespModel respModel = keyCloakLoginByRefershToken(authmodel);
			if (respModel != null && StringUtil.isNotNullOrEmpty(respModel.getAccessToken())) {
				accessToken = respModel.getAccessToken();
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.error(e.getMessage());
		}
		return accessToken;
	}

	@Override
	public Object getUserDetailsByAuth(VendorReqModel authReq) {
		CommonErrorResponse errorResponse = new CommonErrorResponse();
		try {
			if (StringUtil.isNullOrEmpty(authReq.getVendor()) || StringUtil.isNullOrEmpty(authReq.getAuthCode())) {
				errorResponse.setStat(AppConstants.STATUS_NOT_OK_API);
				errorResponse.setEmsg(AppConstants.INVALID_PARAMETER);
				return errorResponse;
			}
			/**
			 * Check with the valid vendor or not
			 */
			List<VendorEntity> vendorEntities = vendorRepository
					.findAllByApiKeyAndAuthorizationStatusAndActiveStatus(authReq.getVendor(), 1, 1);
			if (vendorEntities == null || vendorEntities.size() <= 0) {
				errorResponse.setStat(AppConstants.STATUS_NOT_OK_API);
				errorResponse.setEmsg(AppConstants.INVALID_VENDOR);
				return errorResponse;

			}
			VendorEntity vendorDetails = vendorEntities.get(0);
			String tempAuthCode = vendorDetails.getApiKey() + "_" + authReq.getAuthCode();
			/**
			 * Get user by the given auth code
			 */
			if (StringUtil
					.isNullOrEmpty(HazelcastConfig.getInstance().getVendorAuthCode().get(tempAuthCode.toUpperCase()))) {
				errorResponse.setStat(AppConstants.STATUS_NOT_OK_API);
				errorResponse.setEmsg(AppConstants.INVALID_AUTH_CODE);
				return errorResponse;
			}

			String clientId = HazelcastConfig.getInstance().getVendorAuthCode().get(tempAuthCode.toUpperCase());
			/**
			 * TODO : Check the per hour count and set into the cache
			 */

			String userSessionId = getStringUserSessionIdNew(clientId, vendorDetails.getAppName());
			if (StringUtil.isNotNullOrEmpty(userSessionId)) {
				Auth1ResponseModel authRespModel = new Auth1ResponseModel();
				HazelcastConfig.getInstance().getApiUser256Cache().put(clientId, userSessionId);
				APITokenModule.storeTokenCache(userSessionId, clientId);
				authRespModel.setUserSession(userSessionId);
				authRespModel.setClientId(clientId);
				authRespModel.setStat(AppConstants.STATUS_OK_API);
				return authRespModel;
			} else {
				Log.error("Failed to get access token");
			}

		} catch (Exception e) {
			e.printStackTrace();
			Log.error(e.getMessage());
		}
		errorResponse.setStat(AppConstants.STATUS_NOT_OK_API);
		errorResponse.setEmsg(AppConstants.FAILED_STATUS);
		return errorResponse;
	}

	/**
	 * KC login while logging with vendor
	 * 
	 * @author Gowrisankar
	 * @param authmodel
	 * @return
	 */
	public AuthRespModel keyCloakLoginByRefershToken(AuthReq authmodel) {
		AuthRespModel respModel = new AuthRespModel();
		try {

			/** Get refresh token **/
//			String hazelKey = authmodel.getUserId() + "_" + authmodel.getSource();
//			String userOmsType = HazelcastConfig.getInstance().getUserOmsType().get(authmodel.getUserId());
//			GetTokenResponse cacheKcTokenResp = null;
//			if (userOmsType.equalsIgnoreCase("OMK")) {
//				cacheKcTokenResp = HazelcastConfig.getInstance().getKeycloakSessionKB().get(hazelKey);
//			} else {
//				cacheKcTokenResp = HazelcastConfig.getInstance().getKeycloakSession().get(hazelKey);
//			}

			String hazelKeySso = authmodel.getUserId() + "_" + AppConstants.SOURCE_SSO;

			GetTokenResponse ssoMasterSession = null;

			ssoMasterSession = HazelcastConfig.getInstance().getSsoKeycloakSession().get(hazelKeySso);
			System.out.println("ssoMasterSession  -" + ssoMasterSession);

			if (ssoMasterSession != null) {
				if (StringUtil.isNullOrEmpty(ssoMasterSession.getRefreshToken())) {
					Log.error("KC-LoginByRefershToken - Refresh Token is null");
					return null;
				}

				GetTokenResponse kcTokenResp = kcTokenRest
						.getUserTokenByRefereshToken(ssoMasterSession.getRefreshToken());

				if (kcTokenResp != null) {

					/** Return if failed to login on key clock **/
					if (StringUtil.isNotNullOrEmpty(kcTokenResp.getError())) {
						Log.error(kcTokenResp.getErrorDescription());
						return null;
					}

					if (StringUtil.isNotNullOrEmpty(kcTokenResp.getAccessToken())) {

						/** To get user roles by requesting user Introspect API **/
						GetIntroSpectResponse introSpectResponse = kcTokenRest.getIntroSpect(authmodel,
								kcTokenResp.getAccessToken());
						if (introSpectResponse != null && introSpectResponse.getClientRoles() != null
								&& introSpectResponse.getActive() != null) {

							if (!introSpectResponse.getActive()) {
								Log.error("KC-Login - " + AppConstants.USER_BLOCKED);
								return null;

							}

							/** Add new session into Distributed Cache **/
							String hazelKeySsoVendor = authmodel.getUserId() + "_" + AppConstants.SOURCE_SSO
									+ authmodel.getVendor();
							System.out.println("hazelKeySsoVendor -" + hazelKeySsoVendor);
							HazelcastConfig.getInstance().getKeycloakSession().put(hazelKeySsoVendor, kcTokenResp);
							HazelcastConfig.getInstance().getKeycloakUserInfo().put(hazelKeySsoVendor,
									introSpectResponse);
//							UsersLoggedInRespModel loggedModel = new UsersLoggedInRespModel();
//							Timestamp timestamp = new Timestamp(new Date().getTime());
//							String hour = new SimpleDateFormat("HH:mm:ss").format(timestamp);
//							loggedModel.setSource(hazelKeySsoVendor);
//							loggedModel.setUserId(authmodel.getUserId());
//							loggedModel.setTime(hour);
//							loggedModel.setVendor(authmodel.getVendor());
//							HazelcastConfig.getInstance().getSsoLoggedInUsers().put(hazelKeySsoVendor, loggedModel);
//							accessLogManager.insertUserLogginedInDetails(authmodel.getUserId(), authmodel.getSource(),
//									hazelKeySsoVendor);

							respModel.setAccessToken(kcTokenResp.getAccessToken());
//							respModel.setRefreshToken(kcTokenResp.getRefreshToken());
							System.out.println("New Token -" + kcTokenResp.getAccessToken());
							List<String> resourceAccessRole = introSpectResponse.getClientRoles();

							if (resourceAccessRole.contains(KcConstants.ACTIVE_USER)) {
								respModel.setKcRole(KcConstants.ACTIVE_USER);
							} else if (resourceAccessRole.contains(KcConstants.GUEST_USER)) {
								respModel.setKcRole(KcConstants.GUEST_USER);
							}

							return respModel;

						}
					}
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	/**
	 * Method to get vendor app deatils
	 * 
	 * @author Dinesh Kumar
	 * @param authReq
	 * @return
	 */
	@Override
	public RestResponse<GenericResponse> getVendorAppDetails(VendorReqModel authReq) {
		VendorDetailsRespModel response = new VendorDetailsRespModel();
		try {
			if (StringUtil.isNullOrEmpty(authReq.getVendor()))
				return prepareResponse.prepareFailedResponse(AppConstants.INVALID_PARAMETER);
			/**
			 * Check with the valid vendor or not
			 */
			List<VendorEntity> vendorEntities = vendorRepository
					.findAllByApiKeyAndAuthorizationStatusAndActiveStatus(authReq.getVendor(), 1, 1);
			if (vendorEntities == null || vendorEntities.size() <= 0)
				return prepareResponse.prepareFailedResponse(AppConstants.INVALID_VENDOR);
			response.setImageUrl(vendorEntities.get(0).getIconUrl());
			response.setAppName(vendorEntities.get(0).getAppName());

			return prepareResponse.prepareSuccessResponseObject(response);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
	}

	/**
	 * Method to prepare response for vendor app model
	 * 
	 * @author Dinesh Kumar
	 * @param vendorAppEntities
	 * @return
	 */
	@SuppressWarnings("unused")
	private List<VendorAppRespModel> prepareResponse(List<VendorEntity> vendorAppEntities) {

		List<VendorAppRespModel> responseModel = new ArrayList<>();
		try {

			for (VendorEntity entity : vendorAppEntities) {
				VendorAppRespModel result = new VendorAppRespModel();
				result.setAppId(entity.getId());
				result.setAppName(entity.getAppName());
				result.setApiKey(entity.getApiKey());
				result.setApiSecret(entity.getApiSecret());
				result.setClientId(entity.getClientId());
				result.setRedirectUrl(entity.getRedirectUrl());
				result.setPostbackUrl(entity.getPostbackUrl());
				result.setDescription(entity.getDescription());
				result.setAuthorizationStatus(entity.getAuthorizationStatus());
				result.setContactName(entity.getContactName());
				result.setMobieNo(entity.getMobileNo());
				result.setEmail(entity.getEmail());
				result.setType(entity.getType());
				responseModel.add(result);
			}

		} catch (Exception e) {
			e.printStackTrace();
			Log.error(e.getMessage());
		}
		return responseModel;
	}

}
