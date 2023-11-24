package in.codifi.admin.controller;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.Path;

import org.keycloak.representations.idm.UserRepresentation;

import in.codifi.admin.controller.spec.KeycloakControllerSpec;
import in.codifi.admin.model.request.KeycloakReqModel;
import in.codifi.admin.service.spec.KeycloakServiceSpec;

@Path("/get")
@ApplicationScoped
public class KeycloakController implements KeycloakControllerSpec {

	@Inject
	KeycloakServiceSpec service;

	/**
	 * method to get user based records
	 * 
	 * @author LOKESH
	 */
	@Override
	public List<UserRepresentation> listUsers(KeycloakReqModel keycloakModel) {
		return service.listUsers(keycloakModel);
	}
}
