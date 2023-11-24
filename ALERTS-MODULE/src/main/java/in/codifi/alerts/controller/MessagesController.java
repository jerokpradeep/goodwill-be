package in.codifi.alerts.controller;

import javax.inject.Inject;
import javax.ws.rs.Path;

import org.jboss.resteasy.reactive.RestResponse;

import in.codifi.alerts.controller.spec.MessageControllerSpec;
import in.codifi.alerts.model.response.GenericResponse;
import in.codifi.alerts.service.spec.MessagesServiceSpec;
import in.codifi.alerts.utility.AppConstants;
import in.codifi.alerts.utility.AppUtil;
import in.codifi.alerts.utility.PrepareResponse;
import in.codifi.alerts.utility.StringUtil;
import in.codifi.cache.model.ClinetInfoModel;
import io.quarkus.logging.Log;

@Path("/messages")
public class MessagesController implements MessageControllerSpec {

	@Inject
	MessagesServiceSpec service;
	@Inject
	PrepareResponse prepareResponse;
	@Inject
	AppUtil appUtil;

	/**
	 * method to get exch message
	 * 
	 * @author SOWMIYA
	 * @return
	 */
	@Override
	public RestResponse<GenericResponse> exchMsg(String exch) {
		ClinetInfoModel info = appUtil.getClientInfo();
		if (info == null || StringUtil.isNullOrEmpty(info.getUserId())) {
			Log.error("Client info is null");
			return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
		} else if (StringUtil.isNullOrEmpty(info.getUcc())) {
			return prepareResponse.prepareFailedResponse(AppConstants.GUEST_USER_ERROR);
		}
		return service.exchMsg(info, exch);
	}

	/**
	 * method to get brokerage message
	 * 
	 * @author SOWMIYA
	 * @return
	 */
	@Override
	public RestResponse<GenericResponse> getBrokerageMsg() {
		ClinetInfoModel info = appUtil.getClientInfo();
		if (info == null || StringUtil.isNullOrEmpty(info.getUserId())) {
			Log.error("Client info is null");
			return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
		} else if (StringUtil.isNullOrEmpty(info.getUcc())) {
			return prepareResponse.prepareFailedResponse(AppConstants.GUEST_USER_ERROR);
		}
		return service.getBrokerageMsg(info);
	}

	/**
	 * method to get exchange status
	 * 
	 * @author SOWMIYA
	 * @return
	 */
	@Override
	public RestResponse<GenericResponse> getExchStatus() {
		ClinetInfoModel info = appUtil.getClientInfo();
		if (info == null || StringUtil.isNullOrEmpty(info.getUserId())) {
			Log.error("Client info is null");
			return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
		} else if (StringUtil.isNullOrEmpty(info.getUcc())) {
			return prepareResponse.prepareFailedResponse(AppConstants.GUEST_USER_ERROR);
		}
		return service.getExchStatus(info);
	}

}
