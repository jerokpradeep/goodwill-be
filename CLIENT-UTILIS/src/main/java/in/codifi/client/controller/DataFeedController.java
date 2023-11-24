package in.codifi.client.controller;

import javax.inject.Inject;
import javax.ws.rs.Path;

import org.jboss.resteasy.reactive.RestResponse;

import in.codifi.cache.model.ClinetInfoModel;
import in.codifi.client.controller.spec.IDataFeedController;
import in.codifi.client.model.request.ClientDetailsReqModel;
import in.codifi.client.model.response.GenericResponse;
import in.codifi.client.service.spec.IDataFeedService;
import in.codifi.client.utilis.AppConstants;
import in.codifi.client.utilis.AppUtil;
import in.codifi.client.utilis.PrepareResponse;
import in.codifi.client.utilis.StringUtil;
import io.quarkus.logging.Log;

@Path("/data")
public class DataFeedController implements IDataFeedController {

	@Inject
	AppUtil appUtil;
	@Inject
	PrepareResponse prepareResponse;
	@Inject
	IDataFeedService dataFeedService;

	/**
	 * Method to get stocks for client
	 * 
	 * @author GOWTHAMAN M
	 * @return
	 */
	@Override
	public RestResponse<GenericResponse> getStocksForUser(ClientDetailsReqModel model) {
		ClinetInfoModel info = appUtil.getClientInfo();
		if (info == null || StringUtil.isNullOrEmpty(info.getUserId())) {
			Log.error("Client info is null");
			return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
		} else if (StringUtil.isNullOrEmpty(info.getUcc())) {
			return prepareResponse.prepareFailedResponse(AppConstants.GUEST_USER_ERROR);
		}
		return dataFeedService.getStocksForUser(model);
	}

}
