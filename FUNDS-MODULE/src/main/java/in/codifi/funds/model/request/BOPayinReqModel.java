package in.codifi.funds.model.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BOPayinReqModel {

	private String userId;
	private String exchangeSegment;
	private String razorpayOrderId;
	private String razorpayPaymentId;
	private double amount;
	private String paymentMethod;
	private String accountNumber;

}
