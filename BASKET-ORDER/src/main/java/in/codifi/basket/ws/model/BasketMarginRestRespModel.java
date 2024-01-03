package in.codifi.basket.ws.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BasketMarginRestRespModel {

	@JsonProperty("request_time")
	private String requestTime;
	@JsonProperty("stat")
	private String stat;
	@JsonProperty("emsg")
	private String emsg;
	@JsonProperty("marginused")
	private String marginUsed;
	@JsonProperty("marginusedtrade")
	private String marginUsedTrade;
	@JsonProperty("marginusedprev")
	private String marginusedprev;
	@JsonProperty("remarks")
	private String remarks;

}
