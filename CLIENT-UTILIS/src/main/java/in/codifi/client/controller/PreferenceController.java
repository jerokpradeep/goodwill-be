package in.codifi.client.controller;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.Path;

import org.jboss.resteasy.reactive.RestResponse;

import in.codifi.cache.model.ClinetInfoModel;
import in.codifi.client.controller.spec.IPreferenceController;
import in.codifi.client.model.request.PreferenceReqModel;
import in.codifi.client.model.response.GenericResponse;
import in.codifi.client.service.spec.IPreferenceService;
import in.codifi.client.utilis.AppConstants;
import in.codifi.client.utilis.AppUtil;
import in.codifi.client.utilis.PrepareResponse;
import in.codifi.client.utilis.StringUtil;
import io.quarkus.logging.Log;

@Path("/preferences")
public class PreferenceController implements IPreferenceController {
	
	@Inject
	AppUtil appUtil;
	@Inject
	PrepareResponse prepareResponse;
	@Inject
	IPreferenceService preferenceService;

	/**
	 * method to get User Preference
	 * 
	 * @author GOWTHAMAN M
	 * @return
	 */
	@Override
	public RestResponse<GenericResponse> getPreferenceForWeb() {
		ClinetInfoModel info = appUtil.getClientInfo();
		if (info == null || StringUtil.isNullOrEmpty(info.getUserId())) {
			Log.error("Client info is null");
			return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
		} else if (StringUtil.isNullOrEmpty(info.getUcc())) {
			return prepareResponse.prepareFailedResponse(AppConstants.GUEST_USER_ERROR);
		}
		return preferenceService.getPreferenceForWeb(info);
	}

	/**
	 * Method to update User Preference for web
	 * 
	 * @author GOWTHAMAN M
	 * @return
	 */
	@Override
	public RestResponse<GenericResponse> updatePreferenceForWeb(PreferenceReqModel reqModel) {
		ClinetInfoModel info = appUtil.getClientInfo();
		if (info == null || StringUtil.isNullOrEmpty(info.getUserId())) {
			Log.error("Client info is null");
			return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
		} else if (StringUtil.isNullOrEmpty(info.getUcc())) {
			return prepareResponse.prepareFailedResponse(AppConstants.GUEST_USER_ERROR);
		}
		return preferenceService.updatePreferenceForWeb(reqModel, info);
	}

	/**
	 * method to reset Preference foe web
	 * 
	 * @author GOWTHAMAN M
	 * @return
	 */
	@Override
	public RestResponse<GenericResponse> resetPreferenceForWeb() {
		ClinetInfoModel info = appUtil.getClientInfo();
		if (info == null || StringUtil.isNullOrEmpty(info.getUserId())) {
			Log.error("Client info is null");
			return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
		} else if (StringUtil.isNullOrEmpty(info.getUcc())) {
			return prepareResponse.prepareFailedResponse(AppConstants.GUEST_USER_ERROR);
		}
		return preferenceService.resetPreferenceForWeb(info);
	}

	/**
	 * Method to get User Preference for mobile
	 * 
	 * @author GOWTHAMAN M
	 * @return
	 */
	@Override
	public RestResponse<GenericResponse> getPreferenceForMobile() {
		ClinetInfoModel info = appUtil.getClientInfo();
		if (info == null || StringUtil.isNullOrEmpty(info.getUserId())) {
			Log.error("Client info is null");
			return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
		} else if (StringUtil.isNullOrEmpty(info.getUcc())) {
			return prepareResponse.prepareFailedResponse(AppConstants.GUEST_USER_ERROR);
		}
		return preferenceService.getPreferenceForMobile(info);
	}

	/**
	 * Method to update Preference for Mobile
	 * 
	 * @author GOWTHAMAN M
	 * @return
	 */
	@Override
	public RestResponse<GenericResponse> updatePreferenceForMobile(PreferenceReqModel reqModel) {
		ClinetInfoModel info = appUtil.getClientInfo();
		if (info == null || StringUtil.isNullOrEmpty(info.getUserId())) {
			Log.error("Client info is null");
			return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
		} else if (StringUtil.isNullOrEmpty(info.getUcc())) {
			return prepareResponse.prepareFailedResponse(AppConstants.GUEST_USER_ERROR);
		}
		return preferenceService.updatePreferenceForMobile(reqModel, info);
	}
	
	/**
	 * Method to update multiple preference for mobile
	 * @author DINESH KUMAR
	 *
	 * @param reqModel
	 * @return
	 */
	@Override
	public RestResponse<GenericResponse> updatePreferenceListForMobile(List<PreferenceReqModel> reqModel) {
		ClinetInfoModel info = appUtil.getClientInfo();
		if (info == null || StringUtil.isNullOrEmpty(info.getUserId())) {
			Log.error("Client info is null");
			return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
		} else if (StringUtil.isNullOrEmpty(info.getUcc())) {
			return prepareResponse.prepareFailedResponse(AppConstants.GUEST_USER_ERROR);
		}
		return preferenceService.updatePreferenceListForMobile(reqModel, info);
	}

	/**
	 * method to reset Preference for mob
	 * 
	 * @author GOWTHAMAN M
	 * @return
	 */
	@Override
	public RestResponse<GenericResponse> resetPreferenceForMobile() {
		ClinetInfoModel info = appUtil.getClientInfo();
		if (info == null || StringUtil.isNullOrEmpty(info.getUserId())) {
			Log.error("Client info is null");
			return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
		} else if (StringUtil.isNullOrEmpty(info.getUcc())) {
			return prepareResponse.prepareFailedResponse(AppConstants.GUEST_USER_ERROR);
		}
		return preferenceService.resetPreferenceForMobile(info);
	}

	/**
	 * Method to load user preference
	 * 
	 * @author GOWTHAMAN M
	 * @return
	 */
	@Override
	public RestResponse<GenericResponse> loadUserPreference() {
		ClinetInfoModel info = appUtil.getClientInfo();
		if (info == null || StringUtil.isNullOrEmpty(info.getUserId())) {
			Log.error("Client info is null");
			return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
		} else if (StringUtil.isNullOrEmpty(info.getUcc())) {
			return prepareResponse.prepareFailedResponse(AppConstants.GUEST_USER_ERROR);
		}
		return preferenceService.loadUserPreference();
	}

	/**
	 * Method to load master preferences
	 * 
	 * @author GOWTHAMAN M
	 * @return
	 */
	@Override
	public RestResponse<GenericResponse> loadMasterPreference() {
		ClinetInfoModel info = appUtil.getClientInfo();
		if (info == null || StringUtil.isNullOrEmpty(info.getUserId())) {
			Log.error("Client info is null");
			return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
		} else if (StringUtil.isNullOrEmpty(info.getUcc())) {
			return prepareResponse.prepareFailedResponse(AppConstants.GUEST_USER_ERROR);
		}
		return preferenceService.loadMasterPreference();
	}
	
	

}
