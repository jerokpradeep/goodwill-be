package in.codifi.cache.model;

import java.io.Serializable;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ContractMasterModel implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String exch;
	private String segment;
	private String token;
	private String alterToken;
	private String symbol;
	private String tradingSymbol;
	private String formattedInsName;
	private String isin;
	private String groupName;
	private String insType;
	private String optionType;
	private String strikePrice;
	private Date expiry;
	private String lotSize;
	private String tickSize;
	private String freezQty;
	private String pdc;
	private String weekTag;
	private String companyName;

}
