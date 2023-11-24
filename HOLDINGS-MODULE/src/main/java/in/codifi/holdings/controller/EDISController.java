package in.codifi.holdings.controller;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.Path;

import org.jboss.resteasy.reactive.RestResponse;

import in.codifi.cache.model.ClinetInfoModel;
import in.codifi.holdings.config.RestServiceProperties;
import in.codifi.holdings.controller.spec.EDISControllerSpec;
import in.codifi.holdings.model.request.EdisReqModel;
import in.codifi.holdings.model.response.GenericResponse;
import in.codifi.holdings.service.spec.EDISServiceSpec;
import in.codifi.holdings.utility.AppConstants;
import in.codifi.holdings.utility.AppUtil;
import in.codifi.holdings.utility.PrepareResponse;
import in.codifi.holdings.utility.StringUtil;
import io.quarkus.logging.Log;

@Path("/edis")
public class EDISController implements EDISControllerSpec {
	@Inject
	EDISServiceSpec service;
	@Inject
	PrepareResponse prepareResponse;
	@Inject
	AppUtil appUtil;
	@Inject
	RestServiceProperties props;

	/**
	 * method to initialize edis request
	 * 
	 * @author SOWMIYA
	 * @return
	 */
	public RestResponse<GenericResponse> initializeEdisRequest(List<EdisReqModel> model) {
		if (model == null || model.size() < 1)
			return prepareResponse.prepareFailedResponse(AppConstants.INVALID_PARAMETER);
		ClinetInfoModel info = appUtil.getClientInfo();
		if (info == null || StringUtil.isNullOrEmpty(info.getUserId())) {
			Log.error("Client info is null");
			return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
		} else if (StringUtil.isNullOrEmpty(info.getUcc())) {
			return prepareResponse.prepareFailedResponse(AppConstants.GUEST_USER_ERROR);
		}
		return service.initializeEdisRequest(model, info);
	}

	/**
	 * method to get hs token
	 * 
	 * @author SOWMIYA
	 * @return
	 */
	public RestResponse<GenericResponse> getHSToken() {

		ClinetInfoModel info = appUtil.getClientInfo();
		if (info == null || StringUtil.isNullOrEmpty(info.getUserId())) {
			Log.error("Client info is null");
			return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
		} else if (StringUtil.isNullOrEmpty(info.getUcc())) {
			return prepareResponse.prepareFailedResponse(AppConstants.GUEST_USER_ERROR);
		}
		return service.getHSToken(info);
	}

}

//	/**
//	 * method to revocation return
//	 * 
//	 * @author SOWMIYA
//	 * @param req
//	 * @return
//	 */
//	public Response revocationReturn(String req, String userId) {
//		java.net.URI location = null;
//		try {
//			service.revocationReturn(req, userId);
//			location = new java.net.URI(props.getRedirectUrl());
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return Response.temporaryRedirect(location).build();
//	}
//}
