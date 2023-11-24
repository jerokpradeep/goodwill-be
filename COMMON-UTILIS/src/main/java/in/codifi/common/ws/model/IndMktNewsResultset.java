package in.codifi.common.ws.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class IndMktNewsResultset {
	
	@JsonProperty("SNo")
	private String SNo;
	@JsonProperty("SectionName")
	private String secName;
	@JsonProperty("Date")
	private String date;
	@JsonProperty("Time")
	private String time;
	@JsonProperty("Heading")
	private String heading;
	@JsonProperty("Caption")
	private String caption;
	@JsonProperty("ArtText")
	private String artText;
	@JsonProperty("CompanyCode")
	private String companyCode;
	@JsonProperty("IndustryCode")
	private String industryCode;
	@JsonProperty("Flag")
	private String flag;

}
