package in.codifi.common.ws.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BalanceSheetRestRespModel {

	@JsonProperty("ResponseObject")
	private ResponseObjectBlnSheetRespModel responseObject;

}
