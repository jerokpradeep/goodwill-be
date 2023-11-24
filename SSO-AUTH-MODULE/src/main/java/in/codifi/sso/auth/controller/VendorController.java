package in.codifi.sso.auth.controller;

import javax.inject.Inject;
import javax.ws.rs.Path;

import org.jboss.resteasy.reactive.RestResponse;

import in.codifi.cache.model.ClinetInfoModel;
import in.codifi.sso.auth.controller.spec.VendorControllerSpec;
import in.codifi.sso.auth.entity.primary.VendorAppEntity;
import in.codifi.sso.auth.model.response.GenericResponse;
import in.codifi.sso.auth.service.spec.VendorServiceSpec;
import in.codifi.sso.auth.utility.AppConstants;
import in.codifi.sso.auth.utility.AppUtil;
import in.codifi.sso.auth.utility.PrepareResponse;
import in.codifi.sso.auth.utility.StringUtil;
import io.quarkus.logging.Log;

@Path("/vendor")
public class VendorController implements VendorControllerSpec {

	@Inject
	VendorServiceSpec vendorServiceSpec;

	@Inject
	PrepareResponse prepareResponse;

	@Inject
	AppUtil appUtil;

	/**
	 * Method to get vendor app details
	 * 
	 * @author Dinesh Kumar
	 * @return
	 */
	@Override
	public RestResponse<GenericResponse> getVendorAppDetails() {
		ClinetInfoModel info = appUtil.getClientInfo();
		if (info == null || StringUtil.isNullOrEmpty(info.getUserId())) {
			Log.error("Client info is null");
			return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
		} else if (StringUtil.isNullOrEmpty(info.getUcc())) {
			return prepareResponse.prepareFailedResponse(AppConstants.GUEST_USER_ERROR);
		}
		return vendorServiceSpec.getVendorAppDetails(info.getUserId());
	}

	/**
	 * Method to create new vendor app
	 * 
	 * @author Dinesh Kumar
	 * @param vendorAppReqModel
	 * @return
	 */
	@Override
	public RestResponse<GenericResponse> createNewVendorApp(VendorAppEntity entity) {
		ClinetInfoModel info = appUtil.getClientInfo();
		if (info == null || StringUtil.isNullOrEmpty(info.getUserId())) {
			Log.error("Client info is null");
			return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
		} else if (StringUtil.isNullOrEmpty(info.getUcc())) {
			return prepareResponse.prepareFailedResponse(AppConstants.GUEST_USER_ERROR);
		}
		return vendorServiceSpec.createNewVendorApp(entity, info.getUserId());
	}

	/**
	 * Method to update vendor details
	 * 
	 * @author Dinesh Kumar
	 * @param entity
	 * @return
	 */
	@Override
	public RestResponse<GenericResponse> updateVendorApp(VendorAppEntity entity) {
		ClinetInfoModel info = appUtil.getClientInfo();
		if (info == null || StringUtil.isNullOrEmpty(info.getUserId())) {
			Log.error("Client info is null");
			return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
		} else if (StringUtil.isNullOrEmpty(info.getUcc())) {
			return prepareResponse.prepareFailedResponse(AppConstants.GUEST_USER_ERROR);
		}
		return vendorServiceSpec.updateVendorApp(entity, info.getUserId());
	}

	/**
	 * Method to rest API secret key
	 * 
	 * @author Dinesh Kumar
	 */
	@Override
	public RestResponse<GenericResponse> restAPISecret(long appId) {
		ClinetInfoModel info = appUtil.getClientInfo();
		if (info == null || StringUtil.isNullOrEmpty(info.getUserId())) {
			Log.error("Client info is null");
			return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
		} else if (StringUtil.isNullOrEmpty(info.getUcc())) {
			return prepareResponse.prepareFailedResponse(AppConstants.GUEST_USER_ERROR);
		}
		return vendorServiceSpec.restAPISecret(appId, info.getUserId());
	}

	/**
	 * Method to delete vendor
	 * 
	 * @author Dinesh Kumar
	 */
	@Override
	public RestResponse<GenericResponse> deleteVendor(long appId) {
		ClinetInfoModel info = appUtil.getClientInfo();
		if (info == null || StringUtil.isNullOrEmpty(info.getUserId())) {
			Log.error("Client info is null");
			return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
		} else if (StringUtil.isNullOrEmpty(info.getUcc())) {
			return prepareResponse.prepareFailedResponse(AppConstants.GUEST_USER_ERROR);
		}
		return vendorServiceSpec.deleteVendor(appId, info.getUserId());
	}
}
