package in.codifi.admin.controller.spec;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.keycloak.representations.idm.UserRepresentation;

import in.codifi.admin.model.request.KeycloakReqModel;

@ApplicationScoped
public interface KeycloakControllerSpec {

	/**
	 * method to get user based records
	 * 
	 * @author LOKESH
	 * @return
	 */
	@Path("/keycloak")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public List<UserRepresentation> listUsers(KeycloakReqModel keycloakModel);

}
