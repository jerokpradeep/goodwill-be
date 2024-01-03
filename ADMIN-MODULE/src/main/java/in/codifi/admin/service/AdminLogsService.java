package in.codifi.admin.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.jboss.resteasy.reactive.RestResponse;
import org.json.simple.JSONObject;

import in.codifi.admin.model.response.AccesslogResponseModel;
import in.codifi.admin.model.response.GenericResponse;
import in.codifi.admin.model.response.LogDetailsResponseModel;
import in.codifi.admin.model.response.UsersLoggedInModel;
import in.codifi.admin.repository.UserLogManager;
import in.codifi.admin.repository.AdminLogsDAO;
import in.codifi.admin.service.spec.AdminLogsServiceSpec;
import in.codifi.admin.utility.AppConstants;
import in.codifi.admin.utility.PrepareResponse;
import in.codifi.cache.CacheController;

@ApplicationScoped
public class AdminLogsService implements AdminLogsServiceSpec {
	/**
	 * Method to get the total logged in details for past days
	 * 
	 * @author LOKESH
	 * @return
	 */
	@Inject
	PrepareResponse prepareResponse;
	@Inject
	AdminLogsDAO adminDAO;
	@Inject
	UserLogManager accessLogManager;

	@Inject

	public RestResponse<GenericResponse> userLogDetails() {
		List<LogDetailsResponseModel> result = adminDAO.getUserLogDetails();
		if (result != null && result.size() > 0) {
			return prepareResponse.prepareSuccessResponseObject(result);
		} else {
			return prepareResponse.prepareFailedResponse(AppConstants.NO_RECORDS_FOUND);
		}
	}

	/**
	 * method to get the user based records from the data base (TOP 10 USER)
	 * 
	 * @author LOKESH
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public RestResponse<GenericResponse> getUserbasedRecords(AccesslogResponseModel accessModel) {
		List<JSONObject> result = new ArrayList<JSONObject>();
		if (accessModel.getCount().equalsIgnoreCase("all")) {
			for (Map.Entry<String, String> map : CacheController.getUserRec().entrySet()) {
				String key = map.getKey();
				String[] a = key.split("_");
				String user = a[0];
				String deviceIp = a[1];
				String count = map.getValue();
				JSONObject js = new JSONObject();
				js.put("user", user);
				js.put("device_ip", deviceIp);
				js.put("count", count);
				result.add(js);
			}
		} else {
			for (Map.Entry<String, String> map : CacheController.getUserRec().entrySet().stream().limit(25)
					.collect(Collectors.toList())) {
				String key = map.getKey();
				String[] a = key.split("_");
				String user = a[0];
				String deviceIp = a[1];
				String count = map.getValue();
				JSONObject js = new JSONObject();
				js.put("user", user);
				js.put("device_ip", deviceIp);
				js.put("count", count);
				result.add(js);
			}
		}
		if (result != null && result.size() > 0) {
			return prepareResponse.prepareSuccessResponseObject(result);
		} else {
			return prepareResponse.prepareFailedResponse(AppConstants.NO_RECORDS_FOUND);
		}
	}

	/**
	 * method to get the url based records
	 * 
	 * @author LOKESH
	 * @return
	 */
	@Override
	public RestResponse<GenericResponse> getUrlBasedRecords() {
		List<JSONObject> result = adminDAO.getUrlBasedRecords();
		if (result != null && result.size() > 0) {
			return prepareResponse.prepareSuccessResponseObject(result);
		} else {
			return prepareResponse.prepareFailedResponse(AppConstants.NO_RECORDS_FOUND);
		}
	}

	/**
	 * Method to get last 12 hour login count
	 * 
	 * @author LOKESH
	 * @return
	 */
	@Override
	public RestResponse<GenericResponse> getLast12hourLoginCount() {
		List<JSONObject> result = adminDAO.getLast12hourLoginCount();
		if (!result.isEmpty()) {
			return prepareResponse.prepareSuccessResponseObject(result);
		} else {
			return prepareResponse.prepareFailedResponse(AppConstants.NO_RECORDS_FOUND);
		}
	}

	/**
	 * Method to get distinct url for drop down
	 * 
	 * @author LOKESH
	 * @return
	 */
	@Override
	public RestResponse<GenericResponse> getDistinctUrl() {
		List<String> result = adminDAO.getDistinctUrl();
		if (result != null && result.size() > 0) {
			return prepareResponse.prepareSuccessResponseObject(result);
		} else {
			return prepareResponse.prepareFailedResponse(AppConstants.NO_RECORDS_FOUND);
		}
	}

	/**
	 * method to get users logged in details
	 * 
	 * @author SowmiyaThangaraj
	 */
	@Override
	public RestResponse<GenericResponse> getUserLoggedInDetails() {
		UsersLoggedInModel model = new UsersLoggedInModel();
		model = accessLogManager.getCountBySource();
		int totalCount = accessLogManager.getTotalUserLoggedInDetails();
		model.setTotalCount(totalCount);
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

}
