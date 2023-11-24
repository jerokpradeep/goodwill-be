package in.codifi.alerts.ws.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PushNotifyNotification {

	@JsonProperty("title_color")
	public String titleColor;
	@JsonProperty("title")
	public String title;
	@JsonProperty("body")
	public String body;
	@JsonProperty("channel_id")
	public String channelId;
	@JsonProperty("android_channel_id")
	public String androidChannelId;
}
