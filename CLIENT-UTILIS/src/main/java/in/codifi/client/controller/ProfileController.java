package in.codifi.client.controller;

import javax.inject.Inject;
import javax.ws.rs.Path;

import org.jboss.resteasy.reactive.RestResponse;

import in.codifi.cache.model.ClinetInfoModel;
import in.codifi.client.controller.spec.IProfileController;
import in.codifi.client.model.request.ClientDetailsReqModel;
import in.codifi.client.model.response.GenericResponse;
import in.codifi.client.model.response.SessModel;
import in.codifi.client.service.spec.IProfileService;
import in.codifi.client.utilis.AppConstants;
import in.codifi.client.utilis.AppUtil;
import in.codifi.client.utilis.PrepareResponse;
import in.codifi.client.utilis.StringUtil;
import io.quarkus.logging.Log;

@Path("/profile")
public class ProfileController implements IProfileController {
	
	@Inject
	AppUtil appUtil;
	@Inject
	PrepareResponse prepareResponse;
	@Inject
	IProfileService profileService;

	/**
	 * method to get client details
	 * 
	 * @author GOWTHAMAN M
	 * @return
	 */
	@Override
	public RestResponse<GenericResponse> getClientDetails() {
		ClinetInfoModel info = appUtil.getClientInfo();
		if (info == null || StringUtil.isNullOrEmpty(info.getUserId())) {
			Log.error("Client info is null");
			return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
		} else if (StringUtil.isNullOrEmpty(info.getUcc())) {
			return prepareResponse.prepareFailedResponse(AppConstants.GUEST_USER_ERROR);
		}
		return profileService.getClientDetails(info);
	}

	/**
	 * Method to invalidate web socket session
	 * 
	 * @author dinesh
	 * @param model
	 * @return
	 */
	@Override
	public RestResponse<GenericResponse> invalidateWsSession(ClientDetailsReqModel reqModel) {
		ClinetInfoModel info = appUtil.getClientInfo();
		if (info == null || StringUtil.isNullOrEmpty(info.getUserId())) {
			Log.error("Client info is null");
			return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
		} else if (StringUtil.isNullOrEmpty(info.getUcc())) {
			return prepareResponse.prepareFailedResponse(AppConstants.GUEST_USER_ERROR);
		}
		return profileService.invalidateWsSession(reqModel, info);
	}

	/**
	 * Method to create web socket session
	 * 
	 * @param model
	 * @return
	 */
	@Override
	public RestResponse<GenericResponse> createWsSession(ClientDetailsReqModel reqModel) {
		ClinetInfoModel info = appUtil.getClientInfo();
		if (info == null || StringUtil.isNullOrEmpty(info.getUserId())) {
			Log.error("Client info is null");
			return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
		} else if (StringUtil.isNullOrEmpty(info.getUcc())) {
			return prepareResponse.prepareFailedResponse(AppConstants.GUEST_USER_ERROR);
		}
		return profileService.createWsSession(reqModel, info);
	}

	/**
	 * Method to get user session for WS
	 * 
	 * @author DINESH KUMAR
	 * @return
	 */
	@Override
	public RestResponse<GenericResponse> getDetails() {
		ClinetInfoModel info = appUtil.getClientInfo();
		if (info == null || StringUtil.isNullOrEmpty(info.getUserId())) {
			Log.error("Client info is null");
			return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
		} else if (StringUtil.isNullOrEmpty(info.getUcc())) {
			return prepareResponse.prepareFailedResponse(AppConstants.GUEST_USER_ERROR);
		}
		/** Verify session **/
		String userSession = AppUtil.getUserSession(info.getUserId());
		if (StringUtil.isNullOrEmpty(userSession))
			return prepareResponse.prepareUnauthorizedResponse();

		SessModel model = new SessModel();
		model.setKey(userSession);
		model.setUserId(info.getUcc());

		return prepareResponse.prepareSuccessResponseObject(model);
	}

}
