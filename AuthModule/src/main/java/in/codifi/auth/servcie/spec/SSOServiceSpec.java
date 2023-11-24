package in.codifi.auth.servcie.spec;

import org.jboss.resteasy.reactive.RestResponse;

import in.codifi.auth.model.request.VendorReqModel;
import in.codifi.auth.model.response.GenericResponse;

public interface SSOServiceSpec {

	/**
	 * Method to authorize Vendor
	 * 
	 * @author Dinesh Kumar
	 * @param vendorReqModel
	 * @return
	 */
	RestResponse<GenericResponse> ssoAuthorizeVendor(VendorReqModel vendorReqModel);

	/**
	 * Method to check Vendor Authorization
	 * 
	 * @author Dinesh Kumar
	 * @param vendorReqModel
	 * @return
	 */
	RestResponse<GenericResponse> checkVendorAuthorization(VendorReqModel vendorReqModel);

	/**
	 * Method to authorize Vendor
	 * 
	 * @author Dinesh Kumar
	 * @param vendorReqModel
	 * @return
	 */
	Object getUserDetails(VendorReqModel vendorReqModel);

	/**
	 * Method to check Vendor Authorization
	 * 
	 * @author Dinesh Kumar
	 * @param vendorReqModel
	 * @return
	 */
	Object getUserDetailsByAuth(VendorReqModel vendorReqModel);

	/**
	 * Method to get vendor app deatils
	 * 
	 * @author Dinesh Kumar
	 * @param authReq
	 * @return
	 */
	RestResponse<GenericResponse> getVendorAppDetails(VendorReqModel authReq);

}
