package in.codifi.common.ws.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class AnalysisRestResponseModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@JsonProperty("Dateval")
	private String dateval;
	@JsonProperty("Symbol")
	private String symbol;
	@JsonProperty("LTP")
	private Double ltp;
	@JsonProperty("PClose")
	private Double pdc;
	@JsonProperty("ClosePerChg")
	private Integer closePerChg;
	@JsonProperty("Direction")
	private String direction;
	@JsonProperty("Highlight")
	private String highlight;
	@JsonProperty("Isin")
	private String isin;
	@JsonProperty("nseToken")
	private String nseToken;
}
