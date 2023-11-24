package in.codifi.admin.controller.spec;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.jboss.resteasy.reactive.RestResponse;

import in.codifi.admin.model.response.GenericResponse;
import in.codifi.admin.req.model.PayoutReq;

public interface PaymentControllerSpec {

	/**
	 * Method to get payout details
	 * 
	 * @author Gowthaman M
	 * @return
	 */
	@Path("/getPayoutDetails")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	RestResponse<GenericResponse> getPayoutDetails(PayoutReq payoutReq);
	
	/**
	 * Method to download payout details
	 * 
	 * @author Gowthaman M
	 * @return
	 */
	@Path("/downloadPayoutDetails")
	@POST
	@Produces(MediaType.TEXT_PLAIN)
	Response downloadPayoutDetails(PayoutReq payoutReq);

}
