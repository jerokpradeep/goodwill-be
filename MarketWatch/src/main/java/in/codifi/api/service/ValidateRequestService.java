package in.codifi.api.service;

import javax.enterprise.context.ApplicationScoped;

import in.codifi.api.controller.DefaultRestController;
import in.codifi.api.model.MwRequestModel;
import in.codifi.api.util.StringUtil;

@ApplicationScoped
public class ValidateRequestService extends DefaultRestController {

	/**
	 * 
	 * Method to validate the userId
	 * 
	 * @author Dinesh Kumar
	 *
	 * @param reqUserID
	 * @return
	 */
	public boolean isValidUser(MwRequestModel pDto) {

		try {
			String userIdFromToken = getUserId();
			if (pDto != null && StringUtil.isNotNullOrEmpty(pDto.getUserId())
					&& StringUtil.isNotNullOrEmpty(userIdFromToken)) {
				if (pDto.getUserId().equalsIgnoreCase(userIdFromToken)) {
					return true;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
}
