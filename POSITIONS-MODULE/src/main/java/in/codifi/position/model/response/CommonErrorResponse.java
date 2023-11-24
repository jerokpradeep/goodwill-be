package in.codifi.position.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommonErrorResponse {
	@JsonProperty("stat")
	private String stat;
	@JsonProperty("emsg")
	private String emsg;
}
