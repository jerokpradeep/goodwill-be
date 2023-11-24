package in.codifi.admin.restservice;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.WebApplicationException;

import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.resteasy.reactive.ClientWebApplicationException;

import com.esotericsoftware.minlog.Log;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import in.codifi.admin.config.HazelcastConfig;
import in.codifi.admin.config.KeyCloakConfig;
import in.codifi.admin.req.model.UserRoleMapReqModel;
import in.codifi.admin.utility.AppConstants;
import in.codifi.admin.utility.KcConstants;
import in.codifi.admin.utility.StringUtil;
import in.codifi.admin.ws.model.kc.CreateUserRequestModel;
import in.codifi.admin.ws.model.kc.GetUserInfoResp;
import in.codifi.admin.ws.service.spec.KcAdminRestSpec;

@ApplicationScoped
public class KcAdminRest {

	@Inject
	@RestClient
	KcAdminRestSpec kcAdminRestSpec;

	@Inject
	KeyCloakConfig props;

	@Inject
	KcTokenRest kcTokenRest;

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
		try {

			String token = "Bearer " + getAccessToken();
			response = kcAdminRestSpec.getUserInfo(token, userName.toLowerCase(), KcConstants.EXACT_TRUE);
		} catch (ClientWebApplicationException e) {
			int statusCode = e.getResponse().getStatus();
			if (statusCode == 401)
				HazelcastConfig.getInstance().getKeycloakAdminSession().clear();
			e.printStackTrace();
		}
		return response;
	}

	private String getAccessToken() {

		if (HazelcastConfig.getInstance().getKeycloakAdminSession().containsKey(props.getAdminClientId())) {
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
		try {
			String token = "Bearer " + getAccessToken();
			String request = key + ":" + value;
			response = kcAdminRestSpec.getUserInfoByAttribute(token, request);
		} catch (ClientWebApplicationException e) {
			int statusCode = e.getResponse().getStatus();
			if (statusCode == 401)
				HazelcastConfig.getInstance().getKeycloakAdminSession().clear();
			e.printStackTrace();
		}
		return response;
	}

	public List<GetUserInfoResp> getKcAllUserDetails(String min, String max) throws ClientWebApplicationException {
		List<GetUserInfoResp> response = new ArrayList<>();
		try {
			String token = "Bearer " + getAccessToken();
			response = kcAdminRestSpec.getAllUserInfoByAttribute(token, min, max);
		} catch (ClientWebApplicationException e) {
			int statusCode = e.getResponse().getStatus();
			if (statusCode == 401)
				HazelcastConfig.getInstance().getKeycloakAdminSession().clear();
			e.printStackTrace();
		}
		return response;
	}

	/**
	 * Method to get user count in keyclock
	 * 
	 * @author Gowthaman
	 * @return
	 */
	public int getCount() {
		int count = 0;
		try {
			String token = "Bearer " + getAccessToken();
			count = kcAdminRestSpec.getCount(token);
		} catch (Exception e) {
			e.printStackTrace();
			Log.error(e.getMessage());
		}
		return count;
	}

	/**
	 * Method to update Kc User Details
	 * 
	 * @author Gowthaman
	 * @param req
	 * @return
	 */
	public String updateKcUserDetails(CreateUserRequestModel user) {
		String response = "";
		String id = "";
		try {
			String token = "Bearer " + getAccessToken();
			id = getKeycloakIdFromKc(user.getUsername());
			kcAdminRestSpec.updateUser(token, id, user);
			response = AppConstants.USER_UPDATED;
		} catch (ClientWebApplicationException e) {
			int statusCode = e.getResponse().getStatus();
			if (statusCode == 401)
				HazelcastConfig.getInstance().getKeycloakAdminSession().clear();
			e.printStackTrace();
			response = AppConstants.USER_NOT_UPDATED;
		}
		return response;
	}
	
	/**
	 * Method to get KeycloakIdFromKc
	 * @param userId
	 * @return
	 */
	private String getKeycloakIdFromKc(String userId) {
		String id = "";
		try {
			List<GetUserInfoResp> infoResps = new ArrayList<>();
			infoResps = getUserInfo(userId);
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
	 * Method to add New User
	 * 
	 * @author Gowthaman
	 * @param user
	 * @return
	 */
	public String addNewUser(CreateUserRequestModel user) {
		String message = "User Created";
		int count = 1;
		try {
			String token = "Bearer " + getAccessToken();
			kcAdminRestSpec.addNewUser(token, user);
		} catch (WebApplicationException e) {
			int statusCode = e.getResponse().getStatus();
			if (statusCode == 401 && count > 0) {
				count--;
				HazelcastConfig.getInstance().getKeycloakAdminSession().clear();
				addNewUser(user);
			} else if (statusCode == 409) {
				return "User already exists";
			} else if (count == 0) {
				message = "";
			} else {
				e.printStackTrace();
				message = e.getMessage();
			}
		}
		return message;
	}

	/**
	 * Method to user Role Mapping
	 * 
	 * @author Gowthaman
	 * @param user
	 * @param userId
	 * @param clientId
	 * @return
	 * @throws ClientWebApplicationException
	 */
	public String userRoleMapping(List<UserRoleMapReqModel> user, String userId, String clientId)
			throws ClientWebApplicationException {
		String message = "Roles Mapped";
		String id = "";
		int count = 1;
		try {
			String token = "Bearer " + getAccessToken();
			id = getKeycloakIdFromKc(userId);
			if (StringUtil.isNullOrEmpty(id)) {
				Log.error("Failed to get kc token for user - " + userId);
				return AppConstants.FAILED_STATUS;
			}
			try {
				ObjectMapper mapper = new ObjectMapper();
				System.out.println(mapper.writeValueAsString(user));
				System.out.println("userid - " + id + "clientId - " + clientId);
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}

			kcAdminRestSpec.userRoleMapping(token, id, clientId, user);
		} catch (ClientWebApplicationException e) {
			int statusCode = e.getResponse().getStatus();
			if (statusCode == 401 && count > 0) {
				count--;
				HazelcastConfig.getInstance().getKeycloakAdminSession().clear();
				userRoleMapping(user, id, clientId);
			} else {
				e.printStackTrace();
				message = e.getMessage();
			}
		}
		return message;
	}
}
