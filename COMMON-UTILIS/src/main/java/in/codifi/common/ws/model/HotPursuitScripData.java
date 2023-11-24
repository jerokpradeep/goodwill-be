package in.codifi.common.ws.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class HotPursuitScripData {

	@JsonProperty("CompanyCode")
	private String companyCode;
	@JsonProperty("MarketSegmentId")
	private String MktSegmentId;
	@JsonProperty("ODINCode")
	private String ODINCode;
	@JsonProperty("Symbol")
	private String symbol;
	@JsonProperty("Series")
	private String series;
	
}
