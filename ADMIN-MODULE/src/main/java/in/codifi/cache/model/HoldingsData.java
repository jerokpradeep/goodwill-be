package in.codifi.cache.model;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HoldingsData implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String userId;
	private String holdingsType;
	private String isin;
	private int qty;
	private int collateralQty;
	private String haircut;
	private int brokerCollQty;
	private int dpQty;
	private int benQty;
	private int unpledgeQy;
	private double closePrice;
	private double actualPrice;
	private String product;
	private String poaStatus;
	private int authFlag;
	private int authQty;
	private String reqId;
	private String txnId;

}
