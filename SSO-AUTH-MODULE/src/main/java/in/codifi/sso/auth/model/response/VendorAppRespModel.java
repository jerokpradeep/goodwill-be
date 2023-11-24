package in.codifi.sso.auth.model.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VendorAppRespModel {

	private long appId;
	private String appName;
	private String apiKey;
	private String apiSecret;
	private String clientId;
	private String redirectUrl;
	private String postbackUrl;
	private String description;
	private int authorizationStatus;
	private String contactName;
	private String mobieNo;
	private String email;
	private String checkSum;
	private String imageUrl;
	private String type;
}
