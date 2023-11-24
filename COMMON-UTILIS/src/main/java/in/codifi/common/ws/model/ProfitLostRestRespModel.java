package in.codifi.common.ws.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProfitLostRestRespModel {
	@JsonProperty("ResponseObject")
	private ResponseObjectRestModel responseObject;

}
