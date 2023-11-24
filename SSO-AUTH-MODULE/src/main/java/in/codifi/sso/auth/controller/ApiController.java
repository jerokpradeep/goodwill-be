package in.codifi.sso.auth.controller;

import javax.inject.Inject;
import javax.ws.rs.Path;

import org.jboss.resteasy.reactive.RestResponse;

import in.codifi.cache.model.ClinetInfoModel;
import in.codifi.sso.auth.controller.spec.ApiControllerSpec;
import in.codifi.sso.auth.model.request.ApiKeyReqModel;
import in.codifi.sso.auth.model.response.GenericResponse;
import in.codifi.sso.auth.service.spec.ApiServiceSpec;
import in.codifi.sso.auth.utility.AppConstants;
import in.codifi.sso.auth.utility.AppUtil;
import in.codifi.sso.auth.utility.PrepareResponse;
import in.codifi.sso.auth.utility.StringUtil;
import io.quarkus.logging.Log;

@Path("/api")
public class ApiController implements ApiControllerSpec {

	@Inject
	AppUtil appUtil;

	@Inject
	PrepareResponse prepareResponse;

	@Inject
	ApiServiceSpec serviceSpec;

	/**
	 * Method to get API Key
	 * 
	 * @author Dinesh Kumar
	 * @return
	 */
	@Override
	public RestResponse<GenericResponse> getApiKey() {
		ClinetInfoModel info = appUtil.getClientInfo();
		if (info == null || StringUtil.isNullOrEmpty(info.getUserId())) {
			Log.error("Client info is null");
			return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
		} else if (StringUtil.isNullOrEmpty(info.getUcc())) {
			return prepareResponse.prepareFailedResponse(AppConstants.GUEST_USER_ERROR);
		}
		return serviceSpec.getApiKey(info.getUserId());
	}

	/**
	 * Method to generate API Key
	 * 
	 * @author Dinesh Kumar
	 * @return
	 */
	@Override
	public RestResponse<GenericResponse> generateApiKey(ApiKeyReqModel req) {
		ClinetInfoModel info = appUtil.getClientInfo();
		if (info == null || StringUtil.isNullOrEmpty(info.getUserId())) {
			Log.error("Client info is null");
			return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
		} else if (StringUtil.isNullOrEmpty(info.getUcc())) {
			return prepareResponse.prepareFailedResponse(AppConstants.GUEST_USER_ERROR);
		}
		req.setUserId(info.getUserId());
		return serviceSpec.generateApiKey(req);
	}

	/**
	 * method to regenerate api key
	 * 
	 * @author SowmiyaThangaraj
	 */
	@Override
	public RestResponse<GenericResponse> reGenerateApiKey(ApiKeyReqModel req) {
		ClinetInfoModel info = appUtil.getClientInfo();
		if (info == null || StringUtil.isNullOrEmpty(info.getUserId())) {
			Log.error("Client info is null");
			return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
		} else if (StringUtil.isNullOrEmpty(info.getUcc())) {
			return prepareResponse.prepareFailedResponse(AppConstants.GUEST_USER_ERROR);
		}
		req.setUserId(info.getUserId());
		return serviceSpec.reGenerateApiKey(req);
	}

}
