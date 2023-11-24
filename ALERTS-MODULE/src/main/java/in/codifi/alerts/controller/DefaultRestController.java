package in.codifi.alerts.controller;

import javax.inject.Inject;

import org.eclipse.microprofile.jwt.JsonWebToken;

import in.codifi.cache.model.ClinetInfoModel;

/**
 * @author mohup
 *
 */
@SuppressWarnings("unused")
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

	public String getUserId() {
		return this.idToken.getClaim(USER_ID_KEY).toString();
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
			model.setUcc(this.idToken.getClaim(UCC).toString().toUpperCase());
		}

		if (this.idToken.containsClaim(USER_FIRST_NAME)) {
			String name = this.idToken.getClaim(USER_FIRST_NAME).toString();
			if (this.idToken.containsClaim(USER_LAST_NAME)) {
				name = name + " " + this.idToken.getClaim(USER_LAST_NAME).toString();
			}
			model.setName(name);
		}
		if (this.idToken.containsClaim(EMAIL)) {
			model.setEmail(this.idToken.getClaim(EMAIL).toString());
		}
		return model;
	}
}
