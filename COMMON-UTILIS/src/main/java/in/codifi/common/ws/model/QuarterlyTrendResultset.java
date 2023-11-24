package in.codifi.common.ws.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class QuarterlyTrendResultset implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@JsonProperty("CompanyCode")
	private String companyCode;
	@JsonProperty("yrc")
	private String yrc;
	@JsonProperty("revenu")
	private String revenu;
	@JsonProperty("profitmargin")
	private String profitMargin;

}
