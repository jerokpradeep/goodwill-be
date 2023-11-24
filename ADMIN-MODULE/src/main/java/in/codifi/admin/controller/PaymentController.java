package in.codifi.admin.controller;

import javax.inject.Inject;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import org.jboss.resteasy.reactive.RestResponse;

import in.codifi.admin.controller.spec.PaymentControllerSpec;
import in.codifi.admin.model.response.GenericResponse;
import in.codifi.admin.req.model.PayoutReq;
import in.codifi.admin.service.spec.PaymentServiceSpec;

@Path("/payment")
public class PaymentController implements PaymentControllerSpec{
	
	@Inject
	PaymentServiceSpec paymentService;
	
	/**
	 * Method to get payout details
	 * 
	 * @author Gowthaman M
	 * @return
	 */
	@Override
	public RestResponse<GenericResponse> getPayoutDetails(PayoutReq payoutReq) {
		return paymentService.getPayoutDetails(payoutReq);
	}

	/**
	 * Method to download payout details
	 * 
	 * @author Gowthaman M
	 * @return
	 */
	@Override
	public Response downloadPayoutDetails(PayoutReq payoutReq) {
		return paymentService.downloadPayoutDetails(payoutReq);
	}

}
