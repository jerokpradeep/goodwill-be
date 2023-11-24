package in.codifi.client.ws.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Bankdetail implements Serializable {

	private static final long serialVersionUID = 1L;
	@JsonProperty("bankn")
	public String bankn;
	@JsonProperty("acctnum")
	public String acctnum;
	@JsonProperty("ifsc_code")
	public String ifsc_code;

}
