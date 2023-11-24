package in.codifi.funds.controller;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.Path;

import org.jboss.resteasy.reactive.RestResponse;

import in.codifi.cache.model.ClinetInfoModel;
import in.codifi.funds.controller.spec.FundsControllerSpec;
import in.codifi.funds.model.response.GenericResponse;
import in.codifi.funds.service.spec.FundsServiceSpec;
import in.codifi.funds.utility.AppConstants;
import in.codifi.funds.utility.AppUtil;
import in.codifi.funds.utility.PrepareResponse;
import in.codifi.funds.utility.StringUtil;
import io.quarkus.logging.Log;

@Path("/funds")
public class FundsController implements FundsControllerSpec {

	@Inject
	FundsServiceSpec fundsService;
	@Inject
	PrepareResponse prepareResponse;
	@Inject
	AppUtil appUtil;

	/**
	 * 
	 * Method to get limits
	 * 
	 * @author SOWMIYA
	 *
	 * @return
	 */
	@Override
	public RestResponse<GenericResponse> getLimits() {
		ClinetInfoModel info = appUtil.getClientInfo();
		if (info == null || StringUtil.isNullOrEmpty(info.getUserId())) {
			Log.error("Client info is null");
			return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
		} else if (StringUtil.isNullOrEmpty(info.getUcc())) {
			return prepareResponse.prepareFailedResponse(AppConstants.GUEST_USER_ERROR);
		}
		return fundsService.getLimits(info);
	}

	/**
	 * Method to get commodity limits
	 * 
	 * @author DINESH KUMAR
	 *
	 * @return
	 */
	@Override
	public RestResponse<GenericResponse> getCommodityLimits() {
		ClinetInfoModel info = appUtil.getClientInfo();
		if (info == null || StringUtil.isNullOrEmpty(info.getUserId())) {
			Log.error("Client info is null");
			return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
		} else if (StringUtil.isNullOrEmpty(info.getUcc())) {
			return prepareResponse.prepareFailedResponse(AppConstants.GUEST_USER_ERROR);
		}
		return fundsService.getCommodityLimits(info);
	}

	private boolean isActiveUser(List<String> clientRoles) {
		boolean isActive = false;
		if (StringUtil.isListNotNullOrEmpty(clientRoles)) {
			for (int i = 0; i < clientRoles.size(); i++) {
				String role = clientRoles.get(i);
				if (role.equalsIgnoreCase(AppConstants.ACTIVE_USER)) {
					isActive = true;
					break;
				}
			}
		}
		return isActive;
	}

}
