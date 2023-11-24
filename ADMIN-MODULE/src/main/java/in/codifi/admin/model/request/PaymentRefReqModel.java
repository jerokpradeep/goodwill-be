package in.codifi.admin.model.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentRefReqModel {

	private String paymentStatus;
	private String fromDate;
	private String toDate;
	private String userId;
	private String fromAmount;
	private String toAmount;
	private String paymentMethod;
	private String exchSeg;
	private int offSet;
}
