package in.codifi.alerts.ws.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AndroidNotification {

	@JsonProperty("channel_id")
	public String channelId;
}
