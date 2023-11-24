package in.codifi.alerts.ws.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class RestExchMsgRespModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@JsonProperty("stat")
	private String stat;

	@JsonProperty("exch")
	private String exch;

	@JsonProperty("exch_msg")
	private String exch_msg;

	@JsonProperty("exch_tm")
	private String exch_tm;

	@JsonProperty("emsg")
	private String emsg;
}
