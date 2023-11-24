package in.codifi.auth.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AuthRespModel {

	private String accessToken;
	private String kcRole;
	private String redirectUrl;
	private String clientId;
	private boolean isAuthorized;
}
