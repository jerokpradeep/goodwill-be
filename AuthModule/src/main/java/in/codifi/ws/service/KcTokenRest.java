package in.codifi.ws.service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.WebApplicationException;

import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.resteasy.reactive.ClientWebApplicationException;

import in.codifi.auth.config.KeyCloakConfig;
import in.codifi.auth.entity.logs.RestAccessLogModel;
import in.codifi.auth.model.request.AuthReq;
import in.codifi.auth.repository.AccessLogManager;
import in.codifi.auth.utility.AppConstants;
import in.codifi.ws.model.kc.GetIntroSpectResponse;
import in.codifi.ws.model.kc.GetTokenResponse;
import in.codifi.ws.service.spec.KcTokenRestSpec;
import io.quarkus.logging.Log;

@ApplicationScoped
public class KcTokenRest {

	@Inject
	@RestClient
	KcTokenRestSpec tokenService;

	@Inject
	KeyCloakConfig props;

	@Inject
	KcAdminRest adminRest;

	@Inject
	AccessLogManager accessLogManager;

	/**
	 * 
	 * Method to get token from keycloak
	 * 
	 * @author Dinesh Kumar
	 *
	 * @param authmodel
	 * @return
	 * @throws ClientWebApplicationException
	 */
	public GetTokenResponse getToken(AuthReq authmodel) throws ClientWebApplicationException {
		RestAccessLogModel accessLogModel = new RestAccessLogModel();
		GetTokenResponse tokenDetail = null;
		String clientId = props.getClientId();
		String clientSecret = props.getClientSecret();
		String grantType = props.getGrantType();
		String userId = authmodel.getUserId();
		String password = AppConstants.KC_DEAFULT_PASSWORD;
		accessLogModel.setInTime(new Timestamp(new Date().getTime()));
		tokenDetail = tokenService.fetchToken(clientId, clientSecret, grantType, userId, password);
		accessLogModel.setOutTime(new Timestamp(new Date().getTime()));
		accessLogModel.setMethod("getToken");
		accessLogModel.setModule(AppConstants.MODULE);
		accessLogModel.setUserId(userId);
		accessLogModel.setResBody(tokenDetail.toString());
		insertRestAccessLogs(accessLogModel);
		return tokenDetail;
	}

	/**
	 * 
	 * Method to insert rest service access logs
	 * 
	 * @author Dinesh Kumar
	 *
	 * @param accessLogModel
	 */
	public void insertRestAccessLogs(RestAccessLogModel accessLogModel) {

		ExecutorService pool = Executors.newSingleThreadExecutor();
		pool.execute(new Runnable() {
			@Override
			public void run() {
				try {
					accessLogManager.insertRestAccessLog(accessLogModel);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		pool.shutdown();
	}

	/**
	 * 
	 * Method to get admin token
	 * 
	 * @author Dinesh Kumar
	 *
	 * @return
	 * @throws ClientWebApplicationException
	 */
	public GetTokenResponse getAdminToken() throws ClientWebApplicationException {
		RestAccessLogModel accessLogModel = new RestAccessLogModel();
		GetTokenResponse tokenDetail = null;
		String clientId = props.getAdminClientId();
		String clientSecret = props.getAdminSecret();
		String grantType = props.getAdminGrantType();
		accessLogModel.setInTime(new Timestamp(new Date().getTime()));
		tokenDetail = tokenService.fetchAdminToken(clientId, clientSecret, grantType);
		accessLogModel.setOutTime(new Timestamp(new Date().getTime()));
		accessLogModel.setMethod("getAdminToken");
		accessLogModel.setModule(AppConstants.MODULE);
		accessLogModel.setResBody(tokenDetail.toString());
		insertRestAccessLogs(accessLogModel);
		return tokenDetail;
	}

	/**
	 * 
	 * Method to get access token for admin
	 * 
	 * @author Dinesh Kumar
	 *
	 * @return
	 * @throws ClientWebApplicationException
	 */
	public String getAdminAccessToken() throws ClientWebApplicationException {
		RestAccessLogModel accessLogModel = new RestAccessLogModel();
		String token = null;
		String clientId = props.getAdminClientId();
		String clientSecret = props.getAdminSecret();
		String grantType = props.getAdminGrantType();
		try {
			accessLogModel.setInTime(new Timestamp(new Date().getTime()));
			token = tokenService.fetchAdminToken(clientId, clientSecret, grantType).getAccessToken();
			accessLogModel.setOutTime(new Timestamp(new Date().getTime()));
			accessLogModel.setMethod("getAdminAccessToken");
			accessLogModel.setModule(AppConstants.MODULE);
			accessLogModel.setResBody(token);
			insertRestAccessLogs(accessLogModel);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return token;
	}

	/**
	 * 
	 * Method to get Introspect for user
	 * 
	 * @author Dinesh Kumar
	 *
	 * @param authmodel
	 * @param token
	 * @return
	 * @throws ClientWebApplicationException
	 */
	public GetIntroSpectResponse getIntroSpect(AuthReq authmodel, String token) throws ClientWebApplicationException {
		RestAccessLogModel accessLogModel = new RestAccessLogModel();
		GetIntroSpectResponse introSpectResponse = null;
		String userId = authmodel.getUserId();
		String clientId = props.getClientId();
		String clientSecret = props.getClientSecret();
		accessLogModel.setInTime(new Timestamp(new Date().getTime()));
		introSpectResponse = tokenService.getIntroSpect(userId, clientId, clientSecret, token);
		accessLogModel.setOutTime(new Timestamp(new Date().getTime()));
		accessLogModel.setMethod("getIntroSpect");
		accessLogModel.setModule(AppConstants.MODULE);
		accessLogModel.setUserId(userId);
		accessLogModel.setResBody(introSpectResponse.toString());
		insertRestAccessLogs(accessLogModel);
		return introSpectResponse;
	}

	/**
	 * 
	 * Method to logout
	 * 
	 * @author Dinesh Kumar
	 *
	 * @param userId
	 * @param token
	 * @param refreshToken
	 * @throws ClientWebApplicationException
	 */
	public void logout(String userId, String token, String refreshToken) throws ClientWebApplicationException {
		String clientId = props.getClientId();
		String clientSecret = props.getClientSecret();
		RestAccessLogModel accessLogModel = new RestAccessLogModel();
		accessLogModel.setInTime(new Timestamp(new Date().getTime()));
		tokenService.logout(token, clientId, clientSecret, refreshToken, userId);
		accessLogModel.setOutTime(new Timestamp(new Date().getTime()));
		accessLogModel.setMethod("logout");
		accessLogModel.setModule(AppConstants.MODULE);
		accessLogModel.setUserId(userId);
		insertRestAccessLogs(accessLogModel);
	}

	/**
	 * method to get user Token
	 * 
	 * @author SowmiyaThangaraj
	 * @param authmodel
	 * @param retryCount
	 * @return
	 */
	public GetTokenResponse getUserToken(AuthReq authmodel, int retryCount) {
		GetTokenResponse tokenDetail = new GetTokenResponse();
		String clientId = props.getClientId();
		String clientSecret = props.getClientSecret();
		String grantType = props.getGrantType();
		String userId = authmodel.getUserId();
//		String password = authmodel.getPassword();
		String password = AppConstants.KC_DEAFULT_PASSWORD;
		try {
			tokenDetail = tokenService.fetchToken(clientId, clientSecret, grantType, userId, password);
		} catch (WebApplicationException ex) {
			retryCount = retryCount + 1;
			if (ex.getResponse().getStatus() == 401) {
				Log.error("KC-Login - " + AppConstants.INVALID_CREDENTIALS + "for user - " + authmodel.getUserId());
				if (retryCount < 2) {
					authmodel.setPassword(AppConstants.KC_DEAFULT_PASSWORD);
					adminRest.resetPassword(authmodel);
					Log.info("Password reseted sucessfully for - " + authmodel.getUserId());
					tokenDetail = this.getUserToken(authmodel, retryCount);
					return tokenDetail;
				}
			} else if (ex.getResponse().getStatus() == 400) {
				Log.error("KC-Login - " + AppConstants.USER_BLOCKED + "for user - " + authmodel.getUserId());
				if (retryCount < 2) {
					adminRest.unblockAccount(authmodel.getUserId());
					tokenDetail = this.getUserToken(authmodel, retryCount);
					return tokenDetail;
				}
			} else {
				Log.error("KC-Login failed - " + ex.getResponse().getStatus() + "for user - " + authmodel.getUserId());
			}
		}
		return tokenDetail;
	}

	/**
	 * Method to get access token by refresh token
	 * 
	 * @author dinesh
	 * @param refereshToken
	 * @return
	 * @throws ClientWebApplicationException
	 */
	public GetTokenResponse getUserTokenByRefereshToken(String refereshToken) {
		GetTokenResponse tokenDetail = new GetTokenResponse();
		String clientId = props.getClientId();
		String clientSecret = props.getClientSecret();
		String grantType = props.getGrantTypeRefreshToken();
		tokenDetail = tokenService.fetchTokenByRefereshToken(clientId, clientSecret, grantType, refereshToken);

		try {
			tokenDetail = tokenService.fetchTokenByRefereshToken(clientId, clientSecret, grantType, refereshToken);
		} catch (WebApplicationException ex) {

			if (ex.getResponse().getStatus() == 401) {
				Log.error("KC-Login By Referesh Token - " + AppConstants.INVALID_CREDENTIALS);

			} else if (ex.getResponse().getStatus() == 400) {
				Log.error("KC-Login By Referesh Token - " + AppConstants.USER_BLOCKED);
			} else {
				Log.error("KC-Login By Referesh Token failed - " + ex.getResponse().getStatus());
			}
		}
		return tokenDetail;
	}
}
