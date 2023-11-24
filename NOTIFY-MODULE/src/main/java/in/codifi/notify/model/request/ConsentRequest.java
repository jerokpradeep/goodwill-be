package in.codifi.notify.model.request;

import java.sql.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConsentRequest {

	private int consent_yes;
	private String userId;
	private String source;
	private Date date;
	private String deviceId;
	private String ipAddress;
	private int messageId;
	private String messageTitle;
	

}
