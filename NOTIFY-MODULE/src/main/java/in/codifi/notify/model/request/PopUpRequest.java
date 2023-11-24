package in.codifi.notify.model.request;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PopUpRequest {

	private List<String> message;
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
	private List<String> userId;
	private Long popupId;
	private int ssoLogin;

}
