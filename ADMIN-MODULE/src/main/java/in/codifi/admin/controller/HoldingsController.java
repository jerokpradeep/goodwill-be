package in.codifi.admin.controller;

import javax.inject.Inject;
import javax.ws.rs.Path;

import org.jboss.resteasy.reactive.RestResponse;

import in.codifi.admin.controller.spec.HoldingsControllerSpec;
import in.codifi.admin.model.request.FormDataModel;
import in.codifi.admin.model.request.HoldingsReqModel;
import in.codifi.admin.model.response.GenericResponse;
import in.codifi.admin.service.spec.HoldingsServiceSpec;
import in.codifi.admin.utility.AppConstants;
import in.codifi.admin.utility.PrepareResponse;
import in.codifi.admin.utility.StringUtil;

@Path("/holdings")
public class HoldingsController implements HoldingsControllerSpec {
	@Inject
	HoldingsServiceSpec holdingsService;
	@Inject
	PrepareResponse prepareResponse;

	/**
	 * 
	 * Method to upload holdings file
	 *
	 * @author SOWMIYA
	 *
	 * @param file
	 * @return
	 */
	public RestResponse<GenericResponse> uploadHoldingsFile(FormDataModel file) {
		if (file == null || StringUtil.isNullOrEmpty(file.getType()))
			return prepareResponse.prepareFailedResponse(AppConstants.INVALID_PARAMETERS);
		return holdingsService.uploadHoldingsFile(file, file.getType());

	}

	/**
	 * 
	 * Method to insert holdings file
	 *
	 * @author SOWMIYA
	 *
	 * @param file
	 * @return
	 */
	public RestResponse<GenericResponse> insertHoldingsFile() {
		return holdingsService.insertHoldingsFile();

	}

	/**
	 * 
	 * Method to load holdings data
	 *
	 * @author SOWMIYA
	 *
	 * @return
	 */
//	@Override
//	public RestResponse<GenericResponse> loadHoldingsData() {
//		return holdingsService.loadHoldingsData();
//	}

	/**
	 * method to get holdings data
	 * 
	 * @author SOWMIYA
	 */
	public RestResponse<GenericResponse> getHoldingsData(HoldingsReqModel reqModel) {
		return holdingsService.getHoldingsData(reqModel);
	}

	/**
	 * method to get holdings count
	 * 
	 * @author SOWMIYA
	 * @return
	 */
	public RestResponse<GenericResponse> getHoldingsCount() {
		return holdingsService.getHoldingsCount();
	}

}
