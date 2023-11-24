package in.codifi.api.util;

import javax.enterprise.context.ApplicationScoped;

import in.codifi.api.controller.DefaultRestController;
import in.codifi.cache.model.ClinetInfoModel;

@ApplicationScoped
public class AppUtil extends DefaultRestController {

	/**
	 * 
	 * Method to validate the userId
	 * 
	 * @author Dinesh Kumar
	 *
	 * @param reqUserID
	 * @return
	 */
	public boolean isValidUser(String userID) {

		try {
			String userIdFromToken = getUserId();
			if (StringUtil.isNotNullOrEmpty(userID) && StringUtil.isNotNullOrEmpty(userIdFromToken)) {
				if (userID.equalsIgnoreCase(userIdFromToken)) {
					return true;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

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

}
