package in.codifi.auth.controller;

import javax.inject.Inject;
import javax.ws.rs.Path;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MultivaluedMap;

import org.jboss.resteasy.reactive.RestResponse;

import in.codifi.auth.controller.spec.AuthSecuredControllerSpec;
import in.codifi.auth.model.request.AuthReq;
import in.codifi.auth.model.response.GenericResponse;
import in.codifi.auth.servcie.spec.AuthServiceSpec;
import in.codifi.auth.utility.AppConstants;
import in.codifi.auth.utility.AppUtils;
import in.codifi.auth.utility.PrepareResponse;
import in.codifi.auth.utility.StringUtil;
import in.codifi.cache.model.ClinetInfoModel;
import io.quarkus.logging.Log;

@Path("/custaccess")
public class AuthSecuredController implements AuthSecuredControllerSpec {

	@Inject
	AuthServiceSpec authServiceSpec;

	@Inject
	PrepareResponse prepareResponse;

	@Inject
	AppUtils appUtils;
	
	@Context
	ContainerRequestContext request;

	/**
	 * Method to logout
	 * 
	 * @author Dinesh Kumar
	 * @return
	 */
	@Override
	public RestResponse<GenericResponse> logout(AuthReq authReq) {
		ClinetInfoModel info = appUtils.getClientInfo();
		if (info == null || StringUtil.isNullOrEmpty(info.getUserId())) {
			Log.error("Client info is null");
			return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
		}
		authReq.setUserId(info.getUserId());
		return authServiceSpec.logout(authReq);
	}

	/**
	 * Method to re login
	 * 
	 * @author Dinesh Kumar
	 * @return
	 */
	@Override
	public RestResponse<GenericResponse> reLogin(AuthReq authReq) {
		ClinetInfoModel info = appUtils.getClientInfo();
		if (info == null || StringUtil.isNullOrEmpty(info.getUserId())) {
			Log.error("Client info is null");
			return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
		}
		MultivaluedMap<String, String> headers = request.getHeaders();
		String deviceIp = headers.getFirst(AppConstants.X_FORWARDED_FOR);
		authReq.setUserId(info.getUserId());
		return authServiceSpec.reLogin(authReq,deviceIp);
	}
}
