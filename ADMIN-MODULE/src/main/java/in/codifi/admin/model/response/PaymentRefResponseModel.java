package in.codifi.admin.model.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentRefResponseModel {

	private String user_id;
	private String order_id;
	private String receipt_id;
	private String bank_name;
	private String acc_num;
	private String exch_seg;
	private String payment_method;
	private String upi_id;
	private String amount;
	private String request;
	private String payment_status;
	private String bo_update;
	private String voucher_no;
	private String created_on;
	private String count;
}
