package in.codifi.auth.cache;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.jboss.resteasy.reactive.RestResponse;

import in.codifi.auth.config.HazelcastConfig;
import in.codifi.auth.model.response.GenericResponse;
import in.codifi.auth.servcie.AuthService;
import in.codifi.auth.utility.AppConstants;
import in.codifi.auth.utility.PrepareResponse;

@ApplicationScoped
public class CacheService {

	@Inject
	AuthService authService;
	@Inject
	PrepareResponse prepareResponse;

	/**
	 * 
	 * Method to clear cache
	 * 
	 * @author Dinesh Kumar
	 *
	 */
	public void clearCache() {
		HazelcastConfig.getInstance().getKeycloakAdminSession().clear();

		/** To clear Kambala rest session **/
		HazelcastConfig.getInstance().getRestUserSession().clear();
		HazelcastConfig.getInstance().getIsRestUserSessionActive().clear();
		HazelcastConfig.getInstance().getUserSessionDetails().clear();
	}

	/**
	 * Method to reload 2FA preference
	 * 
	 * @author DINESH KUMAR
	 *
	 * @return
	 */
	public RestResponse<GenericResponse> reload2FACache() {
		return authService.loadTwoFAUserPreference();
	}

	/**
	 * 
	 * Method to clear all kambala rest session
	 * 
	 * @author Dinesh Kumar
	 *
	 * @return
	 */
	public RestResponse<GenericResponse> clearAllRestSession() {
		HazelcastConfig.getInstance().getRestUserSession().clear();
		HazelcastConfig.getInstance().getIsRestUserSessionActive().clear();
		HazelcastConfig.getInstance().getUserSessionDetails().clear();
		return prepareResponse.prepareSuccessMessage(AppConstants.SUCCESS_STATUS);
	}

	/**
	 * 
	 * Method to clear user kambala rest session
	 * 
	 * @author Dinesh Kumar
	 *
	 * @param userId
	 * @return
	 */
	public RestResponse<GenericResponse> clearRestSessionById(String userId) {
		HazelcastConfig.getInstance().getRestUserSession().remove(userId + AppConstants.HAZEL_KEY_REST_SESSION);
		HazelcastConfig.getInstance().getIsRestUserSessionActive().remove(userId + AppConstants.HAZEL_KEY_REST_SESSION);
		return prepareResponse.prepareSuccessMessage(AppConstants.SUCCESS_STATUS);
	}

}
