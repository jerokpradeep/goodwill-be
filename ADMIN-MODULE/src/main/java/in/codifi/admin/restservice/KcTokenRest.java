package in.codifi.admin.restservice;

import java.util.Date;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.WebApplicationException;

import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.resteasy.reactive.ClientWebApplicationException;

import in.codifi.admin.config.KeyCloakConfig;
import in.codifi.admin.model.response.KcUserDetailsRequest;
import in.codifi.admin.utility.AppConstants;
import in.codifi.admin.ws.model.kc.GetTokenResponse;
import in.codifi.admin.ws.service.kc.KcTokenRestSpec;
import io.quarkus.logging.Log;

@ApplicationScoped
public class KcTokenRest {

	@Inject
	@RestClient
	KcTokenRestSpec tokenService;

	@Inject
	KeyCloakConfig props;

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
	public GetTokenResponse getToken(KcUserDetailsRequest authmodel) throws ClientWebApplicationException {
		GetTokenResponse tokenDetail = null;
		String clientId = props.getClientId();
		String clientSecret = props.getClientSecret();
		String grantType = props.getGrantType();
		String userId = authmodel.getUserId();
		String password = authmodel.getPassword();

		tokenDetail = tokenService.fetchToken(clientId, clientSecret, grantType, userId, password);

		return tokenDetail;
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
		GetTokenResponse tokenDetail = null;
		String clientId = props.getAdminClientId();
		String clientSecret = props.getAdminSecret();
		String grantType = props.getAdminGrantType();
		tokenDetail = tokenService.fetchAdminToken(clientId, clientSecret, grantType);
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
		String token = null;
		String clientId = props.getAdminClientId();
		String clientSecret = props.getAdminSecret();
		String grantType = props.getAdminGrantType();
		System.out.println("Get admin token " + new Date());
		try {
			token = tokenService.fetchAdminToken(clientId, clientSecret, grantType).getAccessToken();
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
//	public GetIntroSpectResponse getIntroSpect(AuthReq authmodel, String token) throws ClientWebApplicationException {
//		GetIntroSpectResponse introSpectResponse = null;
//		String userId = authmodel.getUserId();
//		String clientId = props.getClientId();
//		String clientSecret = props.getClientSecret();
//		introSpectResponse = tokenService.getIntroSpect(userId, clientId, clientSecret, token);
//		return introSpectResponse;
//	}

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
		tokenService.logout(token, clientId, clientSecret, refreshToken, userId);
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
		String grantType = props.getGrantType();
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
