package in.codifi.holdings.service;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.jboss.resteasy.reactive.RestResponse;

import com.fasterxml.jackson.databind.ObjectMapper;

import in.codifi.cache.model.ClinetInfoModel;
import in.codifi.holdings.model.request.HoldingsReqModel;
import in.codifi.holdings.model.response.GenericResponse;
import in.codifi.holdings.service.spec.HoldingsServiceSpec;
import in.codifi.holdings.utility.AppConstants;
import in.codifi.holdings.utility.AppUtil;
import in.codifi.holdings.utility.PrepareResponse;
import in.codifi.holdings.utility.StringUtil;
import in.codifi.holdings.ws.model.RestHoldingsReqModel;
import in.codifi.holdings.ws.model.RestNonPoaHoldingsReqModel;
import in.codifi.holdings.ws.service.HoldingsRestService;
import io.quarkus.logging.Log;

@ApplicationScoped
public class HoldingsService implements HoldingsServiceSpec {

	@Inject
	PrepareResponse prepareResponse;

	@Inject
	HoldingsRestService restService;

	ObjectMapper mapper = new ObjectMapper();

	/**
	 * Method to get holdings for CNC product
	 * 
	 * @author DINESH KUMAR
	 *
	 * @param info
	 * @return
	 */
	@Override
	public RestResponse<GenericResponse> getCNCHoldings(ClinetInfoModel info) {

		try {

			/** Get user session from cache **/
			String userSession = AppUtil.getUserSession(info.getUserId());
			if (StringUtil.isNullOrEmpty(userSession))
				return prepareResponse.prepareUnauthorizedResponse();

			/** Prepare holding request **/
			HoldingsReqModel reqModel = new HoldingsReqModel();
			reqModel.setProduct(AppConstants.REST_PRODUCT_CNC);
			String request = prepareHodingRequest(reqModel, userSession, info);
			if (StringUtil.isNullOrEmpty(request))
				return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);

			/** Get holding data from kambal API **/
			return restService.getHoldings(request, AppConstants.PRODUCT_CNC, info.getUserId());
		} catch (Exception e) {
			Log.error(e.getMessage());
			e.printStackTrace();
		}

		return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);

	}

	/**
	 * Method to get holdings for MTF product
	 * 
	 * @author DINESH KUMAR
	 *
	 * @param info
	 * @return
	 */
	@Override
	public RestResponse<GenericResponse> getMTFHoldings(ClinetInfoModel info) {

		try {

			/** Get user session from cache **/
			String userSession = AppUtil.getUserSession(info.getUserId());
			if (StringUtil.isNullOrEmpty(userSession))
				return prepareResponse.prepareUnauthorizedResponse();

			/** Prepare holding request **/
			HoldingsReqModel reqModel = new HoldingsReqModel();
			reqModel.setProduct(AppConstants.REST_PRODUCT_MTF);
			String request = prepareHodingRequest(reqModel, userSession, info);
			if (StringUtil.isNullOrEmpty(request))
				return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);

			/** Get holding data from kambal API **/
			return restService.getHoldings(request, AppConstants.PRODUCT_MTF, info.getUserId());
		} catch (Exception e) {
			Log.error(e.getMessage());
			e.printStackTrace();
		}

		return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);

	}

	/**
	 * Method to prepare holding request
	 * 
	 * @author Dinesh Kumar
	 *
	 * @param reqModel
	 * @param userSession
	 * @return
	 */
	private String prepareHodingRequest(HoldingsReqModel reqModel, String userSession, ClinetInfoModel info) {
		String request = "";
		try {
			RestHoldingsReqModel model = new RestHoldingsReqModel();
			model.setUid(info.getUcc());
			model.setActid(info.getUcc());
			if (reqModel != null && StringUtil.isNotNullOrEmpty(reqModel.getProduct())) {
				model.setPrd(reqModel.getProduct());
			}
			String json = mapper.writeValueAsString(model);
			request = AppConstants.JDATA + AppConstants.SYMBOL_EQUAL + json + AppConstants.SYMBOL_AND
					+ AppConstants.JKEY + AppConstants.SYMBOL_EQUAL + userSession;
		} catch (Exception e) {
			e.printStackTrace();
			Log.error(e.getMessage());
		}
		return request;
	}

	/**
	 * 
	 * Method to get holdings data by product
	 * 
	 * @author Dinesh Kumar
	 *
	 * @param reqModel
	 * @param info
	 * @return
	 */
	@Override
	public RestResponse<GenericResponse> getHoldingsByProduct(HoldingsReqModel reqModel, ClinetInfoModel info) {

		try {

			/** To validate request **/
			if (!validateRequest(reqModel))
				return prepareResponse.prepareFailedResponse(AppConstants.INVALID_PARAMETER);

			/** Get user session from cache **/
			String userSession = AppUtil.getUserSession(info.getUserId());
			if (StringUtil.isNullOrEmpty(userSession))
				return prepareResponse.prepareUnauthorizedResponse();

			/** Prepare holding request **/
			String request = prepareHodingRequest(reqModel, userSession, info);
			if (StringUtil.isNullOrEmpty(request))
				return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);

			/** Get holding data from kambal API **/
			return restService.getHoldings(request, reqModel.getProduct(), info.getUserId());// TODO need to check
																								// product
		} catch (Exception e) {
			Log.error(e.getMessage());
			e.printStackTrace();
		}

		return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);

	}

	/**
	 * To validate the request
	 * 
	 * @author Dinesh Kumar
	 *
	 * @param reqModel
	 * @return
	 */
	private boolean validateRequest(HoldingsReqModel reqModel) {
		if (StringUtil.isNotNullOrEmpty(reqModel.getProduct())) {
			return true;
		}
		return false;
	}

	/**
	 * Method to get holdings for CNC product
	 * 
	 * @author DINESH KUMAR
	 *
	 * @param info
	 * @return
	 */
	@Override
	public RestResponse<GenericResponse> getCNCNonPOAHoldings(ClinetInfoModel info) {

		try {

			/** Get user session from cache **/
			String userSession = AppUtil.getUserSession(info.getUserId());
			if (StringUtil.isNullOrEmpty(userSession))
				return prepareResponse.prepareUnauthorizedResponse();

			/** Prepare holding request **/
			HoldingsReqModel reqModel = new HoldingsReqModel();
			reqModel.setProduct(AppConstants.REST_PRODUCT_CNC);
			String request = prepareNonPoaHodingRequest(reqModel, userSession, info);
			if (StringUtil.isNullOrEmpty(request))
				return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);

			/** Get holding data from kambal API **/
			return restService.getNonPoaHoldings(request, AppConstants.PRODUCT_CNC, info.getUserId());
		} catch (Exception e) {
			Log.error(e.getMessage());
			e.printStackTrace();
		}

		return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);

	}

	/**
	 * Method to get holdings for MTF product
	 * 
	 * @author DINESH KUMAR
	 *
	 * @param info
	 * @return
	 */
	@Override
	public RestResponse<GenericResponse> getMTFNonPOAHoldings(ClinetInfoModel info) {

		try {

			/** Get user session from cache **/
			String userSession = AppUtil.getUserSession(info.getUserId());
			if (StringUtil.isNullOrEmpty(userSession))
				return prepareResponse.prepareUnauthorizedResponse();

			/** Prepare holding request **/
			HoldingsReqModel reqModel = new HoldingsReqModel();
			reqModel.setProduct(AppConstants.REST_PRODUCT_MTF);
			String request = prepareNonPoaHodingRequest(reqModel, userSession, info);
			if (StringUtil.isNullOrEmpty(request))
				return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);

			/** Get holding data from kambal API **/
			return restService.getNonPoaHoldings(request, AppConstants.PRODUCT_MTF, info.getUserId());
		} catch (Exception e) {
			Log.error(e.getMessage());
			e.printStackTrace();
		}

		return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);

	}

	/**
	 * Method to prepare holding request
	 * 
	 * @author Dinesh Kumar
	 *
	 * @param reqModel
	 * @param userSession
	 * @return
	 */
	private String prepareNonPoaHodingRequest(HoldingsReqModel reqModel, String userSession, ClinetInfoModel info) {
		String request = "";
		try {
			RestNonPoaHoldingsReqModel model = new RestNonPoaHoldingsReqModel();
			model.setUid(info.getUcc());
			model.setActid(info.getUcc());
			if (reqModel != null && StringUtil.isNotNullOrEmpty(reqModel.getProduct())) {
				model.setPrd(reqModel.getProduct());
			}
			String json = mapper.writeValueAsString(model);
			request = AppConstants.JDATA + AppConstants.SYMBOL_EQUAL + json + AppConstants.SYMBOL_AND
					+ AppConstants.JKEY + AppConstants.SYMBOL_EQUAL + userSession;
		} catch (Exception e) {
			e.printStackTrace();
			Log.error(e.getMessage());
		}
		return request;
	}
}