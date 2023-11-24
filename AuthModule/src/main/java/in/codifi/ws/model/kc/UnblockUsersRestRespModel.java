package in.codifi.ws.model.kc;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UnblockUsersRestRespModel implements Serializable {

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

}
