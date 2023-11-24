package in.codifi.admin.entity;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class HoldingsJSONEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@JsonProperty("buy_value")
	private double buyValue;
	@JsonProperty("buy_avg")
	private double buyAvg;
	@JsonProperty("isin")
	private String isin;
	@JsonProperty("holdings_type")
	private String holdingsType;
	@JsonProperty("auth_flag")
	private int authFlag;
	@JsonProperty("poa_status")
	private String poaStatus;
	@JsonProperty("bse_code")
	private String bseCode;
	@JsonProperty("nse_code")
	private String nseCode;
	@JsonProperty("req_id")
	private String reqId;
	@JsonProperty("txn_id")
	private String txnId;
	@JsonProperty("symbol")
	private String symbol;
	@JsonProperty("qty")
	private String qty;
	@JsonProperty("authQty")
	private int authQty;

}
