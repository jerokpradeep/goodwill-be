package in.codifi.sso.auth.model.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApiKeyReqModel {

	private String source;
	private String userId;
}
