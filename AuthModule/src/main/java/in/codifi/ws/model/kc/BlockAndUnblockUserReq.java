package in.codifi.ws.model.kc;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BlockAndUnblockUserReq {

	@JsonProperty("enabled")
	private Boolean enabled;
}
