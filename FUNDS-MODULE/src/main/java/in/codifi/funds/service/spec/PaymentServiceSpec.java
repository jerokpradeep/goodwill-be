package in.codifi.funds.service.spec;

import org.jboss.resteasy.reactive.RestResponse;

import in.codifi.cache.model.ClinetInfoModel;
import in.codifi.funds.model.request.BOPayinReqModel;
import in.codifi.funds.model.request.PaymentReqModel;
import in.codifi.funds.model.request.UPIReqModel;
import in.codifi.funds.model.request.VerifyPaymentReqModel;
import in.codifi.funds.model.response.GenericResponse;

public interface PaymentServiceSpec {
	/**
	 * method to create new payment details
	 * 
	 * @author SOWMIYA
	 * @param limitOrderEntity
	 * @param info
	 * @return
	 */

	RestResponse<GenericResponse> createNewPayment(PaymentReqModel limitOrderEntity, ClinetInfoModel info);

	/**
	 * method to get upi id from database
	 * 
	 * @author SOWMIYA
	 * @param
	 * @param info
	 * @return
	 */
	RestResponse<GenericResponse> getUPIId(ClinetInfoModel info);

	/**
	 * method to set upi id from database
	 * 
	 * @author SOWMIYA
	 * @param model
	 * @param info
	 * @return
	 */
	RestResponse<GenericResponse> setUPIId(ClinetInfoModel info, UPIReqModel model);

	/**
	 * method to get payment details
	 * 
	 * @author SOWMIYA
	 * @param info
	 * @return
	 */
	RestResponse<GenericResponse> getPaymentDetails(ClinetInfoModel info);

	/**
	 * method to verify payment
	 * 
	 * @author SOWMIYA
	 * @param info
	 * @return
	 */
	RestResponse<GenericResponse> verifyPayments(ClinetInfoModel info, VerifyPaymentReqModel model);

	/**
	 * method to get pay out details
	 * 
	 * @author SOWMIYA
	 * @param info
	 * @return
	 */
	RestResponse<GenericResponse> getPayOutDetails(ClinetInfoModel info);

	/**
	 * method to get pay out check bank details
	 * 
	 * @author SOWMIYA
	 * @param info
	 * @return
	 */
	RestResponse<GenericResponse> payOutCheckBalance(ClinetInfoModel info);

	/**
	 * method to get pay out details
	 * 
	 * @author SOWMIYA
	 * @param info
	 * @return
	 */
	RestResponse<GenericResponse> payOut(ClinetInfoModel info, PaymentReqModel model);

	/**
	 * Method to cancel pay out
	 * 
	 * @author SOWMIYA
	 * @param info
	 * @return
	 */
	RestResponse<GenericResponse> cancelPayOut(ClinetInfoModel info, PaymentReqModel model);

	/**
	 * method to get hs token
	 * 
	 * @author SowmiyaThangaraj
	 * @param info
	 * @return
	 */
	RestResponse<GenericResponse> getHSToken(ClinetInfoModel info);

	/**
	 * method to back office payin
	 * 
	 * @author SowmiyaThangaraj
	 * @param reqModel
	 * @return
	 */
	RestResponse<GenericResponse> boPayIn(BOPayinReqModel reqModel);

}
