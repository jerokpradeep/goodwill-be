package in.codifi.basket.utils;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Response.Status;

import org.jboss.resteasy.reactive.RestResponse;
import org.json.simple.JSONArray;
import org.springframework.stereotype.Component;

import in.codifi.basket.model.response.GenericResponse;

@Component
public class PrepareResponse {

	/**
	 * Common method for Response
	 *
	 * @param errorMessage
	 * @return
	 */
	public RestResponse<GenericResponse> prepareFailedResponse(String errorMessage) {

		GenericResponse responseObject = new GenericResponse();
//        responseObject.setErrorMessage(errorMessage);
		responseObject.setResult(AppConstants.EMPTY_ARRAY);
		responseObject.setStatus(AppConstants.STATUS_NOT_OK);
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
	public RestResponse<GenericResponse> prepareSuccessResponseObject(Object resultData) {
		GenericResponse responseObject = new GenericResponse();
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
	public RestResponse<GenericResponse> prepareSuccessResponseWithMessage(Object resultData, String message) {
		GenericResponse responseObject = new GenericResponse();
		responseObject.setResult(getResult(resultData));
		responseObject.setStatus(message);
		return RestResponse.ResponseBuilder.create(Status.OK, responseObject).build();
	}

	public RestResponse<GenericResponse> prepareResponse(Object resultData) {
		return RestResponse.ResponseBuilder.create(Status.OK, (GenericResponse) resultData).build();
	}

	/**
	 * Common method to Success Response
	 *
	 * @param resultData
	 * @return
	 */
	public RestResponse<GenericResponse> prepareSuccessMessage(String message) {
		GenericResponse responseObject = new GenericResponse();
//		responseObject.setResult(AppConstants.EMPTY_ARRAY);
		responseObject.setStatus(AppConstants.STATUS_OK);
		responseObject.setMessage(message);
		return RestResponse.ResponseBuilder.create(Status.OK, responseObject).build();
	}

	/**
	 * Common Method for unauthorized
	 * 
	 * @author Dinesh Kumar
	 * @return
	 */
	public RestResponse<GenericResponse> prepareUnauthorizedResponse() {
		GenericResponse responseObject = new GenericResponse();
		return RestResponse.ResponseBuilder.create(Status.UNAUTHORIZED, responseObject).build();
	}

	/**
	 * Common method to prepare UnauthorizedResponse for List
	 * 
	 * @return
	 */
	public RestResponse<List<GenericResponse>> prepareUnauthorizedResponseForList() {
		List<GenericResponse> list = new ArrayList<>();
		GenericResponse responseObject = new GenericResponse();
		responseObject.setStatus(AppConstants.STATUS_NOT_OK);
		responseObject.setMessage(AppConstants.FAILED_STATUS);
		return RestResponse.ResponseBuilder.create(Status.UNAUTHORIZED, list).build();
	}

	/**
	 * Common method for failed response with only message for list
	 *
	 * @param errorMessage
	 * @return
	 */
	public RestResponse<List<GenericResponse>> prepareFailedResponseForList(String errorMessage) {

		List<GenericResponse> list = new ArrayList<>();
		GenericResponse responseObject = new GenericResponse();
		responseObject.setResult(AppConstants.EMPTY_ARRAY);
		responseObject.setStatus(AppConstants.STATUS_NOT_OK);
		responseObject.setMessage(errorMessage);
		list.add(responseObject);
		return RestResponse.ResponseBuilder.create(Status.OK, list).build();
	}

	/**
	 * Common method to Success Response
	 *
	 * @param resultData
	 * @return
	 */
	public RestResponse<List<GenericResponse>> prepareSuccessMessageForList(String message) {
		List<GenericResponse> list = new ArrayList<>();
		GenericResponse responseObject = new GenericResponse();
		responseObject.setStatus(AppConstants.STATUS_OK);
		responseObject.setMessage(message);
		list.add(responseObject);
		return RestResponse.ResponseBuilder.create(Status.OK, list).build();
	}
}
