package in.codifi.funds.ws.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GetHSTokenRestRespModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@JsonProperty("request_time")
	private String requestTime;
	@JsonProperty("stat")
	private String stat;
	@JsonProperty("hstk")
	private String hstk;
	@JsonProperty("emsg")
	private String emsg;
	@JsonProperty("uid")
	private String uid;
	@JsonProperty("actid")
	private String actid;
	@JsonProperty("brkname")
	private String brkname;

}
