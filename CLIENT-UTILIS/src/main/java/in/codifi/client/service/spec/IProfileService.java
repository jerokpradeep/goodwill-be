package in.codifi.client.service.spec;

import org.jboss.resteasy.reactive.RestResponse;

import in.codifi.cache.model.ClinetInfoModel;
import in.codifi.client.model.request.ClientDetailsReqModel;
import in.codifi.client.model.response.GenericResponse;

public interface IProfileService {

	/**
	 * method to get client details
	 * 
	 * @author GOWTHAMAN M
	 * @return
	 */
	RestResponse<GenericResponse> getClientDetails(ClinetInfoModel info);

	/**
	 * Method to invalidate web socket session
	 * 
	 * @author dinesh
	 * @param model
	 * @return
	 */
	RestResponse<GenericResponse> invalidateWsSession(ClientDetailsReqModel reqModel, ClinetInfoModel info);

	/**
	 * Method to create web socket session
	 * 
	 * @param model
	 * @return
	 */
	RestResponse<GenericResponse> createWsSession(ClientDetailsReqModel reqModel, ClinetInfoModel info);

}
