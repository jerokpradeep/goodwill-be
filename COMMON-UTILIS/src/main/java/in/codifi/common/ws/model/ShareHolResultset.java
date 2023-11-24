package in.codifi.common.ws.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ShareHolResultset implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@JsonProperty("CompanyCode")
	private String companyCode;
	@JsonProperty("ScripData_NSE")
	private ShareHolScripData scripDataNSE;
	@JsonProperty("ScripData_BSE")
	private ShareHolScripData scripDataBSE;
	@JsonProperty("YRC")
	private String yrc;
	@JsonProperty("FIIHolding")
	private String fiiHolding;
	@JsonProperty("MutualFundHolding")
	private String mutualFundHolding;
	@JsonProperty("PromoterHolding")
	private String promoterHolding;
	@JsonProperty("Others")
	private String others;

}
