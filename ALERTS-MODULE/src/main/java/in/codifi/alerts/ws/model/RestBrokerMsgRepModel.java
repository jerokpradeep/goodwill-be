package in.codifi.alerts.ws.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class RestBrokerMsgRepModel implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@JsonProperty("stat")
	private String stat;

	@JsonProperty("norentm")
	private String time;

	@JsonProperty("msgtyp")
	private String msgType;

	@JsonProperty("dmsg")
	private String msg;

	@JsonProperty("note")
	private String note;

	@JsonProperty("msgsubtyp")
	private String msgSubType;
	
	@JsonProperty("emsg")
	private String emsg;
}
