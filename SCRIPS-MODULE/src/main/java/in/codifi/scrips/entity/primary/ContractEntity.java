package in.codifi.scrips.entity.primary;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
//@Entity(name = "TBL_GLOBAL_CONTRACT_DETAILS_MASTER")
@Entity(name = "TBL_GLOBAL_CONTRACT_MASTER_DETAILS")
public class ContractEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	@Column(name = "ID")
	private int id;

	@Column(name = "EXCH")
	private String exch;

	@Column(name = "EXCHANGE_SEGMENT")
	private String segment;

	@Column(name = "TOKEN")
	private String token;

	@Column(name = "ISIN")
	private String isin;

	@Column(name = "SYMBOL")
	private String symbol;

	@Column(name = "TRADING_SYMBOL")
	private String tradingSymbol;

	@Column(name = "FORMATTED_INS_NAME")
	private String formattedInsName;

	@Column(name = "COMPANY_NAME")
	private String companyName;

	@Column(name = "GROUP_NAME")
	private String groupName;

	@Column(name = "INSTRUMENT_TYPE")
	private String insType;

	@Column(name = "OPTION_TYPE")
	private String optionType;

	@Column(name = "ALTER_TOKEN")
	private String alterToken;

	@Column(name = "STRIKE_PRICE")
	private String strikePrice;

	@Temporal(TemporalType.DATE)
	@Column(name = "EXPIRY_DATE")
	private Date expiryDate;

	@Column(name = "LOT_SIZE")
	private String lotSize;

	@Column(name = "TICK_SIZE")
	private String tickSize;

	@Column(name = "PDC")
	private String pdc;

	@Column(name = "FREEZE_QTY")
	private String freezeQty;

	@Column(name = "WEEK_TAG")
	private String weekTag;

	@Column(name = "SORT_ORDER_1")
	private int sortOrder1;

	@Column(name = "SORT_ORDER_2")
	private int sortOrder2;

	@Column(name = "SORT_ORDER_3")
	private int sortOrder3;

}
