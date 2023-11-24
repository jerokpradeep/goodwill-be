package in.codifi.common.controller;

import javax.inject.Inject;
import javax.ws.rs.Path;

import org.jboss.resteasy.reactive.RestResponse;

import in.codifi.cache.model.ClinetInfoModel;
import in.codifi.common.controller.spec.MarketingControllerSpec;
import in.codifi.common.model.response.GenericResponse;
import in.codifi.common.service.spec.IndicesServiceSpec;
import in.codifi.common.service.spec.MarketingServiceSpec;
import in.codifi.common.utility.AppConstants;
import in.codifi.common.utility.AppUtil;
import in.codifi.common.utility.PrepareResponse;
import in.codifi.common.utility.StringUtil;
import io.quarkus.logging.Log;

@Path("/marketing")
public class MarketingController implements MarketingControllerSpec {

	@Inject
	MarketingServiceSpec marketingService;
	@Inject
	IndicesServiceSpec indicesService;
	@Inject
	PrepareResponse prepareResponse;
	@Inject
	AppUtil appUtil;

	/*
	 * method to get Marketing data using findAll()
	 * 
	 * @author SOWMIYA
	 * 
	 * @return
	 */
	@Override
	public RestResponse<GenericResponse> getMarketingData() {
		ClinetInfoModel info = appUtil.getClientInfo();
		if (info == null || StringUtil.isNullOrEmpty(info.getUserId())) {
			Log.error("Client info is null");
			return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
		} else if (StringUtil.isNullOrEmpty(info.getUcc())) {
			return prepareResponse.prepareFailedResponse(AppConstants.GUEST_USER_ERROR);
		}
		return marketingService.getMarketingData();
	}

}
