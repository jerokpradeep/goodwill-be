package in.codifi.admin.ws.service.spec;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
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

import in.codifi.admin.req.model.UserRoleMapReqModel;
import in.codifi.admin.ws.model.kc.CreateUserRequestModel;
import in.codifi.admin.ws.model.kc.GetUserInfoResp;

@RegisterRestClient(configKey = "auth-user-api")
@RegisterClientHeaders
public interface KcAdminRestSpec {

	@Path("/users/")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@APIResponse(description = "To get user info to check whether user exist or not")
	public List<GetUserInfoResp> getUserInfo(@HeaderParam(HttpHeaders.AUTHORIZATION) String authHeader,
			@QueryParam(value = "username") String username, @QueryParam(value = "exact") String exact);

	@Path("/users/")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@APIResponse(description = "To get user info by attribute to check whether user exist or not")
	public List<GetUserInfoResp> getUserInfoByAttribute(@HeaderParam(HttpHeaders.AUTHORIZATION) String authHeader,
			@QueryParam(value = "q") String request);
	
	@Path("/users/")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@APIResponse(description = "To get user info by attribute to check whether user exist or not")
	public List<GetUserInfoResp> getAllUserInfoByAttribute(@HeaderParam(HttpHeaders.AUTHORIZATION) String authHeader,
			@QueryParam(value = "first") String min, @QueryParam(value = "max") String max);

	@Path("/users/count")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@APIResponse(description = "To get user info by attribute to check whether user exist or not")
	public int getCount(@HeaderParam(HttpHeaders.AUTHORIZATION) String authHeader);
	
	@Path("/users/{userId}")
	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@APIResponse(description = "Create a new user after phone, email and mpin registration")
	public void updateUser(@HeaderParam(HttpHeaders.AUTHORIZATION) String authHeader,
			@PathParam("userId") String userId, CreateUserRequestModel user);
	
	@Path("/users")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@APIResponse(description = "Create a new user after phone, email and mpin registration")
	public void addNewUser(@HeaderParam(HttpHeaders.AUTHORIZATION) String authHeader, CreateUserRequestModel user);

	@Path("/users/{userId}/role-mappings/clients/{clientId}")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public void userRoleMapping(@HeaderParam(HttpHeaders.AUTHORIZATION) String authHeader,
			@PathParam("userId") String userId, @PathParam("clientId") String clientId, List<UserRoleMapReqModel> user);

}
