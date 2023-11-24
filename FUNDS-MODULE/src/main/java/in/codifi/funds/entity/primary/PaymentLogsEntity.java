package in.codifi.funds.entity.primary;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;

import lombok.Getter;
import lombok.Setter;

@Entity(name = "TBL_RAZORPAY_PAYMENT_LOGS")
@Getter
@Setter
public class PaymentLogsEntity extends CommonEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Column(name = "METHOD")
	private String method;
	@Column(name = "ORDER_ID")
	private String orderId;
	@Column(name = "USER_ID")
	private String userId;
	@Column(name = "REQUEST")
	private String request;
	@Column(name = "RESPONSE")
	private String response;

}
