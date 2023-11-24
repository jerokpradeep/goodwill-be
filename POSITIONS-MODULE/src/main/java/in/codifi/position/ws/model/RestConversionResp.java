package in.codifi.position.ws.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RestConversionResp implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@JsonProperty("stat")
	private String stat;
	@JsonProperty("request_time")
	private String requestTime;
	@JsonProperty("emsg")
	private String emsg;
}
