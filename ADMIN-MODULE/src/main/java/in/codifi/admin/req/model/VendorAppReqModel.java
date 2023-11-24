package in.codifi.admin.req.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VendorAppReqModel {
	
	private String client_id;
	private int authorization_status;
	private String api_key;
}
