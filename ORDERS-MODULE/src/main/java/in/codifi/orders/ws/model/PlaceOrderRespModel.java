package in.codifi.orders.ws.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class PlaceOrderRespModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@JsonProperty("request_time")
	private String requestTime;
	@JsonProperty("stat")
	private String stat;
	@JsonProperty("norenordno")
	private String norenordno;
	@JsonProperty("emsg")
	private String emsg;

}