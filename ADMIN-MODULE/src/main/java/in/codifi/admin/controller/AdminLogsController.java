package in.codifi.admin.controller;

import javax.inject.Inject;
import javax.ws.rs.Path;

import org.jboss.resteasy.reactive.RestResponse;

import in.codifi.admin.controller.spec.AdminLogsControllerSpec;
import in.codifi.admin.model.response.AccesslogResponseModel;
import in.codifi.admin.model.response.GenericResponse;
import in.codifi.admin.service.spec.AdminLogsServiceSpec;

@Path("/admin")
public class AdminLogsController implements AdminLogsControllerSpec {

	@Inject
	AdminLogsServiceSpec service;

	/**
	 * Method to get the total logged in details for past days
	 * 
	 * @author LOKESH
	 * @return
	 */
	@Override
	public RestResponse<GenericResponse> userLogDetails() {
		return service.userLogDetails();
	}

	/**
	 * method to get the user based records from the data base (TOP 10 USER)
	 * 
	 * @author LOKESH
	 * @return
	 */
	@Override
	public RestResponse<GenericResponse> getUserbasedRecords(AccesslogResponseModel accessModel) {
		return service.getUserbasedRecords(accessModel);
	}

	/**
	 * method to get the user based records from the data base (TOP 10 USER)
	 * 
	 * @author LOKESH
	 * @return
	 */
	@Override
	public RestResponse<GenericResponse> getUrlBasedRecords() {
		return service.getUrlBasedRecords();
	}

	/**
	 * Method to get last 12 hour login count
	 * 
	 * @author LOKESH
	 * @return
	 */
	@Override
	public RestResponse<GenericResponse> getLast12hourLoginCount() {
		return service.getLast12hourLoginCount();
	}

	/**
	 * Method to get distinct url for drop down
	 * 
	 * @author LOKESH
	 * @return
	 */
	@Override
	public RestResponse<GenericResponse> getDistinctUrl() {
		return service.getDistinctUrl();
	}

	/**
	 * method to get users logged in details
	 * 
	 * @author SowmiyaThangaraj
	 */
	@Override
	public RestResponse<GenericResponse> getUserLoggedInDetails() {
		return service.getUserLoggedInDetails();
	}
}
