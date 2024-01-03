package in.codifi.api.controller;

import javax.inject.Inject;
import javax.ws.rs.Path;

import org.jboss.resteasy.reactive.RestResponse;

import in.codifi.api.controller.spec.AdvanceMWTestControllerSpec;
import in.codifi.api.model.MWReqModel;
import in.codifi.api.model.MwRequestModel;
import in.codifi.api.model.ResponseModel;
import in.codifi.api.service.spec.AdvanceMWTestServiceSpec;
import in.codifi.api.util.AppConstants;
import in.codifi.api.util.AppUtil;
import in.codifi.api.util.PrepareResponse;
import in.codifi.api.util.StringUtil;
import in.codifi.cache.model.ClinetInfoModel;
import io.quarkus.logging.Log;

@Path("/advance")
public class AdvanceMWTestController implements AdvanceMWTestControllerSpec {

	@Inject
	AppUtil appUtil;
	@Inject
	PrepareResponse prepareResponse;
	@Inject
	AdvanceMWTestServiceSpec advanceMWService;

	/**
	 * method to advance market watch
	 * 
	 * @author sowmiya
	 */
	@Override
	public RestResponse<ResponseModel> advanceMW(MWReqModel reqModel) {
		ClinetInfoModel info = appUtil.getClientInfo();
		if (info == null || StringUtil.isNullOrEmpty(info.getUserId())) {
			Log.error("Client info is null");
			return prepareResponse.prepareFailedResponse(AppConstants.INVALID_PARAMETER);
		}
		return advanceMWService.advanceMW(reqModel, info);
	}

	/**
	 * method to add new scrip
	 * 
	 * @author SowmiyaThangaraj
	 */
	@Override
	public RestResponse<ResponseModel> addscrip(MwRequestModel pDto) {
		ClinetInfoModel info = appUtil.getClientInfo();
		if (info == null || StringUtil.isNullOrEmpty(info.getUserId())) {
			Log.error("Client info is null");
			return prepareResponse.prepareFailedResponse(AppConstants.INVALID_PARAMETER);
		} else {
			return advanceMWService.addscrip(pDto, info);

		}
	}

	/**
	 * method to advance market watch scrips
	 * 
	 * @author sowmiya
	 */
	@Override
	public RestResponse<ResponseModel> advanceMWScrips(MWReqModel reqModel) {
		ClinetInfoModel info = appUtil.getClientInfo();
		if (info == null || StringUtil.isNullOrEmpty(info.getUserId())) {
			Log.error("Client info is null");
			return prepareResponse.prepareFailedResponse(AppConstants.INVALID_PARAMETER);
		}
		return advanceMWService.advanceMWScrips(reqModel, info);
	}

	/**
	 * method to delete scrip
	 * 
	 * @author sowmiya
	 */
	@Override
	public RestResponse<ResponseModel> deletescrip(MwRequestModel pDto) {
		ClinetInfoModel info = appUtil.getClientInfo();
		if (info == null || StringUtil.isNullOrEmpty(info.getUserId())) {
			Log.error("Client info is null");
			return prepareResponse.prepareFailedResponse(AppConstants.INVALID_PARAMETER);
		} else {
			return advanceMWService.deletescrip(pDto, info);

		}
	}

	/**
	 * method to sort market watch scrips
	 * 
	 * @author sowmiya
	 */
	@Override
	public RestResponse<ResponseModel> sortMwScrips(MwRequestModel pDto) {
//		ClinetInfoModel info = appUtil.getClientInfo();
//		if (info == null || StringUtil.isNullOrEmpty(info.getUserId())) {
//			Log.error("Client info is null");
//			return prepareResponse.prepareFailedResponse(AppConstants.INVALID_PARAMETER);
//		} else {
		String userid = "GC110180";
			return advanceMWService.sortMwScrips(pDto, userid);

//		}
	}

}
