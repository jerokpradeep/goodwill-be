package in.codifi.admin.ws.service.kc;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.resteasy.reactive.ClientWebApplicationException;

import in.codifi.admin.config.HazelcastConfig;
import in.codifi.admin.config.KeyCloakConfig;
import in.codifi.admin.ws.model.kc.GetTokenResponse;

@ApplicationScoped
public class KeyCloakTokenRestService {

	@Inject
	@RestClient
	IKeyCloakTokenRestService tokenService;
	@Inject
	KeyCloakConfig props;

	/**
	 * Method to get admin token
	 * 
	 * @author Dinesh Kumar
	 * @return
	 * @throws ClientWebApplicationException
	 */

	public GetTokenResponse getAdminToken() throws ClientWebApplicationException {
		GetTokenResponse tokenDetail = null;
		String clientId = props.getAdminClientId();
		String clientSecret = props.getAdminSecret();
		String grantType = props.getAdminGrantType();
		tokenDetail = tokenService.fetchAdminToken(clientId, clientSecret, grantType);
		return tokenDetail;
	}

	/**
	 * Method to get access token for admin
	 * 
	 * @author Dinesh Kumar
	 * @return
	 * @throws ClientWebApplicationException
	 */
	public String getAdminAccessToken() throws ClientWebApplicationException {
		String token = null;
		String clientId = props.getAdminClientId();
		String clientSecret = props.getAdminSecret();
		String grantType = props.getAdminGrantType();
		try {
			token = tokenService.fetchAdminToken(clientId, clientSecret, grantType).getAccessToken();
			HazelcastConfig.getInstance().getKeycloakAdminSession().put(clientId, token);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return token;
	}
}
