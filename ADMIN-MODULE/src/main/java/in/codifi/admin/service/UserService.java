package in.codifi.admin.service;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.jboss.resteasy.reactive.RestResponse;
import org.json.simple.JSONObject;

import in.codifi.admin.model.response.GenericResponse;
import in.codifi.admin.model.response.UsersLoggedInModel;
import in.codifi.admin.repository.AccessLogManager;
import in.codifi.admin.service.spec.UserServiceSpec;
import in.codifi.admin.utility.AppConstants;
import in.codifi.admin.utility.PrepareResponse;

@ApplicationScoped
public class UserService implements UserServiceSpec {

	@Inject
	PrepareResponse prepareResponse;
	@Inject
	AccessLogManager accessLogManager;

	/**
	 * method to get users logged in details
	 * 
	 * @author LOKESH
	 */
	@Override
	public RestResponse<GenericResponse> getUserLoggedInDetails() {
		UsersLoggedInModel model = new UsersLoggedInModel();
		model = accessLogManager.getCountBySource();

		List<String> distinctVendor = accessLogManager.findDistinctVendors();
		if (distinctVendor != null && !distinctVendor.isEmpty()) {
			List<JSONObject> ssoCountByVendor = accessLogManager.getCountByVendor(distinctVendor);
			if (ssoCountByVendor != null) {
				model.setSso(ssoCountByVendor);
				return prepareResponse.prepareSuccessResponseObject(model);
			}
		} else {
			return prepareResponse.prepareSuccessResponseObject(model);
		}

		return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
	}

	/**
	 * Method to Truncate user logged in details
	 * 
	 * @author LOKESH
	 * @return
	 */
	@Override
	public RestResponse<GenericResponse> truncateUserLoggedInDetails() {
		return accessLogManager.truncateUserLoggedInDetails();
	}
}
