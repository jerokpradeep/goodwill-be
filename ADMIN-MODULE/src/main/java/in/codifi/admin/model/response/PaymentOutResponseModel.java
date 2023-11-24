package in.codifi.admin.model.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentOutResponseModel {

	private String user_id;
	private String bank_acc_no;
	private String ifsc_code;
	private String exch_seg;
	private String amount;
	private String request;
	private String response;
	private String created_on;
	private String payOutReason;
	private String count;

}
