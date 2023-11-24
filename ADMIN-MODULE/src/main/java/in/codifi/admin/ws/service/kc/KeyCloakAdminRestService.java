package in.codifi.admin.ws.service.kc;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.WebApplicationException;

import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.resteasy.reactive.ClientWebApplicationException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import in.codifi.admin.config.HazelcastConfig;
import in.codifi.admin.config.KeyCloakConfig;
import in.codifi.admin.req.model.UserRoleMapReqModel;
import in.codifi.admin.utility.AppConstants;
import in.codifi.admin.utility.StringUtil;
import in.codifi.admin.ws.model.kc.CreateUserRequestModel;
import io.quarkus.logging.Log;

@ApplicationScoped
public class KeyCloakAdminRestService {

	@Inject
	@RestClient
	IKeyCloakAdminRestService iKeyCloakAdminRestService;
	@Inject
	KeyCloakConfig props;
	@Inject
	KeyCloakTokenRestService cloakTokenRestService;

	/**
	 * User Creation in Keycloak
	 * 
	 * @param user
	 * @return
	 * @throws ClientWebApplicationException
	 */
	public String addNewUser(CreateUserRequestModel user) {
		String message = "User Created";
		int count = 1;
		try {
			String token = "Bearer " + getAccessToken();
			iKeyCloakAdminRestService.addNewUser(token, user);
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

	private String getAccessToken() {
		if (HazelcastConfig.getInstance().getKeycloakAdminSession().containsKey(props.getAdminClientId())) {
			return HazelcastConfig.getInstance().getKeycloakAdminSession().get(props.getAdminClientId());
		}
		return cloakTokenRestService.getAdminAccessToken();
	}

	/**
	 * method to role mapping for users
	 * 
	 * @author SOWMIYA
	 * @param mapReqModels
	 * @param userId
	 * @param clientAppVueId
	 * @return
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

			iKeyCloakAdminRestService.userRoleMapping(token, id, clientId, user);
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

	/**
	 * method to get key cloak id from kc
	 * 
	 * @author SOWMIYA
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
	 * method to get user info
	 * 
	 * @author SOWMIYA
	 * @param userId
	 * @return
	 */
	private List<GetUserInfoResp> getUserInfo(String userId) {
		List<GetUserInfoResp> response = new ArrayList<>();
		try {
			String token = "Bearer " + getAccessToken();
			response = iKeyCloakAdminRestService.getUserInfo(token, userId.toLowerCase());
		} catch (ClientWebApplicationException e) {
			int statusCode = e.getResponse().getStatus();
			if (statusCode == 401)
				HazelcastConfig.getInstance().getKeycloakAdminSession().clear();
			e.printStackTrace();
		}
		return response;
	}

}
