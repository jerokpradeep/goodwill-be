package in.codifi.brokerage.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;

import lombok.Getter;
import lombok.Setter;

@Entity(name = "TBL_BROKERAGE")
@Getter
@Setter
public class BrokerageEquityEntity extends CommonEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Column(name = "PLAN")
	private String plan;
	@Column(name = "BASE")
	private String base;
	@Column(name = "SEGMENT")
	private String segment;
	@Column(name = "INSTRUMENT_TYPE")
	private String instrumentType;
	@Column(name = "DESCRIPTION")
	private String description;
	@Column(name = "TYPE")
	private String type;
	@Column(name = "LOT")
	private String lot;
	@Column(name = "TURN_OVER")
	private String turnOver;
	@Column(name = "COMPARE_COST")
	private String compareCost;
	@Column(name = "SECURITIES_TRANSACTION_TAX_BUY")
	private String securitiesTransactionTaxBuy;
	@Column(name = "SECURITIES_TRANSACTION_TAX_SELL")
	private String securitiesTransactionTaxSell;
	@Column(name = "TRANSACTION_CHARGES_NSE_BUY")
	private String transactionChargesNseBuy;
	@Column(name = "TRANSACTION_CHARGES_NSE_SELL")
	private String transactionChargesNseSell;
	@Column(name = "SEBI_CHARGES_BUY")
	private String sebiChargesBuy;
	@Column(name = "SEBI_CHARGES_SELL")
	private String sebiChargesSell;
	@Column(name = "STAMP_CHARGES_BUY")
	private String stampChargesBuy;
	@Column(name = "STAMP_CHARGES_SELL")
	private String stampChargesSell;
	@Column(name = "clearing_member")
	private String clearing_member;
	@Column(name = "GST")
	private String gst;
	@Column(name = "IPFT")
	private String ipft;

}
