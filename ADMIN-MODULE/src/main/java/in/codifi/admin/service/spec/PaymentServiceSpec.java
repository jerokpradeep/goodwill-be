package in.codifi.admin.service.spec;

import javax.ws.rs.core.Response;

import org.jboss.resteasy.reactive.RestResponse;

import in.codifi.admin.model.response.GenericResponse;
import in.codifi.admin.req.model.PayoutReq;

public interface PaymentServiceSpec {
	
	/**
	 * Method to get payment Detail
	 * 
	 * @author Gowthaman M
	 * @return
	 */
	RestResponse<GenericResponse> getPayoutDetails(PayoutReq payoutReq);

	/**
	 * Method to download payout details
	 * 
	 * @author Gowthaman M
	 * @return
	 */
	Response downloadPayoutDetails(PayoutReq payoutReq);

}
