package in.codifi.alerts.ws.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PushNotifyData {

	@JsonProperty("type")
	public String type;
	@JsonProperty("message")
	public String message;
	@JsonProperty("title")
	public String title;
}
