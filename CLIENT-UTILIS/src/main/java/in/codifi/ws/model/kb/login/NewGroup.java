package in.codifi.ws.model.kb.login;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class NewGroup implements Serializable {

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
	@JsonProperty("instname")
	private String instname;
	@JsonProperty("pp")
	private String pp;
	@JsonProperty("ls")
	private String ls;
	@JsonProperty("ti")
	private String ti;
	@JsonProperty("optt")
	private String optt;
	@JsonProperty("weekly")
	private String weekly;
	@JsonProperty("nontrd")
	private String nontrd;

}
