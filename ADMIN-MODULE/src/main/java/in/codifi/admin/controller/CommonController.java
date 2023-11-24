package in.codifi.admin.controller;

import javax.inject.Inject;
import javax.ws.rs.Path;

import org.jboss.resteasy.reactive.RestResponse;

import in.codifi.admin.controller.spec.CommonControllerSpec;
import in.codifi.admin.model.request.SendNoficationReqModel;
import in.codifi.admin.model.request.UserReqModel;
import in.codifi.admin.model.response.GenericResponse;
import in.codifi.admin.model.response.KcUserDetailsRequest;
import in.codifi.admin.req.model.MobVersionReqModel;
import in.codifi.admin.req.model.VendorAppReqModel;
import in.codifi.admin.service.spec.CommonServiceSpec;
import in.codifi.admin.ws.model.kc.CreateUserRequestModel;

@Path("/com")
public class CommonController implements CommonControllerSpec {

	@Inject
	CommonServiceSpec service;

	/**
	 * Method to send recommendation message
	 * 
	 * @author Gowthaman
	 * @param reqModel
	 * @return
	 */
	public RestResponse<GenericResponse> sendRecommendationMessage(SendNoficationReqModel reqModel) {
		return service.sendRecommendationMessge(reqModel);
	}

	/**
	 * method to get mobile version
	 * 
	 * @author LOKESH
	 * @return
	 */
	public RestResponse<GenericResponse> getMobileVersion() {
		return service.getMobileVersion();
	}

	/**
	 * method to add new mobile version
	 * 
	 * @author LOKESH
	 */
	public RestResponse<GenericResponse> addVersion(MobVersionReqModel model) {
		return service.addVersion(model);
	}

	/**
	 * method to update mobile version
	 * 
	 * @author LOKESH
	 */
	public RestResponse<GenericResponse> updateVersion(MobVersionReqModel model) {
		return service.updateVersion(model);
	}

	/**
	 * method to delete version
	 * 
	 * @author LOKESH
	 * @param entity
	 * @return
	 */
	public RestResponse<GenericResponse> deleteVersion(MobVersionReqModel model) {
		return service.deleteVersion(model);
	}

	/**
	 * Method to get api from Vendor
	 * 
	 * @author LOKESH
	 * @return
	 */
	@Override
	public RestResponse<GenericResponse> getVendor(VendorAppReqModel VendorAppModel) {
		return service.getVendor(VendorAppModel);
	}

	/**
	 * Method to Update authorization_status
	 * 
	 * @author LOKESH
	 * @return
	 */
	@Override
	public RestResponse<GenericResponse> updateVendor(VendorAppReqModel VendorAppModel) {
		return service.updateVendor(VendorAppModel);
	}
	
	/**
	 * Method to get kc user details
	 * 
	 * @author Gowthaman
	 * @param req
	 * @return
	 */
	@Override
	public RestResponse<GenericResponse> getKcUserDetails(KcUserDetailsRequest req) {
		return service.getKcUserDetails(req);
	}
	
	/**
	 * Method to get kc All user details
	 * 
	 * @author Gowthaman
	 * @param req
	 * @return
	 */
	@Override
	public RestResponse<GenericResponse> getKcAllUserDetails(KcUserDetailsRequest req) {
		return service.getKcAllUserDetails(req);
	}
	
	/**
	 * Method to get kc User details in csv
	 * 
	 * @author Gowthaman
	 * @return
	 */
	@Override
	public RestResponse<GenericResponse> getUserDetailsCsv() {
		return service.getUserDetailsCsv();
	}
	
	/**
	 * Method to update Kc User Details
	 * 
	 * @author Gowthaman
	 * @param req
	 * @return
	 */
	@Override
	public RestResponse<GenericResponse> updateKcUserDetails(CreateUserRequestModel req) {
		return service.updateKcUserDetails(req);
	}
	
	/**
	 * Method to add New User in keyclock
	 * 
	 * @author Gowthaman
	 * @param req
	 * @return
	 */
	@Override
	public RestResponse<GenericResponse> addNewUser(CreateUserRequestModel req) {
		return service.addNewUser(req);
	}
	
	
	/**
	 * Method to retrieve  push notification message
	 * 
	 * @author Babin
	 * @param reqModel
	 * @return
	 */
	public RestResponse<GenericResponse> getUserNotificationList(UserReqModel reqModel) {
		return service.getUserNotificationList(reqModel);
	}

	
}
