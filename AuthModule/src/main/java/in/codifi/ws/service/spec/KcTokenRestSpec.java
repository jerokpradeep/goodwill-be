package in.codifi.ws.service.spec;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;

import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.rest.client.annotation.RegisterClientHeaders;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import in.codifi.ws.model.kc.GetIntroSpectResponse;
import in.codifi.ws.model.kc.GetTokenResponse;

@RegisterRestClient(configKey = "token-service")
@RegisterClientHeaders
public interface KcTokenRestSpec {

	/**
	 * 
	 * Method to get token from key cloak
	 * 
	 * @author Dinesh Kumar
	 *
	 * @param client_id
	 * @param client_secret
	 * @param grant_type
	 * @param userId
	 * @param password
	 * @return
	 */
	@Path("/token")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@APIResponse(description = "Fetches access token for user creation")
	public GetTokenResponse fetchToken(@FormParam("client_id") String client_id,
			@FormParam("client_secret") String client_secret, @FormParam("grant_type") String grant_type,
			@FormParam("username") String userId, @FormParam("password") String password);

	@Path("/token")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@APIResponse(description = "Fetches access token for user creation")
	public GetTokenResponse fetchAdminToken(@FormParam("client_id") String client_id,
			@FormParam("client_secret") String client_secret, @FormParam("grant_type") String grant_type);

	/**
	 * 
	 * Method to get IntroSpect
	 * 
	 * @author Dinesh Kumar
	 *
	 * @param userId
	 * @param clientId
	 * @param clientSecret
	 * @param token
	 * @return
	 */
	@Path("/token/introspect")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@APIResponse(description = "Fetches Intro Spect")
	public GetIntroSpectResponse getIntroSpect(@FormParam("username") String userId,
			@FormParam("client_id") String clientId, @FormParam("client_secret") String clientSecret,
			@FormParam("token") String token);

	/**
	 * 
	 * Method to logout
	 * 
	 * @author Dinesh Kumar
	 *
	 * @param authHeader
	 * @param clientId
	 * @param clientSecret
	 * @param token
	 * @param userId
	 */
	@Path("/logout")
	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@APIResponse(description = "Logout")
	public void logout(@HeaderParam(HttpHeaders.AUTHORIZATION) String authHeader,
			@FormParam("client_id") String clientId, @FormParam("client_secret") String clientSecret,
			@FormParam("refresh_token") String token, @FormParam("user_id") String userId);

	/**
	 * Method to get new access token by refresh token
	 * 
	 * @author Dinesh Kumar
	 * 
	 * @param client_id
	 * @param client_secret
	 * @param grant_type
	 * @param token
	 * @return
	 */
	@Path("/token")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@APIResponse(description = "Fetches access token for user creation")
	public GetTokenResponse fetchTokenByRefereshToken(@FormParam("client_id") String client_id,
			@FormParam("client_secret") String client_secret, @FormParam("grant_type") String grant_type,
			@FormParam("refresh_token") String token);
}
