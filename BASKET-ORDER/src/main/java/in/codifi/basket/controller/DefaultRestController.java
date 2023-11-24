/**
 * 
 */
package in.codifi.basket.controller;

import javax.inject.Inject;

import org.eclipse.microprofile.jwt.JsonWebToken;

import in.codifi.cache.model.ClinetInfoModel;

/**
 * @author mohup
 *
 */
public class DefaultRestController {
	private static final String USER_ID_KEY = "preferred_username";
	private static final String UCC = "ucc";
	private static final String EMAIL = "email";
	private static final String USER_FIRST_NAME = "given_name";
	private static final String USER_LAST_NAME = "family_name";
	private static final String REALM_ACCESS = "realm_access";
	/**
	 *      * Injection point for the ID Token issued by the OpenID Connect Provider
	 *     
	 */

	@Inject
	JsonWebToken idToken;

	@Inject
	JsonWebToken accessToken;

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
		model.setUserId(this.idToken.getClaim(USER_ID_KEY).toString().toUpperCase());
		if (this.idToken.containsClaim(UCC)) {
			model.setUcc(this.idToken.getClaim(UCC).toString());
		}
		return model;
	}

	public String getAcToken() {
		return this.accessToken.getRawToken();
	}
}
