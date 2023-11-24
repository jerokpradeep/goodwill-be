package in.codifi.auth.controller;

import javax.inject.Inject;
import javax.ws.rs.Path;

import org.jboss.resteasy.reactive.RestResponse;

import in.codifi.auth.controller.spec.SSOControllerSpec;
import in.codifi.auth.model.request.VendorReqModel;
import in.codifi.auth.model.response.CommonErrorResponse;
import in.codifi.auth.model.response.GenericResponse;
import in.codifi.auth.servcie.spec.SSOServiceSpec;
import in.codifi.auth.utility.AppConstants;
import in.codifi.auth.utility.AppUtils;
import in.codifi.auth.utility.PrepareResponse;

@Path("/sso")
public class SSOController implements SSOControllerSpec {

	@Inject
	AppUtils appUtil;

	@Inject
	PrepareResponse prepareResponse;

	@Inject
	SSOServiceSpec ssoServiceSpec;

	/**
	 * Method to authorize Vendor
	 * 
	 * @author dinesh
	 * @param vendorReqModel
	 * @return
	 */
	@Override
	public RestResponse<GenericResponse> ssoAuthorizeVendor(VendorReqModel vendorReqModel) {
		if (vendorReqModel == null)
			return prepareResponse.prepareFailedResponse(AppConstants.INVALID_PARAMETER);
		return ssoServiceSpec.ssoAuthorizeVendor(vendorReqModel);
	}

	/**
	 * Method to check Vendor Authorization
	 * 
	 * @author dinesh
	 * @param vendorReqModel
	 * @return
	 */
	@Override
	public RestResponse<GenericResponse> checkVendorAuthorization(VendorReqModel vendorReqModel) {
		if (vendorReqModel == null)
			return prepareResponse.prepareFailedResponse(AppConstants.INVALID_PARAMETER);
		return ssoServiceSpec.checkVendorAuthorization(vendorReqModel);
	}

	/**
	 * Method to authorize Vendor
	 * 
	 * @author dinesh
	 * @param vendorReqModel
	 * @return
	 */
	@Override
	public Object getUserDetails(VendorReqModel vendorReqModel) {
		return ssoServiceSpec.getUserDetails(vendorReqModel);
	}

	/**
	 * Method to check Vendor Authorization
	 * 
	 * @author dinesh
	 * @param vendorReqModel
	 * @return
	 */
	@Override
	public Object getUserDetailsByAuth(VendorReqModel vendorReqModel) {
		CommonErrorResponse errorResponse = new CommonErrorResponse();
		if (vendorReqModel == null) {
			errorResponse.setStat(AppConstants.STATUS_NOT_OK_API);
			errorResponse.setEmsg(AppConstants.INVALID_PARAMETER);
			return errorResponse;
		}
		return ssoServiceSpec.getUserDetailsByAuth(vendorReqModel);
	}

	/**
	 * Method to get vendor APP details
	 * 
	 * @author Dinesh Kumar
	 * @param vendorReqModel
	 * @return
	 */
	@Override
	public RestResponse<GenericResponse> getVendorAppDetails(VendorReqModel vendorReqModel) {
		if (vendorReqModel == null)
			return prepareResponse.prepareFailedResponse(AppConstants.INVALID_PARAMETER);
		return ssoServiceSpec.getVendorAppDetails(vendorReqModel);
	}

}
