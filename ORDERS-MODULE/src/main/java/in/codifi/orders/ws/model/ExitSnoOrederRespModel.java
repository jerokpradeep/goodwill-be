package in.codifi.orders.ws.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExitSnoOrederRespModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@JsonProperty("stat")
	private String stat;
	@JsonProperty("request_time")
	private String requestTime;
	@JsonProperty("dmsg")
	private String dmsg;
	@JsonProperty("emsg")
	private String emsg;
}
