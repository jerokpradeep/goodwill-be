package in.codifi.common.controller;

import javax.inject.Inject;

import org.eclipse.microprofile.jwt.JsonWebToken;

import in.codifi.cache.model.ClinetInfoModel;

public class DefaultRestController {

	private static final String USER_ID_KEY = "preferred_username";
	private static final String UCC = "ucc";
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
		model.setUserId(this.idToken.getClaim(USER_ID_KEY).toString());
		if (this.idToken.containsClaim(UCC)) {
			model.setUcc(this.idToken.getClaim(UCC).toString());
		}
		return model;
	}

}
