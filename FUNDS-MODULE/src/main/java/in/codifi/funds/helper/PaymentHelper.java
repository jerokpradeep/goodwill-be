package in.codifi.funds.helper;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.json.JSONObject;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.Utils;

import in.codifi.cache.model.ClinetInfoModel;
import in.codifi.funds.config.PaymentsProperties;
import in.codifi.funds.entity.primary.PaymentLogsEntity;
import in.codifi.funds.model.request.PaymentReqModel;
import in.codifi.funds.model.request.VerifyPaymentReqModel;
import in.codifi.funds.model.response.RazorpayModel;
import in.codifi.funds.repository.PaymentLogsRepository;
import in.codifi.funds.utility.AppConstants;
import in.codifi.funds.utility.CodifiUtil;
import in.codifi.funds.utility.PrepareResponse;
import io.quarkus.logging.Log;

@ApplicationScoped
public class PaymentHelper {
	@Inject
	CodifiUtil commonMethods;

	@Inject
	PaymentsProperties props;
	@Inject
	PrepareResponse prepareResponse;
	@Inject
	PaymentLogsRepository paymentRepo;

	/**
	 * 
	 * Method to create payment in razorpay
	 * 
	 * @author Sowmiya
	 *
	 * @param paymentReqModel
	 * @param info
	 * @return
	 */
	public RazorpayModel createPayment(PaymentReqModel paymentReqModel, ClinetInfoModel info) {
		RazorpayModel responseDTO = new RazorpayModel();
		try {
			if (paymentReqModel != null && info != null) {
				Order order = null;
				CodifiUtil.trustedManagement();
				System.out.println("KEY -- >  " + props.getRazorpayKey());
				System.out.println("Secret -- >  " + props.getRazorpaySecret());
				RazorpayClient razorpay = new RazorpayClient(props.getRazorpayKey(), props.getRazorpaySecret());
				JSONObject orderRequest = new JSONObject();
				orderRequest.put(AppConstants.RAZORPAY_AMOUNT, paymentReqModel.getAmount() * 100);
				orderRequest.put(AppConstants.RAZORPAY_CURRENCY, AppConstants.RAZORPAY_CURRENCY_INR);
				orderRequest.put(AppConstants.RAZORPAY_RECEIPT, paymentReqModel.getReceipt());
				if (paymentReqModel.getPayMethod().equalsIgnoreCase(AppConstants.RAZORPAY_UPI)) {
					orderRequest.put(AppConstants.RAZORPAY_METHOD, AppConstants.RAZORPAY_UPI);
				} else {
					orderRequest.put(AppConstants.RAZORPAY_METHOD, AppConstants.RAZORPAY_NET_BANKING);
				}
				JSONObject bankDetails = new JSONObject();
				bankDetails.put(AppConstants.RAZORPAY_ACCOUNT_NUMBER, paymentReqModel.getBankActNo());
				bankDetails.put(AppConstants.RAZORPAY_ACCOUNT_NAME, paymentReqModel.getClientName());
				bankDetails.put(AppConstants.RAZORPAY_ACCOUNT_IFSC, paymentReqModel.getIfscCode());
				orderRequest.put(AppConstants.RAZORPAY_BANK_ACCOUNT, bankDetails);
				JSONObject notes = new JSONObject();
				notes.put(AppConstants.RAZORPAY_CLIENT_CODE, info.getUserId());
				orderRequest.put(AppConstants.RAZORPAY_NOTES, notes);
				Log.info(orderRequest);
				order = razorpay.Orders.create(orderRequest);
				responseDTO.setStat(AppConstants.SUCCESS);
				responseDTO.setOrder(order);
				Log.info(order);
				PaymentLogsEntity paymentEntity = preparePaymentLogEntity("CreateOrderId", "", info.getUserId(),
						orderRequest.toString(), order.toString());
				paymentEntity = paymentRepo.save(paymentEntity);
			} else {
				responseDTO.setStat(0);
				responseDTO.setMessage(AppConstants.EMPTY_PARAMETER);
			}

		} catch (Exception e) {
			e.printStackTrace();
			Log.error(e.getMessage());
		}

		return responseDTO;
	}

	/**
	 * 
	 * Method to Insert razorpay payment response
	 * 
	 * @author Dinesh Kumar
	 *
	 * @param method
	 * @param orderId
	 * @param userId
	 * @param request
	 * @param response
	 * @return
	 */
	private PaymentLogsEntity preparePaymentLogEntity(String method, String orderId, String userId, Object request,
			Object response) {
		PaymentLogsEntity paymentEntity = new PaymentLogsEntity();
		try {
			ObjectMapper mapper = new ObjectMapper();
			String req = mapper.writeValueAsString(request);
			String res = mapper.writeValueAsString(response);

			paymentEntity.setMethod(method);
			paymentEntity.setOrderId(orderId);
			paymentEntity.setRequest(req);
			paymentEntity.setResponse(res);
			paymentEntity.setUserId(userId);
			paymentEntity.setCreatedBy(userId);
		} catch (Exception e) {
			e.printStackTrace();
			Log.error(e.getMessage());
		}
		return paymentEntity;

	}

	/**
	 * 
	 * Method to verify payment
	 * 
	 * @author Dinesh Kumar
	 *
	 * @param model
	 * @param userId
	 * @return
	 */
	public boolean verifyPayment(VerifyPaymentReqModel model, String userId) {

		boolean isEqual = false;
		try {
			JSONObject orderRequest = new JSONObject();
			orderRequest.put(AppConstants.RAZORPAY_AMOUNT, model.getAmount());
			orderRequest.put(AppConstants.RAZORPAY_CURRENCY, model.getCurrency());
			orderRequest.put(AppConstants.RAZORPAY_RECEIPT, model.getReceipt());
			orderRequest.put(AppConstants.RAZORPAY_ORDERID, model.getRazorpayOrderId());
			orderRequest.put(AppConstants.RAZORPAY_PAYMENTID, model.getRazorpayPaymentId());
			orderRequest.put(AppConstants.RAZORPAY_SIGNATURE, model.getRazorpaySignature());
			isEqual = Utils.verifyPaymentSignature(orderRequest, props.getRazorpaySecret());
			PaymentLogsEntity paymentEntity = preparePaymentLogEntity("verifypayment", "", userId,
					orderRequest.toString(), isEqual);
			paymentEntity = paymentRepo.save(paymentEntity);
		} catch (Exception e) {
			Log.error(e.getMessage());
			e.printStackTrace();
		}
		return isEqual;

	}

}
