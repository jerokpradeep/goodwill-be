package in.codifi.holdings.utility;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.core.Response.Status;

import org.jboss.resteasy.reactive.RestResponse;
import org.json.JSONArray;

import in.codifi.holdings.model.response.GenericResponse;

/**
 * Class to prepare common response
 * 
 * @author Dinesh Kumar
 *
 */

@ApplicationScoped
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
	 * @param errorMessage
	 * @return
	 */
	public RestResponse<GenericResponse> prepareFailedResponseObject(Object resultData, String errorMessage) {

		GenericResponse responseObject = new GenericResponse();
		responseObject.setResult(resultData);
		responseObject.setStatus(AppConstants.STATUS_NOT_OK);
		responseObject.setMessage(errorMessage);
		return RestResponse.ResponseBuilder.create(Status.OK, responseObject).build();
	}

	public RestResponse<GenericResponse> prepareFailedResponseForRestService(String errorMessage) {

		GenericResponse responseObject = new GenericResponse();
		if (errorMessage.contains(AppConstants.REST_NO_DATA)) {
			responseObject.setStatus(AppConstants.STATUS_OK);
			responseObject.setMessage(AppConstants.REST_NO_DATA);
		} else {
			responseObject.setStatus(AppConstants.STATUS_NOT_OK);
			responseObject.setMessage(errorMessage);
		}
		responseObject.setResult(AppConstants.EMPTY_ARRAY);

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

	/**
	 * 
	 * Method to prepare unauthorized response body
	 * 
	 * @author Dinesh Kumar
	 *
	 * @return
	 */
	public GenericResponse prepareUnauthorizedResponseBody() {
		GenericResponse responseObject = new GenericResponse();
		responseObject.setStatus(AppConstants.STATUS_NOT_OK);
		responseObject.setMessage(AppConstants.UNAUTHORIZED);
		return responseObject;
	}

	/**
	 * 
	 * Method to prepare Success response body
	 * 
	 * @author Dinesh Kumar
	 *
	 * @param resultData
	 * @return
	 */
	public GenericResponse prepareSuccessResponseBody(Object resultData) {
		GenericResponse responseObject = new GenericResponse();
		responseObject.setResult(getResult(resultData));
		responseObject.setStatus(AppConstants.STATUS_OK);
		responseObject.setMessage(AppConstants.SUCCESS_STATUS);
		return responseObject;
	}

	public GenericResponse prepareFailedResponseBody(String errorMessage) {

		GenericResponse responseObject = new GenericResponse();
		if (errorMessage.contains(AppConstants.REST_NO_DATA)) {
			responseObject.setStatus(AppConstants.STATUS_OK);
			responseObject.setMessage(AppConstants.REST_NO_DATA);
		} else {
			responseObject.setStatus(AppConstants.STATUS_NOT_OK);
			responseObject.setMessage(errorMessage);
		}
		return responseObject;
	}

}
