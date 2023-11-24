package in.codifi.notify.model.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PopupResp {

	private String message[];
	private String messageTitle;
	private String userType;
	private int status;
	private int okExist;
	private String okTitle;
	private int cancelExist;
	private String cancelTitle;
	private int sourceExist;
	private String sourceMsg;
	private String sourceLink;
	private String destination;
	private String displayType;
	private int ssoLogin;

}
