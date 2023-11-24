package in.codifi.ws.model.kb.login;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class QuickAuthLoginRespModel {

	@JsonProperty("request_time")
	private String requestTime;

	@JsonProperty("stat")
	private String stat;

	@JsonProperty("susertoken")
	private String sUserToken;

	@JsonProperty("lastaccesstime")
	private String lastAccesstime;

	@JsonProperty("emsg")
	private String emsg;

}
