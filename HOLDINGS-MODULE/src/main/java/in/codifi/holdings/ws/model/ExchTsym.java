package in.codifi.holdings.ws.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ExchTsym implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@JsonProperty("exch")
	private String exch;
	@JsonProperty("token")
	private String token;
	@JsonProperty("tsym")
	private String tsym;
	@JsonProperty("pp")
	private String pp;
	@JsonProperty("ti")
	private String ti;
	@JsonProperty("ls")
	private String ls;
	@JsonProperty("isin")
	private String isin;

}
