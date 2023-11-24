package in.codifi.auth.cache;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.jboss.resteasy.reactive.RestResponse;

import in.codifi.auth.config.HazelcastConfig;
import in.codifi.auth.model.response.GenericResponse;
import in.codifi.auth.servcie.AuthService;

@ApplicationScoped
public class CacheService {

	@Inject
	AuthService authService;

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

}
