package in.codifi.funds.ws.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class BankDetailsRestResp implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@JsonProperty("UPI")
	public Boolean upi;
	@JsonProperty("SWIFT")
	public Object swift;
	@JsonProperty("ISO3166")
	public String iso3166;
	@JsonProperty("STATE")
	public String state;
	@JsonProperty("CITY")
	public String city;
	@JsonProperty("ADDRESS")
	public String address;
	@JsonProperty("CENTRE")
	public String centre;
	@JsonProperty("CONTACT")
	public String contact;
	@JsonProperty("RTGS")
	public Boolean rtgs;
	@JsonProperty("NEFT")
	public Boolean neft;
	@JsonProperty("DISTRICT")
	public String district;
	@JsonProperty("MICR")
	public String micr;
	@JsonProperty("BRANCH")
	public String branch;
	@JsonProperty("IMPS")
	public Boolean imps;
	@JsonProperty("BANK")
	public String bank;
	@JsonProperty("BANKCODE")
	public String bankcode;
	@JsonProperty("IFSC")
	public String ifsc;
}
