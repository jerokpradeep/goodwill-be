package in.codifi.alerts.ws.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PushNotifyReq {

	@JsonProperty("collapse_key")
	public String collapseKey;
	@JsonProperty("notification")
	public PushNotifyNotification notification;
	@JsonProperty("data")
	public PushNotifyData data;
	@JsonProperty("android")
	public PushNotifyAndroid android;
	@JsonProperty("to")
	public String to;
	@JsonProperty("click_action")
	public String clickAction;
}
