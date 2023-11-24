package in.codifi.funds.model.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentReqModel {

	private String bankName;
	private String bankActNo;
	private String segment;
	private String upiId;
	private String payMethod;
	private double amount;
	private String orderId;
	private String userId;
	private String paymentStatus;
	private String receipt;
	private String ifscCode;
	private String clientName;
	private String device;
	private String payoutReason;

}
