package in.codifi.funds.model.response;

import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetPaymentResposeModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String phone;
	private String email;
	private String userName;
	private List<String> segEnable;
	private String upiId;
	private List<BankDetails> bankDetails;
	private List<String> payoutReasons;

}
