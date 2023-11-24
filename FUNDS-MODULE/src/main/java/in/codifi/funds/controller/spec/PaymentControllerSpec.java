package in.codifi.funds.controller.spec;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jboss.resteasy.reactive.RestResponse;

import in.codifi.funds.model.request.PaymentReqModel;
import in.codifi.funds.model.request.UPIReqModel;
import in.codifi.funds.model.request.VerifyPaymentReqModel;
import in.codifi.funds.model.response.GenericResponse;

public interface PaymentControllerSpec {

	/**
	 * 
	 * Method to create new payment details
	 * 
	 * @author SOWMIYA
	 *
	 * @return
	 */
	@Path("/create")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	RestResponse<GenericResponse> createNewPayment(PaymentReqModel limitOrderEntity);

	/**
	 * 
	 * Method to get upi id
	 * 
	 * @author SOWMIYA
	 *
	 * @return
	 */
	@Path("/upi")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	RestResponse<GenericResponse> getUPIId();

	/**
	 * 
	 * Method to set upi id
	 * 
	 * @author SOWMIYA
	 *
	 * @return
	 */
	@Path("/upi/update")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	RestResponse<GenericResponse> setUPIId(UPIReqModel model);

	/**
	 * 
	 * Method to get payment details
	 * 
	 * @author SOWMIYA
	 *
	 * @return
	 */
	@Path("/details")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	RestResponse<GenericResponse> getPaymentDetails();

	/*
	 * Method to verify payments
	 * 
	 * @author SOWMIYA
	 * 
	 * @return
	 */
	@Path("/verify")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	RestResponse<GenericResponse> verifyPayments(VerifyPaymentReqModel model);

	/*
	 * method to payout
	 * 
	 * @author SOWMIYA
	 * 
	 * @return
	 */
	@Path("/payout")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	RestResponse<GenericResponse> payOut(PaymentReqModel model);

	/*
	 * method to get the the payout check balance
	 * 
	 * @author SOWMIYA
	 * 
	 * @return
	 */
	@Path("/payout/checkbalance")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	RestResponse<GenericResponse> payOutCheckBalance();

	/*
	 * method to get the the payout check balance
	 * 
	 * @author SOWMIYA
	 * 
	 * @return
	 */
	@Path("/payout/details")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	RestResponse<GenericResponse> getPayOutDetails();

	/*
	 * method to cancelPayOut
	 * 
	 * @author SOWMIYA
	 * 
	 * @return
	 */
	@Path("/payout/cancel")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	RestResponse<GenericResponse> cancelPayOut(PaymentReqModel model);

	/**
	 * method to get hs token for payout
	 * 
	 * @author SOWMIYA
	 */

	@Path("/get/hstoken")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	RestResponse<GenericResponse> getHSToken();

}
