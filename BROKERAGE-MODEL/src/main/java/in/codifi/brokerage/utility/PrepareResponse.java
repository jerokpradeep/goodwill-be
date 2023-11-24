package in.codifi.brokerage.utility;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Response.Status;

import org.jboss.resteasy.reactive.RestResponse;
import org.json.simple.JSONArray;
import org.springframework.stereotype.Component;

import in.codifi.brokerage.model.response.GenericResponse;


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

//	/**
//	 * Common method for Response
//	 *
//	 * @param errorMessage
//	 * @return
//	 */
//	public RestResponse<CommonResponseModel> prepareFailedResponse(String errorMessage) {
//		CommonResponseModel responseObject = new CommonResponseModel();
//		if(errorMessage.equalsIgnoreCase(ApplicationConstants.INVALID_PARAMETERS)) {
//			responseObject.setStatus(ApplicationConstants.INVALID_PARAMETERS);
//		} else {
//			responseObject.setStatus(ApplicationConstants.FAILED_STATUS);
//		}
//		return RestResponse.ResponseBuilder.create(Status.OK, responseObject).build();
//	}

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

//	private List<Object> getResult(Object resultData) {
//		List<Object> result = new ArrayList<>();
//		if (resultData instanceof JSONArray || resultData instanceof List) {
//			result = (List<Object>) resultData;
//		} else {
//			result.add(resultData);
//		}
//		return result;
//	}

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

}
