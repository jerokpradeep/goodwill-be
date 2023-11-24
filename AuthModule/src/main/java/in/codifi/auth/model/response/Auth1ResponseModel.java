package in.codifi.auth.model.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Auth1ResponseModel {
	private String stat;
	private String clientId;
	private String userSession;
}
