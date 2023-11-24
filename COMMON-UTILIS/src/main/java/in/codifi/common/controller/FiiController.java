package in.codifi.common.controller;

import javax.inject.Inject;
import javax.ws.rs.Path;

import org.jboss.resteasy.reactive.RestResponse;

import in.codifi.cache.model.ClinetInfoModel;
import in.codifi.common.controller.spec.FiiControllerSpec;
import in.codifi.common.model.response.GenericResponse;
import in.codifi.common.service.spec.FiiServiceSpec;
import in.codifi.common.utility.AppConstants;
import in.codifi.common.utility.AppUtil;
import in.codifi.common.utility.PrepareResponse;
import in.codifi.common.utility.StringUtil;
import io.quarkus.logging.Log;

@Path("/fii")
public class FiiController implements FiiControllerSpec {

	@Inject
	PrepareResponse prepareResponse;
	@Inject
	AppUtil appUtil;
	@Inject
	FiiServiceSpec fiiServiceSpec;

	/**
	 * Method to get Fii index futures
	 * 
	 * @author DINESH KUMAR
	 *
	 * @return
	 */
	@Override
	public RestResponse<GenericResponse> getFiiIndexFutureData() {
		ClinetInfoModel info = appUtil.getClientInfo();
		if (info == null || StringUtil.isNullOrEmpty(info.getUserId())) {
			Log.error("Client info is null");
			return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
		}
		return fiiServiceSpec.getFiiIndexFutureData();
	}
}
