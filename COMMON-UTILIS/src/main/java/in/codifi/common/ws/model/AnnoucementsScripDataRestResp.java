package in.codifi.common.ws.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class AnnoucementsScripDataRestResp implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@JsonProperty("CompanyCode")
	private int companyCode;
	@JsonProperty("MarketSegmentId")
	private int mktSegmentId;
	@JsonProperty("ODINCode")
	private int ODINCode;
	@JsonProperty("Symbol")
	private String symbol;
	@JsonProperty("Series")
	private String series;
}
