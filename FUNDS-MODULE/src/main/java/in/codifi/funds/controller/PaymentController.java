package in.codifi.funds.controller;

import javax.inject.Inject;
import javax.ws.rs.Path;

import org.jboss.resteasy.reactive.RestResponse;

import in.codifi.cache.model.ClinetInfoModel;
import in.codifi.funds.controller.spec.PaymentControllerSpec;
import in.codifi.funds.model.request.PaymentReqModel;
import in.codifi.funds.model.request.UPIReqModel;
import in.codifi.funds.model.request.VerifyPaymentReqModel;
import in.codifi.funds.model.response.GenericResponse;
import in.codifi.funds.service.spec.PaymentServiceSpec;
import in.codifi.funds.utility.AppConstants;
import in.codifi.funds.utility.AppUtil;
import in.codifi.funds.utility.PrepareResponse;
import in.codifi.funds.utility.StringUtil;
import io.quarkus.logging.Log;

@Path("/payment")
public class PaymentController implements PaymentControllerSpec {
	@Inject
	PaymentServiceSpec paymentService;
	@Inject
	AppUtil appUtil;
	@Inject
	PrepareResponse prepareResponse;

	/**
	 * method to create new payment details
	 * 
	 * @author SOWMIYA
	 * @param limitOrderEntity
	 * @param info
	 * @return
	 */
	@Override
	public RestResponse<GenericResponse> createNewPayment(PaymentReqModel limitOrderEntity) {
		ClinetInfoModel info = appUtil.getClientInfo();
		if (info == null || StringUtil.isNullOrEmpty(info.getUserId())) {
			Log.error("Client info is null");
			return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
		} else if (StringUtil.isNullOrEmpty(info.getUcc())) {
			return prepareResponse.prepareFailedResponse(AppConstants.GUEST_USER_ERROR);
		}
		return paymentService.createNewPayment(limitOrderEntity, info);
	}

	/**
	 * 
	 * Method to get UPI ID
	 * 
	 * @author SOWMIYA
	 *
	 * @return
	 */
	public RestResponse<GenericResponse> getUPIId() {
		ClinetInfoModel info = appUtil.getClientInfo();
		if (info == null || StringUtil.isNullOrEmpty(info.getUserId())) {
			Log.error("Client info is null");
			return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
		} else if (StringUtil.isNullOrEmpty(info.getUcc())) {
			return prepareResponse.prepareFailedResponse(AppConstants.GUEST_USER_ERROR);
		}
		return paymentService.getUPIId(info);
	}

	/**
	 * 
	 * Method to set UPI ID
	 * 
	 * @author SOWMIYA
	 *
	 * @param model
	 * @return
	 */
	public RestResponse<GenericResponse> setUPIId(UPIReqModel model) {
		ClinetInfoModel info = appUtil.getClientInfo();
		if (info == null || StringUtil.isNullOrEmpty(info.getUserId())) {
			Log.error("Client info is null");
			return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
		} else if (StringUtil.isNullOrEmpty(info.getUcc())) {
			return prepareResponse.prepareFailedResponse(AppConstants.GUEST_USER_ERROR);
		}
		return paymentService.setUPIId(info, model);
	}

	/**
	 * 
	 * method to get payment details
	 * 
	 * @author SOWMIYA
	 *
	 * @return
	 */
	public RestResponse<GenericResponse> getPaymentDetails() {
		ClinetInfoModel info = appUtil.getClientInfo();
		if (info == null || StringUtil.isNullOrEmpty(info.getUserId())) {
			Log.error("Client info is null");
			return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
		} else if (StringUtil.isNullOrEmpty(info.getUcc())) {
			return prepareResponse.prepareFailedResponse(AppConstants.GUEST_USER_ERROR);
		}
		return paymentService.getPaymentDetails(info);
	}

	/**
	 * Method to verify payments
	 * 
	 * @author SOWMIYA
	 * @param info
	 * @return
	 */
	public RestResponse<GenericResponse> verifyPayments(VerifyPaymentReqModel model) {
		ClinetInfoModel info = appUtil.getClientInfo();
		if (info == null || StringUtil.isNullOrEmpty(info.getUserId())) {
			Log.error("Client info is null");
			return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
		} else if (StringUtil.isNullOrEmpty(info.getUcc())) {
			return prepareResponse.prepareFailedResponse(AppConstants.GUEST_USER_ERROR);
		}
		return paymentService.verifyPayments(info, model);
	}

	/**
	 * method to get pay out check bank details
	 * 
	 * @author SOWMIYA
	 * @param info
	 * @return
	 */
	@Override
	public RestResponse<GenericResponse> payOutCheckBalance() {
		ClinetInfoModel info = appUtil.getClientInfo();
		if (info == null || StringUtil.isNullOrEmpty(info.getUserId())) {
			Log.error("Client info is null");
			return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
		} else if (StringUtil.isNullOrEmpty(info.getUcc())) {
			return prepareResponse.prepareFailedResponse(AppConstants.GUEST_USER_ERROR);
		}
		return paymentService.payOutCheckBalance(info);
	}

	/**
	 * method to get pay out details
	 * 
	 * @author SOWMIYA
	 * @param info
	 * @return
	 */
	@Override
	public RestResponse<GenericResponse> getPayOutDetails() {
		ClinetInfoModel info = appUtil.getClientInfo();
		if (info == null || StringUtil.isNullOrEmpty(info.getUserId())) {
			Log.error("Client info is null");
			return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
		} else if (StringUtil.isNullOrEmpty(info.getUcc())) {
			return prepareResponse.prepareFailedResponse(AppConstants.GUEST_USER_ERROR);
		}
		return paymentService.getPayOutDetails(info);
	}

	/**
	 * method to get pay out details
	 * 
	 * @author SOWMIYA
	 * @param info
	 * @return
	 */
	@Override
	public RestResponse<GenericResponse> payOut(PaymentReqModel model) {
		ClinetInfoModel info = appUtil.getClientInfo();
		if (info == null || StringUtil.isNullOrEmpty(info.getUserId())) {
			Log.error("Client info is null");
			return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
		} else if (StringUtil.isNullOrEmpty(info.getUcc())) {
			return prepareResponse.prepareFailedResponse(AppConstants.GUEST_USER_ERROR);
		}
		return paymentService.payOut(info, model);
	}

	/**
	 * method to cancel pay out
	 * 
	 * @author SOWMIYA
	 * @param info
	 * @return
	 */
	public RestResponse<GenericResponse> cancelPayOut(PaymentReqModel model) {
		ClinetInfoModel info = appUtil.getClientInfo();
		if (info == null || StringUtil.isNullOrEmpty(info.getUserId())) {
			Log.error("Client info is null");
			return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
		} else if (StringUtil.isNullOrEmpty(info.getUcc())) {
			return prepareResponse.prepareFailedResponse(AppConstants.GUEST_USER_ERROR);
		}
		return paymentService.cancelPayOut(info, model);
	}

	/**
	 * method to get hs token
	 * 
	 * @author SOWMIYA
	 * @return
	 */
	public RestResponse<GenericResponse> getHSToken() {

		ClinetInfoModel info = appUtil.getClientInfo();
		if (info == null || StringUtil.isNullOrEmpty(info.getUserId())) {
			Log.error("Client info is null");
			return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
		} else if (StringUtil.isNullOrEmpty(info.getUcc())) {
			return prepareResponse.prepareFailedResponse(AppConstants.GUEST_USER_ERROR);
		}
		return paymentService.getHSToken(info);
	}

}
