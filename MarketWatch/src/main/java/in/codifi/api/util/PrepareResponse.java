package in.codifi.api.util;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.core.Response.Status;

import org.jboss.resteasy.reactive.RestResponse;
import org.json.simple.JSONArray;

import in.codifi.api.model.ResponseModel;

/**
 * Class to prepare common response
 * 
 * @author Dinesh Kumar
 *
 */
@ApplicationScoped
public class PrepareResponse {

	/**
	 * Common method for Response
	 *
	 * @param errorMessage
	 * @return
	 */
	public RestResponse<ResponseModel> prepareFailedResponse(String errorMessage) {

		ResponseModel responseObject = new ResponseModel();
		responseObject.setResult(AppConstants.EMPTY_ARRAY);
		responseObject.setStatus(AppConstants.STAT_NOT_OK);
		responseObject.setMessage(errorMessage);
		return RestResponse.ResponseBuilder.create(Status.OK, responseObject).build();
	}

	@SuppressWarnings("unchecked")
	private List<Object> getResult(Object resultData) {
		List<Object> result = new ArrayList<>();
		if (resultData instanceof JSONArray || resultData instanceof List) {
			result = (List<Object>) resultData;
		} else {
			result.add(resultData);
		}
		return result;
	}

	/**
	 * Common method to Success Response
	 *
	 * @param resultData
	 * @return
	 */
	public RestResponse<ResponseModel> prepareSuccessResponseObject(Object resultData) {
		ResponseModel responseObject = new ResponseModel();
		responseObject.setResult(getResult(resultData));
		responseObject.setStatus(AppConstants.STATUS_OK);
		responseObject.setMessage(AppConstants.SUCCESS_STATUS);
		return RestResponse.ResponseBuilder.create(Status.OK, responseObject).build();
	}

	/**
	 * Common method to Success Response
	 *
	 * @param resultData
	 * @return
	 */
	public RestResponse<ResponseModel> prepareSuccessResponseWithMessage(Object resultData, String message) {
		ResponseModel responseObject = new ResponseModel();
		responseObject.setResult(getResult(resultData));
		responseObject.setStatus(message);
		return RestResponse.ResponseBuilder.create(Status.OK, responseObject).build();
	}

	public RestResponse<ResponseModel> prepareResponse(Object resultData) {
		return RestResponse.ResponseBuilder.create(Status.OK, (ResponseModel) resultData).build();
	}

	/**
	 * Common method to Success Response
	 *
	 * @param resultData
	 * @return
	 */
	public RestResponse<ResponseModel> prepareSuccessMessage(String message) {
		ResponseModel responseObject = new ResponseModel();
//		responseObject.setResult(AppConstants.EMPTY_ARRAY);
		responseObject.setStatus(AppConstants.STATUS_OK);
		responseObject.setMessage(message);
		return RestResponse.ResponseBuilder.create(Status.OK, responseObject).build();
	}

}
