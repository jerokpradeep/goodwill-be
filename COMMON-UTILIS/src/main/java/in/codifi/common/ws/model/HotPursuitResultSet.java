package in.codifi.common.ws.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class HotPursuitResultSet  {
	
	@JsonProperty("SNo")
	private String sNo;
	@JsonDeserialize(using = HotPursuitScripDataDeserializer.class)
	@JsonProperty("ScripData_NSE")
	private HotPursuitScripData scripDataNSE;
	@JsonDeserialize(using = HotPursuitScripDataDeserializer.class)
	@JsonProperty("ScripData_BSE")
	private HotPursuitScripData scripDataBSE;
	@JsonProperty("CompanyCode")
	private String companyCode;
	@JsonProperty("IndustryCode")
	private String industryCode;
	@JsonProperty("SectionName")
	private String sectionName;
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
	@JsonProperty("Flag")
	private String flag;

}
