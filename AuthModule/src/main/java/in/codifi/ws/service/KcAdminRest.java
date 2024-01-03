package in.codifi.ws.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.core.Response;

import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.resteasy.reactive.ClientWebApplicationException;

import com.fasterxml.jackson.databind.ObjectMapper;

import in.codifi.auth.config.HazelcastConfig;
import in.codifi.auth.config.KeyCloakConfig;
import in.codifi.auth.entity.logs.RestAccessLogModel;
import in.codifi.auth.model.request.AuthReq;
import in.codifi.auth.repository.AccessLogManager;
import in.codifi.auth.utility.AppConstants;
import in.codifi.auth.utility.StringUtil;
import in.codifi.ws.model.kc.BlockAndUnblockUserReq;
import in.codifi.ws.model.kc.GetUserInfoResp;
import in.codifi.ws.model.kc.RestPasswordErrorResp;
import in.codifi.ws.model.kc.RestPasswordReq;
import in.codifi.ws.service.spec.KcAdminRestSpec;
import io.quarkus.logging.Log;

@ApplicationScoped
public class KcAdminRest {

	@Inject
	@RestClient
	KcAdminRestSpec kcAdminRestSpec;

	@Inject
	KeyCloakConfig props;

	@Inject
	KcTokenRest kcTokenRest;

	@Inject
	AccessLogManager accessLogManager;

	/**
	 * 
	 * Method to get id by userId
	 * 
	 * @author Dinesh Kumar
	 *
	 * @param userId
	 * @return
	 */
	private String getKeycloakId(String userId) {
		String id = "";
		try {
			List<GetUserInfoResp> infoResps = new ArrayList<>();
			if (HazelcastConfig.getInstance().getKeycloakUserDetails().containsKey(userId)) {
				infoResps = HazelcastConfig.getInstance().getKeycloakUserDetails().get(userId);
			} else {
				infoResps = getUserInfo(userId);
			}
			for (GetUserInfoResp getUserInfoResp : infoResps) {
				if (getUserInfoResp.getUsername().equalsIgnoreCase(userId)) {
					return getUserInfoResp.getId();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return id;
	}

	/**
	 * 
	 * Method to get user by userName to check whether user exist or not
	 * 
	 * @author Dinesh Kumar
	 *
	 * @param userName
	 * @return
	 * @throws ClientWebApplicationException
	 */
	public List<GetUserInfoResp> getUserInfo(String userName) throws ClientWebApplicationException {
		List<GetUserInfoResp> response = new ArrayList<>();
		RestAccessLogModel accessLogModel = new RestAccessLogModel();
		try {
			String token = "Bearer " + getAccessToken();
			accessLogModel.setInTime(new Timestamp(new Date().getTime()));
			response = kcAdminRestSpec.getUserInfo(token, userName.toLowerCase());
			accessLogModel.setOutTime(new Timestamp(new Date().getTime()));
			accessLogModel.setMethod("getUserInfo");
			accessLogModel.setModule(AppConstants.MODULE);
			accessLogModel.setReqBody(userName);
			accessLogModel.setUserId(userName);
			accessLogModel.setResBody(response.toString());

		} catch (ClientWebApplicationException e) {
			int statusCode = e.getResponse().getStatus();
			if (statusCode == 401)
				HazelcastConfig.getInstance().getKeycloakAdminSession().clear();
			e.printStackTrace();
			accessLogModel.setOutTime(new Timestamp(new Date().getTime()));
		} finally {
			insertRestAccessLogs(accessLogModel);
		}
		return response;
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
					accessLogManager.insert24RestAccessLog(accessLogModel);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		pool.shutdown();
	}

	private String getAccessToken() {
		if (HazelcastConfig.getInstance().getKeycloakAdminSession().containsKey(props.getAdminClientId())) {
			System.out.println();
			return HazelcastConfig.getInstance().getKeycloakAdminSession().get(props.getAdminClientId());
		}
		return kcTokenRest.getAdminAccessToken();
	}

	/**
	 * 
	 * Method to get user by attribute to check whether user exist or not
	 * 
	 * @author Dinesh Kumar
	 *
	 * @param key
	 * @param value
	 * @return
	 * @throws ClientWebApplicationException
	 */
	public List<GetUserInfoResp> getUserInfoByAttribute(String key, String value) throws ClientWebApplicationException {
		List<GetUserInfoResp> response = new ArrayList<>();
		System.out.println("getUserInfoByAttribute");
		Log.error(key);
		
		RestAccessLogModel accessLogModel = new RestAccessLogModel();
		try {
			String token = "Bearer " + getAccessToken();
			System.out.println("token");
			Log.error(token);
			String request = key + ":" + value;
			System.out.println("request:" + request);
			accessLogModel.setInTime(new Timestamp(new Date().getTime()));
			response = kcAdminRestSpec.getUserInfoByAttribute(token, request);
			System.out.println("getUserInfoByAttribute");
			Log.error(response);
			accessLogModel.setOutTime(new Timestamp(new Date().getTime()));
			accessLogModel.setMethod("getUserInfoByAttribute");
			accessLogModel.setModule(AppConstants.MODULE);
			accessLogModel.setReqBody(request);
			accessLogModel.setUserId(token);
			accessLogModel.setResBody(response.toString());
		} catch (ClientWebApplicationException e) {
			int statusCode = e.getResponse().getStatus();
			accessLogModel.setOutTime(new Timestamp(new Date().getTime()));
			if (statusCode == 401)
				HazelcastConfig.getInstance().getKeycloakAdminSession().clear();
			e.printStackTrace();
		} finally {
			insertRestAccessLogs(accessLogModel);
		}
		return response;
	}

	/**
	 * 
	 * Method to reset password
	 * 
	 * @author Dinesh Kumar
	 *
	 * @param authReq
	 * @return
	 * @throws ClientWebApplicationException
	 */
	public String resetPassword(AuthReq authReq) throws ClientWebApplicationException {
		String response = AppConstants.FAILED_STATUS;
		RestAccessLogModel accessLogModel = new RestAccessLogModel();
//		try {
		String token = "Bearer " + getAccessToken();
		RestPasswordReq request = new RestPasswordReq();
		request.setType("password");
		request.setValue(authReq.getPassword());
		String id = getKeycloakId(authReq.getUserId());
		if (StringUtil.isNullOrEmpty(id))
			return AppConstants.FAILED_STATUS;
		accessLogModel.setInTime(new Timestamp(new Date().getTime()));
		kcAdminRestSpec.resetPassword(token, id, request);
		accessLogModel.setOutTime(new Timestamp(new Date().getTime()));
		accessLogModel.setMethod("resetPassword");
		accessLogModel.setModule(AppConstants.MODULE);
		accessLogModel.setReqBody(request.toString());
		accessLogModel.setUserId(authReq.getUserId());
		insertRestAccessLogs(accessLogModel);
		return AppConstants.SUCCESS_STATUS;
//		} catch (ClientWebApplicationException e) {
//			int statusCode = e.getResponse().getStatus();
//			if (statusCode == 401)
//				HazelcastConfig.getInstance().getKeycloakAdminSession().clear();
//			e.printStackTrace();
//		}
//		return response;
	}

	/**
	 * 
	 * Method to unblock account
	 * 
	 * @author Dinesh Kumar
	 *
	 * @param userId
	 * @return
	 * @throws ClientWebApplicationException
	 */
	public String unblockAccount(String userId) throws ClientWebApplicationException {
		String response = AppConstants.FAILED_STATUS;
		RestAccessLogModel accessLogModel = new RestAccessLogModel();
		try {
			String token = "Bearer " + getAccessToken();
			BlockAndUnblockUserReq request = new BlockAndUnblockUserReq();
			request.setEnabled(true);
			String id = getKeycloakId(userId);
			if (StringUtil.isNullOrEmpty(id))
				return AppConstants.FAILED_STATUS;
			accessLogModel.setInTime(new Timestamp(new Date().getTime()));
			kcAdminRestSpec.blockAndUnblock(token, id, request);
			accessLogModel.setOutTime(new Timestamp(new Date().getTime()));
			accessLogModel.setMethod("unblockAccount");
			accessLogModel.setModule(AppConstants.MODULE);
			accessLogModel.setReqBody(request.toString());
			accessLogModel.setUserId(userId);
			return AppConstants.SUCCESS_STATUS;
		} catch (ClientWebApplicationException e) {
			int statusCode = e.getResponse().getStatus();
			if (statusCode == 401)
				HazelcastConfig.getInstance().getKeycloakAdminSession().clear();
			e.printStackTrace();
		} finally {
			insertRestAccessLogs(accessLogModel);
		}
		return response;
	}

	/**
	 * Method to block user
	 * 
	 * @author DINESH KUMAR
	 *
	 * @param userId
	 * @return
	 * @throws ClientWebApplicationException
	 */
	public String blockAccount(String userId) throws ClientWebApplicationException {
		String response = AppConstants.FAILED_STATUS;
		RestAccessLogModel accessLogModel = new RestAccessLogModel();
		try {
			String token = "Bearer " + getAccessToken();
			BlockAndUnblockUserReq request = new BlockAndUnblockUserReq();
			request.setEnabled(false);
			String id = getKeycloakId(userId);
			if (StringUtil.isNullOrEmpty(id))
				return AppConstants.FAILED_STATUS;
			accessLogModel.setInTime(new Timestamp(new Date().getTime()));
			kcAdminRestSpec.blockAndUnblock(token, id, request);
			accessLogModel.setOutTime(new Timestamp(new Date().getTime()));
			accessLogModel.setMethod("blockAccount");
			accessLogModel.setModule(AppConstants.MODULE);
			accessLogModel.setReqBody(request.toString());
			accessLogModel.setUserId(userId);
			return AppConstants.SUCCESS_STATUS;
		} catch (ClientWebApplicationException e) {
			int statusCode = e.getResponse().getStatus();
			if (statusCode == 401)
				HazelcastConfig.getInstance().getKeycloakAdminSession().clear();
			accessLogModel.setOutTime(new Timestamp(new Date().getTime()));
			e.printStackTrace();
		} finally {
			insertRestAccessLogs(accessLogModel);
		}
		return response;
	}

	/**
	 * 
	 * Method to change password
	 * 
	 * @author Dinesh Kumar
	 *
	 * @param authReq
	 * @return
	 * @throws ClientWebApplicationException
	 */
	public String changePassword(AuthReq authReq) {
		RestAccessLogModel accessLogModel = new RestAccessLogModel();
		try {
			String token = "Bearer " + getAccessToken();
			RestPasswordReq request = new RestPasswordReq();
			request.setType("password");
			request.setValue(authReq.getNewPassword());
			String id = getKeycloakId(authReq.getUserId());
			if (StringUtil.isNullOrEmpty(id))
				return AppConstants.FAILED_STATUS;
			accessLogModel.setInTime(new Timestamp(new Date().getTime()));
			kcAdminRestSpec.resetPassword(token, id, request);
			accessLogModel.setOutTime(new Timestamp(new Date().getTime()));
			accessLogModel.setMethod("resetPassword");
			accessLogModel.setModule(AppConstants.MODULE);
			accessLogModel.setReqBody(request.toString());
			accessLogModel.setUserId(authReq.getUserId());
			return AppConstants.SUCCESS_STATUS;
		} catch (ClientWebApplicationException e) {
			ObjectMapper mapper = new ObjectMapper();
			Response response = e.getResponse();
			if (response.getStatus() == 401) {
				HazelcastConfig.getInstance().getKeycloakAdminSession().clear();
				accessLogModel.setOutTime(new Timestamp(new Date().getTime()));
			} else if (response.getStatus() == 400) {
				RestPasswordErrorResp errorResp = response.readEntity(RestPasswordErrorResp.class);
				accessLogModel.setOutTime(new Timestamp(new Date().getTime()));
				return errorResp.getErrorDescription();
			}
			e.printStackTrace();
		} finally {
			insertRestAccessLogs(accessLogModel);
		}
		return AppConstants.FAILED_STATUS;
	}
}
