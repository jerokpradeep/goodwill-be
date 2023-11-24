package in.codifi.funds.model.response;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BankDetails implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String ifscCode;
	private String bankName;
	private String bankActNo;
	private String bankCode;
	private String clientName;

}
