/**
 * 
 */
package in.codifi.funds.controller;

import javax.inject.Inject;

import org.eclipse.microprofile.jwt.JsonWebToken;

import in.codifi.cache.model.ClinetInfoModel;

/**
 * @author mohup
 *
 */
public class DefaultRestController {
	private static final String USER_ID_KEY = "preferred_username";
	private static final String EMAIL = "email";
	private static final String UCC = "ucc";
	private static final String USER_FIRST_NAME = "given_name";
	private static final String USER_LAST_NAME = "family_name";
	private static final String CLIENT_ROLE = "clientRole";
	/**
	 *      * Injection point for the ID Token issued by the OpenID Connect Provider
	 *     
	 */

	@Inject
	JsonWebToken idToken;

	public String getUserId() {
		return this.idToken.getClaim(USER_ID_KEY).toString().toUpperCase();
	}

	/**
	 * 
	 * Method to get client details
	 * 
	 * @author Dinesh Kumar
	 *
	 * @return
	 */
	public ClinetInfoModel clientInfo() {
		ClinetInfoModel model = new ClinetInfoModel();
//		if (this.idToken.containsClaim(CLIENT_ROLE)) {
//			List<String> clientRoles = this.idToken.getClaim(CLIENT_ROLE);
//			List<String> roles = new ArrayList<>();
//			JsonStringImpl s = null;
//			for (int i = 0; i < clientRoles.size(); i++) {
//				roles.a
//			}
//		
//			model.setClientRoles(clientRoles);
//		}
		if (this.idToken.containsClaim(UCC)) {
			model.setUcc(this.idToken.getClaim(UCC).toString());
		}
		model.setUserId(this.idToken.getClaim(USER_ID_KEY).toString().toUpperCase());
		return model;
	}
}
