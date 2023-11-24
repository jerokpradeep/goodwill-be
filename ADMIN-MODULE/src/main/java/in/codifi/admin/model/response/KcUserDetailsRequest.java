package in.codifi.admin.model.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KcUserDetailsRequest {
	
	private String userId;
	private String pan;
	private String mobile;
	private String email;
	private String password;
	private String min;
	private String max;

}
