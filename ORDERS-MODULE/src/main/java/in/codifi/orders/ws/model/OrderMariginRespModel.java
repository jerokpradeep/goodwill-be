package in.codifi.orders.ws.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderMariginRespModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@JsonProperty("stat")
	private String stat;
	@JsonProperty("request_time")
	private String requestTime;
	@JsonProperty("remarks")
	private String remarks;
	@JsonProperty("cash")
	private String cash;
	@JsonProperty("marginused")
	private String marginUsed;
	@JsonProperty("ordermargin")
	private String orderMargin;
	@JsonProperty("marginusedprev")
	private String marginUsedPrev;
	@JsonProperty("emsg")
	private String emsg;

}