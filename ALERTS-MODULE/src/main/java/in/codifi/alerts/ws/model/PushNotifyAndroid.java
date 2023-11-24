package in.codifi.alerts.ws.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PushNotifyAndroid {

	@JsonProperty("notification")
	public AndroidNotification notification;
}
