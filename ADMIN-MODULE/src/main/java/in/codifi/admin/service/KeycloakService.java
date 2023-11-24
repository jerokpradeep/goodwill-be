package in.codifi.admin.service;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;

import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.UserRepresentation;

import in.codifi.admin.model.request.KeycloakReqModel;
import in.codifi.admin.service.spec.KeycloakServiceSpec;

@ApplicationScoped
public class KeycloakService implements KeycloakServiceSpec {

	/**
	 * method to get user based records
	 * 
	 * @author LOKESH
	 * @return
	 */
	public List<UserRepresentation> listUsers(KeycloakReqModel keycloakModel) {

		String serverUrl = "https://keycloak.cholasecurities.com/idaas/";
		String realmName = "amvuet";
		String clientId = "chola";
		String clientSecret = "0vudsOThFapcU545ZyZTu1iTNPIFpIzu";

		// Initialize the Keycloak admin client
		Keycloak keycloak = KeycloakBuilder.builder().serverUrl(serverUrl).realm(realmName).clientId(clientId)
				.clientSecret(clientSecret)
				.username(keycloakModel.getUserName())
				.password(keycloakModel.getPassword())
				.build();

		// Get the realm resource
		RealmResource realmResource = keycloak.realm(realmName);

		// Get the UsersResource by calling users() on the realm resource
		UsersResource usersResource = realmResource.users();

		// Retrieve a list of user representations
		List<UserRepresentation> users = usersResource.list();

	
		// Close the Keycloak admin client when done
		keycloak.close();

		return users;
	}
}
