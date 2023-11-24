package in.codifi.admin.controller;

import javax.inject.Inject;
import javax.ws.rs.Path;

import org.jboss.resteasy.reactive.RestResponse;

import in.codifi.admin.controller.spec.PositionControllerSpec;
import in.codifi.admin.model.request.FormDataModel;
import in.codifi.admin.model.request.PositionReqModel;
import in.codifi.admin.model.response.GenericResponse;
import in.codifi.admin.service.spec.PositionServiceSpec;
import in.codifi.admin.utility.AppConstants;
import in.codifi.admin.utility.PrepareResponse;
import in.codifi.admin.utility.StringUtil;

@Path("/position")
public class PositionController implements PositionControllerSpec {

	@Inject
	PositionServiceSpec positionService;
	@Inject
	PrepareResponse prepareResponse;

	/**
	 * 
	 * Method to upload position file
	 *
	 * @author SOWMIYA
	 *
	 * @param file
	 * @return
	 */
	public RestResponse<GenericResponse> uploadPositionFile(FormDataModel file) {
		if (file == null || StringUtil.isNullOrEmpty(file.getExchange()))
			return prepareResponse.prepareFailedResponse(AppConstants.INVALID_PARAMETERS);
		return positionService.uploadPositionFile(file, file.getExchange());
	}

	/**
	 * 
	 * Method to insert position file
	 *
	 * @author SOWMIYA
	 *
	 * @param file
	 * @return
	 */
	public RestResponse<GenericResponse> insertPositionFile() {
		return positionService.insertPositionFile();

	}

	/**
	 * method to get positionAvg price
	 * 
	 * @author SOWMIYA
	 * @param reqModel
	 * @return
	 */
	public RestResponse<GenericResponse> getPositionAvgUser(PositionReqModel reqModel) {
		return positionService.getPositionAvgUser(reqModel);
	}

	/**
	 * method to get position count by exchange
	 * 
	 * @author SOWMIYA
	 * @return
	 */
	public RestResponse<GenericResponse> getPositionCountByExch() {
		return positionService.getPositionCountByExch();
	}

}
