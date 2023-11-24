package in.codifi.auth.model.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UnblockReq {

	private String userId;
	private String pan;
	private String source;
	private String otp;
}
