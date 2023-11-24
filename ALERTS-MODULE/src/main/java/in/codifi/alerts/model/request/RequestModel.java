package in.codifi.alerts.model.request;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestModel {

	private long id;
	private String alertName;
	private String alertType;
	private String exch;
	private String operator;
	private String scripName;
	private String token;
	private double value;
	private String exchSeg;
	private Date expiry;
	private String triggeredTime;
	private String triggerStatus;

}
