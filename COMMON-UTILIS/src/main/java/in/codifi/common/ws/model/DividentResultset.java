package in.codifi.common.ws.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DividentResultset {

	@JsonProperty("CompanyName")
	private String companyName;
	@JsonProperty("CompanyCode")
	private String companyCode;
	@JsonProperty("Symbol")
	private String symbol;
	@JsonProperty("ISIN")
	private String isin;
	@JsonProperty("AnnouncementDate")
	private String announcementDate;
	@JsonProperty("Divamount")
	private String divamount;
	@JsonProperty("Divdate")
	private String divdate;
	@JsonProperty("Divper")
	private String divper;
	@JsonProperty("Recorddate")
	private String recorddate;
	@JsonProperty("Description")
	private String description;
	@JsonProperty("Remark")
	private String remark;
	@JsonProperty("Upcoming")
	private Boolean upcoming;

}
