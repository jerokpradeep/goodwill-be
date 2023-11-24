package in.codifi.ws.model.kc;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangePwdRestReqModel {

	private String oldpwd;
	private String pwd;
	private String uid;

}
