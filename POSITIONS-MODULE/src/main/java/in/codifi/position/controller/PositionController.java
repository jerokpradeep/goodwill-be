package in.codifi.position.controller;

import javax.inject.Inject;
import javax.ws.rs.Path;

import org.jboss.resteasy.reactive.RestResponse;

import in.codifi.cache.model.ClinetInfoModel;
import in.codifi.position.controller.spec.PositionControllerSpecs;
import in.codifi.position.model.request.PositionConversionReq;
import in.codifi.position.model.response.GenericResponse;
import in.codifi.position.service.spec.PositionServiceSpec;
import in.codifi.position.utility.AppConstants;
import in.codifi.position.utility.AppUtil;
import in.codifi.position.utility.PrepareResponse;
import in.codifi.position.utility.StringUtil;
import io.quarkus.logging.Log;

@Path("/positions")
public class PositionController implements PositionControllerSpecs {

	@Inject
	PositionServiceSpec positionService;
	@Inject
	PrepareResponse prepareResponse;
	@Inject
	AppUtil appUtil;

	/**
	 * Method to get the position
	 * 
	 * @author Nesan
	 *
	 */

	@Override
	public RestResponse<GenericResponse> getposition() {

		ClinetInfoModel info = appUtil.getClientInfo();
		if (info == null || StringUtil.isNullOrEmpty(info.getUserId())) {
			Log.error("Client info is null");
			return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
		} else if (StringUtil.isNullOrEmpty(info.getUcc())) {
			return prepareResponse.prepareFailedResponse(AppConstants.GUEST_USER_ERROR);
		}
		return positionService.getposition(info);
	}

	/**
	 * Method to get the position conversion
	 * 
	 * @author Nesan
	 *
	 */
	@Override
	public RestResponse<GenericResponse> positionConversion(PositionConversionReq positionConversionReq) {

		ClinetInfoModel info = appUtil.getClientInfo();
		if (info == null || StringUtil.isNullOrEmpty(info.getUserId())) {
			Log.error("Client info is null");
			return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
		} else if (StringUtil.isNullOrEmpty(info.getUcc())) {
			return prepareResponse.prepareFailedResponse(AppConstants.GUEST_USER_ERROR);
		}

		return positionService.positionConversion(positionConversionReq, info);
	}

}
