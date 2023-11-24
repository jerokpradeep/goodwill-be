package in.codifi.sso.auth.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.jboss.resteasy.reactive.RestResponse;

import in.codifi.sso.auth.config.HazelcastConfig;
import in.codifi.sso.auth.entity.primary.ApiKeyEntity;
import in.codifi.sso.auth.model.request.ApiKeyReqModel;
import in.codifi.sso.auth.model.response.ApiKeyRespModel;
import in.codifi.sso.auth.model.response.GenericResponse;
import in.codifi.sso.auth.repository.ApiKeyDao;
import in.codifi.sso.auth.service.spec.ApiServiceSpec;
import in.codifi.sso.auth.utility.AppConstants;
import in.codifi.sso.auth.utility.CommonUtils;
import in.codifi.sso.auth.utility.EmailUtils;
import in.codifi.sso.auth.utility.PrepareResponse;
import in.codifi.sso.auth.utility.StringUtil;
import in.codifi.ws.model.kc.GetIntroSpectResponse;
import io.quarkus.logging.Log;

@ApplicationScoped
public class ApiService implements ApiServiceSpec {

	@Inject
	ApiKeyDao apiKeyDao;

	@Inject
	PrepareResponse prepareResponse;

	@Inject
	CommonUtils commonUtils;

	@Inject
	EmailUtils emailUtils;

	/**
	 * Method to get API Key
	 * 
	 * @author Dinesh Kumar
	 */
	@Override
	public RestResponse<GenericResponse> getApiKey(String userId) {
		try {
			ApiKeyRespModel returnDto = new ApiKeyRespModel();
			ApiKeyEntity response = apiKeyDao.getAPIDetails(userId);
			if (response == null)
				return prepareResponse.prepareFailedResponse(AppConstants.INTERNAL_ERROR);

			if (StringUtil.isNullOrEmpty(response.getApi_key()))
				return prepareResponse.prepareFailedResponse(AppConstants.API_NOT_AVAILABLE);

			returnDto.setAvailable(true);
			String apiKey = response.getApi_key();
			returnDto.setExpiryDate(response.getExpiryDate());
			returnDto.setApiKey(apiKey);
			if (response.getExpiryDate() != null) {
				if (response.getExpiryDate().getTime() < new Date().getTime()) {
					returnDto.setExpired(true);
					returnDto.setMessage(AppConstants.APIKEY_EXPIRED);
				} else {
					returnDto.setExpired(false);
				}
			}
			return prepareResponse.prepareSuccessResponseObject(returnDto);

		} catch (Exception e) {
			Log.error("getApiKey -" + e);
		}
		return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
	}

	/**
	 * Method to create API Key
	 * 
	 * @author Dinesh Kumar
	 * @param req
	 * @return
	 */
	@Override
	public RestResponse<GenericResponse> generateApiKey(ApiKeyReqModel req) {
		try {

			if (StringUtil.isNullOrEmpty(req.getSource()))
				return prepareResponse.prepareFailedResponse(AppConstants.INVALID_PARAMETER);

			ApiKeyEntity response = apiKeyDao.getAPIDetails(req.getUserId());

			if (StringUtil.isNotNullOrEmpty(response.getApi_key()))
				return prepareResponse.prepareFailedResponse(AppConstants.API_EXIST);

			String apiKey = commonUtils.generatealpanumeric();
			GetIntroSpectResponse userInfo = null;
			String emailId = "";
			if (HazelcastConfig.getInstance().getKeycloakUserInfoKB()
					.containsKey(req.getUserId() + "_" + req.getSource())) {
				userInfo = HazelcastConfig.getInstance().getKeycloakUserInfoKB()
						.get(req.getUserId() + "_" + req.getSource());
				emailId = userInfo.getEmail();
			}

			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			java.sql.Timestamp timestamp = new java.sql.Timestamp(Calendar.getInstance().getTimeInMillis());
			Calendar cal = Calendar.getInstance();
			cal.setTime(timestamp);
			cal.add(Calendar.DATE, 7);
			String exp = dateFormat.format(cal.getTime());
			String subcrition = apiKeyDao.activateSubcripstion(req.getUserId(), apiKey, exp);
			if (subcrition.equalsIgnoreCase(AppConstants.SUCCESS_STATUS))
				if (StringUtil.isNotNullOrEmpty(emailId)) {
					emailUtils.sendEmailAPIKey("User", emailId, AppConstants.APIKEY_GENERATED, apiKey);
				} else {
					Log.error("Email id is empty to send API key");
				}
			return prepareResponse.prepareSuccessMessage(AppConstants.SUCCESS_STATUS);

		} catch (Exception e) {
			Log.error("createApiKey -" + e);
		}
		return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
	}

	/**
	 * Method to regenerate API Key
	 * 
	 * @author Dinesh Kumar
	 * @param req
	 * @return
	 */
	@Override
	public RestResponse<GenericResponse> reGenerateApiKey(ApiKeyReqModel req) {
		try {
			if (StringUtil.isNullOrEmpty(req.getSource()))
				return prepareResponse.prepareFailedResponse(AppConstants.INVALID_PARAMETER);

			ApiKeyEntity response = apiKeyDao.getAPIDetails(req.getUserId());

			if (response == null || StringUtil.isNullOrEmpty(response.getApi_key()))
				return prepareResponse.prepareFailedResponse(AppConstants.API_NOT_AVAILABLE);

			GetIntroSpectResponse userInfo = null;
			String emailId = "";
			if (HazelcastConfig.getInstance().getKeycloakUserInfoKB()
					.containsKey(req.getUserId() + "_" + req.getSource())) {
				userInfo = HazelcastConfig.getInstance().getKeycloakUserInfoKB()
						.get(req.getUserId() + "_" + req.getSource());
				emailId = userInfo.getEmail();
			}

			String apiKey = commonUtils.generatealpanumeric();
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			java.sql.Timestamp timestamp = new java.sql.Timestamp(Calendar.getInstance().getTimeInMillis());
			Calendar cal = Calendar.getInstance();
			cal.setTime(timestamp);
			cal.add(Calendar.DATE, 7);
			String exp = dateFormat.format(cal.getTime());

			String success = apiKeyDao.regenerateApiKey(req.getUserId(), apiKey, exp);
			if (success.equalsIgnoreCase(AppConstants.SUCCESS_STATUS)) {
				if (StringUtil.isNotNullOrEmpty(emailId)) {
					emailUtils.sendEmailAPIKey("User", emailId, AppConstants.APIKEY_GENERATED, apiKey);
				} else {
					Log.error("Email id is empty to send API key");
				}
				return prepareResponse.prepareSuccessMessage(AppConstants.SUCCESS_STATUS);
			}
		} catch (Exception e) {
			Log.error("reGenerateApiKey -" + e);
		}
		return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
	}
}
