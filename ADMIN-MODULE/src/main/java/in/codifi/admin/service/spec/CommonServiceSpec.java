package in.codifi.admin.service.spec;

import org.jboss.resteasy.reactive.RestResponse;

import in.codifi.admin.model.request.SendNoficationReqModel;
import in.codifi.admin.model.request.UserReqModel;
import in.codifi.admin.model.response.GenericResponse;
import in.codifi.admin.model.response.KcUserDetailsRequest;
import in.codifi.admin.req.model.MobVersionReqModel;
import in.codifi.admin.req.model.VendorAppReqModel;
import in.codifi.admin.ws.model.kc.CreateUserRequestModel;

public interface CommonServiceSpec {

	/**
	 * Method to send Recommendation message
	 * 
	 * @author Gowthaman
	 * @param reqModel
	 * @return
	 */
	RestResponse<GenericResponse> sendRecommendationMessge(SendNoficationReqModel reqModel);

	/**
	 * method to get mobile version
	 * 
	 * @author LOKESH
	 * 
	 * @return
	 */
	RestResponse<GenericResponse> getMobileVersion();

	/**
	 * method to add mobile version
	 * 
	 * @author LOKESH
	 * 
	 * @return
	 */
	RestResponse<GenericResponse> addVersion(MobVersionReqModel model);

	/**
	 * methdo to update version
	 * 
	 * @author LOKESH
	 * @param entity
	 * @return
	 */
	RestResponse<GenericResponse> updateVersion(MobVersionReqModel model);

	/**
	 * method to delete version
	 * 
	 * @author LOKESH
	 * @param entity
	 * @return
	 */
	RestResponse<GenericResponse> deleteVersion(MobVersionReqModel model);

	/**
	 * Method to get api from Vendor
	 * 
	 * @author LOKESH
	 * @return
	 */
	RestResponse<GenericResponse> getVendor(VendorAppReqModel VendorAppModel);

	/**
	 * Method to Update authorization_status
	 * 
	 * @author LOKESH
	 * @return
	 */
	RestResponse<GenericResponse> updateVendor(VendorAppReqModel VendorAppModel);
	
	/**
	 * Method to get kc user details
	 * 
	 * @author Gowthaman
	 * @param req
	 * @return
	 */
	RestResponse<GenericResponse> getKcUserDetails(KcUserDetailsRequest req);
	
	/**
	 * Method to get kc All user details
	 * 
	 * @author Gowthaman
	 * @param req
	 * @return
	 */
	RestResponse<GenericResponse> getKcAllUserDetails(KcUserDetailsRequest req);
	
	/**
	 * Method to get kc User details in csv
	 * 
	 * @author Gowthaman
	 * @return
	 */
	RestResponse<GenericResponse> getUserDetailsCsv();
	
	/**
	 * Method to update Kc User Details
	 * 
	 * @author Gowthaman
	 * @param req
	 * @return
	 */
	RestResponse<GenericResponse> updateKcUserDetails(CreateUserRequestModel user);
	
	/**
	 * Method to add New User in keyclock
	 * 
	 * @author Gowthaman
	 * @param req
	 * @return
	 */
	RestResponse<GenericResponse> addNewUser(CreateUserRequestModel user);
	
	
	/**
	 * 
	 @author babin
	 @createdOn 18-Sep-2023
	 @param reqModel
	 @return
	 */
	RestResponse<GenericResponse> getUserNotificationList(UserReqModel reqModel);

}
