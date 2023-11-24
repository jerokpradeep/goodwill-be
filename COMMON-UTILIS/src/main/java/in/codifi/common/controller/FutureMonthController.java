package in.codifi.common.controller;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.Path;

import org.jboss.resteasy.reactive.RestResponse;

import in.codifi.cache.model.ClinetInfoModel;
import in.codifi.common.controller.spec.FutureMonthControllerSpec;
import in.codifi.common.entity.FutureMonthMappingEntity;
import in.codifi.common.model.request.MapReqModel;
import in.codifi.common.model.response.GenericResponse;
import in.codifi.common.service.spec.FutureMonthServiceSpec;
import in.codifi.common.utility.AppConstants;
import in.codifi.common.utility.AppUtil;
import in.codifi.common.utility.PrepareResponse;
import in.codifi.common.utility.StringUtil;
import io.quarkus.logging.Log;

@Path("/futuremonth")
public class FutureMonthController implements FutureMonthControllerSpec {

	@Inject
	PrepareResponse prepareResponse;
	@Inject
	AppUtil appUtil;
	@Inject
	FutureMonthServiceSpec futureMonthService;

	/**
	 * Get future month data details
	 * 
	 * @author SOWMIYA
	 *
	 * @return
	 */
	@Override
	public RestResponse<GenericResponse> getFutureMonthData() {
		ClinetInfoModel info = appUtil.getClientInfo();
		if (info == null || StringUtil.isNullOrEmpty(info.getUserId())) {
			Log.error("Client info is null");
			return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
		}
		return futureMonthService.getFutureMonthData();
	}

	/**
	 * Method to insert future month map data.This is for Admin
	 * 
	 * @author SOWMIYA
	 *
	 * @return
	 */
	@Override
	public RestResponse<GenericResponse> insertFutureMonthData() {
		ClinetInfoModel info = appUtil.getClientInfo();
		if (info == null || StringUtil.isNullOrEmpty(info.getUserId())) {
			Log.error("Client info is null");
			return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
		} else if (StringUtil.isNullOrEmpty(info.getUcc())) {
			return prepareResponse.prepareFailedResponse(AppConstants.GUEST_USER_ERROR);
		}
		return futureMonthService.insertFutureMonthData();
	}

	/**
	 * Method to add scrips in future month mapping scrips. This is for admin
	 * 
	 * @author SOWMIYA
	 *
	 * @param entities
	 * @param info
	 * @return
	 */
	@Override
	public RestResponse<GenericResponse> addMappingScrips(List<FutureMonthMappingEntity> entities) {
		ClinetInfoModel info = appUtil.getClientInfo();
		if (info == null || StringUtil.isNullOrEmpty(info.getUserId())) {
			Log.error("Client info is null");
			return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
		} else if (StringUtil.isNullOrEmpty(info.getUcc())) {
			return prepareResponse.prepareFailedResponse(AppConstants.GUEST_USER_ERROR);
		}
		return futureMonthService.addMappingScrips(entities, info);
	}

	/**
	 * Method to delete futute month mapping scrips. This is for admin
	 * 
	 * @author SOWMIYA
	 *
	 * @param ids
	 * @param info
	 * @return
	 */
	@Override
	public RestResponse<GenericResponse> deleteMappingScrips(MapReqModel request) {
		ClinetInfoModel info = appUtil.getClientInfo();
		if (info == null || StringUtil.isNullOrEmpty(info.getUserId())) {
			Log.error("Client info is null");
			return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
		} else if (StringUtil.isNullOrEmpty(info.getUcc())) {
			return prepareResponse.prepareFailedResponse(AppConstants.GUEST_USER_ERROR);
		}
		return futureMonthService.deleteMappingScrips(request, info);
	}

	/**
	 * Method to get future month mapping scrips. This is for admin
	 * 
	 * @author SOWMIYA
	 *
	 * @return
	 */
	@Override
	public RestResponse<GenericResponse> getMappingScrips() {
		ClinetInfoModel info = appUtil.getClientInfo();
		if (info == null || StringUtil.isNullOrEmpty(info.getUserId())) {
			Log.error("Client info is null");
			return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
		} else if (StringUtil.isNullOrEmpty(info.getUcc())) {
			return prepareResponse.prepareFailedResponse(AppConstants.GUEST_USER_ERROR);
		}
		return futureMonthService.getMappingScrips();
	}

}
