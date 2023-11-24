package in.codifi.funds.service;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.jboss.resteasy.reactive.RestResponse;

import com.fasterxml.jackson.databind.ObjectMapper;

import in.codifi.cache.model.ClinetInfoModel;
import in.codifi.funds.model.response.GenericResponse;
import in.codifi.funds.service.spec.FundsServiceSpec;
import in.codifi.funds.utility.AppConstants;
import in.codifi.funds.utility.AppUtil;
import in.codifi.funds.utility.PrepareResponse;
import in.codifi.funds.utility.StringUtil;
import in.codifi.funds.ws.model.RestLimitsReq;
import in.codifi.funds.ws.service.FundsRestService;
import io.quarkus.logging.Log;

@ApplicationScoped
public class FundsService implements FundsServiceSpec {

	@Inject
	PrepareResponse prepareResponse;

	@Inject
	FundsRestService restService;

	/*
	 * method to get limits
	 * 
	 * @author SOWMIYA
	 * 
	 * @return
	 */

	@Override
	public RestResponse<GenericResponse> getLimits(ClinetInfoModel info) {

		try {

			/** Get user session from cache **/
			String userSession = AppUtil.getUserSession(info.getUserId());
			if (StringUtil.isNullOrEmpty(userSession))
				return prepareResponse.prepareUnauthorizedResponse();

			/** prepare request body */
			String request = prepareLimitsRequest(info, userSession);
			if (StringUtil.isNullOrEmpty(request))
				return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
			return restService.getLimits(request, info.getUserId());

		} catch (Exception e) {
			e.printStackTrace();
			Log.error(e.getMessage());
		}

		return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
	}

	/**
	 * Method to get commodity limits
	 * 
	 * @author DINESH KUMAR
	 *
	 * @param info
	 * @return
	 */
	@Override
	public RestResponse<GenericResponse> getCommodityLimits(ClinetInfoModel info) {

		try {

			/** Get user session from cache **/
			String userSession = AppUtil.getUserSession(info.getUserId());
			if (StringUtil.isNullOrEmpty(userSession))
				return prepareResponse.prepareUnauthorizedResponse();

			/** prepare request body */
			String request = prepareCommodityLimitsRequest(info, userSession);
			if (StringUtil.isNullOrEmpty(request))
				return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
			return restService.getLimits(request, info.getUserId());

		} catch (Exception e) {
			e.printStackTrace();
			Log.error(e.getMessage());
		}

		return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
	}

	/**
	 * Method to prepare limits request
	 * 
	 * @author DINESH KUMAR
	 *
	 * @param model
	 * @param userSession
	 * @return
	 */
	private String prepareLimitsRequest(ClinetInfoModel model, String userSession) {
		String request = "";
		try {
			ObjectMapper mapper = new ObjectMapper();
			RestLimitsReq reqModel = new RestLimitsReq();
			reqModel.setUid(model.getUserId());
			reqModel.setActid(model.getUserId());
			String json = mapper.writeValueAsString(reqModel);
			request = AppConstants.JDATA + AppConstants.SYMBOL_EQUAL + json + AppConstants.SYMBOL_AND
					+ AppConstants.JKEY + AppConstants.SYMBOL_EQUAL + userSession;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return request;
	}

	/**
	 * Method to prepare commodity limits request
	 * 
	 * @author DINESH KUMAR
	 *
	 * @param model
	 * @param userSession
	 * @return
	 */
	private String prepareCommodityLimitsRequest(ClinetInfoModel model, String userSession) {
		String request = "";
		try {
			ObjectMapper mapper = new ObjectMapper();
			RestLimitsReq reqModel = new RestLimitsReq();
			reqModel.setUid(model.getUserId());
			reqModel.setActid(model.getUserId());
			reqModel.setSeg("COM");
			String json = mapper.writeValueAsString(reqModel);
			request = AppConstants.JDATA + AppConstants.SYMBOL_EQUAL + json + AppConstants.SYMBOL_AND
					+ AppConstants.JKEY + AppConstants.SYMBOL_EQUAL + userSession;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return request;
	}
}
