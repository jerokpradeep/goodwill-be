package in.codifi.api.controller;

import javax.inject.Inject;
import javax.ws.rs.Path;

import org.jboss.resteasy.reactive.RestResponse;

import in.codifi.api.controller.spec.ITickerTapeController;
import in.codifi.api.model.ReqModel;
import in.codifi.api.model.ResponseModel;
import in.codifi.api.service.spec.ITickerTapeService;
import in.codifi.api.util.AppConstants;
import in.codifi.api.util.AppUtil;
import in.codifi.api.util.PrepareResponse;

@Path("/ticker-tape")
public class TickerTapeController implements ITickerTapeController {

	@Inject
	ITickerTapeService service;
	@Inject
	PrepareResponse prepareResponse;
	@Inject
	AppUtil utils;

	/**
	 * 
	 * Method to get ticker tape scrips
	 * 
	 * @author Dinesh Kumar
	 *
	 * @param model
	 * @return
	 */
	@Override
	public RestResponse<ResponseModel> getTicketTapeScrips(ReqModel model) {
		try {
			if (model == null)
				return prepareResponse.prepareFailedResponse(AppConstants.INVALID_PARAMETER);
			if (!utils.isValidUser(model.getUserId()))
				return prepareResponse.prepareFailedResponse(AppConstants.INVALID_USER_SESSION);
			return service.getTicketTapeScrips(model);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
	}
}
