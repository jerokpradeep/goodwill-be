package in.codifi.auth.model.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ForgetPassReq {

	private String userId;
	private String pan;
	private String oldPassword;
	private String password;
	private String source;
	private String otp;

}
