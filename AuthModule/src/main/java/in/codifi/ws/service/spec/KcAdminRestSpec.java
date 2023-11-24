package in.codifi.ws.service.spec;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;

import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.rest.client.annotation.RegisterClientHeaders;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import in.codifi.ws.model.kc.BlockAndUnblockUserReq;
import in.codifi.ws.model.kc.GetUserInfoResp;
import in.codifi.ws.model.kc.RestPasswordReq;

@RegisterRestClient(configKey = "auth-user-api")
@RegisterClientHeaders
public interface KcAdminRestSpec {

	@Path("/users/")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@APIResponse(description = "To get user info to check whether user exist or not")
	public List<GetUserInfoResp> getUserInfo(@HeaderParam(HttpHeaders.AUTHORIZATION) String authHeader,
			@QueryParam(value = "username") String username);

	@Path("/users/")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@APIResponse(description = "To get user info by attribute to check whether user exist or not")
	public List<GetUserInfoResp> getUserInfoByAttribute(@HeaderParam(HttpHeaders.AUTHORIZATION) String authHeader,
			@QueryParam(value = "q") String request);

	@Path("/users/{userId}/reset-password")
	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@APIResponse(description = "Reset a password for a user")
	public void resetPassword(@HeaderParam(HttpHeaders.AUTHORIZATION) String authHeader,
			@PathParam("userId") String userId, RestPasswordReq restPasswordReq);

	@Path("/users/{userId}")
	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@APIResponse(description = "To get user info")
	public List<GetUserInfoResp> blockAndUnblock(@HeaderParam(HttpHeaders.AUTHORIZATION) String authHeader,
			@PathParam("userId") String userId, BlockAndUnblockUserReq unblockUserReq);
}
