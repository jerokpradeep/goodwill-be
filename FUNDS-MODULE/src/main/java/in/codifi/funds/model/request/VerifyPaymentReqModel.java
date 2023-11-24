package in.codifi.funds.model.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VerifyPaymentReqModel {

	private int amount;
	private String currency;
	private String receipt;
	private String razorpaySignature;
	private String razorpayOrderId;
	private String razorpayPaymentId;
}
