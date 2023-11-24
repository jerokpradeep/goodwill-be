package in.codifi.admin.model.response;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PayoutResponse implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String DEFAULT = "0";
	private String date;
	private String ldCode;
	private String amount;
	private String clientBankcode;
	private String paymentMode;
	private String clientBankAccountNumber;
	private String clientBankIfscCode;
	private String payment;

}
