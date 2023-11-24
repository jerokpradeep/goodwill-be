package in.codifi.client.service.spec;

import org.jboss.resteasy.reactive.RestResponse;

import in.codifi.client.model.request.ClientDetailsReqModel;
import in.codifi.client.model.response.GenericResponse;

public interface IDataFeedService {

	/**
	 * Method to get stocks for client
	 * 
	 * @author gowthaman
	 * @return
	 */
	RestResponse<GenericResponse> getStocksForUser(ClientDetailsReqModel model);

}
