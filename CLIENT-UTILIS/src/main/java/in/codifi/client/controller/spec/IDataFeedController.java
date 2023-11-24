package in.codifi.client.controller.spec;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jboss.resteasy.reactive.RestResponse;

import in.codifi.client.model.request.ClientDetailsReqModel;
import in.codifi.client.model.response.GenericResponse;

public interface IDataFeedController {

	/**
	 * Method to get stocks for client
	 * 
	 * @author GOWTHAMAN M
	 * @return
	 */
	@Path("/get/stock")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	RestResponse<GenericResponse> getStocksForUser(ClientDetailsReqModel model);

}
