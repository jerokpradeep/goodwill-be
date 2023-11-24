package in.codifi.common.ws.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ScripNewsResultset {
	
	@JsonProperty("SNo")
	private String sNo;
	@JsonProperty("Date")
	private String date;
	@JsonProperty("Time")
	private String time;
	@JsonProperty("CompanyCode")
	private String companyCode;
	@JsonProperty("ScripData_NSE")
	private ScripDataNSEModel scripDataNSE;
	@JsonProperty("ScripData_BSE")
	private ScripDataBSEModel scripDataBSE;
	@JsonProperty("ISIN")
	private String isin;
	@JsonProperty("Heading")
	private String heading;
	@JsonProperty("Caption")
	private String caption;
	@JsonProperty("ArtText")
	private String artText;


}
