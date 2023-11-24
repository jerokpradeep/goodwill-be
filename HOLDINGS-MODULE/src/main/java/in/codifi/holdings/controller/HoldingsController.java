package in.codifi.holdings.controller;

import javax.inject.Inject;
import javax.ws.rs.Path;

import org.jboss.resteasy.reactive.RestResponse;

import in.codifi.cache.model.ClinetInfoModel;
import in.codifi.holdings.controller.spec.HoldingsControllerSpec;
import in.codifi.holdings.model.request.HoldingsReqModel;
import in.codifi.holdings.model.response.GenericResponse;
import in.codifi.holdings.service.spec.HoldingsServiceSpec;
import in.codifi.holdings.utility.AppConstants;
import in.codifi.holdings.utility.AppUtil;
import in.codifi.holdings.utility.PrepareResponse;
import in.codifi.holdings.utility.StringUtil;
import io.quarkus.logging.Log;

@Path("/holdings")
public class HoldingsController implements HoldingsControllerSpec {

	@Inject
	HoldingsServiceSpec holdingService;
	@Inject
	PrepareResponse prepareResponse;
	@Inject
	AppUtil appUtil;

	/**
	 * Method to get CNC Holdings data
	 * 
	 * @author Dinesh Kumar
	 *
	 * @param reqModel
	 * @return
	 */
	@Override
	public RestResponse<GenericResponse> getCNCHoldings() {

		ClinetInfoModel info = appUtil.getClientInfo();
		if (info == null || StringUtil.isNullOrEmpty(info.getUserId())) {
			Log.error("Client info is null");
			return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
		} else if (StringUtil.isNullOrEmpty(info.getUcc())) {
			return prepareResponse.prepareFailedResponse(AppConstants.GUEST_USER_ERROR);
		}
		return holdingService.getCNCHoldings(info);
	}

	/**
	 * Method to get MTF Holdings data
	 * 
	 * @author Dinesh Kumar
	 *
	 * @param reqModel
	 * @return
	 */
	@Override
	public RestResponse<GenericResponse> getMTFHoldings() {

		ClinetInfoModel info = appUtil.getClientInfo();
		if (info == null || StringUtil.isNullOrEmpty(info.getUserId())) {
			Log.error("Client info is null");
			return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
		} else if (StringUtil.isNullOrEmpty(info.getUcc())) {
			return prepareResponse.prepareFailedResponse(AppConstants.GUEST_USER_ERROR);
		}
		return holdingService.getMTFHoldings(info);
	}

	/**
	 * Method to get Holdings data
	 * 
	 * @author Dinesh Kumar
	 *
	 * @param reqModel
	 * @return
	 */
	@Override
	public RestResponse<GenericResponse> getHoldingsByProduct(HoldingsReqModel reqModel) {

		if (reqModel == null)
			return prepareResponse.prepareFailedResponse(AppConstants.INVALID_PARAMETER);

		ClinetInfoModel info = appUtil.getClientInfo();
		if (info == null || StringUtil.isNullOrEmpty(info.getUserId())) {
			Log.error("Client info is null");
			return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
		} else if (StringUtil.isNullOrEmpty(info.getUcc())) {
			return prepareResponse.prepareFailedResponse(AppConstants.GUEST_USER_ERROR);
		}
		return holdingService.getHoldingsByProduct(reqModel, info);
	}

	/**
	 * Method to get non POA CNC holdings
	 * 
	 * @author DINESH KUMAR
	 *
	 * @return
	 */
	@Override
	public RestResponse<GenericResponse> getCNCNonPOAHoldings() {

		ClinetInfoModel info = appUtil.getClientInfo();
		if (info == null || StringUtil.isNullOrEmpty(info.getUserId())) {
			Log.error("Client info is null");
			return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
		} else if (StringUtil.isNullOrEmpty(info.getUcc())) {
			return prepareResponse.prepareFailedResponse(AppConstants.GUEST_USER_ERROR);
		}
		return holdingService.getCNCNonPOAHoldings(info);
	}

	/**
	 * Method to get non POA MTF Holdings data
	 * 
	 * @author Dinesh Kumar
	 *
	 * @param reqModel
	 * @return
	 */
	@Override
	public RestResponse<GenericResponse> getMTFNonPOAHoldings() {

		ClinetInfoModel info = appUtil.getClientInfo();
		if (info == null || StringUtil.isNullOrEmpty(info.getUserId())) {
			Log.error("Client info is null");
			return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
		} else if (StringUtil.isNullOrEmpty(info.getUcc())) {
			return prepareResponse.prepareFailedResponse(AppConstants.GUEST_USER_ERROR);
		}
		return holdingService.getMTFNonPOAHoldings(info);
	}

}
