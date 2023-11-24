package in.codifi.ws.model.kb.login;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LogoutReqModel {

	@JsonProperty("uid")
	private String uId;
}
