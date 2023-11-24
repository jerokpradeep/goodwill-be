package in.codifi.admin.service.spec;

import java.util.List;

import org.keycloak.representations.idm.UserRepresentation;

import in.codifi.admin.model.request.KeycloakReqModel;

public interface KeycloakServiceSpec {

	/**
	 * method to get user based records
	 * 
	 * @author LOKESH
	 * @return
	 */
	List<UserRepresentation> listUsers(KeycloakReqModel keycloakModel);
}
