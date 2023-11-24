package in.codifi.orders.ws.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

/**
 * class to TradeBook request model maper to avoid serializable error while
 * preparing request
 * 
 * @author Nesan
 *
 */
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TradeBookReqModel implements Serializable {

	private static final long serialVersionUID = 1L;

	@JsonProperty("uid")
	private String uid;
	@JsonProperty("actid")
	private String actid;

}