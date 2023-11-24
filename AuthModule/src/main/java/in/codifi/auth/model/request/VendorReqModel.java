package in.codifi.auth.model.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VendorReqModel {
	private String vendor;
	private String userId;
	private String checkSum;
	private String authCode;
}
