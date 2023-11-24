package in.codifi.auth.model.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OtpRespModel {
	private boolean totpAvailable;
	private String token;
	private String kcRole;
}
