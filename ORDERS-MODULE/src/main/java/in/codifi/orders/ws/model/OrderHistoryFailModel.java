package in.codifi.orders.ws.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderHistoryFailModel {

	@JsonProperty("stat")
	private String stat;

	@JsonProperty("emsg")
	private String emsg;

}