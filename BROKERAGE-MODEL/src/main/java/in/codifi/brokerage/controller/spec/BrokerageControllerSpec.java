package in.codifi.brokerage.controller.spec;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jboss.resteasy.reactive.RestResponse;

import in.codifi.brokerage.model.request.BrokerageReqModel;
import in.codifi.brokerage.model.response.GenericResponse;

public interface BrokerageControllerSpec {
	/**
	 * 
	 * Method to brokerage calculation
	 *
	 * @author SOWMIYA
	 *
	 * @param file
	 * @return
	 */
	@Path("/calculation")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public RestResponse<GenericResponse> brokerageCalculation(BrokerageReqModel brokerageReqModel);

}
