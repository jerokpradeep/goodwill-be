package in.codifi.common.ws.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ScripDataBSEModel {

	@JsonProperty("CompanyCode")
	private Integer companyCode;
	@JsonProperty("MarketSegmentId")
	private Integer marketSegmentId;
	@JsonProperty("ODINCode")
	private Integer oDINCode;
	@JsonProperty("Symbol")
	private String symbol;
	@JsonProperty("Series")
	private String series;

}
