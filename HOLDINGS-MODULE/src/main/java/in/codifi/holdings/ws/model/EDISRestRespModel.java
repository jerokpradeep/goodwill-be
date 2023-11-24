package in.codifi.holdings.ws.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class EDISRestRespModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@JsonProperty("request_time")
	private String requestTime;
	@JsonProperty("stat")
	private String stat;
	@JsonProperty("reqid")
	private String reqid;
	@JsonProperty("DPId")
	private String dPId;
	@JsonProperty("encdata")
	private String encdata;
	@JsonProperty("emsg")
	private String emsg;

}
