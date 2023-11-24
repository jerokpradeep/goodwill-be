package in.codifi.admin.model.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VerifyClientResp {

	private String isExist;
	private String mobileNo;
	private String emailID;
	private String name; 
	private String userId;
	
}
