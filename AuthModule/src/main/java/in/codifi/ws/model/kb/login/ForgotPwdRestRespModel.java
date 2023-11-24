package in.codifi.ws.model.kb.login;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ForgotPwdRestRespModel {

	@JsonProperty("request_time")
	private String request_time;
	@JsonProperty("stat")
	private String stat;
	@JsonProperty("emsg")
	private String emsg;

}
