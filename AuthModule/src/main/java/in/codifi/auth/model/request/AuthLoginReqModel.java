package in.codifi.auth.model.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthLoginReqModel {
	private String userId;
	private String password;
	private String source;
}
