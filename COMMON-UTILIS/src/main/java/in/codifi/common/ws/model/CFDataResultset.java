package in.codifi.common.ws.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class CFDataResultset implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@JsonProperty("Year")
	private String year;
	@JsonProperty("EPS")
	private String eps;
	@JsonProperty("PE")
	private String pe;
	@JsonProperty("PEG")
	private String peg;
	@JsonProperty("DividendYield")
	private String dividendYield;
	@JsonProperty("CurrentRatio")
	private String currentRatio;
	@JsonProperty("DebtEquityRatio")
	private String debtEquityRatio;
	@JsonProperty("EbitdaGrowth")
	private String ebitdaGrowth;
	@JsonProperty("PatGrowth")
	private String patGrowth;
	@JsonProperty("NetSalesGrowth")
	private String netSalesGrowth;
	@JsonProperty("QuickRatio")
	private String quickRatio;
}
