package in.codifi.common.ws.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class PutCallRatioResultSet implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@JsonProperty("Symbol")
	private String symbol;
	@JsonProperty("ExpiryDate")
	private String expiryDate;
	@JsonProperty("Date")
	private String date;
	@JsonProperty("PutOI")
	private String putOI;
	@JsonProperty("CallOI")
	private String callOI;
	@JsonProperty("TotalOI")
	private String totalOI;
	@JsonProperty("PCRatioOI")
	private String PCRatioOI;
	@JsonProperty("PutQty")
	private String putQty;
	@JsonProperty("CallQty")
	private String callQty;
	@JsonProperty("TotalQty")
	private String totalQty;
	@JsonProperty("PCRatioQty")
	private String PCRatioQty;

}
