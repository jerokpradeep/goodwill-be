package in.codifi.alerts.utility;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.enterprise.context.ApplicationScoped;

import in.codifi.alerts.config.HazelcastConfig;
import in.codifi.alerts.controller.DefaultRestController;
import in.codifi.cache.model.ClinetInfoModel;

@ApplicationScoped
public class AppUtil extends DefaultRestController {

	/**
	 * 
	 * Method to get client info
	 * 
	 * @author Dinesh Kumar
	 *
	 * @return
	 */
	public ClinetInfoModel getClientInfo() {
		ClinetInfoModel model = clientInfo();
		return model;
	}

	/**
	 * method to get user session
	 * 
	 * @author SowmiyaThangaraj
	 * @param userId
	 * @return
	 */
	public static String getUserSession(String userId) {
		String userSession = "";
		String hzUserSessionKey = userId + AppConstants.HAZEL_KEY_REST_SESSION;
		userSession = HazelcastConfig.getInstance().getRestUserSession().get(hzUserSessionKey);
		return userSession;
	}

	/**
	 * ō Method to validate give input is Email
	 * 
	 * @author DINESH KUMAR
	 *
	 * @param input
	 * @return
	 */
	public boolean isEmail(String input) {
		Pattern pattern = Pattern.compile("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$"); // Regular expression for an email
																					// address
		Matcher matcher = pattern.matcher(input);
		return matcher.matches();
	}

}
