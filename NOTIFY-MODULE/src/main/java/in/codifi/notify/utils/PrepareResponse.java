package in.codifi.notify.utils;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Response.Status;

import org.jboss.resteasy.reactive.RestResponse;
import org.json.simple.JSONArray;
import org.springframework.stereotype.Component;

import in.codifi.notify.model.response.GenericResponse;

@Component
public class PrepareResponse {

	/**
	 * Common method for failed response with only message
	 *
	 * @param errorMessage
	 * @return
	 */
	public RestResponse<GenericResponse> prepareFailedResponse(String errorMessage) {

		GenericResponse responseObject = new GenericResponse();
		responseObject.setResult(AppConstants.EMPTY_ARRAY);
		responseObject.setStatus(AppConstants.STATUS_NOT_OK);
		responseObject.setMessage(errorMessage);
		return RestResponse.ResponseBuilder.create(Status.OK, responseObject).build();
	}

	/**
	 * Common method for failed response with result object
	 *
	 * @return
	 */
	public RestResponse<GenericResponse> prepareFailedResponseObject(String errorMessage) {

		GenericResponse responseObject = new GenericResponse();
		responseObject.setResult(AppConstants.INVALID_PARAMETERS);
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
		responseObject.setStatus(AppConstants.STATUS_OK);
		responseObject.setMessage(message);
		responseObject.setResult(AppConstants.EMPTY_ARRAY);
		return RestResponse.ResponseBuilder.create(Status.OK, responseObject).build();
	}

	/**
	 * Common method to prepare UnauthorizedResponse
	 * 
	 * @return
	 */
	public RestResponse<GenericResponse> prepareUnauthorizedResponse() {
		GenericResponse responseObject = new GenericResponse();
		responseObject.setStatus(AppConstants.STATUS_NOT_OK);
		responseObject.setMessage(AppConstants.FAILED_STATUS);
		return RestResponse.ResponseBuilder.create(Status.UNAUTHORIZED, responseObject).build();
	}

}
