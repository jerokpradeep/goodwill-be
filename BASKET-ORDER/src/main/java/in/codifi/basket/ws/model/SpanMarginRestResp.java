package in.codifi.basket.ws.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class SpanMarginRestResp implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@JsonProperty("stat")
	private String stat;
	@JsonProperty("emsg")
	private String emsg;
	@JsonProperty("span")
	private String span;
	@JsonProperty("expo")
	private String expo;
	@JsonProperty("span_trade")
	private String span_trade;
	@JsonProperty("expo_trade")
	private String expo_trade;

}
