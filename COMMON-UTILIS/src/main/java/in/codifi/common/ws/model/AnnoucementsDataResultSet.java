package in.codifi.common.ws.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class AnnoucementsDataResultSet implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@JsonProperty("Date")
	private String date;
	@JsonProperty("CompanyCode")
	private String companyCode;
	@JsonProperty("CompanyLongName")
	private String companyLongName;
	@JsonDeserialize(using = AnnoucementsScripDataDeserializer.class)
	@JsonProperty("ScripData_NSE")
	private AnnoucementsScripDataRestResp ScripDataNSE;
	@JsonDeserialize(using = AnnoucementsScripDataDeserializer.class)
	@JsonProperty("ScripData_BSE")
	private AnnoucementsScripDataRestResp ScripDataBSE;
	@JsonProperty("BSECode")
	private String bseCode;
	@JsonProperty("Symbol")
	private String symbol;
	@JsonProperty("Caption")
	private String caption;
	@JsonProperty("Memo")
	private String memo;
}
