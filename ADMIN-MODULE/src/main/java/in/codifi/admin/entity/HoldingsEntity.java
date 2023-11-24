package in.codifi.admin.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(name = "TBL_HOLDINGS_DATA")
public class HoldingsEntity extends CommonEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Column(name = "USER_ID")
	private String userId;
	@Column(name = "HOLDINGS_TYPE")
	private String holdingsType;
	@Column(name = "ISIN")
	private String isin;
	@Column(name = "QTY")
	private int qty;
	@Column(name = "COLLATERAL_QTY")
	private int collateralQty = 0;
	@Column(name = "HAIRCUT")
	private String haircut;
	@Column(name = "BROKER_COLL_QTY")
	private int brokerCollQty = 0;
	@Column(name = "DP_QTY")
	private int dpQty = 0;
	@Column(name = "BEN_QTY")
	private int benQty = 0;
	@Column(name = "UNPLEDGE_QTY")
	private int unpledgeQy = 0;
	@Column(name = "CLOSE_PRICE")
	private double closePrice;
	@Column(name = "ACTUAL_PRICE")
	private double actualPrice;
	@Column(name = "PRODUCT")
	private String product;
	@Column(name = "POA_Status")
	private String poaStatus;
	@Column(name = "AUTH_FLAG")
	private int authFlag = 0;
	@Column(name = "AUTH_QTY")
	private int authQty = 0;
	@Column(name = "REQ_ID")
	private String reqId;
	@Column(name = "TXN_ID")
	private String txnId;

}
