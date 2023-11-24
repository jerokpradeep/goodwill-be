package in.codifi.admin.ws.service.kc;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.rest.client.annotation.RegisterClientHeaders;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import in.codifi.admin.ws.model.kc.GetTokenResponse;

@RegisterRestClient(configKey = "token-service")
@RegisterClientHeaders
public interface IKeyCloakTokenRestService {

	@Path("/token")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@APIResponse(description = "Fetches access token for user creation")
	public GetTokenResponse fetchAdminToken(@FormParam("client_id") String client_id,
			@FormParam("client_secret") String client_secret, @FormParam("grant_type") String grant_type);

}
