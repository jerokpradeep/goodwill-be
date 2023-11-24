package in.codifi.api.controller.spec;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jboss.resteasy.reactive.RestResponse;

import in.codifi.api.model.ReqModel;
import in.codifi.api.model.ResponseModel;

public interface ITickerTapeController {

	/**
	 * 
	 * Method to get stocks for client
	 * 
	 * @author Dinesh Kumar
	 *
	 * @param model
	 * @return
	 */
	@Path("/get")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	RestResponse<ResponseModel> getTicketTapeScrips(ReqModel model);

}
