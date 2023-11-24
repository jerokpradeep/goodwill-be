package in.codifi.alerts.service;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.jboss.resteasy.reactive.RestResponse;

import com.fasterxml.jackson.databind.ObjectMapper;

import in.codifi.alerts.model.response.GenericResponse;
import in.codifi.alerts.service.spec.MessagesServiceSpec;
import in.codifi.alerts.utility.AppConstants;
import in.codifi.alerts.utility.AppUtil;
import in.codifi.alerts.utility.PrepareResponse;
import in.codifi.alerts.utility.StringUtil;
import in.codifi.alerts.ws.model.RestBrokerMsgReqModel;
import in.codifi.alerts.ws.model.RestExchMsgReqModel;
import in.codifi.alerts.ws.model.RestExchStatusRequestModel;
import in.codifi.alerts.ws.service.MessagesRestService;
import in.codifi.cache.model.ClinetInfoModel;

@ApplicationScoped
public class MessagesService implements MessagesServiceSpec {

	@Inject
	PrepareResponse prepareResponse;
	@Inject
	MessagesRestService restService;

	/**
	 * method to get exchange message
	 * 
	 * @author SOWMIYA
	 */
	@Override
	public RestResponse<GenericResponse> exchMsg(ClinetInfoModel info, String exch) {
		if (StringUtil.isNullOrEmpty(exch))
			return prepareResponse.prepareFailedResponse(AppConstants.INVALID_PARAMETER);
		try {
			/** Get user session from cache **/
			String userSession = AppUtil.getUserSession(info.getUserId());
			if (StringUtil.isNullOrEmpty(userSession))
				return prepareResponse.prepareUnauthorizedResponse();

			/** method to prepare exchange request message **/
			String request = prepareExchMsgReq(userSession, info, exch);

			if (StringUtil.isNullOrEmpty(request))
				return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);

			return restService.getExchMsg(request, info.getUserId());

		} catch (Exception e) {
			e.printStackTrace();
		}
		return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
	}

	/**
	 * method to prepare exchange messages
	 * 
	 * @author SOWMIYA
	 * @param info
	 */
	private String prepareExchMsgReq(String userSession, ClinetInfoModel info, String exch) {
		ObjectMapper mapper = new ObjectMapper();
		String request = "";
		try {
			RestExchMsgReqModel model = new RestExchMsgReqModel();
			model.setUid(info.getUserId());
			model.setExch(exch);
			String json = mapper.writeValueAsString(model);
			request = AppConstants.JDATA + AppConstants.SYMBOL_EQUAL + json + AppConstants.SYMBOL_AND
					+ AppConstants.JKEY + AppConstants.SYMBOL_EQUAL + userSession;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return request;

	}

	@Override
	public RestResponse<GenericResponse> getBrokerageMsg(ClinetInfoModel info) {
		try {
			/** Get user session from cache **/
			String userSession = AppUtil.getUserSession(info.getUserId());
			if (StringUtil.isNullOrEmpty(userSession))
				return prepareResponse.prepareUnauthorizedResponse();

			/** method to prepare exchange request message **/
			String request = prepareBrokerageMsgReq(userSession, info);

			if (StringUtil.isNullOrEmpty(request))
				return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);

			return restService.getBrokerageMsg(request, info.getUserId());

		} catch (Exception e) {
			e.printStackTrace();
		}
		return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
	}

	/**
	 * method to get brokerage message
	 * 
	 * @author SOWMIYA
	 * @param userSession
	 * @param info
	 * @return
	 */
	private String prepareBrokerageMsgReq(String userSession, ClinetInfoModel info) {
		String request = "";
		ObjectMapper mapper = new ObjectMapper();
		try {
			RestBrokerMsgReqModel model = new RestBrokerMsgReqModel();
			model.setUid(info.getUserId());
			String json = mapper.writeValueAsString(model);
			request = AppConstants.JDATA + AppConstants.SYMBOL_EQUAL + json + AppConstants.SYMBOL_AND
					+ AppConstants.JKEY + AppConstants.SYMBOL_EQUAL + userSession;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return request;
	}

	/**
	 * method to get exchange status
	 * 
	 * @author SOWMIYA
	 */
	@Override
	public RestResponse<GenericResponse> getExchStatus(ClinetInfoModel info) {
		try {
			/** Get user session from cache **/
			String userSession = AppUtil.getUserSession(info.getUserId());
			if (StringUtil.isNullOrEmpty(userSession))
				return prepareResponse.prepareUnauthorizedResponse();

			/** method to prepare exchange status request message **/
			String request = prepareExchStatusReq(userSession, info);

			if (StringUtil.isNullOrEmpty(request))
				return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);

			return restService.getExchStatus(request, info.getUserId());

		} catch (Exception e) {
			e.printStackTrace();
		}
		return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
	}

	/**
	 * method to prepare exchange status request
	 * 
	 * @author SOWMIYA
	 * 
	 * @param userSession
	 * @param info
	 * @return
	 */
	private String prepareExchStatusReq(String userSession, ClinetInfoModel info) {
		String request = "";
		ObjectMapper mapper = new ObjectMapper();
		try {
			RestExchStatusRequestModel model = new RestExchStatusRequestModel();
			model.setUid(info.getUserId());
			String json = mapper.writeValueAsString(model);
			request = AppConstants.JDATA + AppConstants.SYMBOL_EQUAL + json + AppConstants.SYMBOL_AND
					+ AppConstants.JKEY + AppConstants.SYMBOL_EQUAL + userSession;

		} catch (Exception e) {
			e.printStackTrace();

		}

		return request;
	}

}
