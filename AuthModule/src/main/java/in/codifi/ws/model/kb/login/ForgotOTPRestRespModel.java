package in.codifi.ws.model.kb.login;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ForgotOTPRestRespModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@JsonProperty("ReqStatus")
	private String ReqStatus;
	@JsonProperty("stat")
	private String stat;
	@JsonProperty("emsg")
	private String emsg;
	@JsonProperty("uid")
	private String uid;

}
