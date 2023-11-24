package in.codifi.api.controller;

import javax.inject.Inject;
import javax.ws.rs.Path;

import org.jboss.resteasy.reactive.RestResponse;

import in.codifi.api.controller.spec.IMarketWatchController;
import in.codifi.api.model.MwRequestModel;
import in.codifi.api.model.ResponseModel;
import in.codifi.api.service.ValidateRequestService;
import in.codifi.api.service.spec.IMarketWatchService;
import in.codifi.api.util.AppConstants;
import in.codifi.api.util.AppUtil;
import in.codifi.api.util.PrepareResponse;
import in.codifi.api.util.StringUtil;
import in.codifi.cache.model.ClinetInfoModel;
import io.quarkus.logging.Log;

/**
 * Class for Market Watch Controller
 * 
 * @author Gowrisankar
 *
 */
@Path("/marketWatch")
public class MarketWatchController implements IMarketWatchController {

	@Inject
	IMarketWatchService codifiMwService;
	@Inject
	ValidateRequestService validateRequestService;
	@Inject
	PrepareResponse prepareResponse;
	@Inject
	AppUtil appUtil;

	/**
	 * Method to provide the user scrips details from cache or data base
	 * 
	 * @author Gowrisankar
	 */
	@Override
	public RestResponse<ResponseModel> getAllMwScrips(MwRequestModel pDto) {

		ClinetInfoModel info = appUtil.getClientInfo();
		if (info == null || StringUtil.isNullOrEmpty(info.getUserId())) {
			Log.error("Client info is null");
			return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
		} else {
			return codifiMwService.getAllMwScrips(info.getUserId());
		}
	}

	/**
	 * Method to get the Scrip for given user id and market watch Id
	 * 
	 * @author Gowrisankar
	 */
	@Override
	public RestResponse<ResponseModel> getMWScrips(MwRequestModel pDto) {
		ClinetInfoModel info = appUtil.getClientInfo();
		if (info == null || StringUtil.isNullOrEmpty(info.getUserId())) {
			Log.error("Client info is null");
			return prepareResponse.prepareFailedResponse(AppConstants.INVALID_PARAMETER);
		} else {
			return codifiMwService.getMWScrips(pDto, info);

		}
	}

	/**
	 * Method to delete the scrips from the cache and market watch
	 * 
	 * @author Gowrisankar
	 */
	@Override
	public RestResponse<ResponseModel> deletescrip(MwRequestModel pDto) {
		ClinetInfoModel info = appUtil.getClientInfo();
		if (info == null || StringUtil.isNullOrEmpty(info.getUserId())) {
			Log.error("Client info is null");
			return prepareResponse.prepareFailedResponse(AppConstants.INVALID_PARAMETER);
		} else {
			return codifiMwService.deletescrip(pDto, info);

		}
	}

	/**
	 * Method to add the scrip into cache and data base
	 * 
	 * @author Gowrisankar
	 */
	@Override
	public RestResponse<ResponseModel> addscrip(MwRequestModel pDto) {

		ClinetInfoModel info = appUtil.getClientInfo();
		if (info == null || StringUtil.isNullOrEmpty(info.getUserId())) {
			Log.error("Client info is null");
			return prepareResponse.prepareFailedResponse(AppConstants.INVALID_PARAMETER);
		} else {
			return codifiMwService.addscrip(pDto, info);

		}
	}

	/**
	 * Method to sort the scrip into cache and data base
	 * 
	 * @author Gowrisankar
	 */
	@Override
	public RestResponse<ResponseModel> sortMwScrips(MwRequestModel pDto) {

		ClinetInfoModel info = appUtil.getClientInfo();
		if (info == null || StringUtil.isNullOrEmpty(info.getUserId())) {
			Log.error("Client info is null");
			return prepareResponse.prepareFailedResponse(AppConstants.INVALID_PARAMETER);
		} else {
			return codifiMwService.sortMwScrips(pDto, info);

		}
	}

	/**
	 * Method to create the new marketWatch
	 * 
	 * @author Dinesh Kumar
	 */
	@Override
	public RestResponse<ResponseModel> createMW(MwRequestModel pDto) {

		ClinetInfoModel info = appUtil.getClientInfo();
		if (info == null || StringUtil.isNullOrEmpty(info.getUserId())) {
			Log.error("Client info is null");
			return prepareResponse.prepareFailedResponse(AppConstants.INVALID_PARAMETER);
		} else {
			return codifiMwService.createMW(info.getUserId());

		}
	}

	/**
	 * Method to rename market watch
	 * 
	 * @author Dinesh Kumar
	 */
	@Override
	public RestResponse<ResponseModel> renameMarketWatch(MwRequestModel pDto) {

		ClinetInfoModel info = appUtil.getClientInfo();
		if (info == null || StringUtil.isNullOrEmpty(info.getUserId())) {
			Log.error("Client info is null");
			return prepareResponse.prepareFailedResponse(AppConstants.INVALID_PARAMETER);
		} else {
			return codifiMwService.renameMarketWatch(pDto, info);

		}
	}

	/**
	 * 
	 * Method to Delete expired scrips in MW List
	 * 
	 * @author Dinesh Kumar
	 *
	 * @return
	 */
	@Override
	public RestResponse<ResponseModel> deleteExpiredContract() {
		return codifiMwService.deleteExpiredContract();
	}
}
