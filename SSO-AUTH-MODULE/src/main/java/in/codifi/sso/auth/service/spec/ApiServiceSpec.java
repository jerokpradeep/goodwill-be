package in.codifi.sso.auth.service.spec;

import org.jboss.resteasy.reactive.RestResponse;

import in.codifi.sso.auth.model.request.ApiKeyReqModel;
import in.codifi.sso.auth.model.response.GenericResponse;

public interface ApiServiceSpec {

	/**
	 * Method to get API Key
	 * 
	 * @author Dinesh Kumar
	 * @return
	 */
	RestResponse<GenericResponse> getApiKey(String userId);

	RestResponse<GenericResponse> generateApiKey(ApiKeyReqModel req);

	RestResponse<GenericResponse> reGenerateApiKey(ApiKeyReqModel req);
}
