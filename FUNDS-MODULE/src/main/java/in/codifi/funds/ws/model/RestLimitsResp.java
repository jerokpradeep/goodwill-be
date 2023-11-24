package in.codifi.funds.ws.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class RestLimitsResp implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@JsonProperty("request_time")
	private String requestTime;
	@JsonProperty("stat")
	private String stat;
	@JsonProperty("prfname")
	private String prfname;
	@JsonProperty("cash")
	private String cash;
	@JsonProperty("payin")
	private String payin;
	@JsonProperty("payout")
	private String payout;
	@JsonProperty("brkcollamt")
	private String brkcollamt;
	@JsonProperty("unclearedcash")
	private String unclearedcash;
	@JsonProperty("aux_daycash")
	private String auxDaycash;
	@JsonProperty("aux_brkcollamt")
	private String auxBrkcollamt;
	@JsonProperty("aux_unclearedcash")
	private String auxUnclearedcash;
	@JsonProperty("daycash")
	private String daycash;
	@JsonProperty("turnoverlmt")
	private String turnoverlmt;
	@JsonProperty("pendordvallmt")
	private String pendordvallmt;
	@JsonProperty("collateral")
	private String collateral;
	@JsonProperty("blk_amt")
	private String blkAmt;
	@JsonProperty("emsg")
	private String emsg;
	@JsonProperty("marginused")
	private String marginUsed;
	@JsonProperty("csc")
	private String cacSellCredits;
	@JsonProperty("brokerage")
	private String brokerage;
	@JsonProperty("span")
	private String span;
	@JsonProperty("expo")
	private String expo;
	@JsonProperty("premium")
	private String premium;
	@JsonProperty("turnover")
	private String turnover;

}
