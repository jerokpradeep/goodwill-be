package in.codifi.common.ws.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ResultsetModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@JsonProperty("FIIDate")
	private String fIIDate;
	@JsonProperty("DebtGrossPurchase")
	private String debtGrossPurchase;
	@JsonProperty("EquityGrossPurchase")
	private String equityGrossPurchase;
	@JsonProperty("DebtGrossSales")
	private String debtGrossSales;
	@JsonProperty("EquityGrossSales")
	private String equityGrossSales;
	@JsonProperty("DebtNetInvestment")
	private String debtNetInvestment;
	@JsonProperty("EquityNetInvestment")
	private String equityNetInvestment;
	@JsonProperty("DebtCumulativeNetInvestment")
	private String debtCumulativeNetInvestment;
	@JsonProperty("EquityCumulativeNetInvestment")
	private String equityCumulativeNetInvestment;

}
