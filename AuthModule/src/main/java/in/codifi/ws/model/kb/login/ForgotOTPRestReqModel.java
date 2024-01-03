package in.codifi.ws.model.kb.login;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ForgotOTPRestReqModel {

	@JsonProperty("uid")
	private String userId;
	@JsonProperty("pan")
	private String password;

}
